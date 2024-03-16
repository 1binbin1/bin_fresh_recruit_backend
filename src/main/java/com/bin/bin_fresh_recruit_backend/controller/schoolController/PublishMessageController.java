package com.bin.bin_fresh_recruit_backend.controller.schoolController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.LoginUser;
import com.bin.bin_fresh_recruit_backend.model.request.school.MessageDeleteRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.MessageListRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.MessageUpdateRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.PublishMessageRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolIntroVo;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolMessageVo;
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
    @LoginUser
    @PostMapping("/message")
    public BaseResponse<SchoolIntroVo> publishMessage(HttpServletRequest request, @RequestBody PublishMessageRequest publishMessageRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        return ResultUtils.success(schoolIntroService.publishMessage(request, publishMessageRequest));
    }

    /**
     * 获取咨询列表（分页）
     *
     * @param request            登录态
     * @param messageListRequest 请求参数
     * @return 响应参数
     */
    @LoginUser
    @PostMapping("/message/list")
    public BaseResponse<PageVo<SchoolMessageVo>> getMessageList(HttpServletRequest request, @RequestBody MessageListRequest messageListRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (messageListRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        PageVo<SchoolMessageVo> result = schoolIntroService.getMessageList(request, messageListRequest);
        return ResultUtils.success(result);
    }

    /**
     * 更新资讯
     *
     * @param request              登录态
     * @param messageUpdateRequest 请求参数
     * @return 响应数据
     */
    @LoginUser
    @PostMapping("/message/update")
    public BaseResponse<SchoolMessageVo> updateMessage(HttpServletRequest request, @RequestBody MessageUpdateRequest messageUpdateRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (messageUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SchoolMessageVo result = schoolIntroService.updateMessage(request, messageUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 删除咨询
     *
     * @param request              登录态
     * @param messageDeleteRequest 请求参数
     * @return 响应数据
     */
    @LoginUser
    @PostMapping("/message/delete")
    public BaseResponse<SchoolMessageVo> deleteMessage(HttpServletRequest request, @RequestBody MessageDeleteRequest messageDeleteRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (messageDeleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SchoolMessageVo result = schoolIntroService.deleteMessage(request, messageDeleteRequest);
        return ResultUtils.success(result);
    }
}