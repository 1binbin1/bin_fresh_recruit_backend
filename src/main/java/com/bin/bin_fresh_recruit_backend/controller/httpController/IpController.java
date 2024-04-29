package com.bin.bin_fresh_recruit_backend.controller.httpController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.IgnoreAuth;
import com.bin.bin_fresh_recruit_backend.model.vo.other.IpVo;
import com.bin.bin_fresh_recruit_backend.model.vo.other.LoginInfoList;
import com.bin.bin_fresh_recruit_backend.model.vo.other.LoginInfoVo;
import com.bin.bin_fresh_recruit_backend.service.LoginInfoService;
import com.bin.bin_fresh_recruit_backend.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.*;

/**
 * 获取IP地址和城市
 *
 * @Author: hongxiaobin
 * @Time: 2024/4/28 22:06
 */
@RestController
@RequestMapping("/ip")
@Slf4j
public class IpController {
    @Resource
    private LoginInfoService loginInfoService;

    @IgnoreAuth
    @GetMapping("/get")
    public BaseResponse<IpVo> getIpAddress(HttpServletRequest request) {
        String ipAddr = IpUtil.getIpAddr(request);
        return ResultUtils.success(IpUtil.getIpVoBaseResponse(ipAddr));
    }


    @IgnoreAuth
    @GetMapping("/get/city")
    public BaseResponse<IpVo> getIpCityInfo(@RequestParam("ip") String ipAddr) {
        return ResultUtils.success(IpUtil.getIpVoBaseResponse(ipAddr));
    }

    /**
     * 获取最近登录信息
     *
     * @param request
     * @param current
     * @param pageSize
     * @return
     */
    @IgnoreAuth
    @GetMapping("/getLogin")
    public BaseResponse<LoginInfoVo<LoginInfoList>> getLoginInfo(HttpServletRequest request, @RequestParam("a_id") String aId, @RequestParam("current") Integer current, @RequestParam("page_size") Integer pageSize) {
        if (current == 0) {
            current = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (StringUtils.isAnyBlank(aId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginInfoVo<LoginInfoList> result = loginInfoService.getLoginInfo(request, aId, current, pageSize);
        return ResultUtils.success(result);
    }
}
