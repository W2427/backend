package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;

/**
 * 任务查询条件数据传输对象类。
 */
public class ShiftLogDTO extends PageDTO {

    private Long baiId;

    private Long entityId;

    private String entityNo;

    private String displayName;

    private Long processId;

    private String process;

    private String processStage;

    private String taskNode;

    private Long currentExecutorId;

    private String currentExecutorName;

    private Long shiftPersonId;

    private String shiftPersonName;

    public Long getBaiId() {
        return baiId;
    }

    public void setBaiId(Long baiId) {
        this.baiId = baiId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(String taskNode) {
        this.taskNode = taskNode;
    }

    public Long getCurrentExecutorId() {
        return currentExecutorId;
    }

    public void setCurrentExecutorId(Long currentExecutorId) {
        this.currentExecutorId = currentExecutorId;
    }

    public String getCurrentExecutorName() {
        return currentExecutorName;
    }

    public void setCurrentExecutorName(String currentExecutorName) {
        this.currentExecutorName = currentExecutorName;
    }

    public Long getShiftPersonId() {
        return shiftPersonId;
    }

    public void setShiftPersonId(Long shiftPersonId) {
        this.shiftPersonId = shiftPersonId;
    }

    public String getShiftPersonName() {
        return shiftPersonName;
    }

    public void setShiftPersonName(String shiftPersonName) {
        this.shiftPersonName = shiftPersonName;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
