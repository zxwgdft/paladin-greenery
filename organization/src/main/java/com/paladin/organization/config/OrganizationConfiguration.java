package com.paladin.organization.config;

import com.paladin.framework.security.SHATokenProvider;
import com.paladin.framework.security.TokenProvider;
import com.paladin.framework.service.DataContainerManager;
import com.paladin.framework.service.FileStoreService;
import com.paladin.framework.service.ServiceSupportManager;
import com.paladin.framework.service.impl.FtpFileStoreService;
import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainerManager;
import com.paladin.framework.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

/**
 * @author TontoZhou
 * @since 2019/12/10
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(OrganizationProperties.class)
public class OrganizationConfiguration {

    @Bean
    public TokenProvider getTokenProvider(Environment env) {
        SHATokenProvider tokenProvider = new SHATokenProvider();
        tokenProvider.setBase64Key(env.getProperty("jwt.key"));
        String expireMillisecondStr = env.getProperty("jwt.expire-millisecond");
        long expireMillisecond = StringUtil.isEmpty(expireMillisecondStr) ? 30 * 60 * 1000 : Long.valueOf(expireMillisecondStr);
        tokenProvider.setTokenExpireMilliseconds(expireMillisecond);
        tokenProvider.setIssuer(env.getProperty("jwt.issuer"));
        return tokenProvider;
    }


    /**
     * spring bean 获取帮助类
     */
    @Bean
    public SpringBeanHelper springBeanHolder() {
        return new SpringBeanHelper();
    }

    /**
     * mongodb事务管理器
     */
    @Bean
    public MongoTransactionManager transactionManager(MongoDbFactory factory) {
        return new MongoTransactionManager(factory);
    }

    /**
     * spring container 管理器（用于spring加载完毕后运行的对象）
     */
    @Bean
    public SpringContainerManager springContainerManager() {
        return new SpringContainerManager();
    }

    /**
     * 数据容器管理器
     */
    @Bean
    public DataContainerManager getDataContainerManager() {
        return new DataContainerManager();
    }

    /**
     * service支持管理器
     */
    @Bean
    public ServiceSupportManager getServiceSupportContainer() {
        return new ServiceSupportManager();
    }

    /**
     * 文件存储服务
     *
     * @return
     */
    @Bean
    public FileStoreService getFileStoreService(Environment env) {
        String host = env.getProperty("paladin.file.ftp-host");
        String port = env.getProperty("paladin.file.ftp-port");
        String username = env.getProperty("paladin.file.ftp-username");
        String password = env.getProperty("paladin.file.ftp-password");
        String visitHost = env.getProperty("paladin.file.ftp-visit-host");
        String visitPort = env.getProperty("paladin.file.ftp-visit-port");
        return new FtpFileStoreService(host, Integer.valueOf(port),
                username, password, visitHost, Integer.valueOf(visitPort));
    }

}
