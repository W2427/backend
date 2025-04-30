package com.ose.dto.jpql;

import com.ose.vo.InspectResult;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据实体版本号 JPQL 数据传输对象。
 */
public class TaskProcQLDTO extends BaseDTO {


    private static final long serialVersionUID = 5473913082470214884L;
    @Schema(description = "Task ID")
    private Long taskId;

    @Schema(description = "ActInstId ID")
    private Long actInstId;

    @Schema(description = "InspectResult")
    private InspectResult inspectResult;

    public TaskProcQLDTO(Long taskId, Long actInstId, InspectResult inspectResult) {
        this.taskId = taskId;
        this.actInstId = actInstId;
        this.inspectResult = inspectResult;
    }

    public TaskProcQLDTO() {
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public InspectResult getInspectResult() {
        return inspectResult;
    }

    public void setInspectResult(InspectResult inspectResult) {
        this.inspectResult = inspectResult;
    }
}
