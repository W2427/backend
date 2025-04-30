package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.ApplyStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "overtime_apply_form")
public class OverTimeApplicationForm extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -727274505694048683L;

    @Schema(description = "系统登录用户名")
    @Column
    private String employeeId;

    @Schema(description = "用户Id")
    @Column
    private Long userId;

    @Schema(description = "显示名")
    @Column
    private String displayName;

    @Schema(description = "公司")
    @Column
    private String company;

    @Schema(description = "公司")
    @Column
    private String division;

    @Schema(description = "部门")
    @Column
    private String department;

    @Schema(description = "团队")
    @Column
    private String team;

    @Schema(description = "加班项目名")
    @Column
    private String projectName;

    @Schema(description = "加班项目id")
    @Column
    private Long projectId;

    @Schema(description = "加班任务")
    @Column
    private String task;

    @Schema(description = "加班起始日期")
    @Column
    private Date startDate;

    @Schema(description = "加班结束日期")
    @Column
    private Date endDate;

    @Column
    private Double overTimeHours;

    @Column(columnDefinition = "text")
    private String taskDescription;

    @Column
    private Double mealTimeHours;

    @Column
    private String startTime;


    @Schema(description = "加班工时")
    @Column
    private Double manHour;

    @Schema(description = "项目主管审批状态")
    @Column
    @Enumerated(EnumType.STRING)
    private ApplyStatus applyStatus;

    @Schema(description = "行政人员复核状态")
    @Column
    @Enumerated(EnumType.STRING)
    private ApplyStatus reviewStatus;

    @Schema(description = "项目经理审核Id")
    @Column(length = 65535)
    private String applyRoleId;

    @Schema(description = "项目经理审核Id")
    @Column
    private String applyRoleName;

    @Schema(description = "行政人员审核Id")
    @Column(length = 65535)
    private String reviewRoleId;

    @Schema(description = "项目经理审核Id")
    @Column
    private String reviewRoleName;

    @Schema(description = "apply备注")
    @Column
    private String applyRemark;

    @Schema(description = "review备注")
    @Column
    private String reviewRemark;

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Double getOverTimeHours() {
        return overTimeHours;
    }

    public void setOverTimeHours(Double overTimeHours) {
        this.overTimeHours = overTimeHours;
    }

    public Double getMealTimeHours() {
        return mealTimeHours;
    }

    public void setMealTimeHours(Double mealTimeHours) {
        this.mealTimeHours = mealTimeHours;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Double getManHour() {
        return manHour;
    }

    public void setManHour(Double manHour) {
        this.manHour = manHour;
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

    public ApplyStatus getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(ApplyStatus applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ApplyStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ApplyStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getApplyRoleId() {
        return applyRoleId;
    }

    public void setApplyRoleId(String applyRoleId) {
        this.applyRoleId = applyRoleId;
    }

    public String getReviewRoleId() {
        return reviewRoleId;
    }

    public void setReviewRoleId(String reviewRoleId) {
        this.reviewRoleId = reviewRoleId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getApplyRoleName() {
        return applyRoleName;
    }

    public void setApplyRoleName(String applyRoleName) {
        this.applyRoleName = applyRoleName;
    }

    public String getReviewRoleName() {
        return reviewRoleName;
    }

    public void setReviewRoleName(String reviewRoleName) {
        this.reviewRoleName = reviewRoleName;
    }
}
