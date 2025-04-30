package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class StructureNtF253RadiographicInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -3739751003261219832L;

    private String projectName;

    private String seriesNo;

    private Date date;

    private String isoNo;

    private String drawingNo;

    private String structureName;

    @Schema(description = "报告号")
    private String reportNo;

    private List<StructureNtF253RadiographicInspectionReportItemDTO> items;

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
    public List<StructureNtF253RadiographicInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<StructureNtF253RadiographicInspectionReportItemDTO> items) {
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
