package com.ose.tasks.dto.performance;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.util.Date;

public class PerformanceAppraisalDTO extends BaseDTO {

    private static final long serialVersionUID = 240969369844648739L;

    @Schema(description = "员工编号")
    private String employeeId;

    @Schema(description = "绩效考核编号")
    private String pefId;

    @Schema(description = "员工姓名")
    private String name;

    @Schema(description = "一月出勤天数")
    private String siteDays1;

    @Schema(description = "二月出勤天数")
    private String siteDays2;

    @Schema(description = "三月出勤天数")
    private String siteDays3;

    private String siteDaysSum;

    @Schema(description = "工作年限")
    private String yearsOfEmployment;

    @Schema(description = "工作经验年限")
    private String yearsOfExperience;

    @Schema(description = "入职日期")
    private String joiningDate;

    @Schema(description = "转为正式员工日期")
    private String dateTransferToRegularEmployee;

    @Schema(description = "评估人员工编号")
    private String employeeIdOfAppraiser;

    private String evaluationParty;

    @Schema(description = "评估人id")
    private String appraiserId;

    @Schema(description = "评估人姓名")
    private String appraiser;

    private String month1;

    private String month1TotalLeave;

    private String month1StandardManhour;

    private String month1Overtime;

    private String month1Attendance;

    private String month2;

    private String month2TotalLeave;

    private String month2StandardManhour;

    private String month2Overtime;

    private String month2Attendance;

    private String month3;

    private String month3TotalLeave;

    private String month3StandardManhour;

    private String month3Overtime;

    private String month3Attendance;

    private String totalLeave;

    private String standardManhour;

    private String summaryStandardManhour;

    private String overtime;

    private String attendance;

    public String getSiteDays1() {
        return siteDays1;
    }

    public void setSiteDays1(String siteDays1) {
        this.siteDays1 = siteDays1;
    }

    public String getSiteDays2() {
        return siteDays2;
    }

    public void setSiteDays2(String siteDays2) {
        this.siteDays2 = siteDays2;
    }

    public String getSiteDays3() {
        return siteDays3;
    }

    public void setSiteDays3(String siteDays3) {
        this.siteDays3 = siteDays3;
    }

    public String getSiteDaysSum() {
        return siteDaysSum;
    }

    public void setSiteDaysSum(String siteDaysSum) {
        this.siteDaysSum = siteDaysSum;
    }

    public String getSummaryStandardManhour() {
        return summaryStandardManhour;
    }

    public void setSummaryStandardManhour(String summaryStandardManhour) {
        this.summaryStandardManhour = summaryStandardManhour;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPefId() {
        return pefId;
    }

    public void setPefId(String pefId) {
        this.pefId = pefId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYearsOfEmployment() {
        return yearsOfEmployment;
    }

    public void setYearsOfEmployment(String yearsOfEmployment) {
        this.yearsOfEmployment = yearsOfEmployment;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getDateTransferToRegularEmployee() {
        return dateTransferToRegularEmployee;
    }

    public void setDateTransferToRegularEmployee(String dateTransferToRegularEmployee) {
        this.dateTransferToRegularEmployee = dateTransferToRegularEmployee;
    }

    public String getEmployeeIdOfAppraiser() {
        return employeeIdOfAppraiser;
    }

    public void setEmployeeIdOfAppraiser(String employeeIdOfAppraiser) {
        this.employeeIdOfAppraiser = employeeIdOfAppraiser;
    }

    public String getEvaluationParty() {
        return evaluationParty;
    }

    public void setEvaluationParty(String evaluationParty) {
        this.evaluationParty = evaluationParty;
    }

    public String getAppraiserId() {
        return appraiserId;
    }

    public void setAppraiserId(String appraiserId) {
        this.appraiserId = appraiserId;
    }

    public String getAppraiser() {
        return appraiser;
    }

    public void setAppraiser(String appraiser) {
        this.appraiser = appraiser;
    }

    public String getMonth1() {
        return month1;
    }

    public void setMonth1(String month1) {
        this.month1 = month1;
    }

    public String getMonth1TotalLeave() {
        return month1TotalLeave;
    }

    public void setMonth1TotalLeave(String month1TotalLeave) {
        this.month1TotalLeave = month1TotalLeave;
    }

    public String getMonth1StandardManhour() {
        return month1StandardManhour;
    }

    public void setMonth1StandardManhour(String month1StandardManhour) {
        this.month1StandardManhour = month1StandardManhour;
    }

    public String getMonth1Overtime() {
        return month1Overtime;
    }

    public void setMonth1Overtime(String month1Overtime) {
        this.month1Overtime = month1Overtime;
    }

    public String getMonth1Attendance() {
        return month1Attendance;
    }

    public void setMonth1Attendance(String month1Attendance) {
        this.month1Attendance = month1Attendance;
    }

    public String getMonth2() {
        return month2;
    }

    public void setMonth2(String month2) {
        this.month2 = month2;
    }

    public String getMonth2TotalLeave() {
        return month2TotalLeave;
    }

    public void setMonth2TotalLeave(String month2TotalLeave) {
        this.month2TotalLeave = month2TotalLeave;
    }

    public String getMonth2StandardManhour() {
        return month2StandardManhour;
    }

    public void setMonth2StandardManhour(String month2StandardManhour) {
        this.month2StandardManhour = month2StandardManhour;
    }

    public String getMonth2Overtime() {
        return month2Overtime;
    }

    public void setMonth2Overtime(String month2Overtime) {
        this.month2Overtime = month2Overtime;
    }

    public String getMonth2Attendance() {
        return month2Attendance;
    }

    public void setMonth2Attendance(String month2Attendance) {
        this.month2Attendance = month2Attendance;
    }

    public String getMonth3() {
        return month3;
    }

    public void setMonth3(String month3) {
        this.month3 = month3;
    }

    public String getMonth3TotalLeave() {
        return month3TotalLeave;
    }

    public void setMonth3TotalLeave(String month3TotalLeave) {
        this.month3TotalLeave = month3TotalLeave;
    }

    public String getMonth3StandardManhour() {
        return month3StandardManhour;
    }

    public void setMonth3StandardManhour(String month3StandardManhour) {
        this.month3StandardManhour = month3StandardManhour;
    }

    public String getMonth3Overtime() {
        return month3Overtime;
    }

    public void setMonth3Overtime(String month3Overtime) {
        this.month3Overtime = month3Overtime;
    }

    public String getMonth3Attendance() {
        return month3Attendance;
    }

    public void setMonth3Attendance(String month3Attendance) {
        this.month3Attendance = month3Attendance;
    }

    public String getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(String totalLeave) {
        this.totalLeave = totalLeave;
    }

    public String getStandardManhour() {
        return standardManhour;
    }

    public void setStandardManhour(String standardManhour) {
        this.standardManhour = standardManhour;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
