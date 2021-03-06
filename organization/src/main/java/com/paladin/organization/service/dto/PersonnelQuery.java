package com.paladin.organization.service.dto;

import com.paladin.framework.service.OffsetPage;
import com.paladin.framework.service.QueryCondition;
import com.paladin.framework.service.QueryType;
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
public class PersonnelQuery extends OffsetPage {

    @ApiModelProperty(value = "人员姓名")
    @QueryCondition(type = QueryType.LIKE)
    private String name;

}
