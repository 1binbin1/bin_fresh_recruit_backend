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
    LOGIN_ERROR(40005, "账号不存在或密码错误"),
    ROLE_ERROR(40005, "角色代码错误"),
    TYPE_ERROR(40015, "类型错误"),
    INSERT_ERROR(40006, "插入数据异常"),
    UPDATE_ERROR(40007, "更新数据异常"),
    USER_SEX_ERROR(4008, "性别代码错误"),
    GET_INFO_ERROR(4009, "获取角色信息错误"),
    SQL_ERROR(4010, "数据库执行错误"),
    FILE_SIZE_ERROR(4010, "文件太大"),
    SIZE_ERROR(4014, "字符数超过限制"),
    SEND_STATE_ERROR(4011, "简历状态码错误"),
    CODE_ERROR(4012, "验证码错误"),
    PUSH_CODE_ERROR(4013, "验证码发送错误"),
    NO_RESOURCE_ERROR(4015, "该账号不存在该资源"),
    NO_COMPANY_ERROR(4016, "企业不存在"),
    NO_JOB_ERROR(4017, "岗位不存在"),
    NO_RESUME_ERROR(4018, "简历不存在"),
    SEND_RESUME_ERROR(4019, "投递记录已存在"),
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
