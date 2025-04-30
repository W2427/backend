package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PeLiningInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 5376924071691435285L;

    @Schema(description = "报告编号")
    private String reportNo;
    private List<PeLiningInspectionItemDTO> items;

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Override
    public List<PeLiningInspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PeLiningInspectionItemDTO> items) {
        this.items = items;
    }
}
