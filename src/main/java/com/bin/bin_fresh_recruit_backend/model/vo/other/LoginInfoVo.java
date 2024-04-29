package com.bin.bin_fresh_recruit_backend.model.vo.other;

import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: hongxiaobin
 * @Time: 2024/4/29 17:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginInfoVo<T> extends PageVo<T> {
    /**
     * 账号ID
     */
    @JsonProperty("a_id")
    private String aId;

    /**
     * 账号评分
     */
    @JsonProperty("score")
    private Integer score;
}
