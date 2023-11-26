package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 聊天记录表
 * @author hongxiaobin
 * @TableName t_chat
 */
@TableName(value ="t_chat")
@Data
public class Chat implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应届生ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 企业ID
     */
    @TableField(value = "com_id")
    private String comId;

    /**
     * 聊天ID
     */
    @TableField(value = "chat_id")
    private String chatId;

    /**
     * 聊天内容
     */
    @TableField(value = "chat_content")
    private String chatContent;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除，0-否，1-是
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}