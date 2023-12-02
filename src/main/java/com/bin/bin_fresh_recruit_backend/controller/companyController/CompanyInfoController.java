package com.bin.bin_fresh_recruit_backend.controller.companyController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.model.request.company.CompanyInfoRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.company.CompanyInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.company.CompanyVo;
import com.bin.bin_fresh_recruit_backend.service.CompanyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 企业信息相关控制器
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/2 17:49
 */
@RestController
@RequestMapping("com/info")
@Slf4j
public class CompanyInfoController {
    @Resource
    private CompanyInfoService companyInfoService;

    /**
     * 修改企业信息
     *
     * @param request            登录态
     * @param companyInfoRequest 请求参数
     * @return 修改结果信息
     */
    @PostMapping("/update")
    public BaseResponse<CompanyInfoVo> updateCompanyInfo(HttpServletRequest request, @RequestBody CompanyInfoRequest companyInfoRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (companyInfoRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CompanyInfoVo companyInfoVo = companyInfoService.updateCompanyInfo(request, companyInfoRequest);
        return ResultUtils.success(companyInfoVo);
    }


    /**
     * 获取企业信息
     *
     * @param comId 企业ID
     * @return 响应信息
     */
    @GetMapping("/one")
    public BaseResponse<CompanyVo> getCompanyOne(@RequestParam("com_id") String comId) {
        if (comId == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        CompanyVo companyVo = companyInfoService.getCompanyInfo(comId);
        return ResultUtils.success(companyVo);
    }
}
