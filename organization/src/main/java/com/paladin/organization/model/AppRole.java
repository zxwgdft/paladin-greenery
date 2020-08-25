package com.paladin.organization.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2020/1/8
 */
@Setter
@Getter
@ApiModel(description = "应用系统角色")
@Document("APP_ROLE")
public class AppRole {

    public static final String FIELD_ID = "id";
    public static final String FIELD_APP_ID = "appId";
    public static final String FIELD_PROPERTIES = "properties";
    public static final String FIELD_MODEL_ID = "modelId";
    public static final String FIELD_RESOURCE_IDS = "resourceIds";



    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "应用ID")
    private String appId;
    @ApiModelProperty(value = "模型ID")
    private String modelId;
    @ApiModelProperty(value = "属性")
    private Map<String, Object> properties;
    @ApiModelProperty(value = "资源ID集合")
    private List<String> resourceIds;

}
