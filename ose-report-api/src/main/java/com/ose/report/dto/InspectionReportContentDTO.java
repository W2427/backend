package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class InspectionReportContentDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 8844162956300640210L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "项目号")
    private String projectNo;

    @Schema(description = "模块号")
    private String moduleNo;

    @Schema(description = "工序")
    private String process;


    private List<InspectionReportContentItemDTO> items;

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

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Override
    public List<InspectionReportContentItemDTO> getItems() {
        return items;
    }

    public void setItems(List<InspectionReportContentItemDTO> items) {
        this.items = items;
    }
}
