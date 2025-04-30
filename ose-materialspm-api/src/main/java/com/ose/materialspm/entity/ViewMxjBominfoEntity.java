package com.ose.materialspm.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.ose.dto.BaseDTO;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "v_mxj_BOMinfo")
@NamedQuery(name = "ViewMxjBominfoEntity.findAll", query = "SELECT a FROM ViewMxjBominfoEntity a")
public class ViewMxjBominfoEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Column(name = "LP_POS")
    private String lpPos;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "CG_GROUP_CODE")
    private String cgGroupCode;

    @Column(name = "PART_CODE")
    private String partCode;

    @Column(name = "Tag_Number")
    private String tagNumber;

    @Column(name = "SHORT_DESC")
    private String shortDesc;

    @Column(name = "UNIT_CODE")
    private String unitCode;

    @Column(name = "QUANTITY")
    private String quantity;

    @Column(name = "RESV_QTY")
    private String resvQty;

    @Column(name = "ISSUE_QTY")
    private String issueQty;

    @Column(name = "IDENT")
    private String ident;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "LP_ID")
    private String id;

    @Column(name = "LAST_LP_ID")
    private String lastLpId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LST_DATE")
    private Date lstDate;

    @Column(name = "LMOD")
    private String lmod;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CONTROL_DATE")
    private Date controlDate;

    @Column(name = "ln_id")
    private String lnId;

    @Column(name = "bompath")
    private String bompath;

    public ViewMxjBominfoEntity() {

    }

    public String getLpPos() {
        return lpPos;
    }

    public void setLpPos(String lpPos) {
        this.lpPos = lpPos;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getCgGroupCode() {
        return cgGroupCode;
    }

    public void setCgGroupCode(String cgGroupCode) {
        this.cgGroupCode = cgGroupCode;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getResvQty() {
        return resvQty;
    }

    public void setResvQty(String resvQty) {
        this.resvQty = resvQty;
    }

    public String getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(String issueQty) {
        this.issueQty = issueQty;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getLastLpId() {
        return lastLpId;
    }

    public void setLastLpId(String lastLpId) {
        this.lastLpId = lastLpId;
    }

    public Date getLstDate() {
        return lstDate;
    }

    public void setLstDate(Date lstDate) {
        this.lstDate = lstDate;
    }

    public String getLmod() {
        return lmod;
    }

    public void setLmod(String lmod) {
        this.lmod = lmod;
    }

    public Date getControlDate() {
        return controlDate;
    }

    public void setControlDate(Date controlDate) {
        this.controlDate = controlDate;
    }

    public String getLnId() {
        return lnId;
    }

    public void setLnId(String lnId) {
        this.lnId = lnId;
    }

    public String getBompath() {
        return bompath;
    }

    public void setBompath(String bompath) {
        this.bompath = bompath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
