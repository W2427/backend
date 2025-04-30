package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class ModelDeployDTO extends BaseDTO {

    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "工序ID")
    private Long bpmProcessId;

    @Schema(description = "项目ID/工作流中是租户ID")
    private Long projectId;

    @Schema(description = "流程名称")
    private String processName;

    @Schema(description = "流程Key, Id")
    private Long processKey;

    @Schema(description = "上传的临时文件名")
    private String temporaryName;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "分类")
    private String category;


    public String getTemporaryName() {
        return temporaryName;
    }

    public void setTemporaryName(String temporaryName) {
        this.temporaryName = temporaryName;
    }

    public String getDescription() {
        return description;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProcessKey() {
        return processKey;
    }

    public void setProcessKey(Long processKey) {
        this.processKey = processKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getBpmProcessId() {
        return bpmProcessId;
    }

    public void setBpmProcessId(Long bpmProcessId) {
        this.bpmProcessId = bpmProcessId;
    }
}
