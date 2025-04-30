package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 焊接合格率统计归档数据实体。
 */
@Entity
@Table(name = "statistics")
public class WBSNDTRateArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = 551348476405176792L;

    @Override
    @Schema(description = "NDT一次合格寸径")
    @JsonProperty("qualifiedNps")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "NDT一次合格个数")
    @JsonProperty("qualifiedCount")
    public Double getValue02() {
        return super.getValue02();
    }

    @Override
    @Schema(description = "NDT不合格寸径")
    @JsonProperty("failedNps")
    public Double getValue03() {
        return super.getValue03();
    }

    @Override
    @Schema(description = "NDT不合格个数")
    @JsonProperty("failedCount")
    public Double getValue04() {
        return super.getValue04();
    }

    public WBSNDTRateArchiveData() {
    }

    public WBSNDTRateArchiveData(
        String module,
        String stage,
        String entityMaterial,
        String teamName,
        String weldType,
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
        setEntityMaterial(StringUtils.isEmpty(entityMaterial) ? null : entityMaterial);
        setTeamName(StringUtils.isEmpty(teamName) ? null : teamName);
        setWeldType(StringUtils.isEmpty(weldType) ? null : weldType);
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
