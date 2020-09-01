package com.paladin.organization.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/8/28
 */
@Getter
@Setter
@ApiModel("上传图片")
@AllArgsConstructor
public class UploadFileBase64 {

    @ApiModelProperty("文件名称")
    private String filename;
    @ApiModelProperty("文件大小")
    private String base64str;

}
