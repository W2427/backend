package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class GalvanizeInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 1058698300276687810L;

    @Schema(description = "报告号")
    private String reportNo;
    private List<GalvanizeInspectionItemDTO> items;

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Override
    public List<GalvanizeInspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<GalvanizeInspectionItemDTO> items) {
        this.items = items;
    }
}
