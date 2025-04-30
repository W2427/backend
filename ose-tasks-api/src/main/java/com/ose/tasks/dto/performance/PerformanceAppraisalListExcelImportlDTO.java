package com.ose.tasks.dto.performance;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.util.Date;

public class PerformanceAppraisalListExcelImportlDTO extends BaseDTO {
    private static final long serialVersionUID = 8864323175373948482L;
    @Schema(description = "序号")
    private Integer serialNumber;

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

    private String month1TotalLeave;

    private String month1StandardManhour;

    private String month1Overtime;

    private String month1Attendance;

    private String month2TotalLeave;

    private String month2StandardManhour;

    private String month2Overtime;

    private String month2Attendance;

    private String month3TotalLeave;

    private String month3StandardManhour;

    private String month3Overtime;

    private String month3Attendance;

    private String totalLeave;

    private String standardManhour;

    private String overtime;

    private String attendance;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "合同公司")
    private String contractCompany;

    @Schema(description = "部门")
    private String division;

    @Schema(description = "职位")
    private String jobTitle;

    @Schema(description = "入职日期")
    private String joiningDate;

    @Schema(description = "转为正式员工日期")
    private String dateTransferToRegularEmployee;

    @Schema(description = "工作年限")
    private String yearsOfEmployment;

    @Schema(description = "工作经验年限")
    private String yearsOfExperience;

    @Schema(description = "第一学位")
    private String firstDegree;

    @Schema(description = "评估代码")
    private String evaluationCode;

    @Schema(description = "评估人1员工编号")
    private String employeeIdOfAppraiser1;

    @Schema(description = "评估人1姓名")
    private String appraiser1;

    @Schema(description = "审核人1员工编号")
    private String employeeIdOfReviewer1;

    @Schema(description = "审核人1姓名")
    private String reviewer1;

    @Schema(description = "审核人2员工编号")
    private String employeeIdOfReviewer2;

    @Schema(description = "审核人2姓名")
    private String reviewer2;

    @Schema(description = "评估人2（项目经理）员工编号")
    private String employeeIdOfAppraiser2;

    @Schema(description = "评估人2（项目经理）姓名")
    private String appraiser2;

    @Schema(description = "评估人3/审核人3（负责人/工程师）员工编号")
    private String employeeIdOfAppraiserReviewer3;

    @Schema(description = "评估人3/审核人3（负责人/工程师）姓名")
    private String appraiserReviewer3;

    @Schema(description = "员工类别")
    private String employeeCategory;

    @Schema(description = "工程师级别")
    private String engineerLevel;

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

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getFirstDegree() {
        return firstDegree;
    }

    public void setFirstDegree(String firstDegree) {
        this.firstDegree = firstDegree;
    }

    public String getEvaluationCode() {
        return evaluationCode;
    }

    public void setEvaluationCode(String evaluationCode) {
        this.evaluationCode = evaluationCode;
    }

    public String getEmployeeIdOfAppraiser1() {
        return employeeIdOfAppraiser1;
    }

    public void setEmployeeIdOfAppraiser1(String employeeIdOfAppraiser1) {
        this.employeeIdOfAppraiser1 = employeeIdOfAppraiser1;
    }

    public String getAppraiser1() {
        return appraiser1;
    }

    public void setAppraiser1(String appraiser1) {
        this.appraiser1 = appraiser1;
    }

    public String getEmployeeIdOfReviewer1() {
        return employeeIdOfReviewer1;
    }

    public void setEmployeeIdOfReviewer1(String employeeIdOfReviewer1) {
        this.employeeIdOfReviewer1 = employeeIdOfReviewer1;
    }

    public String getReviewer1() {
        return reviewer1;
    }

    public void setReviewer1(String reviewer1) {
        this.reviewer1 = reviewer1;
    }

    public String getEmployeeIdOfReviewer2() {
        return employeeIdOfReviewer2;
    }

    public void setEmployeeIdOfReviewer2(String employeeIdOfReviewer2) {
        this.employeeIdOfReviewer2 = employeeIdOfReviewer2;
    }

    public String getReviewer2() {
        return reviewer2;
    }

    public void setReviewer2(String reviewer2) {
        this.reviewer2 = reviewer2;
    }

    public String getEmployeeIdOfAppraiser2() {
        return employeeIdOfAppraiser2;
    }

    public void setEmployeeIdOfAppraiser2(String employeeIdOfAppraiser2) {
        this.employeeIdOfAppraiser2 = employeeIdOfAppraiser2;
    }

    public String getAppraiser2() {
        return appraiser2;
    }

    public void setAppraiser2(String appraiser2) {
        this.appraiser2 = appraiser2;
    }

    public String getEmployeeIdOfAppraiserReviewer3() {
        return employeeIdOfAppraiserReviewer3;
    }

    public void setEmployeeIdOfAppraiserReviewer3(String employeeIdOfAppraiserReviewer3) {
        this.employeeIdOfAppraiserReviewer3 = employeeIdOfAppraiserReviewer3;
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

    public String getAppraiserReviewer3() {
        return appraiserReviewer3;
    }

    public void setAppraiserReviewer3(String appraiserReviewer3) {
        this.appraiserReviewer3 = appraiserReviewer3;
    }

    public String getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(String employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getEngineerLevel() {
        return engineerLevel;
    }

    public void setEngineerLevel(String engineerLevel) {
        this.engineerLevel = engineerLevel;
    }

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
}
