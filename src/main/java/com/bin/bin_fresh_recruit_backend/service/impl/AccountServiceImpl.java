package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.config.PushMsgConfig;
import com.bin.bin_fresh_recruit_backend.config.QiniuyunOSSConfig;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.AccountMapper;
import com.bin.bin_fresh_recruit_backend.mapper.CompanyInfoMapper;
import com.bin.bin_fresh_recruit_backend.mapper.FreshUserInfoMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.CompanyInfo;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshUserInfo;
import com.bin.bin_fresh_recruit_backend.model.request.account.AccountGetCodeRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.account.AccountInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.utils.IdUtils;
import com.bin.bin_fresh_recruit_backend.utils.LoginIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.regex.Pattern;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.*;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.*;

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

    @Resource
    private QiniuyunOSSConfig qiniuyunOssConfig;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private PushMsgConfig pushMsgConfig;

    /**
     * 账号注册
     *
     * @param phone         手机号
     * @param password      密码
     * @param checkPassword 确认密码
     * @param role          角色
     * @return AccountInfoVo
     */
    @Override
    @Transactional(rollbackFor = SQLException.class)
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
        if (insertResult == 0 || !saveResult) {
            throw new BusinessException(ErrorCode.INSERT_ERROR);
        }
        return new AccountInfoVo(id, phone, "");
    }

    /**
     * 账号登录
     *
     * @param phone    手机号
     * @param password 密码
     * @param role     角色
     * @param request  登录态
     * @return AccountInfoVo
     */
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
            throw new BusinessException(ErrorCode.LOGIN_ERROR);
        }
        // 记录登录态
        AccountInfoVo accountInfoVo = new AccountInfoVo(account.getAId(), account.getAPhone(), account.getAAvatar());
        HttpSession session = request.getSession();
        session.setAttribute(LoginIdUtils.getSessionId(role), accountInfoVo);
        return accountInfoVo;
    }

    /**
     * 忘记密码
     *
     * @param phone         手机号
     * @param password      密码
     * @param checkPassword 确认密码
     * @return AccountInfoVo
     */
    @Override
    public AccountInfoVo accountForget(String phone, String password, String checkPassword, Integer role, String code) {
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_phone", phone);
        accountQueryWrapper.eq("a_role", role);
        Long count = accountMapper.selectCount(accountQueryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.ACCOUNTNOT_ERROR);
        }
        Account accountInfo = this.getOne(accountQueryWrapper);
        // 验证码
        String trueCode = redisTemplate.opsForValue().get(VERIFICATION_CODE + accountInfo.getAId());
        if (!code.equals(trueCode)) {
            throw new BusinessException(ErrorCode.CODE_ERROR);
        }
        // 修改
        String digestPassword = DigestUtils.md5DigestAsHex((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8));
        Account account = new Account();
        account.setAPassword(digestPassword);
        int updateResult = accountMapper.update(account, accountQueryWrapper);
        if (updateResult == 0) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return new AccountInfoVo(accountInfo.getAId(), phone, accountInfo.getAAvatar());
    }

    /**
     * 退出登录
     *
     * @param request 登录态
     * @return AccountInfoVo
     */
    @Override
    public AccountInfoVo accountLoginOut(HttpServletRequest request, Integer role) {
        if (request == null) {
            return null;
        }
        HttpSession session = request.getSession();
        String sessionId = LoginIdUtils.getSessionId(role);
        Account loginInfo = getLoginInfo(request, sessionId);
        AccountInfoVo accountInfoVo = new AccountInfoVo(loginInfo.getAId(), loginInfo.getAPhone(), loginInfo.getAAvatar());
        session.removeAttribute(sessionId);
        return accountInfoVo;
    }

    /**
     * 获取登录信息
     *
     * @param request 登录态
     * @param role    角色代码
     * @return AccountInfoVo
     */
    @Override
    public Account getLoginInfo(HttpServletRequest request, String role) {
        Object attribute;
        switch (role) {
            case USER_LOGIN_STATE:
                attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
                break;
            case COM_LOGIN_STATE:
                attribute = request.getSession().getAttribute(COM_LOGIN_STATE);
                break;
            case SCHOOL_LOGIN_STATE:
                attribute = request.getSession().getAttribute(SCHOOL_LOGIN_STATE);
                break;
            default:
                throw new BusinessException(ErrorCode.ROLE_ERROR);
        }
        AccountInfoVo accountInfoVo = (AccountInfoVo) attribute;
        if (accountInfoVo == null || accountInfoVo.getId() == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_id", accountInfoVo.getId());
        Account account = this.getOne(accountQueryWrapper);
        if (account == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        return account;
    }

    /**
     * 上传头像
     *
     * @param request 登录态
     * @param file    文件
     * @return 响应数据
     */
    @Override
    public AccountInfoVo accountUploadAvatar(HttpServletRequest request, MultipartFile file, Integer role) {
        Account loginInfo = getLoginInfo(request, LoginIdUtils.getSessionId(role));
        String aId = loginInfo.getAId();
        // 上传头像
        long size = file.getSize();
        if (size > PHOTO_SIZE) {
            throw new BusinessException(ErrorCode.FILE_SIZE_ERROR, "图片太大，最多只能1MB");
        }
        if (aId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String uploadResultUrl = qiniuyunOssConfig.upload(file, aId, PHOTO_PREFIX);
        if (uploadResultUrl == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        // 保存到数据库
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        Account account = new Account();
        account.setAAvatar(uploadResultUrl);
        accountQueryWrapper.eq("a_id", aId);
        boolean update = this.update(account, accountQueryWrapper);
        if (!update) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        Account accountInfo = this.getOne(accountQueryWrapper);
        return new AccountInfoVo(accountInfo.getAId(), accountInfo.getAPhone(), accountInfo.getAAvatar());
    }

    /**
     * 发送样验证码
     *
     * @param accountGetCodeRequest 请求参数
     * @return 是否发送成功
     */
    @Override
    public Boolean pushMsgCode(AccountGetCodeRequest accountGetCodeRequest) {
        String phone = accountGetCodeRequest.getPhone();
        Integer role = accountGetCodeRequest.getRole();
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_phone", phone);
        accountQueryWrapper.eq("a_role", role);
        Account account = accountMapper.selectOne(accountQueryWrapper);
        if (account == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        String id = account.getAId();
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, 5)));
        // 发送
        Boolean pushMsg;
        try {
            pushMsg = pushMsgConfig.pushMsg(phone, code);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.PUSH_CODE_ERROR, e.getMessage());
        }
        // 保存
        redisTemplate.opsForValue().set((VERIFICATION_CODE + id), code, VERIFICATION_CODE_TIME);
        return pushMsg;
    }
}




