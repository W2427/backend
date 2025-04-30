package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * BOM结构节点表
 */
@Entity
@Table(name = "m_list_nodes")
@NamedQuery(name = "ListNodesEntity.findAll", query = "SELECT a FROM ListNodesEntity a")
public class ListNodesEntity extends BaseDTO {

    private static final long serialVersionUID = 6801140138592407316L;

    @Id
    @Column(name = "ln_id", nullable = false)
    private Integer lnId;

    @Column(name = "ls_id", nullable = false)
    private Integer lsId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "control_status", nullable = false)
    private Integer controlStatus = 1;

    @Column(name = "ln_code", nullable = false)
    private String lnCode;

    @Column(name = "revision_id1", nullable = false)
    private Integer revisionId1;

    @Column(name = "revision_id2", nullable = false)
    private Integer revisionId2;

    @Column(name = "addend", nullable = false)
    private Double addend = 0.0;

    @Column(name = "factor", nullable = false)
    private Double factor = 1.0;

    @Column(name = "control_date", nullable = false)
    private Date controlDate;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "released_ind", nullable = false)
    private String releasedInd = "N";

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "object_code")
    private String objectCode;

    @Column(name = "load_ind", nullable = false)
    private String loadInd;

    @Column(name = "parent_ln_id")
    private Integer parentLnId;

    @Column(name = "generated_name")
    private String generatedName;

    @Column(name = "mar_error_code")
    private String marErrorCode;

    @Column(name = "fah_order_seq")
    private Integer fahOrderSeq;

    @Column(name = "fah_id")
    private Integer fahId;

    @Column(name = "fah_prio")
    private Integer fahPrio;

    @Column(name = "issue_progress", nullable = false)
    private String issueProgress = "N";

    @Column(name = "lock_ind", nullable = false)
    private String lockInd = "N";

    @Column(name = "spf_uid")
    private String spfUid;

    @Column(name = "site_stat_id")
    private Integer siteStatId;

    @Column(name = "complete", nullable = false)
    private String complete = "N";

    @Column(name = "est_short_node_ind", nullable = false)
    private String estShortNodeInd = "N";

    @Column(name = "stat_id")
    private Integer statId;

    public Integer getLnId() {
        return lnId;
    }

    public void setLnId(Integer lnId) {
        this.lnId = lnId;
    }

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

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getControlStatus() {
        return controlStatus;
    }

    public void setControlStatus(Integer controlStatus) {
        this.controlStatus = controlStatus;
    }

    public String getLnCode() {
        return lnCode;
    }

    public void setLnCode(String lnCode) {
        this.lnCode = lnCode;
    }

    public Integer getRevisionId1() {
        return revisionId1;
    }

    public void setRevisionId1(Integer revisionId1) {
        this.revisionId1 = revisionId1;
    }

    public Integer getRevisionId2() {
        return revisionId2;
    }

    public void setRevisionId2(Integer revisionId2) {
        this.revisionId2 = revisionId2;
    }

    public Double getAddend() {
        return addend;
    }

    public void setAddend(Double addend) {
        this.addend = addend;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public Date getControlDate() {
        return controlDate;
    }

    public void setControlDate(Date controlDate) {
        this.controlDate = controlDate;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getReleasedInd() {
        return releasedInd;
    }

    public void setReleasedInd(String releasedInd) {
        this.releasedInd = releasedInd;
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

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getLoadInd() {
        return loadInd;
    }

    public void setLoadInd(String loadInd) {
        this.loadInd = loadInd;
    }

    public Integer getParentLnId() {
        return parentLnId;
    }

    public void setParentLnId(Integer parentLnId) {
        this.parentLnId = parentLnId;
    }

    public String getGeneratedName() {
        return generatedName;
    }

    public void setGeneratedName(String generatedName) {
        this.generatedName = generatedName;
    }

    public String getMarErrorCode() {
        return marErrorCode;
    }

    public void setMarErrorCode(String marErrorCode) {
        this.marErrorCode = marErrorCode;
    }

    public Integer getFahOrderSeq() {
        return fahOrderSeq;
    }

    public void setFahOrderSeq(Integer fahOrderSeq) {
        this.fahOrderSeq = fahOrderSeq;
    }

    public Integer getFahId() {
        return fahId;
    }

    public void setFahId(Integer fahId) {
        this.fahId = fahId;
    }

    public Integer getFahPrio() {
        return fahPrio;
    }

    public void setFahPrio(Integer fahPrio) {
        this.fahPrio = fahPrio;
    }

    public String getIssueProgress() {
        return issueProgress;
    }

    public void setIssueProgress(String issueProgress) {
        this.issueProgress = issueProgress;
    }

    public String getLockInd() {
        return lockInd;
    }

    public void setLockInd(String lockInd) {
        this.lockInd = lockInd;
    }

    public String getSpfUid() {
        return spfUid;
    }

    public void setSpfUid(String spfUid) {
        this.spfUid = spfUid;
    }

    public Integer getSiteStatId() {
        return siteStatId;
    }

    public void setSiteStatId(Integer siteStatId) {
        this.siteStatId = siteStatId;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getEstShortNodeInd() {
        return estShortNodeInd;
    }

    public void setEstShortNodeInd(String estShortNodeInd) {
        this.estShortNodeInd = estShortNodeInd;
    }

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }
}
