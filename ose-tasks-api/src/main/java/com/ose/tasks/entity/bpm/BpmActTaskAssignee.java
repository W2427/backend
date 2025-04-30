package com.ose.tasks.entity.bpm;

import jakarta.persistence.*;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_act_task_assignee"
    ,
    indexes = {
        @Index(columnList = "act_inst_id, task_def_key"),
        @Index(columnList = "act_inst_id, taskCategory")
    }
    )
public class BpmActTaskAssignee extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    //流程实例id
    @Column(name = "act_inst_id")
    private Long actInstId;

    // 任务节点id
    @Column(name = "task_def_key")
    private String taskDefKey;

    // 任务名称
    @Column(name = "task_name")
    private String taskName;

    // 班组ID
    @Column(name = "team_id")
    private Long teamId;

    //节点执行权限
    private String taskCategory;

    //权限名
    @Transient
    private String taskCategoryName;

    //担当人id
    @Column(name = "assignee")
    private Long assignee;

    //担当人名字
    @Column(name = "assignee_name")
    private String assigneeName;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "last_modified_name")
    private String lastModifiedName;

    // 排序
    @Column(name = "order_no")
    private int orderNo = 0;

    @Column
    private int seq = 0;//ru task seq 对应

    @Transient
    private boolean executed;

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    @Transient
    public boolean isExecuted() {
        return executed;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public String getTaskCategoryName() {
        return taskCategoryName;
    }

    public void setTaskCategoryName(String taskCategoryName) {
        this.taskCategoryName = taskCategoryName;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedName() {
        return lastModifiedName;
    }

    public void setLastModifiedName(String lastModifiedName) {
        this.lastModifiedName = lastModifiedName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
