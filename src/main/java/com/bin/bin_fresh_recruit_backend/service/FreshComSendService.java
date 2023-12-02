package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshComSend;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeSendRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshComSendVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hongxiaobin
 * @description 针对表【t_fresh_com_send(应届生投递记录表)】的数据库操作Service
 * @createDate 2023-12-02 17:06:05
 */
public interface FreshComSendService extends IService<FreshComSend> {

    /**
     * 投递简历
     *
     * @param request 登录态
     * @param resumeSendRequest 简历投递请求
     * @return 请求响应
     */
    FreshComSendVo sendResume(HttpServletRequest request, ResumeSendRequest resumeSendRequest);
}
