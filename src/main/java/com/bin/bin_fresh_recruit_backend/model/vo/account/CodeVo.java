package com.bin.bin_fresh_recruit_backend.model.vo.account;

import lombok.Data;

/**
 * 发送短信响应
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/3 19:32
 */
@Data
public class CodeVo {
    private String respCode;

    private String respDesc;

    private String smsId;

    private String failList;
}
