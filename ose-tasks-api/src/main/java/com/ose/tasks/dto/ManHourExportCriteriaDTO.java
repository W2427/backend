package com.ose.tasks.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class ManHourExportCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 8165564512771158259L;
    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "周次")
    private Integer week;

    @Schema(description = "周次")
    private Integer weekly;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "周一")
    private String monday;

    @Schema(description = "周二")
    private String tuesday;

    @Schema(description = "周三")
    private String wednesday;

    @Schema(description = "周四")
    private String thursday;

    @Schema(description = "周五")
    private String friday;

    @Schema(description = "周六")
    private String saturday;

    @Schema(description = "周日")
    private String sunday;

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

    public Integer getWeekly() {
        return weekly;
    }

    public void setWeekly(Integer weekly) {
        this.weekly = weekly;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
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
