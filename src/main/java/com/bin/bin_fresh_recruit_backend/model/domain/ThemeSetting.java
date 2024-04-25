package com.bin.bin_fresh_recruit_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 主题设置
 * @TableName t_theme_setting
 */
@TableName(value ="t_theme_setting")
@Data
public class ThemeSetting implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号ID
     */
    @TableField(value = "a_id")
    private String aId;

    /**
     * 主题颜色
     */
    @TableField(value = "ts_theme_color")
    private String tsThemeColor;

    /**
     * 导航栏选中颜色
     */
    @TableField(value = "ts_theme_active_color")
    private String tsThemeActiveColor;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除，0-否，1-是
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}