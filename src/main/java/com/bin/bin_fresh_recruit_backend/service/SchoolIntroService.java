package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.SchoolIntro;
import com.bin.bin_fresh_recruit_backend.model.request.school.PublishMessageRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolIntroVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hongxiaobin
 * @description 针对表【t_school_intro(就业咨询信息表)】的数据库操作Service
 * @createDate 2023-11-04 15:34:12
 */
public interface SchoolIntroService extends IService<SchoolIntro> {

    /**
     * 发布资讯
     *
     * @param publishMessageRequest 请求参数
     * @return 响应体
     */
    SchoolIntroVo publishMessage(HttpServletRequest request, PublishMessageRequest publishMessageRequest);
}
