package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Date;

public class RadiographicTestReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 7970901053632289558L;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "模块号")
    private String moduleName;

    @Schema(description = "认可标准")
    private String acceptCriteria;

    @Schema(description = "图号")
    private String drawingNo;

    @Schema(description = "仪器型号")
    private String instrumentType;

    @Schema(description = "焦距")
    private String sfd;

    @Schema(description = "胶片类型")
    private String filmType;

    @Schema(description = "X-管电压")
    private String xTubeVoltage;

    @Schema(description = "源类型")
    private String sourceType;

    @Schema(description = "源尺寸")
    private String sourceSize;

    @Schema(description = "曝光时间")
    private String exposureTime;

    @Schema(description = "X-管电流")
    private String xTubeCurrent;

    @Schema(description = "拍片方式")
    private String radioTechnique;

    @Schema(description = "显影条件")
    private String developCondition;

    @Schema(description = "增感方式")
    private String intensifyScreen;

    @Schema(description = "片盒底片数")
    private String noFilmInHolder;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    private List<RadiographicTestItemDTO> items;

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAcceptCriteria() {
        return acceptCriteria;
    }

    public void setAcceptCriteria(String acceptCriteria) {
        this.acceptCriteria = acceptCriteria;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getSfd() {
        return sfd;
    }

    public void setSfd(String sfd) {
        this.sfd = sfd;
    }

    public String getFilmType() {
        return filmType;
    }

    public void setFilmType(String filmType) {
        this.filmType = filmType;
    }

    public String getxTubeVoltage() {
        return xTubeVoltage;
    }

    public void setxTubeVoltage(String xTubeVoltage) {
        this.xTubeVoltage = xTubeVoltage;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceSize() {
        return sourceSize;
    }

    public void setSourceSize(String sourceSize) {
        this.sourceSize = sourceSize;
    }

    public String getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(String exposureTime) {
        this.exposureTime = exposureTime;
    }

    public String getxTubeCurrent() {
        return xTubeCurrent;
    }

    public void setxTubeCurrent(String xTubeCurrent) {
        this.xTubeCurrent = xTubeCurrent;
    }

    public String getRadioTechnique() {
        return radioTechnique;
    }

    public void setRadioTechnique(String radioTechnique) {
        this.radioTechnique = radioTechnique;
    }

    public String getDevelopCondition() {
        return developCondition;
    }

    public void setDevelopCondition(String developCondition) {
        this.developCondition = developCondition;
    }

    public String getIntensifyScreen() {
        return intensifyScreen;
    }

    public void setIntensifyScreen(String intensifyScreen) {
        this.intensifyScreen = intensifyScreen;
    }

    public String getNoFilmInHolder() {
        return noFilmInHolder;
    }

    public void setNoFilmInHolder(String noFilmInHolder) {
        this.noFilmInHolder = noFilmInHolder;
    }

    @Override
    public List<RadiographicTestItemDTO> getItems() {
        return items;
    }

    public void setItems(List<RadiographicTestItemDTO> items) {
        this.items = items;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }
}
