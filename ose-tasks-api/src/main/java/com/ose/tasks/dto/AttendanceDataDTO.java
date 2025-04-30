package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class AttendanceDataDTO extends BaseDTO {

    private static final long serialVersionUID = -3487675356634754975L;

    @Schema(description = "用户名")
    private String employeeId;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "部门")
    private String division;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "周次")
    private Integer weekly;

    @Schema(description = "总工时")
    private Double totalHour;

    @Schema(description = "正常总工时")
    private Double normalTime;

    @Schema(description = "加班总工时")
    private Double overTime;

    @Schema(description = "周一")
    private Double mondayHour;

    @Schema(description = "周二")
    private Double tuesdayHour;

    @Schema(description = "周三")
    private Double wednesdayHour;

    @Schema(description = "周四")
    private Double thursdayHour;

    @Schema(description = "周五")
    private Double fridayHour;

    @Schema(description = "周六")
    private Double saturdayHour;

    @Schema(description = "周日")
    private Double sundayHour;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getWeekly() {
        return weekly;
    }

    public void setWeekly(Integer weekly) {
        this.weekly = weekly;
    }

    public Double getTotalHour() {
        return totalHour;
    }

    public void setTotalHour(Double totalHour) {
        this.totalHour = totalHour;
    }

    public Double getNormalTime() {
        return normalTime;
    }

    public void setNormalTime(Double normalTime) {
        this.normalTime = normalTime;
    }

    public Double getOverTime() {
        return overTime;
    }

    public void setOverTime(Double overTime) {
        this.overTime = overTime;
    }

    public Double getMondayHour() {
        return mondayHour;
    }

    public void setMondayHour(Double mondayHour) {
        this.mondayHour = mondayHour;
    }

    public Double getTuesdayHour() {
        return tuesdayHour;
    }

    public void setTuesdayHour(Double tuesdayHour) {
        this.tuesdayHour = tuesdayHour;
    }

    public Double getWednesdayHour() {
        return wednesdayHour;
    }

    public void setWednesdayHour(Double wednesdayHour) {
        this.wednesdayHour = wednesdayHour;
    }

    public Double getThursdayHour() {
        return thursdayHour;
    }

    public void setThursdayHour(Double thursdayHour) {
        this.thursdayHour = thursdayHour;
    }

    public Double getFridayHour() {
        return fridayHour;
    }

    public void setFridayHour(Double fridayHour) {
        this.fridayHour = fridayHour;
    }

    public Double getSaturdayHour() {
        return saturdayHour;
    }

    public void setSaturdayHour(Double saturdayHour) {
        this.saturdayHour = saturdayHour;
    }

    public Double getSundayHour() {
        return sundayHour;
    }

    public void setSundayHour(Double sundayHour) {
        this.sundayHour = sundayHour;
    }
}
