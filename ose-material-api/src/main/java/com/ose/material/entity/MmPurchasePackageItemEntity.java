package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;


/**
 * 采购包明细
 */
@Entity
@Table(name = "mm_purchase_package_item")
public class MmPurchasePackageItemEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 7964230468003303531L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "公司ID")
    @Column
    public Long companyId;

    @Schema(description = "采购包ID")
    @Column
    public Long purchasePackageId;

    @Schema(description = "采购包编号")
    @Column
    public String purchasePackageNo;

    @Schema(description = "Ident码")
    @Column
    private String identCode;

    @Schema(description = "类型（一类一码，一物一码）")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private QrCodeType qrCodeType;

    @Schema(description = "材料描述")
    @Column
    private String mmMaterialCodeDescription;

    @Schema(description = "材料编码-导入")
    @Column
    private String mmMaterialCodeNo;

    @Schema(description = "规格描述-导入")
    @Column
    private String specDescription;

    @Schema(description = "采购总量-导入")
    @Column
    private Double totalQty=0.0;

    @Schema(description = "已采购总量-导入")
    @Column
    private Double buyTotalQty=0.0;

    @Schema(description = "计量单位-导入")
    @Column
    private String designUnit;

    @Schema(description = "单件规格量-导入")
    @Column
    private Double specValue=0.0;;

    @Schema(description = "件数-导入")
    @Column
    private Integer pieceQty=0;

    @Schema(description = "已采购件数-导入")
    @Column
    private Integer buyPieceQty=0;

    @Schema(description = "备注-导入")
    @Column
    private String remarks;

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

    public Long getPurchasePackageId() {
        return purchasePackageId;
    }

    public void setPurchasePackageId(Long purchasePackageId) {
        this.purchasePackageId = purchasePackageId;
    }

    public String getPurchasePackageNo() {
        return purchasePackageNo;
    }

    public void setPurchasePackageNo(String purchasePackageNo) {
        this.purchasePackageNo = purchasePackageNo;
    }

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public QrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(QrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public String getMmMaterialCodeDescription() {
        return mmMaterialCodeDescription;
    }

    public void setMmMaterialCodeDescription(String mmMaterialCodeDescription) {
        this.mmMaterialCodeDescription = mmMaterialCodeDescription;
    }

    public String getMmMaterialCodeNo() {
        return mmMaterialCodeNo;
    }

    public void setMmMaterialCodeNo(String mmMaterialCodeNo) {
        this.mmMaterialCodeNo = mmMaterialCodeNo;
    }

    public String getSpecDescription() {
        return specDescription;
    }

    public void setSpecDescription(String specDescription) {
        this.specDescription = specDescription;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public String getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
    }

    public Double getSpecValue() {
        return specValue;
    }

    public void setSpecValue(Double specValue) {
        this.specValue = specValue;
    }

    public Integer getPieceQty() {
        return pieceQty;
    }

    public void setPieceQty(Integer pieceQty) {
        this.pieceQty = pieceQty;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Double getBuyTotalQty() {
        return buyTotalQty;
    }

    public void setBuyTotalQty(Double buyTotalQty) {
        this.buyTotalQty = buyTotalQty;
    }

    public Integer getBuyPieceQty() {
        return buyPieceQty;
    }

    public void setBuyPieceQty(Integer buyPieceQty) {
        this.buyPieceQty = buyPieceQty;
    }
}
