package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.LoginInfoMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.LoginInfo;
import com.bin.bin_fresh_recruit_backend.model.vo.other.LoginInfoList;
import com.bin.bin_fresh_recruit_backend.model.vo.other.LoginInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.LoginInfoService;
import com.bin.bin_fresh_recruit_backend.utils.IpUtil;
import com.bin.bin_fresh_recruit_backend.utils.LoginIdUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.GET_LOGININFO_DAYS;
import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.NO_DELETE;

/**
 * @author hongxiaobin
 * @description 针对表【t_login_info(登录信息)】的数据库操作Service实现
 * @createDate 2024-04-29 16:00:18
 */
@Service
public class LoginInfoServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo>
        implements LoginInfoService {

    @Resource
    private AccountService accountService;

    /**
     * 获取90天内登录信息
     *
     * @param request
     * @param current
     * @param pageSize
     * @return
     */
    @Override
    public LoginInfoVo<LoginInfoList> getLoginInfo(HttpServletRequest request, Integer role, Integer current, Integer pageSize) {
        Account loginInfo = accountService.getLoginInfo(request, LoginIdUtils.getSessionId(role));
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String aId = loginInfo.getAId();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, GET_LOGININFO_DAYS);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String day = simpleDateFormat.format(instance.getTime());
        // 查询总数
        QueryWrapper<LoginInfo> loginInfoQueryWrapper = new QueryWrapper<>();
        loginInfoQueryWrapper.eq("a_id", aId);
        loginInfoQueryWrapper.gt("create_time", day);
        loginInfoQueryWrapper.eq("is_delete", NO_DELETE);
        loginInfoQueryWrapper.orderByDesc("create_time");
        long count = this.count(loginInfoQueryWrapper);
        // 查询合格数
        QueryWrapper<LoginInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("a_id", aId);
        infoQueryWrapper.gt("create_time", day);
        infoQueryWrapper.eq("is_delete", NO_DELETE);
        String ipAddr = IpUtil.getIpAddr(request);
        infoQueryWrapper.eq("login_ip", ipAddr);
        long successCount = this.count(infoQueryWrapper);
        // 计算占比（转为正整数）
        Integer ceil = (int) Math.ceil(((double) successCount / count) * 100);
        LoginInfoVo<LoginInfoList> result = new LoginInfoVo<>();
        result.setScore(ceil);
        result.setAId(aId);
        // 分页查询
        Page<LoginInfo> loginInfoPage = this.page(new Page<>(current, pageSize), loginInfoQueryWrapper);
        ArrayList<LoginInfoList> list = new ArrayList<>();
        for (LoginInfo record : loginInfoPage.getRecords()) {
            LoginInfoList loginInfoList = new LoginInfoList();
            BeanUtils.copyProperties(record, loginInfoList);
            list.add(loginInfoList);
        }
        result.setList(list);
        result.setCurrent(current);
        result.setTotal(loginInfoPage.getTotal());
        result.setPageSize(pageSize);
        return result;
    }
}




