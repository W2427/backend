package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 出库单明细表。
 */
@Entity
@Table(name = "mm_issue_detail",
    indexes = {
        @Index(columnList = "orgId,projectId,mmIssueId,status"),
        @Index(columnList = "orgId,projectId,mmIssueId,mmMaterialCodeNo,specValue,status"),
        @Index(columnList = "orgId,projectId,id,status")
    })
public class MmIssueDetailEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6891960876057551544L;

    @Schema(description ="组织ID")
    @Column
    public Long orgId;

    @Schema(description ="项目ID")
    @Column
    public Long projectId;

    @Schema(description ="出库单ID")
    @Column
    public Long mmIssueId;

    @Schema(description ="Ident码")
    @Column
    private String identCode;

    @Schema(description ="材料编码")
    @Column
    private String mmMaterialCodeNo;

    @Schema(description ="材料描述")
    @Column
    private String mmMaterialCodeDescription;

    @Schema(description ="材料描述2")
    @Column
    private String specName;

    @Schema(description ="材质")
    @Column
    private String specQuality;

    @Schema(description ="计量单位")
    @Column
    private String designUnit;

    @Schema(description ="出库总量数量")
    @Column
    private Double issuedTotalQty = 0.0;

    @Schema(description ="计划出库总量数量")
    @Column
    private Double planIssuedTotalQty = 0.0;

    @Schema(description ="出库总件数")
    @Column
    private Integer issuedPieceQty = 0;

    @Schema(description ="计划出库总件数")
    @Column
    private Integer planIssuedPieceQty = 0;

    @Schema(description ="类型（一类一码，一物一码）")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private QrCodeType qrCodeType;

    @Schema(description ="规格量")
    @Column
    private Double specValue;

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

    public Long getMmIssueId() {
        return mmIssueId;
    }

    public void setMmIssueId(Long mmIssueId) {
        this.mmIssueId = mmIssueId;
    }

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public String getMmMaterialCodeNo() {
        return mmMaterialCodeNo;
    }

    public void setMmMaterialCodeNo(String mmMaterialCodeNo) {
        this.mmMaterialCodeNo = mmMaterialCodeNo;
    }

    public String getMmMaterialCodeDescription() {
        return mmMaterialCodeDescription;
    }

    public void setMmMaterialCodeDescription(String mmMaterialCodeDescription) {
        this.mmMaterialCodeDescription = mmMaterialCodeDescription;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecQuality() {
        return specQuality;
    }

    public void setSpecQuality(String specQuality) {
        this.specQuality = specQuality;
    }

    public String getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
    }

    public Double getIssuedTotalQty() {
        return issuedTotalQty;
    }

    public void setIssuedTotalQty(Double issuedTotalQty) {
        this.issuedTotalQty = issuedTotalQty;
    }

    public Double getPlanIssuedTotalQty() {
        return planIssuedTotalQty;
    }

    public void setPlanIssuedTotalQty(Double planIssuedTotalQty) {
        this.planIssuedTotalQty = planIssuedTotalQty;
    }

    public Integer getIssuedPieceQty() {
        return issuedPieceQty;
    }

    public void setIssuedPieceQty(Integer issuedPieceQty) {
        this.issuedPieceQty = issuedPieceQty;
    }

    public Integer getPlanIssuedPieceQty() {
        return planIssuedPieceQty;
    }

    public void setPlanIssuedPieceQty(Integer planIssuedPieceQty) {
        this.planIssuedPieceQty = planIssuedPieceQty;
    }

    public QrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(QrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public Double getSpecValue() {
        return specValue;
    }

    public void setSpecValue(Double specValue) {
        this.specValue = specValue;
    }
}
