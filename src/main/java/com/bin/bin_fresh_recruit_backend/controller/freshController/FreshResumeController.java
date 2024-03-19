package com.bin.bin_fresh_recruit_backend.controller.freshController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.LoginUser;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeRequest;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeSendRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshComSendVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshSendStateVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;
import com.bin.bin_fresh_recruit_backend.service.FreshComSendService;
import com.bin.bin_fresh_recruit_backend.service.FreshResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * C端-简历相关
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/5 14:33
 */
@RestController
@RequestMapping("/fresh/resume")
@Slf4j
public class FreshResumeController {
    @Resource
    private FreshResumeService freshResumeService;

    @Resource
    private FreshComSendService freshComSendService;

    /**
     * 上传简历附件
     *
     * @param request     登录态
     * @param file        简历文件
     * @param serviceType 上传服务类型 0-阿里云（默认）1-七牛云
     * @return 简历信息
     */
    @LoginUser
    @PostMapping("/add")
    public BaseResponse<ResumeInfoVo> addResume(HttpServletRequest request, @RequestBody MultipartFile file, @RequestParam("service_type") Integer serviceType) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (file == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ResumeInfoVo resumeInfoVo = freshResumeService.addResume(request, file, serviceType);
        return ResultUtils.success(resumeInfoVo);
    }

    /**
     * 删除简历附件
     *
     * @param request       登录态
     * @param resumeRequest 请求参数
     * @return 已删除的简历ID
     */
    @LoginUser
    @PostMapping("/delete")
    public BaseResponse<String> deleteResume(HttpServletRequest request, @RequestBody ResumeRequest resumeRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (resumeRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String resumeId = freshResumeService.deleteResume(request, resumeRequest);
        return ResultUtils.success(resumeId);
    }

    /**
     * 更新简历附件
     *
     * @param request  登录态
     * @param file     文件
     * @param resumeId 简历ID
     * @return 简历信息
     */
    @LoginUser
    @PostMapping("/update")
    public BaseResponse<ResumeInfoVo> updateResume(HttpServletRequest request,
                                                   @RequestBody MultipartFile file,
                                                   @RequestParam("resume_id") String resumeId,
                                                   @RequestParam("service_type") Integer serviceType) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (file == null || resumeId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ResumeInfoVo resumeInfoVo = freshResumeService.updateResume(request, file, resumeId, serviceType);
        return ResultUtils.success(resumeInfoVo);
    }

    /**
     * 获取简历附件列表
     *
     * @param request 登录态
     * @return 列表
     */
    @LoginUser
    @GetMapping("/list")
    public BaseResponse<List<ResumeInfoVo>> getResumeList(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        List<ResumeInfoVo> list = freshResumeService.getResumeList(request);
        return ResultUtils.success(list);
    }

    /**
     * 获取简历详情
     *
     * @param request  登录态
     * @param resumeId 简历ID
     * @return 简历信息
     */
    @LoginUser
    @GetMapping("/one")
    public BaseResponse<ResumeInfoVo> getResumeOne(HttpServletRequest request,
                                                   @RequestParam("resume_id") String resumeId) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (resumeId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ResumeInfoVo resumeOne = freshResumeService.getResumeOne(request, resumeId);
        return ResultUtils.success(resumeOne);
    }

    /**
     * 投递简历
     *
     * @param request           登录态
     * @param resumeSendRequest 简历投递请求
     * @return 响应数据
     */
    @LoginUser
    @PostMapping("/send")
    public BaseResponse<FreshComSendVo> sendResume(HttpServletRequest request, @RequestBody ResumeSendRequest resumeSendRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (resumeSendRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        FreshComSendVo freshComSendVo = freshComSendService.sendResume(request, resumeSendRequest);
        return ResultUtils.success(freshComSendVo);
    }

    /**
     * 获取投递进度列表
     *
     * @param request 请求参数
     * @return 响应参数
     */
    @LoginUser
    @GetMapping("/send/state")
    public BaseResponse<List<FreshSendStateVo>> getFreshSensState(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        List<FreshSendStateVo> result = freshComSendService.getSendState(request);
        return ResultUtils.success(result);
    }
}
