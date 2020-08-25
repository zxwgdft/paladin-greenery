package com.paladin.organization.model;

import com.paladin.framework.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

/**
 * @author TontoZhou
 * @since 2020/1/7
 */
@Setter
@Getter
@ApiModel(description = "机构")
public class Agency extends BaseModel {

    @Id
    @ApiModelProperty(value = "机构代码")
    private String code;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "所在城市代码")
    private Integer cityCode;

    @ApiModelProperty(value = "所在地区代码")
    private Integer districtCode;

    @ApiModelProperty(value = "机构地址")
    private String address;

    @ApiModelProperty(value = "乡镇街道")
    private String townStreet;

    @ApiModelProperty(value = "机构简称")
    private String sname;

    @ApiModelProperty(value = "是否公立单位或医院")
    private Boolean isPublic;

    @ApiModelProperty(value = "单位类别(机关，参公事业，全额事业，差额事业（公共单位）...)")
    private Integer type;

    @ApiModelProperty(value = "单位级别(正科，副科，股级..)")
    private Integer level;


}
