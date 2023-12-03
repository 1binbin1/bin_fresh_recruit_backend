package com.bin.bin_fresh_recruit_backend.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页响应
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/3 11:22
 */
@Data
public class PageVo<T> {
    /**
     * 列表
     */
    @JsonProperty("list")
    private List<T> list;

    /**
     * 总数
     */
    @JsonProperty("total")
    private long total;


    /**
     * 当前页号
     */
    @JsonProperty("current")
    private long current = 1;

    /**
     * 页面大小
     */
    @JsonProperty("page_size")
    private long pageSize = 10;
}
