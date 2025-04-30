package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 详细设计状态统计归档数据实体。
 */
@Entity
@Table(name = "statistics")
public class DetailDesignArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = 2750253530578323934L;

    @Override
    @Schema(description = "创建数量")
    @JsonProperty("createdCount")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "完成数量")
    @JsonProperty("finishedCount")
    public Double getValue02() {
        return super.getValue02();
    }

    public DetailDesignArchiveData() {
    }

    public DetailDesignArchiveData(
        String module,
        String pressureTestPackage,
        String subSystem,
        String issueType,
        Long departmentId,
        String departmentName,
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
        setPressureTestPackage(StringUtils.isEmpty(pressureTestPackage) ? null : pressureTestPackage);
        setSubSystem(StringUtils.isEmpty(subSystem) ? null : subSystem);
        setIssueType(StringUtils.isEmpty(issueType) ? null : issueType);
        setDepartmentId(departmentId == null ? null : departmentId);
        setDepartmentName(StringUtils.isEmpty(departmentName) ? null : departmentName);
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
