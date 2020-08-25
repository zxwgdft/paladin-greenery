package com.paladin.organization.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/3/25
 */
@Getter
@Setter
@ApiModel(description = "APP重定向")
public class AppRedirect {

    @ApiModelProperty("重定向确认码")
    private String code;
    @ApiModelProperty("重定向APP_ID")
    private String appId;
    @ApiModelProperty("重定向的用户")
    private String userId;

}
