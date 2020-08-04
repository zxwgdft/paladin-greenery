package com.paladin.vod.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author TontoZhou
 * @since 2020/8/4
 */
@Getter
@Setter
public class UploadFile {

    public static final int STATUS_UPLOADING = 1;    //  上传中
    public static final int STATUS_COMPLETED = 2;    // 上传完成

    // 上传错误
    public static final int STATUS_UPLOAD_ERROR = 3;
    // 标记删除
    public static final int STATUS_DELETE = 7;
    // 删除文件错误
    public static final int STATUS_DELETE_ERROR = 8;
    // 删除文件成功
    public static final int STATUS_DELETED = 9;

    public static final String FIELD_STATUS = "status";
    public static final String FIELD_USER_ID = "userId";

    @Id
    private String id;

    // 用户ID
    private String userId;

    // 文件名称
    private String fileName;

    // 客户端文件路径
    private String clientFilePath;

    // 服务端相对路径
    private String serverRelativePath;

    // 文件大小
    private long fileSize;

    // 块大小
    private int chunkSize;

    // 完成块数
    private int finishChunk;

    // 创建时间
    private Date createTime;

    // 更新时间
    private Date updateTime;

    // 状态
    private Integer status;
}
