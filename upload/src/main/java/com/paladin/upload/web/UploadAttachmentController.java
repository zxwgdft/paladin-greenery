package com.paladin.upload.web;

import com.paladin.upload.model.UploadAttachment;
import com.paladin.upload.service.FileResourceContainer;
import com.paladin.upload.service.UploadAttachmentService;
import com.paladin.upload.service.dto.FileCreateParam;
import com.paladin.upload.service.vo.FileResource;
import com.paladin.upload.web.dto.UploadFileBase64;
import com.paladin.upload.web.dto.UploadPictureBase64;
import com.paladin.upload.web.dto.UploadPictureForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api("附件上传")
@Controller
@RequestMapping("/upload/attachment")
public class UploadAttachmentController {

    @Autowired
    private UploadAttachmentService attachmentService;


    @ApiOperation(value = "通过ID获取附件")
    @ResponseBody
    @GetMapping("/get/file")
    public FileResource getAttachment(@RequestParam("id") String id) {
        return FileResourceContainer.getFileResource(id);
    }

    @ApiOperation(value = "通过ID获取多个附件")
    @GetMapping("/get/files")
    @ResponseBody
    public List<FileResource> getAttachments(@RequestParam("id[]") String[] ids) {
        return FileResourceContainer.getFileResources(ids);
    }

    @ApiOperation(value = "上传附件文件")
    @PostMapping("/upload/file")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String filename) {
        return attachmentService.createAttachment(file, filename).getId();
    }

    @ApiOperation(value = "上传多个附件文件")
    @PostMapping("/upload/files")
    @ResponseBody
    public String[] uploadFiles(@RequestParam("files") MultipartFile[] files) {
        String[] result = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            result[i] = attachmentService.createAttachment(file).getId();
        }
        return result;
    }

    @ApiOperation(value = "上传base64格式的附件文件")
    @PostMapping("/upload/file/base64")
    @ResponseBody
    public String uploadFileByBase64(@RequestBody UploadFileBase64 uploadFile) {
        UploadAttachment result = attachmentService.createAttachment(uploadFile.getBase64str(), uploadFile.getFilename());
        return result.getId();
    }

    @ApiOperation(value = "上传base64格式的附件文件")
    @PostMapping("/upload/files/base64")
    @ResponseBody
    public String[] uploadFilesByBase64(@RequestBody List<UploadFileBase64> uploadFiles) {
        String[] result = new String[uploadFiles.size()];
        int i = 0;
        for (UploadFileBase64 uploadFile : uploadFiles) {
            result[i++] = attachmentService.createAttachment(uploadFile.getBase64str(), uploadFile.getFilename()).getId();
        }
        return result;
    }

    @ApiOperation(value = "上传图片，并生成缩略图")
    @PostMapping("/upload/picture")
    @ResponseBody
    public String uploadPicture(@RequestBody UploadPictureForm uploadPicture) {
        return attachmentService.createPictureAndThumbnail(
                uploadPicture.getFile(),
                uploadPicture.getFilename(),
                uploadPicture.getThumbnailWidth(),
                uploadPicture.getThumbnailHeight()
        ).getId();
    }


    @ApiOperation(value = "上传base64格式图片，并生成缩略图")
    @PostMapping("/upload/picture/base64")
    @ResponseBody
    public String uploadPicture(@RequestBody UploadPictureBase64 uploadPicture) {
        FileCreateParam param = new FileCreateParam(uploadPicture.getBase64str(), uploadPicture.getFilename());
        param.setThumbnailHeight(uploadPicture.getThumbnailHeight());
        param.setThumbnailWidth(uploadPicture.getThumbnailWidth());
        return attachmentService.createPictureAndThumbnail(param).getId();
    }


}
