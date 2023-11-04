package com.bin.bin_fresh_recruit_backend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.*;

/**
 * UUID生成
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/4 23:34
 */
public class IdUtils {
    /**
     * 生成ID
     *
     * @param role 角色代码 0-管理员 1-应届生 2-企业
     * @return ID
     */
    public static String getId(int role) {
        // 标识+UUID+分秒 （1+5+6）
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "");
        String uuidSub = uuidStr.substring(uuidStr.length() - 5);
        String date = sdf.format(new Date());
        String id;
        switch (role) {
            case SCHOOL_ROLE:
                id = "O" + uuidSub + date;
                break;
            case FRESH_ROLE:
                id = "C" + uuidSub + date;
                break;
            case COMPANY_ROLE:
                id = "B" + uuidSub + date;
                break;
            default:
                id = "错误ID";
        }
        return id;
    }
}
