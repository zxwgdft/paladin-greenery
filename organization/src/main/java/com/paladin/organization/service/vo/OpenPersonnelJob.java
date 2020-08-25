package com.paladin.organization.service.vo;

import com.paladin.organization.core.FileResourceContainer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * <工作信息VO>
 *
 * @author Huangguochen
 * @create 2020/4/13 10:20
 */
@Getter
@Setter
public class OpenPersonnelJob {

    @ApiModelProperty(value="ID")
    private String id;

    /**
     * 工号
     */
    @ApiModelProperty(value="工号")
    private String employeeNo;

    /**
     * 所在科室
     */
    @ApiModelProperty(value="所在科室")
    private String dept;

    /**
     * 科室具体名称
     */
    @ApiModelProperty(value="科室具体名称")
    private String deptName;

    /**
     * 从事专业类别
     */
    @ApiModelProperty(value="从事专业类别")
    private Integer major;

    /**
     * 行政／业务管理职务
     */
    @ApiModelProperty(value="行政／业务管理职务")
    private Integer duty;

    /**
     * 专业技术资格（评或考试）
     */
    @ApiModelProperty(value="专业技术资格（评或考试）")
    private String technicalQualification;

    /**
     * 专业技术资格取得时间
     */
    @ApiModelProperty(value="专业技术资格取得时间")
    private Date gainDate;

    /**
     * 专业技术职务（聘）
     */
    @ApiModelProperty(value="专业技术职务（聘）")
    private Integer techPost;

    /**
     * 专业技术聘用时间
     */
    @ApiModelProperty(value="专业技术聘用时间")
    private Date employDate;

    /**
     * 专业技术职务聘用岗位
     */
    @ApiModelProperty(value="专业技术职务聘用岗位")
    private String employPost;

    /**
     * 编制情况
     */
    @ApiModelProperty(value="编制情况")
    private Integer formation;

    /**
     * 年内人员流动情况
     */
    @ApiModelProperty(value="年内人员流动情况")
    private Integer inorout;

    /**
     * 流入/流出时间
     */
    @ApiModelProperty(value="流入/流出时间")
    private Date inoroutDate;

    /**
     * 是否从事统计信息化业务工作
     */
    @ApiModelProperty(value="是否从事统计信息化业务工作")
    private Integer isStatistical;

    /**
     * 从事统计信息化业务工作内容
     */
    @ApiModelProperty(value="从事统计信息化业务工作内容")
    private String workContent;

    /**
     * 附件
     */
    @ApiModelProperty(value="附件")
    private String attachments;

    public List<FileResource> getAttachmentFiles() {
        if (attachments != null && attachments.length() > 0) {
            return FileResourceContainer.getFileResources(attachments.split(","));
        }
        return null;
    }


}
