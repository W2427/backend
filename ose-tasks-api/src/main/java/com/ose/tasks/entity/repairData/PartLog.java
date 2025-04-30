package com.ose.tasks.entity.repairData;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 补充 数据 日志 。
 */
@Entity
@Table(
    name = "patch_data_log"
)
public class PartLog extends BaseEntity {


    private static final long serialVersionUID = -6532378901814918302L;
    @Schema(description = "partId")
    @Column
    private Long partId;

    @Schema(description = "entityId")
    @Column
    private Long entityId;

    @Schema(description = "actInstId")
    @Column
    private Long actInstId;

    @Schema(description = "processId")
    @Column
    private Long processId;

    @Schema(description = "version")
    @Column
    private Long version;

    @Schema(description = "taskDefKey")
    @Column
    private String taskDefKey;

    @Schema(description = "status")
    @Column
    private Boolean isCompleted;

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }
}
