package com.paladin.organization.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * @author TontoZhou
 * @since 2020/1/10
 */
@Setter
@Getter
@ApiModel(description = "应用角色模型")
@Document("APP_ROLE_MODEL")
public class AppRoleModel extends DynamicModel {

    public static final String FIELD_APP_ID = "appId";
    public static final String FIELD_ID = "id";

    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "应用ID")
    private String appId;

}
