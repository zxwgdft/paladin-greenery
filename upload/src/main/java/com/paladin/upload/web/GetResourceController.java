package com.paladin.upload.web;

import com.paladin.upload.service.FileResourceContainer;
import com.paladin.upload.service.vo.FileResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/9/2
 */
@Api("资源访问")
@Controller
@RequestMapping("/upload/get")
public class GetResourceController {


    @ApiOperation(value = "通过ID获取附件")
    @GetMapping("/attachment")
    @ResponseBody
    public FileResource getAttachment(@RequestParam("id") String id) {
        return FileResourceContainer.getFileResource(id);
    }

    @ApiOperation(value = "通过ID获取多个附件")
    @GetMapping("/attachments")
    @ResponseBody
    public List<FileResource> getAttachments(@RequestParam("id[]") String[] ids) {
        return FileResourceContainer.getFileResources(ids);
    }

}
