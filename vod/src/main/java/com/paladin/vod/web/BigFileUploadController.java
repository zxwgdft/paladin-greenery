package com.paladin.vod.web;


import com.paladin.framework.common.R;
import com.paladin.vod.service.BigFileUploadService;
import com.paladin.vod.service.BigFileUploader;
import com.paladin.vod.service.dto.UploadFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequestMapping("/vod/upload")
public class BigFileUploadController {

    @Autowired
    private BigFileUploadService bigFileUploaderService;

    @GetMapping(value = "/index")
    public Object index() {
        return "/index";
    }

    @PostMapping("/begin")
    @ResponseBody
    public R beginUpload(UploadFileDTO uploadFile) {


        bigFileUploaderService.beginUploadFile(uploadFile);

        return R.success();
    }


    @PostMapping("/check")
    @ResponseBody
    public Object uploadCheck(WebUploadParam param) {
        try {
            BigFileUploader uploader = bigFileUploaderService.getOrCreateUploader(param.getMd5(), param.getChunks(), param.getName());
            int status = uploader.isCompleted() ? BigFileUploader.UPLOAD_COMPLETED : BigFileUploader.UPLOAD_SUCCESS;
            HashMap<String, Object> result = new HashMap<>();
            result.put("status", status);
            result.put("result", uploader.getUploadedChunk());
            return R.success(result);
        } catch (Exception e) {
            return R.success(BigFileUploader.UPLOAD_ERROR);
        }
    }

    @PostMapping("/chunk")
    @ResponseBody
    public Object uploadChunk(WebUploadParam param) {
        int result;
        try {
            result = bigFileUploaderService.uploadFileChunk(param.getMd5(), param.getChunk(), param.getFile().getBytes());
        } catch (Exception e) {
            result = BigFileUploader.UPLOAD_ERROR;
        }
        return R.success(result);
    }

    @GetMapping(value = "/find/file")
    @ResponseBody
    public Object findVideos() {
        return R.success(bigFileUploaderService.findAllFiles());
    }

    @GetMapping(value = "/delete/file")
    @ResponseBody
    public Object removeVideos(String relativePath) {
        return R.success(bigFileUploaderService.deleteFile(relativePath));
    }
}
