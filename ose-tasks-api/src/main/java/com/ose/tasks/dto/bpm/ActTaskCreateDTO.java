package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class ActTaskCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "工序")
    private Long processId;

    @Schema(description = "责任人")
    private Long assignee;

    @Schema(description = "流程模型名称")
    private String modelName;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

}
