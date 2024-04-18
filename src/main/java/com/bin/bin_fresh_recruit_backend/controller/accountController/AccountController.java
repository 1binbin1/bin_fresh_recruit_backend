package com.bin.bin_fresh_recruit_backend.controller.accountController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.config.DefaultFileConfig;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.IgnoreAuth;
import com.bin.bin_fresh_recruit_backend.interceptor.LoginUser;
import com.bin.bin_fresh_recruit_backend.model.request.account.AccountGetCodeRequest;
import com.bin.bin_fresh_recruit_backend.model.request.account.AccountLoginOutRequest;
import com.bin.bin_fresh_recruit_backend.model.request.account.AccountLoginRequest;
import com.bin.bin_fresh_recruit_backend.model.request.account.AccountRegisterForgetRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.account.AccountInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Objects;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.*;

/**
 * 账号相关控制器
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/4 22:12
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
    @Resource
    private AccountService accountService;


    /**
     * 注册账号 C端O端
     *
     * @param accountRegisterForgetRequest 请求参数
     * @return 响应数据
     */
    @IgnoreAuth
    @PostMapping("/register")
    public BaseResponse<AccountInfoVo> accountRegister(@RequestBody AccountRegisterForgetRequest accountRegisterForgetRequest) {
        if (accountRegisterForgetRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String phone = accountRegisterForgetRequest.getPhone();
        String password = accountRegisterForgetRequest.getPassword();
        String checkPassword = accountRegisterForgetRequest.getCheckPassword();
        Integer role = accountRegisterForgetRequest.getRole();
        // 校验
        if (StringUtils.isAnyBlank(phone, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if ((SCHOOL_ROLE != role) && (COMPANY_ROLE != role)) {
            throw new BusinessException(ErrorCode.ROLE_ERROR);
        }
        AccountInfoVo accountInfoVo = accountService.accountRegister(phone, password, checkPassword, role);
        return ResultUtils.success(accountInfoVo);
    }

    /**
     * 登录账号
     *
     * @param accountLoginRequest 请求参数
     * @return 响应数据
     */
    @IgnoreAuth
    @PostMapping("/login")
    public BaseResponse<AccountInfoVo> accountLogin(@RequestBody AccountLoginRequest accountLoginRequest) {
        if (accountLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String phone = accountLoginRequest.getPhone();
        Integer role = accountLoginRequest.getRole();
        Integer loginType = accountLoginRequest.getLoginType();
        String code = accountLoginRequest.getCode();
        String password = accountLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if ((SCHOOL_ROLE != role) && (FRESH_ROLE != role) && (COMPANY_ROLE != role)) {
            throw new BusinessException(ErrorCode.ROLE_ERROR);
        }
        if (Objects.equals(loginType, LOGIN_TYPE_PASSWORD) && StringUtils.isAnyBlank(password)){
            throw new BusinessException(ErrorCode.PASSWORD_NULL);
        }
        if (Objects.equals(loginType, LOGIN_TYPE_CODE) && StringUtils.isAnyBlank(code)){
            throw new BusinessException(ErrorCode.CODE_NULL);
        }
        AccountInfoVo accountInfoVo = accountService.accountLogin(loginType,phone, password, role,code);
        return ResultUtils.success(accountInfoVo);
    }

    /**
     * 忘记密码
     *
     * @param accountRegisterForgetRequest 请求参数
     * @return 响应数据
     */
    @IgnoreAuth
    @PostMapping("/forget")
    public BaseResponse<AccountInfoVo> accountForget(@RequestBody AccountRegisterForgetRequest accountRegisterForgetRequest) {
        if (accountRegisterForgetRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String password = accountRegisterForgetRequest.getPassword();
        String checkPassword = accountRegisterForgetRequest.getCheckPassword();
        Integer role = accountRegisterForgetRequest.getRole();
        String phone = accountRegisterForgetRequest.getPhone();
        String code = accountRegisterForgetRequest.getCode();
        if ((SCHOOL_ROLE != role) && (FRESH_ROLE != role) && (COMPANY_ROLE != role)) {
            throw new BusinessException(ErrorCode.ROLE_ERROR);
        }
        // 校验
        if (StringUtils.isAnyBlank(password, checkPassword, phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AccountInfoVo accountInfoVo = accountService.accountForget(phone, password, checkPassword, role, code);
        return ResultUtils.success(accountInfoVo);
    }

    /**
     * 退出登录
     *
     * @param request 登录态
     * @return 响应数据
     */
    @LoginUser
    @PostMapping("/loginout")
    public BaseResponse<AccountInfoVo> accountLoginOut(HttpServletRequest request, @RequestBody AccountLoginOutRequest accountLoginOutRequest) {
        if (accountLoginOutRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        AccountInfoVo accountInfoVo = accountService.accountLoginOut(request, accountLoginOutRequest.getRole());
        return ResultUtils.success(accountInfoVo);
    }

    /**
     * 上传头像
     *
     * @param request     登录态
     * @param file        文件
     * @param serviceType 上传服务类型 0-阿里云（默认） 1-七牛云
     * @return 账号信息
     */
    @LoginUser
    @PostMapping("/upload")
    public BaseResponse<AccountInfoVo> accountUploadAvatar(HttpServletRequest request, @RequestBody MultipartFile file,
                                                           @RequestParam Integer role, @RequestParam("service_type") Integer serviceType) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (file == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(accountService.accountUploadAvatar(request, file, role, serviceType));
    }


    /**
     * 获取验证码
     *
     * @param accountGetCodeRequest 请求参数
     * @return 响应数据
     */
    @PostMapping("/getcode")
    public BaseResponse<Boolean> pushMsgCode(@RequestBody AccountGetCodeRequest accountGetCodeRequest) {
        if (accountGetCodeRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(accountService.pushMsgCode(accountGetCodeRequest));
    }
}
