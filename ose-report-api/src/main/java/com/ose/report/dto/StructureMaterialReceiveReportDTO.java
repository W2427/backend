package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class StructureMaterialReceiveReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -5975030131401184271L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告编号")
    private String reportNo;

    private List<StructureMaterialReceiveItemDTO> items;

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
    public List<StructureMaterialReceiveItemDTO> getItems() {
        return items;
    }

    public void setItems(List<StructureMaterialReceiveItemDTO> items) {
        this.items = items;
    }

}
