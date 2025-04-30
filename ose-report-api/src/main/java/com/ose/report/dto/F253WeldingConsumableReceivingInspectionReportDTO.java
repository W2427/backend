package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class F253WeldingConsumableReceivingInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -4582286758692349563L;

    private String projectName;

    @Schema(description = "序列号")
    private String seriesNo;

    @Schema(description = "报告号")
    private String reportNo;

    private List<F253WeldingConsumableReceivingInspectionReportItemDTO> items;

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
    public List<F253WeldingConsumableReceivingInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<F253WeldingConsumableReceivingInspectionReportItemDTO> items) {
        this.items = items;
    }
}
