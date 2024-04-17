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

    @JsonProperty(value = "start")
    private Integer start = 1;

    @JsonProperty(value = "end")
    private Integer end = 1;
}
