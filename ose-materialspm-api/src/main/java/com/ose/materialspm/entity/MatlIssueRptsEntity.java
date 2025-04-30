package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 仓库和出库单关联表
 */
@Entity
@Table(name = "m_matl_issue_rpts")
@NamedQuery(name = "MatlIssueRptsEntity.findAll", query = "SELECT a FROM MatlIssueRptsEntity a")
public class MatlIssueRptsEntity extends BaseDTO {

    private static final long serialVersionUID = 5794778774216388184L;
    @Id
    @Column(name = "mir_id", nullable = false)
    private Integer mirId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "mir_number", nullable = false)
    private String mirNumber;

    @Column(name = "revision_id", nullable = false)
    private Integer revisionId;

    @Column(name = "mir_create_date", nullable = false)
    private Date mirCreateDate;

    @Column(name = "mir_type", nullable = false)
    private String mirType;

    @Column(name = "issue_type", nullable = false)
    private String issueType;

    @Column(name = "remove_tag_ind", nullable = false)
    private String removeTagInd;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "fah_id")
    private Integer fahId;

    @Column(name = "posted_date")
    private Date postedDate;

    @Column(name = "issue_date")
    private Date issueDate;

    @Column(name = "issue_by")
    private String issueBy;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "bnl_id")
    private Integer bnlId;

    @Column(name = "wh_id")
    private Integer whId;

    @Column(name = "loc_id")
    private Integer locId;

    @Column(name = "scrap_wh_id")
    private Integer scrapWhId;

    @Column(name = "direct_issue_type")
    private String directIssueType;

    @Column(name = "commodity_id")
    private Integer commodityId;

    @Column(name = "ident")
    private Integer ident;

    @Column(name = "tag_number")
    private String tagNumber;

    @Column(name = "to_project")
    private String toProject;

    @Column(name = "ready_for_approval_ind", nullable = false)
    private String readyForApprovalInd;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "srr_id")
    private Integer srrId;

    @Column(name = "mtr_id")
    private Integer mtrId;

    public Integer getMirId() {
        return mirId;
    }

    public void setMirId(Integer mirId) {
        this.mirId = mirId;
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

    public String getMirNumber() {
        return mirNumber;
    }

    public void setMirNumber(String mirNumber) {
        this.mirNumber = mirNumber;
    }

    public Integer getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Integer revisionId) {
        this.revisionId = revisionId;
    }

    public Date getMirCreateDate() {
        return mirCreateDate;
    }

    public void setMirCreateDate(Date mirCreateDate) {
        this.mirCreateDate = mirCreateDate;
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

    public String getRemoveTagInd() {
        return removeTagInd;
    }

    public void setRemoveTagInd(String removeTagInd) {
        this.removeTagInd = removeTagInd;
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

    public Integer getFahId() {
        return fahId;
    }

    public void setFahId(Integer fahId) {
        this.fahId = fahId;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
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

    public Integer getBnlId() {
        return bnlId;
    }

    public void setBnlId(Integer bnlId) {
        this.bnlId = bnlId;
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

    public Integer getScrapWhId() {
        return scrapWhId;
    }

    public void setScrapWhId(Integer scrapWhId) {
        this.scrapWhId = scrapWhId;
    }

    public String getDirectIssueType() {
        return directIssueType;
    }

    public void setDirectIssueType(String directIssueType) {
        this.directIssueType = directIssueType;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
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

    public String getToProject() {
        return toProject;
    }

    public void setToProject(String toProject) {
        this.toProject = toProject;
    }

    public String getReadyForApprovalInd() {
        return readyForApprovalInd;
    }

    public void setReadyForApprovalInd(String readyForApprovalInd) {
        this.readyForApprovalInd = readyForApprovalInd;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getSrrId() {
        return srrId;
    }

    public void setSrrId(Integer srrId) {
        this.srrId = srrId;
    }

    public Integer getMtrId() {
        return mtrId;
    }

    public void setMtrId(Integer mtrId) {
        this.mtrId = mtrId;
    }
}
