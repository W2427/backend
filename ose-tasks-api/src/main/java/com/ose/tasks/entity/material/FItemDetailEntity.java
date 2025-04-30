package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.material.FItemStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 材料主表详情实体类。
 */
@Entity
@Table(name = "mat_f_item_detail",
indexes = {
    @Index(columnList = "org_id,project_id,relationship_id,relationship_total_id"),
    @Index(columnList = "org_id,project_id,rn_qr_code,item_status"),
    @Index(columnList = "project_id,rn_qr_code,item_status")
})
public class FItemDetailEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    // 材料主表ID
    @Column(name = "f_item_id")
    private Long fItemId;

    // 材料单位ID
    @Column(name = "unit_id")
    private int unitId;

    // 材料单位
    @Column(name = "unit_code")
    private String unitCode;

    // 单位量
    @Column(name = "qty", precision = 19, scale = 3)
    private BigDecimal qty;

    // 数量
    @Column(name = "qty_cnt")
    private int qtyCnt;

    @Column(name = "relationship_id")
    private Long relationshipId;

    @Column(name = "relationship_total_id")
    private Long relationshipTotalId;

    @Column(name = "rn_qr_code_id")
    private Long rnQrCodeId;

    @Schema(description = "heat no id")
    @Column
    private Long heatNoId;

    @Column(name = "rn_qr_code")
    private String rnQrCode;

    // issue, recv
    @Column(name = "item_status")
    @Enumerated(EnumType.STRING)
    private FItemStatusType itemStatus;

    @Schema(description = "材料批次号")
    private String materialBatchNumber;

    @Schema(description = "仓库ID")
    private String whId;

    @Schema(description = "仓库")
    private String whCode;

    @Schema(description = "库位ID")
    private String locId;

    @Schema(description = "库位")
    private String locCode;

    @Schema(description = "货架-层号")
    private String goodsShelf;

    @Transient
    @Schema(description = "显示用单位长度")
    private String displayQty;

    @Transient
    @Schema(description = "显示用单位")
    private String displayQtyUnitCode;

    @Transient
    private int recvQtyCnt;

    @Transient
    private int issueQtyCnt;

//    @Transient
//    @Schema(description = "炉号")
//    private String heatNo;
//
//    @Transient
//    @Schema(description = "批号")
//    private String batchNo;


    public int getRecvQtyCnt() {
        return recvQtyCnt;
    }

    public void setRecvQtyCnt(int recvQtyCnt) {
        this.recvQtyCnt = recvQtyCnt;
    }

    public int getIssueQtyCnt() {
        return issueQtyCnt;
    }

    public void setIssueQtyCnt(int issueQtyCnt) {
        this.issueQtyCnt = issueQtyCnt;
    }

    public String getWhId() {
        return whId;
    }

    public void setWhId(String whId) {
        this.whId = whId;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getGoodsShelf() {
        return goodsShelf;
    }

    public void setGoodsShelf(String goodsShelf) {
        this.goodsShelf = goodsShelf;
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

    public Long getfItemId() {
        return fItemId;
    }

    public void setfItemId(Long fItemId) {
        this.fItemId = fItemId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Long getRnQrCodeId() {
        return rnQrCodeId;
    }

    public void setRnQrCodeId(Long rnQrCodeId) {
        this.rnQrCodeId = rnQrCodeId;
    }

    public FItemStatusType getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(FItemStatusType itemStatus) {
        this.itemStatus = itemStatus;
    }

    public int getQtyCnt() {
        return qtyCnt;
    }

    public void setQtyCnt(int qtyCnt) {
        this.qtyCnt = qtyCnt;
    }

    public String getRnQrCode() {
        return rnQrCode;
    }

    public void setRnQrCode(String rnQrCode) {
        this.rnQrCode = rnQrCode;
    }

    public Long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public Long getRelationshipTotalId() {
        return relationshipTotalId;
    }

    public void setRelationshipTotalId(Long relationshipTotalId) {
        this.relationshipTotalId = relationshipTotalId;
    }

    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
    }

    public String getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(String displayQty) {
        this.displayQty = displayQty;
    }

    public String getDisplayQtyUnitCode() {
        return displayQtyUnitCode;
    }

    public void setDisplayQtyUnitCode(String displayQtyUnitCode) {
        this.displayQtyUnitCode = displayQtyUnitCode;
    }

    @Schema(description = "材料主表实体类")
    @JsonProperty(value = "fItemId", access = READ_ONLY)
    public ReferenceData getFItemIdRef() {
        return this.fItemId == null
            ? null
            : new ReferenceData(this.fItemId);
    }

    @Schema(description = "（入库、出库、退库）报告体类")
    @JsonProperty(value = "relationshipId", access = READ_ONLY)
    public ReferenceData getRelationshipIdRef() {
        return this.relationshipId == null
            ? null
            : new ReferenceData(this.relationshipId);
    }

    public Long getHeatNoId() {
        return heatNoId;
    }

    public void setHeatNoId(Long heatNoId) {
        this.heatNoId = heatNoId;
    }
}
