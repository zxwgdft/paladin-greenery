package com.paladin.organization.core;

import com.paladin.framework.security.UserSession;

/**
 * @author TontoZhou
 * @since 2020/3/26
 */
public class AppClientSession extends UserSession {

    private String appId;
    private AppClientDataLoader sessionLoader;

    public AppClientSession(String appId, AppClientDataLoader sessionLoader) {
        this.appId = appId;
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

    @Override
    public String getUserId() {
        return appId;
    }


}
