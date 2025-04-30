package com.ose.tasks.entity.bpm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

/**
 * 工序阶段 实体类。
 */
@Entity
@Table(name = "bpm_process_category")
public class BpmProcessCategory extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -8635813266138087045L;

    //项目id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    //组织id
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 名称
    private String nameCn;

    // 名称
    private String nameEn;

    // 备注
    @Column(length = 500)
    private String memo;

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

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

}
