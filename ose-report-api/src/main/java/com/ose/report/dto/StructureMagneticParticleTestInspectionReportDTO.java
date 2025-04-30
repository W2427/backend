package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class StructureMagneticParticleTestInspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = -209057668763566643L;

    private String projectName;

    private String seriesNo;

    private Date date;

    private String isoNo;

    private String drawingNo;

    private String structureName;

    private String material;

    private String detectionScale;

    @Schema(description = "报告号")
    private String reportNo;

    private List<StructureMagneticParticleTestInspectionReportItemDTO> items;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Override
    public List<StructureMagneticParticleTestInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<StructureMagneticParticleTestInspectionReportItemDTO> items) {
        this.items = items;
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    @Override
    public String getStructureName() {
        return structureName;
    }

    @Override
    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDetectionScale() {
        return detectionScale;
    }

    public void setDetectionScale(String detectionScale) {
        this.detectionScale = detectionScale;
    }
}
