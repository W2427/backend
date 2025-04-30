package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * WBS 添加的日志 。
 */
@Entity
@Table(
    name = "wbs_entry_add_log"
)
public class WBSEntryAddLog extends BaseEntity {


    private static final long serialVersionUID = -7152398215878924390L;
    @Schema(description = "Batch Task ID")
    @Column
    private Long batchTaskId;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "project Node Id")
    @Column
    private Long projectNodeId;

    @Schema(description = "work WbsEntry Id")
    @Column
    private Long workWbsEntryId;

    public Long getBatchTaskId() {
        return batchTaskId;
    }

    public void setBatchTaskId(Long batchTaskId) {
        this.batchTaskId = batchTaskId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public Long getWorkWbsEntryId() {
        return workWbsEntryId;
    }

    public void setWorkWbsEntryId(Long workWbsEntryId) {
        this.workWbsEntryId = workWbsEntryId;
    }
}
