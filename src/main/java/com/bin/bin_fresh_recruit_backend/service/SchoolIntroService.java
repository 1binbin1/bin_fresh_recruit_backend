package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.model.domain.SchoolIntro;
import com.bin.bin_fresh_recruit_backend.model.request.school.MessageDeleteRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.MessageListRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.MessageUpdateRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.PublishMessageRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolIntroVo;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolMessageVo;

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

    /**
     * 获取咨询列表
     *
     * @param request            登录态
     * @param messageListRequest 请求参数
     * @return 响应参数
     */
    PageVo<SchoolMessageVo> getMessageList(HttpServletRequest request, MessageListRequest messageListRequest);

    /**
     * 更新资讯
     *
     * @param request              登录态
     * @param messageUpdateRequest 请求参数
     * @return 响应数据
     */
    SchoolMessageVo updateMessage(HttpServletRequest request, MessageUpdateRequest messageUpdateRequest);

    /**
     * 删除资讯
     *
     * @param request              登录态
     * @param messageDeleteRequest 请求参数
     * @return 响应数据
     */
    SchoolMessageVo deleteMessage(HttpServletRequest request, MessageDeleteRequest messageDeleteRequest);

    /**
     * 应届生获取资讯列表
     *
     * @param request  登录态
     * @param current  页码
     * @param pageSize 页大小
     * @return 响应
     */
    PageVo<SchoolIntroVo> getByFresh(HttpServletRequest request, Integer current, Integer pageSize);
}
