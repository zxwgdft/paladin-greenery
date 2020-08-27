package com.paladin.upload.core;

import com.paladin.framework.security.UserSession;
import com.paladin.framework.security.UserSessionFactory;
import com.paladin.framework.security.WebSecurityManager;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author TontoZhou
 * @since 2020/8/18
 */
@Component
public class UploadSecurityManager extends WebSecurityManager implements HandlerInterceptor, UserSessionFactory {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        getUserSession(request);
        return true;
    }

    @Override
    public UserSessionFactory getUserSessionFactory() {
        return this;
    }

    @Override
    public UserSession createUserSession(String userId, String userType) {
        return new UserSession(userId, userType);
    }
}
