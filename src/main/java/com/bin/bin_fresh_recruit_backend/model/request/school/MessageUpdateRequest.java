package com.bin.bin_fresh_recruit_backend.model.request.school;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: hongxiaobin
 * @Time: 2024/3/15 15:12
 */
@Data
public class MessageUpdateRequest {
    /**
     * 资讯id
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * 标题
     */
    @JsonProperty("title")
    private String title;

    /**
     * 内容
     */
    @JsonProperty("intro_content")
    private String introContent;
}
