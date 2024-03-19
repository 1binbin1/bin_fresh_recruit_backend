package com.bin.bin_fresh_recruit_backend.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.bin.bin_fresh_recruit_backend.utils.FileTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 阿里云OSS上传文件
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/13 15:15
 */
@Slf4j
@Component
@Configuration
public class AliyunOSSConfig {
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.bucketname}")
    private String bucketname;

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

    /**
     * 文件上传
     *
     * @param file 要上传的文件
     * @param uid  用户id
     * @return 文件访问路径
     */
    public String upload(MultipartFile file, String uid, String pathPrefix) {
        String fileName = getFileName(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            //        设置上传信息
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(FileTypeUtils.getContentType(fileName));
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            //1 在文件名称里面添加随机唯一值
            if (!Objects.equals(pathPrefix, "")) {
                fileName = bucketname + "/" + pathPrefix + "/" + uid + "/" + fileName;
            }
            //oss方法实现上传
            //第一个参数 bucket名称
            //第二个参数 上传到oss文件路径和名称 fileName
            //第三个参数 上传文件输入流
            ossClient.putObject(bucketname, fileName, inputStream, objectMetadata);
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            String url = "https://" + bucketname + "." + endpoint + "/" + fileName;

            return url;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败：{}", fileName);
            return null;
        }
    }
}
