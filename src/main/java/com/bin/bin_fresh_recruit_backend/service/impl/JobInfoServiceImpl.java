package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.constant.CommonConstant;
import com.bin.bin_fresh_recruit_backend.constant.DictConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.*;
import com.bin.bin_fresh_recruit_backend.model.domain.*;
import com.bin.bin_fresh_recruit_backend.model.enums.SendStatus;
import com.bin.bin_fresh_recruit_backend.model.request.company.*;
import com.bin.bin_fresh_recruit_backend.model.vo.company.ComJobInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.JobInfoService;
import com.bin.bin_fresh_recruit_backend.utils.AlgorithmUtils;
import com.bin.bin_fresh_recruit_backend.utils.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.JOB_ID;
import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.RECOMMEND_NO;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.COM_LOGIN_STATE;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.USER_LOGIN_STATE;


/**
 * @author hongxiaobin
 * @description 针对表【t_job_info(岗位信息表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class JobInfoServiceImpl extends ServiceImpl<JobInfoMapper, JobInfo>
        implements JobInfoService {

    @Resource
    private AccountService accountService;

    @Resource
    private FreshComSendMapper freshComSendMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private JobInfoMapper jobInfoMapper;

    @Resource
    private JobPurposeMapper jobPurposeMapper;

    @Resource
    private DictMapper dictMapper;

    @Resource
    private AccountMapper accountMapper;

    /**
     * 新增岗位信息
     *
     * @param request           登录态
     * @param jobInfoAddRequest 岗位信息请求
     * @return 岗位信息
     */
    @Override
    public JobInfoVo addJob(HttpServletRequest request, JobInfoAddRequest jobInfoAddRequest) {
        Account loginInfo = accountService.getLoginInfo(request, COM_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String comId = loginInfo.getAId();
        String jobIntro = jobInfoAddRequest.getJobIntro();
        String jobRequire = jobInfoAddRequest.getJobRequire();
        String jobName = jobInfoAddRequest.getJobName();
        String jobPay = jobInfoAddRequest.getJobPay();
        String jobType = jobInfoAddRequest.getJobType();
        if (StringUtils.isAnyBlank(comId, jobIntro, jobRequire, jobName, jobPay, jobType)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 保存
        JobInfo jobInfo = new JobInfo();
        jobInfo.setComId(comId);
        jobInfo.setJobId(IdUtils.getId(JOB_ID));
        jobInfo.setJobName(jobName);
        jobInfo.setJobType(jobType);
        jobInfo.setJobIntro(jobIntro);
        jobInfo.setJobRequire(jobRequire);
        jobInfo.setJobPay(jobPay);
        jobInfo.setCreateTime(new Date());
        boolean save = this.save(jobInfo);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        JobInfoVo jobInfoVo = new JobInfoVo();
        BeanUtils.copyProperties(jobInfo, jobInfoVo);
        return jobInfoVo;
    }

    /**
     * 修改岗位信息
     *
     * @param request              登录态
     * @param jobInfoUpdateRequest 岗位信息请求
     * @return 岗位信息
     */
    @Override
    public JobInfoVo updateJob(HttpServletRequest request, JobInfoUpdateRequest jobInfoUpdateRequest) {
        Account loginInfo = accountService.getLoginInfo(request, COM_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String comId = loginInfo.getAId();
        String jobId = jobInfoUpdateRequest.getJobId();
        if (StringUtils.isAnyBlank(jobId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "岗位ID为空");
        }
        // 保存
        JobInfo jobInfo = new JobInfo();
        BeanUtils.copyProperties(jobInfoUpdateRequest, jobInfo);
        QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
        jobInfoQueryWrapper.eq("job_id", jobId);
        jobInfoQueryWrapper.eq("com_id", comId);
        boolean update = this.update(jobInfo, jobInfoQueryWrapper);
        if (!update) {
            throw new BusinessException(ErrorCode.NO_RESOURCE_ERROR);
        }
        jobInfo = this.getOne(jobInfoQueryWrapper);
        if (jobInfo == null) {
            throw new BusinessException(ErrorCode.NO_RESOURCE_ERROR);
        }
        JobInfoVo jobInfoVo = new JobInfoVo();
        BeanUtils.copyProperties(jobInfo, jobInfoVo);
        return jobInfoVo;
    }

    /**
     * 岗位删除
     *
     * @param request          登录态
     * @param jobInfoIdRequest 岗位ID
     * @return 响应信息
     */
    @Override
    public String deleteJob(HttpServletRequest request, JobInfoIdRequest jobInfoIdRequest) {
        Account loginInfo = accountService.getLoginInfo(request, COM_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String comId = loginInfo.getAId();
        String jobId = jobInfoIdRequest.getJobId();
        if (StringUtils.isAnyBlank(jobId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 删除
        QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
        jobInfoQueryWrapper.eq("job_id", jobId);
        jobInfoQueryWrapper.eq("com_id", comId);
        boolean remove = this.remove(jobInfoQueryWrapper);
        if (!remove) {
            throw new BusinessException(ErrorCode.NO_RESOURCE_ERROR);
        }
        return jobId;
    }

    /**
     * 简历筛选
     *
     * @param request               登录态
     * @param resumeFiltrateRequest 请求信息
     * @return 响应信息
     */
    @Override
    public ResumeInfoVo filrate(HttpServletRequest request, ResumeFiltrateRequest resumeFiltrateRequest) {
        Account loginInfo = accountService.getLoginInfo(request, COM_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String comId = loginInfo.getAId();
        if (StringUtils.isAnyBlank(comId)) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String jobId = resumeFiltrateRequest.getJobId();
        String userId = resumeFiltrateRequest.getUserId();
        Integer sendState = resumeFiltrateRequest.getSendState();
        if (StringUtils.isAnyBlank(jobId, userId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (!Objects.equals(sendState, SendStatus.SEND_STATUS_HAVE) &&
                !Objects.equals(sendState, SendStatus.SEND_STATUS_LOOKED) &&
                !Objects.equals(sendState, SendStatus.SEND_STATUS_INVITED) &&
                !Objects.equals(sendState, SendStatus.SEND_STATUS_NO_PASS) &&
                !Objects.equals(sendState, SendStatus.SEND_STATUS_FINISH) &&
                !Objects.equals(sendState, SendStatus.SEND_STATUS_SUCCESS)) {
            throw new BusinessException(ErrorCode.SEND_STATE_ERROR);
        }
        // 修改
        QueryWrapper<FreshComSend> freshComSendQueryWrapper = new QueryWrapper<>();
        freshComSendQueryWrapper.eq("com_id", comId);
        freshComSendQueryWrapper.eq("user_id", userId);
        freshComSendQueryWrapper.eq("job_id", jobId);
        freshComSendQueryWrapper.eq("is_delete", CommonConstant.NO_DELETE);
        FreshComSend freshComSend = new FreshComSend();
        freshComSend.setSendState(sendState);
        int update = freshComSendMapper.update(freshComSend, freshComSendQueryWrapper);
        if (update == 0) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        FreshComSend freshComSendInfo = freshComSendMapper.selectOne(freshComSendQueryWrapper);
        ResumeInfoVo resumeInfoVo = new ResumeInfoVo();
        BeanUtils.copyProperties(freshComSendInfo, resumeInfoVo);
        return resumeInfoVo;
    }

    /**
     * 查询单个岗位信息
     *
     * @param jobId 请求参数
     * @return 岗位信息
     */
    @Override
    public ComJobInfoVo getJobOne(String jobId) {
        if (StringUtils.isAnyBlank(jobId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
        jobInfoQueryWrapper.eq("job_id", jobId);
        JobInfo jobInfo = this.getOne(jobInfoQueryWrapper);
        ComJobInfoVo jobInfoVo = new ComJobInfoVo();
        // 查询企业信息
        if (jobInfo != null) {
            ArrayList<String> comIds = new ArrayList<>();
            comIds.add(jobInfo.getComId());
            Map<String, CompanyInfo> companyInfo = companyInfoMapper.getCompanyInfo(comIds);
            BeanUtils.copyProperties(jobInfo, jobInfoVo);
            jobInfoVo.setComName(companyInfo.get(jobInfo.getComId()).getComName());
            jobInfoVo.setComAddress(companyInfo.get(jobInfo.getComId()).getComAddress());
        }

        return jobInfoVo;
    }

    /**
     * 岗位查询列表
     *
     * @param jobSearchRequest 搜索条件
     * @return 响应信息
     */
    @Override
    public PageVo<ComJobInfoVo> getJobList(JobSearchRequest jobSearchRequest) {
        if (jobSearchRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 查询企业
        QueryWrapper<CompanyInfo> companyInfoQueryWrapper = new QueryWrapper<>();
        if (!Objects.equals(jobSearchRequest.getComType(), "")) {
            companyInfoQueryWrapper.eq("com_type", jobSearchRequest.getComType());
        }
        if (!Objects.equals(jobSearchRequest.getComNum(), "")) {
            companyInfoQueryWrapper.like("com_num", jobSearchRequest.getComNum());
        }
        if (!Objects.equals(jobSearchRequest.getComAddress(), "")) {
            companyInfoQueryWrapper.like("com_address", jobSearchRequest.getComAddress());
        }
        companyInfoQueryWrapper.orderByDesc("create_time");
        List<CompanyInfo> companyInfos = companyInfoMapper.selectList(companyInfoQueryWrapper);
        PageVo<ComJobInfoVo> result = new PageVo<>();
        ArrayList<ComJobInfoVo> jobInfoVos = new ArrayList<>();
        if (companyInfos == null || companyInfos.size() == 0) {
            result.setList(jobInfoVos);
            return result;
        }
        // 提取comId
        List<String> comIds = new ArrayList<>();
        for (CompanyInfo companyInfo : companyInfos) {
            comIds.add(companyInfo.getComId());
        }
        long current = jobSearchRequest.getCurrent();
        long pageSize = jobSearchRequest.getPageSize();
        QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
        if (!Objects.equals(jobSearchRequest.getJobType(), "")) {
            jobInfoQueryWrapper.eq("job_type", jobSearchRequest.getJobType());
        }
        jobInfoQueryWrapper.like("job_name", jobSearchRequest.getSearchContent());
        jobInfoQueryWrapper.in("com_id", comIds);
        Page<JobInfo> jobInfoPage = this.page(new Page<>(current, pageSize), jobInfoQueryWrapper);

        Map<String, Account> account = new HashMap<>();
        Map<String, CompanyInfo> companyInfoMap = new HashMap<>();
        if (comIds.size() != 0) {
            account = accountMapper.getAccount(comIds);
            companyInfoMap = companyInfoMapper.getCompanyInfo(comIds);
        }
        // 处理结果
        for (JobInfo jobInfo : jobInfoPage.getRecords()) {
            ComJobInfoVo jobInfoVo = new ComJobInfoVo();
            BeanUtils.copyProperties(jobInfo, jobInfoVo);
            // 企业信息
            jobInfoVo.setAAvatar(account.get(jobInfo.getComId()).getAAvatar());
            jobInfoVo.setComAddress(companyInfoMap.get(jobInfo.getComId()).getComAddress());
            jobInfoVo.setComName(companyInfoMap.get(jobInfo.getComId()).getComName());
            jobInfoVos.add(jobInfoVo);
        }
        result.setList(jobInfoVos);
        result.setTotal(jobInfoPage.getTotal());
        result.setCurrent(jobInfoPage.getCurrent());
        result.setPageSize(jobInfoPage.getSize());
        return result;
    }

    /**
     * 获取企业岗位列表
     *
     * @param jobComSearchRequest 请求条件
     * @return 响应信息
     */
    @Override
    public PageVo<ComJobInfoVo> getJobListByCom(JobComSearchRequest jobComSearchRequest) {
        String comId = jobComSearchRequest.getComId();
        if (StringUtils.isAnyBlank(comId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long current = jobComSearchRequest.getCurrent();
        long pageSize = jobComSearchRequest.getPageSize();
        String searchContent = jobComSearchRequest.getSearchContent();
        QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
        jobInfoQueryWrapper.eq("com_id", comId);
        jobInfoQueryWrapper.and(j -> j.like("job_name", searchContent).or().like("job_type", searchContent).or().like("job_intro", searchContent).or().like("job_require", searchContent).or().like("job_pay", searchContent));
        jobInfoQueryWrapper.orderByDesc("create_time");
        Page<JobInfo> page = this.page(new Page<>(current, pageSize), jobInfoQueryWrapper);
        PageVo<ComJobInfoVo> jobInfoPageVo = new PageVo<>();
        List<ComJobInfoVo> jobInfoVos = new ArrayList<>();
        // 查询企业信息
        ArrayList<String> comIds = new ArrayList<>();
        comIds.add(comId);
        Map<String, CompanyInfo> companyInfo = companyInfoMapper.getCompanyInfo(comIds);
        Map<String, Account> account = accountMapper.getAccount(comIds);
        for (JobInfo record : page.getRecords()) {
            ComJobInfoVo jobInfoVo = new ComJobInfoVo();
            BeanUtils.copyProperties(record, jobInfoVo);
            // 封装企业信息
            jobInfoVo.setComName(companyInfo.get(record.getComId()).getComName());
            jobInfoVo.setComAddress(companyInfo.get(record.getComId()).getComAddress());
            jobInfoVo.setAAvatar(account.get(record.getComId()).getAAvatar());
            jobInfoVos.add(jobInfoVo);
        }
        jobInfoPageVo.setList(jobInfoVos);
        jobInfoPageVo.setTotal(page.getTotal());
        jobInfoPageVo.setCurrent(page.getCurrent());
        jobInfoPageVo.setPageSize(page.getSize());
        return jobInfoPageVo;
    }

    /**
     * 获取推荐岗位列表
     *
     * @param request     登录态
     * @param limit       推荐个数
     * @param isRecommend 是否推荐 0-否 1-是
     * @return 岗位列表
     */
    @Override
    public List<ComJobInfoVo> getRecommendList(HttpServletRequest request, Integer limit, Integer isRecommend) {
        QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
        long count = this.count();
        Random random = new Random();
        Page<JobInfo> jobInfoPage;
        ArrayList<String> comIds = new ArrayList<>();
        ArrayList<JobInfo> jobInfos = new ArrayList<>();
        if (Objects.equals(isRecommend, RECOMMEND_NO)) {
            // 随机查询
            Page<JobInfo> infoPage = new Page<>(random.nextInt((int) count) - limit, limit);
            jobInfoPage = jobInfoMapper.selectPage(infoPage, jobInfoQueryWrapper);
            // 处理结果
            for (JobInfo jobInfo : jobInfoPage.getRecords()) {
                // 构造id
                comIds.add(jobInfo.getComId());
                jobInfos.add(jobInfo);
            }
        } else {
            List<JobInfo> listByUserId = getRecommendListByUserId(request, limit);
            for (JobInfo jobInfo : listByUserId) {
                comIds.add(jobInfo.getComId());
                jobInfos.add(jobInfo);
            }
        }
        // 组装结果
        Map<String, Account> account = new HashMap<>();
        Map<String, CompanyInfo> companyInfoHashMap = new HashMap<>();
        if (comIds.size() != 0) {
            account = accountMapper.getAccount(comIds);
            companyInfoHashMap = companyInfoMapper.getCompanyInfo(comIds);
        }
        ArrayList<ComJobInfoVo> result = new ArrayList<>();
        for (JobInfo jobInfo : jobInfos) {
            ComJobInfoVo comJobInfoVo = new ComJobInfoVo();
            BeanUtils.copyProperties(jobInfo, comJobInfoVo);
            comJobInfoVo.setAAvatar(account.get(jobInfo.getComId()).getAAvatar());
            comJobInfoVo.setComAddress(companyInfoHashMap.get(jobInfo.getComId()).getComAddress());
            comJobInfoVo.setComName(companyInfoHashMap.get(jobInfo.getComId()).getComName());
            result.add(comJobInfoVo);
        }
        return result;
    }

    /**
     * 获取推荐列表
     *
     * @param request 登录态
     * @param limit   个数
     * @return 分页列表
     */
    private List<JobInfo> getRecommendListByUserId(HttpServletRequest request, Integer limit) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = loginInfo.getAId();
        // 获取应届生的意向岗位类别
        QueryWrapper<JobPurpose> jobPurposeQueryWrapper = new QueryWrapper<>();
        jobPurposeQueryWrapper.eq("user_id", userId);
        List<JobPurpose> jobPurposes = jobPurposeMapper.selectList(jobPurposeQueryWrapper);
        ArrayList<String> freshType = new ArrayList<>();
        for (JobPurpose jobPurpose : jobPurposes) {
            freshType.add(jobPurpose.getJobType());
        }
        // 获取所有类别
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_type", DictConstant.DICT_JOB_TYPE);
        List<Dict> dicts = dictMapper.selectList(dictQueryWrapper);
        ArrayList<String> dictType = new ArrayList<>();
        for (Dict dict : dicts) {
            dictType.add(dict.getDictContent());
        }
        // 查找相似
        // TODO hongxiaobin 2024/1/7 18:13 验证推荐效果
        List<String> recommendTypeList = AlgorithmUtils.getRecommendTypeList(freshType, dictType);
        List<String> recommend = new ArrayList<>();
        for (String s : recommendTypeList) {
            String[] strings = s.split("/");
            recommend.addAll(Arrays.asList(strings));
        }

        QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
        for (String str : recommend) {
            jobInfoQueryWrapper.or().like("job_type", str).or().like("job_name", str).or().like("job_intro", str);
        }
        Random random = new Random();
        long count = this.count(jobInfoQueryWrapper);
        long recommendNum = Math.min(count, limit);
        jobInfoQueryWrapper.last(" limit " + recommendNum);
        List<JobInfo> jobInfos = jobInfoMapper.selectList(jobInfoQueryWrapper);
        // 补充推荐列表
        long num = limit - count;
        List<JobInfo> result = new ArrayList<>();
        if (num > 0) {
            QueryWrapper<JobInfo> jobInfoQueryWrapper1 = new QueryWrapper<>();
            String sql = " limit " + random.nextInt((int) num) + "," + num;
            jobInfoQueryWrapper1.last(sql);
            result = jobInfoMapper.selectList(jobInfoQueryWrapper1);
        }
        // 合并
        jobInfos.addAll(result);
        return jobInfos;
    }
}




