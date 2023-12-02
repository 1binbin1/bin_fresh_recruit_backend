package com.bin.bin_fresh_recruit_backend.controller.freshController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.PurposeDeleteRequest;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.PurposeRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.JobPurposeVo;
import com.bin.bin_fresh_recruit_backend.service.JobPurposeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * C端-意向岗位相关
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/5 14:33
 */
@RestController
@RequestMapping("/fresh/purpose")
@Slf4j
public class FreshPurposeController {
    @Resource
    private JobPurposeService jobPurposeService;

    /**
     * 新增岗位意向
     *
     * @param request        登录态
     * @param purposeRequest 请求参数
     * @return 岗位意向信息
     */
    @PostMapping("/add")
    public BaseResponse<JobPurposeVo> addPurpose(HttpServletRequest request, @RequestBody PurposeRequest purposeRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (purposeRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        JobPurposeVo jobPurposeVo = jobPurposeService.addJobPurpose(request, purposeRequest);
        return ResultUtils.success(jobPurposeVo);
    }

    /**
     * 更新岗位意向
     *
     * @param request        登录态
     * @param purposeRequest 请求参数
     * @return 岗位意向信息
     */
    @PostMapping("/update")
    public BaseResponse<JobPurposeVo> updatePurpose(HttpServletRequest request, @RequestBody PurposeRequest purposeRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (purposeRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        JobPurposeVo purposeVo = jobPurposeService.updateJobPurpose(request, purposeRequest);
        return ResultUtils.success(purposeVo);
    }

    /**
     * 删除岗位意向
     *
     * @param request              登录态
     * @param purposeDeleteRequest 请求参数
     * @return 请求响应
     */
    @PostMapping("/delete")
    public BaseResponse<Integer> deletePurpose(HttpServletRequest request, @RequestBody PurposeDeleteRequest purposeDeleteRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (purposeDeleteRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Integer result = jobPurposeService.deleteJobPurpose(request, purposeDeleteRequest);
        return ResultUtils.success(result);
    }

    /**
     * 获取意向岗位列表
     *
     * @param request 登录态
     * @return 意向岗位列表
     */
    @GetMapping("/list")
    public BaseResponse<List<JobPurposeVo>> getPurposeList(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        List<JobPurposeVo> purposeList = jobPurposeService.getPurposeList(request);
        return ResultUtils.success(purposeList);
    }

    /**
     * 获取岗位意向信息
     *
     * @param request 登录态
     * @param jobId   意向岗位id
     * @return 岗位信息
     */
    @GetMapping("/one")
    public BaseResponse<JobPurposeVo> getPurposeOne(HttpServletRequest request, @RequestParam("id") Integer jobId) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (jobId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        JobPurposeVo jobPurposeVo = jobPurposeService.getPurposeOne(request, jobId);
        return ResultUtils.success(jobPurposeVo);
    }
}
