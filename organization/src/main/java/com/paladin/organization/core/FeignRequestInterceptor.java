package com.paladin.organization.core;

import com.paladin.framework.security.UserSession;
import com.paladin.framework.security.WebSecurityManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author TontoZhou
 * @since 2020/9/1
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        UserSession userSession = WebSecurityManager.getCurrentUserSession();
        template.header(WebSecurityManager.HEADER_USER_ID, userSession.getUserId());
        template.header(WebSecurityManager.HEADER_USER_TYPE, userSession.getUserType());
    }
}
