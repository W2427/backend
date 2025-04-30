package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.WeldMaterialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "weld_material")
public class WeldMaterial extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 2939756950041542093L;

    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    @Schema(description = "批次号")
    @Column
    private String batchNo;

    @Schema(description = "焊材类型")
    @Enumerated(EnumType.STRING)
    private WeldMaterialType weldMaterialType;

    @Schema(description = "焊剂")
    @Column
    private String flux;

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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public WeldMaterialType getWeldMaterialType() {
        return weldMaterialType;
    }

    public void setWeldMaterialType(WeldMaterialType weldMaterialType) {
        this.weldMaterialType = weldMaterialType;
    }

    public String getFlux() {
        return flux;
    }

    public void setFlux(String flux) {
        this.flux = flux;
    }
}
