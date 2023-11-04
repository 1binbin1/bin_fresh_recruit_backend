package com.bin.bin_fresh_recruit_backend.common;

/**
 * 错误状态码
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/4 16:17
 */
public enum ErrorCode {
    /**
     * 请求参数为空
     */
    PARAMS_ERROR(40000, "请求参数错误"),
    /**
     * 请求数据为空
     */
    NULL_ERROR(40001, "请求数据为空"),
    /**
     * 未登录
     */
    NO_LOGIN(40100, "未登录"),
    /**
     * 无权限
     */
    NO_AUTH(40101, "无权限"),
    /**
     * 系统内部异常
     */
    SYSTEM_ERROR(50000, "系统内部异常");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
