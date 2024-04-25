package com.bin.bin_fresh_recruit_backend.service;

import com.bin.bin_fresh_recruit_backend.model.domain.ThemeSetting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.vo.theme.ThemeSettingVo;

/**
* @author hongxiaobin
* @description 针对表【t_theme_setting(主题设置)】的数据库操作Service
* @createDate 2024-04-25 10:34:55
*/
public interface ThemeSettingService extends IService<ThemeSetting> {

    ThemeSettingVo saveTheme(String aId, Integer themeType, String color);

    ThemeSettingVo getTheme(String aId);
}
