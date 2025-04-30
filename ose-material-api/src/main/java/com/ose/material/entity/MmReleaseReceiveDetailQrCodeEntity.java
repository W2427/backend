package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * 入库材料二维码。
 */
@Entity
@Table(name = "mm_release_receive_detail_qr_code",
    indexes = {
        @Index(columnList = "orgId,projectId,deleted"),
        @Index(columnList = "orgId,projectId,releaseReceiveDetailId,status"),
        @Index(columnList = "orgId,projectId,releaseReceiveId,qrCode,status"),
        @Index(columnList = "orgId,projectId,releaseReceiveId,status")
    })
public class MmReleaseReceiveDetailQrCodeEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 7567599563203147580L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "项目ID")
    @Column
    public Long companyId;

    @Schema(description = "入库单ID")
    @Column
    private Long releaseReceiveId;

    @Schema(description = "入库单明细ID")
    @Column
    private Long releaseReceiveDetailId;

    @Schema(description = "发货单ID")
    @Column
    private Long shippingId;

    @Schema(description = "发货单编号")
    @Column
    private String shippingNumber;

    @Schema(description = "发货单详情ID")
    @Column
    private Long shippingDetailId;

    @Schema(description = "采购包ID")
    @Column
    private Long purchasePackageId;

    @Schema(description = "采购包编号")
    @Column
    private String purchasePackageNo;

    @Schema(description = "ident码")
    @Column
    private String identCode;

    @Schema(description = "材料分类")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    public QrCodeType qrCodeType;

    @Schema(description = "材料编码")
    @Column
    private String mmMaterialCodeNo;

    @Schema(description = "材料编码描述")
    @Column
    private String mmMaterialCodeDescription;

    @Schema(description = "规格描述")
    @Column
    private String specDescription;

    @Schema(description = "件号")
    @Column
    private String pieceTagNo;

    @Schema(description = "炉批号")
    @Column
    private String heatBatchNo;

    @Schema(description = "材料证书编码")
    @Column
    public String materialCertificate;

    @Schema(description = "设计单位")
    @Column
    public String designUnit;

    @Schema(description = "单件规格量")
    @Column
    public Double specValue=0.0;

    @Schema(description = "仓库ID")
    @Column
    public Long wareHouseLocationId;

    @Schema(description = "仓库名称")
    @Column
    public String wareHouseLocationName;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "件数盘点数量")
    @Column
    public Integer pieceInventoryQty=0;

    @Schema(description = "件数")
    @Column
    public Integer pieceQty=0;

    @Schema(description = "合格件数")
    @Column
    public Integer qualifiedPieceQty=0;

    @Schema(description = "外检合格件数")
    @Column
    public Integer externalQualifiedPieceQty=0;

    @Schema(description = "总量盘点数量")
    @Column
    public Double totalInventoryQty=0.0;

    @Schema(description = "总量")
    @Column
    public Double totalQty=0.0;

    @Schema(description = "合格总量")
    @Column
    public Double qualifiedTotalQty=0.0;

    @Schema(description = "外检合格总量")
    @Column
    public Double externalQualifiedTotalQty=0.0;

    @Schema(description = "车船号")
    @Column
    private String shippingNo;

    @Schema(description = "二维码")
    @Column
    private String qrCode;

    @Schema(description = "第一次超额出库")
    @Column
    private Boolean firstExcess = false;

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

    public Long getReleaseReceiveDetailId() {
        return releaseReceiveDetailId;
    }

    public void setReleaseReceiveDetailId(Long releaseReceiveDetailId) {
        this.releaseReceiveDetailId = releaseReceiveDetailId;
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

    public Long getShippingDetailId() {
        return shippingDetailId;
    }

    public void setShippingDetailId(Long shippingDetailId) {
        this.shippingDetailId = shippingDetailId;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getPieceInventoryQty() {
        return pieceInventoryQty;
    }

    public void setPieceInventoryQty(Integer pieceInventoryQty) {
        this.pieceInventoryQty = pieceInventoryQty;
    }

    public Double getTotalInventoryQty() {
        return totalInventoryQty;
    }

    public void setTotalInventoryQty(Double totalInventoryQty) {
        this.totalInventoryQty = totalInventoryQty;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getPieceQty() {
        return pieceQty;
    }

    public void setPieceQty(Integer pieceQty) {
        this.pieceQty = pieceQty;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public Integer getQualifiedPieceQty() {
        return qualifiedPieceQty;
    }

    public void setQualifiedPieceQty(Integer qualifiedPieceQty) {
        this.qualifiedPieceQty = qualifiedPieceQty;
    }

    public Double getQualifiedTotalQty() {
        return qualifiedTotalQty;
    }

    public void setQualifiedTotalQty(Double qualifiedTotalQty) {
        this.qualifiedTotalQty = qualifiedTotalQty;
    }

    public Boolean getFirstExcess() {
        return firstExcess;
    }

    public void setFirstExcess(Boolean firstExcess) {
        this.firstExcess = firstExcess;
    }

    public Integer getExternalQualifiedPieceQty() {
        return externalQualifiedPieceQty;
    }

    public void setExternalQualifiedPieceQty(Integer externalQualifiedPieceQty) {
        this.externalQualifiedPieceQty = externalQualifiedPieceQty;
    }

    public Double getExternalQualifiedTotalQty() {
        return externalQualifiedTotalQty;
    }

    public void setExternalQualifiedTotalQty(Double externalQualifiedTotalQty) {
        this.externalQualifiedTotalQty = externalQualifiedTotalQty;
    }
}
