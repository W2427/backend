package com.ose.tasks.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;


public class WeeklyManHourDTO extends BaseDTO {

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

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "每周工时")
    private Map<String, Double> weeklyHours = new HashMap<>();

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Map<String, Double> getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(Map<String, Double> weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    // 添加每周工时数据
    public void addWeeklyHour(String weekKey, Double hours) {
        weeklyHours.put(weekKey, hours);
    }

    // 获取指定周的工时
    public Double getWeeklyHour(String weekKey) {
        return weeklyHours.get(weekKey);
    }
}
