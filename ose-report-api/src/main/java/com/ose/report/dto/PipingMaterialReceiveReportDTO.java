package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PipingMaterialReceiveReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 646390123804104010L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "生产厂家")
    private String supplier;

    private List<PipingMaterialReceiveItemDTO> items;

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
    public List<PipingMaterialReceiveItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PipingMaterialReceiveItemDTO> items) {
        this.items = items;
    }

    @Override
    public String getSupplier() {
        return supplier;
    }

    @Override
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
