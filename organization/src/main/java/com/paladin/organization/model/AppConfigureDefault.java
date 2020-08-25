package com.paladin.organization.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/4/9
 */
@Getter
@Setter
@ApiModel(description = "机构分组")
@Document("APP_CONFIGURE_DEFAULT")
public class AppConfigureDefault {

    @ApiModelProperty(value = "机构分组ID")
    private String id;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "分组名称")
    private String name;

    @ApiModelProperty(value = "分组的机构")
    private List<String> agencies;

}
