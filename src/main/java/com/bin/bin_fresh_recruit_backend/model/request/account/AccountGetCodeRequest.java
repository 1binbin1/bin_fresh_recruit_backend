package com.bin.bin_fresh_recruit_backend.model.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号相关请求参数
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/4 22:47
 */
@Data
public class AccountGetCodeRequest implements Serializable {
    private static final long serialVersionUID = 2774531409721924647L;

    /**
     * 手机号
     */
    @JsonProperty("phone")
    private String phone;

    /**
     * 角色
     * 0-管理员 1-应届生 2-企业
     */
    @JsonProperty("role")
    private Integer role;

    /**
     * 发送类型 0-忘记密码 1-登录 2-注册
     */
    @JsonProperty("type")
    private Integer type;
}
