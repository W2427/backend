package com.ose.tasks.dto.bpm;

import java.util.List;
import java.util.Map;

import com.ose.dto.BaseDTO;

/**
 * 实体管理 数据传输对象
 */
public class TaskCompleteDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private Long orgId;

    private Long projectId;

    private List<String> attachFiles;

    private String comment;

    private List<String> pictures;

    private Map<String, Object> command;

    private String operator;

    private String taskType;

    private boolean checkNodeType;

    private Long parentTaskId;

    public List<String> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(List<String> attachFiles) {
        this.attachFiles = attachFiles;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Map<String, Object> getCommand() {
        return command;
    }

    public void setCommand(Map<String, Object> command) {
        this.command = command;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public boolean isCheckNodeType() {
        return checkNodeType;
    }

    public void setCheckNodeType(boolean checkNodeType) {
        this.checkNodeType = checkNodeType;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }
}
