package com.paladin.upload.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/9/3
 */
@Getter
@Setter
public class FileFrom {
    @ApiModelProperty("来源服务")
    private String fromService;
    @ApiModelProperty("业务对象")
    private String business;
    @ApiModelProperty("业务ID")
    private String businessId;
}
