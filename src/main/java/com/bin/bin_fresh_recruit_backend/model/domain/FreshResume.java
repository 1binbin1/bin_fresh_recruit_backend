package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 应届生附件简历表
 * @author hongxiaobin
 * @TableName t_fresh_resume
 */
@TableName(value ="t_fresh_resume")
@Data
public class FreshResume implements Serializable {
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