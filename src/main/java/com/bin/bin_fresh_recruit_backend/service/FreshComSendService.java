package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshComSend;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobComSendSearchRequest;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.ResumeSendRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobSendVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshComSendVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshSendStateVo;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolRateVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hongxiaobin
 * @description 针对表【t_fresh_com_send(应届生投递记录表)】的数据库操作Service
 * @createDate 2023-12-02 17:06:05
 */
public interface FreshComSendService extends IService<FreshComSend> {

    /**
     * 投递简历
     *
     * @param request           登录态
     * @param resumeSendRequest 简历投递请求
     * @return 请求响应
     */
    FreshComSendVo sendResume(HttpServletRequest request, ResumeSendRequest resumeSendRequest);

    /**
     * 查询就业数据
     *
     * @param request 登录态（学校就业中心）
     * @return 响应数据
     */
    List<SchoolRateVo> getRate(HttpServletRequest request);

    /**
     * 企业获取投递进度
     *
     * @param request                 登录态
     * @param jobComSendSearchRequest 请求参数
     * @return 响应数据（分页）
     */
    PageVo<JobSendVo> getFreshSend(HttpServletRequest request, JobComSendSearchRequest jobComSendSearchRequest);

    /**
     * 获取投递进度
     *
     * @param request 请求参数
     * @return 响应
     */
    List<FreshSendStateVo> getSendState(HttpServletRequest request);
}
