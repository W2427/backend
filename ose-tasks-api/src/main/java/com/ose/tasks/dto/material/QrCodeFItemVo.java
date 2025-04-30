package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.material.TagNumberType;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * 二维码查询DTO
 */
public class QrCodeFItemVo extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    private Long qrCodeId;

    private String qrCode;

    private Long fItemId;

    private Long fItemDetailId;

    private String dpCode;

    private int dpId;

    private String spec;

    @Schema(description = "材质")
    private String texture;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    private int tagNumberId;

    private String tagNumber;

    private String ident;

    private TagNumberType tagNumberType;

    private Long heatNoId;

    private String heatNoCode;

    private int qtyUnitId;

    private String qtyUnitCode;

    private BigDecimal qty;

    private int qtyCnt;

    private String shortDesc;

    @Schema(description = "仓库ID")
    private String whId;

    @Schema(description = "仓库")
    private String whCode;

    @Schema(description = "库位ID")
    private String locId;

    @Schema(description = "库位")
    private String locCode;

    @Schema(description = "货架-层号")
    private String goodsShelf;

    @Schema(description = "显示用单位长度")
    private String displayQty;

    @Schema(description = "显示用单位")
    private String displayQtyUnitCode;

    @Schema(description = "材料批次号")
    private String materialBatchNumber;

    public Long getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(Long qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public int getDpId() {
        return dpId;
    }

    public void setDpId(int dpId) {
        this.dpId = dpId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public int getTagNumberId() {
        return tagNumberId;
    }

    public void setTagNumberId(int tagNumberId) {
        this.tagNumberId = tagNumberId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Long getHeatNoId() {
        return heatNoId;
    }

    public void setHeatNoId(Long heatNoId) {
        this.heatNoId = heatNoId;
    }

    public String getHeatNoCode() {
        return heatNoCode;
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }

    public int getQtyUnitId() {
        return qtyUnitId;
    }

    public void setQtyUnitId(int qtyUnitId) {
        this.qtyUnitId = qtyUnitId;
    }

    public String getQtyUnitCode() {
        return qtyUnitCode;
    }

    public void setQtyUnitCode(String qtyUnitCode) {
        this.qtyUnitCode = qtyUnitCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public int getQtyCnt() {
        return qtyCnt;
    }

    public void setQtyCnt(int qtyCnt) {
        this.qtyCnt = qtyCnt;
    }

    public Long getfItemId() {
        return fItemId;
    }

    public void setfItemId(Long fItemId) {
        this.fItemId = fItemId;
    }

    public Long getfItemDetailId() {
        return fItemDetailId;
    }

    public void setfItemDetailId(Long fItemDetailId) {
        this.fItemDetailId = fItemDetailId;
    }

    public TagNumberType getTagNumberType() {
        return tagNumberType;
    }

    public void setTagNumberType(TagNumberType tagNumberType) {
        this.tagNumberType = tagNumberType;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getWhId() {
        return whId;
    }

    public void setWhId(String whId) {
        this.whId = whId;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getGoodsShelf() {
        return goodsShelf;
    }

    public void setGoodsShelf(String goodsShelf) {
        this.goodsShelf = goodsShelf;
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

    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
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

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
