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
public class AddChatByComRequest extends ChatCommonRequest implements Serializable {
    private static final long serialVersionUID = 2558313967702570065L;

    @JsonProperty("user_id")
    private String userId;

}
