package com.paladin.organization.service.dto;

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
public class MergeFileBase64 {

    @ApiModelProperty("原附件ID")
    private String[] originIds;
    @ApiModelProperty("当前附件ID")
    private String[] currentIds;
    @ApiModelProperty("来源服务")
    private String fromService;
    @ApiModelProperty("业务对象")
    private String business;
    @ApiModelProperty("业务ID")
    private String businessId;
    @ApiModelProperty("上传附件")
    private List<UploadFileBase64> uploadFiles;


}
