package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ExternalInspectionItemListDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 5692025309135156166L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "外检流程")
    private String procedure;

    private List<ExternalInspectionItemDTO> items;

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
    public List<ExternalInspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ExternalInspectionItemDTO> items) {
        this.items = items;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }
}
