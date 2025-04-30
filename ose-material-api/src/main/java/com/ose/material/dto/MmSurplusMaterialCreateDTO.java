package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serial;

/**
 * 在库材料主表创建DTO
 */
public class MmSurplusMaterialCreateDTO extends BaseDTO {


    @Serial
    private static final long serialVersionUID = -6101411934046006619L;

    @Schema(description = "项目ID")
    public Long companyId;

    @Schema(description ="Ident码")
    private String identCode;

    @Schema(description ="材料编码")
    private String mmMaterialCodeNo;

    @Schema(description ="材料描述")
    private String mmMaterialCodeDescription;

    @Schema(description ="材料描述2")
    private String specName;

    @Schema(description ="材质")
    private String specQuality;

    @Schema(description ="规格描述")
    private String specDescription;

    @Schema(description ="计量单位")
    private String designUnit;

    @Schema(description ="件号")
    private String pieceTagNo;

    @Schema(description ="炉批号")
    private String heatBatchNo;

    @Schema(description ="材料证书")
    private String materialCertificate;

    @Schema(description ="类型（一类一码，一物一码）")
    @Enumerated(EnumType.STRING)
    private QrCodeType qrCodeType;

    @Schema(description ="规格量")
    private Double specValue;

    @Schema(description ="仓库货架")
    private String wareHouseLocationName;

    @Schema(description ="车船号")
    private String shippingNo;

    @Schema(description = "材料二维码")
    private String qrCode;

    @Schema(description = "创建人ID")
    private Long createdBy;

    @Schema(description = "余料名")
    private String name;

    @Schema(description = "套料图号")
    private String nestDrawingNo;

    @Schema(description = "套料单编号")
    private String nestNo;

    @Schema(description = "原余料名")
    private String originalSurplusName;

    @Schema(description = "原余料ID")
    private Long originalSurplusId;

    @Schema(description = "出库状态（已出库，未出库）")
    private Boolean issuedFlag;

    @Schema(description = "备注")
    private String description;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNestDrawingNo() {
        return nestDrawingNo;
    }

    public void setNestDrawingNo(String nestDrawingNo) {
        this.nestDrawingNo = nestDrawingNo;
    }

    public String getNestNo() {
        return nestNo;
    }

    public void setNestNo(String nestNo) {
        this.nestNo = nestNo;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getOriginalSurplusName() {
        return originalSurplusName;
    }

    public void setOriginalSurplusName(String originalSurplusName) {
        this.originalSurplusName = originalSurplusName;
    }

    public Long getOriginalSurplusId() {
        return originalSurplusId;
    }

    public void setOriginalSurplusId(Long originalSurplusId) {
        this.originalSurplusId = originalSurplusId;
    }

    public Boolean getIssuedFlag() {
        return issuedFlag;
    }

    public void setIssuedFlag(Boolean issuedFlag) {
        this.issuedFlag = issuedFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
