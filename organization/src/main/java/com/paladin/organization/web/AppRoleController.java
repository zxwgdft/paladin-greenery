package com.paladin.organization.web;

import com.paladin.framework.common.HttpCode;
import com.paladin.framework.common.R;
import com.paladin.organization.model.AppRole;
import com.paladin.organization.model.AppRoleModel;
import com.paladin.organization.service.AppRoleService;
import com.paladin.organization.service.dto.AppRoleModelSave;
import com.paladin.organization.service.dto.AppRoleSave;
import com.paladin.organization.service.dto.AppRoleUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author TontoZhou
 * @since 2019/11/1
 */
@Api("应用角色管理")
@RestController
@RequestMapping("/organization/app/role")
public class AppRoleController {

    @Autowired
    private AppRoleService appRoleService;

    @ApiOperation(value = "获取某个应用的所有角色")
    @GetMapping("/get")
    public List<AppRole> getRoleByApp(@RequestParam String appId) {
        return appRoleService.findAppRole(appId);
    }

    @ApiOperation(value = "新增一个应用角色")
    @PostMapping("/save")
    public R saveAppRole(@Valid @RequestBody AppRoleSave roleSave, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.fail(HttpCode.BAD_REQUEST, "请求参数异常", bindingResult.getAllErrors());
        }
        return R.success(appRoleService.createRole(roleSave));
    }

    @ApiOperation(value = "更新一个应用角色")
    @PostMapping("/update/properties")
    public R updateAppRoleProperties(@RequestBody AppRoleUpdate roleUpdate) {
        appRoleService.updateRoleProperties(roleUpdate);
        return R.success();
    }

    @ApiOperation(value = "授权资源给角色")
    @PostMapping("/grant")
    public R updateAppRoleParent(@RequestParam String roleId, @RequestParam List<String> resourceIds) {
        appRoleService.grantResource(roleId, resourceIds);
        return R.success();
    }


    @ApiOperation(value = "获取某个应用的角色模型")
    @GetMapping("/model/get")
    public List<AppRoleModel> getRoleModelByApp(@RequestParam String appId) {
        return appRoleService.findAppRoleModels(appId);
    }

    @ApiOperation(value = "新增一个应用角色模型")
    @PostMapping("/model/save")
    public R saveAppRoleModel(@RequestBody AppRoleModelSave modelSave) {
        return R.success(appRoleService.createRoleModel(modelSave));
    }


}
