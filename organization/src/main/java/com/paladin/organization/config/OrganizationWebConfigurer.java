package com.paladin.organization.config;

import com.paladin.framework.spring.web.DateFormatter;
import com.paladin.organization.core.OrgSecurityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;

@Slf4j
@Configuration
public class OrganizationWebConfigurer implements WebMvcConfigurer {

    @Autowired
    private OrgSecurityManager webSecurityManager;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String staticPath = "classpath:/static/";
        String faviconPath = "classpath:favicon.ico";

        registry.addResourceHandler("/static/**").addResourceLocations(staticPath);
        //registry.addResourceHandler("/file/**").addResourceLocations(filePath);
        registry.addResourceHandler("/favicon.ico").addResourceLocations(faviconPath);

        //log.info("文件资源存放地址：" + filePath);
        log.info("静态资源存放地址：" + staticPath);
        log.info("favicon存放地址：" + faviconPath);

        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registry.addViewController("/").setViewName("redirect:/login");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Date.class, new DateFormatter());
        //registry.addConverterFactory(new Integer2EnumConverterFactory());
        //registry.addConverterFactory(new String2EnumConverterFactory());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webSecurityManager)
                .addPathPatterns("/**")
                .excludePathPatterns("/organization/authenticate/**");
    }


}
