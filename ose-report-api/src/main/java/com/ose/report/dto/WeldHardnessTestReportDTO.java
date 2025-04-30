package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WeldHardnessTestReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -3991236334222245582L;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "构结构名称件号")
    private String structureName;

    @Schema(description = "NDT图号")
    private String ndtDrawingNo;
    private List<WeldHardnessTestItemDTO> items;

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
    public String getStructureName() {
        return structureName;
    }

    @Override
    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getNdtDrawingNo() {
        return ndtDrawingNo;
    }

    public void setNdtDrawingNo(String ndtDrawingNo) {
        this.ndtDrawingNo = ndtDrawingNo;
    }

    @Override
    public List<WeldHardnessTestItemDTO> getItems() {
        return items;
    }

    public void setItems(List<WeldHardnessTestItemDTO> items) {
        this.items = items;
    }
}
