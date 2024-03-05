package com.bin.bin_fresh_recruit_backend.model.vo.school;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: hongxiaobin
 * @Time: 2024/3/4 22:03
 */
@Data
public class FreshManageVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "school_id")
    private String schoolId;

    @JsonProperty(value = "fresh_id")
    private String freshId;
}
