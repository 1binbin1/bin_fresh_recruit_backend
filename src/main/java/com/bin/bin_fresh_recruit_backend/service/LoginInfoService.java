package com.bin.bin_fresh_recruit_backend.service;

import com.bin.bin_fresh_recruit_backend.model.domain.LoginInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.vo.other.LoginInfoList;
import com.bin.bin_fresh_recruit_backend.model.vo.other.LoginInfoVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author hongxiaobin
* @description 针对表【t_login_info(登录信息)】的数据库操作Service
* @createDate 2024-04-29 16:00:18
*/
public interface LoginInfoService extends IService<LoginInfo> {

    LoginInfoVo<LoginInfoList> getLoginInfo(HttpServletRequest request,Integer role, Integer current, Integer pageSize);
}
