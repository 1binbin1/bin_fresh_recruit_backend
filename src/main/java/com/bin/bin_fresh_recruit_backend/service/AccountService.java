package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.vo.account.AccountInfoVo;

/**
 * @author hongxiaobin
 * @description 针对表【t_account(账号信息表)】的数据库操作Service
 * @createDate 2023-11-04 15:34:12
 */
public interface AccountService extends IService<Account> {

    /**
     * 注册账号
     *
     * @param phone         手机号
     * @param password      密码
     * @param checkPassword 确认密码
     * @param role          角色
     * @return 响应数据
     */
    AccountInfoVo accountRegister(String phone, String password, String checkPassword, Integer role);
}
