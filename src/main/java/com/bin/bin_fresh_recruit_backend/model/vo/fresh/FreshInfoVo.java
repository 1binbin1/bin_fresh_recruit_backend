package com.bin.bin_fresh_recruit_backend.model.vo.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: hongxiaobin
 * @Time: 2023/11/5 17:00
 */
@Data
public class FreshInfoVo implements Serializable {
    private static final long serialVersionUID = -7233657521412585668L;

    /**
     * id
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * 用户姓名
     */
    @JsonProperty("user_name")
    private String userName;

    /**
     * 性别 0-男 1-女
     */
    @JsonProperty("user_sex")
    private Integer userSex;

    /**
     * 用户手机号
     */
    @JsonProperty("user_phone")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @JsonProperty("user_email")
    private String userEmail;

    /**
     * 毕业学校
     */
    @JsonProperty("user_school")
    private String userSchool;

    /**
     * 专业
     */
    @JsonProperty("user_major")
    private String userMajor;

    /**
     * 毕业年份
     */
    @JsonProperty("user_year")
    private String userYear;

    /**
     * 学历
     */
    @JsonProperty("user_education")
    private String userEducation;

    /**
     * 头像链接
     */
    @JsonProperty("a_avatar")
    private String aAvatar;
}
