package com.paladin.organization.service.vo;

import com.paladin.organization.core.FileResourceContainer;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 *<p>功能描述</p>：培训经历
 * @author Huangguochen
 * @create 2020/4/8 15:19
 */
@Getter
@Setter
public class OpenPersonnelCultivate {

    private String id;

    private String personnelId;

    private Integer cultivateType;

    private String cultivateUnit;

    private String cultivatePlace;

    private Date cultivateStartTime;

    private Date cultivateEndTime;

    private Integer endSituation;

    private String attachments;

    public List<FileResource> getAttachmentFiles() {
        if (attachments != null && attachments.length() > 0) {
            return FileResourceContainer.getFileResources(attachments.split(","));
        }
        return null;
    }

}