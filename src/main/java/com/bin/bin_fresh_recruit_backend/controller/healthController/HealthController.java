package com.bin.bin_fresh_recruit_backend.controller.healthController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 保活判断
 * @Author: hongxiaobin
 * @Time: 2024/3/9 20:17
 */
@RestController
@RequestMapping("/")
public class HealthController {
    @GetMapping("/health")
    public BaseResponse<String> checkHealth(){
        return ResultUtils.success("服务器正常，我还活着");
    }
}
