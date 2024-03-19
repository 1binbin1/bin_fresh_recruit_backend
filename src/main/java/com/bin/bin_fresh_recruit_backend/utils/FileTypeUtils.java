package com.bin.bin_fresh_recruit_backend.utils;

/**
 * @Author: hongxiaobin
 * @Time: 2024/3/19 19:00
 */
public class FileTypeUtils {
    // 根据文件名获取contentType
    public static String getContentType(String filename) {
        String contentType = null;
        String suffix = filename.substring(filename.lastIndexOf("."));
        switch (suffix) {
            case ".jpg":
            case ".jpeg":
                contentType = "image/jpeg";
                break;
            case ".png":
                contentType = "image/png";
                break;
            case ".gif":
                contentType = "image/gif";
                break;
            case ".bmp":
                contentType = "image/bmp";
                break;
            case ".webp":
                contentType = "image/webp";
                break;
            // office类型
            case ".doc":
                contentType = "application/msword";
                break;
            case ".docx":
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                break;
            case ".xls":
                contentType = "application/vnd.ms-excel";
                break;
            case ".xlsx":
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                break;
            case ".ppt":
                contentType = "application/vnd.ms-powerpoint";
                break;
            case ".pptx":
                contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
                break;
            case ".pdf":
                contentType = "application/pdf";
                break;
            case ".txt":
                contentType = "text/plain";
                break;
            default:
                contentType = "application/octet-stream";
        }
        return contentType;
    }

}
