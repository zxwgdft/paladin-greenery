package com.paladin.gateway.model;

import com.paladin.framework.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/9/8
 */
@Getter
@Setter
public class Route extends BaseModel {

    public final static String FIELD_ENABLED = "enabled";

    private String id;
    private String uri;
    private String predicates;
    private String filters;
    private String description;
    private Integer orders;
    private Boolean enabled;
}
