package com.ose.tasks.dto.performance;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class PerformanceAppraisalListDetailDTO extends BaseDTO {
    @Schema(description = "绩效考核编号")
    private String performanceAppraisalId;

    @Schema(description = "员工编号")
    private String employeeId;

    @Schema(description = "员工姓名")
    private String employeeName;

    @Schema(description = "工作年限")
    private Double yearsOfEmployment;

    @Schema(description = "工作经验年限")
    private Double yearsOfExperience;

    @Schema(description = "员工类别")
    private String employeeCategory;

    @Schema(description = "工程师级别")
    private String engineerLevel;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "季度")
    private Integer quarter;

    @Schema(description = "月份")
    private Integer month;

    @Schema(description = "出勤天数")
    private Double siteDays;

    @Schema(description = "评估代码")
    private String evaluationCode;

    @Schema(description = "评估人1编号")
    private String appraiserId1;

    @Schema(description = "评估人1姓名")
    private String appraiserName1;

    @Schema(description = "审核人1编号")
    private String reviewerId1;

    @Schema(description = "审核人1姓名")
    private String reviewerName1;

    @Schema(description = "审核人2编号")
    private String reviewerId2;

    @Schema(description = "审核人2姓名")
    private String reviewerName2;

    @Schema(description = "评估人2编号")
    private String appraiserId2;

    @Schema(description = "评估人2（项目经理）姓名")
    private String appraiserName2;

    @Schema(description = "评估人3/审核人3（负责人/工程师）编号")
    private String appraiserReviewerId3;

    @Schema(description = "评估人3/审核人3（负责人/工程师）姓名")
    private String appraiserReviewerName3;

    @Schema(description = "总休假时长")
    private Double totalLeave;

    @Schema(description = "标准工时")
    private Double standardManhour;

    @Schema(description = "加班时长")
    private Double overtime;

    @Schema(description = "出勤率")
    private Double attendancePercentage;

    public Double getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(Double totalLeave) {
        this.totalLeave = totalLeave;
    }

    public Double getStandardManhour() {
        return standardManhour;
    }

    public void setStandardManhour(Double standardManhour) {
        this.standardManhour = standardManhour;
    }

    public String getPerformanceAppraisalId() {
        return performanceAppraisalId;
    }

    public void setPerformanceAppraisalId(String performanceAppraisalId) {
        this.performanceAppraisalId = performanceAppraisalId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Double getYearsOfEmployment() {
        return yearsOfEmployment;
    }

    public void setYearsOfEmployment(Double yearsOfEmployment) {
        this.yearsOfEmployment = yearsOfEmployment;
    }

    public Double getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Double yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getSiteDays() {
        return siteDays;
    }

    public void setSiteDays(Double siteDays) {
        this.siteDays = siteDays;
    }

    public String getEvaluationCode() {
        return evaluationCode;
    }

    public void setEvaluationCode(String evaluationCode) {
        this.evaluationCode = evaluationCode;
    }

    public String getAppraiserId1() {
        return appraiserId1;
    }

    public void setAppraiserId1(String appraiserId1) {
        this.appraiserId1 = appraiserId1;
    }

    public String getAppraiserName1() {
        return appraiserName1;
    }

    public void setAppraiserName1(String appraiserName1) {
        this.appraiserName1 = appraiserName1;
    }

    public String getReviewerId1() {
        return reviewerId1;
    }

    public void setReviewerId1(String reviewerId1) {
        this.reviewerId1 = reviewerId1;
    }

    public String getReviewerName1() {
        return reviewerName1;
    }

    public void setReviewerName1(String reviewerName1) {
        this.reviewerName1 = reviewerName1;
    }

    public String getReviewerId2() {
        return reviewerId2;
    }

    public void setReviewerId2(String reviewerId2) {
        this.reviewerId2 = reviewerId2;
    }

    public String getReviewerName2() {
        return reviewerName2;
    }

    public void setReviewerName2(String reviewerName2) {
        this.reviewerName2 = reviewerName2;
    }

    public String getAppraiserId2() {
        return appraiserId2;
    }

    public void setAppraiserId2(String appraiserId2) {
        this.appraiserId2 = appraiserId2;
    }

    public String getAppraiserName2() {
        return appraiserName2;
    }

    public void setAppraiserName2(String appraiserName2) {
        this.appraiserName2 = appraiserName2;
    }

    public String getAppraiserReviewerId3() {
        return appraiserReviewerId3;
    }

    public void setAppraiserReviewerId3(String appraiserReviewerId3) {
        this.appraiserReviewerId3 = appraiserReviewerId3;
    }

    public String getAppraiserReviewerName3() {
        return appraiserReviewerName3;
    }

    public void setAppraiserReviewerName3(String appraiserReviewerName3) {
        this.appraiserReviewerName3 = appraiserReviewerName3;
    }
}
