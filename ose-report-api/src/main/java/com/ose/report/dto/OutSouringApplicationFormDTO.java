package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Date;

public class OutSouringApplicationFormDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -2082773880577568563L;

    @Schema(description = "报告编号")
    private String applicationNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "部门")
    private String department;
    private List<OutSouringApplicationItemDTO> items;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public List<OutSouringApplicationItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OutSouringApplicationItemDTO> items) {
        this.items = items;
    }
}
