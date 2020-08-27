package com.paladin.gateway.filter;

import com.paladin.framework.common.HttpCode;
import com.paladin.framework.common.R;
import com.paladin.framework.security.TokenProvider;
import com.paladin.framework.security.UserClaims;
import com.paladin.gateway.util.WebFluxUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
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
                UserClaims userClaims = tokenProvider.parseJWT(token);

                ServerHttpRequest.Builder builder = request.mutate();

                builder.header(config.getUserIdField(), userClaims.getUserId());
                builder.header(config.getUserTypeField(), userClaims.getUserType());

                exchange = exchange.mutate().request(builder.build()).build();

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
