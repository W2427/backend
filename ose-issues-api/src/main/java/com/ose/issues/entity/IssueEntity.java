package com.ose.issues.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "issue_entities")
public class IssueEntity extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    private Long projectId;

    private String entityNo;

    private Long issueId;

    private String area;

    private String layer;

    private String pressureTestPackage;

    private String cleanPackage;

    private String subSystem;

    private Long department;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getPressureTestPackage() {
        return pressureTestPackage;
    }

    public void setPressureTestPackage(String pressureTestPackage) {
        this.pressureTestPackage = pressureTestPackage;
    }

    public String getCleanPackage() {
        return cleanPackage;
    }

    public void setCleanPackage(String cleanPackage) {
        this.cleanPackage = cleanPackage;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

}
