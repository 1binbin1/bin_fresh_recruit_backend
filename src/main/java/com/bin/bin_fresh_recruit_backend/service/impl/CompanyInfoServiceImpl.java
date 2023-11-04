package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.model.domain.CompanyInfo;
import com.bin.bin_fresh_recruit_backend.service.CompanyInfoService;
import com.bin.bin_fresh_recruit_backend.mapper.CompanyInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author hongxiaobin
* @description 针对表【t_company_info(企业信息表)】的数据库操作Service实现
* @createDate 2023-11-04 15:34:12
*/
@Service
public class CompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo>
    implements CompanyInfoService {

}




