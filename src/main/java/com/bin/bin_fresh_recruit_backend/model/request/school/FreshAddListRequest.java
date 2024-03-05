package com.bin.bin_fresh_recruit_backend.model.request.school;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应届生管理请求对象
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/4 22:09
 */
@Data
public class FreshAddListRequest implements Serializable {
    private static final long serialVersionUID = -3328874506761688433L;

    /**
     * 应届生ID
     */
    @JsonProperty(value = "fresh_ids")
    private String[] freshIds;
}
