package com.ose.tasks.entity.trident;

import com.ose.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table( name ="entity_tkrm" )
public class EntityRoom extends BaseEntity {

    private static final long serialVersionUID = 4469915299932682234L;
    //project_id	room	area	section	areaName	level	description	type	f_group
    @Column
    private Long projectId;

    @Column
    private String room;

    @Column
    private String area;

    @Column
    private String section;

    @Column
    private String areaName;

    @Column
    private Integer level;

    @Column
    private String description;

    @Column
    private String type;

    @Column
    private String fGroup;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getfGroup() {
        return fGroup;
    }

    public void setfGroup(String fGroup) {
        this.fGroup = fGroup;
    }
}
