package com.bin.bin_fresh_recruit_backend.model.vo.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: hongxiaobin
 * @Time: 2023/11/26 17:35
 */
@Data
public class ResumeInfoVo implements Serializable {
    private static final long serialVersionUID = 603050774483310740L;

    /**
     * 应届生id
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * 简历id
     */
    @JsonProperty("resume_id")
    private String resumeId;

    /**
     * 简历附件
     */
    @JsonProperty("user_name_link")
    private String userNameLink;

    /**
     * 简历名称
     */
    @JsonProperty("resume_name")
    private String resumeName;

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
