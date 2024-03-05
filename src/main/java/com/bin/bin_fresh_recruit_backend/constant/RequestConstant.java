package com.bin.bin_fresh_recruit_backend.constant;

/**
 * 请求参数长度
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/18 18:01
 */
public interface RequestConstant {
    /**
     * 标题长度
     */
    Integer TITLE_MAX_LENGTH = 16;

    /**
     * 文本长度
     */
    Integer TEXT_MAX_LENGTH = 2000;

    /**
     * 忘记密码
     */
    Integer FORGET = 0;

    /**
     * 登录
     */
    Integer LOGIN = 1;

    /**
     * 注册
     */
    Integer REGISTER = 2;

    /**
     * 默认页码
     */
    Integer DEFAULT_CURRENT = 1;

    // 默认页大小
    Integer DEFAULT_PAGE_SIZE = 20;
}
