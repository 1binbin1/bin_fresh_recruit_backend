package com.bin.bin_fresh_recruit_backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件转换工具类
 *
 * @Author: hongxiaobin
 * @Time: 2024/4/18 0:28
 */
@Slf4j
public class FileSwitchUtils {
    /**
     * Url 转换 File
     *
     * @param url
     * @param fileName
     * @return
     * @throws Exception
     */
    public static File urlToFlie(String url, String fileName) throws Exception {
        log.info("开始 url 转换 MultipartFile，url={} ,fileName={}", url, fileName);
        File file = null;
        try {
            HttpURLConnection httpUrl = (HttpURLConnection) new URL(url).openConnection();
            httpUrl.connect();
            log.info("成功建立httpUrl连接" + httpUrl);
            file = inputStreamToFile(httpUrl.getInputStream(), fileName);
            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("完成 url 转换 MultipartFile，url={} ,fileName={}", url, fileName);
        return file;
    }


    /**
     * InputStream 转 File
     *
     * @param ins
     * @param fileName
     * @return
     * @throws Exception
     */
    public static File inputStreamToFile(InputStream ins, String fileName) throws Exception {
        log.info("开始 InputStream 转换 File,fileName={}", fileName);
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        log.info("完成 InputStream 转换 File,fileName={}", fileName);
        return file;
    }


    /**
     * File 转 MultipartFile
     *
     * @param file
     * @return
     */
    public static CommonsMultipartFile fileToMultipartFile(File file) {
        log.info("fileToMultipartFile文件转换中：" + file.getAbsolutePath());
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(file.getName(), "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CommonsMultipartFile((org.apache.commons.fileupload.FileItem) item);
    }
}
