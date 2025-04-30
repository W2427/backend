package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 套料参数实体类
 */
@Entity
@Table(name = "mat_f_material_nest_parameter")
public class FMaterialNestParameter extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -307858779102541736L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "切割损耗量")
    private Double wastage;

    @Schema(description = "管线末端放弃量")
    private Double abandonment;

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

    public Double getWastage() {
        return wastage;
    }

    public void setWastage(Double wastage) {
        this.wastage = wastage;
    }

    public Double getAbandonment() {
        return abandonment;
    }

    public void setAbandonment(Double abandonment) {
        this.abandonment = abandonment;
    }
}
