package com.bin.bin_fresh_recruit_backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.PHOTO_PREFIX;

/**
 * @Author: hongxiaobin
 * @Time: 2024/3/19 18:52
 */
@Slf4j
@Component
@Configuration
public class UploadServiceConfig {
    @Resource
    private AliyunOSSConfig aliyunOSSConfig;

    @Resource
    private QiniuyunOSSConfig qiniuyunOssConfig;

    public String upload(MultipartFile file, String aId, String prefix, Integer serviceType) {
        String uploadResultUrl;
        switch (serviceType) {
            case 0:
                uploadResultUrl = aliyunOSSConfig.upload(file, aId, PHOTO_PREFIX);
                break;
            case 1:
                uploadResultUrl = qiniuyunOssConfig.upload(file, aId, PHOTO_PREFIX);
                break;
            default:
                uploadResultUrl = "";
        }
        return uploadResultUrl;
    }
}
