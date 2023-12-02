package com.bin.bin_fresh_recruit_backend.controller.companyController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobInfoAddRequest;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobInfoDeleteRequest;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobInfoUpdateRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobInfoVo;
import com.bin.bin_fresh_recruit_backend.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: hongxiaobin
 * @Time: 2023/12/2 17:49
 */
@RestController
@RequestMapping("/com/job")
@Slf4j
public class JobController {
    @Resource
    private JobInfoService jobInfoService;

    /**
     * 新增岗位信息
     *
     * @param request           登录态
     * @param jobInfoAddRequest 岗位信息
     * @return 新增的岗位信息
     */
    @PostMapping("/add")
    public BaseResponse<JobInfoVo> addJob(HttpServletRequest request, @RequestBody JobInfoAddRequest jobInfoAddRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (jobInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        JobInfoVo jobInfoVo = jobInfoService.addJob(request, jobInfoAddRequest);
        return ResultUtils.success(jobInfoVo);
    }

    /**
     * 更新岗位信息
     *
     * @param request              登录态
     * @param jobInfoUpdateRequest 岗位信息
     * @return 岗位信息
     */
    @PostMapping("/update")
    public BaseResponse<JobInfoVo> updateJob(HttpServletRequest request, @RequestBody JobInfoUpdateRequest jobInfoUpdateRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (jobInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        JobInfoVo jobInfoVo = jobInfoService.updateJob(request, jobInfoUpdateRequest);
        return ResultUtils.success(jobInfoVo);
    }

    @PostMapping("/delete")
    public BaseResponse<String> deleteJob(HttpServletRequest request, @RequestBody JobInfoDeleteRequest jobInfoDeleteRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (jobInfoDeleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String jobId = jobInfoService.deleteJob(request, jobInfoDeleteRequest);
        return ResultUtils.success(jobId);
    }
}
