package com.paladin.organization.web;

import com.paladin.framework.common.HttpCode;
import com.paladin.framework.common.R;
import com.paladin.organization.model.AppResource;
import com.paladin.organization.model.AppResourceModel;
import com.paladin.organization.service.AppResourceService;
import com.paladin.organization.service.dto.AppResourceModelSave;
import com.paladin.organization.service.dto.AppResourceSave;
import com.paladin.organization.service.dto.AppResourceUpdate;
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
@Api("应用资源管理")
@RestController
@RequestMapping("/organization/app/resource")
public class AppResourceController {

    @Autowired
    private AppResourceService appResourceService;

    @ApiOperation(value = "获取某个应用的所有资源")
    @GetMapping("/get")
    public List<AppResource> getResourceByApp(@RequestParam String appId) {
        return appResourceService.findAppResource(appId);
    }

    @ApiOperation(value = "新增一个应用资源")
    @PostMapping("/save")
    public R saveAppResource(@Valid @RequestBody AppResourceSave resourceSave, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.fail(HttpCode.BAD_REQUEST, "请求参数异常", bindingResult.getAllErrors());
        }
        return R.success(appResourceService.createResource(resourceSave));
    }

    @ApiOperation(value = "更新一个应用资源")
    @PostMapping("/update/properties")
    public R updateAppResourceProperties(@RequestBody AppResourceUpdate resourceUpdate) {
        appResourceService.updateResourceProperties(resourceUpdate);
        return R.success();
    }

    @ApiOperation(value = "更新一个应用资源")
    @PostMapping("/update/parent")
    public R updateAppResourceParent(@RequestParam String resourceId, @RequestParam String parentId) {
        appResourceService.updateResourceParent(resourceId, parentId);
        return R.success();
    }


    @ApiOperation(value = "获取某个应用的资源模型")
    @GetMapping("/model/get")
    public List<AppResourceModel> getResourceModelByApp(@RequestParam String appId) {
        return appResourceService.findAppResourceModels(appId);
    }

    @ApiOperation(value = "新增一个应用资源模型")
    @PostMapping("/model/save")
    public R saveAppResourceModel(@RequestBody AppResourceModelSave modelSave) {
        return R.success(appResourceService.createResourceModel(modelSave));
    }


}
