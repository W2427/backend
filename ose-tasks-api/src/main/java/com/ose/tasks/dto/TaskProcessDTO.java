package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class TaskProcessDTO extends BaseDTO {

    private static final long serialVersionUID = -4389885109210094599L;
    @Schema(description = "工序ID")
    private Long processId;

    @Schema(description = "工序名称")
    private String processName;

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
