package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;



public class EmployeeDataDTO extends BaseDTO {

    @Schema(description = "工号")
    private String employeeId;

    @Schema(description = "员工id")
    private String id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "所属公司")
    private String company;

    @Schema(description = "合同公司")
    private String contractCompany;

    @Schema(description = "所属事业部")
    private String division;

    @Schema(description = "所属部门")
    private String department;

    @Schema(description = "所属团队")
    private String team;

    @Schema(description = "首次参加工作时间")
    private String initialEmploymentDate;


    @Schema(description = "员工状态(2:试用期, 3:正式, 5:待离职, -1:无状态)")
    private String employeeStatus;

    @Schema(description = "实际转正日期")
    private String regularTime;

    @Schema(description = "实际入职时间")
    private String confirmJoinTime;

    @Schema(description = "工龄")
    private Double lengthOfCareer;

    @Schema(description = "总年假")
    private Double totalAnnualLeave;

    @Schema(description = "当前累计剩余年假")
    private Double remainingAnnual;

    @Schema(description = "上月剩余年假")
    private Double remainingAnnualLastMth = 0.0;

    @Schema(description = "当前累计可用调休")
    private Double remainingOt;

    @Schema(description = "上月剩余可用调休")
    private Double remainingOtLastMth;

    @Schema(description = "超出时长")
    private Double absence = 0.0;

    @Schema(description = "当月请假 （年假、事假）")
    private Double usedAnnualLeaveThisMonth;

    @Schema(description = "计算当月请假 （病假）")
    private Double usedSickLeaveThisMonth;

    @Schema(description = "当月请假 （除 年假、事假、病假 以外的所有假）")
    private Double usedOtherLeaveThisMonth;

    @Schema(description = "当月总工时")
    private Double totalNormalManHour;

    @Schema(description = "当月总加班工时")
    private Double totalOvertime;

    @Schema(description = "上年度剩余年假")
    private Double specialAnnualLeave;

    @Schema(description = "总年假（上年度剩余年假+当年度总年假）")
    private Double totalAnnual;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getSpecialAnnualLeave() {
        return specialAnnualLeave;
    }

    public void setSpecialAnnualLeave(Double specialAnnualLeave) {
        this.specialAnnualLeave = specialAnnualLeave;
    }

    public Double getTotalAnnual() {
        return totalAnnual;
    }

    public void setTotalAnnual(Double totalAnnual) {
        this.totalAnnual = totalAnnual;
    }

    public Double getRemainingOtLastMth() {
        return remainingOtLastMth;
    }

    public void setRemainingOtLastMth(Double remainingOtLastMth) {
        this.remainingOtLastMth = remainingOtLastMth;
    }

    public Double getAbsence() {
        return absence;
    }

    public void setAbsence(Double absence) {
        this.absence = absence;
    }

    public Double getRemainingAnnual() {
        return remainingAnnual;
    }

    public void setRemainingAnnual(Double remainingAnnual) {
        this.remainingAnnual = remainingAnnual;
    }


    public Double getTotalNormalManHour() {
        return totalNormalManHour;
    }

    public void setTotalNormalManHour(Double totalNormalManHour) {
        this.totalNormalManHour = totalNormalManHour;
    }

    public Double getTotalOvertime() {
        return totalOvertime;
    }

    public void setTotalOvertime(Double totalOvertime) {
        this.totalOvertime = totalOvertime;
    }

    public Double getUsedAnnualLeaveThisMonth() {
        return usedAnnualLeaveThisMonth;
    }

    public void setUsedAnnualLeaveThisMonth(Double usedAnnualLeaveThisMonth) {
        this.usedAnnualLeaveThisMonth = usedAnnualLeaveThisMonth;
    }

    public Double getUsedSickLeaveThisMonth() {
        return usedSickLeaveThisMonth;
    }

    public void setUsedSickLeaveThisMonth(Double usedSickLeaveThisMonth) {
        this.usedSickLeaveThisMonth = usedSickLeaveThisMonth;
    }

    public Double getUsedOtherLeaveThisMonth() {
        return usedOtherLeaveThisMonth;
    }

    public void setUsedOtherLeaveThisMonth(Double usedOtherLeaveThisMonth) {
        this.usedOtherLeaveThisMonth = usedOtherLeaveThisMonth;
    }

    public String getConfirmJoinTime() {
        return confirmJoinTime;
    }

    public void setConfirmJoinTime(String confirmJoinTime) {
        this.confirmJoinTime = confirmJoinTime;
    }

    public Double getRemainingOt() {
        return remainingOt;
    }

    public void setRemainingOt(Double remainingOt) {
        this.remainingOt = remainingOt;
    }

    public Double getRemainingAnnualLastMth() {
        return remainingAnnualLastMth;
    }

    public void setRemainingAnnualLastMth(Double remainingAnnualLastMth) {
        this.remainingAnnualLastMth = remainingAnnualLastMth;
    }

    public String getRegularTime() {
        return regularTime;
    }

    public void setRegularTime(String regularTime) {
        this.regularTime = regularTime;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Double getTotalAnnualLeave() {
        return totalAnnualLeave;
    }

    public void setTotalAnnualLeave(Double totalAnnualLeave) {
        this.totalAnnualLeave = totalAnnualLeave;
    }


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

    public String getContractCompany() {
        return contractCompany;
    }

    public void setContractCompany(String contractCompany) {
        this.contractCompany = contractCompany;
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getInitialEmploymentDate() {
        return initialEmploymentDate;
    }

    public void setInitialEmploymentDate(String initialEmploymentDate) {
        this.initialEmploymentDate = initialEmploymentDate;
    }

    public Double getLengthOfCareer() {
        return lengthOfCareer;
    }

    public void setLengthOfCareer(Double lengthOfCareer) {
        this.lengthOfCareer = lengthOfCareer;
    }
}
