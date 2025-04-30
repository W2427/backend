package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "bpm_execute_case")
public class BpmExecuteCase extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -141270983613403797L;

    @Schema(description = "组织id")
    @Column
    private Long orgId;

    @Schema(description = "项目id")
    @Column
    private Long projectId;

    @Schema(description = "显示的名称")
    @Column
    private String displayName;

    @Schema(description = "描述")
    @Column
    private String description;

    @Schema(description = "流程实例的类别")
    @Column
    private String actCategory;

    @Schema(description = "工序名称")
    @Column
    private String processNames;

    @Schema(description = "实体子类型")
    @Column
    private String entitySubType;

    @Schema(description = "任务节点名称")
    @Column
    private String taskDefKey;

    @Schema(description = "任务节点权限")
    @Column
    private String privateCategory;

    @Schema(description = "执行任务的 类名")
    @Column
    private String execClass;

    @Schema(description = "是否会签")
    @Column
    private Boolean countersignFlag;

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

    public String getExecClass() {
        return execClass;
    }

    public void setExecClass(String execClass) {
        this.execClass = execClass;
    }

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

    public String getPrivateCategory() {
        return privateCategory;
    }

    public void setPrivateCategory(String privateCategory) {
        this.privateCategory = privateCategory;
    }

    public Boolean getCountersignFlag() {
        return countersignFlag;
    }

    public void setCountersignFlag(Boolean countersignFlag) {
        this.countersignFlag = countersignFlag;
    }

}
