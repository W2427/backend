package com.ose.tasks.dto.process;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class EntityProcessDTO extends BaseDTO {

    private static final long serialVersionUID = 1856374783359522524L;

    @Schema(description = "工序阶段名称")
    private String stageName;

    @Schema(description = "工序名称")
    private String processName;

    @Schema(description = "计划条目数")
    private Long entryCount;

    public EntityProcessDTO() {
    }

    public EntityProcessDTO(String stageName, String processName) {
        this.setStageName(stageName);
        this.setProcessName(processName);
    }

    public EntityProcessDTO(String stageName, String processName, Long entryCount) {
        this(stageName, processName);
        this.setEntryCount(entryCount);
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Long getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(Long entryCount) {
        this.entryCount = entryCount;
    }

}
