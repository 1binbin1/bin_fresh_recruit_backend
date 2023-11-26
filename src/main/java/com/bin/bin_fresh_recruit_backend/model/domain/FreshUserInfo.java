package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 应届生个人信息表
 * @author hongxiaobin
 * @TableName t_fresh_user_info
 */
@TableName(value ="t_fresh_user_info")
@Data
public class FreshUserInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 用户姓名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 性别 0-男 1-女
     */
    @TableField(value = "user_sex")
    private Integer userSex;

    /**
     * 用户手机号
     */
    @TableField(value = "user_phone")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_email")
    private String userEmail;

    /**
     * 毕业学校
     */
    @TableField(value = "user_school")
    private String userSchool;

    /**
     * 专业
     */
    @TableField(value = "user_major")
    private String userMajor;

    /**
     * 毕业年份
     */
    @TableField(value = "user_year")
    private String userYear;

    /**
     * 学历
     */
    @TableField(value = "user_education")
    private String userEducation;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除，0-否，1-是
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}