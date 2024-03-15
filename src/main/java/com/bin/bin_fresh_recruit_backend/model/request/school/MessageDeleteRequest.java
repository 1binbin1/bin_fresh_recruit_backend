package com.bin.bin_fresh_recruit_backend.model.request.school;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: hongxiaobin
 * @Time: 2024/3/15 15:12
 */
@Data
public class MessageDeleteRequest {
    /**
     * 资讯id
     */
    @JsonProperty("id")
    private Integer id;
}
