package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 任务 状态表，存储状态信息
 */
@Entity
@Table(name = "bpm_activity_instance_shift_log")
public class BpmActivityInstanceShiftLog extends BaseEntity {

    private static final long serialVersionUID = -4427541441924325878L;
    //任务基础表id
    @Column
    private Long baiId;
    @Column
    private Long entityId;

    //项目id
    @Column
    private String entityNo;

    //项目id
    @Column
    private String displayName;

    //项目id
    @Column
    private String process;

    //项目id
    @Column
    private String processStage;

    @Column
    private String taskNode;

    @Schema(description = "移交时间")
    @Column(name = "shift_date")
    private Date shiftDate;

    @Column
    private Long currentExecutorId;

    @Column
    private String currentExecutorName;

    @Column
    private Long shiftPersonId;

    @Column
    private String shiftPersonName;

    public Long getBaiId() {
        return baiId;
    }

    public void setBaiId(Long baiId) {
        this.baiId = baiId;
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

    public Date getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
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
