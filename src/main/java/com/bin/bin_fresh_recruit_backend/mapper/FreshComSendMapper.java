package com.bin.bin_fresh_recruit_backend.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshComSend;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobSendVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Wrapper;

/**
 * @author hongxiaobin
 * @description 针对表【t_fresh_com_send(应届生投递记录表)】的数据库操作Mapper
 * @createDate 2023-12-02 17:06:05
 * @Entity com.bin.bin_fresh_recruit_backend.model.domain.TFreshComSend
 */
public interface FreshComSendMapper extends BaseMapper<FreshComSend> {
}




