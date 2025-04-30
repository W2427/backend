package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 放行单查询DTO
 */
public class MmReleaseNotePrintDTO extends BaseDTO {

    private static final long serialVersionUID = -4128282859838155424L;
    /**
     *
     */

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "材料编码")
    private String materialCodeNo;

    @Schema(description = "ident码")
    private String identCode;

    @Schema(description = "二维码类型")
    private String qrCodeType;

    @Schema(description = "规格名称")
    private String specName;

    @Schema(description = "材料描述")
    private String description;

    @Schema(description = "规格量")
    private String specQty;

    @Schema(description = "计量单位")
    private String designUnit;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getMaterialCodeNo() {
        return materialCodeNo;
    }

    public void setMaterialCodeNo(String materialCodeNo) {
        this.materialCodeNo = materialCodeNo;
    }

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public String getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(String qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecQty() {
        return specQty;
    }

    public void setSpecQty(String specQty) {
        this.specQty = specQty;
    }

    public String getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
    }
}
