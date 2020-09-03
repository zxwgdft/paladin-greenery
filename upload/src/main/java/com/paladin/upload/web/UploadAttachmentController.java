package com.paladin.upload.web;

import com.paladin.upload.model.UploadAttachment;
import com.paladin.upload.service.UploadAttachmentService;
import com.paladin.upload.service.dto.MergeFileBase64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api("附件上传")
@Controller
@RequestMapping("/upload/attachment")
public class UploadAttachmentController {

    @Autowired
    private UploadAttachmentService attachmentService;


    @ApiOperation(value = "上传base64格式的附件文件")
    @PostMapping("/base64")
    @ResponseBody
    public String[] uploadFilesByBase64(@RequestBody MergeFileBase64 uploadFiles) {
        List<UploadAttachment> attachments = attachmentService.mergeAttachments(uploadFiles);
        if (attachments != null && attachments.size() > 0) {
            String[] ids = new String[attachments.size()];
            int i = 0;
            for (UploadAttachment attachment : attachments) {
                ids[i++] = attachment.getId();
            }
            return ids;
        }
        return new String[0];
    }


}
