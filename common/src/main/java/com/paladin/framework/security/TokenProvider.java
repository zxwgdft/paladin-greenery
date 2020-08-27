package com.paladin.framework.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Date;

/**
 * @author TontoZhou
 * @since 2019/12/26
 */
public abstract class TokenProvider {

    public final static String USER_ID = "uid";
    public final static String USER_TYPE = "utp";

    private long tokenExpireMilliseconds;
    private String issuer;

    public String createJWT(String userId, String userType, Date expiration) {
        Date now = new Date();
        if (expiration == null) {
            expiration = new Date(now.getTime() + this.tokenExpireMilliseconds);
        }

        DefaultClaims claims = new DefaultClaims();
        claims.put(USER_ID, userId);
        claims.put(USER_TYPE, userType);
        claims.put(Claims.ISSUED_AT, now);
        claims.put(Claims.ISSUER, issuer);
        claims.put(Claims.EXPIRATION, expiration);

        return createJWT(claims);
    }

    public abstract String createJWT(Claims claims);

    public abstract UserClaims parseJWT(String jwtToken);

    protected UserClaims convert(Claims claims) {
        return new UserClaims(claims);
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public void setTokenExpireMilliseconds(long tokenExpireMilliseconds) {
        this.tokenExpireMilliseconds = tokenExpireMilliseconds;
    }

    public long getExpireMilliseconds() {
        return this.tokenExpireMilliseconds;
    }


}
