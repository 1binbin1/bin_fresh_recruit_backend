package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.model.domain.Chat;
import com.bin.bin_fresh_recruit_backend.service.ChatService;
import com.bin.bin_fresh_recruit_backend.mapper.ChatMapper;
import org.springframework.stereotype.Service;

/**
* @author hongxiaobin
* @description 针对表【t_chat(聊天记录表)】的数据库操作Service实现
* @createDate 2023-11-04 15:34:12
*/
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
    implements ChatService {

}




