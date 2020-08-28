package com.paladin.upload.web;

import com.paladin.framework.common.R;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.PageResult;
import com.paladin.upload.model.UploadVideo;
import com.paladin.upload.service.UploadVideoService;
import com.paladin.upload.service.dto.UploadVideoDTO;
import com.paladin.upload.service.dto.UploadVideoQuery;
import com.paladin.upload.service.util.BigFileUploader;
import com.paladin.upload.service.vo.UploadVideoVO;
import com.paladin.upload.web.dto.UploadChunk;
import com.paladin.upload.web.dto.UploadStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Api("上传视频")
@Controller
@RequestMapping("/upload/video")
public class UploadVideoController {

    @Autowired
    private UploadVideoService uploadFileService;

    @ApiOperation(value = "开启视频上传任务")
    @PostMapping("/create")
    @ResponseBody
    public UploadStatus beginUpload(@Valid UploadVideoDTO uploadFileDTO) {
        UploadVideo uploadFile = uploadFileService.createUploadFile(uploadFileDTO);
        BigFileUploader uploader = uploadFileService.getOrCreateUploader(uploadFile.getId());
        return getUploadStatus(uploader);
    }

    @ApiOperation(value = "继续视频上传任务")
    @PostMapping("/continue")
    @ResponseBody
    public UploadStatus continueUpload(@RequestParam("id") String id) {
        BigFileUploader uploader = uploadFileService.getOrCreateUploader(id);
        return getUploadStatus(uploader);
    }

    @ApiOperation(value = "上传视频文件块数据")
    @PostMapping("/chunk")
    @ResponseBody
    public UploadStatus uploadChunk(UploadChunk uploadChunk) {
        try {
            BigFileUploader uploader = uploadFileService.uploadFileChunk(uploadChunk.getId(), uploadChunk.getChunk(), uploadChunk.getFile().getInputStream());
            return getUploadStatus(uploader);
        } catch (IOException e) {
            throw new BusinessException("上传文件异常", e);
        }
    }

    private UploadStatus getUploadStatus(BigFileUploader uploader) {
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

    @ApiOperation(value = "查找上传中的视频文件")
    @GetMapping(value = "/find/uploading")
    @ResponseBody
    public List<UploadVideoVO> findUploadingFiles() {
        return uploadFileService.findUploadingFile();
    }

    @ApiOperation(value = "查找上传完成了的视频文件页")
    @GetMapping(value = "/find/completed/page")
    @ResponseBody
    public PageResult<UploadVideoVO> findCompletedFiles(UploadVideoQuery query) {
        return uploadFileService.findCompletedFile(query);
    }

    @ApiOperation(value = "删除视频文件")
    @PostMapping(value = "/delete")
    @ResponseBody
    public R deleteFile(@RequestParam("id") String id) {
        uploadFileService.removeUploadFile(id);
        return R.success();
    }

    @ApiOperation(value = "清理视频文件")
    @GetMapping(value = "/clean")
    @ResponseBody
    public R cleanFile() {
        uploadFileService.cleanUploadFile();
        return R.success();
    }

}
