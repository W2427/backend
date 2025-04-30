package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * WBS 实体条目抽检比例视图。
 */
@Entity
@Table(name = "wbs_entity_proportion")
public class WBSEntityProportion extends BaseEntity {

    private static final long serialVersionUID = 6036665868507851797L;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "区域（模块）ID")
    @Column
    private String sector;

    @Schema(description = "工序阶段名称")
    @Column
    private String stage;

    @Schema(description = "工序名称")
    @Column
    private String process;

    @Schema(description = "实体类型")
    @Column

    private String entityType;

    @Schema(description = "实体子类型")
    @Column
    private Integer proportion;

    @Schema(description = "实体工序计划条目总数")
    @Column
    private Integer totalCount;

    @Schema(description = "已抽取的实体工序计划条目数")
    @Column
    private Integer activeCount;

    /**
     * 取得仍需抽取的数量。
     *
     * @return 仍需抽取的数量
     */
    public int remaining() {
        return Double.valueOf(Math.ceil(totalCount * proportion / 100.0)).intValue() - activeCount;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

}
