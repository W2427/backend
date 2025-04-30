package com.ose.material.dto;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.entity.MmShippingDetailRelationEntity;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

/**
 * 发货单详情查询列表
 */
public class MmShippingDetailSearchDetailDTO extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5735425888124712779L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "请购单ID")
    private Long companyId;

    @Schema(description = "发货单ID")
    private Long shippingId;

    @Schema(description = "材料编码")
    private String mmMaterialCodeNo;

    @Schema(description = "材料描述")
    private String mmMaterialCodeDescription;

    @Schema(description = "规格描述")
    private String specDescription;

    @Schema(description = "ident码")
    private String identCode;

    @Schema(description = "件号")
    private String pieceTagNo;

    @Schema(description = "炉批号")
    private String heatBatchNo;

    @Schema(description = "设计单位")
    private String designUnit;

    @Schema(description = "材料分类")
    @Enumerated(EnumType.STRING)
    public QrCodeType qrCodeType;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "仓库ID")
    public Long wareHouseLocationId;

    @Schema(description = "材料证书编码")
    public String materialCertificate;

    @Schema(description = "采购包ID")
    private Long purchasePackageId;

    @Schema(description = "采购包编号")
    private String purchasePackageNo;

    @Schema(description = "总量")
    private  Double totalQty;

    @Schema(description = "发货总量")
    private  Double shippedTotalQty;

    @Schema(description = "规格量")
    private  Double specValue;

    @Schema(description = "发货件数")
    private  Integer pieceQty;

    @Schema(description = "件数")
    private  Integer shippedPieceQty;

    @Schema(description = "请购与发货单详情关系")
    public List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities;

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

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
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

    public String getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
    }

    public QrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(QrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getWareHouseLocationId() {
        return wareHouseLocationId;
    }

    public void setWareHouseLocationId(Long wareHouseLocationId) {
        this.wareHouseLocationId = wareHouseLocationId;
    }

    public String getMaterialCertificate() {
        return materialCertificate;
    }

    public void setMaterialCertificate(String materialCertificate) {
        this.materialCertificate = materialCertificate;
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

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
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

    public List<MmShippingDetailRelationEntity> getMmShippingDetailRelationEntities() {
        return mmShippingDetailRelationEntities;
    }

    public void setMmShippingDetailRelationEntities(List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities) {
        this.mmShippingDetailRelationEntities = mmShippingDetailRelationEntities;
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
