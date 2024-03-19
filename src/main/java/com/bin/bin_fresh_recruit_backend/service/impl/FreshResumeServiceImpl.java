package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.config.UploadServiceConfig;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.FreshResumeMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshResume;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.FreshResumeService;
import com.bin.bin_fresh_recruit_backend.utils.IdUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.*;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.USER_LOGIN_STATE;

/**
 * @author hongxiaobin
 * @description 针对表【t_fresh_resume(应届生附件简历表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class FreshResumeServiceImpl extends ServiceImpl<FreshResumeMapper, FreshResume>
        implements FreshResumeService {
    @Resource
    private AccountService accountService;

    @Resource
    private FreshResumeMapper freshResumeMapper;

    @Resource
    private UploadServiceConfig uploadServiceConfig;

    /**
     * 添加简历
     *
     * @param request 登录态
     * @param file    简历文件
     * @return 简历信息
     */
    @Override
    public ResumeInfoVo addResume(HttpServletRequest request, MultipartFile file, Integer serviceType) {
        long size = file.getSize();
        if (size > RESUME_SIZE) {
            throw new BusinessException(ErrorCode.FILE_SIZE_ERROR, "文件太大，最多只能10MB");
        }
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        String userId = loginInfo.getAId();
        // 上传文件
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String uploadResultUrl = uploadServiceConfig.upload(file, userId, RESUME_PREFIX, serviceType);
        if (uploadResultUrl == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        // 保存到数据库
        FreshResume freshResume = new FreshResume();
        freshResume.setUserId(userId);
        String resumeId = IdUtils.getId(RESUME_ID);
        freshResume.setResumeId(resumeId);
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            fileName = DEFAULT_RESUME_NAME;
        }
        freshResume.setResumeName(fileName);
        freshResume.setUserNameLink(uploadResultUrl);
        boolean save = this.save(freshResume);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        QueryWrapper<FreshResume> freshResumeQueryWrapper = new QueryWrapper<>();
        freshResumeQueryWrapper.eq("resume_id", resumeId);
        FreshResume resume = this.getOne(freshResumeQueryWrapper);
        ResumeInfoVo resumeInfoVo = new ResumeInfoVo();
        BeanUtils.copyProperties(resume, resumeInfoVo);
        return resumeInfoVo;
    }

    /**
     * 删除简历
     *
     * @param request       登录态
     * @param resumeRequest 请求参数
     * @return ID
     */
    @Override
    public String deleteResume(HttpServletRequest request, ResumeRequest resumeRequest) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        String userId = loginInfo.getAId();
        String resumeId = resumeRequest.getResumeId();
        if (resumeId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 数据库删除
        QueryWrapper<FreshResume> freshResumeQueryWrapper = new QueryWrapper<>();
        freshResumeQueryWrapper.eq("user_id", userId);
        freshResumeQueryWrapper.eq("resume_id", resumeId);
        int delete = freshResumeMapper.delete(freshResumeQueryWrapper);
        if (delete == 0) {
            throw new BusinessException(ErrorCode.NO_RESOURCE_ERROR);
        }
        return resumeId;
    }

    /**
     * 更新简历信息
     *
     * @param request  登录态
     * @param file     简历文件
     * @param resumeId 简历ID
     * @return 简历信息
     */
    @Override
    public ResumeInfoVo updateResume(HttpServletRequest request, MultipartFile file, String resumeId, Integer serviceType) {
        long size = file.getSize();
        if (size > RESUME_SIZE) {
            throw new BusinessException(ErrorCode.FILE_SIZE_ERROR, "文件太大，最多只能10MB");
        }
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        String userId = loginInfo.getAId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String uploadResultUrl = uploadServiceConfig.upload(file, userId, RESUME_PREFIX, serviceType);
        if (uploadResultUrl == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            fileName = DEFAULT_RESUME_NAME;
        }
        // 更新数据库
        QueryWrapper<FreshResume> freshResumeQueryWrapper = new QueryWrapper<>();
        freshResumeQueryWrapper.eq("user_id", userId);
        freshResumeQueryWrapper.eq("resume_id", resumeId);
        FreshResume freshResume = new FreshResume();
        freshResume.setUserId(userId);
        freshResume.setResumeId(resumeId);
        freshResume.setUserNameLink(uploadResultUrl);
        freshResume.setResumeName(fileName);
        boolean update = this.update(freshResume, freshResumeQueryWrapper);
        if (!update) {
            throw new BusinessException(ErrorCode.NO_RESOURCE_ERROR);
        }
        FreshResume resume = this.getOne(freshResumeQueryWrapper);
        ResumeInfoVo resumeInfoVo = new ResumeInfoVo();
        BeanUtils.copyProperties(resume, resumeInfoVo);
        return resumeInfoVo;
    }

    /**
     * 简历列表
     *
     * @param request 登录态
     * @return 响应数据
     */
    @Override
    public List<ResumeInfoVo> getResumeList(HttpServletRequest request) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        String userId = loginInfo.getAId();
        QueryWrapper<FreshResume> freshResumeQueryWrapper = new QueryWrapper<>();
        freshResumeQueryWrapper.eq("user_id", userId);
        freshResumeQueryWrapper.orderByDesc("create_time");
        List<FreshResume> freshResumes = freshResumeMapper.selectList(freshResumeQueryWrapper);
        ArrayList<ResumeInfoVo> resumeInfoVos = new ArrayList<>();
        for (FreshResume freshResume : freshResumes) {
            ResumeInfoVo resumeInfoVo = new ResumeInfoVo();
            resumeInfoVo.setUserId(freshResume.getUserId());
            resumeInfoVo.setResumeId(freshResume.getResumeId());
            resumeInfoVo.setUserNameLink(freshResume.getUserNameLink());
            resumeInfoVo.setResumeName(freshResume.getResumeName());
            resumeInfoVo.setCreateTime(freshResume.getCreateTime());
            resumeInfoVo.setUpdateTime(freshResume.getUpdateTime());
            resumeInfoVos.add(resumeInfoVo);
        }
        return resumeInfoVos;
    }

    /**
     * 获取简历信息
     *
     * @param request  登录态
     * @param resumeId 简历ID
     * @return 简历信息
     */
    @Override
    public ResumeInfoVo getResumeOne(HttpServletRequest request, String resumeId) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        String userId = loginInfo.getAId();
        QueryWrapper<FreshResume> freshResumeQueryWrapper = new QueryWrapper<>();
        freshResumeQueryWrapper.eq("user_id", userId);
        freshResumeQueryWrapper.eq("resume_id", resumeId);
        FreshResume freshResume = this.getOne(freshResumeQueryWrapper);
        ResumeInfoVo resumeInfoVo = new ResumeInfoVo();
        if (freshResume == null) {
            throw new BusinessException(ErrorCode.NO_RESOURCE_ERROR);
        }
        BeanUtils.copyProperties(freshResume, resumeInfoVo);
        return resumeInfoVo;
    }
}




