package com.paladin.upload.config;

import com.paladin.framework.spring.DevelopCondition;
import com.paladin.framework.spring.web.DateFormatter;
import com.paladin.upload.core.UploadSecurityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;

@Slf4j
@Configuration
public class UploadWebConfigurer implements WebMvcConfigurer {

    @Value("${attachment.upload-folder}")
    private String filePath;

    @Autowired
    private Environment environment;

    @Autowired
    private UploadSecurityManager webSecurityManager;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath = this.filePath;

        if (!filePath.startsWith("file:")) {
            filePath = "file:" + filePath;
        }

        registry.addResourceHandler("/static/file/**").addResourceLocations(filePath);

        if (DevelopCondition.isDevelop(environment)) {
            registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
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
                .addPathPatterns("/upload/**");
    }
}
