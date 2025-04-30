package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class PipingSpoolReleaseInspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = -4220406412068948838L;

    private String projectName;

    private String seriesNo;

    @Schema(description = "报告号")
    private String reportNo;

    private Date date;

    private List<PipingSpoolReleaseInspectionReportItemDTO> items;

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

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Override
    public List<PipingSpoolReleaseInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PipingSpoolReleaseInspectionReportItemDTO> items) {
        this.items = items;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
