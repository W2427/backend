package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PipeMaterialInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -2312805107488302723L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "序列号")
    private String seriesNo;

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    private List<PipeMaterialInspectionItemDTO> items;

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

    @Override
    public List<PipeMaterialInspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PipeMaterialInspectionItemDTO> items) {
        this.items = items;
    }
}
