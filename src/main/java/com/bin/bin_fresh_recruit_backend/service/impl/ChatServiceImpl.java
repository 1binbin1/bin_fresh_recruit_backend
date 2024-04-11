package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.common.ErrorCode;
import com.bin.bin_fresh_recruit_backend.config.UploadServiceConfig;
import com.bin.bin_fresh_recruit_backend.constant.CommonConstant;
import com.bin.bin_fresh_recruit_backend.exception.BusinessException;
import com.bin.bin_fresh_recruit_backend.mapper.AccountMapper;
import com.bin.bin_fresh_recruit_backend.mapper.ChatMapper;
import com.bin.bin_fresh_recruit_backend.mapper.CompanyInfoMapper;
import com.bin.bin_fresh_recruit_backend.mapper.FreshUserInfoMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Account;
import com.bin.bin_fresh_recruit_backend.model.domain.Chat;
import com.bin.bin_fresh_recruit_backend.model.domain.CompanyInfo;
import com.bin.bin_fresh_recruit_backend.model.domain.FreshUserInfo;
import com.bin.bin_fresh_recruit_backend.model.vo.chat.ChatVo;
import com.bin.bin_fresh_recruit_backend.model.vo.chat.LatelyFreshVo;
import com.bin.bin_fresh_recruit_backend.service.AccountService;
import com.bin.bin_fresh_recruit_backend.service.ChatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.bin.bin_fresh_recruit_backend.constant.CommonConstant.*;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.COM_LOGIN_STATE;
import static com.bin.bin_fresh_recruit_backend.constant.RedisConstant.USER_LOGIN_STATE;
import static com.bin.bin_fresh_recruit_backend.model.enums.ChatType.CHAT_TYPE_CONTENT;
import static com.bin.bin_fresh_recruit_backend.model.enums.ChatType.CHAT_TYPE_PICTURE;

/**
 * @author hongxiaobin
 * @description 针对表【t_chat(聊天记录表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
        implements ChatService {
    @Resource
    private AccountService accountService;

    @Resource
    private ChatMapper chatMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private FreshUserInfoMapper freshUserInfoMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private UploadServiceConfig uploadServiceConfig;

    /**
     * @param request  登录态
     * @param aId      账号ID
     * @param content  聊天内容
     * @param userType 发起人类型
     * @return 响应
     */
    @Override
    public ChatVo addChat(HttpServletRequest request, String aId, String content, Integer userType) {
        Account account = getAccountByUserType(request, userType);
        // 保存记录
        Chat chat = new Chat();
        if (CHAT_USER_FRESH.equals(userType)) {
            chat.setUserId(account.getAId());
            chat.setComId(aId);
        }
        if (CHAT_USER_COM.equals(userType)) {
            chat.setUserId(aId);
            chat.setComId(account.getAId());
        }
        chat.setUserType(userType);
        chat.setChatContent(content);
        chat.setChatType(CHAT_TYPE_CONTENT);
        boolean save = this.save(chat);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        ChatVo chatVo = new ChatVo();
        BeanUtils.copyProperties(chat, chatVo);
        return chatVo;
    }

    /**
     * 获取聊天记录(30day内)
     */
    @Override
    public List<ChatVo> getChatList(String userId, String comId) {
        // 获取时间范围
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now().plusDays(1);
        LocalDate lastDate = now.minusDays(CommonConstant.CHAT_DATES);
        String nowDateString = now.format(formatter);
        String lastDateString = lastDate.format(formatter);
        // 查询
        QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
        chatQueryWrapper.eq("user_id", userId);
        chatQueryWrapper.eq("com_id", comId);
        String sql = " limit " + CommonConstant.MAX_CHAT_LIST;
        chatQueryWrapper.last(sql);
        chatQueryWrapper.between("create_time", lastDateString, nowDateString);
        List<Chat> chatList = this.list(chatQueryWrapper);
        if (chatList == null) {
            return new ArrayList<>();
        }
        // 查询头像信息
        QueryWrapper<Account> freshAccountQueryWrapper = new QueryWrapper<>();
        freshAccountQueryWrapper.eq("a_id", userId);
        Account fresh = accountService.getOne(freshAccountQueryWrapper);

        QueryWrapper<Account> comAccountQueryWrapper = new QueryWrapper<>();
        comAccountQueryWrapper.eq("a_id", comId);
        Account com = accountService.getOne(comAccountQueryWrapper);

        if (fresh == null || com == null) {
            throw new BusinessException(ErrorCode.NO_RESOURCE_ERROR);
        }

        // 组装
        ArrayList<ChatVo> chatVoArrayList = new ArrayList<>();
        for (Chat chat : chatList) {
            ChatVo chatVo = new ChatVo();
            BeanUtils.copyProperties(chat, chatVo);
            if (CHAT_USER_FRESH.equals(chat.getUserType())) {
                chatVo.setAAvatar(fresh.getAAvatar());
            }
            if (CHAT_USER_COM.equals(chat.getUserType())) {
                chatVo.setAAvatar(com.getAAvatar());
            }
            chatVoArrayList.add(chatVo);
        }
        return chatVoArrayList;
    }


    /**
     * 企业获取聊天对象
     *
     * @param request 登录态
     * @return 响应对象
     */
    @Override
    public List<LatelyFreshVo> getLatelyFreshList(HttpServletRequest request) {
        Account loginInfo = accountService.getLoginInfo(request, COM_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String comId = loginInfo.getAId();
        // 查询最近聊天对象
        QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
        chatQueryWrapper.select("user_id,max(create_time) as create_time");
        chatQueryWrapper.eq("com_id", comId);
        chatQueryWrapper.groupBy("user_id");
        chatQueryWrapper.orderByDesc("create_time");
        String sql = " limit " + CommonConstant.MAX_LATELY_FRESH;
        chatQueryWrapper.last(sql);
        List<Chat> list = this.list(chatQueryWrapper);
        // 查询用户信息
        ArrayList<String> freshUserIds = new ArrayList<>();
        ArrayList<String> accountIds = new ArrayList<>();
        for (Chat chat : list) {
            freshUserIds.add(chat.getUserId());
            accountIds.add(chat.getUserId());
        }
        // 查询
        Map<String, FreshUserInfo> userInfo = new HashMap<>();
        Map<String, Account> accountInfo = new HashMap<>();
        if (freshUserIds.size() != 0) {
            userInfo = freshUserInfoMapper.getFreshUserInfo(freshUserIds);
        }
        if (accountIds.size() != 0) {
            accountInfo = accountMapper.getAccount(accountIds);
        }
        // 构造
        ArrayList<LatelyFreshVo> latelyFreshVos = new ArrayList<>();
        for (Chat chat : list) {
            LatelyFreshVo latelyFreshVo = new LatelyFreshVo();
            BeanUtils.copyProperties(chat, latelyFreshVo);
            latelyFreshVo.setAAvatar(accountInfo.get(chat.getUserId()).getAAvatar());
            latelyFreshVo.setUserName(userInfo.get(chat.getUserId()).getUserName());
            latelyFreshVos.add(latelyFreshVo);
        }
        return latelyFreshVos;
    }

    /**
     * 应届生获取聊天对象
     *
     * @param request 登录态
     * @return 响应对象
     */
    @Override
    public List<LatelyFreshVo> getLatelyComList(HttpServletRequest request) {
        Account loginInfo = accountService.getLoginInfo(request, USER_LOGIN_STATE);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String userId = loginInfo.getAId();
        // 查询最近聊天对象
        QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
        chatQueryWrapper.select("com_id,max(create_time) as create_time");
        chatQueryWrapper.eq("user_id", userId);
        chatQueryWrapper.groupBy("com_id");
        chatQueryWrapper.orderByDesc("create_time");
        String sql = " limit " + CommonConstant.MAX_LATELY_FRESH;
        chatQueryWrapper.last(sql);
        List<Chat> list = this.list(chatQueryWrapper);
        // 查询用户信息
        ArrayList<String> comUserIds = new ArrayList<>();
        ArrayList<String> accountIds = new ArrayList<>();
        for (Chat chat : list) {
            comUserIds.add(chat.getComId());
            accountIds.add(chat.getComId());
        }
        // 查询
        Map<String, CompanyInfo> comInfo = new HashMap<>();
        Map<String, Account> accountInfo = new HashMap<>();
        if (comUserIds.size() != 0) {
            comInfo = companyInfoMapper.getCompanyInfo(comUserIds);
        }
        if (accountIds.size() != 0) {
            accountInfo = accountMapper.getAccount(accountIds);
        }
        // 构造
        ArrayList<LatelyFreshVo> latelyComVos = new ArrayList<>();
        for (Chat chat : list) {
            LatelyFreshVo latelyFreshVo = new LatelyFreshVo();
            BeanUtils.copyProperties(chat, latelyFreshVo);
            latelyFreshVo.setAAvatar(accountInfo.get(chat.getComId()).getAAvatar());
            latelyFreshVo.setComName(comInfo.get(chat.getComId()).getComName());
            latelyComVos.add(latelyFreshVo);
        }
        return latelyComVos;
    }

    /**
     * 发送图片
     *
     * @param request
     * @param multipartFile
     * @param aId
     * @param userType
     * @return
     */
    @Override
    public ChatVo addChatByPicture(HttpServletRequest request, MultipartFile multipartFile, String aId, Integer userType) {
        Account account = getAccountByUserType(request, userType);
        // 保存记录
        Chat chat = new Chat();
        if (CHAT_USER_FRESH.equals(userType)) {
            chat.setUserId(account.getAId());
            chat.setComId(aId);
        }
        if (CHAT_USER_COM.equals(userType)) {
            chat.setUserId(aId);
            chat.setComId(account.getAId());
        }
        // 文件上传
        if (!Objects.requireNonNull(multipartFile.getContentType()).startsWith("image")) {
            throw new BusinessException(ErrorCode.NO_IMAGE_ERROR);
        }
        String uploadUrl = uploadServiceConfig.upload(multipartFile, account.getAId(), CHAT_PICTURE, 0);
        if (StringUtils.isAnyBlank(uploadUrl)) {
            throw new BusinessException(ErrorCode.UPLOAD_ERROR);
        }
        chat.setChatContent(uploadUrl);
        chat.setChatType(CHAT_TYPE_PICTURE);
        boolean save = this.save(chat);
        if (!save) {
            throw new BusinessException(ErrorCode.SQL_ERROR);
        }
        ChatVo chatVo = new ChatVo();
        BeanUtils.copyProperties(chat, chatVo);
        return chatVo;
    }

    private Account getAccountByUserType(HttpServletRequest request, Integer userType) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        String state;
        if (CHAT_USER_FRESH.equals(userType)) {
            state = USER_LOGIN_STATE;
        } else if (CHAT_USER_COM.equals(userType)) {
            state = COM_LOGIN_STATE;
        } else {
            throw new BusinessException(ErrorCode.ROLE_ERROR);
        }
        Account account = accountService.getLoginInfo(request, state);
        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNTNOT_ERROR);
        }
        return account;
    }
}




