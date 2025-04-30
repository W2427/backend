package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.entity.BaseEntity;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 任务 状态表，存储状态信息
 */
@Entity
@Table(name = "bpm_activity_instance_state",
        indexes = {
            @Index(columnList = "baiId",unique = true),
            @Index(columnList = "projectId,baiId,finishState,suspensionState"),
            @Index(columnList = "projectId,entityId"),
            @Index(columnList = "projectId,id"),
            @Index(columnList = "projectId,teamName,teamId"),
            @Index(columnList = "projectId,workSiteAddress"),
            @Index(columnList = "workSiteName"),
            @Index(columnList = "workSiteAddress"),
            @Index(columnList = "currentExecutor"),
            @Index(columnList = "suspensionState"),
            @Index(columnList = "finishState"),
            @Index(columnList = "teamName"),
            @Index(columnList = "taskPackageName"),
        })
public class BpmActivityInstanceState extends BaseEntity {

    private static final long serialVersionUID = 8893202326677433085L;
    //任务基础表id
    @Column
    private Long baiId;

    //项目id
    @Column
    private Long projectId;

    @Column
    private Long entityId;

    //组织id
    @Column
    private Long orgId;

    @Schema(description = "最后更新时间")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    @Column
    private Date lastModifiedAt;

    @Transient
    private String taskDefKey;

    @Schema(description = "实际开始时间")
    @Column(name = "start_date")
    private Date startDate;

    @Schema(description = "实际结束时间")
    @Column(name = "end_date")
    private Date endDate;

    @Schema(description = "工时")
    @Column(name = "cost_hour")
    private Double costHour;

    @Schema(description = "挂起状态")
    @Enumerated(EnumType.STRING)
    private SuspensionState suspensionState;

    @Schema(description = "完成状态")
    @Enumerated(EnumType.STRING)
    private ActInstFinishState finishState;

    @Schema(description = "执行结果 不合格 flag")
    private boolean executeNgFlag = false;

    @Schema(description = "正在运行的任务节点执行人")
    private String currentExecutor;

    @Schema(description = "工作场地 ID")
    private Long workSiteId;

    @Schema(description = "工作场地名称")
    private String workSiteName;

    @Schema(description = "工作场地地址")
    private String workSiteAddress;

    @Schema(description = "工作班组")
    private String teamName;

    @Schema(description = "工作班组ID")
    private Long teamId;

    @Schema(description = "任务包名称")
    private String taskPackageName;

    @Schema(description = "任务包ID")
    private Long taskPackageId;

    @Schema(description = "流程内驳回次数")
    @Column(columnDefinition = "INT default 0")
    private int unAcceptCount = 0;

    @Schema(description = "当前任务节点")
    @Transient
    private String currentTaskNode;


    @Schema(description = "当前任务并行网关优先选项")
    @Column
    private String currentSelectForGateway;

    @Schema(description = "当前任务节点")
    @Transient
    private Date currentTaskNodeDate;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "cosign被拒")
    private Boolean isRejectCoSign = false;

    public Boolean getRejectCoSign() {
        return isRejectCoSign;
    }

    public void setRejectCoSign(Boolean rejectCoSign) {
        isRejectCoSign = rejectCoSign;
    }

    public String getCurrentSelectForGateway() {
        return currentSelectForGateway;
    }

    public void setCurrentSelectForGateway(String currentSelectForGateway) {
        this.currentSelectForGateway = currentSelectForGateway;
    }

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setSuspensionState(SuspensionState suspensionState) {
        this.suspensionState = suspensionState;
    }

    public ActInstFinishState getFinishState() {
        return finishState;
    }

    public void setFinishState(ActInstFinishState finishState) {
        this.finishState = finishState;
    }

    public SuspensionState getSuspensionState() {
        return suspensionState;
    }


    public boolean isExecuteNgFlag() {
        return executeNgFlag;
    }

    public void setExecuteNgFlag(boolean executeNgFlag) {
        this.executeNgFlag = executeNgFlag;
    }

    public Double getCostHour() {
        return costHour;
    }

    public void setCostHour(Double costHour) {
        this.costHour = costHour;
    }

    public String getCurrentExecutor() {
        return currentExecutor;
    }

    public void setCurrentExecutor(String currentExecutor) {
        this.currentExecutor = currentExecutor;
    }

    public String getCurrentTaskNode() {
        return currentTaskNode;
    }

    public void setCurrentTaskNode(String currentTaskNode) {
        this.currentTaskNode = currentTaskNode;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public int getUnAcceptCount() {
        return unAcceptCount;
    }

    public void setUnAcceptCount(int unAcceptCount) {
        this.unAcceptCount = unAcceptCount;
    }

    public void addUnAcceptCount() {
        this.unAcceptCount = unAcceptCount + 1;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getWorkSiteAddress() {
        return workSiteAddress;
    }

    public void setWorkSiteAddress(String workSiteAddress) {
        this.workSiteAddress = workSiteAddress;
    }

    public Date getCurrentTaskNodeDate() {
        return currentTaskNodeDate;
    }

    public void setCurrentTaskNodeDate(Date currentTaskNodeDate) {
        this.currentTaskNodeDate = currentTaskNodeDate;
    }

    public Long getBaiId() {
        return baiId;
    }

    public void setBaiId(Long baiId) {
        this.baiId = baiId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

}
