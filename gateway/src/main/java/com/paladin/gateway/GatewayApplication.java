package com.paladin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author TontoZhou
 * @since 2019/12/23
 */

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.paladin.gateway.dao.mapper")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
