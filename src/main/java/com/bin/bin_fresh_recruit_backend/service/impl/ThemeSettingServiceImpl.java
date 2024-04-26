package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.TThemeSettingMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.ThemeSetting;
import com.bin.bin_fresh_recruit_backend.model.vo.theme.ThemeSettingVo;
import com.bin.bin_fresh_recruit_backend.service.ThemeSettingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author hongxiaobin
 * @description 针对表【t_theme_setting(主题设置)】的数据库操作Service实现
 * @createDate 2024-04-25 10:34:55
 */
@Service
public class ThemeSettingServiceImpl extends ServiceImpl<TThemeSettingMapper, ThemeSetting>
        implements ThemeSettingService {

    @Override
    public ThemeSettingVo saveTheme(String aId, Integer themeType, String color) {
        // 不存在创建，存在更新
        QueryWrapper<ThemeSetting> themeSettingQueryWrapper = new QueryWrapper<>();
        themeSettingQueryWrapper.eq("a_id", aId);
        ThemeSetting themeSetting = this.getOne(themeSettingQueryWrapper);
        ThemeSetting setting = new ThemeSetting();
        boolean save;
        if (themeSetting == null) {
            setting.setAId(aId);
            save = this.save(setting);
        } else {
            // 更新
            QueryWrapper<ThemeSetting> saveQw = new QueryWrapper<>();
            ThemeSetting newTheme = new ThemeSetting();
            saveQw.eq("a_id", aId);
            switch (themeType) {
                case 1:
                    newTheme.setTsThemeColor(color);
                    break;
                case 2:
                    newTheme.setTsThemeActiveColor(color);
                    break;
                default:
                    throw new BusinessException(ErrorCode.NULL_ERROR);
            }
            save = this.update(newTheme,saveQw);
        }
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        setting = this.getOne(themeSettingQueryWrapper);
        ThemeSettingVo result = new ThemeSettingVo();
        BeanUtils.copyProperties(setting, result);
        return result;
    }

    @Override
    public ThemeSettingVo getTheme(String aId) {
        QueryWrapper<ThemeSetting> themeSettingQueryWrapper = new QueryWrapper<>();
        themeSettingQueryWrapper.eq("a_id", aId);
        ThemeSetting themeSetting = this.getOne(themeSettingQueryWrapper);
        if (themeSetting == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        ThemeSettingVo result = new ThemeSettingVo();
        BeanUtils.copyProperties(themeSetting, result);
        return result;
    }
}




