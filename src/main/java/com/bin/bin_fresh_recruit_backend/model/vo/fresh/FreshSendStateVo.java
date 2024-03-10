package com.bin.bin_fresh_recruit_backend.model.vo.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: hongxiaobin
 * @Time: 2024/3/11 0:02
 */
@Data
public class FreshSendStateVo implements Serializable {
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
     * 岗位名称
     */
    @JsonProperty("job_name")
    public String jobName;

    /**
     * 企业名称
     */
    @JsonProperty("com_name")
    public String comName;

    /**
     * 企业头像
     */
    @JsonProperty("a_avatar")
    public String aAvatar;

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
