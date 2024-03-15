package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.common.PageVo;
import com.bin.bin_fresh_recruit_backend.constant.RequestConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.SchoolIntroMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.SchoolIntro;
import com.bin.bin_fresh_recruit_backend.model.request.school.MessageDeleteRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.MessageListRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.MessageUpdateRequest;
import com.bin.bin_fresh_recruit_backend.model.request.school.PublishMessageRequest;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolIntroVo;
import com.bin.bin_fresh_recruit_backend.model.vo.school.SchoolMessageVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.SchoolIntroService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.SCHOOL_LOGIN_STATE;

/**
 * @author hongxiaobin
 * @description 针对表【t_school_intro(就业咨询信息表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class SchoolIntroServiceImpl extends ServiceImpl<SchoolIntroMapper, SchoolIntro>
        implements SchoolIntroService {
    @Resource
    private AccountService accountService;

    @Override
    public SchoolIntroVo publishMessage(HttpServletRequest request, PublishMessageRequest publishMessageRequest) {
        Account schoolAccount = accountService.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (schoolAccount == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = schoolAccount.getAId();
        if (StringUtils.isAnyBlank(schoolId)) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String title = publishMessageRequest.getTitle();
        String message = publishMessageRequest.getMessage();
        if (StringUtils.isAnyBlank(title, message)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (title.length() > RequestConstant.TITLE_MAX_LENGTH || message.length() > RequestConstant.TEXT_MAX_LENGTH) {
            throw new BusinessException(ErrorCode.SIZE_ERROR);
        }
        // 保存
        SchoolIntro schoolIntro = new SchoolIntro();
        schoolIntro.setSchoolId(schoolId);
        schoolIntro.setTitle(title);
        schoolIntro.setIntroContent(message);
        boolean save = this.save(schoolIntro);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        SchoolIntroVo schoolIntroVo = new SchoolIntroVo();
        BeanUtils.copyProperties(schoolIntro, schoolIntroVo);
        return schoolIntroVo;
    }

    /**
     * 获取咨询列表
     *
     * @param request            登录态
     * @param messageListRequest 请求参数
     * @return 响应参数
     */
    @Override
    public PageVo<SchoolMessageVo> getMessageList(HttpServletRequest request, MessageListRequest messageListRequest) {
        // 获取登录信息
        Account loginInfo = accountService.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = loginInfo.getAId();
        // 构造查询条件
        String searchContent = messageListRequest.getSearchContent();
        QueryWrapper<SchoolIntro> schoolIntroQueryWrapper = new QueryWrapper<>();
        schoolIntroQueryWrapper.eq("school_id", schoolId);
        if (searchContent != null) {
            schoolIntroQueryWrapper.and(j -> j.like("title", searchContent).or().like("intro_content", searchContent));
        }
        // 查询
        Page<SchoolIntro> page = this.page(new Page<>(messageListRequest.getCurrent(), messageListRequest.getPageSize()), schoolIntroQueryWrapper);
        PageVo<SchoolMessageVo> result = new PageVo<>();
        ArrayList<SchoolMessageVo> list = new ArrayList<>();
        for (SchoolIntro record : page.getRecords()) {
            SchoolMessageVo schoolMessageVo = new SchoolMessageVo();
            BeanUtils.copyProperties(record, schoolMessageVo);
            list.add(schoolMessageVo);
        }
        result.setList(list);
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        return result;
    }

    /**
     * 更新资讯
     *
     * @param request              登录态
     * @param messageUpdateRequest 请求参数
     * @return
     */
    @Override
    public SchoolMessageVo updateMessage(HttpServletRequest request, MessageUpdateRequest messageUpdateRequest) {
        // 获取登录信息
        Account loginInfo = accountService.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = loginInfo.getAId();
        Integer id = messageUpdateRequest.getId();
        String title = messageUpdateRequest.getTitle();
        String introContent = messageUpdateRequest.getIntroContent();
        if (StringUtils.isAnyBlank(title, introContent)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 判断是否存在记录
        QueryWrapper<SchoolIntro> schoolIntroQueryWrapper = new QueryWrapper<>();
        schoolIntroQueryWrapper.eq("school_id", schoolId);
        schoolIntroQueryWrapper.eq("id", id);
        SchoolIntro one = this.getOne(schoolIntroQueryWrapper);
        if (one == null) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        // 更新
        SchoolIntro schoolIntro = new SchoolIntro();
        schoolIntro.setSchoolId(schoolId);
        schoolIntro.setIntroContent(introContent);
        schoolIntro.setTitle(title);
        boolean updateResult = this.update(schoolIntro, schoolIntroQueryWrapper);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        SchoolMessageVo schoolIntroVo = new SchoolMessageVo();
        BeanUtils.copyProperties(schoolIntro, schoolIntroVo);
        return schoolIntroVo;
    }

    /**
     * 删除资讯
     *
     * @param request              登录态
     * @param messageDeleteRequest 请求参数
     * @return
     */
    @Override
    public SchoolMessageVo deleteMessage(HttpServletRequest request, MessageDeleteRequest messageDeleteRequest) {
        // 获取登录信息
        Account loginInfo = accountService.getLoginInfo(request, SCHOOL_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String schoolId = loginInfo.getAId();
        // 判断记录存在
        QueryWrapper<SchoolIntro> schoolIntroQueryWrapper = new QueryWrapper<>();
        schoolIntroQueryWrapper.eq("school_id", schoolId);
        schoolIntroQueryWrapper.eq("id", messageDeleteRequest.getId());
        SchoolIntro one = this.getOne(schoolIntroQueryWrapper);
        if (one == null) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        // 删除
        boolean remove = this.remove(schoolIntroQueryWrapper);
        if (!remove) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        SchoolMessageVo result = new SchoolMessageVo();
        BeanUtils.copyProperties(one, result);
        return result;
    }
}




