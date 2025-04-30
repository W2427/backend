package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.material.SpmPlanReleaseNoteItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 */

public class SpmReleaseNoteItemResultDTO extends BaseDTO {

    private static final long serialVersionUID = -5725891260099927636L;

    private Object ident;

    private Object poNumber;

    private Object pohId;

    private Object poSupp;

    private Object itemShipID;

    private Object poliId;

    private Object relnId;

    private Object relnNumber;

    private Object qtyUnitId;

    private Object qtyUnitCode;

    private BigDecimal poliQty;

    private BigDecimal relnQty;

    private BigDecimal relnWeight;

    // 制造商
    private String manufacturer;

    // 材质
    private String material;

    @Schema(description = "SPM已入库数量")
    private BigDecimal recvOnSiteQty;

    private Object tagNumber;

    private Object commodityId;

    private Object shortDesc;

    private Object dpId;

    private Object dpCode;

    private SpmPlanReleaseNoteItemEntity spmPlanReleaseNoteItemEntity;

    public SpmPlanReleaseNoteItemEntity getSpmPlanReleaseNoteItemEntity() {
        return spmPlanReleaseNoteItemEntity;
    }

    public void setSpmPlanReleaseNoteItemEntity(SpmPlanReleaseNoteItemEntity spmPlanReleaseNoteItemEntity) {
        this.spmPlanReleaseNoteItemEntity = spmPlanReleaseNoteItemEntity;
    }

    public Object getIdent() {
        return ident;
    }

    public void setIdent(Object ident) {
        this.ident = ident;
    }

    public Object getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(Object poNumber) {
        this.poNumber = poNumber;
    }

    public Object getPoSupp() {
        return poSupp;
    }

    public void setPoSupp(Object poSupp) {
        this.poSupp = poSupp;
    }

    public Object getQtyUnitId() {
        return qtyUnitId;
    }

    public void setQtyUnitId(Object qtyUnitId) {
        this.qtyUnitId = qtyUnitId;
    }

    public Object getQtyUnitCode() {
        return qtyUnitCode;
    }

    public void setQtyUnitCode(Object qtyUnitCode) {
        this.qtyUnitCode = qtyUnitCode;
    }

    public BigDecimal getPoliQty() {
        return poliQty;
    }

    public void setPoliQty(BigDecimal poliQty) {
        this.poliQty = poliQty;
    }

    public BigDecimal getRelnQty() {
        return relnQty;
    }

    public void setRelnQty(BigDecimal relnQty) {
        this.relnQty = relnQty;
    }

    public BigDecimal getRelnWeight() {
        return relnWeight;
    }

    public void setRelnWeight(BigDecimal relnWeight) {
        this.relnWeight = relnWeight;
    }

    public BigDecimal getRecvOnSiteQty() {
        return recvOnSiteQty;
    }

    public void setRecvOnSiteQty(BigDecimal recvOnSiteQty) {
        this.recvOnSiteQty = recvOnSiteQty;
    }

    public Object getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(Object tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Object getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Object commodityId) {
        this.commodityId = commodityId;
    }

    public Object getPohId() {
        return pohId;
    }

    public void setPohId(Object pohId) {
        this.pohId = pohId;
    }

    public Object getPoliId() {
        return poliId;
    }

    public void setPoliId(Object poliId) {
        this.poliId = poliId;
    }

    public Object getRelnId() {
        return relnId;
    }

    public void setRelnId(Object relnId) {
        this.relnId = relnId;
    }

    public Object getRelnNumber() {
        return relnNumber;
    }

    public void setRelnNumber(Object relnNumber) {
        this.relnNumber = relnNumber;
    }

    public Object getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(Object shortDesc) {
        this.shortDesc = shortDesc;
    }

    public Object getDpId() {
        return dpId;
    }

    public void setDpId(Object dpId) {
        this.dpId = dpId;
    }

    public Object getDpCode() {
        return dpCode;
    }

    public void setDpCode(Object dpCode) {
        this.dpCode = dpCode;
    }

    public Object getItemShipID() {
        return itemShipID;
    }

    public void setItemShipID(Object itemShipID) {
        this.itemShipID = itemShipID;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
