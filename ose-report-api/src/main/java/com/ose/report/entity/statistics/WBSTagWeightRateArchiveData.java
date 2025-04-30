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
public class WBSTagWeightRateArchiveData extends ArchiveDataBase {


    private static final long serialVersionUID = 1159700327876607611L;

    @Override
    @Schema(description = "总设计重量")
    @JsonProperty("totalWeight")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "已完成重量")
    @JsonProperty("doneWeight")
    public Double getValue02() {
        return super.getValue02();
    }

    @Override
    @Schema(description = "所有四级计划")
    @JsonProperty("totalWbsEntry")
    public Double getValue03() {
        return super.getValue03();
    }

    @Override
    @Schema(description = "已完成四级计划")
    @JsonProperty("doneWbsEntry")
    public Double getValue04() {
        return super.getValue04();
    }

    @Override
    @Schema(description = "总焊口长度")
    @JsonProperty("totalLength")
    public Double getValue05() {
        return super.getValue05();
    }

    @Override
    @Schema(description = "已完成焊口长度")
    @JsonProperty("doneLength")
    public Double getValue06() {
        return super.getValue06();
    }

    public WBSTagWeightRateArchiveData() {
    }

    public WBSTagWeightRateArchiveData(
        String module,
        String deck,
        String entitySubType,
        String process,
        String teamName,
        String address,
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
        setDeck(StringUtils.isEmpty(deck) ? null : deck);
        setEntitySubType(StringUtils.isEmpty(entitySubType) ? null : entitySubType);
        setProcess(StringUtils.isEmpty(process) ? null : process);
        setTeamName(StringUtils.isEmpty(teamName) ? null : teamName);
        setAddress(StringUtils.isEmpty(address) ? null : address);
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
