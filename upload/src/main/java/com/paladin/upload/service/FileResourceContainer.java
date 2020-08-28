package com.paladin.upload.service;

import com.paladin.framework.service.FileStoreService;
import com.paladin.framework.spring.SpringContainer;
import com.paladin.upload.model.UploadAttachment;
import com.paladin.upload.service.vo.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileResourceContainer implements SpringContainer {

    //TODO 在这里可以做缓存处理，从内存或redis获取
    //TODO 可以修改为文件服务器获取，在这里返回的URL修改为文件服务器地址（需要在UploadAttachmentService保存文件中做相应修改）

    @Autowired
    private UploadAttachmentService attachmentService;

    @Autowired
    private FileStoreService fileStoreService;

    private static FileResourceContainer container;

    public boolean initialize() {
        container = this;
        return true;
    }

    public static List<FileResource> getFileResources(String... ids) {
        if (ids != null && ids.length > 0) {
            List<UploadAttachment> attachments = container.attachmentService.getAttachments(ids);
            if (attachments != null && attachments.size() > 0) {
                List<FileResource> results = new ArrayList<>(attachments.size());
                for (UploadAttachment attachment : attachments) {
                    results.add(convert(attachment));
                }
                return results;
            }
        }

        return new ArrayList<FileResource>();
    }

    public static FileResource getFileResource(String id) {
        if (id != null && id.length() > 0) {
            UploadAttachment attachment = container.attachmentService.get(id);
            if (attachment != null) {
                return convert(attachment);
            }
        }
        return null;
    }

    public static FileResource convert(UploadAttachment attachment) {
        FileResource fr = new FileResource();
        fr.setId(attachment.getId());

        String suffix = attachment.getSuffix();
        String name = attachment.getName() + (suffix == null ? "" : suffix);
        fr.setName(name);
        String url = container.fileStoreService.getFileUrl(attachment.getRelativePath());
        fr.setUrl(url);

        String trp = attachment.getThumbnailRelativePath();
        if (trp != null && trp.length() > 0) {
            fr.setThumbnailUrl(container.fileStoreService.getFileUrl(trp));
        } else {
            fr.setThumbnailUrl(url);
        }

        fr.setSize(attachment.getSize());

        return fr;
    }


}
