package com.ose.auth.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateProjectHourDTO extends PageDTO {

    private static final long serialVersionUID = -219768479114821898L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "每日工时填写上限")
    private Integer dayWorkHour;

    @Schema(description = "项目task")
    private String projectTasks;

    public String getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(String projectTasks) {
        this.projectTasks = projectTasks;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getDayWorkHour() {
        return dayWorkHour;
    }

    public void setDayWorkHour(Integer dayWorkHour) {
        this.dayWorkHour = dayWorkHour;
    }
}
