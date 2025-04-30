package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class F253PipingAfterWeldingInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -6603987389215292404L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "模块号")
    private String moduleNo;

    @Schema(description = "序列号")
    private  String seriesNo;

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    private List<F253PipingAfterWeldingInspectionReportItemDTO> items;

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
    public String getModuleNo() {
        return moduleNo;
    }

    @Override
    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    @Override
    public List<F253PipingAfterWeldingInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<F253PipingAfterWeldingInspectionReportItemDTO> items) {
        this.items = items;
    }
}
