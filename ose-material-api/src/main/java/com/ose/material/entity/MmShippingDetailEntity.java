package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * 采购单详情。
 */
@Entity
@Table(name = "mm_shipping_detail",
    indexes = {
        @Index(columnList = "orgId,projectId,deleted")
    })
public class MmShippingDetailEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -9071780040132411515L;
    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "请购单ID")
    @Column
    private Long companyId;

    @Schema(description = "发货单ID")
    @Column
    private Long shippingId;

    @Schema(description = "采购包ID")
    @Column
    private Long purchasePackageId;

    @Schema(description = "采购包编号")
    @Column
    private String purchasePackageNo;

    @Schema(description = "ident码")
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

    @Schema(description = "件号-导入")
    @Column
    private String pieceTagNo;

    @Schema(description = "炉批号-导入")
    @Column
    private String heatBatchNo;

    @Schema(description = "材料证书编码-导入")
    @Column
    public String materialCertificate;

    @Schema(description = "到货总量-导入")
    @Column
    private Double totalQty=0.0;

    @Schema(description = "发货总量-导入")
    @Column
    private Double shippedTotalQty=0.0;

    @Schema(description = "计量单位-导入")
    @Column
    private String designUnit;

    @Schema(description = "单件规格量-导入")
    @Column
    private Double specValue=0.0;

    @Schema(description = "件数-导入")
    @Column
    private Integer pieceQty=0;

    @Schema(description = "发货件数-导入")
    @Column
    private Integer shippedPieceQty=0;

    @Schema(description = "仓库ID")
    @Column
    public Long wareHouseLocationId;

    @Schema(description = "仓库名称")
    @Column
    public String wareHouseLocationName;

    @Schema(description = "车船号")
    @Column
    public String shippingNo;

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

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
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

    public String getPieceTagNo() {
        return pieceTagNo;
    }

    public void setPieceTagNo(String pieceTagNo) {
        this.pieceTagNo = pieceTagNo;
    }

    public String getHeatBatchNo() {
        return heatBatchNo;
    }

    public void setHeatBatchNo(String heatBatchNo) {
        this.heatBatchNo = heatBatchNo;
    }

    public String getMaterialCertificate() {
        return materialCertificate;
    }

    public void setMaterialCertificate(String materialCertificate) {
        this.materialCertificate = materialCertificate;
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

    public Long getWareHouseLocationId() {
        return wareHouseLocationId;
    }

    public void setWareHouseLocationId(Long wareHouseLocationId) {
        this.wareHouseLocationId = wareHouseLocationId;
    }

    public String getWareHouseLocationName() {
        return wareHouseLocationName;
    }

    public void setWareHouseLocationName(String wareHouseLocationName) {
        this.wareHouseLocationName = wareHouseLocationName;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Double getShippedTotalQty() {
        return shippedTotalQty;
    }

    public void setShippedTotalQty(Double shippedTotalQty) {
        this.shippedTotalQty = shippedTotalQty;
    }

    public Integer getShippedPieceQty() {
        return shippedPieceQty;
    }

    public void setShippedPieceQty(Integer shippedPieceQty) {
        this.shippedPieceQty = shippedPieceQty;
    }
}
