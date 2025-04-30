package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * 请假信息
 */
@Entity
@Table(name = "dingtalk_leavedata")
public class DingTalkLeaveData extends BaseVersionedBizEntity {

    @Schema(description = "职工Id")
    private String jobNumber;

    @Schema(description = "用户Id")
    private String userId;

    @Schema(description = "请假开始时间")
    private String startLeaveTime;

    @Schema(description = "请假结束时间")
    private String endLeaveTime;

    @Schema(description = "请假单位(天/小时)")
    private String durationUnit;

    @Schema(description = "请假类型")
    private String leaveCode;

    @Schema(description = "请假时长(小时)")
    private Double durationPercent;

    @Schema(description = "员工姓名")
    private String userName;

    @Schema(description = "公司属地")
    private String userCompany;

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartLeaveTime() {
        return startLeaveTime;
    }

    public void setStartLeaveTime(String startLeaveTime) {
        this.startLeaveTime = startLeaveTime;
    }

    public String getEndLeaveTime() {
        return endLeaveTime;
    }

    public void setEndLeaveTime(String endLeaveTime) {
        this.endLeaveTime = endLeaveTime;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Double getDurationPercent() {
        return durationPercent;
    }

    public void setDurationPercent(Double durationPercent) {
        this.durationPercent = durationPercent;
    }

    public String getLeaveCode() { return leaveCode; }
    public void setLeaveCode(String leaveCode) { this.leaveCode = leaveCode; }

}
