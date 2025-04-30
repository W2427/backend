package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 遗留问题统计归档数据实体。
 */
@Entity
@Table(name = "statistics")
public class ManHourStatisticsArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = -855669658495280143L;

    @Override
    @Schema(description = "正常工时")
    @JsonProperty("workHour")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "加班工时")
    @JsonProperty("outHour")
    public Double getValue02() {
        return super.getValue02();
    }

    public ManHourStatisticsArchiveData() {
    }

    public ManHourStatisticsArchiveData(
        String projectName,
        String weekly,
        Integer groupYear,
        Integer groupMonth,
        Integer groupDay,
        Integer groupWeek,
        Integer groupDate,
        Double value01,
        Double value02,
        Double value03,
        Double value04,
        Double value05,
        Double value06,
        Double value07,
        Double value08,
        Double value09
    ) {
        setProjectName(StringUtils.isEmpty(projectName) ? null : projectName);
        setWeekly(StringUtils.isEmpty(weekly) ? null : weekly);
        setGroupYear(groupYear);
        setGroupMonth(groupMonth);
        setGroupDay(groupDay);
        setGroupWeek(groupWeek);
        setGroupDate(groupDate);
        setValue01(value01);
        setValue02(value02);
        setValue03(value03);
        setValue04(value04);
        setValue05(value05);
        setValue06(value06);
        setValue07(value07);
        setValue08(value08);
        setValue09(value09);
    }
}
