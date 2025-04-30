package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * 材料入库单明细表。
 */
@Entity
@Table(name = "mm_release_receive_detail",
    indexes = {
        @Index(columnList = "orgId,projectId,deleted"),
        @Index(columnList = "orgId,projectId,releaseReceiveId,status"),
        @Index(columnList = "orgId,projectId,releaseReceiveId,id,status"),
        @Index(columnList = "orgId,projectId,pieceTagNo,status")
    })
public class MmReleaseReceiveDetailEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 4398346264024014186L;

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
    private Long purchasePackageId;

    @Schema(description = "采购包编号")
    @Column
    private String purchasePackageNo;

    @Schema(description = "发货单ID")
    @Column
    private Long shippingId;

    @Schema(description = "发货单编号")
    @Column
    private String shippingNumber;

    @Schema(description = "入库单ID")
    @Column
    public Long releaseReceiveId;

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

    @Schema(description = "入库总量")
    @Column
    private Double receiveTotalQty=0.0;

    @Schema(description = "内检合格总量")
    @Column
    private Double qualifiedTotalQty=0.0;

    @Schema(description = "外检合格总量")
    @Column
    private Double externalQualifiedTotalQty=0.0;

    @Schema(description = "计量单位-导入")
    @Column
    private String designUnit;

    @Schema(description = "单件规格量-导入")
    @Column
    private Double specValue=0.0;

    @Schema(description = "入库件数")
    @Column
    private Integer receivePieceQty=0;

    @Schema(description = "内检合格件数")
    @Column
    private Integer qualifiedPieceQty=0;

    @Schema(description = "外检合格件数")
    @Column
    private Integer externalQualifiedPieceQty=0;

    @Schema(description = "到货件数")
    @Column
    private Integer pieceQty=0;

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

    public Long getReleaseReceiveId() {
        return releaseReceiveId;
    }

    public void setReleaseReceiveId(Long releaseReceiveId) {
        this.releaseReceiveId = releaseReceiveId;
    }

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
    }

    public String getShippingNumber() {
        return shippingNumber;
    }

    public void setShippingNumber(String shippingNumber) {
        this.shippingNumber = shippingNumber;
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

    public Double getReceiveTotalQty() {
        return receiveTotalQty;
    }

    public void setReceiveTotalQty(Double receiveTotalQty) {
        this.receiveTotalQty = receiveTotalQty;
    }

    public Integer getReceivePieceQty() {
        return receivePieceQty;
    }

    public void setReceivePieceQty(Integer receivePieceQty) {
        this.receivePieceQty = receivePieceQty;
    }

    public Double getQualifiedTotalQty() {
        return qualifiedTotalQty;
    }

    public void setQualifiedTotalQty(Double qualifiedTotalQty) {
        this.qualifiedTotalQty = qualifiedTotalQty;
    }

    public Integer getQualifiedPieceQty() {
        return qualifiedPieceQty;
    }

    public void setQualifiedPieceQty(Integer qualifiedPieceQty) {
        this.qualifiedPieceQty = qualifiedPieceQty;
    }

    public Double getExternalQualifiedTotalQty() {
        return externalQualifiedTotalQty;
    }

    public void setExternalQualifiedTotalQty(Double externalQualifiedTotalQty) {
        this.externalQualifiedTotalQty = externalQualifiedTotalQty;
    }

    public Integer getExternalQualifiedPieceQty() {
        return externalQualifiedPieceQty;
    }

    public void setExternalQualifiedPieceQty(Integer externalQualifiedPieceQty) {
        this.externalQualifiedPieceQty = externalQualifiedPieceQty;
    }
}
