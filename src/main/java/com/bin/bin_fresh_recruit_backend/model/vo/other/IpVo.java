package com.bin.bin_fresh_recruit_backend.model.vo.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: hongxiaobin
 * @Time: 2024/4/28 22:11
 */
@Data
public class IpVo {
    /**
     * IP地址
     */
    @JsonProperty("ip_address")
    private String ipAddress;

    /**
     * 国家
     */
    @JsonProperty("country")
    private String country;

    /**
     * 省份
     */
    @JsonProperty("province")
    private String province;

    /**
     * 城市
     */
    @JsonProperty("city")
    private String city;

    /**
     * 地址
     */
    @JsonProperty("address")
    private String address;

    @JsonProperty("city_info")
    private String cityInfo;
}
