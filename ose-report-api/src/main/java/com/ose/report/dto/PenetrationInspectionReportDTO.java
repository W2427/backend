package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Date;

public class PenetrationInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -4297226406379661426L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "区域号")
    private String areaNo;

    @Schema(description = "认可标准")
    private String acceptCriteria;

    @Schema(description = "渗透剂型号")
    private String penetrationMedium;

    @Schema(description = "表面处理")
    private String surfacePrep;

    @Schema(description = "清洗剂型号")
    private String intermediateCleaning;

    @Schema(description = "工作温度")
    private String workPieceTemp;

    @Schema(description = "显像剂型号")
    private String developer;

    @Schema(description = "去除方法")
    private String removeMethod;

    @Schema(description = "渗透时间")
    private String penetrationTime;

    @Schema(description = "检测比例")
    private String examinationScale;

    @Schema(description = "显像时间")
    private String displayTime;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    private List<PenetrationInspectionItemDTO> items;

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

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }

    public String getAcceptCriteria() {
        return acceptCriteria;
    }

    public void setAcceptCriteria(String acceptCriteria) {
        this.acceptCriteria = acceptCriteria;
    }

    public String getPenetrationMedium() {
        return penetrationMedium;
    }

    public void setPenetrationMedium(String penetrationMedium) {
        this.penetrationMedium = penetrationMedium;
    }

    public String getSurfacePrep() {
        return surfacePrep;
    }

    public void setSurfacePrep(String surfacePrep) {
        this.surfacePrep = surfacePrep;
    }

    public String getIntermediateCleaning() {
        return intermediateCleaning;
    }

    public void setIntermediateCleaning(String intermediateCleaning) {
        this.intermediateCleaning = intermediateCleaning;
    }

    public String getWorkPieceTemp() {
        return workPieceTemp;
    }

    public void setWorkPieceTemp(String workPieceTemp) {
        this.workPieceTemp = workPieceTemp;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getRemoveMethod() {
        return removeMethod;
    }

    public void setRemoveMethod(String removeMethod) {
        this.removeMethod = removeMethod;
    }

    public String getPenetrationTime() {
        return penetrationTime;
    }

    public void setPenetrationTime(String penetrationTime) {
        this.penetrationTime = penetrationTime;
    }

    public String getExaminationScale() {
        return examinationScale;
    }

    public void setExaminationScale(String examinationScale) {
        this.examinationScale = examinationScale;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    @Override
    public List<PenetrationInspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PenetrationInspectionItemDTO> items) {
        this.items = items;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }
}
