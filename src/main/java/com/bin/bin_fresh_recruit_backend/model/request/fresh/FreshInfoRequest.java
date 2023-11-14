package com.bin.bin_fresh_recruit_backend.model.request.fresh;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: hongxiaobin
 * @Time: 2023/11/5 16:58
 */
@Data
public class FreshInfoRequest implements Serializable {
    private static final long serialVersionUID = -4229243524113912917L;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 性别 0-男 1-女
     */
    private Integer userSex;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 毕业学校
     */
    private String userSchool;

    /**
     * 专业
     */
    private String userMajor;

    /**
     * 毕业年份
     */
    private String userYear;

    /**
     * 学历
     */
    private String userEducation;
}
