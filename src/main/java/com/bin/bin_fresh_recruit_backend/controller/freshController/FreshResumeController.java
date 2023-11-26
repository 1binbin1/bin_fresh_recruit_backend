package com.bin.bin_fresh_recruit_backend.controller.freshController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;
import com.bin.bin_fresh_recruit_backend.service.FreshResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    /**
     * 上传简历附件
     *
     * @param request 登录态
     * @param file    简历文件
     * @return 简历信息
     */
    @PostMapping("/add")
    public BaseResponse<ResumeInfoVo> addResume(HttpServletRequest request, @RequestBody MultipartFile file) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (file == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ResumeInfoVo resumeInfoVo = freshResumeService.addResume(request, file);
        return ResultUtils.success(resumeInfoVo);
    }

    /**
     * 删除简历附件
     *
     * @param request       登录态
     * @param resumeRequest 请求参数
     * @return 已删除的简历ID
     */
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
    @PostMapping("/update")
    public BaseResponse<ResumeInfoVo> updateResume(HttpServletRequest request,
                                                   @RequestBody MultipartFile file,
                                                   @RequestParam("resume_id") String resumeId) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (file == null || resumeId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ResumeInfoVo resumeInfoVo = freshResumeService.updateResume(request, file, resumeId);
        return ResultUtils.success(resumeInfoVo);
    }


}
