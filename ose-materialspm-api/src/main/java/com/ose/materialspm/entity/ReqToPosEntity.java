package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 请购和订单关联表
 */
@Entity
@Table(name = "m_req_to_pos")
@NamedQuery(name = "ReqToPosEntity.findAll", query = "SELECT a FROM ReqToPosEntity a")
public class ReqToPosEntity extends BaseDTO {

    private static final long serialVersionUID = 1065156107741837555L;
    @Id
    @Column(name = "rp_id", nullable = false)
    private Integer rpId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "base_poh_id", nullable = false)
    private Integer basePohId;

    @Column(name = "r_id", nullable = false)
    private Integer rId;

    @Column(name = "rli_id")
    private Integer rliId;

    @Column(name = "parent_rp_id")
    private Integer parentRpId;

    @Column(name = "ident")
    private Integer ident;

    @Column(name = "tag_number")
    private String tagNumber;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "weight_factor", nullable = false)
    private Double weightFactor = 0.0;

    @Column(name = "manual_ind", nullable = false)
    private String manualInd = "N";

    @Column(name = "available_for_sup_ind", nullable = false)
    private String availableForSupInd = "Y";

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "exped_ccp_id")
    private Integer expedCcpId;

    @Column(name = "insp_ccp_id")
    private Integer inspCcpId;

    @Column(name = "exped_par_id")
    private Integer expedParId;

    @Column(name = "insp_par_id")
    private Integer inspParId;

    @Column(name = "orig_exped_ilv_id")
    private Integer origExpedIlvId;

    @Column(name = "mod_exped_ilv_id")
    private Integer modExpedIlvId;

    @Column(name = "actual_exped_ilv_id")
    private Integer actualExpedIlvId;

    @Column(name = "orig_insp_ilv_id")
    private Integer origInspIlvId;

    @Column(name = "mod_insp_ilv_id")
    private Integer modInspIlvId;

    @Column(name = "actual_insp_ilv_id")
    private Integer actualInspIlvId;

    @Column(name = "crit_ilv_id")
    private Integer critIlvId;

    @Column(name = "expediter")
    private String expediter;

    @Column(name = "inspector")
    private String inspector;

    @Column(name = "exped_sf_id")
    private Integer expedSfId;

    @Column(name = "insp_sf_id")
    private Integer inspSfId;

    @Column(name = "last_contact_date")
    private Date lastContactDate;

    @Column(name = "next_contact_date")
    private Date nextContactDate;

    @Column(name = "fab_location")
    private String fabLocation;

    @Column(name = "insp_location")
    private String inspLocation;

    public Integer getRpId() {
        return rpId;
    }

    public void setRpId(Integer rpId) {
        this.rpId = rpId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getBasePohId() {
        return basePohId;
    }

    public void setBasePohId(Integer basePohId) {
        this.basePohId = basePohId;
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public Integer getRliId() {
        return rliId;
    }

    public void setRliId(Integer rliId) {
        this.rliId = rliId;
    }

    public Integer getParentRpId() {
        return parentRpId;
    }

    public void setParentRpId(Integer parentRpId) {
        this.parentRpId = parentRpId;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Double getWeightFactor() {
        return weightFactor;
    }

    public void setWeightFactor(Double weightFactor) {
        this.weightFactor = weightFactor;
    }

    public String getManualInd() {
        return manualInd;
    }

    public void setManualInd(String manualInd) {
        this.manualInd = manualInd;
    }

    public String getAvailableForSupInd() {
        return availableForSupInd;
    }

    public void setAvailableForSupInd(String availableForSupInd) {
        this.availableForSupInd = availableForSupInd;
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

    public Integer getExpedCcpId() {
        return expedCcpId;
    }

    public void setExpedCcpId(Integer expedCcpId) {
        this.expedCcpId = expedCcpId;
    }

    public Integer getInspCcpId() {
        return inspCcpId;
    }

    public void setInspCcpId(Integer inspCcpId) {
        this.inspCcpId = inspCcpId;
    }

    public Integer getExpedParId() {
        return expedParId;
    }

    public void setExpedParId(Integer expedParId) {
        this.expedParId = expedParId;
    }

    public Integer getInspParId() {
        return inspParId;
    }

    public void setInspParId(Integer inspParId) {
        this.inspParId = inspParId;
    }

    public Integer getOrigExpedIlvId() {
        return origExpedIlvId;
    }

    public void setOrigExpedIlvId(Integer origExpedIlvId) {
        this.origExpedIlvId = origExpedIlvId;
    }

    public Integer getModExpedIlvId() {
        return modExpedIlvId;
    }

    public void setModExpedIlvId(Integer modExpedIlvId) {
        this.modExpedIlvId = modExpedIlvId;
    }

    public Integer getActualExpedIlvId() {
        return actualExpedIlvId;
    }

    public void setActualExpedIlvId(Integer actualExpedIlvId) {
        this.actualExpedIlvId = actualExpedIlvId;
    }

    public Integer getOrigInspIlvId() {
        return origInspIlvId;
    }

    public void setOrigInspIlvId(Integer origInspIlvId) {
        this.origInspIlvId = origInspIlvId;
    }

    public Integer getModInspIlvId() {
        return modInspIlvId;
    }

    public void setModInspIlvId(Integer modInspIlvId) {
        this.modInspIlvId = modInspIlvId;
    }

    public Integer getActualInspIlvId() {
        return actualInspIlvId;
    }

    public void setActualInspIlvId(Integer actualInspIlvId) {
        this.actualInspIlvId = actualInspIlvId;
    }

    public Integer getCritIlvId() {
        return critIlvId;
    }

    public void setCritIlvId(Integer critIlvId) {
        this.critIlvId = critIlvId;
    }

    public String getExpediter() {
        return expediter;
    }

    public void setExpediter(String expediter) {
        this.expediter = expediter;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public Integer getExpedSfId() {
        return expedSfId;
    }

    public void setExpedSfId(Integer expedSfId) {
        this.expedSfId = expedSfId;
    }

    public Integer getInspSfId() {
        return inspSfId;
    }

    public void setInspSfId(Integer inspSfId) {
        this.inspSfId = inspSfId;
    }

    public Date getLastContactDate() {
        return lastContactDate;
    }

    public void setLastContactDate(Date lastContactDate) {
        this.lastContactDate = lastContactDate;
    }

    public Date getNextContactDate() {
        return nextContactDate;
    }

    public void setNextContactDate(Date nextContactDate) {
        this.nextContactDate = nextContactDate;
    }

    public String getFabLocation() {
        return fabLocation;
    }

    public void setFabLocation(String fabLocation) {
        this.fabLocation = fabLocation;
    }

    public String getInspLocation() {
        return inspLocation;
    }

    public void setInspLocation(String inspLocation) {
        this.inspLocation = inspLocation;
    }
}
