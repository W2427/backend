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
public class WBSProgressGroupArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = 2750253530578323934L;

    @Override
    @Schema(description = "物量 工作量")
    @JsonProperty("workLoad")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "未完成的 物量 工作量")
    @JsonProperty("unfinishedWorkLoad")
    public Double getValue02() {
        return super.getValue02();
    }

    @Override
    @Schema(description = "任务个数")
    @JsonProperty("workCount")
    public Double getValue03() {
        return super.getValue03();
    }

    @Override
    @Schema(description = "未完成的任务个数")
    @JsonProperty("unfinishedWorkCount")
    public Double getValue04() {
        return super.getValue04();
    }

    @Override
    @Schema(description = "材料匹配率")
    @JsonProperty("matchPercent")
    public Double getValue05() {
        return super.getValue05();
    }

    @Override
    @Schema(description = "图纸完成率")
    @JsonProperty("issuePercent")
    public Double getValue06() {
        return super.getValue06();
    }


    public WBSProgressGroupArchiveData() {
    }

    public WBSProgressGroupArchiveData(
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
