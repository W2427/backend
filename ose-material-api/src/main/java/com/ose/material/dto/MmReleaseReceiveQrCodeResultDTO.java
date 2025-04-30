package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 入库材料二维码查询DTO
 */
public class MmReleaseReceiveQrCodeResultDTO extends BaseDTO {

    private static final long serialVersionUID = 2303090171608791138L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "公司ID")
    public Long companyId;

    @Schema(description = "在库单明细ID")
    private Long mmMaterialInStockDetailId;

    @Schema(description = "入库单明细ID")
    private Long mmReleaseReceiveDetailId;

    @Schema(description = "Ident码")
    private String identCode;

    @Schema(description = "材料编码")
    private String mmMaterialCodeNo;

    @Schema(description = "材料描述")
    private String mmMaterialCodeDescription;

    @Schema(description = "材料描述2")
    private String specName;

    @Schema(description = "材质")
    private String specQuality;

    @Schema(description = "规格描述")
    private String specDescription;

    @Schema(description = "计量单位")
    private String designUnit;

    @Schema(description = "件号")
    private String pieceTagNo;

    @Schema(description = "炉批号")
    private String heatBatchNo;

    @Schema(description = "材料证书")
    private String materialCertificate;

    @Schema(description = "在库总量数量")
    private Double inStockTotalQty = 0.0;

    @Schema(description = "入库总量数量")
    private Double receivedTotalQty = 0.0;

    @Schema(description = "出库总量数量")
    private Double issuedTotalQty = 0.0;

    @Schema(description = "在库总件数")
    private Integer inStockPieceQty = 0;

    @Schema(description = "入库总件数")
    private Integer receivedPieceQty = 0;

    @Schema(description = "出库总件数")
    private Integer issuedPieceQty = 0;

    @Schema(description = "类型（一类一码，一物一码）")
    @Enumerated(EnumType.STRING)
    private QrCodeType qrCodeType;

    @Schema(description = "规格量")
    private Double specValue;

    @Schema(description = "仓库货架")
    private String warehouseShelfNo;

    @Schema(description = "车船号")
    private String shippingNo;

    @Schema(description = "二维码")
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

    public Long getMmMaterialInStockDetailId() {
        return mmMaterialInStockDetailId;
    }

    public void setMmMaterialInStockDetailId(Long mmMaterialInStockDetailId) {
        this.mmMaterialInStockDetailId = mmMaterialInStockDetailId;
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

    public String getWarehouseShelfNo() {
        return warehouseShelfNo;
    }

    public void setWarehouseShelfNo(String warehouseShelfNo) {
        this.warehouseShelfNo = warehouseShelfNo;
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

    public Long getMmReleaseReceiveDetailId() {
        return mmReleaseReceiveDetailId;
    }

    public void setMmReleaseReceiveDetailId(Long mmReleaseReceiveDetailId) {
        this.mmReleaseReceiveDetailId = mmReleaseReceiveDetailId;
    }
}
