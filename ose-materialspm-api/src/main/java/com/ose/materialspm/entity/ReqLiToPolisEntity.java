package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 请购详情与订单详情关联表
 */
@Entity
@Table(name = "m_req_li_to_polis")
@NamedQuery(name = "ReqLiToPolisEntity.findAll", query = "SELECT a FROM ReqLiToPolisEntity a")
public class ReqLiToPolisEntity extends BaseDTO {

    private static final long serialVersionUID = 7137476293728219353L;
    @Id
    @Column(name = "rltp_id", nullable = false)
    private Integer rltpId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "rli_id", nullable = false)
    private Integer rliId;

    @Column(name = "poli_id", nullable = false)
    private Integer poliId;

    @Column(name = "r_id", nullable = false)
    private Integer rId;

    @Column(name = "poh_id", nullable = false)
    private Integer pohId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "poli_qty", nullable = false)
    private Double poliQty = 0.0;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "last_rli_id")
    private Integer lastRliId;

    @Column(name = "parent_poli_id")
    private Integer parentPoliId;

    public Integer getRltpId() {
        return rltpId;
    }

    public void setRltpId(Integer rltpId) {
        this.rltpId = rltpId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getRliId() {
        return rliId;
    }

    public void setRliId(Integer rliId) {
        this.rliId = rliId;
    }

    public Integer getPoliId() {
        return poliId;
    }

    public void setPoliId(Integer poliId) {
        this.poliId = poliId;
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public Integer getPohId() {
        return pohId;
    }

    public void setPohId(Integer pohId) {
        this.pohId = pohId;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Double getPoliQty() {
        return poliQty;
    }

    public void setPoliQty(Double poliQty) {
        this.poliQty = poliQty;
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

    public Integer getLastRliId() {
        return lastRliId;
    }

    public void setLastRliId(Integer lastRliId) {
        this.lastRliId = lastRliId;
    }

    public Integer getParentPoliId() {
        return parentPoliId;
    }

    public void setParentPoliId(Integer parentPoliId) {
        this.parentPoliId = parentPoliId;
    }
}
