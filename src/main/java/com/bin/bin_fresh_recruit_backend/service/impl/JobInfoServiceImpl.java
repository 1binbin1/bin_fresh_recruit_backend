package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.JobInfoMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.JobInfo;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobInfoAddRequest;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobInfoDeleteRequest;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobInfoUpdateRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.JobInfoService;
import com.bin.bin_fresh_recruit_backend.utils.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.JOB_ID;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.COM_LOGIN_STATE;


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
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        JobInfoVo jobInfoVo = new JobInfoVo();
        BeanUtils.copyProperties(jobInfo, jobInfoVo);
        return jobInfoVo;
    }

    @Override
    public String deleteJob(HttpServletRequest request, JobInfoDeleteRequest jobInfoDeleteRequest) {
        Account loginInfo = accountService.getLoginInfo(request, COM_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String comId = loginInfo.getAId();
        String jobId = jobInfoDeleteRequest.getJobId();
        if (StringUtils.isAnyBlank(jobId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 删除
        QueryWrapper<JobInfo> jobInfoQueryWrapper = new QueryWrapper<>();
        jobInfoQueryWrapper.eq("job_id", jobId);
        jobInfoQueryWrapper.eq("com_id", comId);
        boolean remove = this.remove(jobInfoQueryWrapper);
        if (!remove) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        return jobId;
    }
}




