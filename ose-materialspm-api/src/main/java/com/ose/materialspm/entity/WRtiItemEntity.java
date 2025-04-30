package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "W_RTI_ITEMS")
@NamedQuery(name = "WRtiItemEntity.findAll", query = "SELECT a FROM WRtiItemEntity a")
public class WRtiItemEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "IVPR_ID")
    private Integer ivprId;

    @Column(name = "ESI_STATUS")
    private String esiStatus;

    @Column(name = "MIRTI_ID")
    private Integer mirtiId;

    @Column(name = "WH_ID")
    private Integer whId;

    @Column(name = "LOC_ID")
    private Integer locId;

    @Column(name = "SMST_ID")
    private Integer smstId;

    @Column(name = "RETURN_QTY", precision = 15, scale = 3)
    private BigDecimal returnQty;

    @Column(name = "UNIT_ID")
    private Integer unitId;

    @Column(name = "RTI_NUMBER")
    private String rtiNumber;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "IIS_ID")
    private Integer iisId;

    @Column(name = "MIR_NUMBER")
    private String mirNumber;

    public WRtiItemEntity() {

    }

    public Integer getIvprId() {
        return ivprId;
    }

    public void setIvprId(Integer ivprId) {
        this.ivprId = ivprId;
    }

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public Integer getMirtiId() {
        return mirtiId;
    }

    public void setMirtiId(Integer mirtiId) {
        this.mirtiId = mirtiId;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public Integer getSmstId() {
        return smstId;
    }

    public void setSmstId(Integer smstId) {
        this.smstId = smstId;
    }

    public BigDecimal getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getRtiNumber() {
        return rtiNumber;
    }

    public void setRtiNumber(String rtiNumber) {
        this.rtiNumber = rtiNumber;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getIisId() {
        return iisId;
    }

    public void setIisId(Integer iisId) {
        this.iisId = iisId;
    }

    public String getMirNumber() {
        return mirNumber;
    }

    public void setMirNumber(String mirNumber) {
        this.mirNumber = mirNumber;
    }
}
