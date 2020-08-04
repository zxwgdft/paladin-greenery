package com.paladin.gateway.filter;

import com.paladin.framework.common.HttpCode;
import com.paladin.framework.common.R;
import com.paladin.framework.jwt.TokenProvider;
import com.paladin.gateway.util.WebFluxUtil;
import io.jsonwebtoken.*;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author TontoZhou
 * @since 2019/12/23
 */
public class AuthFilter implements GatewayFilter, Ordered {

    private AuthGatewayFilterFactory.Config config;

    private TokenProvider tokenProvider;

    public AuthFilter(AuthGatewayFilterFactory.Config config) {
        this.config = config;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst(config.getTokenField());

        if (token != null && token.length() > 0) {
            try {
                Claims claims = tokenProvider.parseJWT(token);
                String subject = claims.getSubject();

                // subject 一般为user_id
                exchange = WebFluxUtil.addRequestHeader(exchange, config.getUserIdField(), subject);

//                // 过期暂时不处理，由客户端处理
//                Date expirationTime = claims.getExpiration();
//                if (expirationTime.getTime() - System.currentTimeMillis() < config.getUpdateTokenIdle()) {
//                    // 剩余过期时间小于一定值，则刷新一个新jwtToken
//                    String newToken = tokenProvider.createJWT(subject);
//                    // 在相应头中添加新Token
//                    exchange.getResponse().getHeaders().add(config.getRefreshTokenField(), newToken);
//                }

                return chain.filter(exchange);
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
                return WebFluxUtil.writeResponseByJson(exchange, HttpStatus.UNAUTHORIZED, R.fail(HttpCode.UNAUTHORIZED, "授权访问失败，无效Token"));
            } catch (ExpiredJwtException e) {
                return WebFluxUtil.writeResponseByJson(exchange, HttpStatus.UNAUTHORIZED, R.fail(HttpCode.UNAUTHORIZED, "授权访问失败，Token已过期"));
            }
        }

        return WebFluxUtil.writeResponseByJson(exchange, HttpStatus.UNAUTHORIZED, R.fail(HttpCode.UNAUTHORIZED, "未授权访问"));
    }


    @Override
    public int getOrder() {
        return 0;
    }

    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
}
