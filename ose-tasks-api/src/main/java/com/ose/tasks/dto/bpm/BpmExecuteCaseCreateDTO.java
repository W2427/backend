package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class BpmExecuteCaseCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 4784570913290706342L;

    @Schema(description = "组织id")
    private Long orgId;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "显示的名称")
    private String displayName;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "流程实例的类别")
    private String actCategory;

    @Schema(description = "工序名称")
    private String processNames;

    @Schema(description = "实体子类型")
    private String entitySubType;

    @Schema(description = "任务节点名称")
    private String taskDefKey;

    @Schema(description = "任务节点权限")
    private String privateCategory;

    @Schema(description = "执行任务的 类名")
    private String execClass;

    @Schema(description = "是否会签")
    private Boolean countersignFlag;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActCategory() {
        return actCategory;
    }

    public void setActCategory(String actCategory) {
        this.actCategory = actCategory;
    }

    public String getProcessNames() {
        return processNames;
    }

    public void setProcessNames(String processNames) {
        this.processNames = processNames;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getPrivateCategory() {
        return privateCategory;
    }

    public void setPrivateCategory(String privateCategory) {
        this.privateCategory = privateCategory;
    }

    public String getExecClass() {
        return execClass;
    }

    public void setExecClass(String execClass) {
        this.execClass = execClass;
    }

    public Boolean getCountersignFlag() {
        return countersignFlag;
    }

    public void setCountersignFlag(Boolean countersignFlag) {
        this.countersignFlag = countersignFlag;
    }
}
