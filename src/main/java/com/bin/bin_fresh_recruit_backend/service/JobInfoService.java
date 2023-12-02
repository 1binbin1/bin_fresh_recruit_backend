package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.model.domain.JobInfo;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobInfoAddRequest;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobInfoDeleteRequest;
import com.bin.bin_fresh_recruit_backend.model.request.company.JobInfoUpdateRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.company.JobInfoVo;

import javax.servlet.http.HttpServletRequest;

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
     * @param request 登录态
     * @param jobInfoDeleteRequest 岗位ID
     * @return id
     */
    String deleteJob(HttpServletRequest request, JobInfoDeleteRequest jobInfoDeleteRequest);
}
