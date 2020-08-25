package com.paladin.organization.model;

import com.paladin.framework.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

/*
 * 个人信息 OrgPersonnel
 *
 */
@Setter
@Getter
@ApiModel(description = "人员")
public class Personnel extends BaseModel {

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "身份证件种类")
    private Integer identificationType;

    @ApiModelProperty(value = "身份证件号码")
    private String identificationNo;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "曾用名")
    private String usedName;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "手机号码")
    private String cellphone;

    @ApiModelProperty(value = "办公室电话")
    private String officePhone;

    @ApiModelProperty(value = "用户图片")
    private String profilePhoto;

    @ApiModelProperty(value = "机构ID")
    private String agencyId;
    
    @ApiModelProperty(value = "国籍")
    private Integer nationality;

    @ApiModelProperty(value = "民族")
    private Integer nation;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @ApiModelProperty(value = "开始工作时间")
    private Date startWorkTime;

    @ApiModelProperty(value = "入党时间")
    private Date joinPartyTime;

    @ApiModelProperty(value = "政治面貌")
    private Integer politicalAffiliation;

    @ApiModelProperty(value = "兴趣爱好")
    private String interest;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;


}