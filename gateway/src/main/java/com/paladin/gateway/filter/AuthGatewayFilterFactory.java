package com.paladin.gateway.filter;

import com.paladin.framework.jwt.RSATokenProvider;
import com.paladin.framework.jwt.SHATokenProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * 验证网关管理器工厂
 *
 * @author TontoZhou
 * @since 2019/12/23
 */
@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    private final static String JWT_TYPE_RSA = "RSA";
    private final static String JWT_TYPE_SHA = "SHA";

    public AuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        AuthFilter filter = new AuthFilter(config);

        if (JWT_TYPE_RSA.equals(config.getJwtType())) {
            RSATokenProvider tokenProvider = new RSATokenProvider();
            tokenProvider.setPublicKey(config.getJwtBase64Key());
            filter.setTokenProvider(tokenProvider);
        } else {
            SHATokenProvider tokenProvider = new SHATokenProvider();
            tokenProvider.setBase64Key(config.getJwtBase64Key());
            filter.setTokenProvider(tokenProvider);
        }

        return filter;
    }

    @Getter
    @Setter
    public static class Config {

        /**
         * Token域
         */
        private String tokenField = HttpHeaders.AUTHORIZATION;

        /**
         * 刷新Token域
         */
        private String refreshTokenField = HttpHeaders.AUTHORIZATION;

        /**
         * 用户ID域
         */
        private String userIdField = "User-ID";

        /**
         * 更新token的间隔，过期剩余时间如果小于这个间隔，则将更新新的TOKEN，该值应该小于token过期时间
         */
        private long updateTokenIdle = 10 * 60 * 1000;

        /**
         * Token签名加密类型，RSA、SHA
         */
        private String JwtType = JWT_TYPE_SHA;

        /**
         * base64格式秘钥，如果是RSA则是公钥
         */
        private String jwtBase64Key;


    }

}
