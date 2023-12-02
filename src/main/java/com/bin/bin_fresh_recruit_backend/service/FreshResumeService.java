package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshResume;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 删除简历
     *
     * @param request       登录态
     * @param resumeRequest 请求参数
     * @return ID
     */
    String deleteResume(HttpServletRequest request, ResumeRequest resumeRequest);

    /**
     * 更新简历信息
     *
     * @param request  登录态
     * @param file     简历文件
     * @param resumeId 简历ID
     * @return 响应信息
     */
    ResumeInfoVo updateResume(HttpServletRequest request, MultipartFile file, String resumeId);

    /**
     * 简历列表
     *
     * @param request 登录态
     * @return 简历列表
     */
    List<ResumeInfoVo> getResumeList(HttpServletRequest request);

    /**
     * 简历详情
     *
     * @param request  登录态
     * @param resumeId 简历ID
     * @return 简历信息
     */
    ResumeInfoVo getResumeOne(HttpServletRequest request, String resumeId);
}
