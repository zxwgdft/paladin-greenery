package com.paladin.gateway.util;

import com.paladin.framework.exception.SystemException;
import com.paladin.framework.exception.SystemExceptionCode;
import com.paladin.framework.utils.convert.JsonUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

/**
 * WebFlux工具类
 *
 * @author TontoZhou
 * @since 2019/12/26
 */
public class WebFluxUtil {

    /**
     * 写入数据到响应
     *
     * @param serverWebExchange 契约
     * @param status            HttpStatus状态
     * @param data              数据
     * @return
     */
    public static Mono<Void> writeResponse(ServerWebExchange serverWebExchange, HttpStatus status, byte[] data) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = response
                .bufferFactory().wrap(data);
        return response.writeWith(Flux.just(buffer));
    }

    /**
     * 写入String格式数据到响应
     *
     * @param serverWebExchange 契约
     * @param status            HttpStatus状态
     * @param data              数据
     * @return
     */
    public static Mono<Void> writeStringResponse(ServerWebExchange serverWebExchange, HttpStatus status, String data) {
        return writeResponse(serverWebExchange, status, data == null ? new byte[0] : data.getBytes());
    }

    /**
     * 写入JSON格式数据到响应
     *
     * @param serverWebExchange 契约
     * @param status            HttpStatus状态
     * @param data              数据
     * @return
     */
    public static Mono<Void> writeResponseByJson(ServerWebExchange serverWebExchange, HttpStatus status, Object data) {
        try {
            byte[] bystes = data == null ? new byte[0] : JsonUtil.getJson(data).getBytes();
            return writeResponse(serverWebExchange, status, bystes);
        } catch (IOException e) {
            throw new SystemException(SystemExceptionCode.CODE_ERROR_CODE, "[" + (data == null ? "null" : data.getClass()) + "]无法转化为json格式", e);
        }
    }

    /**
     * 添加请求头，构建新的请求响应契约
     *
     * @param serverWebExchange 原契约
     * @param name              头名称
     * @param values            头值数组
     * @return 返回新契约
     */
    public static ServerWebExchange addRequestHeader(ServerWebExchange serverWebExchange, String name, String... values) {
        ServerHttpRequest request = serverWebExchange.getRequest().mutate().header(name, values).build();
        return serverWebExchange.mutate().request(request).build();
    }



}
