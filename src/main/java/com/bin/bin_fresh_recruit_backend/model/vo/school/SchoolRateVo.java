package com.bin.bin_fresh_recruit_backend.model.vo.school;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应届生就业相关数据
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/5 16:09
 */
@Data
public class SchoolRateVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "school_id")
    private String schoolId;

    /**
     * 总学生数
     */
    @JsonProperty(value = "fresh_total_num")
    private Long freshTotalNum;

    /**
     * 已投递学生数
     */
    @JsonProperty(value = "fresh_send_have_num")
    private Long freshSendHaveNum;

    /**
     * 被查看学生数
     */
    @JsonProperty(value = "fresh_send_looked_num")
    private Long freshSendLookedNum;

    /**
     * 被面试邀请学生数
     */
    @JsonProperty(value = "fresh_send_invited_num")
    private Long freshSendInvitedNum;

    /**
     * 初筛不通过学生数
     */
    @JsonProperty(value = "fresh_send_no_pass_num")
    private Long freshSendNoPassNum;

    /**
     * 流程终止学生数
     */
    @JsonProperty(value = "fresh_send_finish_num")
    private Long freshSendFinishNum;

    /**
     * 应聘成功学生数
     */
    @JsonProperty(value = "fresh_send_success_num")
    private Long freshSendSuccessNum;

    /**
     * 就业率   应聘成功/总人数*100%
     */
    @JsonProperty(value = "employment_rate")
    private float employmentRate;
}
