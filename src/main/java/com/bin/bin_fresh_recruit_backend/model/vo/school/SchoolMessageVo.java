package com.bin.bin_fresh_recruit_backend.model.vo.school;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: hongxiaobin
 * @Time: 2024/3/15 14:51
 */
@Data
public class SchoolMessageVo {
    /**
     * id
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * 学校ID
     */
    @JsonProperty("school_id")
    private String schoolId;

    /**
     * 标题
     */
    @JsonProperty("title")
    private String title;

    /**
     * 资讯内容
     */
    @JsonProperty("intro_content")
    private String introContent;

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonProperty("update_time")
    private Date updateTime;
}
