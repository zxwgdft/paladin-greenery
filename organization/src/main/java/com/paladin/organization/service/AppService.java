package com.paladin.organization.service;

import com.paladin.framework.service.Condition;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.organization.model.App;
import org.springframework.stereotype.Service;

/**
 * @author TontoZhou
 * @since 2019/12/12
 */
@Service
public class AppService extends ServiceSupport<App> {

    public App getAppByAppId(String appId) {
        return searchOne(new Condition(App.FIELD_APP_ID, QueryType.EQUAL, appId));
    }
}
