package com.bin.bin_fresh_recruit_backend.model.request.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 岗位信息表
 *
 * @author hongxiaobin
 * @TableName t_job_info
 */
@Data
public class JobInfoIdRequest implements Serializable {
    private static final long serialVersionUID = 7313794593576633889L;

    /**
     * 岗位ID
     */
    @JsonProperty("job_id")
    private String jobId;
}