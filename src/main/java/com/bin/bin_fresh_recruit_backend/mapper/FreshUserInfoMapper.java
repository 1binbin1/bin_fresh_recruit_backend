package com.bin.bin_fresh_recruit_backend.mapper;

import com.bin.bin_fresh_recruit_backend.model.domain.FreshUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.JobInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author hongxiaobin
* @description 针对表【t_fresh_user_info(应届生个人信息表)】的数据库操作Mapper
* @createDate 2023-11-04 15:34:12
* @Entity com.bin.bin_fresh_recruit_backend.model.domain.TFreshUserInfo
*/
public interface FreshUserInfoMapper extends BaseMapper<FreshUserInfo> {
    @MapKey("userId")
    @Select({
            "<script>",
            "select",
            "*",
            "from t_fresh_user_info",
            "where user_id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"})
    Map<String, FreshUserInfo> getFreshUserInfo(@Param("ids") List<String> ids);
}




