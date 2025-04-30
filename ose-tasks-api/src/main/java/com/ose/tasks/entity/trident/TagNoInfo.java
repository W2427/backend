package com.ose.tasks.entity.trident;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag_no_info")
public class TagNoInfo extends BaseBizEntity {

    private static final long serialVersionUID = 7707773686462998409L;

    @Column
    private Long entityId;

    @Column
    private String memo;

    @Column //ENG CONST PROC, PRD, CMM, MTG INP
    private String type;

    @Column
    private Long operatorId;

    @Column
    private String operatorName;

    @Column
    private Long projectId;

    @Column
    private Long orgId;

    @Column
    private String discCode;

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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscCode() {
        return discCode;
    }

    public void setDiscCode(String discCode) {
        this.discCode = discCode;
    }
}
