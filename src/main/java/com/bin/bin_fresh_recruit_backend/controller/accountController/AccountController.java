package com.bin.bin_fresh_recruit_backend.controller.accountController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.request.account.AccountLoginRequest;
import com.bin.bin_fresh_recruit_backend.model.request.account.AccountRegisterForgetRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.account.AccountInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
     * 注册账号 B端C端O端
     *
     * @param accountRegisterForgetRequest 请求参数
     * @return 响应数据
     */
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
        AccountInfoVo accountInfoVo = accountService.accountRegister(phone, password, checkPassword, role);
        return ResultUtils.success(accountInfoVo);
    }

    /**
     * 登录账号
     *
     * @param accountLoginRequest 请求参数
     * @return 响应数据
     */
    @PostMapping("/login")
    public BaseResponse<AccountInfoVo> accountLogin(@RequestBody AccountLoginRequest accountLoginRequest) {
        return null;
    }

    /**
     * 忘记密码
     *
     * @param accountRegisterForgetRequest 请求参数
     * @return 响应数据
     */
    @PostMapping("/forget")
    public BaseResponse<AccountInfoVo> accountForget(@RequestBody AccountRegisterForgetRequest accountRegisterForgetRequest) {
        return null;
    }
}
