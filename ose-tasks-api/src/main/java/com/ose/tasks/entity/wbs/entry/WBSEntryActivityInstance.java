package com.ose.tasks.entity.wbs.entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 计划活动实例视图。
 */
@Entity
@Table(name = "wbs_entry_activity_instance")
public class WBSEntryActivityInstance extends BaseEntity {

    private static final long serialVersionUID = 4722109956243085756L;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "计划条目路径")
    @JsonIgnore
    @Column
    private String wbsPath;

    @Schema(description = "模块类型")
    @Column
    private String moduleType;

    @Schema(description = "工序阶段 ID")
    @Column
    private Long stageId;

    @Schema(description = "工序阶段名称")
    @Column
    private String stageName;

    @Schema(description = "工序 ID")
    @Column
    private Long processId;

    @Schema(description = "工序名称")
    @Column
    private String processName;

    @Schema(description = "任务名称")
    @Column
    private String taskName;

    @Schema(description = "实体类型")
    @Column

    private String entityType;

    @Schema(description = "实体子类型")
    @Column(length = 64)
    private String entitySubType;

    @Schema(description = "实体 ID")
    @Column
    private Long entityId;

    @Schema(description = "实体编号")
    @Column
    private String entityNo;

    @Schema(description = "施工工作组组织路径")
    @JsonIgnore
    @Column
    private String teamPath;

    @Schema(description = "施工工作组 ID")
    @JsonIgnore
    @Column
    private Long teamId;

    @Schema(description = "工作场地 ID")
    @Column
    private Long workSiteId;

    @Schema(description = "工作场地名称")
    @Column
    private String workSiteName;

    @Schema(description = "施工工人用户 ID")
    @Column
    private Long assigneeId;

    @Schema(description = "所需权限")
    @Column
    private String privileges;

    @Schema(description = "计划开始时间")
    @Column
    private Date planStartAt;

    @Schema(description = "计划结束时间")
    @Column
    private Date planFinishAt;

    @Schema(description = "实际开始时间")
    @Column
    private Date actualStartedAt;

    @Schema(description = "工序实例 ID")
    @Column
    private String processInstanceId;

    @Schema(description = "是否已结束")
    @Column
    private boolean finished;

    @Schema(description = "备注")
    @Column
    private String remarks;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getWbsPath() {
        return wbsPath;
    }

    public void setWbsPath(String wbsPath) {
        this.wbsPath = wbsPath;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getTeamPath() {
        return teamPath;
    }

    public void setTeamPath(String teamPath) {
        this.teamPath = teamPath;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public Date getPlanStartAt() {
        return planStartAt;
    }

    public void setPlanStartAt(Date planStartAt) {
        this.planStartAt = planStartAt;
    }

    public Date getPlanFinishAt() {
        return planFinishAt;
    }

    public void setPlanFinishAt(Date planFinishAt) {
        this.planFinishAt = planFinishAt;
    }

    public Date getActualStartedAt() {
        return actualStartedAt;
    }

    public void setActualStartedAt(Date actualStartedAt) {
        this.actualStartedAt = actualStartedAt;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public Set<Long> relatedOrgIDs() {
        return teamId == null
            ? new HashSet<>()
            : new HashSet<>(Collections.singletonList(teamId));
    }

    @Schema(description = "工作组信息")
    @JsonProperty(value = "team", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getTeamRef() {
        return teamId == null ? null : new ReferenceData(teamId);
    }

}
