package com.bin.bin_fresh_recruit_backend.controller.schoolController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.request.school.PublishMessageRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolIntroVo;
import com.bin.bin_fresh_recruit_backend.service.SchoolIntroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: hongxiaobin
 * @Time: 2023/12/18 17:49
 */
@RestController
@Slf4j
@RequestMapping("/school")
public class PublishMessageController {
    @Resource
    private SchoolIntroService schoolIntroService;

    /**
     * 发布资讯
     *
     * @param request               登录态
     * @param publishMessageRequest 请求参数
     * @return 响应
     */
    @PostMapping("/message")
    public BaseResponse<SchoolIntroVo> publishMessage(HttpServletRequest request, @RequestBody PublishMessageRequest publishMessageRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        return ResultUtils.success(schoolIntroService.publishMessage(publishMessageRequest));
    }
}
