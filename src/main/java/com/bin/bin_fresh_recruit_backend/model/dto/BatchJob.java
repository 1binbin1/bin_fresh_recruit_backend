package com.bin.bin_fresh_recruit_backend.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: hongxiaobin
 * @Time: 2024/4/15 21:13
 */
@Data
public class BatchJob {
    @ExcelProperty(value = "岗位名称")
    private String jobName;

    @ExcelProperty(value = "岗位类别")
    private String jobType;

    @ExcelProperty(value = "岗位职责")
    private String jobIntro;

    @ExcelProperty(value = "岗位要求")
    private String jobRequire;

    @ExcelProperty(value = "岗位薪资")
    private String jobPay;
}
