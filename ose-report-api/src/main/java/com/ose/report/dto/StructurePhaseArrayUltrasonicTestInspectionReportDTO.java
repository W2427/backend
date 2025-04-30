package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class StructurePhaseArrayUltrasonicTestInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 848682235828993888L;

    private String projectName;

    private String seriesNo;

    private Date date;

    private String isoNo;

    private String drawingNo;

    private String structureName;

    private String materialGroup;

    private String itemSize;

    private String rtRatio;

    @Schema(description = "报告号")
    private String reportNo;

    private List<StructurePhaseArrayUltrasonicTestInspectionReportItemDTO> items;

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
    public List<StructurePhaseArrayUltrasonicTestInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<StructurePhaseArrayUltrasonicTestInspectionReportItemDTO> items) {
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

    public String getMaterialGroup() {
        return materialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        this.materialGroup = materialGroup;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getRtRatio() {
        return rtRatio;
    }

    public void setRtRatio(String rtRatio) {
        this.rtRatio = rtRatio;
    }
}
