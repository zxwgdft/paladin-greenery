package com.paladin.framework.security;

import com.paladin.framework.exception.SystemException;
import com.paladin.framework.exception.SystemExceptionCode;
import com.paladin.framework.utils.secure.RSAEncryptUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSATokenProvider extends TokenProvider {

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    public String createJWT(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.RS512, privateKey)
                .compact();
    }

    public UserClaims parseJWT(String jwtToken) {
        return convert(Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(jwtToken)
                .getBody());
    }

    public void setPublicKey(String base64PublicKey) {
        try {
            this.publicKey = RSAEncryptUtil.getRSAPublicKey(base64PublicKey);
        } catch (Exception e) {
            throw new SystemException(SystemExceptionCode.CODE_ERROR_CONFIG, "设置Base64公钥错误", e);
        }
    }

    public void setPrivateKey(String base64PrivateKey) {
        try {
            this.privateKey = RSAEncryptUtil.getRSAPrivateKey(base64PrivateKey);
        } catch (Exception e) {
            throw new SystemException(SystemExceptionCode.CODE_ERROR_CONFIG, "设置Base64私钥错误", e);
        }
    }

}
