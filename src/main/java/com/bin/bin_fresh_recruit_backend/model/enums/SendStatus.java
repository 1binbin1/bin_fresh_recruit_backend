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
    Integer SendStatusSuccess = 0;

    /**
     * 被查看
     */
    Integer SendStatusLooked = 1;

    /**
     * 邀约面试
     */
    Integer SendStatusInvited = 2;

    /**
     * 初筛不通过
     */
    Integer SendStatusNoPass = 3;

    /**
     * 流程结束
     */
    Integer SendStatusFinish = 4;

}
