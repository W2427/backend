package com.ose.tasks.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;

public class ItpCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -1116050698038875115L;

    @Schema(description = "实体类型")
    @Column
    private String wbsEntityType;

    @Schema(description = "工序阶段")
    private Long processStageId;

    @Schema(description = "工序")
    private Long processId;

    public String getEntityType() {
        return wbsEntityType;
    }

    public void setWbsEntityType(String wbsEntityType) {
        this.wbsEntityType = wbsEntityType;
    }

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
}
