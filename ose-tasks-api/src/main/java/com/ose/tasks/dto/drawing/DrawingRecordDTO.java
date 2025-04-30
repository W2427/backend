package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;

import java.util.Date;

public class DrawingRecordDTO extends BaseDTO {

    private static final long serialVersionUID = 9147933426626584159L;

    private Long drawingId;

    private Long recordId;

    private String sdrlCode ;

    private String packageNo;

    private String projectNo;

    private String orgCode;

    private String systemCode;

    private String documentTitle;

    private String stage;

    private String process;

    private String version;

    private String engineer;

    private Date workHourDate;

    private Double workHour = 0.0 ;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public String getSdrlCode() {
        return sdrlCode;
    }

    public void setSdrlCode(String sdrlCode) {
        this.sdrlCode = sdrlCode;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public Date getWorkHourDate() {
        return workHourDate;
    }

    public void setWorkHourDate(Date workHourDate) {
        this.workHourDate = workHourDate;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
    }
}
