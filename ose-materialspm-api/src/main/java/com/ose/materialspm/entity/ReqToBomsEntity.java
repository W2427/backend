package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 请购和BOM关联表
 */
@Entity
@Table(name = "m_req_to_boms")
@NamedQuery(name = "ReqToBomsEntity.findAll", query = "SELECT a FROM ReqToBomsEntity a")
public class ReqToBomsEntity extends BaseDTO {

    private static final long serialVersionUID = -1381586996210012664L;
    @Id
    @Column(name = "rtb_id", nullable = false)
    private Integer rtbId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "lp_id")
    private Integer lpId;

    @Column(name = "r_id", nullable = false)
    private Integer rId;

    @Column(name = "rtp_ind", nullable = false)
    private String rtpInd = "Y";

    @Column(name = "actual_ind", nullable = false)
    private String actualInd = "Y";

    @Column(name = "rli_id")
    private Integer rliId;

    @Column(name = "mty_id")
    private Integer mtyId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "rtb_specification")
    private String rtbSpecification;

    public Integer getRtbId() {
        return rtbId;
    }

    public void setRtbId(Integer rtbId) {
        this.rtbId = rtbId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getLpId() {
        return lpId;
    }

    public void setLpId(Integer lpId) {
        this.lpId = lpId;
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public String getRtpInd() {
        return rtpInd;
    }

    public void setRtpInd(String rtpInd) {
        this.rtpInd = rtpInd;
    }

    public String getActualInd() {
        return actualInd;
    }

    public void setActualInd(String actualInd) {
        this.actualInd = actualInd;
    }

    public Integer getRliId() {
        return rliId;
    }

    public void setRliId(Integer rliId) {
        this.rliId = rliId;
    }

    public Integer getMtyId() {
        return mtyId;
    }

    public void setMtyId(Integer mtyId) {
        this.mtyId = mtyId;
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

    public String getRtbSpecification() {
        return rtbSpecification;
    }

    public void setRtbSpecification(String rtbSpecification) {
        this.rtbSpecification = rtbSpecification;
    }
}
