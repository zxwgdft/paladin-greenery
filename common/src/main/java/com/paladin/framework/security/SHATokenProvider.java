package com.paladin.framework.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

public class SHATokenProvider extends TokenProvider {

    private byte[] keyBytes;

    public String createJWT(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, keyBytes)
                .compact();
    }

    public UserClaims parseJWT(String jwtToken) {
        return convert(Jwts.parser()
                .setSigningKey(keyBytes)
                .parseClaimsJws(jwtToken)
                .getBody());
    }

    public void setBase64Key(String base64Key) {
        this.keyBytes = Base64.decodeBase64(base64Key);
    }

}
