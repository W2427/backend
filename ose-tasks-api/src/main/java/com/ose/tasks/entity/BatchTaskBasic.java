package com.ose.tasks.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * 批处理任务数据实体。
 */
@Entity
@Table(name = "batch_task")
public class BatchTaskBasic extends BatchTaskBase {

    private static final long serialVersionUID = -875337161095222729L;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "专业")
    private String discipline;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
