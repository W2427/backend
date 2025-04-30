package com.ose.tasks.entity.workinghour;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.workinghour.ProjectWorkingHourCategoryType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * 项目工时分类实体类。
 */
@Entity
@Table(name = "wh_project_working_hour_category")
public class ProjectWorkingHourCategoryEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Enumerated(EnumType.STRING)
    private ProjectWorkingHourCategoryType projectWorkingHourCategoryType;

    private String name;

    private Long parentId;

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

    public ProjectWorkingHourCategoryType getProjectWorkingHourCategoryType() {
        return projectWorkingHourCategoryType;
    }

    public void setProjectWorkingHourCategoryType(ProjectWorkingHourCategoryType projectWorkingHourCategoryType) {
        this.projectWorkingHourCategoryType = projectWorkingHourCategoryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
