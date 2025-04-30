package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "mat_f_material_nest_material_item")
public class FMaterialNestMaterialItem extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "套料清单id")
    private Long fmnId;

    private Long fmnmTotalId;

    private String tagNumber;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Column(name = "qty", precision = 19, scale = 3)
    private BigDecimal qty;

    @Schema(description = "余料长度")
    @Column(precision = 19, scale = 3)
    private BigDecimal surplusLength;

    @Schema(description = "废料长度")
    @Column(precision = 19, scale = 3)
    private BigDecimal scrapLength;

    @Schema(description = "现场余料Flg（余料库：1，仓库：0）")
    private Boolean surplusFlg;

    @Schema(description = "代用材料Flg")
    private Boolean substituteFlg;

    @Schema(description = "关联材料id")
    private Long relationshipId;

    @Schema(description = "关联材料明细id")
    private Long relationshipItemId;

    @Schema(description = "出库状态（已出库，未出库）")
    private Boolean issuedFlg;

    @Transient
    private List<FMaterialNestCutItem> fMaterialNestCutItemList;

    @Schema(description = "材料明细信息")
    @JsonProperty(value = "relationshipItemId")
    public ReferenceData getRelationshipItemIdRef() {
        return this.relationshipItemId == null
            ? null
            : new ReferenceData(this.relationshipItemId);
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Boolean getSubstituteFlg() {
        return substituteFlg;
    }

    public void setSubstituteFlg(Boolean substituteFlg) {
        this.substituteFlg = substituteFlg;
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

    public Long getFmnmTotalId() {
        return fmnmTotalId;
    }

    public void setFmnmTotalId(Long fmnmTotalId) {
        this.fmnmTotalId = fmnmTotalId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getSurplusLength() {
        return surplusLength;
    }

    public void setSurplusLength(BigDecimal surplusLength) {
        this.surplusLength = surplusLength;
    }

    public BigDecimal getScrapLength() {
        return scrapLength;
    }

    public void setScrapLength(BigDecimal scrapLength) {
        this.scrapLength = scrapLength;
    }

    public Boolean getSurplusFlg() {
        return surplusFlg;
    }

    public void setSurplusFlg(Boolean surplusFlg) {
        this.surplusFlg = surplusFlg;
    }

    public Long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public Long getRelationshipItemId() {
        return relationshipItemId;
    }

    public void setRelationshipItemId(Long relationshipItemId) {
        this.relationshipItemId = relationshipItemId;
    }

    public Boolean getIssuedFlg() {
        return issuedFlg;
    }

    public void setIssuedFlg(Boolean issuedFlg) {
        this.issuedFlg = issuedFlg;
    }

    public List<FMaterialNestCutItem> getfMaterialNestCutItemList() {
        return fMaterialNestCutItemList;
    }

    public void setfMaterialNestCutItemList(List<FMaterialNestCutItem> fMaterialNestCutItemList) {
        this.fMaterialNestCutItemList = fMaterialNestCutItemList;
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
}
