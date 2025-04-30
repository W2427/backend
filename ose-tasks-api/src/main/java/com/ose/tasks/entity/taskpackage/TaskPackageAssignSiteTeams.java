package com.ose.tasks.entity.taskpackage;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 任务包班组场地关联实体。
 */
@Entity
@Table(
    name = "task_package_assign_site_team",
    indexes = {
        @Index(columnList = "projectId,taskPackageId,processId")
    }
)
public class TaskPackageAssignSiteTeams extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -9191442268738128672L;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "所属项目 ID")
    @Column(nullable = false, length = 16)
    private Long projectId;

    @Schema(description = "任务包 ID")
    @Column(nullable = false)
    private Long taskPackageId;

    @Schema(description = "工序ID")
    @Column(nullable = false)
    private Long processId;

    @Schema(description = "工作组 ID")
    private Long teamId;

    @Schema(description = "工作组")
    private String teamName;

    @Schema(description = "工作场地 ID")
    private Long workSiteId;

    @Schema(description = "工作场地")
    private String workSiteName;


    @Schema(description = "工作详细场地")
    private String workSiteAddress;

    private String memo;

    @Schema(description = "计划开始时间")
    private Date planStartDate;

    @Schema(description = "计划结束暗井")
    private Date planEndDate;

    @Schema(description = "计划工时")
    private Double planHours;

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

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
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


    public String getMemo() {
        return memo;
    }


    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
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
