package com.ose.test.entity;

import com.ose.dto.BaseDTO;

/**
 */

public class PoDetail extends BaseDTO {

    private static final long serialVersionUID = -5725891260099927636L;

    private Object ident;

    private Object commodityCode;

    private Object shortDesc;

    private Object poliQty;

    private Object pohId;

    private Object orderType;

    private Object poliUnitPrice;

    private Object taxCost;

    private Object unitCode;

    private Object reqCode;

    private Object invQty;

    public Object getIdent() {
        return ident;
    }

    public void setIdent(Object ident) {
        this.ident = ident;
    }

    public Object getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(Object commodityCode) {
        this.commodityCode = commodityCode;
    }

    public Object getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(Object shortDesc) {
        this.shortDesc = shortDesc;
    }

    public Object getPoliQty() {
        return poliQty;
    }

    public void setPoliQty(Object poliQty) {
        this.poliQty = poliQty;
    }

    public Object getPohId() {
        return pohId;
    }

    public void setPohId(Object pohId) {
        this.pohId = pohId;
    }

    public Object getOrderType() {
        return orderType;
    }

    public void setOrderType(Object orderType) {
        this.orderType = orderType;
    }

    public Object getPoliUnitPrice() {
        return poliUnitPrice;
    }

    public void setPoliUnitPrice(Object poliUnitPrice) {
        this.poliUnitPrice = poliUnitPrice;
    }

    public Object getTaxCost() {
        return taxCost;
    }

    public void setTaxCost(Object taxCost) {
        this.taxCost = taxCost;
    }

    public Object getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(Object unitCode) {
        this.unitCode = unitCode;
    }

    public Object getReqCode() {
        return reqCode;
    }

    public void setReqCode(Object reqCode) {
        this.reqCode = reqCode;
    }

    public Object getInvQty() {
        return invQty;
    }

    public void setInvQty(Object invQty) {
        this.invQty = invQty;
    }

}
