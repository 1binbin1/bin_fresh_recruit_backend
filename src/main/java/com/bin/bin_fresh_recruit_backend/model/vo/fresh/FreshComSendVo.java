package com.bin.bin_fresh_recruit_backend.model.vo.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hongxiaobin
 */
@Data
public class FreshComSendVo implements Serializable {
    private static final long serialVersionUID = -7482360869003076357L;

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
     * 企业ID
     */
    @JsonProperty("com_id")
    private String comId;

    /**
     * 岗位ID
     */
    @JsonProperty("job_id")
    private String jobId;

    /**
     * 简历id
     */
    @JsonProperty("resume_id")
    private String resumeId;

    /**
     * 发送时间
     */
    @JsonProperty("send_time")
    private Date sendTime;

    /**
     * 投递状态 0-已投递 1-被查看 2-邀约面试 3-初筛不通过 4-流程结束
     */
    @JsonProperty("send_state")
    private Integer sendState;

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
