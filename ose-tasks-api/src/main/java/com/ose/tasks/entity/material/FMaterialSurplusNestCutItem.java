package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "mat_f_material_surplus_nest_cut_item")
public class FMaterialSurplusNestCutItem extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "余料套料清单id")
    private Long fMaterialSurplusNestId;

    @Schema(description = "余料切割合计表id")
    private Long fMaterialSurplusCutTotalId;

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
    private Long fMaterialSurplusNestItemId;

    @Schema(description = "余料领料单明细ID")
    private Long fMaterialSurplusReceiptsNodeItemId;

    @Schema(description = "余料领料单项目节点ID")
    private Long fMaterialSurplusReceiptsNodeId;

    @Schema(description = "材料准备单项目节点")
    @JsonProperty(value = "fMaterialSurplusReceiptsNodeId")
    public ReferenceData getFMaterialSurplusReceiptsNodeIdRef() {
        return this.fMaterialSurplusReceiptsNodeId == null
            ? null
            : new ReferenceData(this.fMaterialSurplusReceiptsNodeId);
    }

    @Schema(description = "材料准备单ITEM节点")
    @JsonProperty(value = "fMaterialSurplusReceiptsNodeItemId")
    public ReferenceData getFMaterialSurplusReceiptsNodeItemIdRef() {
        return this.fMaterialSurplusReceiptsNodeItemId == null
            ? null
            : new ReferenceData(this.fMaterialSurplusReceiptsNodeItemId);
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

    public Long getfMaterialSurplusCutTotalId() {
        return fMaterialSurplusCutTotalId;
    }

    public void setfMaterialSurplusCutTotalId(Long fMaterialSurplusCutTotalId) {
        this.fMaterialSurplusCutTotalId = fMaterialSurplusCutTotalId;
    }

    public Long getfMaterialSurplusNestId() {
        return fMaterialSurplusNestId;
    }

    public void setfMaterialSurplusNestId(Long fMaterialSurplusNestId) {
        this.fMaterialSurplusNestId = fMaterialSurplusNestId;
    }

    public Long getfMaterialSurplusNestItemId() {
        return fMaterialSurplusNestItemId;
    }

    public void setfMaterialSurplusNestItemId(Long fMaterialSurplusNestItemId) {
        this.fMaterialSurplusNestItemId = fMaterialSurplusNestItemId;
    }

    public Long getfMaterialSurplusReceiptsNodeItemId() {
        return fMaterialSurplusReceiptsNodeItemId;
    }

    public void setfMaterialSurplusReceiptsNodeItemId(Long fMaterialSurplusReceiptsNodeItemId) {
        this.fMaterialSurplusReceiptsNodeItemId = fMaterialSurplusReceiptsNodeItemId;
    }

    public Long getfMaterialSurplusReceiptsNodeId() {
        return fMaterialSurplusReceiptsNodeId;
    }

    public void setfMaterialSurplusReceiptsNodeId(Long fMaterialSurplusReceiptsNodeId) {
        this.fMaterialSurplusReceiptsNodeId = fMaterialSurplusReceiptsNodeId;
    }
}
