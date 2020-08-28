package com.paladin.upload.web;

import com.paladin.upload.model.UploadAttachment;
import com.paladin.upload.service.FileResourceContainer;
import com.paladin.upload.service.UploadAttachmentService;
import com.paladin.upload.service.dto.FileCreateParam;
import com.paladin.upload.service.vo.FileResource;
import com.paladin.upload.web.dto.UploadPicture;
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
    public FileResource uploadFile(@RequestParam("file") MultipartFile file) {
        return FileResourceContainer.convert(attachmentService.createAttachment(file));
    }

    @ApiOperation(value = "上传多个附件文件")
    @PostMapping("/upload/files")
    @ResponseBody
    public FileResource[] uploadFiles(@RequestParam("files") MultipartFile[] files) {
        FileResource[] result = new FileResource[files.length];
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            result[i] = FileResourceContainer.convert(attachmentService.createAttachment(file));
        }
        return result;
    }

    @ApiOperation(value = "上传base64格式的附件文件")
    @PostMapping("/upload/file/base64")
    @ResponseBody
    public FileResource uploadFileByBase64(@RequestParam String fileStr, @RequestParam(required = false) String filename) {
        UploadAttachment result = attachmentService.createAttachment(fileStr, filename == null || filename.length() == 0 ? "附件" : filename);
        return FileResourceContainer.convert(result);
    }

    @ApiOperation(value = "上传图片，图片过大会被压缩")
    @PostMapping("/upload/picture")
    @ResponseBody
    public FileResource uploadPicture(@RequestParam MultipartFile file,
                                      @RequestParam(required = false) Integer thumbnailWidth,
                                      @RequestParam(required = false) Integer thumbnailHeight) {
        return FileResourceContainer.convert(attachmentService.createPictureAndThumbnail(file, null, thumbnailWidth, thumbnailHeight));
    }


    @ApiOperation(value = "上传base64格式图片，并生成缩略图")
    @PostMapping("/upload/picture/base64")
    @ResponseBody
    public FileResource uploadPicture(@RequestBody UploadPicture uploadPicture) {
        FileCreateParam param = new FileCreateParam(uploadPicture.getBase64str(), uploadPicture.getFilename());
        param.setThumbnailHeight(uploadPicture.getThumbnailHeight());
        param.setThumbnailWidth(uploadPicture.getThumbnailWidth());
        return FileResourceContainer.convert(attachmentService.createPictureAndThumbnail(param));
    }


}
