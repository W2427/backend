package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table( name ="drawing_markup",
indexes = {
    @Index(columnList = "projectId,entityNo"),
    @Index(columnList = "projectId,dwgNo"),
    @Index(columnList = "projectId,parentNo")
})
public class DrawingMarkup extends BaseBizEntity {


    private static final long serialVersionUID = -6942048926301067102L;

    @Column
    private Long drawingId;

    @Column
    private Long projectId;

    @Column
    private String dwgNo;

    @Column
    private String revision;

    @Column
    private Integer pageNo;

    @Column
    private String parentNo;

    @Column
    private String entityNo;

    @Column
    private String displayedEntityNo;

    @Column
    private String description;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public String getDisplayedEntityNo() {
        return displayedEntityNo;
    }

    public void setDisplayedEntityNo(String displayedEntityNo) {
        this.displayedEntityNo = displayedEntityNo;
    }
}
