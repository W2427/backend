package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * BOM层级节点定义
 */
@Entity
@Table(name = "m_list_structure_trees")
@NamedQuery(name = "ListStructureTreesEntity.findAll", query = "SELECT a FROM ListStructureTreesEntity a")
public class ListStructureTreesEntity extends BaseDTO {


    private static final long serialVersionUID = -7204285017454040517L;
    @Id
    @Column(name = "ls_id", nullable = false)
    private Integer lsId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "parent_ls_id")
    private Integer parentLsId;

    public Integer getLsId() {
        return lsId;
    }

    public void setLsId(Integer lsId) {
        this.lsId = lsId;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
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

    public Integer getParentLsId() {
        return parentLsId;
    }

    public void setParentLsId(Integer parentLsId) {
        this.parentLsId = parentLsId;
    }
}
