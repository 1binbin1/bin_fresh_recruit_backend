package com.bin.bin_fresh_recruit_backend.controller.chatController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.constant.CommonConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.request.chat.AddChatByComRequest;
import com.bin.bin_fresh_recruit_backend.model.request.chat.AddChatByFreshRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.chat.ChatVo;
import com.bin.bin_fresh_recruit_backend.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 聊天相关控制器
 *
 * @Author: hongxiaobin
 * @Time: 2024/1/6 11:13
 */
@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    @Resource
    private ChatService chatService;

    /**
     * 应届生方发起聊天
     *
     * @param addChatByFreshRequest 请求参数
     * @param request               登录态
     * @return 响应
     */
    @PostMapping("/fresh")
    public BaseResponse<ChatVo> addChatByFresh(@RequestBody AddChatByFreshRequest addChatByFreshRequest,
                                               HttpServletRequest request) {
        String comId = addChatByFreshRequest.getComId();
        String content = addChatByFreshRequest.getContent();
        if (StringUtils.isAnyBlank(comId, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChatVo chatVo = chatService.addChat(request, comId, content, CommonConstant.CHAT_USER_FRESH);
        return ResultUtils.success(chatVo);
    }

    /**
     * 企业方发起聊天
     *
     * @param addChatByComRequest 请求参数
     * @param request             登录态
     * @return 响应
     */
    @PostMapping("/com")
    public BaseResponse<ChatVo> addChatByCom(@RequestBody AddChatByComRequest addChatByComRequest,
                                             HttpServletRequest request) {
        String userId = addChatByComRequest.getUserId();
        String content = addChatByComRequest.getContent();
        if (StringUtils.isAnyBlank(userId, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChatVo chatVo = chatService.addChat(request, userId, content, CommonConstant.CHAT_USER_FRESH);
        return ResultUtils.success(chatVo);
    }
}
