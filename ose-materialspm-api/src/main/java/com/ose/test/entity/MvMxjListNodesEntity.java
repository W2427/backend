package com.ose.test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.ose.dto.BaseDTO;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "mv_mxj_list_nodes")
@NamedQuery(name = "MvMxjListNodesEntity.findAll", query = "SELECT a FROM MvMxjListNodesEntity a")
public class MvMxjListNodesEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ln_id")
    private String lnId;

    @Column(name = "parent_ln_id")
    private String parentLnId;

    @Column(name = "ln_code")
    private String lnCode;

    @Column(name = "bompath")
    private String bompath;

    @Column(name = "proj_id")
    private String projId;

    public MvMxjListNodesEntity() {

    }

    public String getLnId() {
        return lnId;
    }

    public void setLnId(String lnId) {
        this.lnId = lnId;
    }

    public String getParentLnId() {
        return parentLnId == null ? "0" : parentLnId;
    }

    public void setParentLnId(String parentLnId) {
        this.parentLnId = parentLnId;
    }

    public String getLnCode() {
        return lnCode;
    }

    public void setLnCode(String lnCode) {
        this.lnCode = lnCode;
    }

    public String getBompath() {
        return bompath;
    }

    public void setBompath(String bompath) {
        this.bompath = bompath;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

}
