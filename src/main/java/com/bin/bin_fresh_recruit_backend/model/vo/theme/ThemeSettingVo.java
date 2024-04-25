package com.bin.bin_fresh_recruit_backend.model.vo.theme;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: hongxiaobin
 * @Time: 2024/4/25 10:36
 */
@Data
public class ThemeSettingVo {
    /**
     * 账号ID
     */
    @JsonProperty(value = "a_id")
    private String aId;

    /**
     * 主题颜色
     */
    @JsonProperty(value = "ts_theme_color")
    private String tsThemeColor;

    /**
     * 导航栏选中颜色
     */
    @JsonProperty(value = "ts_theme_active_color")
    private String tsThemeActiveColor;
}
