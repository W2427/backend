package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * WBS 条目前置任务统计信息。
 */
@Entity
@Table(name = "wbs_entry_predecessor_statistic")
@Cacheable(false)
public class WBSEntryPredecessorStatistics extends BaseEntity {

    private static final long serialVersionUID = 6217749757797919191L;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "后置任务实体类型")
    @Column
    private String entityType;

    @Schema(description = "前置任务实体类型")
    @Column
    private String predecessorEntityTypes;

    @Schema(description = "前置任务总数")
    @Column
    private int total;

    @Schema(description = "未完成的前置任务总数")
    @Column
    private int notFinished;

    @Schema(description = "未接受的前置任务总数")
    @Column
    private int notApproved;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getPredecessorEntityTypes() {
        return predecessorEntityTypes;
    }

    public void setPredecessorEntityTypes(String predecessorEntityTypes) {
        this.predecessorEntityTypes = predecessorEntityTypes;
    }

    public boolean isEntityTypeMatched() {
        return entityType != null && entityType.equals(predecessorEntityTypes);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNotFinished() {
        return notFinished;
    }

    public void setNotFinished(int notFinished) {
        this.notFinished = notFinished;
    }

    public int getNotApproved() {
        return notApproved;
    }

    public void setNotApproved(int notApproved) {
        this.notApproved = notApproved;
    }

}
