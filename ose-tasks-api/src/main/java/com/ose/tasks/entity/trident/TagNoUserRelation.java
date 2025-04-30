package com.ose.tasks.entity.trident;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag_no_user_relation")
public class TagNoUserRelation extends BaseBizEntity {


    private static final long serialVersionUID = -7222842375673741724L;
    @Column
    private Long entityId;

    @Column //ENG CONST PROC
    private String type;

    @Column
    private Long userId;

    @Column
    private String userName;

    @Column
    private Long projectId;

    @Column
    private Long orgId;

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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
