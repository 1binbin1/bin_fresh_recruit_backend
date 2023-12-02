package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 应届生投递记录表
 * @author hongxiaobin
 * @TableName t_fresh_com_send
 */
@TableName(value ="t_fresh_com_send")
@Data
public class FreshComSend implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应届生ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 企业ID
     */
    @TableField(value = "com_id")
    private String comId;

    /**
     * 岗位ID
     */
    @TableField(value = "job_id")
    private String jobId;

    /**
     * 简历id
     */
    @TableField(value = "resume_id")
    private String resumeId;

    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    private Date sendTime;

    /**
     * 投递状态 0-已投递 1-被查看 2-邀约面试 3-初筛不通过 4-流程结束
     */
    @TableField(value = "send_state")
    private Integer sendState;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除，0-否，1-是
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}