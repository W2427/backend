package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class F253StructureFabricationFitupInspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = -6603987389215292404L;

    private String projectName;

    private String seriesNo;

    @Schema(description = "报告号")
    private String reportNo;

    private String moduleNo;

    private List<F253StructureFabricationFitupInspectionReportItemDTO> items;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
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
    public List<F253StructureFabricationFitupInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<F253StructureFabricationFitupInspectionReportItemDTO> items) {
        this.items = items;
    }

}
