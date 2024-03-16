package com.bin.bin_fresh_recruit_backend.controller.dictController;

import com.bin.bin_fresh_recruit_backend.common.BaseResponse;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.ResultUtils;
import com.bin.bin_fresh_recruit_backend.constant.DictConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.interceptor.IgnoreAuth;
import com.bin.bin_fresh_recruit_backend.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: hongxiaobin
 * @Time: 2024/1/6 16:59
 */
@RestController
@RequestMapping("/dict")
@Slf4j
public class DictController {
    @Resource
    private DictService dictService;

    @IgnoreAuth
    @GetMapping("/get")
    public BaseResponse<List<String>> getDictList(@RequestParam("dict_type") Integer dictType) {
        if (dictType == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (!Objects.equals(dictType, DictConstant.DICT_JOB_TYPE) &&
                !Objects.equals(dictType, DictConstant.DICT_PAY_TYPE) &&
                !dictType.equals(DictConstant.DICT_CITY_TYPE) &&
                !dictType.equals(DictConstant.DICT_JOB_NAME)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<String> result = dictService.getDictList(dictType);
        return ResultUtils.success(result);
    }
}
