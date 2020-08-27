package com.paladin.organization.web;

import com.paladin.common.CommonBusiness;
import com.paladin.organization.core.*;
import com.paladin.organization.model.SysUser;
import com.paladin.organization.service.dto.OAuthParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author TontoZhou
 * @since 2019/12/23
 */
@Api("人员认证")
@RestController
@RequestMapping("/organization")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OrgSecurityManager webSecurityManager;

    //TODO 是否固定域名跨域，还是对所有（安全性）
    @ApiOperation(value = "用户账号认证")
    @PostMapping("/authenticate/user")
    public OpenToken authenticateByAccount(@RequestBody LoginUser loginUser) {
        SysUser sysUser = authenticationManager.authenticateUser(loginUser);
        return webSecurityManager.createToken(sysUser.getId(), CommonBusiness.USER_TYPE_PERSONNEL);
    }

    //TODO 应用客户端后台认证，这里对所有跨域，但是会
    @ApiOperation(value = "应用客户端认证")
    @PostMapping("/authenticate/app")
    public OpenToken authenticateByApp(@RequestBody LoginApp loginApp) {
        String appId = authenticationManager.authenticateApp(loginApp);
        return webSecurityManager.createToken(appId, CommonBusiness.USER_TYPE_APP);
    }

    //TODO 如果登录h5页面由我们服务提供，则需要限定域名提高安全性，否则失去跳转我们页面进行页面的认证的意义
    @ApiOperation(value = "oauth2第三方授权认证")
    @PostMapping("/authenticate/oauth")
    public String authenticateByApp(@RequestBody OAuthParam oauthParam) {
        return authenticationManager.authenticateAndRedirect(oauthParam);
    }

    //TODO 是否固定域名跨域，还是对所有（安全性）
    @ApiOperation(value = "token刷新")
    @GetMapping("/authenticate/refresh")
    public OpenToken authenticateRefresh() {
        return webSecurityManager.refreshToken();
    }


    //TODO 注销功能是否要做

}
