package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PipeMaterialTraceabilityReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -2312805107488302723L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告编号")
    private String reportNo;
    private List<PipeMaterialTraceabilityItemDTO> items;

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
    public List<PipeMaterialTraceabilityItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PipeMaterialTraceabilityItemDTO> items) {
        this.items = items;
    }
}
