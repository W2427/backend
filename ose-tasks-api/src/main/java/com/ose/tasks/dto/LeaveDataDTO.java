package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class LeaveDataDTO extends BaseDTO {

//    private static final long serialVersionUID = 5827682673160098536L;
    @Schema(description = "dr工作日期")
    private String workHourDate;

    @Schema(description = "dr职工姓名")
    private String engineer;

    @Schema(description = "dr请假时长")
    private Double workHour;

    @Schema(description = "dr请假类型")
    private String task;

    @Schema(description = "钉钉职工号")
    private String jobNumber;

    @Schema(description = "钉钉请假开始时间")
    private String startLeaveTime;

    @Schema(description = "钉钉请假结束时间")
    private String endLeaveTime;

    @Schema(description = "钉钉请假时长(小时)")
    private Double durationPercent;

    @Schema(description = "钉钉请假类型")
    private String leaveCode;

    @Schema(description = "请假对比时长")
    private Double checkHour;

    @Schema(description = "公司属地")
    private String userCompany;

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Double getCheckHour() {
        return checkHour;
    }

    public void setCheckHour(Double checkHour) {
        this.checkHour = checkHour;
    }

    public String getWorkHourDate() {
        return workHourDate;
    }

    public void setWorkHourDate(String workHourDate) {
        this.workHourDate = workHourDate;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
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

    public Double getDurationPercent() {
        return durationPercent;
    }

    public void setDurationPercent(Double durationPercent) {
        this.durationPercent = durationPercent;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }
}
