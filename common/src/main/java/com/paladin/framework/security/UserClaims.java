package com.paladin.framework.security;

import io.jsonwebtoken.Claims;

/**
 * @author TontoZhou
 * @since 2020/8/27
 */
public class UserClaims {

    private Claims claims;

    public UserClaims(Claims claims) {
        this.claims = claims;
    }

    public String getUserId() {
        return (String) claims.get(TokenProvider.USER_ID);
    }

    public String getUserType() {
        return (String) claims.get(TokenProvider.USER_TYPE);
    }

    public Claims getClaims() {
        return claims;
    }

}
