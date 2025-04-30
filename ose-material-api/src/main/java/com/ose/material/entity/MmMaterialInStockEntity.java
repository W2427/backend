package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;


/**
 * 在库材料主表。
 */
@Entity
@Table(name = "mm_material_in_stock",
    indexes = {
        @Index(columnList = "orgId,projectId,deleted"),
        @Index(columnList = "orgId,projectId,mmMaterialCodeNo,status")
    })
public class MmMaterialInStockEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 7059130208384904215L;

    @Schema(description ="组织ID")
    @Column
    public Long orgId;

    @Schema(description ="项目ID")
    @Column
    public Long projectId;

    @Schema(description ="公司Id")
    @Column
    public Long companyId;

    @Schema(description ="ident码")
    @Column
    private String identCode;

    @Schema(description ="材料编码")
    @Column
    private String mmMaterialCodeNo;

    @Schema(description ="材料编码Id")
    @Column
    private Long mmMaterialCodeId;

    @Schema(description ="材料描述")
    @Column
    private String mmMaterialCodeDescription;

    @Schema(description ="类型（一类一码，一物一码）")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private QrCodeType qrCodeType;

    @Schema(description ="材料规格")
    @Column
    private String specName;

    @Schema(description ="材质")
    @Column
    private String specQuality;

    @Schema(description ="计量单位")
    @Column
    private String designUnit;

    @Schema(description ="在库总量数量")
    @Column
    private Double inStockTotalQty = 0.0;

    @Schema(description ="入库总量数量")
    @Column
    private Double receivedTotalQty = 0.0;

    @Schema(description ="出库总量数量")
    @Column
    private Double issuedTotalQty = 0.0;

    @Schema(description ="在库总件数")
    @Column
    private Integer inStockPieceQty = 0;

    @Schema(description ="入库总件数")
    @Column
    private Integer receivedPieceQty = 0;

    @Schema(description ="出库总件数")
    @Column
    private Integer issuedPieceQty = 0;

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public Long getMmMaterialCodeId() {
        return mmMaterialCodeId;
    }

    public void setMmMaterialCodeId(Long mmMaterialCodeId) {
        this.mmMaterialCodeId = mmMaterialCodeId;
    }

    public String getMmMaterialCodeDescription() {
        return mmMaterialCodeDescription;
    }

    public void setMmMaterialCodeDescription(String mmMaterialCodeDescription) {
        this.mmMaterialCodeDescription = mmMaterialCodeDescription;
    }

    public QrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(QrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
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

    public Double getInStockTotalQty() {
        return inStockTotalQty;
    }

    public void setInStockTotalQty(Double inStockTotalQty) {
        this.inStockTotalQty = inStockTotalQty;
    }

    public Double getReceivedTotalQty() {
        return receivedTotalQty;
    }

    public void setReceivedTotalQty(Double receivedTotalQty) {
        this.receivedTotalQty = receivedTotalQty;
    }

    public Double getIssuedTotalQty() {
        return issuedTotalQty;
    }

    public void setIssuedTotalQty(Double issuedTotalQty) {
        this.issuedTotalQty = issuedTotalQty;
    }

    public Integer getInStockPieceQty() {
        return inStockPieceQty;
    }

    public void setInStockPieceQty(Integer inStockPieceQty) {
        this.inStockPieceQty = inStockPieceQty;
    }

    public Integer getReceivedPieceQty() {
        return receivedPieceQty;
    }

    public void setReceivedPieceQty(Integer receivedPieceQty) {
        this.receivedPieceQty = receivedPieceQty;
    }

    public Integer getIssuedPieceQty() {
        return issuedPieceQty;
    }

    public void setIssuedPieceQty(Integer issuedPieceQty) {
        this.issuedPieceQty = issuedPieceQty;
    }
}
