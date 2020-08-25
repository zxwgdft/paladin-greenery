package com.paladin.organization.service.dto;

import com.paladin.organization.model.DynamicProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/1/10
 */
@Setter
@Getter
@ApiModel(description = "应用资源模型更新")
public class AppResourceModelSave {

    @NotEmpty(message = "所属应用不能为空")
    @ApiModelProperty(value = "应用ID")
    private String appId;

    @NotEmpty(message = "名称不能为空")
    @Length(max = 50, message = "名称长度不能大于50")
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "属性")
    private List<DynamicProperty> properties;

}
