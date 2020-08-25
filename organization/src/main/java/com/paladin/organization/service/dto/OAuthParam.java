package com.paladin.organization.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/1/15
 */
@Getter
@Setter
@ApiModel(description = "OAuth参数")
public class OAuthParam {

    // 暂时我们的授权类型只有一种，可以不传
    @ApiModelProperty("授权类型")
    private String grant_type;
    // 暂时授权范围只有一个，可以不传
    @ApiModelProperty("授权范围")
    private String scope;
    @ApiModelProperty("客户端ID")
    private String appId;
    // 重定向URL暂时直接取app中配置的url，暂时不可用
    @ApiModelProperty("重定向返回URL")
    private String redirectUrl;

    @ApiModelProperty("登录用户名")
    private String username;
    @ApiModelProperty("用户密码")
    private String password;

}
