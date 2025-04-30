package com.ose.materialspm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.ose.dto.BaseDTO;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "V_MXJ_VALID_ISSUE_REPT")
@NamedQuery(name = "VMxjValidIssueReptEntity.findAll", query = "SELECT a FROM VMxjValidIssueReptEntity a")
public class VMxjValidIssueReptEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "MIR_ID")
    private String mirId;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "MIR_DESC")
    private String mirDesc;

    @Column(name = "DP_CODE")
    private String dpCode;

    @Column(name = "MIR_NUMBER")
    private String mirNumber;

    @Column(name = "REVISION_ID")
    private String revisionId;

    @Column(name = "MIR_TYPE")
    private String mirType;

    @Column(name = "ISSUE_TYPE")
    private String issueType;

    @Column(name = "WH_CODE")
    private String whCode;

    @Column(name = "LOC_CODE")
    private String locCode;

    @Column(name = "FAH_ID")
    private String fahId;

    @Column(name = "FAH_CODE")
    private String fahCode;

    @Column(name = "RUN_NUMBER")
    private String runNumber;

    public VMxjValidIssueReptEntity() {

    }

    public String getMirId() {
        return mirId;
    }

    public void setMirId(String mirId) {
        this.mirId = mirId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getMirDesc() {
        return mirDesc;
    }

    public void setMirDesc(String mirDesc) {
        this.mirDesc = mirDesc;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public String getMirNumber() {
        return mirNumber;
    }

    public void setMirNumber(String mirNumber) {
        this.mirNumber = mirNumber;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
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

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getFahId() {
        return fahId;
    }

    public void setFahId(String fahId) {
        this.fahId = fahId;
    }

    public String getFahCode() {
        return fahCode;
    }

    public void setFahCode(String fahCode) {
        this.fahCode = fahCode;
    }

    public String getRunNumber() {
        return runNumber;
    }

    public void setRunNumber(String runNumber) {
        this.runNumber = runNumber;
    }

}
