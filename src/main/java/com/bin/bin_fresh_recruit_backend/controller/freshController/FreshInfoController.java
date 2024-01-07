package com.bin.bin_fresh_recruit_backend.controller.freshController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.FreshInfoRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshInfoVo;
import com.bin.bin_fresh_recruit_backend.service.FreshUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 应届生-个人信息相关
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/5 11:50
 */
@RestController
@RequestMapping("/fresh/info")
@Slf4j
public class FreshInfoController {
    @Resource
    private FreshUserInfoService freshUserInfoService;

    /**
     * 修改个人信息
     *
     * @param freshInfoRequest 请求数据
     * @param request          登录态
     * @return 响应数据
     */
    @PostMapping("/update")
    public BaseResponse<FreshInfoVo> updateFreshInfo(@RequestBody FreshInfoRequest freshInfoRequest,
                                                     HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (freshInfoRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        FreshInfoVo freshInfoVo = freshUserInfoService.updateFreshInfo(request, freshInfoRequest);
        return ResultUtils.success(freshInfoVo);
    }

    /**
     * 获取个人信息
     *
     * @param userId 用户ID
     * @return 响应数据
     */
    @GetMapping("/one")
    public BaseResponse<FreshInfoVo> getOneFreshInfo(@RequestParam("user_id") String userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(freshUserInfoService.getFreshInfoOne(userId));
    }
}
