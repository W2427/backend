package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 修改项目数据接口。
 */
public class RepairWbsListDTO extends BaseDTO {

    private static final long serialVersionUID = 1412831602182018427L;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "组织id")
    private Long orgId;

    @Schema(description = "组织id")
    private  String projectName;

    @Schema(description = "组织id")
    private Long wbsEntryId;

    @Schema(description = "组织id")
    private String wbsName;

    @Schema(description = "组织id")
    private String stage;

    @Schema(description = "组织id")
    private String process;

    @Schema(description = "组织id")
    private String entityType;

    @Schema(description = "组织id")
    private String startAt;

    @Schema(description = "组织id")
    private String startedAt;

    @Schema(description = "组织id")
    private String finishedAt;

    @Schema(description = "组织id")
    private EntityStatus runningStatus;

    @Schema(description = "组织id")
    private Boolean predecessorFinished;

    @Schema(description = "组织id")
    private Integer predecessorTotal;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getWbsEntryId() {
        return wbsEntryId;
    }

    public void setWbsEntryId(Long wbsEntryId) {
        this.wbsEntryId = wbsEntryId;
    }

    public String getWbsName() {
        return wbsName;
    }

    public void setWbsName(String wbsName) {
        this.wbsName = wbsName;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }

    public EntityStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(EntityStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public Boolean isPredecessorFinished() {
        return predecessorFinished;
    }

    public void setPredecessorFinished(Boolean predecessorFinished) {
        this.predecessorFinished = predecessorFinished;
    }

    public Integer getPredecessorTotal() {
        return predecessorTotal;
    }

    public void setPredecessorTotal(Integer predecessorTotal) {
        this.predecessorTotal = predecessorTotal;
    }
}
