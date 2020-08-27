package com.paladin.organization.core;

import com.paladin.common.CommonBusiness;
import com.paladin.framework.security.UserSession;
import com.paladin.organization.model.Personnel;

/**
 * @author TontoZhou
 * @since 2020/1/2
 */
public class OrgUserSession extends UserSession {

    private OrgUserDataLoader orgUserSessionLoad;

    public OrgUserSession(String userId, OrgUserDataLoader orgUserSessionLoad) {
        super(userId, CommonBusiness.USER_TYPE_PERSONNEL);
        this.orgUserSessionLoad = orgUserSessionLoad;
    }

    private volatile boolean loaded = false;
    private volatile boolean loaded_personnel = false;

    public void lazyLoad() {
        if (!loaded) {
            synchronized (this) {
                if (!loaded) {
                    orgUserSessionLoad.loadUserSession(this);
                    loaded = true;
                }
            }
        }
    }

    public void lazyLoadPersonnel() {
        if (!loaded_personnel) {
            synchronized (this) {
                if (!loaded_personnel) {
                    orgUserSessionLoad.loadPersonnel(this);
                    loaded_personnel = true;
                }
            }
        }
    }

    protected boolean isSystemAdmin;
    protected String userName;
    protected String personnelId;

    public String getUserName() {
        lazyLoad();
        return userName;
    }

    public boolean isSystemAdmin() {
        lazyLoad();
        return isSystemAdmin;
    }

    public String getPersonnelId() {
        lazyLoad();
        return personnelId;
    }


    protected Personnel personnel;

    public Personnel getPersonnel() {
        lazyLoad();
        lazyLoadPersonnel();
        return personnel;
    }

}
