package com.bin.bin_fresh_recruit_backend.model.request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: hongxiaobin
 * @Time: 2024/1/6 11:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddChatByFreshRequest extends ChatCommonRequest implements Serializable {
    private static final long serialVersionUID = -1585151568027328366L;

    @JsonProperty("com_id")
    private String comId;

}

