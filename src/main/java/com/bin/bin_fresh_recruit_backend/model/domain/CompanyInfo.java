package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业信息表
 *
 * @author hongxiaobin
 * @TableName t_company_info
 */
@TableName(value = "t_company_info")
@Data
public class CompanyInfo implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 企业ID
     */
    @TableField(value = "com_id")
    private String comId;

    /**
     * 企业名称
     */
    @TableField(value = "com_name")
    private String comName;

    /**
     * 手机号
     */
    @TableField(value = "com_phone")
    private String comPhone;

    /**
     * 企业介绍
     */
    @TableField(value = "com_intro")
    private String comIntro;

    /**
     * 企业地址
     */
    @TableField(value = "com_address")
    private String comAddress;

    /**
     * 企业人数
     */
    @TableField(value = "com_num")
    private String comNum;

    /**
     * 企业类型
     */
    @TableField(value = "com_type")
    private String comType;

    /**
     * 企业工作时间
     */
    @TableField(value = "com_work_time")
    private String comWorkTime;

    /**
     * 企业成立时间
     */
    @TableField(value = "com_set_time")
    private String comSetTime;

    /**
     * 企业福利
     */
    @TableField(value = "com_welfare")
    private String comWelfare;

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
}