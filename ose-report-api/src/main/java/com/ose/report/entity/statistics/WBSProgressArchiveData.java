package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * WBS 进度统计归档数据实体。
 */
@Entity
@Table(name = "statistics")
public class WBSProgressArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = 2750253530578323934L;

    @Override
    @Schema(description = "总权重")
    @JsonProperty("totalScore")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "计划工时")
    @JsonProperty("estimatedManHours")
    public Double getValue02() {
        return super.getValue02();
    }

    @Override
    @Schema(description = "完成权重之和")
    @JsonProperty("finishedScore")
    public Double getValue03() {
        return super.getValue03();
    }

    @Override
    @Schema(description = "完成工时")
    @JsonProperty("actualManHours")
    public Double getValue04() {
        return super.getValue04();
    }

    public WBSProgressArchiveData() {
    }

    public WBSProgressArchiveData(
        String module,
        String stage,
        String process,
        Integer groupYear,
        Integer groupMonth,
        Integer groupDay,
        Integer groupWeek,
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
        setModule(StringUtils.isEmpty(module) ? null : module);
        setStage(StringUtils.isEmpty(stage) ? null : stage);
        setProcess(StringUtils.isEmpty(process) ? null : process);
        setGroupYear(groupYear);
        setGroupMonth(groupMonth);
        setGroupDay(groupDay);
        setGroupWeek(groupWeek);
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
