package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.model.domain.TJobInfo;
import com.bin.bin_fresh_recruit_backend.service.TJobInfoService;
import com.bin.bin_fresh_recruit_backend.mapper.TJobInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author hongxiaobin
* @description 针对表【t_job_info(岗位信息表)】的数据库操作Service实现
* @createDate 2023-11-04 15:34:12
*/
@Service
public class TJobInfoServiceImpl extends ServiceImpl<TJobInfoMapper, TJobInfo>
    implements TJobInfoService{

}




