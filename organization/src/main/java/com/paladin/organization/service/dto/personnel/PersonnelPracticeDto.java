package com.paladin.organization.service.dto.personnel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * <执业信息DTO>
 *
 * @author Huangguochen
 * @create 2020/4/13 14:20
 */
@ApiModel(description = "执业信息")
@Getter
@Setter
public class PersonnelPracticeDto {

    /**
     * 执业信息
     */
    @NotBlank(message = "人员ID不能为空")
    @ApiModelProperty(value="ID")
    private String id;

    /**
     * 人员类别
     */
    @ApiModelProperty(value="人员类别")
    private Integer personnelType;

    /**
     * 是否全科医生
     */
    @ApiModelProperty(value="是否全科医生")
    private Integer generalPractitioner;

    /**
     * 医师执业证书编号
     */
    @ApiModelProperty(value="医师执业证书编号")
    private String docCertCode;

    /**
     * 医师资格证书编码
     */
    @ApiModelProperty(value="医师资格证书编码")
    private String docQualificationCode;

    /**
     * 是否获得国家住院医师规范化培训合格证书
     */
    @ApiModelProperty(value="是否获得国家住院医师规范化培训合格证书")
    private Integer isCountryCert;

    /**
     * 住院医师规范化培训合格证书编号
     */
    @ApiModelProperty(value="住院医师规范化培训合格证书编号")
    private String inpatientCertCode;

    /**
     * 是否注册为全科医学专业
     */
    @ApiModelProperty(value="是否注册为全科医学专业")
    private Integer isGeneralPractitioner;

    /**
     * 执业地点
     */
    @ApiModelProperty(value="执业地点")
    private String practiceAddress;

    /**
     * 执业注册时间(首次注册时间)
     */
    @ApiModelProperty(value="执业注册时间(首次注册时间)")
    private Date registrationDate;

    /**
     * 医师执业类别
     */
    @ApiModelProperty(value="医师执业类别")
    private Integer practiceCategory;

    /**
     * 医师执业范围
     */
    @ApiModelProperty(value="医师执业范围")
    private String practiceScope;

    /**
     * 是否由乡镇卫生院或社区卫生服务机构派驻村卫生室工作
     */
    @ApiModelProperty(value="是否由乡镇卫生院或社区卫生服务机构派驻村卫生室工作")
    private Integer isDispatched;

    /**
     * 是否多地点职业
     */
    @ApiModelProperty(value="是否多地点职业")
    private Integer isMultisite;

    /**
     * 第2执业单位类别
     */
    @ApiModelProperty(value="第2执业单位类别")
    private Integer secondCategory;

    /**
     * 第3执业单位类别
     */
    @ApiModelProperty(value="第3执业单位类别")
    private Integer thirdCategory;

    /**
     * 专业特长
     */
    @ApiModelProperty(value="专业特长")
    private String expertise;

    /**
     * 全科医生取得培训合格证书情况
     */
    @ApiModelProperty(value="全科医生取得培训合格证书情况")
    private String docTrainCert;

    /**
     * (护士执业证书编号)
     */
    @ApiModelProperty(value="(护士执业证书编号)")
    private String nurseCertCode;

    /**
     * 护士执业单位
     */
    @ApiModelProperty(value="护士执业单位")
    private String nurseInstitution;

    /**
     * （护士）最后注册日期
     */
    @ApiModelProperty(value="（护士）最后注册日期")
    private Date lastRegistrationDate;

    /**
     * (护士)是否免考
     */
    @ApiModelProperty(value="(护士)是否免考")
    private Integer isExam;

    /**
     * 从事护士工作开始时间
     */
    @ApiModelProperty(value="从事护士工作开始时间")
    private Date startWorkDate;

    /**
     * (护士)工作类别
     */
    @ApiModelProperty(value="(护士)工作类别")
    private String nurseCategory;

    /**
     * 乡村医生执业证书编号
     */
    @ApiModelProperty(value="乡村医生执业证书编号")
    private String vdocCertCode;

    /**
     * 注册村级卫生机构名称
     */
    @ApiModelProperty(value="注册村级卫生机构名称")
    private String registVillageAgency;

    /**
     * （村医）注册时间(首次注册时间)
     */
    @ApiModelProperty(value="（村医）注册时间(首次注册时间)")
    private Date registDate;

    /**
     * 上年总收入
     */
    @ApiModelProperty(value="上年总收入")
    private Integer totalIncome;

    /**
     * 是否有医疗责任保险
     */
    @ApiModelProperty(value="是否有医疗责任保险")
    private Integer isMedicalInsurance;

    /**
     * 是否有工伤保险
     */
    @ApiModelProperty(value="是否有工伤保险")
    private Integer isInjuryInsurance;

    /**
     * 是否有养老保险
     */
    @ApiModelProperty(value="是否有养老保险")
    private String isEndowmentInsurance;

    /**
     * 从事乡村医生工作年限(年)
     */
    @ApiModelProperty(value="从事乡村医生工作年限(年)")
    private Integer workYears;

    /**
     * 高中及以下学历乡村医生是否为在职培训合格者
     */
    @ApiModelProperty(value="高中及以下学历乡村医生是否为在职培训合格者")
    private Integer isOnjobTrain;

    /**
     * 特殊岗位证书名称
     */
    @ApiModelProperty(value="特殊岗位证书名称")
    private String certName;

    /**
     * 特殊岗位证书发证单位
     */
    @ApiModelProperty(value="特殊岗位证书发证单位")
    private String issueUnit;

    /**
     * 特殊岗位证书发证获得时间
     */
    @ApiModelProperty(value="特殊岗位证书发证获得时间")
    private Date issueDate;

    /**
     * 特殊岗位证书有效期开始时间
     */
    @ApiModelProperty(value="特殊岗位证书有效期开始时间")
    private Date startDate;

    /**
     * 特殊岗位证书有效期结束时间
     */
    @ApiModelProperty(value="特殊岗位证书有效期结束时间")
    private Date endDate;

    /**
     * 是否在村卫生室工作
     */
    @ApiModelProperty(value="是否在村卫生室工作")
    private Integer isInVillageClinic;

    /**
     * 护考时间
     */
    @ApiModelProperty(value="护考时间")
    private Date nurseExamTime;
}
