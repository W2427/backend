package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class PipingPMITestReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -8779807954506953071L;

    private String projectName;

    private String seriesNo;

    private Date date;

    @Schema(description = "报告号")
    private String reportNo;

    private List<PipingPMITestReportItemDTO> items;

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
    public List<PipingPMITestReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PipingPMITestReportItemDTO> items) {
        this.items = items;
    }
}
