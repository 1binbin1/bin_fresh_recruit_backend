/*
 * com.binbin.binapiadmin.config.QiniuyunOSSConfig, 2023-07-12
 * Copyright© 2023 hongxiaobin(1binbin),Inc. All rights reserved.
 * Github link : http://github.com/1binbin
 */

package com.bin.bin_fresh_recruit_backend.config;

import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 七牛云对象存储
 *
 * @Author hongxiaobin
 * @Time 2023/3/5-16:25
 */
@Slf4j
@Component
@Configuration
public class QiniuyunOSSConfig {
    /**
     * 公钥
     */
    @Value("${qiniu.accessKey}")
    private String accessKey;
    /**
     * 私钥
     */
    @Value("${qiniu.secretKey}")
    private String accessSecretKey;
    /**
     * 存储空间
     */
    @Value("${qiniu.bucketName}")
    private String bucketName;
    /**
     * 域名
     */
    @Value("${qiniu.path}")
    private String path;
    /**
     * 空间里的文件夹
     */
    @Value("${qiniu.documentName}")
    private String documentName;

    /**
     * 文件上传
     *
     * @param file 要上传的文件
     * @param uid  用户id
     * @return 文件访问路径
     */
    public String upload(MultipartFile file, String uid) {
        // 生成文件名
        String fileName = getFileName(file.getOriginalFilename());
        // 构造一个带指定Region对象的配置类，华南地区
        com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(configuration);
        try {
            byte[] bytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String token = auth.uploadToken(bucketName);
            String name = documentName + uid + "/" + fileName;
            Response response = uploadManager.put(bytes, name, token);
            // 解析上传成功结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            if (putRet.toString().isEmpty()) {
                return null;
            }
            return path + "/" + name;
        } catch (Exception e) {
            log.error("文件上传失败：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成唯一图片名称
     *
     * @param fileName 真实文件名
     * @return 文件名称
     */
    private static String getFileName(String fileName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        int index = fileName.lastIndexOf(".");
        if (fileName.isEmpty() || index == -1) {
            throw new RuntimeException("文件上传失败");
        }
        // 获取文件后缀
        String suffix = fileName.substring(index).toLowerCase();
        String date = simpleDateFormat.format(new Date());
        return date + suffix;
    }
}
