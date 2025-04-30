package com.ose.tasks.entity.taskpackage;

import com.ose.auth.vo.UserPrivilege;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 任务包工序权限分配。
 */
@Entity
@Table(
    name = "task_package_assign_node_privilege"
)
public class TaskPackageAssignNodePrivileges extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -9191442268738128672L;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "所属项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "任务包 ID")
    @Column(nullable = false)
    private Long taskPackageId;

    @Schema(description = "工序ID")
    @Column(nullable = false)
    private Long processId;

    @Schema(description = "担当者权限")
    @Enumerated(EnumType.STRING)
    private UserPrivilege privilege;

    @Schema(description = "工作组 ID")
    private Long teamId;

    @Schema(description = "用户 ID")
    private Long userId;

    private String memo;

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

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public UserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getMemo() {
        return memo;
    }


    public void setMemo(String memo) {
        this.memo = memo;
    }
}
