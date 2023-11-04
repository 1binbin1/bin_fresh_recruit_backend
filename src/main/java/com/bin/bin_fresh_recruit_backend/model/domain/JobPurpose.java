package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 应届生岗位意向表
 * @author hongxiaobin
 * @TableName t_job_purpose
 */
@TableName(value ="t_job_purpose")
@Data
public class JobPurpose implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应届生ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 意向城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 意向工作类别
     */
    @TableField(value = "job_type")
    private String jobType;

    /**
     * 意向薪资
     */
    @TableField(value = "job_pay")
    private String jobPay;

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