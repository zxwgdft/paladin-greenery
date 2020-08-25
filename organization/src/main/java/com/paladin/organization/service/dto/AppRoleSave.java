package com.paladin.organization.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2020/4/1
 */
@Getter
@Setter
@ApiModel(description = "应用角色创建")
public class AppRoleSave {

    @ApiModelProperty(value = "名称")
    @NotEmpty(message = "名称不能为空")
    @Length(max = 50, message = "名称长度不能大于50")
    private String name;

    @ApiModelProperty(value = "应用ID")
    @NotEmpty(message = "所属应用不能为空")
    private String appId;

    @ApiModelProperty(value = "模型ID")
    @NotEmpty(message = "资源模型不能为空")
    private String modelId;

    @ApiModelProperty(value = "属性")
    private Map<String, Object> properties;

}
