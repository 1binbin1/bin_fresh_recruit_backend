package com.bin.bin_fresh_recruit_backend.model.request.school;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: hongxiaobin
 * @Time: 2024/4/17 1:22
 */
@Data
public class FreshDataOutRequest {
    @JsonProperty(value = "send_state")
    private Integer[] sendState;
}
