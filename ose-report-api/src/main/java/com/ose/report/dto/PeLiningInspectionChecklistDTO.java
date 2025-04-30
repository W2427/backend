package com.ose.report.dto;

import java.util.Date;

public class PeLiningInspectionChecklistDTO extends BaseReportDTO {

    private static final long serialVersionUID = 9029033995783380997L;

    private String projectName;
    private String constructor;
    private String workstep;
    private String inspector;
    private String partName;
    private Date date;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getConstructor() {
        return constructor;
    }

    public void setConstructor(String constructor) {
        this.constructor = constructor;
    }

    public String getWorkstep() {
        return workstep;
    }

    public void setWorkstep(String workstep) {
        this.workstep = workstep;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
