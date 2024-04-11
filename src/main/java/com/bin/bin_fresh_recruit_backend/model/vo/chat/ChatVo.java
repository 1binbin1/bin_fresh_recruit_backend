package com.bin.bin_fresh_recruit_backend.model.vo.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: hongxiaobin
 * @Time: 2024/1/6 11:17
 */
@Data
public class ChatVo implements Serializable {
    private static final long serialVersionUID = -1336111412951760459L;

    /**
     * id
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * 应届生ID
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * 企业ID
     */
    @JsonProperty("com_id")
    private String comId;

    /**
     * 发起人类型
     */
    @JsonProperty("user_type")
    private Integer userType;

    /**
     * 聊天内容
     */
    @JsonProperty("chat_content")
    private String chatContent;

    /**
     * 消息类型 0-文字 1-图片 2-文件
     */
    @JsonProperty("chat_type")
    private Integer chatType;

    @JsonProperty("a_avatar")
    private String aAvatar;

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    private Date createTime;
}
