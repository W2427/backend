package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class F253StructureMaterialInspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = 8322358291360358350L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "序列号")
    private String seriesNo;

    private List<F253StructureMaterialInspectionReportItemDTO> items;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    @Override
    public List<F253StructureMaterialInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<F253StructureMaterialInspectionReportItemDTO> items) {
        this.items = items;
    }
}
