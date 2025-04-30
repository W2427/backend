package com.ose.tasks.dto;


import com.ose.entity.BaseEntity;

import jakarta.persistence.MappedSuperclass;

/**
 * BPMN 基础DTO 包含 category
 */
@MappedSuperclass
public abstract class BpmnBaseDTO extends BaseEntity {


    private static final long serialVersionUID = -8788408883302929240L;
    //userTaskName
    private String nodeName;

    //userTaskId usertask3
    private String nodeId;

    //USERTASK, STARTEVENT, ENDEVENT, GATEWAY
    private String nodeType;

    private String taskType;

    //processStage:process:entityType:entitySubType
    private String category;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}
