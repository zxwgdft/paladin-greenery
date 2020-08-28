package com.paladin.upload.config;

import com.paladin.framework.service.DataContainerManager;
import com.paladin.framework.service.FileStoreService;
import com.paladin.framework.service.ServiceSupportManager;
import com.paladin.framework.service.impl.LocalFileStoreService;
import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * @author TontoZhou
 * @since 2019/12/10
 */
@Slf4j
@Configuration
public class UploadConfiguration {

    /**
     * 启用异常统一处理
     *
     * @return
     */
    @Bean
    public HandlerExceptionResolver getHandlerExceptionResolver() {
        return new UploadHandlerExceptionResolver();
    }

    /**
     * 数据容器管理器
     *
     * @return
     */
    @Bean
    public DataContainerManager getDataContainerManager() {
        return new DataContainerManager();
    }

    /**
     * spring bean 获取帮助类
     */
    @Bean
    public SpringBeanHelper springBeanHolder() {
        return new SpringBeanHelper();
    }


    /**
     * service支持管理器
     */
    @Bean
    public ServiceSupportManager getServiceSupportContainer() {
        return new ServiceSupportManager();
    }


    @Bean
    public SpringContainerManager springContainerManager() {
        return new SpringContainerManager();
    }

    /**
     * 文件存储服务
     *
     * @return
     */
    @Bean
    public FileStoreService getFileStoreService(Environment env) {
        String folder = env.getProperty("attachment.upload-folder");
        String visitUrl = env.getProperty("attachment.visit-base-url");

        FileStoreService fileStoreService = new LocalFileStoreService(folder) {
            
//            @Override
//            public String getStoreType() {
//                return "local";
//            }

            @Override
            public String getFileUrl(String relativePath) {
                return visitUrl + relativePath;
            }
        };

        return fileStoreService;
    }

}
