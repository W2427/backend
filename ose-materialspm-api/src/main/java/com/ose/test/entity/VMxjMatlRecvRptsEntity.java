package com.ose.test.entity;

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
@Table(name = "V_MXJ_MATL_RECV_RPTS")
@NamedQuery(name = "VMxjMatlRecvRptsEntity.findAll", query = "SELECT a FROM VMxjMatlRecvRptsEntity a")
public class VMxjMatlRecvRptsEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "MRR_ID")
    private String mrrId;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "MRR_NUMBER")
    private String mrrNumber;

    @Column(name = "REVISION_ID")
    private String revisionId;

    @Column(name = "SHORT_DESC")
    private String shortDesc;

    @Column(name = "DP_CODE")
    private String dpCode;

    @Column(name = "RECV_TYPE")
    private String recvType;

    @Column(name = "WH_CODE")
    private String whCode;

    @Column(name = "LOC_CODE")
    private String locCode;

    @Column(name = "POH_ID")
    private String pohId;

    @Column(name = "PO_NUMBER")
    private String poNumber;

    @Column(name = "PO_SUPP")
    private String poSupp;

    @Column(name = "RELN_NUMBER")
    private String relnNumber;

    public VMxjMatlRecvRptsEntity() {

    }

    public String getMrrId() {
        return mrrId;
    }

    public void setMrrId(String mrrId) {
        this.mrrId = mrrId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getMrrNumber() {
        return mrrNumber;
    }

    public void setMrrNumber(String mrrNumber) {
        this.mrrNumber = mrrNumber;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public String getRecvType() {
        return recvType;
    }

    public void setRecvType(String recvType) {
        this.recvType = recvType;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getPohId() {
        return pohId;
    }

    public void setPohId(String pohId) {
        this.pohId = pohId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getPoSupp() {
        return poSupp;
    }

    public void setPoSupp(String poSupp) {
        this.poSupp = poSupp;
    }

    public String getRelnNumber() {
        return relnNumber;
    }

    public void setRelnNumber(String relnNumber) {
        this.relnNumber = relnNumber;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

}
