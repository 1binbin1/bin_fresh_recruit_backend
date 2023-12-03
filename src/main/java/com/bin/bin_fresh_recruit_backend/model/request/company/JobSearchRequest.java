package com.bin.bin_fresh_recruit_backend.model.request.company;

import com.bin.bin_fresh_recruit_backend.common.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位搜索请求
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/3 11:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobSearchRequest extends PageRequest {

    /**
     * 搜索内容
     */
    @JsonProperty("search_content")
    private String searchContent;


    /**
     * 岗位类别
     */
    @JsonProperty("job_type")
    private String jobType;

    /**
     * 企业地址
     */
    @JsonProperty("com_address")
    private String comAddress;

    /**
     * 企业人数
     */
    @JsonProperty("com_num")
    private String comNum;

    /**
     * 企业类型
     */
    @JsonProperty("com_type")
    private String comType;
}
