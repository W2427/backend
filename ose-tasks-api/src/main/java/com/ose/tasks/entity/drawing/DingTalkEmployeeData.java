package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * 请假信息
 */
@Entity
@Table(name = "dingtalk_employeedata")
public class DingTalkEmployeeData extends BaseVersionedBizEntity {

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "工号")
    private String jobNumber;

    @Schema(description = "入职时间")
    private String confirmJoinTime;

    @Schema(description = "员工状态(2:试用期, 3:正式, 5:待离职, -1:无状态)")
    private String employeeStatus;

    @Schema(description = "实际转正日期")
    private String regularTime;

    @Schema(description = "试用期")
    private String probationPeriodType;

    //---------------------------

    @Schema(description = "首次参加工作时间")
    private String initialEmploymentDate;

    @Schema(description = "工龄")
    private Double lengthOfCareer;

    @Schema(description = "当年度总年假")
    private double totalAnnualLeave;

    @Schema(description = "上月剩余年假")
    private Double remainingAnnualLastMth = 0.0;

    @Schema(description = "累计剩余年假")
    private Double remainingAnnual;

    @Schema(description = "请假时长是否超出")
    private Double absence;

    @Schema(description = "总加班时长（一个季度内）")
    private Double totalOt;

    @Schema(description = "累计剩余加班时长")
    private Double remainingOt;

    @Schema(description = "上月剩余加班时长")
    private Double remainingOtLastMth = 0.0;

    @Schema(description = "上年度剩余年假")
    private double specialAnnualLeave;

    @Schema(description = "总年假（上年度剩余年假+当年度总年假）")
    private double totalAnnual;

    public void setTotalOt(Double totalOt) {
        this.totalOt = totalOt;
    }

    public double getSpecialAnnualLeave() {
        return specialAnnualLeave;
    }

    public void setSpecialAnnualLeave(double specialAnnualLeave) {
        this.specialAnnualLeave = specialAnnualLeave;
    }

    public double getTotalAnnual() {
        return totalAnnual;
    }

    public void setTotalAnnual(double totalAnnual) {
        this.totalAnnual = totalAnnual;
    }

    public Double getAbsence() {
        return absence;
    }

    public void setAbsence(Double absence) {
        this.absence = absence;
    }

    public Double getTotalOt() {
        return totalOt;
    }

    public void setTotalOT(Double totalOt) {
        this.totalOt = totalOt;
    }

    public Double getRemainingOt() {
        return remainingOt;
    }

    public void setRemainingOt(Double remainingOt) {
        this.remainingOt = remainingOt;
    }

    public Double getRemainingOtLastMth() {
        return remainingOtLastMth;
    }

    public void setRemainingOtLastMth(Double remainingOtLastMth) {
        this.remainingOtLastMth = remainingOtLastMth;
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

    public Double getRemainingAnnualLastMth() {
        return remainingAnnualLastMth;
    }

    public double getTotalAnnualLeave() {
        return totalAnnualLeave;
    }

    public void setTotalAnnualLeave(double totalAnnualLeave) {
        this.totalAnnualLeave = totalAnnualLeave;
    }

    public void setRemainingAnnualLastMth(Double remainingAnnualLastMth) {
        this.remainingAnnualLastMth = remainingAnnualLastMth;
    }

    public Double getRemainingAnnual() {
        return remainingAnnual;
    }

    public void setRemainingAnnual(Double remainingAnnual) {
        this.remainingAnnual = remainingAnnual;
    }


    public String getProbationPeriodType() {
        return probationPeriodType;
    }

    public void setProbationPeriodType(String probationPeriodType) {
        this.probationPeriodType = probationPeriodType;
    }

    public String getRegularTime() {
        return regularTime;
    }

    public void setRegularTime(String regularTime) {
        this.regularTime = regularTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getConfirmJoinTime() {
        return confirmJoinTime;
    }

    public void setConfirmJoinTime(String confirmJoinTime) {
        this.confirmJoinTime = confirmJoinTime;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }
}
