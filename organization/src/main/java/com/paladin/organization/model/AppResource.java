package com.paladin.organization.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2020/1/8
 */
@Setter
@Getter
@ApiModel(description = "应用系统资源")
@Document("APP_RESOURCE")
public class AppResource {

    public static final String FIELD_ID = "id";
    public static final String FIELD_APP_ID = "appId";
    public static final String FIELD_PARENT = "parent";
    public static final String FIELD_PROPERTIES = "properties";
    public static final String FIELD_MODEL_ID = "modelId";

    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "应用ID")
    private String appId;
    @ApiModelProperty(value = "模型ID")
    private String modelId;

    @ApiModelProperty(value = "上级资源")
    private String parent;

    // path 可以用于查找某个节点下所有子节点，但是资源这里并不需要这个功能，所以暂时屏蔽该字段
//    @ApiModelProperty(value = "资源路径")
//    private String path;

    @ApiModelProperty(value = "属性")
    private Map<String, Object> properties;
}
