package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class F253StructureFabricationFinalInspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = -2946309833773764807L;

    private String projectName;

    private String seriesNo;

    private Date date;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "位置")
    private String location;

    private List<F253StructureFabricationFinalInspectionReportItemDTO> items;

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
    public List<F253StructureFabricationFinalInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<F253StructureFabricationFinalInspectionReportItemDTO> items) {
        this.items = items;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

}
