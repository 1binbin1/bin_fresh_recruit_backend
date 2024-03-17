package com.bin.bin_fresh_recruit_backend.model.vo.school;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;

/**
 * 应届生就业相关数据
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/5 16:09
 */
@Data
@AllArgsConstructor
public class SchoolRateVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签
     */
    @JsonProperty("label")
    private String label;

    /**
     * 数值
     */
    @JsonProperty("num_data")
    private String numData;
}
