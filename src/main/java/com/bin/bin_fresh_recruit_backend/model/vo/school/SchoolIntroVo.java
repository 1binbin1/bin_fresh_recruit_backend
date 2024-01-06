package com.bin.bin_fresh_recruit_backend.model.vo.school;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资讯发布响应体
 *
 * @author hongxiaobin
 * @TableName t_school_intro
 */
@Data
public class SchoolIntroVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @JsonProperty(value = "id")
    private Integer id;

    /**
     * 标题
     */
    @JsonProperty(value = "title")
    private String title;

    /**
     * 资讯内容
     */
    @JsonProperty(value = "intro_content")
    private String introContent;

    /**
     * 创建时间
     */
    @JsonProperty(value = "create_time")
    private Date createTime;
    
}