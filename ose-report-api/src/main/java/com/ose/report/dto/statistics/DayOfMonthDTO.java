package com.ose.report.dto.statistics;

public class DayOfMonthDTO {

    private Integer month;

    private Integer day;

    public DayOfMonthDTO() {
    }

    public DayOfMonthDTO(Integer month, Integer day) {
        this.month = month;
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

}
