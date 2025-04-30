package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PipingInstallationReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -6144390817892363916L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告编号")
    private String reportNo;
    private List<PipingInstallationItemDTO> items;

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public List<PipingInstallationItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PipingInstallationItemDTO> items) {
        this.items = items;
    }
}
