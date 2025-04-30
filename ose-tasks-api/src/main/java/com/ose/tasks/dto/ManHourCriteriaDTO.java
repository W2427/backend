package com.ose.tasks.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/24
 */
public class ManHourCriteriaDTO extends PageDTO {
    private static final long serialVersionUID = -2102569089810386928L;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "周次")
    private Integer week;

    @Schema(description = "周次")
    private List<Integer> weeks;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "导出类型")
    private String type;

    @Schema(description = "周次")
    private Integer weekly;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "周一")
    private Date monday;

    @Schema(description = "周二")
    private Date tuesday;

    @Schema(description = "周三")
    private Date wednesday;

    @Schema(description = "周四")
    private Date thursday;

    @Schema(description = "周五")
    private Date friday;

    @Schema(description = "周六")
    private Date saturday;

    @Schema(description = "周日")
    private Date sunday;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "一级部门")
    private String division;

    @Schema(description = "二级部门")
    private String department;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Integer> weeks) {
        this.weeks = weeks;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Integer getWeekly() {
        return weekly;
    }

    public void setWeekly(Integer weekly) {
        this.weekly = weekly;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getMonday() {
        return monday;
    }

    public void setMonday(Date monday) {
        this.monday = monday;
    }

    public Date getTuesday() {
        return tuesday;
    }

    public void setTuesday(Date tuesday) {
        this.tuesday = tuesday;
    }

    public Date getWednesday() {
        return wednesday;
    }

    public void setWednesday(Date wednesday) {
        this.wednesday = wednesday;
    }

    public Date getThursday() {
        return thursday;
    }

    public void setThursday(Date thursday) {
        this.thursday = thursday;
    }

    public Date getFriday() {
        return friday;
    }

    public void setFriday(Date friday) {
        this.friday = friday;
    }

    public Date getSaturday() {
        return saturday;
    }

    public void setSaturday(Date saturday) {
        this.saturday = saturday;
    }

    public Date getSunday() {
        return sunday;
    }

    public void setSunday(Date sunday) {
        this.sunday = sunday;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
