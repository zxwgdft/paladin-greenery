package com.paladin.organization.service;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.Condition;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.framework.utils.ValidateUtil;
import com.paladin.framework.utils.secure.SecureUtil;
import com.paladin.organization.dao.mapper.SysUserMapper;
import com.paladin.organization.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author TontoZhou
 * @since 2019/12/12
 */
@Service
public class SysUserService extends ServiceSupport<SysUser> {


    @Value("${organization.default-password}")
    private String defaultPassword;

    @Autowired
    private SysUserMapper sysUserMapper;


    private Pattern accountPattern = Pattern.compile("^\\w{6,30}$");

    /**
     * 创建个人用户账号
     */
    public boolean createPersonnelAccount(String account, String userId) {
        if (account == null || !validateAccount(account)) {
            throw new BusinessException("账号不符合规则或者已经存在该账号");
        }

        String salt = SecureUtil.createSalt();
        String password = SecureUtil.hashByMD5(defaultPassword, salt);

        SysUser user = new SysUser();
        user.setId(UUIDUtil.createUUID());
        user.setAccount(account);
        user.setPassword(password);
        user.setSalt(salt);
        user.setUserId(userId);
        user.setUserType(SysUser.USER_TYPE_PERSONNEL);
        user.setState(SysUser.STATE_ENABLED);

        return save(user);
    }


    /**
     * 验证账号
     */
    public boolean validateAccount(String account) {
        if (!accountPattern.matcher(account).matches())
            return false;

        if (ValidateUtil.isValidatedAllIdcard(account)) {
            return false;
        }

        return searchCount(new Condition(SysUser.FIELD_ACCOUNT, QueryType.EQUAL, account)) == 0;
    }

    /**
     * 通过账号查找用户
     */
    public SysUser getUserByAccount(String account) {
        List<SysUser> users = searchAll(new Condition(SysUser.FIELD_ACCOUNT, QueryType.EQUAL, account));
        return (users != null && users.size() > 0) ? users.get(0) : null;
    }

    /**
     * 更新个人用户账号
     */
    public boolean updatePersonnelAccount(String userId, String newAccount) {
        return sysUserMapper.updateAccount(SysUser.USER_TYPE_PERSONNEL, userId, newAccount) > 0;
    }

    /**
     * 删除个人用户账号
     */
    public boolean removePersonnelAccount(String userId) {
        return remove(
                new Condition(SysUser.FIELD_USER_TYPE, QueryType.EQUAL, SysUser.USER_TYPE_PERSONNEL),
                new Condition(SysUser.FIELD_USER_ID, QueryType.EQUAL, userId)
        ) > 0;
    }

    /**
     * 更新最近一次登录时间
     */
    public void updateLastTime(String account) {
        // 不更新update_time
        SysUser sysUser = getUserByAccount(account);
        SysUser user = new SysUser();
        user.setId(sysUser.getId());
        user.setLastLoginTime(new Date());
        updateSelective(user);
    }
}
