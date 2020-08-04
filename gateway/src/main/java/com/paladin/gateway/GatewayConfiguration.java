package com.paladin.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;

/**
 * @author TontoZhou
 * @since 2019/12/10
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(GlobalCorsProperties.class)
public class GatewayConfiguration {

    //TODO 可以通过nginx解决跨域问题
    @Bean
    @ConditionalOnProperty(prefix = "paladin", value = "cors-enabled", havingValue = "true", matchIfMissing = false)
    public CorsWebFilter corsFilter(GlobalCorsProperties globalCorsProperties) {
        Map<String, CorsConfiguration> corsConfigMap = globalCorsProperties.getCorsConfigurations();
        if (corsConfigMap.size() == 0) {
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            corsConfigMap.put("/**", config);
        }
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        for (Map.Entry<String, CorsConfiguration> entry : corsConfigMap.entrySet()) {
            source.registerCorsConfiguration(entry.getKey(), entry.getValue());
        }
        return new CorsWebFilter(source);
    }

}
