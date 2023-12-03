package com.bin.bin_fresh_recruit_backend.config;

import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.vo.account.CodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 旦米发送验证码
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/3 19:15
 */
@Slf4j
@Component
@Configuration
public class PushMsgConfig {
    @Value("${danmi.accountId}")
    private String accountId;

    @Value("${danmi.accounSid}")
    private String accountSid;

    @Value("${danmi.authToken}")
    private String authToken;

    @Value("${danmi.danMiApi}")
    private String api;

    @Resource
    private RestTemplate restTemplate;

    /**
     * 发送短信
     *
     * @param phone 手机号
     * @param msg   短信内容
     * @return 是否成功
     */
    public Boolean pushMsg(String phone, String msg) {
        String pattern = "^1[3456789]\\d{9}$";
        if (!Pattern.matches(pattern, phone)) {
            throw new BusinessException(ErrorCode.PHONE_ERROR);
        }
        long timeMillis = System.currentTimeMillis();
        String sig = DigestUtils.md5DigestAsHex((accountSid + authToken + timeMillis).getBytes(StandardCharsets.UTF_8));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("accountSid", accountSid);
        params.add("smsContent", msg);
        params.add("to", phone);
        params.add("timestamp", String.valueOf(timeMillis));
        params.add("sig", sig);
        params.add("smsType", String.valueOf(100000));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<CodeVo> response = restTemplate.postForEntity(api, request, CodeVo.class);
        return response.getStatusCode().value() == 200 && Objects.equals(response.getBody().getRespCode(), "0000");
    }
}
