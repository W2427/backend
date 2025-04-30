package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseEntity;

import jakarta.persistence.*;

/**
 * WBS 实体工序条目数据。
 */
@Entity
@Table(name = "wbs_entry")
public class WBSEntityEntry extends BaseEntity {

    private static final long serialVersionUID = 2395923285332541923L;

    @Column
    private Long projectId;

    @Column
    private String sector;

    @Column
    private String stage;

    @Column
    private String process;

    @Column

    private String entityType;

    @Column
    private Integer proportion;

    @Column
    private Integer randomNo;

    @Column
    private Boolean active;

    @Column
    private Boolean deleted;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    public Integer getRandomNo() {
        return randomNo;
    }

    public void setRandomNo(Integer randomNo) {
        this.randomNo = randomNo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
