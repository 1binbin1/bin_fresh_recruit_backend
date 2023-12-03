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
}
