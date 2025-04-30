package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.QrCodeType;
import com.ose.material.vo.MaterialOrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 采购包明细创建DTO
 */
public class MmPurchasePackageItemCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -5086295499923651949L;

    @Schema(description = "仓库ID")
    public Long wareHouseLocationId;

    @Schema(description = "材料编码")
    private String mmMaterialCodeNo;

    @Schema(description = "炉批号")
    private String heatBatchNo;

    @Schema(description = "证书编号")
    private String materialCertificate;

    @Schema(description = "Ident码")
    private String identCode;

    @Schema(description = "材质")
    private String specQuality;

    @Schema(description = "结算代码")
    private String porCode;

    @Schema(description = "油漆代码")
    private String paintingCode;

    @Schema(description = "规格名称")
    private String specName;

    @Schema(description = "安装码")
    private String installationCode;

    @Schema(description = "计量单位")
    private String designUnit;

    @Schema(description = "请购数量")
    private Integer designQty = 0;

    @Schema(description = "类型（一类一码，一物一码）")
    @Enumerated(EnumType.STRING)
    private QrCodeType qrCodeType;

    @Schema(description = "仓库类型（公司、项目）")
    @Enumerated(EnumType.STRING)
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description = "材料描述")
    private String mmMaterialCodeDescription;

    @Schema(description = "备注")
    private String remarks;

    public Long getWareHouseLocationId() {
        return wareHouseLocationId;
    }

    public void setWareHouseLocationId(Long wareHouseLocationId) {
        this.wareHouseLocationId = wareHouseLocationId;
    }

    public String getMmMaterialCodeNo() {
        return mmMaterialCodeNo;
    }

    public void setMmMaterialCodeNo(String mmMaterialCodeNo) {
        this.mmMaterialCodeNo = mmMaterialCodeNo;
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

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public String getSpecQuality() {
        return specQuality;
    }

    public void setSpecQuality(String specQuality) {
        this.specQuality = specQuality;
    }

    public String getPorCode() {
        return porCode;
    }

    public void setPorCode(String porCode) {
        this.porCode = porCode;
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getInstallationCode() {
        return installationCode;
    }

    public void setInstallationCode(String installationCode) {
        this.installationCode = installationCode;
    }

    public String getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
    }

    public Integer getDesignQty() {
        return designQty;
    }

    public void setDesignQty(Integer designQty) {
        this.designQty = designQty;
    }

    public QrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(QrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public MaterialOrganizationType getWareHouseType() {
        return materialOrganizationType;
    }

    public void setWareHouseType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public String getMmMaterialCodeDescription() {
        return mmMaterialCodeDescription;
    }

    public void setMmMaterialCodeDescription(String mmMaterialCodeDescription) {
        this.mmMaterialCodeDescription = mmMaterialCodeDescription;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
