package com.paladin.organization.service;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.organization.core.AppClientSession;
import com.paladin.organization.core.OrgUserSession;
import com.paladin.organization.core.OrgSecurityManager;
import com.paladin.organization.model.App;
import com.paladin.organization.model.SysUser;
import com.paladin.organization.service.constant.RedisKeyPrefix;
import com.paladin.organization.service.dto.AppRedirect;
import com.paladin.organization.service.vo.OpenPersonnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * APP跳转重定向服务
 *
 * @author TontoZhou
 * @since 2020/3/25
 */
@Service
public class AppRedirectService {

    @Autowired
    private AppService appService;
    @Autowired
    private PersonnelService personnelService;

    @Autowired
    @Qualifier("jsonRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    // 跳转码失效时间
    @Value("${app.redirect-code-time-out:5}")
    private int redirectCodeTimeOut;


    /**
     * 获取用户跳转确认码
     */
    private AppRedirect getRedirect(String redirectCode) {
        return (AppRedirect) redisTemplate.opsForValue().get(getRedirectKey(redirectCode));
    }

    /**
     * 持久化跳转确认码
     */
    private void saveRedirect(AppRedirect appRedirect) {
        redisTemplate.opsForValue().set(getRedirectKey(appRedirect.getCode()), appRedirect, redirectCodeTimeOut, TimeUnit.MINUTES);
    }

    /**
     * 确认码在Redis中的Key
     */
    private String getRedirectKey(String redirectCode) {
        return RedisKeyPrefix.PREFIX_REDIRECT_CONFIRM_CODE + "_" + redirectCode;
    }

    /**
     * 获取重定向应用的URL
     *
     * @param appId 跳转重定向的APP ID
     * @return 返回重定向URL，包括了用户ID和确认码
     */
    public String getRedirectUrl(String appId) {
        App app = appService.getAppByAppId(appId);
        if (app == null) {
            throw new BusinessException("找不到对应应用系统[ID:" + appId + "]");
        }

        // TODO 判断是否有权跳转应用

        String url = app.getRedirectUrl();
        OrgUserSession session = OrgSecurityManager.getCurrentOrgUserSession();
        if (session == null) {
            throw new BusinessException("登录用户没有权限重定向跳转");
        }

        // 只有绑定了personnel的用户才能重定向
        String personnelId = session.getPersonnelId();
        if (StringUtil.isEmpty(personnelId)) {
            throw new BusinessException("未绑定人员的用户无法重定向到应用");
        }

        return createRedirectUrl(app, personnelId);
    }

    /**
     * 获取重定向应用的URL
     *
     * @param appId 跳转重定向的APP ID
     * @return 返回重定向URL，包括了用户ID和确认码
     */
    public String getRedirectUrlByOauth(String appId, SysUser sysUser) {
        App app = appService.getAppByAppId(appId);
        if (app == null) {
            throw new BusinessException("找不到对应应用系统[ID:" + appId + "]");
        }

        // TODO 判断是否有权跳转应用
        String personnelId = sysUser.getPersonnelId();
        if (StringUtil.isEmpty(personnelId)) {
            throw new BusinessException("未绑定人员的用户无法重定向到应用");
        }

        return createRedirectUrl(app, personnelId);
    }

    /**
     * 创建重定向URL
     */
    private String createRedirectUrl(App app, String personnelId) {
        String url = app.getRedirectUrl();
        // 确认码用于APP确认是否有该用户跳转登录，防止未经服务器的跳转登录
        String redirectCode = UUIDUtil.createUUID();

        AppRedirect appRedirect = new AppRedirect();
        appRedirect.setAppId(app.getAppId());
        appRedirect.setUserId(personnelId);
        appRedirect.setCode(redirectCode);

        saveRedirect(appRedirect);

        // 拼接URL
        int i = url.indexOf("?");
        if (i < 0) {
            url += "?";
        } else if (i < url.length() - 1) {
            url += "&";
        }
        url += "redirectCode=" + redirectCode;

        return url;
    }

    /**
     * 检查跳转重定向是否合法，如果合法则返回相关人员数据
     *
     * @param redirectCode 重定向code
     * @return 如果合法则返回用户信息，否则返回错误相关信息
     */
    public OpenPersonnel checkRedirect(String redirectCode) {
        AppRedirect appRedirect = getRedirect(redirectCode);
        if (appRedirect == null) {
            throw new BusinessException("没有需要跳转的用户请求，或者跳转请求已经过期");
        }

        AppClientSession session = OrgSecurityManager.getCurrentAppClientSession();
        if (session == null) {
            throw new BusinessException("登录用户非应用客户端");
        }

        // 判断是否同一个app
        if (session.getUserId().equals(appRedirect.getAppId())) {
            OpenPersonnel personnel = personnelService.get(appRedirect.getUserId(), OpenPersonnel.class);
            if (personnel == null) {
                throw new BusinessException("用户信息已经不存在");
            }
            return personnel;
        } else {
            throw new BusinessException("跳转请求不匹配");
        }
    }
}
