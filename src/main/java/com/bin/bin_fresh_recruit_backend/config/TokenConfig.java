package com.bin.bin_fresh_recruit_backend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bin.bin_fresh_recruit_backend.constant.RedisConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Calendar;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.TOKEN_TIME;

/**
 * @Author: hongxiaobin
 * @Time: 2024/3/16 18:53
 */
@Configuration
@Component
public class TokenConfig {
    /**
     * 生成Token
     *
     * @param aId 账号ID
     * @return token
     */
    public String getToken(String aId, Integer role) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,TOKEN_TIME);
        String userRole;
        switch (role) {
            case 0:
                userRole = RedisConstant.SCHOOL_LOGIN_STATE;
                break;
            case 1:
                userRole = RedisConstant.USER_LOGIN_STATE;
                break;
            case 2:
                userRole = RedisConstant.COM_LOGIN_STATE;
                break;
            default:
                userRole = "";
        }
        String token = "";
        token = JWT.create().withAudience(aId+"-"+userRole).withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(userRole));
        return token;
    }
}
