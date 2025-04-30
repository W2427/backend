package com.ose.test.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.ose.dto.BaseDTO;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "M_LOCATIONS")
@NamedQuery(name = "MLocationEntity.findAll", query = "SELECT a FROM MLocationEntity a")
public class MLocationEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "LOC_ID")
    private Long locId;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "LOC_CODE")
    private String locCode;

    @Column(name = "DP_ID")
    private Long dpId;

    @Column(name = "QUARANT_LOC_IND")
    private String quarantLocInd;

    @Column(name = "CABLE_DRUM_IND")
    private String cableDrumInd;

    @Column(name = "USR_ID")
    private String usrId;

    @Column(name = "LMOD")
    private Date lmod;

    @Column(name = "INT_REV")
    private Long intRev;

    @Column(name = "SHORT_DESC")
    private String shortDesc;

    @Column(name = "DESCRIPTION")
    private String description;

    public MLocationEntity() {

    }

    public Long getLocId() {
        return locId;
    }

    public void setLocId(Long locId) {
        this.locId = locId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public Long getDpId() {
        return dpId;
    }

    public void setDpId(Long dpId) {
        this.dpId = dpId;
    }

    public String getQuarantLocInd() {
        return quarantLocInd;
    }

    public void setQuarantLocInd(String quarantLocInd) {
        this.quarantLocInd = quarantLocInd;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public Long getIntRev() {
        return intRev;
    }

    public void setIntRev(Long intRev) {
        this.intRev = intRev;
    }

    public String getCableDrumInd() {
        return cableDrumInd;
    }

    public void setCableDrumInd(String cableDrumInd) {
        this.cableDrumInd = cableDrumInd;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
