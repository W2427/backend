package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.tasks.vo.setting.BatchTaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 批处理任务执行记录查询条件数据传输对象。
 */
public class BatchTaskCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -1110200869737485559L;

    @Schema(description = "批处理任务代码")
    private BatchTaskCode[] code;

    @Schema(description = "批处理任务执行状态")
    private BatchTaskStatus status;

    @Schema(description = "批处理任务启动者 ID")
    private Long launchedBy;

    @Schema(description = "实体ID")
    private Long entityId;

    public BatchTaskCode[] getCode() {
        return code;
    }

    public void setCode(BatchTaskCode[] code) {
        this.code = code;
    }

    public BatchTaskStatus getStatus() {
        return status;
    }

    public void setStatus(BatchTaskStatus status) {
        this.status = status;
    }

    public Long getLaunchedBy() {
        return launchedBy;
    }

    public void setLaunchedBy(Long launchedBy) {
        this.launchedBy = launchedBy;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
