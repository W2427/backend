package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class ApplicationForInspectionExternalDTO extends BaseReportDTO {

    private static final long serialVersionUID = -2051869177680106813L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "申请单号")
    private String reportNo;

    @Schema(description = "申请日期")
    private Date applyDate;

    @Schema(description = "检验地点")
    private String inspectionLocation;

    @Schema(description = "检验日期")
    private Date inspectionDate;

    @Schema(description = "报检项目")
    private String inspectionContents;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getInspectionLocation() {
        return inspectionLocation;
    }

    public void setInspectionLocation(String inspectionLocation) {
        this.inspectionLocation = inspectionLocation;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getInspectionContents() {
        return inspectionContents;
    }

    public void setInspectionContents(String inspectionContents) {
        this.inspectionContents = inspectionContents;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }
}
