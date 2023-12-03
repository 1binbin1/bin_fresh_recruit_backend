package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.model.domain.JobInfo;
import com.bin.bin_fresh_recruit_backend.model.request.company.*;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.ResumeInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hongxiaobin
 * @description 针对表【t_job_info(岗位信息表)】的数据库操作Service
 * @createDate 2023-11-04 15:34:12
 */
public interface JobInfoService extends IService<JobInfo> {

    /**
     * 新增岗位信息
     *
     * @param request           登录态
     * @param jobInfoAddRequest 岗位信息请求
     * @return 岗位信息
     */
    JobInfoVo addJob(HttpServletRequest request, JobInfoAddRequest jobInfoAddRequest);

    /**
     * 更新岗位信息
     *
     * @param request              登录态
     * @param jobInfoUpdateRequest 岗位信息请求
     * @return 岗位信息
     */
    JobInfoVo updateJob(HttpServletRequest request, JobInfoUpdateRequest jobInfoUpdateRequest);

    /**
     * 删除岗位信息
     *
     * @param request          登录态
     * @param jobInfoIdRequest 岗位ID
     * @return id
     */
    String deleteJob(HttpServletRequest request, JobInfoIdRequest jobInfoIdRequest);

    /**
     * 简历筛选
     *
     * @param request               登录态
     * @param resumeFiltrateRequest 请求信息
     * @return 响应结果
     */
    ResumeInfoVo filrate(HttpServletRequest request, ResumeFiltrateRequest resumeFiltrateRequest);

    /**
     * 查询岗位单个信息
     *
     * @param jobInfoIdRequest 请求参数
     * @return 岗位信息
     */
    JobInfoVo getJobOne(JobInfoIdRequest jobInfoIdRequest);

    /**
     * 岗位信息列表
     *
     * @param jobSearchRequest 搜索条件
     * @return 响应信息
     */
    PageVo<JobInfoVo> getJobList(JobSearchRequest jobSearchRequest);

    /**
     * 获取企业岗位列表
     *
     * @param jobComSearchRequest 请求条件
     * @return 响应参数
     */
    PageVo<JobInfoVo> getJobListByCom(JobComSearchRequest jobComSearchRequest);

    /**
     * 获取推荐岗位
     *
     * @param request     登录态
     * @param limit       推荐个数
     * @param isRecommend 是否推荐 0-否 1-是
     * @return 岗位列表
     */
    List<JobInfoVo> getRecommendList(HttpServletRequest request, Integer limit, Integer isRecommend);
}
