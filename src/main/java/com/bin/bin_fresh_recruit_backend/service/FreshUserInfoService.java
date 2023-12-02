package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshUserInfo;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.FreshInfoRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshInfoVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hongxiaobin
 * @description 针对表【t_fresh_user_info(应届生个人信息表)】的数据库操作Service
 * @createDate 2023-11-04 15:34:12
 */
public interface FreshUserInfoService extends IService<FreshUserInfo> {

    /**
     * 查询应届生信息
     *
     * @param userId 用户ID
     * @return 响应数据
     */
    FreshInfoVo getFreshInfoOne(String userId);

    /**
     * 应届生更新个人信息
     *
     * @param request          登录态
     * @param freshInfoRequest 请求参数
     * @return 响应数据
     */
    FreshInfoVo updateFreshInfo(HttpServletRequest request, FreshInfoRequest freshInfoRequest);
}
