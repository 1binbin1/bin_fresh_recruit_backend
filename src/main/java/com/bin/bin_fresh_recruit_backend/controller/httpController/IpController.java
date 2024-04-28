package com.bin.bin_fresh_recruit_backend.controller.httpController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.IgnoreAuth;
import com.bin.bin_fresh_recruit_backend.model.vo.other.IpVo;
import com.bin.bin_fresh_recruit_backend.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @IgnoreAuth
    @GetMapping("/get")
    public BaseResponse<IpVo> getIpAddress(HttpServletRequest request) {
        String ipAddr = IpUtil.getIpAddr(request);
        return getIpVoBaseResponse(ipAddr);
    }


    @IgnoreAuth
    @GetMapping("/get/city")
    public BaseResponse<IpVo> getIpCityInfo(@RequestParam("ip")String ipAddr) {
        return getIpVoBaseResponse(ipAddr);
    }

    private BaseResponse<IpVo> getIpVoBaseResponse(@RequestParam("ip") String ipAddr) {
        log.info("获取到IP地址为：{}",ipAddr);
        String cityInfo;
        if (!StringUtils.isAnyBlank(ipAddr)) {
            cityInfo = IpUtil.getIp2region(ipAddr);
        } else {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (StringUtils.isAnyBlank(cityInfo)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        assert cityInfo != null;
        String[] strings = cityInfo.split("\\|");
        if (strings == null || strings.length < 4) {
            throw new BusinessException(ErrorCode.IP_NULL);
        }
        log.info("IP={}解析结果为:{}", ipAddr, cityInfo);
        IpVo ipVo = new IpVo();
        ipVo.setIpAddress(ipAddr);
        ipVo.setCountry(strings[0]);
        ipVo.setProvince(strings[1]);
        ipVo.setCity(strings[2]);
        ipVo.setAddress(ipVo.getCountry() + "·" + ipVo.getProvince() + "·" + ipVo.getCity());
        ipVo.setCityInfo(cityInfo);
        return ResultUtils.success(ipVo);
    }
}
