package com.bin.bin_fresh_recruit_backend.model.vo.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * 企业查询岗位投递记录
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/10 21:51
 */
@Data
public class JobSendVo {
    /**
     * 应届生ID
     */
    @JsonProperty("user_id")
    public String userId;

    /**
     * 企业ID
     */
    @JsonProperty("com_id")
    public String comId;

    /**
     * 岗位ID
     */
    @JsonProperty("job_id")
    public String jobId;

    /**
     * 简历ID
     */
    @JsonProperty("resume_id")
    public String resumeId;

    /**
     * 应届生昵称
     */
    @JsonProperty("user_name")
    public String userName;

    /**
     * 岗位名称
     */
    @JsonProperty("job_name")
    public String jobName;

    /**
     * 简历链接
     */
    @JsonProperty("user_name_link")
    public String userNameLink;

    /**
     * 简历名称
     */
    @JsonProperty("resume_name")
    public String resumeName;

    /**
     * 投递状态
     */
    @JsonProperty("send_state")
    public Integer sendState;

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    private Date createTime;
}
