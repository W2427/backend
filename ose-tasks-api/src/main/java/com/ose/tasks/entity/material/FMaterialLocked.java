package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 材料锁定实体
 */
@Entity
@Table(name = "mat_f_material_locked")
public class FMaterialLocked extends BaseVersionedBizEntity {
    private static final long serialVersionUID = -7402324286184951472L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "套料清单id")
    private Long fmnId;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "材料NPS")
    private Double nps;

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

    public Long getFmnId() {
        return fmnId;
    }

    public void setFmnId(Long fmnId) {
        this.fmnId = fmnId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }
}
