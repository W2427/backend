package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class IncomingMaterialInspectionRecordDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 7745175453519258223L;

    @Schema(description = "报告编号")
    private String reportNo;
    private List<IncomingMaterialInspectionItemDTO> items;

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Override
    public List<IncomingMaterialInspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<IncomingMaterialInspectionItemDTO> items) {
        this.items = items;
    }
}
