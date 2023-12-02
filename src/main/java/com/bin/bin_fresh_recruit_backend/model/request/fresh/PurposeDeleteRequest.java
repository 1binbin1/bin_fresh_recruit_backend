package com.bin.bin_fresh_recruit_backend.model.request.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 删除岗位意向请求
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/2 15:28
 */
@Data
public class PurposeDeleteRequest {
    /**
     * 意向岗位ID
     */
    @JsonProperty("id")
    private Integer id;
}
