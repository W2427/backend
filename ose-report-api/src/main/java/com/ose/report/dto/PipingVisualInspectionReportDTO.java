package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class PipingVisualInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 2574814478416037125L;

    private String projectName;

    private String seriesNo;

    private Date date;

    @Schema(description = "报告号")
    private String reportNo;

    private List<PipingVisualInspectionReportItemDTO> items;

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
    public List<PipingVisualInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PipingVisualInspectionReportItemDTO> items) {
        this.items = items;
    }
}
