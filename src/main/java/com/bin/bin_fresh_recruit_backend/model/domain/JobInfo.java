package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 岗位信息表
 * @TableName t_job_info
 */
@TableName(value ="t_job_info")
@Data
public class JobInfo implements Serializable {
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
     * 岗位ID
     */
    @TableField(value = "job_id")
    private String jobId;

    /**
     * 岗位名称
     */
    @TableField(value = "job_name")
    private String jobName;

    /**
     * 岗位类别
     */
    @TableField(value = "job_type")
    private String jobType;

    /**
     * 岗位介绍
     */
    @TableField(value = "job_intro")
    private String jobIntro;

    /**
     * 岗位职责
     */
    @TableField(value = "job_require")
    private String jobRequire;

    /**
     * 岗位薪资
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