package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.FreshComSendMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshComSend;
import com.bin.bin_fresh_recruit_backend.model.enums.SendStatus;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeSendRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshComSendVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.FreshComSendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.USER_LOGIN_STATE;

/**
 * @author hongxiaobin
 * @description 针对表【t_fresh_com_send(应届生投递记录表)】的数据库操作Service实现
 * @createDate 2023-12-02 17:06:05
 */
@Service
public class FreshComSendServiceImpl extends ServiceImpl<FreshComSendMapper, FreshComSend>
        implements FreshComSendService {

    @Resource
    private FreshComSendMapper freshComSendMapper;

    @Resource
    private AccountService accountService;

    /**
     * 投递简历
     *
     * @param request           登录态
     * @param resumeSendRequest 简历投递请求
     * @return 投递结果
     */
    @Override
    public FreshComSendVo sendResume(HttpServletRequest request, ResumeSendRequest resumeSendRequest) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = loginInfo.getAId();
        String comId = resumeSendRequest.getComId();
        String jobId = resumeSendRequest.getJobId();
        String resumeId = resumeSendRequest.getResumeId();
        if (StringUtils.isAnyBlank(userId, comId, jobId, resumeId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        FreshComSend freshComSend = new FreshComSend();
        freshComSend.setUserId(userId);
        freshComSend.setComId(comId);
        freshComSend.setJobId(jobId);
        freshComSend.setResumeId(resumeId);
        freshComSend.setSendTime(new Date());
        freshComSend.setSendState(SendStatus.SendStatusSuccess);
        boolean save = this.save(freshComSend);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        FreshComSendVo freshComSendVo = new FreshComSendVo();
        BeanUtils.copyProperties(freshComSend, freshComSendVo);
        return freshComSendVo;
    }
}




