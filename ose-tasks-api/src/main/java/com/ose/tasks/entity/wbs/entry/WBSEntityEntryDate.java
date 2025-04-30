package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * 实体 <->工序的 预设定时间 表
 */
@Entity
@Table(name = "wbs_entry_entity_date")
public class WBSEntityEntryDate extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "实体 ID")
    @Column(nullable = false)
    private Long entityId;

    @Schema(description = "ORG ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "PROJECT ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "工序 ID")
    @Column(nullable = false)
    private Long processId;

    @Schema(description = "计划开始时间")
    @Column
    private Date planStartDate;

    @Schema(description = "计划结束时间")
    @Column
    private Date planEndDate;

    @Schema(description = "实际开始时间")
    @Column
    private Date actualStartDate;

    @Schema(description = "实际结束时间")
    @Column
    private Date actualEndDate;

    @Schema(description = "预计开始时间")
    @Column
    private Date forecastStartDate;

    @Schema(description = "预计结束时间")
    @Column
    private Date forecastEndDate;

    @Schema(description = "是否已完成")
    @Column
    private Boolean completed;

    @Schema(description = "计划工时")
    @Column
    private Double planHours;

    @Schema(description = "实际工时")
    @Column
    private Double actualHours;

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

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Date getForecastStartDate() {
        return forecastStartDate;
    }

    public void setForecastStartDate(Date forecastStartDate) {
        this.forecastStartDate = forecastStartDate;
    }

    public Date getForecastEndDate() {
        return forecastEndDate;
    }

    public void setForecastEndDate(Date forecastEndDate) {
        this.forecastEndDate = forecastEndDate;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Double getPlanHours() {
        return planHours;
    }

    public void setPlanHours(Double planHours) {
        this.planHours = planHours;
    }

    public Double getActualHours() {
        return actualHours;
    }

    public void setActualHours(Double actualHours) {
        this.actualHours = actualHours;
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
}
