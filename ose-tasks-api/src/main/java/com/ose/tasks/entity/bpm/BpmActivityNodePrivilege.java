package com.ose.tasks.entity.bpm;

import jakarta.persistence.*;

import com.ose.auth.vo.UserPrivilege;
import com.ose.entity.BaseBizEntity;

/**
 * 工作流节点权限人员指定
 */
@Entity
@Table(name = "bpm_activity_node_privilege")
public class BpmActivityNodePrivilege extends BaseBizEntity {

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

    //担当者权限
    @Column(name = "privilege")
    @Enumerated(EnumType.STRING)
    private UserPrivilege privilege;

    //备注
    private String memo;

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

    public UserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
    }

    @Transient
    public String getPrivilegeName() {
        return privilege == null ? null : privilege.getDisplayName();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
