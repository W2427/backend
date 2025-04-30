package com.ose.materialspm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.ose.entity.BaseEntity;

/**
 * 工序实体类。
 */
@Entity
@Table(name = "demo")
public class Demo extends BaseEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //项目id
    @Column(name = "project_id", nullable = false, length = 16)
    private Long projectId;

    //组织id
    @Column(name = "org_id", nullable = false, length = 16)
    private Long orgId;

    // 工序名称
    @Column(name = "name", nullable = false, length = 128)
    @NotNull(message = "Demo's name is required")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
