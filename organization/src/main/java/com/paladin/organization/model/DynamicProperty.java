package com.paladin.organization.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/1/10
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "动态属性")
public class DynamicProperty {

    @ApiModelProperty(value = "属性编码")
    private String code;
    @ApiModelProperty(value = "属性名称")
    private String name;
    @ApiModelProperty(value = "是否可为空")
    private boolean nullable = true;
    @ApiModelProperty(value = "默认值")
    private String defaultValue;

}
