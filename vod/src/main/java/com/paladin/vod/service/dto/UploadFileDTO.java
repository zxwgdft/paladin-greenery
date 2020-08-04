package com.paladin.vod.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

/**
 * @author TontoZhou
 * @since 2020/8/4
 */
@Getter
@Setter
@ApiModel
public class UploadFileDTO {

    @Id
    private String id;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("客户端文件路径")
    private String clientFilePath;

    @ApiModelProperty("文件大小")
    private long fileSize;

}
