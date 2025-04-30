package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "W_MIR")
@NamedQuery(name = "WMirEntity.findAll", query = "SELECT a FROM WMirEntity a")
public class WMirEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "FAH_ID")
    private Integer fahId;

    @Column(name = "MIR_NUMBER")
    private String mirNumber;

    @Column(name = "ESI_STATUS")
    private String esiStatus;

    @Column(name = "REVISION_ID")
    private Integer revisionId;

    @Column(name = "MIR_CREATE_DATE")
    private Date mirCreateDate;

    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Column(name = "ISSUE_BY")
    private String issueBy;

    @Column(name = "COMPANY_ID")
    private Integer companyId;

    @Column(name = "WH_ID")
    private Integer whId;

    @Column(name = "LOC_ID")
    private Integer locId;

    @Column(name = "MIR_TYPE")
    private String mirType;

    @Column(name = "ISSUE_TYPE")
    private String issueType;

    @Column(name = "POPL_ISS_BY_PROC")
    private String poplIssByProc;

    @Column(name = "SITE_STAT_IND")
    private String siteStatInd;

    @Column(name = "BNL_ID")
    private Integer bnlId;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "SHORT_DESC")
    private String shortDesc;

    @Column(name = "DESCRIPTION")
    private String description;

    public WMirEntity() {

    }

    public String getMirNumber() {
        return mirNumber;
    }

    public void setMirNumber(String mirNumber) {
        this.mirNumber = mirNumber;
    }

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public Integer getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Integer revisionId) {
        this.revisionId = revisionId;
    }

    public Integer getFahId() {
        return fahId;
    }

    public void setFahId(Integer fahId) {
        this.fahId = fahId;
    }

    public Date getMirCreateDate() {
        return mirCreateDate;
    }

    public void setMirCreateDate(Date mirCreateDate) {
        this.mirCreateDate = mirCreateDate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getMirType() {
        return mirType;
    }

    public void setMirType(String mirType) {
        this.mirType = mirType;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getPoplIssByProc() {
        return poplIssByProc;
    }

    public void setPoplIssByProc(String poplIssByProc) {
        this.poplIssByProc = poplIssByProc;
    }

    public String getSiteStatInd() {
        return siteStatInd;
    }

    public void setSiteStatInd(String siteStatInd) {
        this.siteStatInd = siteStatInd;
    }

    public Integer getBnlId() {
        return bnlId;
    }

    public void setBnlId(Integer bnlId) {
        this.bnlId = bnlId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
