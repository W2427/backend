package com.ose.tasks.entity.bpm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

/**
 * 工作流节点权限
 */
@Entity
@Table(name = "bpm_activity_task_node_privilege",
indexes = {
    @Index(columnList = "org_id, project_id, process_id, task_def_key")
})
public class BpmActivityTaskNodePrivilege extends BaseBizEntity {

    private static final long serialVersionUID = -5705592037581554410L;

    //项目id
    @Column(name = "project_id")
    private Long projectId;

    //组织id
    @Column(name = "org_id")
    private Long orgId;

    //工序id
    @Column(name = "process_id")
    private Long processId;

    // 节点Category
    @Column(name = "category")
    private String category;

    // 任务定义key
    @Column(name = "task_def_key")
    private String taskDefKey;

    // 任务名称
    @Column(name = "task_name")
    private String taskName;


    private String subCategory;

    //备注
    private String memo;

    //操作人id
    private Long assignee;

    //teamid
    private Long teamId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

}
