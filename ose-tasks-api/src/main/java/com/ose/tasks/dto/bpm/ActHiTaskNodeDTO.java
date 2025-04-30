package com.ose.tasks.dto.bpm;

import java.util.Date;
import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.ActReportDTO;

public class ActHiTaskNodeDTO extends BaseDTO {

    private static final long serialVersionUID = 8194574818310660466L;

    private Long taskId;

    private String taskDefKey;

    private String taskType;

    private String taskName;

    private String assignee;

    private String assigneeName;

    private Date startTime;

    private Date endTime;

    private Double hour;

    private String comment;

    private List<ActReportDTO> attachments;

    private List<ActReportDTO> pictures;

    private List<ActReportDTO> documents;

    private Long parentTaskId;

    private String remark;//类型，REVOCATIOn 撤回 标注在这里

    private boolean revocation = false;

    private String code;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Double getHour() {
        return hour;
    }

    public void setHour(Double hour) {
        this.hour = hour;
    }

    public List<ActReportDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ActReportDTO> attachments) {
        this.attachments = attachments;
    }

    public List<ActReportDTO> getPictures() {
        return pictures;
    }

    public void setPictures(List<ActReportDTO> pictures) {
        this.pictures = pictures;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public boolean isRevocation() {
        return revocation;
    }

    public void setRevocation(boolean revocation) {
        this.revocation = revocation;
    }

    public List<ActReportDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ActReportDTO> documents) {
        this.documents = documents;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
