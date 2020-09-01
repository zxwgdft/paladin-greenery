package com.paladin.organization.service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/8/31
 */
@Getter
@Setter
@ApiModel
public class SimpleAgency {

    @ApiModelProperty(value = "机构代码")
    private String code;

    @ApiModelProperty(value = "机构名称")
    private String name;
}
