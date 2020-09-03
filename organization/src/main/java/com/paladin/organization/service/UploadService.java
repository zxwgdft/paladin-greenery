package com.paladin.organization.service;

import com.paladin.organization.core.FeignRequestInterceptor;
import com.paladin.organization.service.dto.MergeFileBase64;
import com.paladin.organization.service.vo.FileResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/8/31
 */
@FeignClient(name = "greenery-upload", configuration = FeignRequestInterceptor.class)
public interface UploadService {

    @PostMapping("/upload/attachment/base64")
    String[] uploadFile(@RequestBody MergeFileBase64 uploadFile);

    @GetMapping("/upload/get/attachments")
    List<FileResource> getAttachments(@RequestParam("id[]") String[] ids);

    @GetMapping("/upload/get/attachment")
    FileResource getAttachment(@RequestParam("id") String[] id);


}
