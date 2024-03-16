package com.bin.bin_fresh_recruit_backend.controller.schoolController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.LoginUser;
import com.bin.bin_fresh_recruit_backend.model.request.school.FreshAddListRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.FreshManageRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.school.FreshManageVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.bin.bin_fresh_recruit_backend.constant.RequestConstant.DEFAULT_CURRENT;
import static com.bin.bin_fresh_recruit_backend.constant.RequestConstant.DEFAULT_PAGE_SIZE;

/**
 * 应届生管理控制器
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/4 21:57
 */
@RestController
@Slf4j
@RequestMapping("/school/fresh")
public class FreshManageController {
    @Resource
    private AccountService accountService;

    /**
     * 添加单个应届生
     *
     * @param freshManageRequest 请求参数
     * @param request            登录态
     * @return 响应数据
     */
    @LoginUser
    @PostMapping("/add_one")
    public BaseResponse<FreshManageVo> addFresh(HttpServletRequest request, @RequestBody FreshManageRequest freshManageRequest) {
        // 验证是否登录
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        // 验证请求参数
        if (freshManageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        FreshManageVo freshManageVo = accountService.addFreshBySchool(request, freshManageRequest);
        return ResultUtils.success(freshManageVo);
    }

    /**
     * 批量单个应届生（最多200个）
     *
     * @param freshAddListRequest 请求参数
     * @param request            登录态
     * @return 响应数据
     */
    @LoginUser
    @PostMapping("/add_list")
    public BaseResponse<List<FreshManageVo>> addFreshList(HttpServletRequest request, @RequestBody FreshAddListRequest freshAddListRequest) {
        // 验证是否登录
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        // 验证请求参数
        if (freshAddListRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<FreshManageVo> freshManageVo = accountService.addFreshBySchoolList(request, freshAddListRequest);
        return ResultUtils.success(freshManageVo);
    }

    /**
     * 删除应届生（单个）
     *
     * @param request            登录态
     * @param freshManageRequest 请求参数
     * @return 响应数据
     */
    @LoginUser
    @PostMapping("/delete")
    public BaseResponse<FreshManageVo> deleteFresh(HttpServletRequest request, @RequestBody FreshManageRequest freshManageRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (freshManageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        FreshManageVo freshManageVo = accountService.deleteFreshBySchool(request, freshManageRequest);
        return ResultUtils.success(freshManageVo);
    }

    /**
     * 分页查询应届生列表
     *
     * @param request  登录态
     * @param current  页码
     * @param pageSize 页大小
     * @return 响应数据
     */
    @LoginUser
    @GetMapping("/list")
    public BaseResponse<PageVo<FreshInfoVo>> getFreshList(HttpServletRequest request,
                                                          @RequestParam("current") long current,
                                                          @RequestParam("page_size") long pageSize) {
        if (request == null) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR);
        }
        if (current == 0) {
            current = DEFAULT_CURRENT;
        }
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PageVo<FreshInfoVo> freshList = accountService.getFreshList(request, current, pageSize);
        return ResultUtils.success(freshList);
    }
}
