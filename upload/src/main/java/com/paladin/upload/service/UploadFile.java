package com.paladin.upload.service;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/8/26
 */
@Getter
@Setter
public class UploadFile {

    // 上传文件ID
    public String id;

    // 单位为M
    public Integer chunkSize;

    // 文件大小
    public Long fileSize;

    // 当前上传块
    public Integer finishChunk;

    // 文件后缀带.
    public String suffix;

    // 服务器相对地址
    public String serverRelativePath;

}
