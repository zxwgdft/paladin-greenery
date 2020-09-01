package com.paladin.organization.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <人员基础信息新增DTO>
 *
 * @author Huangguochen
 * @create 2020/4/8 15:52
 */
@Getter
@Setter
@ApiModel(description = "人员基础信息新增")
public class PersonnelSave {


    @NotNull(message = "账号不能为空")
    @ApiModelProperty(value = "账号")
    private String account;

    @NotNull(message = "身份证件种类不能为空")
    @ApiModelProperty(value = "证件种类")
    private Integer identificationType;

    @NotBlank(message = "证件号码不能为空")
    @ApiModelProperty(value = "身份证件号码")
    private String identificationNo;

    @NotBlank(message = "所属机构不能为空")
    @ApiModelProperty(value = "所属机构ID")
    private String agencyId;

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "曾用名")
    private String usedName;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(value = "性别")
    private Integer sex;

    @NotBlank(message = "手机号码不能为空")
    @ApiModelProperty(value = "手机号码")
    private String cellphone;

    @ApiModelProperty(value = "用户图片")
    private MultipartFile profilePhotoFile;

    @ApiModelProperty(value = "国籍")
    private Integer nationality;

    @ApiModelProperty(value = "民族")
    private Integer nation;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @ApiModelProperty(value = "兴趣爱好")
    private String interest;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;


}
