package com.paladin.upload.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author TontoZhou
 * @since 2020/9/3
 */
@Slf4j
@Service
public class ScheduledService {

    @Autowired
    private UploadAttachmentService uploadAttachmentService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledClean() {
        log.info("开始清理无效文件");
        int count = uploadAttachmentService.cleanAttachmentFile();
        log.info("清理结束，共清理文件数：" + count);
    }


}
