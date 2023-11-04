package com.bin.bin_fresh_recruit_backend.common;

/**
 * 错误状态码
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/4 16:17
 */
public enum ErrorCode {
    PARAMS_ERROR(40000, "请求参数错误"),
    NULL_ERROR(40001, "请求数据为空"),
    NO_LOGIN(40100, "未登录"),
    NO_AUTH(40101, "无权限"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    PHONE_ERROR(40003, "手机号格式错误"),
    PASSWORD_ERROR(40004, "两次密码输入不相同"),
    ACCOUNT_ERROR(40005, "账号已存在"),
    ACCOUNTNOT_ERROR(40005, "账号不存在"),
    ROLE_ERROR(40005, "角色代码错误"),
    INSERT_ERROR(40006, "插入数据异常"),
    ;

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
