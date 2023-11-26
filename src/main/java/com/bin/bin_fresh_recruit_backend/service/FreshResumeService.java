package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshResume;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hongxiaobin
 * @description 针对表【t_fresh_resume(应届生附件简历表)】的数据库操作Service
 * @createDate 2023-11-04 15:34:12
 */
public interface FreshResumeService extends IService<FreshResume> {

    /**
     * 添加简历附件
     *
     * @param request 登录态
     * @param file    简历文件
     * @return 简历信息
     */
    ResumeInfoVo addResume(HttpServletRequest request, MultipartFile file);
}
