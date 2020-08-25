package com.paladin.framework.security;

/**
 * @author TontoZhou
 * @since 2020/1/2
 */
public interface UserSessionFactory {

    UserSession createUserSession(String subject);

}
