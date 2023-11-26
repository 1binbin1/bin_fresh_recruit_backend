package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 账号信息表
 *
 * @author hongxiaobin
 * @TableName t_account
 */
@TableName(value = "t_account")
@Data
public class Account implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号ID
     */
    @TableField(value = "a_id")
    private String aId;

    /**
     * 手机号
     */
    @TableField(value = "a_phone")
    private String aPhone;

    /**
     * 密码
     */
    @TableField(value = "a_password")
    private String aPassword;

    /**
     * 头像链接
     */
    @TableField(value = "a_avatar")
    private String aAvatar;

    /**
     * 角色，0-管理员，1-应届生，2-企业
     */
    @TableField(value = "a_role")
    private Integer aRole;

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
}