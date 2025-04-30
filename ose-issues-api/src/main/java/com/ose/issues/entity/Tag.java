package com.ose.issues.entity;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "issue_tags")
public class Tag extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 7894469969790655015L;

    @Column
    private Long targetId;

    @Column
    private Long projectId;

    @Column(length = 1000)
    private String text;

    @Column
    private Long parentId;

    @Column(length = 1020)
    private String children;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }
}
