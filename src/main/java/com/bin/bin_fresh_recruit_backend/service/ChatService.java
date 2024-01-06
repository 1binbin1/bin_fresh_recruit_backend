package com.bin.bin_fresh_recruit_backend.service;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.model.domain.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.vo.chat.ChatVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author hongxiaobin
* @description 针对表【t_chat(聊天记录表)】的数据库操作Service
* @createDate 2023-11-04 15:34:12
*/
public interface ChatService extends IService<Chat> {
    /**
     * 保存聊天记录
     * @param request 登录态
     * @param aId 账号ID
     * @param content 聊天内容
     * @param userType 发起人类型
     * @return 响应
     */
    ChatVo addChat(HttpServletRequest request, String aId, String content, Integer userType);

    /**
     * 获取聊天记录
     * @param aId 账号ID
     * @param request 登录态
     * @param userType 发起人
     * @return
     */
    List<ChatVo> getChatList(String aId, HttpServletRequest request, Integer userType);
}
