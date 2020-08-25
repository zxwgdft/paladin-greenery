package com.paladin.organization.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
public class SysAttachment {

    /**
     * 文件
     */
    public final static int TYPE_FILE = 1;

    /**
     * 图片
     */
    public final static int TYPE_IMAGE = 2;

    public static final String FIELD_ID = "id";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETE_TIME = "deleteTime";


    @Id
    private String id;

    private Integer type;

    private String storeType;

    private String name;

    private String suffix;

    private Long size;

    private String relativePath;

    private String thumbnailRelativePath;

    private Date createTime;

    private Date deleteTime;

    private Boolean deleted;

}