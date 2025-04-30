package com.ose.tasks.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.drawing.Drawing;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;


public class CheckOutHourDTO extends BaseDTO {

    private static final long serialVersionUID = 5827682673160098536L;
    @Schema(description = "职工号")
    private String username;

    @Schema(description = "职工姓名")
    private String engineer;

    @Schema(description = "工作日期")
    private String workHourDate;

    @Schema(description = "工时")
    private Double workHour;

    @Schema(description = "加班工时")
    private Double outHour;


    @Schema(description = "钉钉加班工时")
    private Double dingTalkOutHour;

    @Schema(description = "加班工时比对")
    private Double checkHour;

    @Schema(description = "上班打卡")
    private String startHour;

    @Schema(description = "下班打卡")
    private String endHour;

    @ExcelIgnore
    @Schema(description = "工作时长")
    private Double workDuration;

    @Schema(description = "公司属地")
    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(Double workDuration) {
        this.workDuration = workDuration;
    }

    public void setCheckHour(Double checkHour) {
        this.checkHour = checkHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public Double getCheckHour() {
        return checkHour;
    }

    public String getStartHour() {
        return startHour;
    }

    public String getEndHour() {
        return endHour;
    }


    //    public void setCheck(Double checkHour) {
//        this.checkHour = checkHour;
//    }
//
//    public Double getCheck() {
//        return checkHour;
//    }



    public void setWorkHourDate(String workHourDate) {
        this.workHourDate = workHourDate;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
    }

    public void setOutHour(Double outHour) {
        this.outHour = outHour;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkHourDate() {
        return workHourDate;
    }

    public String getEngineer() {
        return engineer;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public Double getOutHour() {
        return outHour;
    }

    public String getUsername() {
        return username;
    }

    public Double getDingTalkOutHour() {
        return dingTalkOutHour;
    }

    public void setDingTalkOutHour(Double dingTalkOutHour) {
        this.dingTalkOutHour = dingTalkOutHour;
    }
}
