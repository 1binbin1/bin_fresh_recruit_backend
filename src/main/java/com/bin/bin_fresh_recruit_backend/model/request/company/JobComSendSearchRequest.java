package com.bin.bin_fresh_recruit_backend.model.request.company;

import com.bin.bin_fresh_recruit_backend.common.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
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
public class JobComSendSearchRequest extends PageRequest {

    /**
     * 投递状态
     */
    @JsonProperty("send_status")
    private Integer sendStatus = -1;

    /**
     * 岗位名称
     */
    @JsonProperty("job_name")
    private String jobName;
}
