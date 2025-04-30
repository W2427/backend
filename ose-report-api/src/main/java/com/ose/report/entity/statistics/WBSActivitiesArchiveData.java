package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 任务统计归档数据实体。
 */
@Entity
@Table(name = "statistics")
public class WBSActivitiesArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = 2750253530578323934L;

    @Override
    @Schema(description = "创建任务数")
    @JsonProperty("created_activities")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "已停止任务数")
    @JsonProperty("stopped_activities")
    public Double getValue02() {
        return super.getValue02();
    }

    @Override
    @Schema(description = "已完成任务数")
    @JsonProperty("finished_activities")
    public Double getValue03() {
        return super.getValue03();
    }

    @Override
    @Schema(description = "任务工时")
    @JsonProperty("activities_working_hours")
    public Double getValue04() {
        return super.getValue04();
    }

}
