package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 登录信息
 * @author hongxiaobin
 * @TableName t_login_info
 */
@TableName(value ="t_login_info")
@Data
public class LoginInfo implements Serializable {
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
     * 登录IP
     */
    @TableField(value = "login_ip")
    private String loginIp;

    /**
     * 登录地区
     */
    @TableField(value = "login_address")
    private String loginAddress;

    /**
     * 登录国家
     */
    @TableField(value = "login_country")
    private String loginCountry;

    /**
     * 登录省份
     */
    @TableField(value = "login_province")
    private String loginProvince;

    /**
     * 登录城市
     */
    @TableField(value = "login_city")
    private String loginCity;

    /**
     * 登录设备
     */
    @TableField(value = "login_device")
    private String loginDevice;

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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}