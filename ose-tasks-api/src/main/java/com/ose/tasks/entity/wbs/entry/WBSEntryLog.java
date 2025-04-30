package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseEntity;
import com.ose.tasks.vo.bpm.ActivityExecuteResult;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * WBS 添加的 WbsEntry日志 。
 */
@Entity
@Table(
    name = "wbs_entry_log"
)
public class WBSEntryLog extends BaseEntity {


    private static final long serialVersionUID = 7407398239476552696L;
    @Schema(description = "Batch Task ID")
    @Column
    private Long batchTaskId;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "wbs entry Id")
    @Column
    private Long wbsEntryId;

    @Schema(description = "wbs entry result")
    @Column
    @Enumerated(EnumType.STRING)
    private ActivityExecuteResult result;

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

    public Long getWbsEntryId() {
        return wbsEntryId;
    }

    public void setWbsEntryId(Long wbsEntryId) {
        this.wbsEntryId = wbsEntryId;
    }

    public ActivityExecuteResult getResult() {
        return result;
    }

    public void setResult(ActivityExecuteResult result) {
        this.result = result;
    }
}
