package com.ose.report.dto;

import java.util.Date;

public class ApplicationForInspectionDTO extends BaseReportDTO {

    private static final long serialVersionUID = -7455647576101134348L;

    private String projectName;
    private String reportNo;
    private String applicantName;
    private String applicantTel;
    private Date applyingDate;
    private String inspectionLocation;
    private Date inspectionDate;
    private String inspectionContents;

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

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantTel() {
        return applicantTel;
    }

    public void setApplicantTel(String applicantTel) {
        this.applicantTel = applicantTel;
    }

    public Date getApplyingDate() {
        return applyingDate;
    }

    public void setApplyingDate(Date applyingDate) {
        this.applyingDate = applyingDate;
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
}
