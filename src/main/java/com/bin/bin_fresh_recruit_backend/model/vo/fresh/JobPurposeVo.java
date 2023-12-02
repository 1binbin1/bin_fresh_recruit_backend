package com.bin.bin_fresh_recruit_backend.model.vo.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应届生岗位意向响应表
 *
 * @author hongxiaobin
 */
@Data
public class JobPurposeVo implements Serializable {
    private static final long serialVersionUID = 2120844512494953406L;

    /**
     * id
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * 应届生ID
     */
    @JsonProperty("user_id")
    private String userId;

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

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonProperty("update_time")
    private Date updateTime;
}