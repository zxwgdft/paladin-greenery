package com.paladin.organization.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/4/9
 */
@Setter
@Getter
@ApiModel(description = "更新机构分组")
public class AgencyGroupUpdate {

    @NotEmpty(message = "ID不能为空")
    @ApiModelProperty(value = "机构分组ID")
    private String id;

    @NotEmpty(message = "名称不能为空")
    @Length(max = 50, message = "名称长度不能大于50")
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "机构ID数组")
    private List<String> agencies;
}
