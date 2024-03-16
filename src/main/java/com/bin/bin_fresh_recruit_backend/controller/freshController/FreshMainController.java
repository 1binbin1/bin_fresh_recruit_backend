package com.bin.bin_fresh_recruit_backend.controller.freshController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.constant.CommonConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.IgnoreAuth;
import com.bin.bin_fresh_recruit_backend.interceptor.LoginUser;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobInfoVo;
import com.bin.bin_fresh_recruit_backend.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * C端-主页相关
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/26 13:25
 */
@RequestMapping("/fresh")
@RestController
@Slf4j
public class FreshMainController {
    @Resource
    private JobInfoService jobInfoService;


    /**
     * 岗位推荐
     *
     * @param request     登录态
     * @param limit       推荐个数
     * @param isRecommend 是否推荐 0-否 1-是
     * @return 响应信息
     */
    @LoginUser
    @GetMapping("/job/recommend")
    public BaseResponse<List<JobInfoVo>> getJobRecommend(HttpServletRequest request,
                                                         @RequestParam("limit") Integer limit,
                                                         @RequestParam("is_recommend") Integer isRecommend) {
        if (!Objects.equals(isRecommend, CommonConstant.RECOMMEND_YES) &&
                !Objects.equals(isRecommend, CommonConstant.RECOMMEND_NO)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "是否推荐码错误");
        }
        if (limit == null) {
            limit = CommonConstant.RECOMMEND_LIMIT;
        }
        if (Objects.equals(isRecommend, CommonConstant.RECOMMEND_YES) && request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        List<JobInfoVo> jobInfoVos = jobInfoService.getRecommendList(request, limit, isRecommend);
        return ResultUtils.success(jobInfoVos);
    }
}
