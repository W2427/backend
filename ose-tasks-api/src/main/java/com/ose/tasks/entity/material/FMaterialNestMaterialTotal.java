package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "mat_f_material_nest_material_total")
public class FMaterialNestMaterialTotal extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "套料清单id")
    private Long fmnId;

    private String ident;

    private String tagNumber;

    private String spec;

    @Schema(description = "材质")
    private String texture;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    private String unitCode;

    @Column(length = 500)
    private String shortDesc;

    @Column(precision = 19, scale = 3)
    private BigDecimal lockedTotalQty;

    @Column(precision = 19, scale = 3)
    private BigDecimal stockTotalQty;

    @Schema(description = "包含代用材料Flg")
    private Boolean substituteFlg;

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

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public BigDecimal getLockedTotalQty() {
        return lockedTotalQty;
    }

    public void setLockedTotalQty(BigDecimal lockedTotalQty) {
        this.lockedTotalQty = lockedTotalQty;
    }

    public BigDecimal getStockTotalQty() {
        return stockTotalQty;
    }

    public void setStockTotalQty(BigDecimal stockTotalQty) {
        this.stockTotalQty = stockTotalQty;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public Boolean getSubstituteFlg() {
        return substituteFlg;
    }

    public void setSubstituteFlg(Boolean substituteFlg) {
        this.substituteFlg = substituteFlg;
    }
}
