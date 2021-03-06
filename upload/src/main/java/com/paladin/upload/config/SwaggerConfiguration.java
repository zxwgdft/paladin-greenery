package com.paladin.upload.config;

import com.paladin.framework.spring.DevelopCondition;
import com.paladin.framework.swagger.CommonDocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author TontoZhou
 * @since 2019/12/30
 */
@Configuration
@Conditional(DevelopCondition.class)
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi() {
        return CommonDocketFactory.newInstance();
    }

}
