package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 焊口完工量统计归档数据实体。
 */
@Entity
@Table(name = "statistics")
public class WBSWeldProgressArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = 2750253530578323934L;

    @Override
    @Schema(description = "完成数量")
    @JsonProperty("count")
    public Double getValue01() {
        return super.getValue01();
    }

    @Override
    @Schema(description = "完成总寸径")
    @JsonProperty("nps")
    public Double getValue02() {
        return super.getValue02();
    }

}
