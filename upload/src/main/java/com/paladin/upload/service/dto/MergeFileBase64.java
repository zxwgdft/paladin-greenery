package com.paladin.upload.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/9/2
 */
@Getter
@Setter
@ApiModel
public class MergeFileBase64 extends FileFrom {

    @ApiModelProperty("原附件ID")
    private String[] originIds;
    @ApiModelProperty("当前附件ID")
    private String[] currentIds;
    @ApiModelProperty("上传附件")
    private List<UploadFileBase64> uploadFiles;


}
