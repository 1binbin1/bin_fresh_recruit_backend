package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.Chat;
import com.bin.bin_fresh_recruit_backend.model.vo.chat.ChatVo;
import com.bin.bin_fresh_recruit_backend.model.vo.chat.LatelyFreshVo;
import org.springframework.web.multipart.MultipartFile;

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
     *
     * @param request  登录态
     * @param aId      账号ID
     * @param content  聊天内容
     * @param userType 发起人类型
     * @return 响应
     */
    ChatVo addChat(HttpServletRequest request, String aId, String content, Integer userType);

    /**
     * 获取聊天记录
     *
     * @return
     */
    List<ChatVo> getChatList(String userId, String comId);

    /**
     * 企业获取聊天对象
     *
     * @param request 登录态
     * @return 响应参数
     */
    List<LatelyFreshVo> getLatelyFreshList(HttpServletRequest request);


    /**
     * 应届生获取聊天对象
     *
     * @param request 登录态
     * @return 响应参数
     */
    List<LatelyFreshVo> getLatelyComList(HttpServletRequest request);

    /**
     * 企业发送图片
     * @param request
     * @param multipartFile
     * @param userId
     * @param chatUserCom
     * @return
     */
    ChatVo addChatByPicture(HttpServletRequest request, MultipartFile multipartFile, String userId, Integer chatUserCom);
}
