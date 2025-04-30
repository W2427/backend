package com.ose.tasks.entity.bpm;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.ose.auth.vo.ExecutorRole;
import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.util.StringUtils;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_act_task"
    ,
    indexes = {
        @Index(columnList = "act_inst_id"),
        @Index(columnList = "task_id")
    }
    )
public class BpmActTask extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    @Schema(description = "流程实例id")
    @Column(name = "act_inst_id", nullable = false, length = 20)
    private Long actInstId;

    //任务id
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "cost_hour")
    private Double costHour;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;

    @Column(name = "pictures", columnDefinition = "text")
    private String pictures;

    @Column(name = "attachments", columnDefinition = "text")
    private String attachments;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "operator_name", length = 64)
    private String operatorName;

    @Column(name = "documents", columnDefinition = "text")
    private String documents;

    private Long assigneeId;

    @Column(name = "task_type")
    private String taskType;

    @Column(name = "task_def_key")
    private String taskDefKey;

    @Column
    private Long projectId;

    @Column
    private String executors;

    @Column
    private String remark;

    @Schema(description = "执行者角色")
    @Column
    @Enumerated(EnumType.STRING)
    private ExecutorRole executorRole;

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    @JsonIgnore
    public void setJsonDocuments(List<ActReportDTO> documents) {
        if (documents != null) {
            this.documents = StringUtils.toJSON(documents);
        }
    }

    @JsonIgnore
    public void addJsonDocuments(ActReportDTO documents) {
        if (documents != null) {
            List<ActReportDTO> reportList = getJsonDocumentsReadOnly();
            for (int i = reportList.size() - 1; i >= 0; i--) {
                if (reportList.get(i).getReportQrCode().equals(documents.getReportQrCode())) {
                    reportList.remove(i);
                }
            }
            reportList.add(documents);
            this.documents = StringUtils.toJSON(reportList);
        }
    }

    @JsonProperty(value = "documents", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonDocumentsReadOnly() {
        if (documents != null && !"".equals(documents)) {
            return StringUtils.decode(documents, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonProperty(value = "attachments", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonAttachmentsReadOnly() {
        if (attachments != null && !"".equals(attachments)) {
            return StringUtils.decode(attachments, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Double getCostHour() {
        return costHour;
    }

    public void setCostHour(Double costHour) {
        this.costHour = costHour;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public ExecutorRole getExecutorRole() {
        return executorRole;
    }

    public void setExecutorRole(ExecutorRole executorRole) {
        this.executorRole = executorRole;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getExecutors() {
        return executors;
    }

    public void setExecutors(String executors) {
        this.executors = executors;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
