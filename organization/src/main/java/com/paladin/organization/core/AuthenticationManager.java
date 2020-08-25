package com.paladin.organization.core;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.secure.SecureUtil;
import com.paladin.organization.model.App;
import com.paladin.organization.model.SysUser;
import com.paladin.organization.model.constant.AppConstant;
import com.paladin.organization.service.AppRedirectService;
import com.paladin.organization.service.AppService;
import com.paladin.organization.service.SysUserService;
import com.paladin.organization.service.dto.OAuthParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author TontoZhou
 * @since 2020/3/26
 */
@Component
public class AuthenticationManager {


    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AppService appService;
    @Autowired
    private AppRedirectService appRedirectService;


    /**
     * 用户登录
     *
     * @param loginUser
     * @return 用户信息
     */
    public SysUser authenticateUser(LoginUser loginUser) {
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (StringUtil.isNotEmpty(username) || StringUtil.isNotEmpty(password)) {
            SysUser sysUser = sysUserService.getUserByAccount(username);
            if (sysUser != null) {
                int state = sysUser.getState();
                if (state == AppConstant.USER_STATE_STOP) {
                    throw new BusinessException("账号不可用");
                }

                if (state != AppConstant.USER_STATE_ENABLED) {
                    throw new BusinessException("账号不可用");
                }

                // TODO 增加手机登录（手机账号或手机验证码登录）

                if (SecureUtil.hashByMD5(password, sysUser.getSalt()).equals(sysUser.getPassword())) {
                    return sysUser;
                } else {
                    throw new BusinessException("密码错误");
                }
            } else {
                throw new BusinessException("账号不存在");
            }
        } else {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "账号或密码不能为空");
        }
    }


    /**
     * APP客户端登录
     *
     * @param loginApp
     * @return appId
     */
    public String authenticateApp(LoginApp loginApp) {
        String appId = loginApp.getAppId();
        String appSecret = loginApp.getAppSecret();
        if (StringUtil.isNotEmpty(appId) || StringUtil.isNotEmpty(appSecret)) {
            App app = appService.getAppByAppId(appId);
            if (app != null) {
                int state = app.getState();
                if (state == AppConstant.APP_STATE_STOP) {
                    throw new BusinessException("应用客户端已停用");
                }

                if (state != AppConstant.APP_STATE_ENABLED) {
                    throw new BusinessException("应用客户端不可用");
                }

                // TODO 判断请求地址是否是与APP相同域名，安全性考虑

                if (appSecret.equals(app.getAppSecret())) {
                    return appId;
                } else {
                    throw new BusinessException("秘钥错误");
                }
            } else {
                throw new BusinessException("应用客户端不存在");
            }
        } else {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "应用ID与秘钥不能为空");
        }
    }


    /**
     * oauth授权验证并返回带有验证确认码的重定向url
     *
     * @param oauthParam
     * @return 返回重定向跳转的url
     */
    public String authenticateAndRedirect(OAuthParam oauthParam) {
        SysUser sysUser = authenticateUser(new LoginUser(oauthParam.getUsername(), oauthParam.getPassword(), false));
        // 返回重定向
        return appRedirectService.getRedirectUrlByOauth(oauthParam.getAppId(), sysUser);
    }

}
