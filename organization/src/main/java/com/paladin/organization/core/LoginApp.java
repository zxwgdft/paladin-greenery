package com.paladin.organization.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2019/12/26
 */
@Getter
@Setter
@ApiModel(description = "APP登录认证")
public class LoginApp {

    @ApiModelProperty("用户名")
    private String appId;

    @ApiModelProperty("密码")
    private String appSecret;


}
