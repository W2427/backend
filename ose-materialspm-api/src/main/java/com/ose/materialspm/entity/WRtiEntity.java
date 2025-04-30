package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "W_RTI")
@NamedQuery(name = "WRtiEntity.findAll", query = "SELECT a FROM WRtiEntity a")
public class WRtiEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RTI_NUMBER")
    private String rtiNumber;

    @Column(name = "ESI_STATUS")
    private String esiStatus;

    @Column(name = "REVISION_ID")
    private Integer revisionId;

    @Column(name = "RTI_CREATE_DATE")
    private Date rtiCreateDate;

    @Column(name = "RTI_POST_DATE")
    private Date rtiPostDate;

    @Column(name = "POPL_ISH_BY_PROC")
    private String poplIsHByProc;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "SHORT_DESC")
    private String shortDesc;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COMPANY_ID")
    private Integer companyId;

    public WRtiEntity() {

    }

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public String getRtiNumber() {
        return rtiNumber;
    }

    public void setRtiNumber(String rtiNumber) {
        this.rtiNumber = rtiNumber;
    }

    public Integer getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Integer revisionId) {
        this.revisionId = revisionId;
    }

    public Date getRtiCreateDate() {
        return rtiCreateDate;
    }

    public void setRtiCreateDate(Date rtiCreateDate) {
        this.rtiCreateDate = rtiCreateDate;
    }

    public Date getRtiPostDate() {
        return rtiPostDate;
    }

    public void setRtiPostDate(Date rtiPostDate) {
        this.rtiPostDate = rtiPostDate;
    }

    public String getPoplIsHByProc() {
        return poplIsHByProc;
    }

    public void setPoplIsHByProc(String poplIsHByProc) {
        this.poplIsHByProc = poplIsHByProc;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
