package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.JobPurposeMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.JobPurpose;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.PurposeDeleteRequest;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.PurposeRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.JobPurposeVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.JobPurposeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.USER_LOGIN_STATE;

/**
 * @author hongxiaobin
 * @description 针对表【t_job_purpose(应届生岗位意向表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class JobPurposeServiceImpl extends ServiceImpl<JobPurposeMapper, JobPurpose>
        implements JobPurposeService {
    @Resource
    private JobPurposeMapper jobPurposeMapper;

    @Resource
    private AccountService accountService;

    /**
     * 保存岗位意向
     *
     * @param request        登录态
     * @param purposeRequest 请求参数
     * @return 岗位意向信息
     */
    @Override
    public JobPurposeVo addJobPurpose(HttpServletRequest request, PurposeRequest purposeRequest) {
        String city = purposeRequest.getCity();
        String jobType = purposeRequest.getJobType();
        String jobPay = purposeRequest.getJobPay();
        if (StringUtils.isAnyBlank(city, jobType, jobPay)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = loginInfo.getAId();
        JobPurpose jobPurpose = new JobPurpose();
        jobPurpose.setUserId(userId);
        jobPurpose.setCity(city);
        jobPurpose.setJobType(jobType);
        jobPurpose.setJobPay(jobPay);
        int insert = jobPurposeMapper.insert(jobPurpose);
        if (insert == 0) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        jobPurpose = this.getById(jobPurpose.getId());
        JobPurposeVo jobPurposeVo = new JobPurposeVo();
        BeanUtils.copyProperties(jobPurpose, jobPurposeVo);
        return jobPurposeVo;
    }

    /**
     * 修改岗位意向
     *
     * @param request        登录态
     * @param purposeRequest 请求参数
     * @return 意向岗位信息
     */
    @Override
    public JobPurposeVo updateJobPurpose(HttpServletRequest request, PurposeRequest purposeRequest) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        Integer jobPurposeId = purposeRequest.getId();
        String city = purposeRequest.getCity();
        String jobType = purposeRequest.getJobType();
        String jobPay = purposeRequest.getJobPay();
        if (jobPurposeId == null || StringUtils.isAnyBlank(city, jobType, jobPay)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        JobPurpose jobPurpose = new JobPurpose();
        jobPurpose.setId(jobPurposeId);
        jobPurpose.setUserId(loginInfo.getAId());
        jobPurpose.setCity(city);
        jobPurpose.setJobType(jobType);
        jobPurpose.setJobPay(jobPay);
        boolean updateById = this.updateById(jobPurpose);
        if (!updateById) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        jobPurpose = this.getById(jobPurpose.getId());
        JobPurposeVo jobPurposeVo = new JobPurposeVo();
        BeanUtils.copyProperties(jobPurpose, jobPurposeVo);
        return jobPurposeVo;
    }

    /**
     * 删除岗位意向
     *
     * @param request              登录态
     * @param purposeDeleteRequest 请求参数
     * @return 删除结果
     */
    @Override
    public Integer deleteJobPurpose(HttpServletRequest request, PurposeDeleteRequest purposeDeleteRequest) {
        Integer id = purposeDeleteRequest.getId();
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = loginInfo.getAId();
        // 删除
        QueryWrapper<JobPurpose> jobPurposeQueryWrapper = new QueryWrapper<>();
        jobPurposeQueryWrapper.eq("id", id);
        jobPurposeQueryWrapper.eq("user_id", userId);
        boolean remove = this.remove(jobPurposeQueryWrapper);
        if (!remove) {
            throw new BusinessException(ErrorCode.NO_RESOURCE_ERROR);
        }
        return id;
    }

    /**
     * 获取岗位意向列表
     *
     * @param request 登录态
     * @return 岗位意向列表
     */
    @Override
    public List<JobPurposeVo> getPurposeList(HttpServletRequest request) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = loginInfo.getAId();
        QueryWrapper<JobPurpose> jobPurposeQueryWrapper = new QueryWrapper<>();
        jobPurposeQueryWrapper.eq("user_id", userId);
        jobPurposeQueryWrapper.orderByDesc("create_time");
        List<JobPurpose> list = this.list(jobPurposeQueryWrapper);
        ArrayList<JobPurposeVo> jobPurposeVos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (JobPurpose jobPurpose : list) {
                JobPurposeVo jobPurposeVo = new JobPurposeVo();
                BeanUtils.copyProperties(jobPurpose, jobPurposeVo);
                jobPurposeVos.add(jobPurposeVo);
            }
        }
        return jobPurposeVos;
    }

    /**
     * 获取岗位意向详情
     *
     * @param request 登录态
     * @param jobId   意向岗位ID
     * @return 岗位意向详情
     */
    @Override
    public JobPurposeVo getPurposeOne(HttpServletRequest request, Integer jobId) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        JobPurpose jobPurpose = this.getById(jobId);
        JobPurposeVo jobPurposeVo = new JobPurposeVo();
        if (jobPurpose==null){
            throw new BusinessException(ErrorCode.NO_RESOURCE_ERROR);
        }
        BeanUtils.copyProperties(jobPurpose, jobPurposeVo);
        return jobPurposeVo;
    }
}




