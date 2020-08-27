package com.paladin.organization.core;

import com.paladin.common.CommonBusiness;
import com.paladin.framework.security.UserSession;

/**
 * @author TontoZhou
 * @since 2020/3/26
 */
public class AppClientSession extends UserSession {

    private AppClientDataLoader sessionLoader;

    public AppClientSession(String appId, AppClientDataLoader sessionLoader) {
        super(appId, CommonBusiness.USER_TYPE_APP);
        this.sessionLoader = sessionLoader;
    }

    private volatile boolean loaded = false;

    public void lazyLoad() {
        if (!loaded) {
            synchronized (this) {
                if (!loaded) {
                    sessionLoader.loadAppClientSession(this);
                    loaded = true;
                }
            }
        }
    }


}
