package com.bin.bin_fresh_recruit_backend.model.vo.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 企业信息表
 *
 * @author hongxiaobin
 * @TableName t_company_info
 */
@Data
public class CompanyInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    @JsonProperty("com_id")
    private String comId;

    /**
     * 企业名称
     */
    @JsonProperty("com_name")
    private String comName;

    /**
     * 手机号
     */
    @JsonProperty("com_phone")
    private String comPhone;

    /**
     * 企业介绍
     */
    @JsonProperty("com_intro")
    private String comIntro;

    /**
     * 企业地址
     */
    @JsonProperty("com_address")
    private String comAddress;

    /**
     * 企业人数
     */
    @JsonProperty("com_num")
    private String comNum;

    /**
     * 企业类型
     */
    @JsonProperty("com_type")
    private String comType;

    /**
     * 企业工作时间
     */
    @JsonProperty("com_work_time")
    private String comWorkTime;

    /**
     * 企业成立时间
     */
    @JsonProperty("com_set_time")
    private String comSetTime;

    /**
     * 企业福利
     */
    @JsonProperty("com_welfare")
    private String comWelfare;
}