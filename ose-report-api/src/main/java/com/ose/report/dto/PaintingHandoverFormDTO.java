package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PaintingHandoverFormDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 3999099696050572780L;

    @Schema(description = "报告编号")
    private String applicationNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "专业")
    private String discipline;

    private List<PaintingHandoverItemDTO> items;

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    @Override
    public List<PaintingHandoverItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PaintingHandoverItemDTO> items) {
        this.items = items;
    }
}
