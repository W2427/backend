package com.ose.report.dto.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.dto.BaseDTO;
import com.ose.report.entity.statistics.ArchiveData;
import com.ose.report.vo.ArchiveScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Map;

/**
 * 统计数据归档时间。
 */
public class ArchiveTimeDTO extends BaseDTO {

    private static final long serialVersionUID = 3902963592354779400L;
    public static final int FROM = 0;
    public static final int UNTIL = 1;

    @Schema(name = "归档时间期间类型", hidden = true)
    private ArchiveScheduleType scheduleType;

    @Schema(name = "年", hidden = true)
    private Integer year;

    @Schema(name = "月", hidden = true)
    private Integer month;

    @Schema(name = "日", hidden = true)
    private Integer day;

    @Schema(name = "周", hidden = true)
    private Integer week;

    @JsonIgnore // 日报周报需要归档日期，但是不用这个属性，所以不返回给前台
    @Schema(hidden = true)
    private boolean include = true;

    public ArchiveTimeDTO() {
    }

    public ArchiveTimeDTO(Map<String, Object> record) {
        setYear((Number) record.get("groupYear"));
        setMonth((Number) record.get("groupMonth"));
        setDay((Number) record.get("groupDay"));
        setWeek((Number) record.get("groupWeek"));
    }

    public ArchiveTimeDTO(ArchiveData archiveData) {
        setYear(archiveData.getArchiveYear());
        setMonth(archiveData.getArchiveMonth());
        setDay(archiveData.getArchiveDay());
    }

    public ArchiveTimeDTO(Calendar calendar) {

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDay(calendar.get(Calendar.DAY_OF_MONTH));

        int year = calendar.get(Calendar.YEAR);

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER
            && calendar.get(Calendar.WEEK_OF_YEAR) == 0) {
            year += 1;
        }

        setWeek(year * 100 + (calendar.get(Calendar.WEEK_OF_YEAR) + 1));
    }

    public ArchiveTimeDTO(Integer year, Integer month, Integer day, Integer week) {
        this(year, month, day, week, -1);
    }

    public ArchiveTimeDTO(Integer year, Integer month, Integer day, Integer week, int fromUntil) {

        month = month <= 0 ? null : month;
        day = day <= 0 ? null : day;
        week = week <= 0 ? null : week;

        if (day != null) {
            this.scheduleType = ArchiveScheduleType.DAILY;
        } else if (week != null) {
            year = year > 0 ? year : (week / 100);
            week = week % 100;
            this.scheduleType = ArchiveScheduleType.WEEKLY;
        } else if (month != null) {
            this.scheduleType = ArchiveScheduleType.MONTHLY;
        }

        if (day == null && fromUntil >= 0) {
            if (month != null && fromUntil == FROM) {
                day = 1;
            } else {

                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.HOUR_OF_DAY, 8);

                // 取得指定月份的最后一天
                if (month != null) {
                    calendar.set(Calendar.MONTH, month - 1);
                    day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    // 取得周的第一天/最后一天
                } else if (week != null) {
                    calendar.set(Calendar.WEEK_OF_YEAR, week);
                    if (fromUntil == FROM) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    } else if (fromUntil == UNTIL) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    }
                    month = calendar.get(Calendar.MONTH) + 1;
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                }
            }
        }

        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
    }

    public ArchiveScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ArchiveScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Number year) {
        if (year instanceof BigInteger) {
            this.year = year.intValue();
        } else {
            this.year = (Integer) year;
        }
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Number month) {
        if (month instanceof BigInteger) {
            this.month = month.intValue();
        } else {
            this.month = (Integer) month;
        }
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Number day) {
        if (day instanceof BigInteger) {
            this.day = day.intValue();
        } else {
            this.day = (Integer) day;
        }
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Number week) {
        if (week instanceof BigInteger) {
            this.week = week.intValue();
        } else {
            this.week = (Integer) week;
        }
    }

    public boolean isInclude() {
        return include;
    }

    public void setInclude(boolean include) {
        this.include = include;
    }

    @JsonIgnore
    public Integer toInteger() {
        return this.year * 10000 + this.month * 100 + this.day;
    }

    public int toYearWeek() {

        int year = this.year;

        if (this.week != null) {
            return year * 100 + this.week % 100;
        }

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, this.year);

        if (this.month != null) {
            calendar.set(Calendar.MONTH, this.month - 1);
        }

        if (this.day != null) {
            calendar.set(Calendar.DAY_OF_MONTH, this.day);
        }

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER
            && calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
            year += 1;
        }

        return year * 100 + calendar.get(Calendar.WEEK_OF_YEAR);
    }
}
