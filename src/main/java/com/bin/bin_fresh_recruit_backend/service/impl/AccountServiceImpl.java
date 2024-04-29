package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.config.DefaultFileConfig;
import com.bin.bin_fresh_recruit_backend.config.PushMsgConfig;
import com.bin.bin_fresh_recruit_backend.config.TokenConfig;
import com.bin.bin_fresh_recruit_backend.config.UploadServiceConfig;
import com.bin.bin_fresh_recruit_backend.constant.RequestConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.AccountMapper;
import com.bin.bin_fresh_recruit_backend.mapper.CompanyInfoMapper;
import com.bin.bin_fresh_recruit_backend.mapper.FreshUserInfoMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.*;
import com.bin.bin_fresh_recruit_backend.model.request.account.AccountGetCodeRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.FreshAddListRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.FreshManageRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.account.AccountInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.other.IpVo;
import com.bin.bin_fresh_recruit_backend.model.vo.school.FreshManageVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.FreshUserInfoService;
import com.bin.bin_fresh_recruit_backend.service.LoginInfoService;
import com.bin.bin_fresh_recruit_backend.service.ThemeSettingService;
import com.bin.bin_fresh_recruit_backend.utils.ArrayUtils;
import com.bin.bin_fresh_recruit_backend.utils.IdUtils;
import com.bin.bin_fresh_recruit_backend.utils.IpUtil;
import com.bin.bin_fresh_recruit_backend.utils.LoginIdUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private TokenConfig tokenConfig;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private FreshUserInfoMapper freshUserInfoMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private UploadServiceConfig uploadServiceConfig;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private PushMsgConfig pushMsgConfig;

    @Resource
    private FreshUserInfoService freshUserInfoService;

    @Resource
    private DefaultFileConfig defaultFileConfig;

    @Resource
    private ThemeSettingService themeSettingService;

    @Resource
    private LoginInfoService loginInfoService;

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
        String url = defaultFileConfig.getDefaultAvatar(role);
        int insertResult = 0;
        boolean saveResult;
        Account account = new Account();
        account.setAId(id);
        account.setARole(role);
        account.setAPhone(phone);
        account.setAPassword(digestPassword);
        account.setAAdd(id);
        account.setAAvatar(url);
        saveResult = this.save(account);
        // 保存信息
        switch (role) {
            case SCHOOL_ROLE:
                return new AccountInfoVo(id, phone, "", "");
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
        // 保存主题
        ThemeSetting themeSetting = new ThemeSetting();
        themeSetting.setAId(id);
        boolean save = themeSettingService.save(themeSetting);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        return new AccountInfoVo(id, phone, "", "");
    }

    /**
     * 账号登录
     *
     * @param phone    手机号
     * @param password 密码
     * @param role     角色
     * @return AccountInfoVo
     */
    @Override
    public AccountInfoVo accountLogin(HttpServletRequest request, Integer loginType, String phone, String password, Integer role, String code, Integer isFilterLately) {
        Account account;
        switch (loginType) {
            case 0:
                // 参数校验
                String digestPassword = DigestUtils.md5DigestAsHex((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8));
                QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
                accountQueryWrapper.and(j -> j.eq("a_id", phone).or().eq("a_phone", phone));
                accountQueryWrapper.eq("a_password", digestPassword);
                accountQueryWrapper.eq("a_role", role);
                account = accountMapper.selectOne(accountQueryWrapper);
                break;
            case 1:
                // 验证手机号是否存在
                String pattern = "^1[3456789]\\d{9}$";
                if (!Pattern.matches(pattern, phone)) {
                    throw new BusinessException(ErrorCode.PHONE_ERROR);
                }
                QueryWrapper<Account> codeQueryWrapper = new QueryWrapper<>();
                codeQueryWrapper.eq("a_phone", phone);
                codeQueryWrapper.eq("a_role", role);
                codeQueryWrapper.eq("is_delete", NO_DELETE);
                account = accountMapper.selectOne(codeQueryWrapper);
                if (account == null) {
                    log.info("User login error,phone password or role error");
                    throw new BusinessException(ErrorCode.LOGIN_ERROR);
                }
                // 获取验证码并校验
                String trueCode = redisTemplate.opsForValue().get(LOGIN_VERIFICATION_CODE + account.getAId());
                if (!code.equals(trueCode)) {
                    throw new BusinessException(ErrorCode.CODE_ERROR);
                }
                break;
            default:
                throw new BusinessException(ErrorCode.LOGIN_ERROR, "登录类型错误");
        }
        if (account == null) {
            log.info("User login error,phone password or role error");
            throw new BusinessException(ErrorCode.LOGIN_ERROR);
        }
        // 记录登录态
        // 生成token
        String token = tokenConfig.getToken(account.getAId(), role);
        // 获取用户名
        String userName = "";
        switch (role) {
            case SCHOOL_ROLE:
                userName = "学校就业中心";
                break;
            case FRESH_ROLE:
                QueryWrapper<FreshUserInfo> freshUserInfoQueryWrapper = new QueryWrapper<>();
                freshUserInfoQueryWrapper.eq("user_id", account.getAId());
                FreshUserInfo freshUserInfo = freshUserInfoMapper.selectOne(freshUserInfoQueryWrapper);
                if (freshUserInfo != null) {
                    userName = freshUserInfo.getUserName();
                }
                break;
            case COMPANY_ROLE:
                QueryWrapper<CompanyInfo> companyInfoQueryWrapper = new QueryWrapper<>();
                companyInfoQueryWrapper.eq("com_id", account.getAId());
                CompanyInfo companyInfo = companyInfoMapper.selectOne(companyInfoQueryWrapper);
                if (companyInfo != null) {
                    userName = companyInfo.getComName();
                }
                break;
            default:
                userName = "";
        }
        if ("".equals(userName)) {
            userName = "请完善用户名";
        }
        // 保存登录信息
        boolean saveInfo = saveLoginInfo(request, account.getAId(), isFilterLately);
        if (!saveInfo) {
            throw new BusinessException(ErrorCode.IP_NULL);
        }
        return new AccountInfoVo(account.getAId(), account.getAPhone(), account.getAAvatar(), token, userName);
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
        String trueCode = redisTemplate.opsForValue().get(FORGET_VERIFICATION_CODE + accountInfo.getAId());
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
        return new AccountInfoVo(accountInfo.getAId(), phone, accountInfo.getAAvatar(), "");
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
        String sessionId = LoginIdUtils.getSessionId(role);
        Account loginInfo = getLoginInfo(request, sessionId);
        AccountInfoVo accountInfoVo = new AccountInfoVo(loginInfo.getAId(), loginInfo.getAPhone(), loginInfo.getAAvatar(), "");
        request.removeAttribute(sessionId);
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
        String userId;
        switch (role) {
            case USER_LOGIN_STATE:
                userId = (String) request.getAttribute(USER_LOGIN_STATE);
                break;
            case COM_LOGIN_STATE:
                userId = (String) request.getAttribute(COM_LOGIN_STATE);
                break;
            case SCHOOL_LOGIN_STATE:
                userId = (String) request.getAttribute(SCHOOL_LOGIN_STATE);
                break;
            default:
                throw new BusinessException(ErrorCode.ROLE_ERROR);
        }
        if (StringUtils.isAnyBlank(userId)) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_id", userId);
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
    public AccountInfoVo accountUploadAvatar(HttpServletRequest request, MultipartFile file, Integer role, Integer serviceType) {
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
        String uploadResultUrl = uploadServiceConfig.upload(file, aId, PHOTO_PREFIX, serviceType);

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
        return new AccountInfoVo(accountInfo.getAId(), accountInfo.getAPhone(), accountInfo.getAAvatar(), "");
    }

    /**
     * 发送验证码
     *
     * @param accountGetCodeRequest 请求参数
     * @return 是否发送成功
     */
    @Override
    public Boolean pushMsgCode(AccountGetCodeRequest accountGetCodeRequest) {
        String phone = accountGetCodeRequest.getPhone();
        Integer role = accountGetCodeRequest.getRole();
        Integer type = accountGetCodeRequest.getType();
        if (type == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!type.equals(RequestConstant.FORGET) && !type.equals(RequestConstant.LOGIN) && !type.equals(RequestConstant.REGISTER)) {
            throw new BusinessException(ErrorCode.TYPE_ERROR);
        }
        String redisKey = "";
        if (type.equals(RequestConstant.FORGET)) {
            redisKey = FORGET_VERIFICATION_CODE;
        }
        if (type.equals(RequestConstant.LOGIN)) {
            redisKey = LOGIN_VERIFICATION_CODE;
        }
        if (type.equals(RequestConstant.REGISTER)) {
            redisKey = REGISTER_VERIFICATION_CODE;
        }
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
        redisTemplate.opsForValue().set((redisKey + id), code, VERIFICATION_CODE_TIME, TimeUnit.MILLISECONDS);

        return pushMsg;
    }

    /**
     * 添加应届生（单个）
     *
     * @param request            登录态
     * @param freshManageRequest 请求参数
     * @return
     */
    @Override
    @Transactional
    public FreshManageVo addFreshBySchool(HttpServletRequest request, FreshManageRequest freshManageRequest) {
        Account schoolAccount = this.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (schoolAccount == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = schoolAccount.getAId();
        if (StringUtils.isAnyBlank(schoolId)) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String freshId = freshManageRequest.getFreshId();
        if (StringUtils.isAnyBlank(freshId) || freshId.contains(String.valueOf(NOT_CONTAIN))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 默认密码 123456
        String digestPassword = DigestUtils.md5DigestAsHex((PASSWORD_SALT + DEFAULT_PASSWORD).getBytes(StandardCharsets.UTF_8));
        freshId = START_CHAR + schoolId.substring(schoolId.length() - 4) + "_" + freshId;
        // 保存
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_id", freshId);
        accountQueryWrapper.eq("a_add", schoolId);
        Account one = this.getOne(accountQueryWrapper);
        if (one != null) {
            throw new BusinessException(ErrorCode.ACCOUNT_ERROR);
        }
        // 保存
        String url = defaultFileConfig.getDefaultAvatar(FRESH_ROLE);
        Account account = new Account();
        account.setAId(freshId);
        account.setAPassword(digestPassword);
        account.setARole(FRESH_ROLE);
        account.setAAdd(schoolId);
        account.setAAvatar(url);
        boolean save = this.save(account);
        // 添加到应届生信息
        FreshUserInfo freshUserInfo = new FreshUserInfo();
        freshUserInfo.setUserId(freshId);
        int insertResult = freshUserInfoMapper.insert(freshUserInfo);
        if (!save || insertResult == 0) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        FreshManageVo manageVo = new FreshManageVo();
        manageVo.setFreshId(freshId);
        manageVo.setSchoolId(schoolId);
        ThemeSetting themeSetting = new ThemeSetting();
        themeSetting.setAId(freshId);
        boolean themeSave = themeSettingService.save(themeSetting);
        if (!themeSave) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        return manageVo;
    }

    /**
     * 批量添加应届生
     *
     * @param request             登录态
     * @param freshAddListRequest 请求参数
     * @return 响应数据
     */
    @Override
    @Transactional
    public List<FreshManageVo> addFreshBySchoolList(HttpServletRequest request, FreshAddListRequest freshAddListRequest) {
        Account schoolAccount = this.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (schoolAccount == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = schoolAccount.getAId();
        if (StringUtils.isAnyBlank(schoolId)) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String[] freshIds = freshAddListRequest.getFreshIds();
        if (freshIds.length > MAX_ADD_FRESH_NUM) {
            throw new BusinessException(ErrorCode.OVER_MAX_ERROR);
        }
        // 去重id
        String[] duplicationFreshIds = ArrayUtils.removeDuplication(freshIds);
        String digestPassword = DigestUtils.md5DigestAsHex((PASSWORD_SALT + DEFAULT_PASSWORD).getBytes(StandardCharsets.UTF_8));
        List<Account> accountList = new ArrayList<>();
        List<FreshUserInfo> freshUserList = new ArrayList<>();
        List<FreshManageVo> freshManageVos = new ArrayList<>();
        List<ThemeSetting> themeSettings = new ArrayList<>();
        for (String freshId : duplicationFreshIds) {
            QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
            freshId = START_CHAR + schoolId.substring(schoolId.length() - 4) + "_" + freshId;
            if (freshId.contains(String.valueOf(NOT_CONTAIN))) {
                continue;
            }
            accountQueryWrapper.eq("a_id", freshId);
            accountQueryWrapper.eq("a_add", schoolId);
            Account one = this.getOne(accountQueryWrapper);
            if (one != null) {
                continue;
            }
            // 保存
            String url = defaultFileConfig.getDefaultAvatar(FRESH_ROLE);
            Account account = new Account();
            account.setAId(freshId);
            account.setAPassword(digestPassword);
            account.setARole(FRESH_ROLE);
            account.setAAdd(schoolId);
            account.setAAvatar(url);
            accountList.add(account);

            FreshUserInfo freshUserInfo = new FreshUserInfo();
            freshUserInfo.setUserId(freshId);
            freshUserList.add(freshUserInfo);

            FreshManageVo freshManageVo = new FreshManageVo();
            freshManageVo.setFreshId(freshId);
            freshManageVo.setSchoolId(schoolId);
            freshManageVos.add(freshManageVo);

            ThemeSetting themeSetting = new ThemeSetting();
            themeSetting.setAId(freshId);
            themeSettings.add(themeSetting);
        }
        // 批量添加
        boolean saveBatch = this.saveBatch(accountList);
        if (accountList.size() != 0 && !saveBatch) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        boolean batch = freshUserInfoService.saveBatch(freshUserList);
        if (freshUserList.size() != 0 && !batch) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        boolean batch1 = themeSettingService.saveBatch(themeSettings);
        if (themeSettings.size() != 0 && !batch1) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        return freshManageVos;
    }

    /**
     * 删除应届生(单个)
     *
     * @param request            登录态
     * @param freshManageRequest 请求参数
     * @return 响应数据
     */
    @Override
    @Transactional
    public FreshManageVo deleteFreshBySchool(HttpServletRequest request, FreshManageRequest freshManageRequest) {
        Account schoolAccount = this.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (schoolAccount == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = schoolAccount.getAId();
        if (StringUtils.isAnyBlank(schoolId)) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String freshId = freshManageRequest.getFreshId();
        if (StringUtils.isAnyBlank(freshId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_id", freshId);
        accountQueryWrapper.eq("a_add", schoolId);
        Account account = this.getOne(accountQueryWrapper);
        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNTNOT_ERROR);
        }
        // 删除
        boolean remove = this.remove(accountQueryWrapper);
        QueryWrapper<FreshUserInfo> freshUserInfoQueryWrapper = new QueryWrapper<>();
        freshUserInfoQueryWrapper.eq("user_id", freshId);
        boolean removeFresh = freshUserInfoService.remove(freshUserInfoQueryWrapper);
        if (!remove || !removeFresh) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        FreshManageVo manageVo = new FreshManageVo();
        manageVo.setFreshId(freshId);
        manageVo.setSchoolId(schoolId);
        return manageVo;
    }

    /**
     * 获取应届生列表
     *
     * @param request  登录态
     * @param current  页码
     * @param pageSize 页大小
     * @return 响应数据
     */
    @Override
    public PageVo<FreshInfoVo> getFreshList(HttpServletRequest request, long current, long pageSize) {
        Account schoolAccount = this.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (schoolAccount == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = schoolAccount.getAId();
        if (StringUtils.isAnyBlank(schoolId)) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        // 查询Id
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("a_add", schoolId);
        accountQueryWrapper.eq("a_role", FRESH_ROLE);
        accountQueryWrapper.orderByDesc("create_time");
        Page<Account> accountPage = this.page(new Page<>(current, pageSize), accountQueryWrapper);
        // 查询信息
        ArrayList<String> freshIds = new ArrayList<>();
        for (Account record : accountPage.getRecords()) {
            freshIds.add(record.getAId());
        }
        QueryWrapper<FreshUserInfo> freshUserInfoQueryWrapper = new QueryWrapper<>();
        freshUserInfoQueryWrapper.in("user_id", freshIds);
        List<FreshUserInfo> freshUserInfos = freshUserInfoMapper.selectList(freshUserInfoQueryWrapper);
        List<FreshInfoVo> freshInfoVos = new ArrayList<>();
        for (FreshUserInfo freshUserInfo : freshUserInfos) {
            FreshInfoVo freshInfoVo = new FreshInfoVo();
            BeanUtils.copyProperties(freshUserInfo, freshInfoVo);
            freshInfoVos.add(freshInfoVo);
        }
        // 封装
        PageVo<FreshInfoVo> pageVo = new PageVo<>();
        pageVo.setList(freshInfoVos);
        pageVo.setCurrent(accountPage.getCurrent());
        pageVo.setPageSize(accountPage.getSize());
        pageVo.setTotal(accountPage.getTotal());
        return pageVo;
    }


    /**
     * 保存登录信息
     *
     * @param request
     * @param aId
     * @return
     */
    private boolean saveLoginInfo(HttpServletRequest request, String aId, Integer isFilterLately) {
        // 获取IP解析结果
        String ipAddr = IpUtil.getIpAddr(request);
        IpVo ipVo = IpUtil.getIpVoBaseResponse(ipAddr);
        // 同一个IP同一天登录不显示
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, NO_RECORD_LOGIN_DAY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String day = simpleDateFormat.format(instance.getTime());
        QueryWrapper<LoginInfo> loginInfoQueryWrapper = new QueryWrapper<>();
        loginInfoQueryWrapper.eq("a_id", aId);
        loginInfoQueryWrapper.eq("is_delete", NO_DELETE);
        loginInfoQueryWrapper.eq("login_ip", ipAddr);
        loginInfoQueryWrapper.gt("create_time", day);
        List<LoginInfo> list = loginInfoService.list(loginInfoQueryWrapper);
        if (isFilterLately == 0 && (list != null && list.size() > 0)) {
            return true;
        }
        // 保存
        LoginInfo loginInfo = new LoginInfo();
        UserAgent userAgent = IpUtil.getUserAgent(request);
        if (userAgent != null) {
            loginInfo.setLoginDevice(userAgent.getOperatingSystem().getDeviceType().toString() + " · " + userAgent.getOperatingSystem().getName() + " · " + userAgent.getBrowser().toString());
        }
        loginInfo.setAId(aId);
        loginInfo.setLoginIp(ipAddr);
        loginInfo.setLoginAddress(ipVo.getAddress());
        loginInfo.setLoginCountry(ipVo.getCountry());
        loginInfo.setLoginProvince(ipVo.getProvince());
        loginInfo.setLoginCity(ipVo.getCity());
        return loginInfoService.save(loginInfo);
    }
}




