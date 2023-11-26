package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.mapper.FreshResumeMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshResume;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.FreshResumeService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    /**
     * 添加简历
     *
     * @param request 登录态
     * @param file    简历文件
     * @return 简历信息
     */
    @Override
    public ResumeInfoVo addResume(HttpServletRequest request, MultipartFile file) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        String userId = loginInfo.getAId();
        // 上传文件

        return null;
    }
}




