package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.MaterialOrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

/**
 * 材料出库单创建DTO
 */
public class MmIssueCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -2926664218606084324L;

    @Schema(description = "公司ID")
    public Long companyId;

    @Schema(description = "出库单名称")
    private String name;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "出库单类型")
    private String issueType;

    @Schema(description ="任务包")
    private String taskPackageName;

    @Schema(description ="仓库类型（公司、项目）")
    private MaterialOrganizationType materialOrganizationType;

    @Schema(description ="出库日期")
    private Date date;

    @Schema(description ="出库单计划实体")
    private List<MmIssueCreatePlanDTO> issueEntities;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public MaterialOrganizationType getWareHouseType() {
        return materialOrganizationType;
    }

    public void setWareHouseType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<MmIssueCreatePlanDTO> getIssueEntities() {
        return issueEntities;
    }

    public void setIssueEntities(List<MmIssueCreatePlanDTO> issueEntities) {
        this.issueEntities = issueEntities;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }
}
