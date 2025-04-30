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
public class WBSIssuesArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = 2750253530578323934L;

    @Override
    @Schema(description = "1周内未解决问题")
    @JsonProperty("issueInOneWeek")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "1~4周内未解决问题")
    @JsonProperty("issueBetween1WeekAnd4Weeks")
    public Double getValue02() {
        return super.getValue02();
    }

    @Override
    @Schema(description = "4周前未解决问题")
    @JsonProperty("issueBefore4Weeks")
    public Double getValue03() {
        return super.getValue03();
    }

    @Override
    @Schema(description = "已完成数量")
    @JsonProperty("issueFinished")
    public Double getValue04() {
        return super.getValue04();
    }

    @Override
    @Schema(description = "总数（日/周报）")
    @JsonProperty("total")
    public Double getValue05() {
        return super.getValue05();
    }

    @Override
    @Schema(description = "当日/周完成数量（日/周报）")
    @JsonProperty("closed")
    public Double getValue06() {
        return super.getValue06();
    }

    @Override
    @Schema(description = "剩余数量（日/周报）")
    @JsonProperty("open")
    public Double getValue07() {
        return super.getValue07();
    }

    public WBSIssuesArchiveData() {
    }

    public WBSIssuesArchiveData(
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
