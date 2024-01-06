package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 就业咨询信息表
 *
 * @TableName t_school_intro
 */
@TableName(value = "t_school_intro")
@Data
public class SchoolIntro implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 资讯内容
     */
    @TableField(value = "intro_content")
    private String introContent;

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