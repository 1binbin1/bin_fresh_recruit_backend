package com.bin.bin_fresh_recruit_backend.model.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: hongxiaobin
 * @Time: 2024/1/7 15:53
 */
@Data
public class AccountLoginOutRequest implements Serializable {
    @JsonProperty("role")
    Integer role;

}
