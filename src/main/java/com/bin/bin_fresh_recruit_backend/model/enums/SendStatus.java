package com.bin.bin_fresh_recruit_backend.model.enums;

/**
 * 投递状态
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/2 17:16
 */
public interface SendStatus {
    /**
     * 已投递
     */
    Integer SEND_STATUS_HAVE = 0;

    /**
     * 被查看
     */
    Integer SEND_STATUS_LOOKED = 1;

    /**
     * 邀约面试
     */
    Integer SEND_STATUS_INVITED = 2;

    /**
     * 初筛不通过
     */
    Integer SEND_STATUS_NO_PASS = 3;

    /**
     * 流程结束
     */
    Integer SEND_STATUS_FINISH = 4;

    /**
     * 应聘成功
     */
    Integer SEND_STATUS_SUCCESS = 5;
}
