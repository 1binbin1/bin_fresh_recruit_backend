package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.CompanyInfoMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.CompanyInfo;
import com.bin.bin_fresh_recruit_backend.model.request.company.CompanyInfoRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.company.CompanyInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.company.CompanyVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.CompanyInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.regex.Pattern;

import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.COM_LOGIN_STATE;

/**
 * @author hongxiaobin
 * @description 针对表【t_company_info(企业信息表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class CompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo>
        implements CompanyInfoService {
    @Resource
    private AccountService accountService;

    /**
     * 更新企业信息
     *
     * @param request            登录态
     * @param companyInfoRequest 请求参数
     * @return 更新后的企业信息
     */
    @Override
    @Transactional(rollbackFor = SQLException.class)
    public CompanyInfoVo updateCompanyInfo(HttpServletRequest request, CompanyInfoRequest companyInfoRequest) {
        Account loginInfo = accountService.getLoginInfo(request, COM_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String comPhone = companyInfoRequest.getComPhone();
        // 校验手机号
        String pattern = "^1[3456789]\\d{9}$";
        if (!Pattern.matches(pattern, comPhone)) {
            throw new BusinessException(ErrorCode.PHONE_ERROR);
        }
        // 更新企业信息
        CompanyInfo companyInfo = new CompanyInfo();
        QueryWrapper<CompanyInfo> companyInfoQueryWrapper = new QueryWrapper<>();
        String comId = loginInfo.getAId();
        companyInfoQueryWrapper.eq("com_id", comId);
        boolean update = this.update(companyInfo, companyInfoQueryWrapper);
        if (!update) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        // 更新账号信息中的的手机号
        Account account = new Account();
        account.setAPhone(comPhone);
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_id", comId);
        boolean updateAccount = accountService.update(account, accountQueryWrapper);
        if (!updateAccount) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        CompanyInfoVo companyInfoVo = new CompanyInfoVo();
        BeanUtils.copyProperties(companyInfo, companyInfoVo);
        return companyInfoVo;
    }

    /**
     * 获取企业信息
     * @param comId 企业id
     * @return 企业信息
     */
    @Override
    public CompanyVo getCompanyInfo(String comId) {
        // 企业信息
        QueryWrapper<CompanyInfo> companyInfoQueryWrapper = new QueryWrapper<>();
        companyInfoQueryWrapper.eq("com_id", comId);
        CompanyInfo companyInfo = this.getOne(companyInfoQueryWrapper);
        CompanyVo companyVo = new CompanyVo();
        BeanUtils.copyProperties(companyInfo, companyVo);
        // 补充头像
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_id", comId);
        Account account = accountService.getOne(accountQueryWrapper);
        String aAvatar = account.getAAvatar();
        if (aAvatar != null) {
            companyVo.setAAvatar(aAvatar);
        }
        return companyVo;
    }
}




