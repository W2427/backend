package com.ose.materialspm.entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.ose.dto.BaseDTO;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "W_MRR_ITEMS")
@NamedQuery(name = "WMrrItemEntity.findAll", query = "SELECT a FROM WMrrItemEntity a")
public class WMrrItemEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Column(name = "IDENT")
    private int ident;

    @Column(name = "MRR_ID")
    private Integer mrrId;

    @Column(name = "MRR_NUMBER")
    private String mrrNumber;

    @Column(name = "ESI_STATUS")
    private String esiStatus;

    @Column(name = "WH_ID")
    private int whId;

    @Column(name = "LOC_ID")
    private int locId;

    @Column(name = "SMST_ID")
    private int smstId;

    @Id
    @Column(name = "ITEM_SHIP_ID")
    private int itemShipId;

    @Column(name = "RECV_QTY", precision = 15, scale = 3)
    private BigDecimal recvQty;

    @Column(name = "RECV_DATE")
    private Date recvDate;

    @Column(name = "UNIT_ID")
    private int unitId;

    @Column(name = "TAG_NUMBER")
    private String tagNumber;

    @Column(name = "HEAT_NUMBER")
    private String heatNumber;

    @Column(name = "CERTIFICATE_NUMBER")
    private String certificateNumber;

    @Column(name = "MANUFACTURER")
    private String manufacturer;

    @Column(name = "FILE_LOCATION")
    private String fileLocation;

    @Column(name = "ES_TAG_SHORT_DESC")
    private String esTagShortDesc;

    @Column(name = "ES_TAG_DESCRIPTION")
    private String esTagDescription;

    @Column(name = "BNL_ID")
    private Integer bnlId;

    @Column(name = "ITY_CODE")
    private String ityCode;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "IPR_ID")
    private Integer iprId;

    public WMrrItemEntity() {

    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public Integer getMrrId() {
        return mrrId;
    }

    public void setMrrId(Integer mrrId) {
        this.mrrId = mrrId;
    }

    public String getMrrNumber() {
        return mrrNumber;
    }

    public void setMrrNumber(String mrrNumber) {
        this.mrrNumber = mrrNumber;
    }

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public int getWhId() {
        return whId;
    }

    public void setWhId(int whId) {
        this.whId = whId;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }

    public int getSmstId() {
        return smstId;
    }

    public void setSmstId(int smstId) {
        this.smstId = smstId;
    }

    public int getItemShipId() {
        return itemShipId;
    }

    public void setItemShipId(int itemShipId) {
        this.itemShipId = itemShipId;
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

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
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

    public Integer getBnlId() {
        return bnlId;
    }

    public void setBnlId(Integer bnlId) {
        this.bnlId = bnlId;
    }

    public String getItyCode() {
        return ityCode;
    }

    public void setItyCode(String ityCode) {
        this.ityCode = ityCode;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getIprId() {
        return iprId;
    }

    public void setIprId(Integer iprId) {
        this.iprId = iprId;
    }
}
