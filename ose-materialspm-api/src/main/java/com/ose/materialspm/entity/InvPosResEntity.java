package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 预测预留表
 */
@Entity
@Table(name = "m_inv_pos_res")
@NamedQuery(name = "InvPosResEntity.findAll", query = "SELECT a FROM InvPosResEntity a")
public class InvPosResEntity extends BaseDTO {
    private static final long serialVersionUID = 7328437152345102040L;

    @Id
    @Column(name = "ivpr_id", nullable = false)
    private Integer ivprId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "lp_id", nullable = false)
    private Integer lpId;

    @Column(name = "ivi_id")
    private Integer iviId;

    @Column(name = "item_ship_id")
    private Integer itemShipId;

    @Column(name = "fah_id", nullable = false)
    private Integer fahId;

    @Column(name = "rli_id")
    private Integer rliId;

    @Column(name = "mrli_id")
    private Integer mrliId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "unit_id", nullable = false)
    private Integer unitId;

    @Column(name = "ivpr_type", nullable = false)
    private String ivprType;

    @Column(name = "resv_qty", nullable = false)
    private Double resvQty;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "ident")
    private Integer ident;

    @Column(name = "ln_id")
    private Integer lnId;

    @Column(name = "subst_status", nullable = false)
    private String substStatus;

    @Column(name = "subst_ind", nullable = false)
    private String substInd;

    @Column(name = "trans_ind", nullable = false)
    private String transInd;

    @Column(name = "subst_approved_ind", nullable = false)
    private String substApprovedInd;

    @Column(name = "trans_resv_qty", nullable = false)
    private Double transResvQty;

    @Column(name = "old_resv_qty", nullable = false)
    private Double oldResvQty;

    @Column(name = "manhours")
    private Double manhours;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "weight_qty")
    private Double weightQty;

    @Column(name = "status")
    private String status;

    @Column(name = "lp_qty")
    private Double lpQty;

    @Column(name = "lp_resv_qty")
    private Double lpResvQty;

    @Column(name = "lp_issue_qty")
    private Double lpIssueQty;

    @Column(name = "original_resv_qty", nullable = false)
    private Double originalResvQty;

    @Column(name = "original_lp_id")
    private Integer originalLpId;

    @Column(name = "parent_ivpr_id")
    private Integer parentIvprId;

    @Column(name = "fsmn_sel_ind", nullable = false)
    private String fsmnSelInd;

    public Integer getIvprId() {
        return ivprId;
    }

    public void setIvprId(Integer ivprId) {
        this.ivprId = ivprId;
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

    public Integer getIviId() {
        return iviId;
    }

    public void setIviId(Integer iviId) {
        this.iviId = iviId;
    }

    public Integer getItemShipId() {
        return itemShipId;
    }

    public void setItemShipId(Integer itemShipId) {
        this.itemShipId = itemShipId;
    }

    public Integer getFahId() {
        return fahId;
    }

    public void setFahId(Integer fahId) {
        this.fahId = fahId;
    }

    public Integer getRliId() {
        return rliId;
    }

    public void setRliId(Integer rliId) {
        this.rliId = rliId;
    }

    public Integer getMrliId() {
        return mrliId;
    }

    public void setMrliId(Integer mrliId) {
        this.mrliId = mrliId;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getIvprType() {
        return ivprType;
    }

    public void setIvprType(String ivprType) {
        this.ivprType = ivprType;
    }

    public Double getResvQty() {
        return resvQty;
    }

    public void setResvQty(Double resvQty) {
        this.resvQty = resvQty;
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

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public Integer getLnId() {
        return lnId;
    }

    public void setLnId(Integer lnId) {
        this.lnId = lnId;
    }

    public String getSubstStatus() {
        return substStatus;
    }

    public void setSubstStatus(String substStatus) {
        this.substStatus = substStatus;
    }

    public String getSubstInd() {
        return substInd;
    }

    public void setSubstInd(String substInd) {
        this.substInd = substInd;
    }

    public String getTransInd() {
        return transInd;
    }

    public void setTransInd(String transInd) {
        this.transInd = transInd;
    }

    public String getSubstApprovedInd() {
        return substApprovedInd;
    }

    public void setSubstApprovedInd(String substApprovedInd) {
        this.substApprovedInd = substApprovedInd;
    }

    public Double getTransResvQty() {
        return transResvQty;
    }

    public void setTransResvQty(Double transResvQty) {
        this.transResvQty = transResvQty;
    }

    public Double getOldResvQty() {
        return oldResvQty;
    }

    public void setOldResvQty(Double oldResvQty) {
        this.oldResvQty = oldResvQty;
    }

    public Double getManhours() {
        return manhours;
    }

    public void setManhours(Double manhours) {
        this.manhours = manhours;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getWeightQty() {
        return weightQty;
    }

    public void setWeightQty(Double weightQty) {
        this.weightQty = weightQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLpQty() {
        return lpQty;
    }

    public void setLpQty(Double lpQty) {
        this.lpQty = lpQty;
    }

    public Double getLpResvQty() {
        return lpResvQty;
    }

    public void setLpResvQty(Double lpResvQty) {
        this.lpResvQty = lpResvQty;
    }

    public Double getLpIssueQty() {
        return lpIssueQty;
    }

    public void setLpIssueQty(Double lpIssueQty) {
        this.lpIssueQty = lpIssueQty;
    }

    public Double getOriginalResvQty() {
        return originalResvQty;
    }

    public void setOriginalResvQty(Double originalResvQty) {
        this.originalResvQty = originalResvQty;
    }

    public Integer getOriginalLpId() {
        return originalLpId;
    }

    public void setOriginalLpId(Integer originalLpId) {
        this.originalLpId = originalLpId;
    }

    public Integer getParentIvprId() {
        return parentIvprId;
    }

    public void setParentIvprId(Integer parentIvprId) {
        this.parentIvprId = parentIvprId;
    }

    public String getFsmnSelInd() {
        return fsmnSelInd;
    }

    public void setFsmnSelInd(String fsmnSelInd) {
        this.fsmnSelInd = fsmnSelInd;
    }
}
