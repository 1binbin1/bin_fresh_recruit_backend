package com.bin.bin_fresh_recruit_backend.model.request.school;

import com.bin.bin_fresh_recruit_backend.common.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: hongxiaobin
 * @Time: 2024/3/15 14:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageListRequest extends PageRequest {
    @JsonProperty("search_content")
    private String searchContent;
}
