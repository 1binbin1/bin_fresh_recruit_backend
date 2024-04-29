package com.bin.bin_fresh_recruit_backend.model.vo.other;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: hongxiaobin
 * @Time: 2024/4/29 17:37
 */
@Data
public class LoginInfoList {
    /**
     * 账号ID
     */
    @JsonProperty(value = "a_id")
    private String aId;

    /**
     * 登录IP
     */
    @JsonProperty(value = "login_ip")
    private String loginIp;

    /**
     * 登录地区
     */
    @JsonProperty(value = "login_address")
    private String loginAddress;

    /**
     * 登录国家
     */
    @JsonProperty(value = "login_country")
    private String loginCountry;

    /**
     * 登录省份
     */
    @JsonProperty(value = "login_province")
    private String loginProvince;

    /**
     * 登录城市
     */
    @JsonProperty(value = "login_city")
    private String loginCity;

    /**
     * 登录设备
     */
    @JsonProperty(value = "login_device")
    private String loginDevice;

    /**
     * 创建时间
     */
    @JsonProperty(value = "create_time")
    private Date createTime;

}
