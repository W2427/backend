package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class GalvanizationPartsInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -3554221456045137088L;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "项目编号")
    private String projectNo;

    @Schema(description = "加工厂名称")
    private String supplierName;

    @Schema(description = "检验地点")
    private String inspectionSite;

    private List<GalvanizationPartsInspectionItemDTO> items;

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

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getInspectionSite() {
        return inspectionSite;
    }

    public void setInspectionSite(String inspectionSite) {
        this.inspectionSite = inspectionSite;
    }

    @Override
    public List<GalvanizationPartsInspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<GalvanizationPartsInspectionItemDTO> items) {
        this.items = items;
    }
}
