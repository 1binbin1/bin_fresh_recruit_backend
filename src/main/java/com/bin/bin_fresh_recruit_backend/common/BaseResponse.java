package com.bin.bin_fresh_recruit_backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 全局响应
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/4 16:04
 */
@Data
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
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public BaseResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(int code, T data) {
        this(code, "", data);
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public BaseResponse(ErrorCode errorCode, String msg) {
        this(errorCode.getCode(), msg, null);
    }
}
