package com.ose.tasks.entity.bpm;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_activity_category_config")
public class BpmActivityCategory extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //项目id
    private Long projectId;

    //组织id
    private Long orgId;

    // 名称
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
