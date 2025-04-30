package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PipeFittingInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -4011015638955762993L;

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

    private List<PipeFittingInspectionItemDTO> items;

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
    public List<PipeFittingInspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PipeFittingInspectionItemDTO> items) {
        this.items = items;
    }
}
