package com.paladin.organization.model;

import com.paladin.framework.common.BaseModel;
import com.paladin.organization.model.constant.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

/**
 *<p>功能描述</p>：培训经历
 * @author Huangguochen
 * @create 2020/4/8 15:19
 */
@Getter
@Setter
@ApiModel(description = "培训经历")
public class PersonnelCultivate extends BaseModel {


    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "人员ID")
    private String personnelId;

    @ApiModelProperty(value = "培训类型")
    private Integer cultivateType;

    @ApiModelProperty(value = "培训单位")
    private String cultivateUnit;


    @ApiModelProperty(value = "培训地点")
    private String cultivatePlace;


    @ApiModelProperty(value = "开始时间")
    private Date cultivateStartTime;


    @ApiModelProperty(value = "结束时间")
    private Date cultivateEndTime;


    @ApiModelProperty(value = "结束情况")
    private Integer endSituation;


    @ApiModelProperty(value = "附件（附件ID拼接，最多3个）")
    private String attachments;

    @ApiModelProperty(value = "状态")
    private Integer status = UserStatus.USER_INFO_STATUS_NO_CHECK;
}