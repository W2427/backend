package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.QrCodeType;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 出库材料二维码。
 */
@Entity
@Table(name = "mm_issue_detail_qr_code",
    indexes = {
        @Index(columnList = "orgId,projectId,mmIssueDetailId,status"),
        @Index(columnList = "orgId,projectId,mmIssueDetailId,qrCode,status"),
        @Index(columnList = "orgId,projectId,mmIssueDetailId,id,status")
    })
public class MmIssueDetailQrCodeEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 1480492519072194776L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "公司Id")
    @Column
    public Long companyId;

    @Schema(description = "出库明细ID")
    @Column
    private Long mmIssueDetailId;

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

    @Schema(description ="规格描述")
    @Column
    private String specDescription;

    @Schema(description ="计量单位")
    @Column
    private String designUnit;

    @Schema(description ="件号")
    @Column
    private String pieceTagNo;

    @Schema(description ="炉批号")
    @Column
    private String heatBatchNo;

    @Schema(description ="材料证书")
    @Column
    private String materialCertificate;

    @Schema(description ="出库盘点总量")
    @Column
    private Double inventoryTotalQty = 0.0;

    @Schema(description ="出库盘点件数")
    @Column
    private Integer inventoryPieceQty = 0;

    @Schema(description ="类型（一类一码，一物一码）")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private QrCodeType qrCodeType;

    @Schema(description ="规格量")
    @Column
    private Double specValue;

    @Schema(description ="仓库货架")
    @Column
    private String wareHouseLocationName;

    @Schema(description ="车船号")
    @Column
    private String shippingNo;

    @Schema(description = "二维码")
    @Column
    private String qrCode;

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

    public Long getMmIssueDetailId() {
        return mmIssueDetailId;
    }

    public void setMmIssueDetailId(Long mmIssueDetailId) {
        this.mmIssueDetailId = mmIssueDetailId;
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

    public String getSpecDescription() {
        return specDescription;
    }

    public void setSpecDescription(String specDescription) {
        this.specDescription = specDescription;
    }

    public String getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
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

    public Double getInventoryTotalQty() {
        return inventoryTotalQty;
    }

    public void setInventoryTotalQty(Double inventoryTotalQty) {
        this.inventoryTotalQty = inventoryTotalQty;
    }

    public Integer getInventoryPieceQty() {
        return inventoryPieceQty;
    }

    public void setInventoryPieceQty(Integer inventoryPieceQty) {
        this.inventoryPieceQty = inventoryPieceQty;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
