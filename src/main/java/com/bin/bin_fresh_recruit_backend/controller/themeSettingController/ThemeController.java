package com.bin.bin_fresh_recruit_backend.controller.themeSettingController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.IgnoreAuth;
import com.bin.bin_fresh_recruit_backend.model.vo.theme.ThemeSettingVo;
import com.bin.bin_fresh_recruit_backend.service.ThemeSettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: hongxiaobin
 * @Time: 2024/4/25 10:43
 */
@RestController
@Slf4j
@RequestMapping("/theme")
public class ThemeController {
    @Resource
    private ThemeSettingService themeSettingService;

    /**
     * 保存主题
     *
     * @param aId
     * @return
     */
    @IgnoreAuth
    @GetMapping("/save")
    public BaseResponse<ThemeSettingVo> saveTheme(@RequestParam("a_id") String aId, @RequestParam("theme_type") Integer themeType, @RequestParam("color") String color) {
        if (StringUtils.isAnyBlank(aId, color)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ThemeSettingVo result = themeSettingService.saveTheme(aId, themeType, color);
        return ResultUtils.success(result);
    }

    @IgnoreAuth
    @GetMapping("get")
    public BaseResponse<ThemeSettingVo> getTheme(@RequestParam("a_id") String aId){
        if (StringUtils.isAnyBlank(aId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ThemeSettingVo result = themeSettingService.getTheme(aId);
        return ResultUtils.success(result);
    }
}
