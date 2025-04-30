package com.ose.tasks.entity.material;

import com.ose.entity.BaseBizEntity;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 材料准备单合计详情表
 */
@Entity
@Table(name = "mat_f_material_prepare_total")
public class FMaterialPrepareTotalEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "准备单ID")
    private Long mpId;

    @Schema(description = "物资编码")
    private String materialCode;

    @Schema(description = "材料规格，如2寸等")
    private String materialSpec;

    @Schema(description = "材料")
    private String material;

    @Schema(description = "单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit unitCode;

    @Schema(description = "数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal totalQty;

    @Schema(description = "准备数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal prepareTotalQty;

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

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialSpec() {
        return materialSpec;
    }

    public void setMaterialSpec(String materialSpec) {
        this.materialSpec = materialSpec;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public LengthUnit getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(LengthUnit unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public BigDecimal getPrepareTotalQty() {
        return prepareTotalQty;
    }

    public void setPrepareTotalQty(BigDecimal prepareTotalQty) {
        this.prepareTotalQty = prepareTotalQty;
    }
}
