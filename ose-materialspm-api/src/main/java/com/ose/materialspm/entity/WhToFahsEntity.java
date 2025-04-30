package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 仓库和出库单关联表
 */
@Entity
@Table(name = "m_wh_to_fahs")
@NamedQuery(name = "WhToFahsEntity.findAll", query = "SELECT a FROM WhToFahsEntity a")
public class WhToFahsEntity extends BaseDTO {

    private static final long serialVersionUID = 1524902653017775556L;
    @Id
    @Column(name = "whtf_id", nullable = false)
    private Integer whtfId;

    @Column(name = "wh_id", nullable = false)
    private Integer whId;

    @Column(name = "bnl_id")
    private Integer bnlId;

    @Column(name = "fah_id", nullable = false)
    private Integer fahId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "order_seq", nullable = false)
    private Integer orderSeq;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    public Integer getWhtfId() {
        return whtfId;
    }

    public void setWhtfId(Integer whtfId) {
        this.whtfId = whtfId;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getBnlId() {
        return bnlId;
    }

    public void setBnlId(Integer bnlId) {
        this.bnlId = bnlId;
    }

    public Integer getFahId() {
        return fahId;
    }

    public void setFahId(Integer fahId) {
        this.fahId = fahId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
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

    public Integer getIntRev() {
        return intRev;
    }

    public void setIntRev(Integer intRev) {
        this.intRev = intRev;
    }
}
