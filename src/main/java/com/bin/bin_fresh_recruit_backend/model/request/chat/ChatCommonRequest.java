package com.bin.bin_fresh_recruit_backend.model.request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: hongxiaobin
 * @Time: 2024/1/6 11:29
 */
@Data
public class ChatCommonRequest {
    @JsonProperty("content")
    private String content;
}
