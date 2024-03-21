package com.bin.bin_fresh_recruit_backend.controller.freshController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.LoginUser;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolIntroVo;
import com.bin.bin_fresh_recruit_backend.service.SchoolIntroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 获取资讯列表
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/21 12:04
 */
@Slf4j
@RestController
@RequestMapping("/fresh/message")
public class FreshMessageController {
    @Resource
    private SchoolIntroService schoolIntroService;

    @LoginUser
    @GetMapping("/get")
    public BaseResponse<PageVo<SchoolIntroVo>> getMessage(HttpServletRequest request, @RequestParam("current") Integer current, @RequestParam("page_size") Integer pageSize) {
        if (current == 0) {
            current = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        PageVo<SchoolIntroVo> result = schoolIntroService.getByFresh(request, current, pageSize);
        return ResultUtils.success(result);
    }
}
