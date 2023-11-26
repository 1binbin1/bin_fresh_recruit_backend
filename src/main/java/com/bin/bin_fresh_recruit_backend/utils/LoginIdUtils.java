package com.bin.bin_fresh_recruit_backend.utils;

import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;

import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.*;

/**
 * @Author: hongxiaobin
 * @Time: 2023/11/26 18:07
 */
public class LoginIdUtils {
    public static String getSessionId(Integer role) {
        String sessionId;
        switch (role) {
            case 0:
                sessionId = SCHOOL_LOGIN_STATE;
                break;
            case 1:
                sessionId = USER_LOGIN_STATE;
                break;
            case 2:
                sessionId = COM_LOGIN_STATE;
                break;
            default:
                throw new BusinessException(ErrorCode.ROLE_ERROR);
        }
        return sessionId;
    }
}
