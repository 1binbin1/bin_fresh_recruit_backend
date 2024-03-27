package com.bin.bin_fresh_recruit_backend.controller.companyController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.IgnoreAuth;
import com.bin.bin_fresh_recruit_backend.interceptor.LoginUser;
import com.bin.bin_fresh_recruit_backend.model.request.company.*;
import com.bin.bin_fresh_recruit_backend.model.vo.company.ComJobInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobSendVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;
import com.bin.bin_fresh_recruit_backend.service.FreshComSendService;
import com.bin.bin_fresh_recruit_backend.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @Resource
    private FreshComSendService freshComSendService;

    /**
     * 新增岗位信息
     *
     * @param request           登录态
     * @param jobInfoAddRequest 岗位信息
     * @return 新增的岗位信息
     */
    @LoginUser
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
    @LoginUser
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

    /**
     * 删除岗位
     *
     * @param request          登录态
     * @param jobInfoIdRequest 岗位信息请求
     * @return 删除的岗位ID
     */
    @LoginUser
    @PostMapping("/delete")
    public BaseResponse<String> deleteJob(HttpServletRequest request, @RequestBody JobInfoIdRequest jobInfoIdRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (jobInfoIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String jobId = jobInfoService.deleteJob(request, jobInfoIdRequest);
        return ResultUtils.success(jobId);
    }

    /**
     * 简历筛选
     *
     * @param request               登录态
     * @param resumeFiltrateRequest 请求参数
     * @return 简历投递信息
     */
    @LoginUser
    @PostMapping("/filrate")
    public BaseResponse<ResumeInfoVo> filrateResume(HttpServletRequest request, @RequestBody ResumeFiltrateRequest resumeFiltrateRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (resumeFiltrateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ResumeInfoVo resumeInfoVo = jobInfoService.filrate(request, resumeFiltrateRequest);
        return ResultUtils.success(resumeInfoVo);
    }

    /**
     * 查询岗位信息
     *
     * @param jobId 岗位ID
     * @return 返回参数
     */
    @IgnoreAuth
    @GetMapping("/one")
    public BaseResponse<ComJobInfoVo> getJobOne(@RequestParam(value = "job_id") String jobId) {
        if (jobId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ComJobInfoVo jobInfoVo = jobInfoService.getJobOne(jobId);
        return ResultUtils.success(jobInfoVo);
    }


    /**
     * 岗位搜索
     *
     * @param jobSearchRequest 请求参数
     * @return 响应
     */
    @IgnoreAuth
    @PostMapping("/list/search")
    public BaseResponse<PageVo<ComJobInfoVo>> getJobList(@RequestBody JobSearchRequest jobSearchRequest) {
        if (jobSearchRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        PageVo<ComJobInfoVo> pageVo = jobInfoService.getJobList(jobSearchRequest);
        return ResultUtils.success(pageVo);
    }

    /**
     * 查询企业的岗位
     *
     * @param jobComSearchRequest 请求参数
     * @return 响应信息
     */
    @IgnoreAuth
    @PostMapping("/list/company")
    public BaseResponse<PageVo<ComJobInfoVo>> getJobListByCompany(@RequestBody JobComSearchRequest jobComSearchRequest) {
        if (jobComSearchRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        PageVo<ComJobInfoVo> pageVoCom = jobInfoService.getJobListByCom(jobComSearchRequest);
        return ResultUtils.success(pageVoCom);
    }

    /**
     * 企业获取投递列表
     *
     * @param request                 登录态
     * @param jobComSendSearchRequest 请求参数
     * @return 响应参数（分页）
     */
    @LoginUser
    @PostMapping("/send")
    public BaseResponse<PageVo<JobSendVo>> getFreshSendResumeByCom(HttpServletRequest request, @RequestBody JobComSendSearchRequest jobComSendSearchRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (jobComSendSearchRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        PageVo<JobSendVo> pageVoCom = freshComSendService.getFreshSend(request, jobComSendSearchRequest);
        return ResultUtils.success(pageVoCom);
    }
}
