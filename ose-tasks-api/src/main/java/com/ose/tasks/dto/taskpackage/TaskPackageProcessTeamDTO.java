package com.ose.tasks.dto.taskpackage;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 任务包工序工作组信息数据传输对象。
 */
public class TaskPackageProcessTeamDTO extends BaseDTO {

    private static final long serialVersionUID = 1398189588084696850L;

    @Schema(description = "工序 ID")
    private Long id;

    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "工序阶段名称")
    private String stageName;

    @Schema(description = "工序名称")
    private String processName;

    @Schema(description = "工作组 ID")
    private Long teamId;

    @Schema(description = "工作组名称")
    private String teamName;

    @Schema(description = "工作场地 ID")
    private Long workSiteId;

    @Schema(description = "工作场地名称")
    private String workSiteName;

    @Schema(description = "计划任务数量")
    private Long wbsCount;

    @Schema(description = "已完成计划任务数量")
    private Long wbsApprovedCount;

    @Schema(description = "计划开始时间")
    private Date planStartDate;

    @Schema(description = "计划结束暗井")
    private Date planEndDate;

    @Schema(description = "计划工时")
    private Double planHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public Long getWbsCount() {
        return wbsCount;
    }

    public void setWbsCount(Long wbsCount) {
        this.wbsCount = wbsCount;
    }

    public Long getWbsApprovedCount() {
        return wbsApprovedCount;
    }

    public void setWbsApprovedCount(Long wbsApprovedCount) {
        this.wbsApprovedCount = wbsApprovedCount;
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
