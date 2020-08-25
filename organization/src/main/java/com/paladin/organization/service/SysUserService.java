package com.paladin.organization.service;

import com.paladin.framework.service.Condition;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.organization.model.SysUser;
import org.springframework.stereotype.Service;

/**
 * @author TontoZhou
 * @since 2019/12/12
 */
@Service
public class SysUserService extends ServiceSupport<SysUser> {


    public SysUser getUserByAccount(String account) {
        return searchOne(new Condition(SysUser.FIELD_ACCOUNT, QueryType.EQUAL, account));
    }


}
