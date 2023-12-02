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
public class JobInfoAddRequest implements Serializable {
    private static final long serialVersionUID = 7313794593576633889L;
    /**
     * 岗位名称
     */
    @JsonProperty("job_name")
    private String jobName;

    /**
     * 岗位类别
     */
    @JsonProperty("job_type")
    private String jobType;

    /**
     * 岗位介绍
     */
    @JsonProperty("job_intro")
    private String jobIntro;

    /**
     * 岗位职责
     */
    @JsonProperty("job_require")
    private String jobRequire;

    /**
     * 岗位薪资
     */
    @JsonProperty("job_pay")
    private String jobPay;
}