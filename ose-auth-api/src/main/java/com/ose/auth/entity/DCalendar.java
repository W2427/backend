package com.ose.auth.entity;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * 图纸记录
 */
@Entity
@Table(name = "calendar")
public class DCalendar extends BaseBizEntity {


    private static final long serialVersionUID = 50491560268864501L;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "")
    private String calendarName;

    @Schema(description = "")
    private String calendarType;

    @Schema(description = "每日工时填写上限")
    private Integer dayWorkHour;

    @Schema(description = "每周工时填写上限")
    private Integer weekWorkHour;

    @Schema(description = "每月工时填写上限")
    private Integer monthWorkHour;

    @Schema(description = "每年工时填写上限")
    private Integer yearWorkHour;

    @Schema(description = "项目tasks")
    private String projectTasks;

    public String getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(String projectTasks) {
        this.projectTasks = projectTasks;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getDayWorkHour() {
        return dayWorkHour;
    }

    public void setDayWorkHour(Integer dayWorkHour) {
        this.dayWorkHour = dayWorkHour;
    }

    public Integer getWeekWorkHour() {
        return weekWorkHour;
    }

    public void setWeekWorkHour(Integer weekWorkHour) {
        this.weekWorkHour = weekWorkHour;
    }

    public Integer getMonthWorkHour() {
        return monthWorkHour;
    }

    public void setMonthWorkHour(Integer monthWorkHour) {
        this.monthWorkHour = monthWorkHour;
    }

    public Integer getYearWorkHour() {
        return yearWorkHour;
    }

    public void setYearWorkHour(Integer yearWorkHour) {
        this.yearWorkHour = yearWorkHour;
    }
}
