package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 焊工合格率统计归档数据实体。
 */
@Entity
@Table(name = "statistics")
public class WBSWelderArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = 2750253530578323934L;

    @Override
    @Schema(description = "合格寸径")
    @JsonProperty("nps_qualified")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "合格个数")
    @JsonProperty("qualified_count")
    public Double getValue02() {
        return super.getValue02();
    }

    @Override
    @Schema(description = "不合格寸径")
    @JsonProperty("nps_failed")
    public Double getValue03() {
        return super.getValue03();
    }

    @Override
    @Schema(description = "不合格个数")
    @JsonProperty("failed_count")
    public Double getValue04() {
        return super.getValue04();
    }

    public WBSWelderArchiveData() {
    }

    public WBSWelderArchiveData(
        Long welderId,
        String welderName,
        String entityNps,
        String entityMaterial,
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
        setWelderId(welderId == null ? null : welderId);
        setWelderName(StringUtils.isEmpty(welderName) ? null : welderName);
        setEntityNps(StringUtils.isEmpty(entityNps) ? null : entityNps);
        setEntityMaterial(StringUtils.isEmpty(entityMaterial) ? null : entityMaterial);
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
