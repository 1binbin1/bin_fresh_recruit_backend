package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.constant.CommonConstant;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        Account account = getAccountByUserType(request, userType);
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

    /**
     * 获取聊天记录(30day内)
     *
     * @param aId      账号ID
     * @param request  登录态
     * @param userType 发起人
     * @return
     */
    @Override
    public List<ChatVo> getChatList(String aId, HttpServletRequest request, Integer userType) {
        Account account = getAccountByUserType(request, userType);
        // 获取时间范围
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        LocalDate lastDate = now.minusDays(CommonConstant.CHAT_DATES);
        String nowDateString = now.format(formatter);
        String lastDateString = lastDate.format(formatter);
        // 获取两个id
        String userId = "";
        String comId = "";
        if (CHAT_USER_FRESH.equals(userType)) {
            userId = account.getAId();
            comId = aId;
        }
        if (CHAT_USER_COM.equals(userType)) {
            userId = aId;
            comId = account.getAId();
        }
        // 查询
        QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
        chatQueryWrapper.eq("user_id", userId);
        chatQueryWrapper.eq("com_id", comId);
        chatQueryWrapper.between("create_time", lastDateString, nowDateString);
        List<Chat> chatList = this.list(chatQueryWrapper);
        if (chatList != null) {
            return new ArrayList<>();
        }
        // 组装
        ArrayList<ChatVo> chatVoArrayList = new ArrayList<>();
        for (Chat chat : chatList) {
            ChatVo chatVo = new ChatVo();
            BeanUtils.copyProperties(chat, chatVo);
            chatVoArrayList.add(chatVo);
        }
        return chatVoArrayList;
    }

    private Account getAccountByUserType(HttpServletRequest request, Integer userType) {
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
        return account;
    }
}




