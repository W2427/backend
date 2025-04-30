package com.ose.materialspm.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库清单DTO
 */
public class FMaterialReceiveReceiptDetailDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "默认值：N")
    private String esiStatus;

    //仓库
    private String whId;

    private String whCode;

    //库位
    private String locId;

    private String locCode;

    //材料状态
    private String smstId;

    private String smstCode;

    private String itemShipId;

    private String ident;

    private BigDecimal recvQty;

    private Date recvDate;

    private String unitId;

    private String unitCode;

    private String tagNumber;

    private String heatNumber;

    private String certificateNumber;

    private String storekeeper;

    private String manufacturer;

    private String fileLocation;

    private String esTagShortDesc;

    private String esTagDescription;

    private String bnlId;

    private String ityCode;

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
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

    public String getSmstId() {
        return smstId;
    }

    public void setSmstId(String smstId) {
        this.smstId = smstId;
    }

    public String getSmstCode() {
        return smstCode;
    }

    public void setSmstCode(String smstCode) {
        this.smstCode = smstCode;
    }

    public String getItemShipId() {
        return itemShipId;
    }

    public void setItemShipId(String itemShipId) {
        this.itemShipId = itemShipId;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public BigDecimal getRecvQty() {
        return recvQty;
    }

    public void setRecvQty(BigDecimal recvQty) {
        this.recvQty = recvQty;
    }

    public Date getRecvDate() {
        return recvDate;
    }

    public void setRecvDate(Date recvDate) {
        this.recvDate = recvDate;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getHeatNumber() {
        return heatNumber;
    }

    public void setHeatNumber(String heatNumber) {
        this.heatNumber = heatNumber;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getEsTagShortDesc() {
        return esTagShortDesc;
    }

    public void setEsTagShortDesc(String esTagShortDesc) {
        this.esTagShortDesc = esTagShortDesc;
    }

    public String getEsTagDescription() {
        return esTagDescription;
    }

    public void setEsTagDescription(String esTagDescription) {
        this.esTagDescription = esTagDescription;
    }

    public String getBnlId() {
        return bnlId;
    }

    public void setBnlId(String bnlId) {
        this.bnlId = bnlId;
    }

    public String getItyCode() {
        return ityCode;
    }

    public void setItyCode(String ityCode) {
        this.ityCode = ityCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getStorekeeper() {
        return storekeeper;
    }

    public void setStorekeeper(String storekeeper) {
        this.storekeeper = storekeeper;
    }
}
