package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.AccountMapper;
import com.bin.bin_fresh_recruit_backend.mapper.CompanyInfoMapper;
import com.bin.bin_fresh_recruit_backend.mapper.FreshUserInfoMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.CompanyInfo;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshUserInfo;
import com.bin.bin_fresh_recruit_backend.model.vo.account.AccountInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.*;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.USER_LOGIN_STATE;

/**
 * @author hongxiaobin
 * @description 针对表【t_account(账号信息表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
        implements AccountService {
    @Resource
    private AccountMapper accountMapper;

    @Resource
    private FreshUserInfoMapper freshUserInfoMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Override
    @Transactional
    public AccountInfoVo accountRegister(String phone, String password, String checkPassword, Integer role) {
        // 参数校验
        String pattern = "^1[3456789]\\d{9}$";
        if (!Pattern.matches(pattern, phone)) {
            throw new BusinessException(ErrorCode.PHONE_ERROR);
        }
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_phone", phone);
        accountQueryWrapper.eq("a_role", role);
        Long count = accountMapper.selectCount(accountQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.ACCOUNT_ERROR);
        }
        // 加密并生成ID
        String digestPassword = DigestUtils.md5DigestAsHex((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8));
        String id = IdUtils.getId(role);
        // 保存账号
        int insertResult = 0;
        boolean saveResult;
        Account account = new Account();
        account.setAId(id);
        account.setARole(role);
        account.setAPhone(phone);
        account.setAPassword(digestPassword);
        saveResult = this.save(account);
        // 保存信息
        switch (role) {
            case SCHOOL_ROLE:
                break;
            case FRESH_ROLE:
                // 应届生
                FreshUserInfo freshUserInfo = new FreshUserInfo();
                freshUserInfo.setUserId(id);
                freshUserInfo.setUserPhone(phone);
                insertResult = freshUserInfoMapper.insert(freshUserInfo);
                break;
            case COMPANY_ROLE:
                // 企业
                CompanyInfo companyInfo = new CompanyInfo();
                companyInfo.setComId(id);
                companyInfo.setComPhone(phone);
                insertResult = companyInfoMapper.insert(companyInfo);
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "角色代码错误");
        }
        if (insertResult == 0 || saveResult) {
            throw new BusinessException(ErrorCode.INSERT_ERROR);
        }
        return new AccountInfoVo(id, phone);
    }

    @Override
    public AccountInfoVo accountLogin(String phone, String password, Integer role, HttpServletRequest request) {
        // 参数校验
        String pattern = "^1[3456789]\\d{9}$";
        if (!Pattern.matches(pattern, phone)) {
            throw new BusinessException(ErrorCode.PHONE_ERROR);
        }
        String digestPassword = DigestUtils.md5DigestAsHex((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8));
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_phone", phone);
        accountQueryWrapper.eq("a_password", digestPassword);
        accountQueryWrapper.eq("a_role", role);
        Account account = accountMapper.selectOne(accountQueryWrapper);
        if (account == null) {
            log.info("User login error,phone password or role error");
            throw new BusinessException(ErrorCode.ACCOUNTNOT_ERROR);
        }
        // 记录登录态
        AccountInfoVo accountInfoVo = new AccountInfoVo(account.getAId(), account.getAPhone());
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, accountInfoVo);
        return accountInfoVo;
    }
}




