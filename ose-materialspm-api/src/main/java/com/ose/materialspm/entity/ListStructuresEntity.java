package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * BOM层级节点定义
 */
@Entity
@Table(name = "m_list_structures")
@NamedQuery(name = "ListStructuresEntity.findAll", query = "SELECT a FROM ListStructuresEntity a")
public class ListStructuresEntity extends BaseDTO {

    private static final long serialVersionUID = -2155410983621167288L;
    @Id
    @Column(name = "ls_id", nullable = false)
    private Integer lsId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "control_status", nullable = false)
    private Integer controlStatus = 1;

    @Column(name = "ls_code", nullable = false)
    private String lsCode;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "control_date", nullable = false)
    private Date controlDate;

    @Column(name = "ics_id")
    private Integer icsId;

    @Column(name = "desc_rule")
    private String descRule;

    @Column(name = "weight_calc_ind")
    private String weightCalcInd = "N";

    @Column(name = "spe_code")
    private String speCode;

    public Integer getLsId() {
        return lsId;
    }

    public void setLsId(Integer lsId) {
        this.lsId = lsId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getControlStatus() {
        return controlStatus;
    }

    public void setControlStatus(Integer controlStatus) {
        this.controlStatus = controlStatus;
    }

    public String getLsCode() {
        return lsCode;
    }

    public void setLsCode(String lsCode) {
        this.lsCode = lsCode;
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

    public Date getControlDate() {
        return controlDate;
    }

    public void setControlDate(Date controlDate) {
        this.controlDate = controlDate;
    }

    public Integer getIcsId() {
        return icsId;
    }

    public void setIcsId(Integer icsId) {
        this.icsId = icsId;
    }

    public String getDescRule() {
        return descRule;
    }

    public void setDescRule(String descRule) {
        this.descRule = descRule;
    }

    public String getWeightCalcInd() {
        return weightCalcInd;
    }

    public void setWeightCalcInd(String weightCalcInd) {
        this.weightCalcInd = weightCalcInd;
    }

    public String getSpeCode() {
        return speCode;
    }

    public void setSpeCode(String speCode) {
        this.speCode = speCode;
    }
}
