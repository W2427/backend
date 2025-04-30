package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class MagneticParticleInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -140621700175062604L;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "图纸号")
    private String drawingNo;

    @Schema(description = "结构名称")
    private String itemName;

    @Schema(description = "项目号")
    private String projectNo;

    @Schema(description = "材料")
    private String material;

    @Schema(description = "认可标准")
    private String acceptCriteria;

    @Schema(description = "表面处理")
    private String surfacePrep;

    @Schema(description = "反差剂")
    private String contrast;

    @Schema(description = "试片类型")
    private String sensitivityBlock;

    @Schema(description = "提升力")
    private String liftingForce;

    @Schema(description = "实验仪器类型")
    private String testInstrumentType;

    @Schema(description = "磁粉")
    private String wetParticles;

    @Schema(description = "方法")
    private String method;

    @Schema(description = "磁化时间")
    private String magnezingTime;

    @Schema(description = "磁悬液施加时间")
    private String mtdOfParAppl;

    @Schema(description = "检测比例")
    private String detectionScale;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    private List<MagneticParticleInspectionItmeDTO> items;

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

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAcceptCriteria() {
        return acceptCriteria;
    }

    public void setAcceptCriteria(String acceptCriteria) {
        this.acceptCriteria = acceptCriteria;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getSurfacePrep() {
        return surfacePrep;
    }

    public void setSurfacePrep(String surfacePrep) {
        this.surfacePrep = surfacePrep;
    }

    public String getContrast() {
        return contrast;
    }

    public void setContrast(String contrast) {
        this.contrast = contrast;
    }

    public String getSensitivityBlock() {
        return sensitivityBlock;
    }

    public void setSensitivityBlock(String sensitivityBlock) {
        this.sensitivityBlock = sensitivityBlock;
    }

    public String getLiftingForce() {
        return liftingForce;
    }

    public void setLiftingForce(String liftingForce) {
        this.liftingForce = liftingForce;
    }

    public String getTestInstrumentType() {
        return testInstrumentType;
    }

    public void setTestInstrumentType(String testInstrumentType) {
        this.testInstrumentType = testInstrumentType;
    }

    public String getWetParticles() {
        return wetParticles;
    }

    public void setWetParticles(String wetParticles) {
        this.wetParticles = wetParticles;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMagnezingTime() {
        return magnezingTime;
    }

    public void setMagnezingTime(String magnezingTime) {
        this.magnezingTime = magnezingTime;
    }

    public String getMtdOfParAppl() {
        return mtdOfParAppl;
    }

    public void setMtdOfParAppl(String mtdOfParAppl) {
        this.mtdOfParAppl = mtdOfParAppl;
    }

    public String getDetectionScale() {
        return detectionScale;
    }

    public void setDetectionScale(String detectionScale) {
        this.detectionScale = detectionScale;
    }

    @Override
    public List<MagneticParticleInspectionItmeDTO> getItems() {
        return items;
    }

    public void setItems(List<MagneticParticleInspectionItmeDTO> items) {
        this.items = items;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }
}
