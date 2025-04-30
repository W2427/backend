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
@Table(name = "M_SITE_MATL_STATUS")
@NamedQuery(name = "MSiteMatlStatusEntity.findAll", query = "SELECT a FROM MSiteMatlStatusEntity a")
public class MSiteMatlStatusEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "SMST_ID")
    private Long smstId;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "SMST_CODE")
    private String smstCode;

    @Column(name = "ORDER_SEQ")
    private Long orderSeq;

    @Column(name = "DP_ID")
    private Long dpId;

    @Column(name = "USR_ID")
    private String usrId;

    @Column(name = "LMOD")
    private Date lmod;

    @Column(name = "INT_REV")
    private Long intRev;

    public MSiteMatlStatusEntity() {

    }

    public Long getSmstId() {
        return smstId;
    }

    public void setSmstId(Long smstId) {
        this.smstId = smstId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getSmstCode() {
        return smstCode;
    }

    public void setSmstCode(String smstCode) {
        this.smstCode = smstCode;
    }

    public Long getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Long orderSeq) {
        this.orderSeq = orderSeq;
    }

    public Long getDpId() {
        return dpId;
    }

    public void setDpId(Long dpId) {
        this.dpId = dpId;
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
}
