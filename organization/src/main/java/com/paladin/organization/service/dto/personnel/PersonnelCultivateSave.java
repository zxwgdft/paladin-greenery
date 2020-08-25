package com.paladin.organization.service.dto.personnel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * <培训经历新增DTO>
 *
 * @author Huangguochen
 * @create 2020/4/8 15:40
 */
@Getter
@Setter
@ApiModel(description = "培训经历新增")
public class PersonnelCultivateSave {

    @NotBlank(message = "人员ID不能为空")
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

}
