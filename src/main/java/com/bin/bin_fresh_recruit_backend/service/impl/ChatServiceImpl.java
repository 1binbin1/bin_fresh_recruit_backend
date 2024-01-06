package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.ChatMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.Chat;
import com.bin.bin_fresh_recruit_backend.model.vo.chat.ChatVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.ChatService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.CHAT_USER_COM;
import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.CHAT_USER_FRESH;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.COM_LOGIN_STATE;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.USER_LOGIN_STATE;

/**
 * @author hongxiaobin
 * @description 针对表【t_chat(聊天记录表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
        implements ChatService {
    @Resource
    private AccountService accountService;

    @Resource
    private ChatMapper chatMapper;

    /**
     * @param request  登录态
     * @param aId      账号ID
     * @param content  聊天内容
     * @param userType 发起人类型
     * @return 响应
     */
    @Override
    public ChatVo addChat(HttpServletRequest request, String aId, String content, Integer userType) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String state;
        if (CHAT_USER_FRESH.equals(userType)) {
            state = USER_LOGIN_STATE;
        } else if (CHAT_USER_COM.equals(userType)) {
            state = COM_LOGIN_STATE;
        } else {
            throw new BusinessException(ErrorCode.ROLE_ERROR);
        }
        Account account = accountService.getLoginInfo(request, state);
        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNTNOT_ERROR);
        }
        // 保存记录
        Chat chat = new Chat();
        if (CHAT_USER_FRESH.equals(userType)) {
            chat.setUserId(account.getAId());
            chat.setComId(aId);
        }
        if (CHAT_USER_COM.equals(userType)) {
            chat.setUserId(aId);
            chat.setComId(account.getAId());
        }
        chat.setUserType(userType);
        chat.setChatContent(content);
        boolean save = this.save(chat);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        ChatVo chatVo = new ChatVo();
        BeanUtils.copyProperties(chat, chatVo);
        return chatVo;
    }
}




