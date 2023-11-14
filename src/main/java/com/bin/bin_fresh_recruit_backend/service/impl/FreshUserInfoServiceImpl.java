package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.FreshUserInfoMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshUserInfo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshInfoVo;
import com.bin.bin_fresh_recruit_backend.service.FreshUserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @Override
    public FreshInfoVo getFreshInfoOne(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        FreshUserInfo freshUserInfo = (FreshUserInfo) attribute;
        if (freshUserInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = freshUserInfo.getUserId();
        QueryWrapper<FreshUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        FreshUserInfo userInfo = freshUserInfoMapper.selectOne(queryWrapper);
        return null;
    }
}




