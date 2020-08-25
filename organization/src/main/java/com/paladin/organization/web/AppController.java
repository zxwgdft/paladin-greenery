package com.paladin.organization.web;

import com.paladin.organization.model.App;
import com.paladin.organization.service.AppRedirectService;
import com.paladin.organization.service.AppService;
import com.paladin.organization.service.vo.OpenPersonnel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2019/11/1
 */
@Api("应用管理")
@RestController
@RequestMapping("/organization/app")
public class AppController {

    @Autowired
    private AppService appService;

    @Autowired
    private AppRedirectService appRedirectService;

    @ApiOperation(value = "获取某个应用")
    @GetMapping("/get")
    public App getApp(@RequestParam() String appId) {
        return appService.get(appId);
    }

    @ApiOperation(value = "获取所有应用")
    @GetMapping("/find/all")
    public List<App> findApps() {
        return appService.findAll();
    }

    @ApiOperation(value = "跳转重定向到APP")
    @GetMapping("/redirect")
    public String redirectApp(String appId) {
        return appRedirectService.getRedirectUrl(appId);
    }

    @ApiOperation(value = "检查跳转是否合法")
    @PostMapping("/check")
    public OpenPersonnel checkRedirect(String redirectCode) {
        return appRedirectService.checkRedirect(redirectCode);
    }

}
