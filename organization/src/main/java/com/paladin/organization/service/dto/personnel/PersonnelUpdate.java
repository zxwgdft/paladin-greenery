package com.paladin.organization.service.dto.personnel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * <人员基础信息修改DTO>
 *
 * @author Huangguochen
 * @create 2020/4/8 16:11
 */
@Getter
@Setter
@ApiModel(description = "人员基础信息修改DTO")
public class PersonnelUpdate {

    @NotBlank(message = "人员ID不能为空")
    @ApiModelProperty(value = "ID")
    private String id;

    @NotNull(message = "身份证件种类不能为空")
    @ApiModelProperty(value = "证件种类")
    private Integer identificationType;

    @NotBlank(message = "证件号码不能为空")
    @ApiModelProperty(value = "身份证件号码")
    private String identificationNo;

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "曾用名")
    private String usedName;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(value = "性别")
    private Integer sex;

    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$",message = "手机号格式有误")
    @ApiModelProperty(value = "手机号码")
    private String cellphone;

    @ApiModelProperty(value = "办公室电话")
    private String officePhone;

    @ApiModelProperty(value = "用户图片")
    private String profilePhoto;


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
