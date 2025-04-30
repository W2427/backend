package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.DrawingRecordStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;


/**
 * 考勤信息
 */
@Entity
@Table(name = "dingtalk_workhour")
public class DingTalkWorkHour extends BaseVersionedBizEntity {

    @Schema(description = "用户Id")
    private String jobNumber;

    @Schema(description = "考勤类型")
    private String checkType;

    @Schema(description = "上班时间")
    private String startCheckTime;

    @Schema(description = "下班时间")
    private String endCheckTime;

    @Schema(description = "填报日期")
    private String workHourDate;

    @Schema(description = "工作时长")
    private Long workDuration;

    public void setWorkDuration(Long workDuration) {
        this.workDuration = workDuration;
    }

    public Long getWorkDuration() {
        return workDuration;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getCheckType() {
        return checkType;
    }

    public String getStartCheckTime() {
        return startCheckTime;
    }

    public String getEndCheckTime() {
        return endCheckTime;
    }

    public String getWorkHourDate() {
        return workHourDate;
    }


    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public void setStartCheckTime(String startCheckTime) {
        this.startCheckTime = startCheckTime;
    }

    public void setEndCheckTime(String endCheckTime) {
        this.endCheckTime = endCheckTime;
    }

    public void setWorkHourDate(String workHourDate) {
        this.workHourDate = workHourDate;
    }
}
