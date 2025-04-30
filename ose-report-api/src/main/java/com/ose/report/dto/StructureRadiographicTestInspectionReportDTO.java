package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class StructureRadiographicTestInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 6665195884708045809L;

    private String projectName;

    private String seriesNo;

    private Date date;

    private String isoNo;

    private String drawingNo;

    private String structureName;

    @Schema(description = "报告号")
    private String reportNo;

    private List<StructureRadiographicTestInspectionReportItemDTO> items;

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
    public List<StructureRadiographicTestInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<StructureRadiographicTestInspectionReportItemDTO> items) {
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
}
