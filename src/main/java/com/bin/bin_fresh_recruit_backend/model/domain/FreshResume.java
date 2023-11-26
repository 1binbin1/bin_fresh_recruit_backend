package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应届生附件简历表
 *
 * @author hongxiaobin
 * @TableName t_fresh_resume
 */
@TableName(value = "t_fresh_resume")
@Data
public class FreshResume implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应届生id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 简历id
     */
    @TableField(value = "resume_id")
    private String resumeId;

    /**
     * 简历附件
     */
    @TableField(value = "user_name_link")
    private String userNameLink;

    /**
     * 简历名称
     */
    @TableField(value = "resume_name")
    private String resumeName;

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