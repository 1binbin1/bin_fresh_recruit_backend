package com.bin.bin_fresh_recruit_backend.model.request.company;

import com.bin.bin_fresh_recruit_backend.common.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业岗位搜索请求
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/3 11:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobComSearchRequest extends PageRequest {

    /**
     * 搜索内容
     */
    @JsonProperty("search_content")
    private String searchContent;

    /**
     * 岗位Id
     */
    @JsonProperty("com_id")
    private String comId;
}
