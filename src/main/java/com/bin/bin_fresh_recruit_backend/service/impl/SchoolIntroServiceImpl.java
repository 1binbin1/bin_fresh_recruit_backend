package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.constant.RequestConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.SchoolIntroMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.SchoolIntro;
import com.bin.bin_fresh_recruit_backend.model.request.school.PublishMessageRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolIntroVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.SchoolIntroService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.SCHOOL_LOGIN_STATE;

/**
 * @author hongxiaobin
 * @description 针对表【t_school_intro(就业咨询信息表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class SchoolIntroServiceImpl extends ServiceImpl<SchoolIntroMapper, SchoolIntro>
        implements SchoolIntroService {
    @Resource
    private AccountService accountService;

    @Override
    public SchoolIntroVo publishMessage(HttpServletRequest request, PublishMessageRequest publishMessageRequest) {
        Account schoolAccount = accountService.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (schoolAccount == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = schoolAccount.getAId();
        if (StringUtils.isAnyBlank(schoolId)) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String title = publishMessageRequest.getTitle();
        String message = publishMessageRequest.getMessage();
        if (StringUtils.isAnyBlank(title, message)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (title.length() > RequestConstant.TITLE_MAX_LENGTH || message.length() > RequestConstant.TEXT_MAX_LENGTH) {
            throw new BusinessException(ErrorCode.SIZE_ERROR);
        }
        // 保存
        SchoolIntro schoolIntro = new SchoolIntro();
        schoolIntro.setSchoolId(schoolId);
        schoolIntro.setTitle(title);
        schoolIntro.setIntroContent(message);
        boolean save = this.save(schoolIntro);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        SchoolIntroVo schoolIntroVo = new SchoolIntroVo();
        BeanUtils.copyProperties(schoolIntro, schoolIntroVo);
        return schoolIntroVo;
    }
}




