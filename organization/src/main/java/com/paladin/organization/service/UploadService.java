package com.paladin.organization.service;

import com.paladin.organization.core.FeignRequestInterceptor;
import com.paladin.organization.service.dto.UploadFileBase64;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author TontoZhou
 * @since 2020/8/31
 */
@FeignClient(name = "greenery-upload", configuration = FeignRequestInterceptor.class)
public interface UploadService {

    @PostMapping("/upload/attachment/upload/file/base64")
    String uploadFile(@RequestBody UploadFileBase64 uploadFile);

}
