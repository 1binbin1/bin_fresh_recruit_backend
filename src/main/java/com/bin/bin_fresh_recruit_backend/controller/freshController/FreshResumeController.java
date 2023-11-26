package com.bin.bin_fresh_recruit_backend.controller.freshController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;
import com.bin.bin_fresh_recruit_backend.service.FreshResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
