package com.bin.bin_fresh_recruit_backend.model.request.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: hongxiaobin
 * @Time: 2023/11/5 16:58
 */
@Data
public class FreshInfoRequest implements Serializable {
    private static final long serialVersionUID = -4229243524113912917L;

    /**
     * 用户姓名
     */
    @JsonProperty("user_name")
    private String userName;

    /**
     * 用户手机号
     */
    @JsonProperty("user_phone")
    private String userPhone;

    /**
     * 性别 0-男 1-女
     */
    @JsonProperty("user_sex")
    private Integer userSex;

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
}
