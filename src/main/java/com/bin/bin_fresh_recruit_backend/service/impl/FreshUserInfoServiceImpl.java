package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.FreshUserInfoMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshUserInfo;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.FreshInfoRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.account.AccountInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.FreshUserInfoService;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.MAN;
import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.WOMAN;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.USER_LOGIN_STATE;

/**
 * @author hongxiaobin
 * @description 针对表【t_fresh_user_info(应届生个人信息表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class FreshUserInfoServiceImpl extends ServiceImpl<FreshUserInfoMapper, FreshUserInfo>
        implements FreshUserInfoService {

    @Resource
    private FreshUserInfoMapper freshUserInfoMapper;
    @Resource
    private AccountService accountService;

    @Override
    public FreshInfoVo getFreshInfoOne(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = loginInfo.getAId();
        QueryWrapper<FreshUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        FreshUserInfo userInfo = freshUserInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            throw new BusinessException(ErrorCode.GET_INFO_ERROR);
        }
        FreshInfoVo freshInfoVo = new FreshInfoVo();
        BeanUtils.copyProperties(userInfo, freshInfoVo);
        return freshInfoVo;
    }

    /**
     * 更新应届生个人信息
     *
     * @param request          登录态
     * @param freshInfoRequest 请求参数
     * @return 响应数据
     */
    @Override
    public FreshInfoVo updateFreshInfo(HttpServletRequest request, FreshInfoRequest freshInfoRequest) {
        // 判断参数
        String userName = freshInfoRequest.getUserName();
        Integer userSex = freshInfoRequest.getUserSex();
        String userEmail = freshInfoRequest.getUserEmail();
        String userSchool = freshInfoRequest.getUserSchool();
        String userMajor = freshInfoRequest.getUserMajor();
        String userYear = freshInfoRequest.getUserYear();
        String userEducation = freshInfoRequest.getUserEducation();
        if (StringUtils.isAnyBlank(userName, userEmail, userSchool, userMajor, userYear, userEducation)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (MAN != userSex || WOMAN != userSex) {
            throw new BusinessException(ErrorCode.USER_SEX_ERROR);
        }
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        String userId = loginInfo.getAId();
        // 更新数据库
        FreshUserInfo freshUserInfo = new FreshUserInfo();
        BeanUtils.copyProperties(freshInfoRequest, freshUserInfo);
        QueryWrapper<FreshUserInfo> freshUserInfoQueryWrapper = new QueryWrapper<>();
        freshUserInfoQueryWrapper.eq("user_id", userId);
        int update = freshUserInfoMapper.update(freshUserInfo, freshUserInfoQueryWrapper);
        if (update == 0) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        // 回显
        return getFreshInfoOne(request);
    }
}




