package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "W_MIR_ITEMS")
@NamedQuery(name = "WMirItemEntity.findAll", query = "SELECT a FROM WMirItemEntity a")
public class WMirItemEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "IVPR_ID")
    private Integer ivprId;

    @Column(name = "IDENT")
    private int ident;

    @Column(name = "ESI_STATUS")
    private String esiStatus;

    @Column(name = "IVI_ID")
    private Integer iviId;

    @Column(name = "MIR_ID")
    private Integer mirId;

    @Column(name = "LP_ID")
    private Integer lpId;

    @Column(name = "ISSUE_QTY", precision = 15, scale = 3)
    private BigDecimal issueQty;

    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Column(name = "WH_ID")
    private Integer whId;

    @Column(name = "LOC_ID")
    private Integer locId;

    @Column(name = "SMST_ID")
    private Integer smstId;

    @Column(name = "UNIT_ID")
    private Integer unitId;

    @Column(name = "TAG_NUMBER")
    private String tagNumber;

    @Column(name = "HEAT_ID")
    private Integer heatId;

    @Column(name = "PLATE_ID")
    private Integer plateId;

    @Column(name = "IDENT_DEVIATION")
    private String identDeviation;

    @Column(name = "SAS_ID")
    private Integer sasId;

    @Column(name = "SITE_STAT_IND")
    private String siteStatInd;

    @Column(name = "MIR_NUMBER")
    private String mirNumber;

    @Column(name = "PROJ_ID")
    private String projId;

    public WMirItemEntity() {

    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public Integer getIviId() {
        return iviId;
    }

    public void setIviId(Integer iviId) {
        this.iviId = iviId;
    }

    public Integer getMirId() {
        return mirId;
    }

    public void setMirId(Integer mirId) {
        this.mirId = mirId;
    }

    public Integer getLpId() {
        return lpId;
    }

    public void setLpId(Integer lpId) {
        this.lpId = lpId;
    }

    public Integer getIvprId() {
        return ivprId;
    }

    public void setIvprId(Integer ivprId) {
        this.ivprId = ivprId;
    }

    public BigDecimal getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(BigDecimal issueQty) {
        this.issueQty = issueQty;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public Integer getSmstId() {
        return smstId;
    }

    public void setSmstId(Integer smstId) {
        this.smstId = smstId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Integer getHeatId() {
        return heatId;
    }

    public void setHeatId(Integer heatId) {
        this.heatId = heatId;
    }

    public Integer getPlateId() {
        return plateId;
    }

    public void setPlateId(Integer plateId) {
        this.plateId = plateId;
    }

    public String getIdentDeviation() {
        return identDeviation;
    }

    public void setIdentDeviation(String identDeviation) {
        this.identDeviation = identDeviation;
    }

    public Integer getSasId() {
        return sasId;
    }

    public void setSasId(Integer sasId) {
        this.sasId = sasId;
    }

    public String getSiteStatInd() {
        return siteStatInd;
    }

    public void setSiteStatInd(String siteStatInd) {
        this.siteStatInd = siteStatInd;
    }

    public String getMirNumber() {
        return mirNumber;
    }

    public void setMirNumber(String mirNumber) {
        this.mirNumber = mirNumber;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }
}
