package com.ose.tasks.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class RatedTimeCriterionCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -906764073399200481L;

    @Schema(description = "工序阶段ID")
    private Long processStageId;

    @Schema(description = "工序ID")
    private Long processId;

    @Schema(description = "实体类型ID")
    private Long entitySubTypeId;

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }
}
