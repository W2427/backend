package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;


public class CheckStandardWorkHourDTO extends BaseDTO {

    @Schema(description = "工号")
    private String jobNumber;

    @Schema(description = "姓名")
    private String employeeName;

    @Schema(description = "所属公司")
    private String company;

    @Schema(description = "所属部门")
    private String department;

    @Schema(description = "所属事业部")
    private String division;

    @Schema(description = "所属团队")
    private String team;

    @Schema(description = "填报总工时")
    private double totalNormalManHour;

    @Schema(description = "填报总请假")
    private double totalLeaveHour;

    @Schema(description = "检查填报工时情况")
    private double checkStandardWorkHour;

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public double getTotalNormalManHour() {
        return totalNormalManHour;
    }

    public void setTotalNormalManHour(double totalNormalManHour) {
        this.totalNormalManHour = totalNormalManHour;
    }

    public double getTotalLeaveHour() {
        return totalLeaveHour;
    }

    public void setTotalLeaveHour(double totalLeaveHour) {
        this.totalLeaveHour = totalLeaveHour;
    }

    public double getCheckStandardWorkHour() {
        return checkStandardWorkHour;
    }

    public void setCheckStandardWorkHour(double checkStandardWorkHour) {
        this.checkStandardWorkHour = checkStandardWorkHour;
    }
}
