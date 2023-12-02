package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.CompanyInfo;
import com.bin.bin_fresh_recruit_backend.model.request.company.CompanyInfoRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.company.CompanyInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.company.CompanyVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hongxiaobin
 * @description 针对表【t_company_info(企业信息表)】的数据库操作Service
 * @createDate 2023-11-04 15:34:12
 */
public interface CompanyInfoService extends IService<CompanyInfo> {

    /**
     * 修改企业信息
     *
     * @param request            登录态
     * @param companyInfoRequest 请求参数
     * @return 请求响应
     */
    CompanyInfoVo updateCompanyInfo(HttpServletRequest request, CompanyInfoRequest companyInfoRequest);

    /**
     * 获取企业信息
     *
     * @param comId 企业id
     * @return 企业信息
     */
    CompanyVo getCompanyInfo(String comId);
}
