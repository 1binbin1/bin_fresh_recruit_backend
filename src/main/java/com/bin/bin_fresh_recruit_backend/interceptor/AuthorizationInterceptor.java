package com.bin.bin_fresh_recruit_backend.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author hongxiaobin
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private AccountService accountService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        //支持跨域请求
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,X-Nideshop-Token,X-URL-PATH");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));

        String token = httpServletRequest.getHeader("Authorization");


        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有IgnoreAuth注释，有则跳过认证
        if (method.isAnnotationPresent(IgnoreAuth.class)) {
            IgnoreAuth passToken = method.getAnnotation(IgnoreAuth.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(LoginUser.class)) {
            LoginUser userLoginToken = method.getAnnotation(LoginUser.class);
            if (userLoginToken != null) {
                // 执行认证
                if (token == null) {
                    throw new BusinessException(ErrorCode.NO_LOGIN);
                }
                // 获取 token 中的 user id
                String userInfo;
                String userId;
                String userRole;
                try {
                    userInfo = JWT.decode(token).getAudience().get(0);
                    String[] split = userInfo.split("-");
                    userId = split[0];
                    userRole = split[1];
                } catch (JWTDecodeException j) {
                    throw new BusinessException(ErrorCode.NO_LOGIN);
                }
                //设置userId到request里，后续根据userId，获取用户信息
                httpServletRequest.setAttribute(userRole, userId);
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userRole)).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new BusinessException(ErrorCode.NO_LOGIN);
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}