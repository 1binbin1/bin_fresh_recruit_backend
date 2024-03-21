package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.constant.CommonConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.*;
import com.bin.bin_fresh_recruit_backend.model.domain.*;
import com.bin.bin_fresh_recruit_backend.model.enums.SendStatus;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobComSendSearchRequest;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeSendRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobSendVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshComSendVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshSendStateVo;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolRateVo;
import com.bin.bin_fresh_recruit_backend.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.FRESH_ROLE;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.*;

/**
 * @author hongxiaobin
 * @description 针对表【t_fresh_com_send(应届生投递记录表)】的数据库操作Service实现
 * @createDate 2023-12-02 17:06:05
 */
@Service
public class FreshComSendServiceImpl extends ServiceImpl<FreshComSendMapper, FreshComSend>
        implements FreshComSendService {

    @Resource
    private FreshComSendMapper freshComSendMapper;

    @Resource
    private FreshComSendService freshComSendService;

    @Resource
    private AccountService accountService;

    @Resource
    private CompanyInfoService companyInfoService;

    @Resource
    private JobInfoService jobInfoService;

    @Resource
    private FreshResumeService freshResumeService;

    @Resource
    private JobInfoMapper jobInfoMapper;

    @Resource
    private FreshUserInfoMapper freshUserInfoMapper;

    @Resource
    private FreshResumeMapper freshResumeMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;


    /**
     * 投递简历
     *
     * @param request           登录态
     * @param resumeSendRequest 简历投递请求
     * @return 投递结果
     */
    @Override
    public FreshComSendVo sendResume(HttpServletRequest request, ResumeSendRequest resumeSendRequest) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = loginInfo.getAId();
        String comId = resumeSendRequest.getComId();
        String jobId = resumeSendRequest.getJobId();
        String resumeId = resumeSendRequest.getResumeId();
        if (StringUtils.isAnyBlank(userId, comId, jobId, resumeId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 验证三个id是否存在
        QueryWrapper<CompanyInfo> companyInfoQueryWrapper = new QueryWrapper<>();
        companyInfoQueryWrapper.eq("com_id", comId);
        CompanyInfo companyInfo = companyInfoService.getOne(companyInfoQueryWrapper);
        if (companyInfo == null) {
            throw new BusinessException(ErrorCode.NO_COMPANY_ERROR);
        }
        QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
        jobInfoQueryWrapper.eq("job_id", jobId);
        jobInfoQueryWrapper.eq("com_id", comId);
        JobInfo jobInfo = jobInfoService.getOne(jobInfoQueryWrapper);
        if (jobInfo == null) {
            throw new BusinessException(ErrorCode.NO_JOB_ERROR);
        }
        QueryWrapper<FreshResume> freshResumeQueryWrapper = new QueryWrapper<>();
        freshResumeQueryWrapper.eq("user_id", userId);
        freshResumeQueryWrapper.eq("resume_id", resumeId);
        FreshResume resumeServiceOne = freshResumeService.getOne(freshResumeQueryWrapper);
        if (resumeServiceOne == null) {
            throw new BusinessException(ErrorCode.NO_RESUME_ERROR);
        }
        QueryWrapper<FreshComSend> freshComSendQueryWrapper = new QueryWrapper<>();
        freshComSendQueryWrapper.eq("user_id", userId);
        freshComSendQueryWrapper.eq("com_id", comId);
        freshComSendQueryWrapper.eq("job_id", jobId);
        freshComSendQueryWrapper.eq("is_delete", CommonConstant.NO_DELETE);
        List<FreshComSend> freshComSends = freshComSendService.list(freshComSendQueryWrapper);
        if (freshComSends != null && freshComSends.size() > 0) {
            throw new BusinessException(ErrorCode.SEND_RESUME_ERROR);
        }
        FreshComSend freshComSend = new FreshComSend();
        freshComSend.setUserId(userId);
        freshComSend.setComId(comId);
        freshComSend.setJobId(jobId);
        freshComSend.setResumeId(resumeId);
        freshComSend.setSendTime(new Date());
        freshComSend.setSendState(SendStatus.SEND_STATUS_HAVE);
        boolean save = this.save(freshComSend);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        freshComSend = this.getById(freshComSend.getId());
        FreshComSendVo freshComSendVo = new FreshComSendVo();
        BeanUtils.copyProperties(freshComSend, freshComSendVo);
        return freshComSendVo;
    }

    /**
     * 获取就业数据
     *
     * @param request 登录态（学校就业中心）
     * @return 响应数据
     */
    @Override
    public List<SchoolRateVo> getRate(HttpServletRequest request) {
        List<SchoolRateVo> result = new ArrayList<>();
        Account schoolAccount = accountService.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (schoolAccount == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = schoolAccount.getAId();
        //总学生人数
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_add", schoolId);
        accountQueryWrapper.eq("a_role", FRESH_ROLE);
        List<Account> accountList = accountService.list(accountQueryWrapper);
        long totalNum = accountList.size();
        // ids
        List<String> freshIds = new ArrayList<>();
        for (Account account : accountList) {
            freshIds.add(account.getAId());
        }
        // 查询投递信息
        QueryWrapper<FreshComSend> freshComSendQueryWrapper = new QueryWrapper<>();
        freshComSendQueryWrapper.in("user_id", freshIds);
        List<FreshComSend> freshComSends = freshComSendService.list(freshComSendQueryWrapper);
        // 计算数据
        long haveNum = 0;
        long lookedNum = 0;
        long invitedNum = 0;
        long noPassNum = 0;
        long sendFinishNum = 0;
        long successNum = 0;
        for (FreshComSend freshComSend : freshComSends) {
            switch (freshComSend.getSendState()) {
                case 0:
                    haveNum++;
                    break;
                case 1:
                    lookedNum++;
                    break;
                case 2:
                    invitedNum++;
                    break;
                case 3:
                    noPassNum++;
                    break;
                case 4:
                    sendFinishNum++;
                    break;
                case 5:
                    successNum++;
                    break;
                default:
            }
        }
        result.add(new SchoolRateVo("就业率", ((float) (successNum / totalNum) * 100) + "%"));
        result.add(new SchoolRateVo("应届生人数", String.valueOf(totalNum)));
        result.add(new SchoolRateVo("已投递人数", String.valueOf(haveNum)));
        result.add(new SchoolRateVo("被查看人数", String.valueOf(lookedNum)));
        result.add(new SchoolRateVo("初筛不通过人数", String.valueOf(noPassNum)));
        result.add(new SchoolRateVo("被邀约面试人数", String.valueOf(invitedNum)));
        result.add(new SchoolRateVo("流程终止人数", String.valueOf(sendFinishNum)));
        result.add(new SchoolRateVo("应聘成功人数", String.valueOf(successNum)));
        return result;
    }

    /**
     * 企业获取投递列表
     *
     * @param request                 登录态
     * @param jobComSendSearchRequest 请求参数
     * @return 响应数据
     */
    @Override
    public PageVo<JobSendVo> getFreshSend(HttpServletRequest request, JobComSendSearchRequest jobComSendSearchRequest) {
        Account loginInfo = accountService.getLoginInfo(request, COM_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String comId = loginInfo.getAId();
        // 处理请求参数
        Integer sendStatus = jobComSendSearchRequest.getSendStatus();
        String jobName = jobComSendSearchRequest.getJobName();
        long current = jobComSendSearchRequest.getCurrent();
        long pageSize = jobComSendSearchRequest.getPageSize();
        ArrayList<String> jobIds = new ArrayList<>();
        if (jobName != null && !Objects.equals(jobName, "")) {
            QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
            jobInfoQueryWrapper.like("job_name", jobName);
            List<JobInfo> jobInfos = jobInfoService.list(jobInfoQueryWrapper);
            for (JobInfo jobInfo : jobInfos) {
                jobIds.add(jobInfo.getJobId());
            }
        }
        // 分页查询列表
        QueryWrapper<FreshComSend> freshComSendQueryWrapper = new QueryWrapper<>();
        freshComSendQueryWrapper.eq("t_fresh_com_send.com_id", comId);
        freshComSendQueryWrapper.orderByDesc("create_time");
        if (sendStatus != -1) {
            freshComSendQueryWrapper.eq("t_fresh_com_send.send_state", sendStatus);
        }
        if (jobName != null) {
            if (jobIds.size() != 0) {
                freshComSendQueryWrapper.in("t_fresh_com_send.job_id", jobIds);
            } else {
                freshComSendQueryWrapper.eq("t_fresh_com_send.job_id", "");
            }
        }

        Page<FreshComSend> freshComSendPage = this.page(new Page<>(current, pageSize), freshComSendQueryWrapper);
        // 获取各种id
        List<String> jobId = new ArrayList<>();
        List<String> resumeId = new ArrayList<>();
        List<String> userId = new ArrayList<>();
        for (FreshComSend record : freshComSendPage.getRecords()) {
            jobId.add(record.getJobId());
            resumeId.add(record.getResumeId());
            userId.add(record.getUserId());
        }
        // 查询附属信息
        Map<String, JobInfo> jobInfo = new HashMap<>();
        Map<String, FreshResume> freshResume = new HashMap<>();
        Map<String, FreshUserInfo> freshUserInfo = new HashMap<>();
        if (jobId.size() != 0) {
            jobInfo = jobInfoMapper.getJobInfo(jobId);
        }
        if (resumeId.size() != 0) {
            freshResume = freshResumeMapper.getResumeInfo(resumeId);
        }
        if (userId.size() != 0) {
            freshUserInfo = freshUserInfoMapper.getFreshUserInfo(userId);
        }
        // 处理响应
        PageVo<JobSendVo> result = new PageVo<>();
        List<JobSendVo> jobSendVos = new ArrayList<>();
        for (FreshComSend record : freshComSendPage.getRecords()) {
            JobSendVo jobSendVo = new JobSendVo();
            BeanUtils.copyProperties(record, jobSendVo);
            jobSendVo.setJobName(jobInfo.get(record.getJobId()).getJobName());
            jobSendVo.setUserName(freshUserInfo.get(record.getUserId()).getUserName());
            jobSendVo.setUserNameLink(freshResume.get(record.getResumeId()).getUserNameLink());
            jobSendVo.setResumeName(freshResume.get(record.getResumeId()).getResumeName());
            jobSendVos.add(jobSendVo);
        }
        result.setList(jobSendVos);
        result.setTotal(freshComSendPage.getTotal());
        result.setCurrent(freshComSendPage.getCurrent());
        result.setPageSize(freshComSendPage.getSize());
        return result;
    }

    /**
     * 获取投递进度列表
     *
     * @param request 请求参数
     * @return 响应参数
     */
    @Override
    public PageVo<FreshSendStateVo> getSendState(HttpServletRequest request, Integer current, Integer pageSize) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = loginInfo.getAId();
        // 查询
        QueryWrapper<FreshComSend> freshComSendQueryWrapper = new QueryWrapper<>();
        freshComSendQueryWrapper.eq("user_id", userId);
        freshComSendQueryWrapper.orderByDesc("create_time");
        Page<FreshComSend> page = this.page(new Page<>(current, pageSize), freshComSendQueryWrapper);
        ArrayList<FreshComSend> comSends = new ArrayList<>(page.getRecords());
        // 提取ids
        List<String> jobId = new ArrayList<>();
        List<String> comIds = new ArrayList<>();
        for (FreshComSend comSend : comSends) {
            comIds.add(comSend.getComId());
            jobId.add(comSend.getJobId());
        }
        Map<String, Account> accountHashMap = new HashMap<>();
        Map<String, CompanyInfo> companyInfoHashMap = new HashMap<>();
        Map<String, JobInfo> jobInfo = new HashMap<>();
        if (comIds.size() != 0) {
            accountHashMap = accountMapper.getAccount(comIds);
            companyInfoHashMap = companyInfoMapper.getCompanyInfo(comIds);
        }
        if (jobId.size() != 0) {
            jobInfo = jobInfoMapper.getJobInfo(jobId);
        }
        // 组装结果
        List<FreshSendStateVo> result = new ArrayList<>();
        for (FreshComSend comSend : comSends) {
            FreshSendStateVo freshSendStateVo = new FreshSendStateVo();
            BeanUtils.copyProperties(comSend, freshSendStateVo);
            freshSendStateVo.setComName(companyInfoHashMap.get(comSend.getComId()).getComName());
            freshSendStateVo.setAAvatar(accountHashMap.get(comSend.getComId()).getAAvatar());
            freshSendStateVo.setJobName(jobInfo.get(comSend.getJobId()).getJobName());
            result.add(freshSendStateVo);
        }
        PageVo<FreshSendStateVo> pageResult = new PageVo<>();
        pageResult.setList(result);
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent(page.getCurrent());
        return pageResult;
    }
}




