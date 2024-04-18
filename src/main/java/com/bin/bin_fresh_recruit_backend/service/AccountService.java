package com.bin.bin_fresh_recruit_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.request.account.AccountGetCodeRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.FreshAddListRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.FreshManageRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.account.AccountInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.fresh.FreshInfoVo;
import com.bin.bin_fresh_recruit_backend.model.vo.school.FreshManageVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hongxiaobin
 * @description 针对表【t_account(账号信息表)】的数据库操作Service
 * @createDate 2023-11-04 15:34:12
 */
public interface AccountService extends IService<Account> {

    /**
     * 注册账号
     *
     * @param phone         手机号
     * @param password      密码
     * @param checkPassword 确认密码
     * @param role          角色
     * @return 响应数据
     */
    AccountInfoVo accountRegister(String phone, String password, String checkPassword, Integer role);

    /**
     * 账号登录
     *
     * @param phone    手机号
     * @param password 密码
     * @param role     角色
     * @return 响应数据
     */
    AccountInfoVo accountLogin(Integer loginType, String phone, String password, Integer role,String code);

    /**
     * 忘记密码
     *
     * @param phone         手机号
     * @param password      密码
     * @param checkPassword 确认密码
     * @return 响应数据
     */
    AccountInfoVo accountForget(String phone, String password, String checkPassword, Integer role, String code);

    /**
     * 退出登录
     *
     * @param request 登录态
     * @return 响应数据
     */
    AccountInfoVo accountLoginOut(HttpServletRequest request, Integer role);

    /**
     * 获取登录用户信息
     *
     * @param request 登录态
     * @param role    角色代码(常量)
     * @return 账号信息
     */
    Account getLoginInfo(HttpServletRequest request, String role);

    /**
     * 上传头像
     *
     * @param request 登录态
     * @param file    文件
     * @return 响应数据
     */
    AccountInfoVo accountUploadAvatar(HttpServletRequest request, MultipartFile file, Integer role, Integer serviceType);

    /**
     * 发送验证吗码
     *
     * @param accountGetCodeRequest 请求参数
     * @return 响应数据
     */
    Boolean pushMsgCode(AccountGetCodeRequest accountGetCodeRequest);

    /**
     * 添加应届生（单个）
     *
     * @param request            登录态
     * @param freshManageRequest 请求参数
     * @return 相应数据
     */
    FreshManageVo addFreshBySchool(HttpServletRequest request, FreshManageRequest freshManageRequest);

    /**
     * 删除应届生(单个)
     *
     * @param request            登录态
     * @param freshManageRequest 请求参数
     * @return 响应数据
     */
    FreshManageVo deleteFreshBySchool(HttpServletRequest request, FreshManageRequest freshManageRequest);

    /**
     * 获取应届生列表
     *
     * @param request  登录态
     * @param current  页码
     * @param pageSize 页大小
     * @return 响应数据
     */
    PageVo<FreshInfoVo> getFreshList(HttpServletRequest request, long current, long pageSize);

    /**
     * 批量添加应届生
     *
     * @param request             登录态
     * @param freshAddListRequest 请求参数
     * @return 响应数据
     */
    List<FreshManageVo> addFreshBySchoolList(HttpServletRequest request, FreshAddListRequest freshAddListRequest);
}
