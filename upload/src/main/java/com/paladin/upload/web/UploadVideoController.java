package com.paladin.upload.web;

import com.paladin.framework.common.R;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.PageResult;
import com.paladin.upload.model.UploadVideo;
import com.paladin.upload.service.FileUploader;
import com.paladin.upload.service.UploadVideoService;
import com.paladin.upload.service.dto.UploadVideoDTO;
import com.paladin.upload.service.dto.UploadVideoQuery;
import com.paladin.upload.service.vo.UploadVideoVO;
import com.paladin.upload.web.dto.UploadChunk;
import com.paladin.upload.web.dto.UploadStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/upload/video")
public class UploadVideoController {

    @Autowired
    private UploadVideoService uploadFileService;

    @PostMapping("/create")
    @ResponseBody
    public UploadStatus beginUpload(@Valid UploadVideoDTO uploadFileDTO) {
        UploadVideo uploadFile = uploadFileService.createUploadFile(uploadFileDTO);
        FileUploader uploader = uploadFileService.getOrCreateUploader(uploadFile.getId());
        return getUploadStatus(uploader);
    }

    @PostMapping("/continue")
    @ResponseBody
    public UploadStatus continueUpload(@RequestParam("id") String id) {
        FileUploader uploader = uploadFileService.getOrCreateUploader(id);
        return getUploadStatus(uploader);
    }

    @PostMapping("/chunk")
    @ResponseBody
    public UploadStatus uploadChunk(UploadChunk uploadChunk) {
        try {
            FileUploader uploader = uploadFileService.uploadFileChunk(uploadChunk.getId(), uploadChunk.getChunk(), uploadChunk.getFile().getInputStream());
            return getUploadStatus(uploader);
        } catch (IOException e) {
            throw new BusinessException("上传文件异常", e);
        }
    }

    private UploadStatus getUploadStatus(FileUploader uploader) {
        UploadStatus status = new UploadStatus();

        status.setId(uploader.getId());
        status.setChunkSize(uploader.getChunkSize());
        status.setCurrentChunk(uploader.getCurrentChunk());

        if (uploader.isCompleted()) {
            status.setStatus(UploadVideo.STATUS_COMPLETED);
        } else {
            status.setStatus(UploadVideo.STATUS_UPLOADING);
        }

        return status;
    }

    @GetMapping(value = "/find/uploading")
    @ResponseBody
    public List<UploadVideoVO> findUploadingFiles() {
        return uploadFileService.findUploadingFile();
    }

    @GetMapping(value = "/find/completed/page")
    @ResponseBody
    public PageResult<UploadVideoVO> findCompletedFiles(UploadVideoQuery query) {
        return uploadFileService.findCompletedFile(query);
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public R deleteFile(@RequestParam("id") String id) {
        uploadFileService.removeUploadFile(id);
        return R.success();
    }

    @GetMapping(value = "/clean")
    @ResponseBody
    public R cleanFile() {
        uploadFileService.cleanUploadFile();
        return R.success();
    }

}
