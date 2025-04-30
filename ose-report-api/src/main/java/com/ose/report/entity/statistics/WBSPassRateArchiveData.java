package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 报验合格率统计归档数据实体。
 */
@Entity
@Table(name = "statistics")
public class WBSPassRateArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = 2750253530578323934L;

    @Override
    @Schema(description = "内检单数量")
    @JsonProperty("internal_inspection")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "合格内检单数量")
    @JsonProperty("qualified_internal_inspection")
    public Double getValue02() {
        return super.getValue02();
    }

    @Override
    @Schema(description = "外检单数量")
    @JsonProperty("external_inspection")
    public Double getValue03() {
        return super.getValue03();
    }

    @Override
    @Schema(description = "合格外检单数量")
    @JsonProperty("qualified_external_inspection")
    public Double getValue04() {
        return super.getValue04();
    }

    @Override
    @Schema(description = "内检一次合格数量")
    @JsonProperty("fpy1_internal_inspection")
    public Double getValue05() {
        return super.getValue05();
    }

    @Override
    @Schema(description = "外检一次合格数量")
    @JsonProperty("fpy1_external_inspection")
    public Double getValue06() {
        return super.getValue06();
    }

    @Override
    @Schema(description = "内检一次不合格数量")
    @JsonProperty("fpy2_internal_inspection")
    public Double getValue07() {
        return super.getValue07();
    }

    @Override
    @Schema(description = "外检一次不合格数量")
    @JsonProperty("fpy2_external_inspection")
    public Double getValue08() {
        return super.getValue08();
    }

    public WBSPassRateArchiveData() {
    }

    public WBSPassRateArchiveData(
        String module,
        String stage,
        String process,
        String teamName,
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
        setTeamName(StringUtils.isEmpty(teamName) ? null : teamName);
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
