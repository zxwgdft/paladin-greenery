package com.paladin.framework.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

/**
 * @author TontoZhou
 * @since 2021/2/8
 */
@Getter
@Setter
public class DeletedModel extends BaseModel {

    public static final String FIELD_DELETED = "deleted";

    @ApiModelProperty("是否删除")
    private Boolean deleted;
}
