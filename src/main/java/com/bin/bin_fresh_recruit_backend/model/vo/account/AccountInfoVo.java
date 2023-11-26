package com.bin.bin_fresh_recruit_backend.model.vo.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号相关响应体（登录态保存的）
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/4 22:54
 */
@Data
@AllArgsConstructor
public class AccountInfoVo implements Serializable {
    private static final long serialVersionUID = 8656263509116674078L;

    /**
     * 账号id
     */
    @JsonProperty("id")
    private String id;

    /**
     * 手机号
     */
    @JsonProperty("phone")
    private String phone;

    @JsonProperty("a_avatar")
    private String aAvatar;
}
