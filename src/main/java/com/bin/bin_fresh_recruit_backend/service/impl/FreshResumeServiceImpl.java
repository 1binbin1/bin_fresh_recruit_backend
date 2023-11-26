package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.config.QiniuyunOSSConfig;
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
    private QiniuyunOSSConfig qiniuyunOssConfig;

    /**
     * 添加简历
     *
     * @param request 登录态
     * @param file    简历文件
     * @return 简历信息
     */
    @Override
    public ResumeInfoVo addResume(HttpServletRequest request, MultipartFile file) {
        long size = file.getSize();
        if (size > RESUME_SIZE) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件太多，最多只能10MB");
        }
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        String userId = loginInfo.getAId();
        // 上传文件
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String uploadResultUrl = qiniuyunOssConfig.upload(file, userId);
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
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        return resumeId;
    }
}




