package com.bin.bin_fresh_recruit_backend.config;

import com.bin.bin_fresh_recruit_backend.constant.DefaultFileConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 默认文件配置
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/19 23:22
 */
@Component
@Configuration
@Slf4j
public class DefaultFileConfig {
    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.bucketname}")
    private String bucketname;

    public String getDefaultAvatar(Integer role) {
        String url = "https://" + bucketname + "." + endpoint + "/";
        switch (role) {
            case 0:
                url = url + DefaultFileConstant.SCHOOL_DEFAULT_AVATAR;
                break;
            case 1:
                url = url + DefaultFileConstant.FRESH_DEFAULT_AVATAR;
                break;
            case 2:
                url = url + DefaultFileConstant.COM_DEFAULT_AVATAR;
                break;
            default:
                url = url + "";
        }
        return url;
    }
}
