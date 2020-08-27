package com.paladin.framework.security;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.StringUtil;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author TontoZhou
 * @since 2020/8/25
 */
public abstract class WebSecurityManager {

    public static final String HEADER_USER_ID = "user-id";
    public static final String HEADER_USER_TYPE = "user-type";

    private final static ThreadLocal<UserSession> sessionMap = new ThreadLocal<>();

    public abstract UserSessionFactory getUserSessionFactory();

    public void getUserSession(HttpServletRequest request) throws Exception {
        String userId = request.getHeader(HEADER_USER_ID);
        String userType = request.getHeader(HEADER_USER_TYPE);
        if (StringUtil.isNotEmpty(userId) && StringUtil.isNotEmpty(userType)) {
            UserSession userSession = getUserSessionFactory().createUserSession(userId, userType);
            if (userSession != null) {
                sessionMap.set(userSession);
                return;
            }
        }
        throw new BusinessException(HttpStatus.UNAUTHORIZED, "未认证，请先登录");
    }

    public static UserSession getCurrentUserSession() {
        return sessionMap.get();
    }

}
