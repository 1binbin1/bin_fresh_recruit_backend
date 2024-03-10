package com.bin.bin_fresh_recruit_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.JobInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author hongxiaobin
 * @description 针对表【t_job_info(岗位信息表)】的数据库操作Mapper
 * @createDate 2023-11-04 15:34:12
 * @Entity com.bin.bin_fresh_recruit_backend.model.domain.TJobInfo
 */
public interface JobInfoMapper extends BaseMapper<JobInfo> {
    @MapKey("jobId")
    @Select({
            "<script>",
            "select",
            "*",
            "from t_job_info",
            "where job_id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"})
    Map<String, JobInfo> getJobInfo(@Param("ids") List<String> ids);
}




