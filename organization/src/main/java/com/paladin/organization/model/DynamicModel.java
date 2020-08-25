package com.paladin.organization.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/1/10
 */
@Setter
@Getter
@ApiModel(description = "动态实体类")
public class DynamicModel {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "属性")
    private List<DynamicProperty> properties;

}
