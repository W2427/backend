package com.ose.tasks.entity.bpm;

import jakarta.persistence.*;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.bpm.BpmPlanExecutionState;

@Entity
@Table(name = "bpm_plan_execution_history",
indexes = {
    @Index(columnList = "projectId,version,executionState,serverUrl")
})
public class BpmPlanExecutionHistory extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    //项目id
    private Long projectId;

    //流程id
    private Long bpmActivityInstanceId;

    //实体id
    private Long entityId;

    //工序id
    private Long processId;

    //操作员id
    private Long operator;

    //操作员姓名
    private String operatorName;

    //是否通过
    @Column(nullable = true)
    private Boolean approved;

    private Double hours;

    //执行状态
    @Enumerated(EnumType.STRING)
    private BpmPlanExecutionState executionState;

    private String serverUrl;

    private Long startTimestamp;

    private Boolean forceStart;

    private Long endTimestamp;

    private String memo;

    private Long version;

    private Boolean isHalt;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getBpmActivityInstanceId() {
        return bpmActivityInstanceId;
    }

    public void setBpmActivityInstanceId(Long bpmActivityInstanceId) {
        this.bpmActivityInstanceId = bpmActivityInstanceId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public BpmPlanExecutionState getExecutionState() {
        return executionState;
    }

    public void setExecutionState(BpmPlanExecutionState executionState) {
        this.executionState = executionState;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Double getHours() {
        if (hours == null)
            return (double) 0;
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getHalt() {
        return isHalt;
    }

    public void setHalt(Boolean halt) {
        isHalt = halt;
    }

    public Boolean getForceStart() {
        return forceStart;
    }

    public void setForceStart(Boolean forceStart) {
        this.forceStart = forceStart;
    }
}
