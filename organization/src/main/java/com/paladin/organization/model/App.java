package com.paladin.organization.model;

import com.paladin.framework.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

/**
 * @author TontoZhou
 * @since 2020/1/8
 */
@Setter
@Getter
@ApiModel(description = "应用系统")
public class App extends BaseModel {

    public static final String FIELD_APP_ID = "appId";
    public static final String FIELD_APP_SECRET = "appSecret";

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "应用系统名称")
    private String appName;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "应用秘钥")
    private String appSecret;

    @ApiModelProperty(value = "重定向URL")
    private String redirectUrl;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "APP状态")
    private Integer state;

    @ApiModelProperty(value = "拥有者")
    private String owner;

}
