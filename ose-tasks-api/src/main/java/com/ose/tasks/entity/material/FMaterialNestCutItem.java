package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "mat_f_material_nest_cut_item")
public class FMaterialNestCutItem extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "套料清单id")
    private Long fmnId;

    @Schema(description = "切割合计表id")
    private Long fmnCutTotalId;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "长度")
    @Column(precision = 19, scale = 3)
    private BigDecimal length;

    @Schema(description = "材料描述")
    private String materialDesc;

    @Schema(description = "管段实体ID")
    private Long pipePieceId;

    @Schema(description = "材料节点编号")
    private String materialNo;

    @Schema(description = "套料结果明细ID")
    private Long fMaterialNestMaterialItemId;

    @Schema(description = "材料准备单明细ID")
    private Long fMaterialPrepareItemId;

    @Schema(description = "材料准备单项目节点ID")
    private Long fMaterialPrepareNodeId;

    @Schema(description = "材料准备单项目节点")
    @JsonProperty(value = "fMaterialPrepareNodeId")
    public ReferenceData getFMaterialPrepareNodeIdRef() {
        return this.fMaterialPrepareNodeId == null
            ? null
            : new ReferenceData(this.fMaterialPrepareNodeId);
    }

    @Schema(description = "材料准备单ITEM节点")
    @JsonProperty(value = "fMaterialPrepareItemId")
    public ReferenceData getFMaterialPrepareItemIdRef() {
        return this.fMaterialPrepareItemId == null
            ? null
            : new ReferenceData(this.fMaterialPrepareItemId);
    }

    public Long getfMaterialPrepareItemId() {
        return fMaterialPrepareItemId;
    }

    public void setfMaterialPrepareItemId(Long fMaterialPrepareItemId) {
        this.fMaterialPrepareItemId = fMaterialPrepareItemId;
    }

    public Long getfMaterialPrepareNodeId() {
        return fMaterialPrepareNodeId;
    }

    public void setfMaterialPrepareNodeId(Long fMaterialPrepareNodeId) {
        this.fMaterialPrepareNodeId = fMaterialPrepareNodeId;
    }

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

    public Long getFmnCutTotalId() {
        return fmnCutTotalId;
    }

    public void setFmnCutTotalId(Long fmnCutTotalId) {
        this.fmnCutTotalId = fmnCutTotalId;
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

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public Long getPipePieceId() {
        return pipePieceId;
    }

    public void setPipePieceId(Long pipePieceId) {
        this.pipePieceId = pipePieceId;
    }

    public Long getfMaterialNestMaterialItemId() {
        return fMaterialNestMaterialItemId;
    }

    public void setfMaterialNestMaterialItemId(Long fMaterialNestMaterialItemId) {
        this.fMaterialNestMaterialItemId = fMaterialNestMaterialItemId;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
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
}
