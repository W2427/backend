package com.ose.tasks.dto.drawing;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.bpm.BpmProcess;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DrawingCreateTaskInfoDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "实体类型名称")
    private String entitySubType;

    @Schema(description = "实体类型id")
    private Long entitySubTypeId;

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "图纸名称")
    private String drawingTitle;

    @Schema(description = "图纸名称")
    private String documentTitle;

    @Schema(description = "工序名称")
    private String process;

    @Schema(description = "工序id")
    private Long processId;

    @Schema(description = "工序阶段名称")
    private String processStage;

    @Schema(description = "工序阶段id")
    private Long processStageId;

    @Schema(description = "图纸新建流程版本")
    private String version;

    @Schema(description = "图纸最新有效版")
    private String latestApprovedRev;

    @Schema(description = "流程信息")
    private List<BpmProcess> processes;

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<BpmProcess> getProcesses() {
        return processes;
    }

    public void setProcesses(List<BpmProcess> processes) {
        this.processes = processes;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
    }

    public String getLatestApprovedRev() {
        return latestApprovedRev;
    }

    public void setLatestApprovedRev(String latestApprovedRev) {
        this.latestApprovedRev = latestApprovedRev;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }
}
