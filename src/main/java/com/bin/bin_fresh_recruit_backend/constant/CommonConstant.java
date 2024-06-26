package com.bin.bin_fresh_recruit_backend.constant;

/**
 * @Author: hongxiaobin
 * @Time: 2023/11/4 23:30
 */
public interface CommonConstant {
    /**
     * 密码盐
     */
    String PASSWORD_SALT = "bin";

    /**
     * 角色-学校
     */
    int SCHOOL_ROLE = 0;

    /**
     * 角色-应届生
     */
    int FRESH_ROLE = 1;

    /**
     * 角色-企业
     */
    int COMPANY_ROLE = 2;

    /**
     * 简历ID
     */
    int RESUME_ID = 3;

    /**
     * 岗位ID
     */
    int JOB_ID = 4;

    /**
     * 男
     */
    int MAN = 0;

    /**
     * 女
     */
    int WOMAN = 1;

    /**
     * 默认简历名称
     */
    String DEFAULT_RESUME_NAME = "默认简历.pdf";

    /**
     * 简历大小（10M）
     */
    Long RESUME_SIZE = 1024 * 1024 * 10L;

    /**
     * 头像大小（1MB）
     */
    Long PHOTO_SIZE = 1024 * 1024 * 1L;

    /**
     * 保存头像的文件夹
     */
    String PHOTO_PREFIX = "avatar_photo";

    /**
     * 保存简历的文件夹
     */
    String RESUME_PREFIX = "fresh_resume";

    /**
     * 聊天图片
     */
    String CHAT_PICTURE = "chat_picture";

    /**
     * 不开启推荐
     */
    Integer RECOMMEND_NO = 0;

    /**
     * 开启推荐
     */
    Integer RECOMMEND_YES = 1;

    /**
     * 默认推荐个数
     */
    Integer RECOMMEND_LIMIT = 20;

    /**
     * 额外匹配个数
     */
    Integer MATCH_NUM = 2;

    /**
     * 获取聊天记录天数
     */
    Integer CHAT_DATES = 30;

    /**
     * 发起聊天类型-应届生
     */
    Integer CHAT_USER_FRESH = 1;

    /**
     * 发起聊天类型-企业
     */
    Integer CHAT_USER_COM = 2;

    /**
     * 未删除
     */
    Integer NO_DELETE = 0;

    /**
     * 已删除
     */
    Integer YES_DELETE = 1;

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 开头字符
     */
    String START_CHAR = "C";

    /**
     * 批量添加应届生最多数量
     */
    Integer MAX_ADD_FRESH_NUM = 200;

    /**
     * 最近聊天对象最大个数
     */
    Integer MAX_LATELY_FRESH = 20;

    /**
     * 30天内的最近聊天对象
     */
    Integer MAX_LATTELY_FRESH_DAYS = 30;

    /**
     * 最大记录个数
     */
    Integer MAX_CHAT_LIST = 120;

    /**
     * token过期时间（分钟）
     */
    Integer TOKEN_TIME = 30;

    /**
     * 账号不可包含字符
     */
    char NOT_CONTAIN = '-';

    /**
     * 批量新增岗位最多
     */
    Integer EXCEL_MAX_ROWS = 200;

    /**
     * 数据范围
     */
    Integer RANGE_SIZE = 200;

    /**
     * 只获取半年内的投递记录
     */
    Integer LATE_SEND_DAY = 180;

    /**
     * 账号密码登录
     */
    Integer LOGIN_TYPE_PASSWORD = 0;

    /**
     * 验证码登录
     */
    Integer LOGIN_TYPE_CODE = 1;

    /**
     * 同一天登录不记录
     */
    Integer NO_RECORD_LOGIN_DAY = -1;

    /**
     * 获取登录信息天数
     */
    Integer GET_LOGININFO_DAYS = -90;
}
