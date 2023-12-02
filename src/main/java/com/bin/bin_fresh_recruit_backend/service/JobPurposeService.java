package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.JobPurpose;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.PurposeDeleteRequest;
import com.bin.bin_fresh_recruit_backend.model.request.fresh.PurposeRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.JobPurposeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hongxiaobin
 * @description 针对表【t_job_purpose(应届生岗位意向表)】的数据库操作Service
 * @createDate 2023-11-04 15:34:12
 */
public interface JobPurposeService extends IService<JobPurpose> {

    /**
     * 新增岗位意向
     *
     * @param request        登录态
     * @param purposeRequest 请求参数
     * @return 请求响应
     */
    JobPurposeVo addJobPurpose(HttpServletRequest request, PurposeRequest purposeRequest);

    /**
     * 更新岗位意向
     *
     * @param request        登录态
     * @param purposeRequest 请求参数
     * @return 岗位意向信息
     */
    JobPurposeVo updateJobPurpose(HttpServletRequest request, PurposeRequest purposeRequest);

    /**
     * 删除岗位意向
     *
     * @param request              登录态
     * @param purposeDeleteRequest 请求参数
     * @return 请求结果
     */
    Integer deleteJobPurpose(HttpServletRequest request, PurposeDeleteRequest purposeDeleteRequest);

    /**
     * 获取岗位意向列表
     *
     * @param request 登录态
     * @return 意向岗位列表
     */
    List<JobPurposeVo> getPurposeList(HttpServletRequest request);

    /**
     * 获取岗位意向详情
     *
     * @param request 登录态
     * @param jobId   意向岗位ID
     * @return 岗位意向详情
     */
    JobPurposeVo getPurposeOne(HttpServletRequest request, Integer jobId);
}
