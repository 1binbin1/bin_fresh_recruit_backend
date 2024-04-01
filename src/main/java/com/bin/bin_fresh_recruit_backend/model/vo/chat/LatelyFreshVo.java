package com.bin.bin_fresh_recruit_backend.model.vo.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 最近聊天对象
 *
 * @Author: hongxiaobin
 * @Time: 2024/1/6 11:17
 */
@Data
public class LatelyFreshVo implements Serializable {
    private static final long serialVersionUID = -1336111412951760459L;

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
     * 用户昵称
     */
    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("com_name")
    private String comName;

    /**
     * 头像
     */
    @JsonProperty("a_avatar")
    private String aAvatar;

    /**
     * 发起人类型
     */
    @JsonProperty("user_type")
    private Integer userType;

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    private Date createTime;
}

