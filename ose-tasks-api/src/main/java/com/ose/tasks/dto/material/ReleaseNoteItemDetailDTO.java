package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.material.TagNumberType;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * 放行单查询DTO
 */
public class ReleaseNoteItemDetailDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "材料类型(GOODS: 一物一码, TYPE: 一类一码)")
    private TagNumberType tagNumberType;

    @Schema(description = "炉号")
    private String heatNoCode;

    @Schema(description = "批号")
    private String batchNoCode;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "NPS")
    private String npsText;

    @Schema(description = "NPS 单位")
    private LengthUnit npsUnit;

    @Schema(description = "单位量")
    private BigDecimal qty;

    @Schema(description = "单位")
    private String qtyUnitCode;

    @Schema(description = "件数")
    private int qtyCnt;

    @Schema(description = "材质证书ID")
    private Long certId;

    @Schema(description = "材质证书编号")
    private String certCode;

    @Schema(description = "材质证书页码")
    private String certPageNumber;

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "材质")
    private String texture;

    @Schema(description = "库管员")
    private String storekeeper;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "批次号")
    private String batchNumber;

    @Schema(description = "追溯码（管附件）")
    private String traceCode;

    @Schema(description = "位号（阀件）")
    private String valveNumber;

    @Schema(description = "显示用单位长度")
    private String displayQty;

    @Schema(description = "显示用单位")
    private String displayQtyUnitCode;

    public TagNumberType getTagNumberType() {
        return tagNumberType;
    }

    public void setTagNumberType(TagNumberType tagNumberType) {
        this.tagNumberType = tagNumberType;
    }

    public String getHeatNoCode() {
        return heatNoCode;
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }

    public String getBatchNoCode() {
        return batchNoCode;
    }

    public void setBatchNoCode(String batchNoCode) {
        this.batchNoCode = batchNoCode;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getQtyUnitCode() {
        return qtyUnitCode;
    }

    public void setQtyUnitCode(String qtyUnitCode) {
        this.qtyUnitCode = qtyUnitCode;
    }

    public int getQtyCnt() {
        return qtyCnt;
    }

    public void setQtyCnt(int qtyCnt) {
        this.qtyCnt = qtyCnt;
    }

    public Long getCertId() {
        return certId;
    }

    public void setCertId(Long certId) {
        this.certId = certId;
    }

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }

    public String getCertPageNumber() {
        return certPageNumber;
    }

    public void setCertPageNumber(String certPageNumber) {
        this.certPageNumber = certPageNumber;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getTraceCode() {
        return traceCode;
    }

    public void setTraceCode(String traceCode) {
        this.traceCode = traceCode;
    }

    public String getValveNumber() {
        return valveNumber;
    }

    public void setValveNumber(String valveNumber) {
        this.valveNumber = valveNumber;
    }

    public String getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(String displayQty) {
        this.displayQty = displayQty;
    }

    public String getDisplayQtyUnitCode() {
        return displayQtyUnitCode;
    }

    public void setDisplayQtyUnitCode(String displayQtyUnitCode) {
        this.displayQtyUnitCode = displayQtyUnitCode;
    }

    public String getStorekeeper() {
        return storekeeper;
    }

    public void setStorekeeper(String storekeeper) {
        this.storekeeper = storekeeper;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
}
