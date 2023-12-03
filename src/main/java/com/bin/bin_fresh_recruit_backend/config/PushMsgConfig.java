package com.bin.bin_fresh_recruit_backend.config;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

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
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.signName}")
    private String signName;

    @Value("${aliyun.templateCode}")
    private String templateCode;

    @Resource
    private RestTemplate restTemplate;

    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     * 发送短信
     *
     * @param phone 手机号
     * @param msg   短信内容
     * @return 是否成功
     */
    public Boolean pushMsg(String phone, String msg) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = createClient(accessKeyId, accessKeySecret);
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(signName)
                .setTemplateCode(templateCode).
                setTemplateParam("{\"code\":\"" + msg + "\"}");
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
            if (sendSmsResponse.getStatusCode() == 200 && "OK".equals(sendSmsResponse.body.message)) {
                return true;
            }
        } catch (TeaException error) {
            // 错误 message
            // System.out.println(error.getMessage());
            // 诊断地址
            // System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
            throw new BusinessException(ErrorCode.PUSH_CODE_ERROR, error.getMessage());
        } catch (Exception e) {
            TeaException error = new TeaException(e.getMessage(), e);
            // 错误 message
            // System.out.println(error.getMessage());
            // 诊断地址
            // System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
            throw new BusinessException(ErrorCode.PUSH_CODE_ERROR, e.getMessage());
        }
        return false;
    }
}
