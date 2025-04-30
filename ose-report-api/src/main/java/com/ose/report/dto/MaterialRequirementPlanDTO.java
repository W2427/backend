package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Date;

public class MaterialRequirementPlanDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -8494196249889826897L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "周用料计划单编号")
    private String materialRequirementPlan;
    private List<MaterialRequirementItemDTO> items;

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

    public String getMaterialRequirementPlan() {
        return materialRequirementPlan;
    }

    public void setMaterialRequirementPlan(String materialRequirementPlan) {
        this.materialRequirementPlan = materialRequirementPlan;
    }

    @Override
    public List<MaterialRequirementItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MaterialRequirementItemDTO> items) {
        this.items = items;
    }
}
