package com.bin.bin_fresh_recruit_backend.model.request.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 简历筛选
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/2 16:52
 */
@Data
public class ResumeFiltrateRequest implements Serializable {
    private static final long serialVersionUID = 444219453644207092L;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("job_id")
    private String jobId;

    /**
     * 简历状态
     */
    @JsonProperty("send_state")
    private Integer sendState;
}
