package com.bin.bin_fresh_recruit_backend.common;

import java.io.Serializable;

/**
 * 全局响应
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/4 16:04
 */
public class BaseResponse<T> implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -7912545742723580976L;

    /**
     * 业务状态码
     * 0-正常
     * 1-异常
     */
    private int code;

    /**
     * 响应反馈
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int code, T data) {
        this(code, "", data);
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public BaseResponse(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), message, null);
    }
}
