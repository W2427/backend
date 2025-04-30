package com.ose.tasks.dto.timesheet;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class SelectDataDTO extends BaseDTO {
    private static final long serialVersionUID = -1410097829515964536L;

    @Schema(description = "drawingId")
    private Long drawingId;

    @Schema(description = "drawingNo")
    private String drawingNo;

    @Schema(description = "stage")
    private String stage;

    @Schema(description = "stageId")
    private Long stageId;

    @Schema(description = "process")
    private String process;

    @Schema(description = "processId")
    private Long processId;

    @Schema(description = "version")
    private String version;

    @Schema(description = "task")
    private String task;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }
}
