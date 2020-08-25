package com.paladin.organization.web;


import com.paladin.framework.common.HttpCode;
import com.paladin.framework.common.R;
import com.paladin.framework.service.OffsetPage;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.spring.web.ControllerSupport;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.organization.model.Personnel;
import com.paladin.organization.model.SysAttachment;
import com.paladin.organization.service.PersonnelService;
import com.paladin.organization.service.SysAttachmentService;
import com.paladin.organization.service.dto.personnel.PersonnelSave;
import com.paladin.organization.service.dto.personnel.PersonnelUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @author TontoZhou
 * @since 2019/11/1
 */
@Api("人员管理")
@RestController
@RequestMapping("/organization/personnel")
public class PersonnelController extends ControllerSupport {

    public static final int ATTACHMENTS_MAX_SIZE = 4;

    @Autowired
    private PersonnelService personnelService;

    @Autowired
    private SysAttachmentService attachmentService;


    @ApiOperation(value = "获取某个人员信息")
    @GetMapping("/get")
    public Personnel getPersonnel(@RequestParam() String userId) {
        return personnelService.get(userId);
    }


    @ApiOperation(value = "获取人员信息列表-分页")
    @GetMapping("/find")
    public PageResult<Personnel> findPersonnel(OffsetPage param) {
        return personnelService.searchPage(param);
    }


    @ApiOperation(value = "保存人员基本信息")
    @PostMapping("/save")
    public R save(@Valid PersonnelSave save, @RequestParam(required = false) MultipartFile[] profilePhotoFile, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.fail(HttpCode.BAD_REQUEST, "请求参数异常", bindingResult.getAllErrors());
        }
        if (profilePhotoFile != null && profilePhotoFile.length > 0) {
            List<SysAttachment> attachments = attachmentService.mergeAttachments(save.getProfilePhoto(), profilePhotoFile);
            if (attachments != null && attachments.size() > 1) {
                return R.fail(HttpCode.BAD_REQUEST, "用户头像只能上传一张");
            }
            save.setProfilePhoto(attachmentService.splicingAttachmentId(attachments));
        }
        Personnel personnel = new Personnel();
        personnel.setId(UUIDUtil.createUUID());
        return R.success(personnelService.save(beanCopy(save, personnel)));
    }


    @ApiOperation(value = "修改人员基本信息")
    @PostMapping("/update")
    public R update(@Valid PersonnelUpdate update, @RequestParam(required = false) MultipartFile[] profilePhotoFile, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.fail(HttpCode.BAD_REQUEST, "请求参数异常", bindingResult.getAllErrors());
        }
        if (profilePhotoFile != null && profilePhotoFile.length > 0) {
            List<SysAttachment> attachments = attachmentService.mergeAttachments(update.getProfilePhoto(), profilePhotoFile);
            if (attachments != null && attachments.size() > 1) {
                return R.fail(HttpCode.BAD_REQUEST, "用户头像只能上传一张");
            }
            update.setProfilePhoto(attachmentService.splicingAttachmentId(attachments));
        }
        return R.success(personnelService.update(beanCopy(update, personnelService.get(update.getId()))));
    }

    @ApiOperation(value = "删除人员基本信息")
    @GetMapping("/delete")
    public R delete(@ApiParam(name = "人员ID", required = true) @RequestParam String userId) {
        personnelService.deletePersonnel(userId);
        return R.success();
    }


}

