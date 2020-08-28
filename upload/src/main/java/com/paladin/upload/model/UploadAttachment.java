package com.paladin.upload.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
public class UploadAttachment {

    public static final String FIELD_ID = "id";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETE_TIME = "deleteTime";


    @Id
    private String id;

    private String storeType;

    private String name;

    private String suffix;

    private Long size;

    private String relativePath;

    private String thumbnailRelativePath;

    private Date createTime;

    private String createBy;

    private Date deleteTime;

    private Boolean deleted;

}