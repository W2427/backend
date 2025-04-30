package com.ose.report.dto;

import java.util.Date;

public class DesignChangeReviewFormDTO extends BaseReportDTO {

    private static final long serialVersionUID = -4534872887360742806L;

    private String projectName;
    private String reportNo;
    private String designChangeTitle;
    private String raisedPerson;
    private Date raisedDate;
    private String modificationCause;
    private String actionList;
    private String pipeManhour;
    private String totalManhour;
    private String pipeMaterial;

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

    public String getDesignChangeTitle() {
        return designChangeTitle;
    }

    public void setDesignChangeTitle(String designChangeTitle) {
        this.designChangeTitle = designChangeTitle;
    }

    public String getRaisedPerson() {
        return raisedPerson;
    }

    public void setRaisedPerson(String raisedPerson) {
        this.raisedPerson = raisedPerson;
    }

    public Date getRaisedDate() {
        return raisedDate;
    }

    public void setRaisedDate(Date raisedDate) {
        this.raisedDate = raisedDate;
    }

    public String getModificationCause() {
        return modificationCause;
    }

    public void setModificationCause(String modificationCause) {
        this.modificationCause = modificationCause;
    }

    public String getActionList() {
        return actionList;
    }

    public void setActionList(String actionList) {
        this.actionList = actionList;
    }

    public String getPipeManhour() {
        return pipeManhour;
    }

    public void setPipeManhour(String pipeManhour) {
        this.pipeManhour = pipeManhour;
    }

    public String getTotalManhour() {
        return totalManhour;
    }

    public void setTotalManhour(String totalManhour) {
        this.totalManhour = totalManhour;
    }

    public String getPipeMaterial() {
        return pipeMaterial;
    }

    public void setPipeMaterial(String pipeMaterial) {
        this.pipeMaterial = pipeMaterial;
    }
}
