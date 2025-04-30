package com.ose.report.dto;

import java.util.Date;

public class InspectionReleaseFormDTO extends BaseReportDTO {

    private static final long serialVersionUID = -3706578810547925443L;

    private String projectName;
    private String reportNo;
    private String systemNo;
    private Date date;

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

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
