package com.bin.bin_fresh_recruit_backend.model.request.school;

import com.bin.bin_fresh_recruit_backend.constant.RequestConstant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 发布资讯请求
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/18 17:46
 */
@Data
public class PublishMessageRequest implements Serializable {

    private static final long serialVersionUID = -3328874506761688433L;

    @JsonProperty(value = "title")
    @Size(max = RequestConstant.TITLE_MAX_LENGTH)
    private String title;

    @JsonProperty(value = "message")
    @Size(max = RequestConstant.TEXT_MAX_LENGTH)
    private String message;
}
