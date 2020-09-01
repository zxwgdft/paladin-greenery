package com.paladin.organization.web;


import com.paladin.framework.common.HttpCode;
import com.paladin.framework.common.R;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.spring.web.ControllerSupport;
import com.paladin.organization.service.PersonnelService;
import com.paladin.organization.service.dto.PersonnelQuery;
import com.paladin.organization.service.dto.PersonnelSave;
import com.paladin.organization.service.dto.PersonnelUpdate;
import com.paladin.organization.service.vo.OpenPersonnel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author TontoZhou
 * @since 2019/11/1
 */
@Api("人员管理")
@RestController
@RequestMapping("/organization/personnel")
public class PersonnelController extends ControllerSupport {

    @Autowired
    private PersonnelService personnelService;

    @ApiOperation(value = "获取某个人员信息")
    @GetMapping("/get")
    public OpenPersonnel getPersonnel(@RequestParam() String id) {
        return personnelService.get(id, OpenPersonnel.class);
    }


    @ApiOperation(value = "获取人员信息列表-分页")
    @GetMapping("/find")
    public PageResult<OpenPersonnel> findPersonnel(PersonnelQuery param) {
        return personnelService.searchPage(param, OpenPersonnel.class);
    }


    @ApiOperation(value = "保存人员基本信息")
    @PostMapping("/save")
    public R save(@Valid PersonnelSave save, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validErrorHandler(bindingResult);
        }
        personnelService.savePersonnel(save);
        return R.success();
    }


    @ApiOperation(value = "修改人员基本信息")
    @PostMapping("/update")
    public R update(@Valid PersonnelUpdate update, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.fail(HttpCode.BAD_REQUEST, "请求参数异常", bindingResult.getAllErrors());
        }
        personnelService.updatePersonnel(update);
        return R.success();
    }

    @ApiOperation(value = "删除人员基本信息")
    @GetMapping("/delete")
    public R delete(@ApiParam(name = "人员ID", required = true) @RequestParam String userId) {
        personnelService.deletePersonnel(userId);
        return R.success();
    }


}

