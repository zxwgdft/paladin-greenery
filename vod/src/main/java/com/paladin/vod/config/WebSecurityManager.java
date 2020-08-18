package com.paladin.vod.config;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.jwt.TokenProvider;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author TontoZhou
 * @since 2020/8/18
 */
@Component
public class WebSecurityManager implements HandlerInterceptor {

    private final static ThreadLocal<String> sessionMap = new ThreadLocal<>();

    public static String getCurrentUser() {
        return sessionMap.get();
    }

    @Autowired
    private TokenProvider tokenProvider;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.length() > 0) {
            try {
                Claims claims = tokenProvider.parseJWT(token);
                String subject = claims.getSubject();
                sessionMap.set(subject);
                return true;
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
                throw new BusinessException(HttpStatus.UNAUTHORIZED, "授权访问失败，无效Token");
            } catch (ExpiredJwtException e) {
                throw new BusinessException(HttpStatus.UNAUTHORIZED, "授权访问失败，Token已过期");
            }
        }

        throw new BusinessException(HttpStatus.UNAUTHORIZED, "未授权访问");
    }

}
