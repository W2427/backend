package com.ose.tasks.entity.wbs.entry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseEntity;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.*;

/**
 * WBS 条目数据。
 */
@Entity
@jakarta.persistence.Table(
    name = "wbs_entry_state",
    indexes = {
        @Index(columnList = "projectId,entityId,runningStatus"),
        @Index(columnList = "projectId,taskPackageId"),
        @Index(columnList = "wbsEntryId",unique = true),
        @Index(columnList = "projectId,runningStatus")

    }
)
public class WBSEntryState extends BaseEntity {

    // 支持的专业
    public static final Set<String> SUPPORTED_DISCIPLINES = new HashSet<>(Arrays.asList("PIPING", "STRUCTURE","ELECTRICAL"));
    private static final long serialVersionUID = 8943114264493478301L;

    static {
        SUPPORTED_DISCIPLINES.add("PIPING");
    }

    @Column
    private Long wbsEntryId;

    @Column(nullable = false)
    @JsonIgnore
    private Long lastModifiedBy;

    @Schema(description = "最后更新时间")
    @Column(nullable = false)
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date lastModifiedAt;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "实体 ID")
    @Column
    private Long entityId;

    @Schema(description = "已完成任务的权重总和")
    @Column
    private double finishedScore = 0.0;

    @JsonIgnore
    @Schema(description = "任务执行团队 ID")
    @Column
    private Long teamId;

    @Schema(description = "工作场地 ID")
    @Column
    private Long workSiteId;

    @Schema(description = "工作场地名称")
    @Column
    private String workSiteName;

    @Schema(description = "工作场地地址")
    @Column
    private String workSiteAddress;

    @Schema(description = "启动者 ID")
    @Column
    private Long startedBy;

    @Schema(description = "实际开始时间")
    @Column
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date startedAt;

    @Schema(description = "实际结束时间")
    @Column
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date finishedAt;

    @Schema(description = "实际工时")
    @Column
    private Double actualDuration;

    @Schema(description = "实际工时(新)")
    @Column
    private Double actualManHours;

    @Schema(description = "是否已完成")
    @Column
    private boolean finished = false;

    @Schema(description = "任务执行团队组织路径")
    @Column
    private String teamPath;

    @Schema(description = "任务执行团队组织")
    @Column
    private String teamName;

    @Schema(description = "四级计划运行状态")
    @Column
    @Enumerated(EnumType.STRING)
    private WBSEntryRunningStatus runningStatus;

    @Schema(description = "任务包 ID")
    @Column
    private Long taskPackageId;

    @Schema(description = "手动进度")
    @Column(name = "progress")
    private Double manualProgress;

    @Schema(description = "工序 ID")
    @Column
    private Long processId;

    @Schema(description = "工作流实例 ID")
    @Column
    private Long processInstanceId;

    @Schema(description = "总权重")
    @Column
    private double totalScore = 0.0;

    @Schema(description = "是否活跃")
    @Column(columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean active;

    @Schema(description = "工作量 物量")
    @Column
    private Double workLoad;

    @Schema(description = "计划开始时间")
    @Column
    private Date planStartDate;

    @Schema(description = "计划结束暗井")
    @Column
    private Date planEndDate;

    @Schema(description = "计划工时")
    @Column
    private Double planHours;

    @JsonIgnore
    public Double getManualProgress() {
        return manualProgress;
    }

    public void setManualProgress(Double manualProgress) {
        this.manualProgress = manualProgress;
    }

    public WBSEntryState() {
        super();
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public double getFinishedScore() {
        return finishedScore;
    }

    public void setFinishedScore(double finishedScore) {
        this.finishedScore = finishedScore;
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

    public Long getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(Long startedBy) {
        this.startedBy = startedBy;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Double getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(Double actualDuration) {
        this.actualDuration = actualDuration;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @JsonProperty("progress")
    public double getProgress() {
        return manualProgress == null ? 0.0 : manualProgress;
    }

    public String getTeamPath() {
        return teamPath;
    }

    public void setTeamPath(String teamPath) {
        this.teamPath = teamPath;
    }

    public WBSEntryRunningStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(WBSEntryRunningStatus runningStatus) {
        this.runningStatus = runningStatus;
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

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    @Schema(description = "任务包")
    @JsonProperty(value = "taskPackage", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getTaskPackageRef() {
        return taskPackageId == null ? null : new ReferenceData(taskPackageId);
    }

    public Double getActualManHours() {
        return actualManHours;
    }

    public void setActualManHours(Double actualManHours) {
        this.actualManHours = actualManHours;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getWbsEntryId() {
        return wbsEntryId;
    }

    public void setWbsEntryId(Long wbsEntryId) {
        this.wbsEntryId = wbsEntryId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Double workLoad) {
        this.workLoad = workLoad;
    }

    public String getWorkSiteAddress() {
        return workSiteAddress;
    }

    public void setWorkSiteAddress(String workSiteAddress) {
        this.workSiteAddress = workSiteAddress;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Double getPlanHours() {
        return planHours;
    }

    public void setPlanHours(Double planHours) {
        this.planHours = planHours;
    }
}
