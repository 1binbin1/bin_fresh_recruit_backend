package com.bin.bin_fresh_recruit_backend.model.request.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 岗位意向请求
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/2 15:28
 */
@Data
public class PurposeRequest {
    /**
     * 意向岗位ID
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * 意向城市
     */
    @JsonProperty("city")
    private String city;

    /**
     * 意向工作类别
     */
    @JsonProperty("job_type")
    private String jobType;

    /**
     * 意向薪资
     */
    @JsonProperty("job_pay")
    private String jobPay;
}
