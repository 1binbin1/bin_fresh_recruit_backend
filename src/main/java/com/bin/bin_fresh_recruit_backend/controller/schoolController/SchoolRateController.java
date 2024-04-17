package com.bin.bin_fresh_recruit_backend.controller.schoolController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.LoginUser;
import com.bin.bin_fresh_recruit_backend.model.request.school.FreshDataOutRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolRateVo;
import com.bin.bin_fresh_recruit_backend.service.FreshComSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 学校就业中心相关数据查询
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/5 16:06
 */
@RestController
@RequestMapping("/school")
@Slf4j
public class SchoolRateController {
    @Resource
    private FreshComSendService freshComSendService;

    /**
     * 获取应届生就业相关数据
     *
     * @param request 登录态
     * @return 响应数据
     */
    @LoginUser
    @GetMapping("/rate")
    public BaseResponse<List<SchoolRateVo>> getSchoolFreshRate(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        List<SchoolRateVo> result = freshComSendService.getRate(request);
        return ResultUtils.success(result);
    }

    @LoginUser
    @PostMapping("/data/out")
    public void freshDataOut(HttpServletRequest request, @RequestBody FreshDataOutRequest freshDataOutRequest, HttpServletResponse response) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        freshComSendService.dataOutToExcel(request,response,freshDataOutRequest);
    }
}
