package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.constant.CommonConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.FreshComSendMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.*;
import com.bin.bin_fresh_recruit_backend.model.enums.SendStatus;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeSendRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshComSendVo;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolRateVo;
import com.bin.bin_fresh_recruit_backend.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.FRESH_ROLE;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.SCHOOL_LOGIN_STATE;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.USER_LOGIN_STATE;

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
    public SchoolRateVo getRate(HttpServletRequest request) {
        SchoolRateVo result = new SchoolRateVo();
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
        result.setFreshTotalNum(totalNum);
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
        result.setFreshSendHaveNum(haveNum);
        result.setFreshSendLookedNum(lookedNum);
        result.setFreshSendInvitedNum(invitedNum);
        result.setFreshSendNoPassNum(noPassNum);
        result.setFreshSendFinishNum(sendFinishNum);
        result.setFreshSendSuccessNum(successNum);
        result.setEmploymentRate((float)(successNum/totalNum));
        return result;
    }
}




