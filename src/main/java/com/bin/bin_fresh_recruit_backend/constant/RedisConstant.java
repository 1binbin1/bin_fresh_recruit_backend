package com.bin.bin_fresh_recruit_backend.constant;

/**
 * Redis相关常量
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/5 0:25
 */
public interface RedisConstant {
    /**
     * 应届生登录态
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 企业登录态
     */
    String COM_LOGIN_STATE = "comLoginState";

    /**
     * 学校登录态
     */
    String SCHOOL_LOGIN_STATE = "schoolLoginState";

    /**
     * 忘记密码验证码记录
     */
    String FORGET_VERIFICATION_CODE = "forgetVerificationCode";

    /**
     * 登录验证码
     */
    String LOGIN_VERIFICATION_CODE = "loginVerificationCode";

    /**
     * 注册验证码
     */
    String REGISTER_VERIFICATION_CODE = "registerVerificationCode";

    /**
     * 验证码超时时间
     */
    Long VERIFICATION_CODE_TIME = 5 * 60 * 1000L;
}
