package com.paladin.framework.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.io.Serializable;

/**
 * 用户会话信息
 *
 * @author TontoZhou
 * @since 2018年1月29日
 */
public abstract class UserSession implements Serializable {

    private String userId;
    private String userName;
    private String account;

    public UserSession(String userId, String userName, String account) {
        this.userId = userId;
        this.userName = userName;
        this.account = account;
    }

    /**
     * 获取当前用户会话
     *
     * @return
     */
    public static UserSession getCurrentUserSession() {
        return (UserSession) SecurityUtils.getSubject().getPrincipal();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAccount() {
        return account;
    }


    public abstract boolean isSystemAdmin();

    private final static String ATTRIBUTION_PLACEHOLDER = "paladin_session_placeholder";

    /**
     * 如果session使用了redis方式缓存或其他非内存方式持久化（启用了ControlledSession）则需要在UserSession中值变化时通知session变更;
     * 大多数时候UserSession的值不应该变化;
     * ControlledSession会通过策略减少session持久化次数
     */
    public void onChange() {
        Session session = SecurityUtils.getSubject().getSession();
        // 插入一个无实际意义变量，通过该方法触发session change事件
        session.setAttribute(ATTRIBUTION_PLACEHOLDER, 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final UserSession that = (UserSession) o;
        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

}
