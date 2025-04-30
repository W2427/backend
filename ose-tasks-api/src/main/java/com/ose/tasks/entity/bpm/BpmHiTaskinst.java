package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseEntity;
import com.ose.vo.BpmTaskType;

import jakarta.persistence.*;
import java.util.Date;


/**
 * The persistent class for the act_hi_taskinst database table.
 */
@Entity
@Table(name = "bpm_hi_taskinst"
    ,
    indexes = {
        @Index(columnList = "act_inst_id,END_TIME_"),
        @Index(columnList = "task_def_key_"),
        @Index(columnList = "TENANT_ID_,ASSIGNEE_"),
        @Index(columnList = "TENANT_ID_,OWNER_"),
        @Index(columnList = "task_id_")
    })
public class BpmHiTaskinst extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "ASSIGNEE_")
    private String assignee;

    @Column(name = "CATEGORY_")
    private String category;

    @Column(name = "DESCRIPTION_")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME_")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME_")
    private Date endTime;

    @Column(name = "NAME_")
    private String name;

    @Column(name = "OWNER_")
    private String owner;

    @Column(name = "PARENT_TASK_ID_")
    private Long parentTaskId;

    @Column(name = "PROC_DEF_ID_")
    private String procDefId;

    @Column(name = "act_inst_id")
    private Long actInstId;

    @Column(name = "TASK_DEF_KEY_")
    private String taskDefKey;

    @Column(name = "TENANT_ID_", length = 20)
    private String tenantId;

    @Column(name = "TASK_ID_")
    private Long taskId;

    @Column(name = "operator", length = 20)
    private String operator;

    @Column(name = "task_type")
    private String taskType;

    @Column(name = "code")
    private String code;

    @Column
    private int seq;

    public BpmHiTaskinst() {
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        try {
            BpmTaskType.valueOf(taskType);
            this.taskType = taskType;
        } catch (Exception e) {
            this.taskType = null;
        }
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
