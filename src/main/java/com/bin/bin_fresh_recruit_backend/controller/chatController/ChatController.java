package com.bin.bin_fresh_recruit_backend.controller.chatController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.constant.CommonConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.IgnoreAuth;
import com.bin.bin_fresh_recruit_backend.interceptor.LoginUser;
import com.bin.bin_fresh_recruit_backend.model.request.chat.AddChatByComRequest;
import com.bin.bin_fresh_recruit_backend.model.request.chat.AddChatByFreshRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.chat.ChatVo;
import com.bin.bin_fresh_recruit_backend.model.vo.chat.LatelyFreshVo;
import com.bin.bin_fresh_recruit_backend.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @LoginUser
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
    @LoginUser
    @PostMapping("/com")
    public BaseResponse<ChatVo> addChatByCom(@RequestBody AddChatByComRequest addChatByComRequest,
                                             HttpServletRequest request) {
        String userId = addChatByComRequest.getUserId();
        String content = addChatByComRequest.getContent();
        if (StringUtils.isAnyBlank(userId, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChatVo chatVo = chatService.addChat(request, userId, content, CommonConstant.CHAT_USER_COM);
        return ResultUtils.success(chatVo);
    }

    /**
     * 企业获取聊天记录
     *
     * @param userId 用户ID
     * @return 响应
     */
    @IgnoreAuth
    @GetMapping("/get/list")
    public BaseResponse<List<ChatVo>> getChatByCom(@RequestParam("user_id") String userId, @RequestParam("com_id") String comId) {
        if (StringUtils.isAnyBlank(userId, comId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<ChatVo> chatVos = chatService.getChatList(userId, comId);
        return ResultUtils.success(chatVos);
    }

    /**
     * 企业获取最近聊天对象
     *
     * @param request 登录态
     * @return 响应数据
     */
    @LoginUser
    @GetMapping("/lately/fresh")
    public BaseResponse<List<LatelyFreshVo>> getLatelyFresh(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        List<LatelyFreshVo> result = chatService.getLatelyFreshList(request);
        return ResultUtils.success(result);
    }

    /**
     * 应届生获取最近聊天对象
     *
     * @param request 登录态
     * @return 响应数据
     */
    @LoginUser
    @GetMapping("/lately/com")
    public BaseResponse<List<LatelyFreshVo>> getLatelyCom(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        List<LatelyFreshVo> result = chatService.getLatelyComList(request);
        return ResultUtils.success(result);
    }


    /**
     * 企业发送图片
     *
     * @param file
     * @param userId
     * @return
     */
    @LoginUser
    @PostMapping("/com/picture")
    public BaseResponse<ChatVo> addChatByFileByCom(@RequestBody MultipartFile file, @RequestParam("user_id") String userId, HttpServletRequest request) {
        if (file == null || userId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ChatVo chatVo = chatService.addChatByPicture(request, file, userId, CommonConstant.CHAT_USER_COM);
        return ResultUtils.success(chatVo);
    }

    /**
     * 应届生发送图片
     *
     * @param file
     * @param comId
     * @return
     */
    @LoginUser
    @PostMapping("/fresh/picture")
    public BaseResponse<ChatVo> addChatByFileByFresh(@RequestBody MultipartFile file, @RequestParam("com_id") String comId, HttpServletRequest request) {
        if (file == null || comId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ChatVo chatVo = chatService.addChatByPicture(request, file, comId, CommonConstant.CHAT_USER_FRESH);
        return ResultUtils.success(chatVo);
    }
}

