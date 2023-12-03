/*
 * com.binbin.binapicommon.common.PageRequest, 2023-07-12
 * Copyright© 2023 hongxiaobin(1binbin),Inc. All rights reserved.
 * Github link : http://github.com/1binbin
 */

package com.bin.bin_fresh_recruit_backend.common;


import com.bin.bin_fresh_recruit_backend.constant.SortConstant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * @author hongxiaobin
 * @Time: 2023/11/4 16:22
 */
@Data
public class PageRequest {

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

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = SortConstant.SORT_ORDER_ASC;
}
