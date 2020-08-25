package com.paladin.organization.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2020/4/2
 */
@Getter
@Setter
@ApiModel(description = "应用角色更新")
public class AppRoleUpdate {

    @NotEmpty(message = "资源ID不能为空")
    @ApiModelProperty(value = "ID")
    private String id;

    @NotEmpty(message = "资源模型不能为空")
    @ApiModelProperty(value = "资源模型ID")
    private String modelId;

    @ApiModelProperty(value = "属性")
    private Map<String, Object> properties;



}
