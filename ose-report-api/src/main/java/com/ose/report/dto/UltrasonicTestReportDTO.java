package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Date;

public class UltrasonicTestReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -8527085429967743130L;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "工件名称")
    private String partName;

    @Schema(description = "工件编号")
    private String partNo;

    @Schema(description = "图号")
    private String drawingNo;

    @Schema(description = "材料")
    private String material;

    @Schema(description = "表面处理")
    private String surfacePrep;

    @Schema(description = "NDT图号")
    private String ndtDrawing;

    @Schema(description = "仪器型号/编号")
    private String instrumentType;

    @Schema(description = "耦合剂")
    private String couplant;

    @Schema(description = "探头类型")
    private String probeType;

    @Schema(description = "探头角度")
    private String beamAngle;

    @Schema(description = "标准灵敏度")
    private String referenceSensitivity;

    @Schema(description = "入射点")
    private String beamIndex;

    @Schema(description = "试块形式")
    private String sensitivityBlock;

    @Schema(description = "检验灵敏度")
    private String testSensitivity;

    @Schema(description = "合格标准")
    private String acceptCriteria;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    private List<UltrasonicTestItemDTO> items;

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

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSurfacePrep() {
        return surfacePrep;
    }

    public void setSurfacePrep(String surfacePrep) {
        this.surfacePrep = surfacePrep;
    }

    public String getNdtDrawing() {
        return ndtDrawing;
    }

    public void setNdtDrawing(String ndtDrawing) {
        this.ndtDrawing = ndtDrawing;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getCouplant() {
        return couplant;
    }

    public void setCouplant(String couplant) {
        this.couplant = couplant;
    }

    public String getProbeType() {
        return probeType;
    }

    public void setProbeType(String probeType) {
        this.probeType = probeType;
    }

    public String getBeamAngle() {
        return beamAngle;
    }

    public void setBeamAngle(String beamAngle) {
        this.beamAngle = beamAngle;
    }

    public String getReferenceSensitivity() {
        return referenceSensitivity;
    }

    public void setReferenceSensitivity(String referenceSensitivity) {
        this.referenceSensitivity = referenceSensitivity;
    }

    public String getBeamIndex() {
        return beamIndex;
    }

    public void setBeamIndex(String beamIndex) {
        this.beamIndex = beamIndex;
    }

    public String getSensitivityBlock() {
        return sensitivityBlock;
    }

    public void setSensitivityBlock(String sensitivityBlock) {
        this.sensitivityBlock = sensitivityBlock;
    }

    public String getTestSensitivity() {
        return testSensitivity;
    }

    public void setTestSensitivity(String testSensitivity) {
        this.testSensitivity = testSensitivity;
    }

    public String getAcceptCriteria() {
        return acceptCriteria;
    }

    public void setAcceptCriteria(String acceptCriteria) {
        this.acceptCriteria = acceptCriteria;
    }

    @Override
    public List<UltrasonicTestItemDTO> getItems() {
        return items;
    }

    public void setItems(List<UltrasonicTestItemDTO> items) {
        this.items = items;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }
}
