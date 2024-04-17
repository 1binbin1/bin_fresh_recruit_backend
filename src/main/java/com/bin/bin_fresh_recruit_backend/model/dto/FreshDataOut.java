package com.bin.bin_fresh_recruit_backend.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @Author: hongxiaobin
 * @Time: 2024/4/17 1:47
 */
@Data
public class FreshDataOut {
    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 用户姓名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 性别 0-男 1-女
     */
    @TableField(value = "user_sex")
    private Integer userSex;

    /**
     * 用户手机号
     */
    @TableField(value = "user_phone")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_email")
    private String userEmail;

    /**
     * 专业
     */
    @TableField(value = "user_major")
    private String userMajor;

    /**
     * 岗位名称
     */
    @TableField(value = "job_name")
    private String jobName;

    /**
     * 岗位类别
     */
    @TableField(value = "job_type")
    private String jobType;

    /**
     * 企业名称
     */
    @TableField(value = "com_name")
    private String comName;

    /**
     * 投递时间
     */
    @TableField(value = "send_time")
    private Date sendTime;

    /**
     * 投递状态 0-已投递 1-被查看 2-邀约面试 3-初筛不通过 4-流程结束
     */
    @TableField(value = "send_state")
    private Integer sendState;
}
