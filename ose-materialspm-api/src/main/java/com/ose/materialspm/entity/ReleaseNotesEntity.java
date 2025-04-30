package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 放行单表
 */
@Entity
@Table(name = "m_release_notes")
@NamedQuery(name = "ReleaseNotesEntity.findAll", query = "SELECT a FROM ReleaseNotesEntity a")
public class ReleaseNotesEntity extends BaseDTO {

    private static final long serialVersionUID = 8857643845015113609L;
    @Id
    @Column(name = "reln_id", nullable = false)
    private Integer relnId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "reln_number", nullable = false)
    private String relnNumber;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "poh_id", nullable = false)
    private Integer pohId;

    @Column(name = "weight_unit_id", nullable = false)
    private Integer weightUnitId;

    @Column(name = "rm_id", nullable = false)
    private Integer rmId;

    @Column(name = "dd_id", nullable = false)
    private Integer ddId;

    @Column(name = "frt_id", nullable = false)
    private Integer frtId;

    @Column(name = "reln_date", nullable = false)
    private Date relnDate;

    @Column(name = "delv_date", nullable = false)
    private Date delvDate;

    @Column(name = "count_revision", nullable = false)
    private Integer countRevision = 0;

    @Column(name = "freight_value", nullable = false)
    private String freightValue;

    @Column(name = "selling_price", nullable = false)
    private Double sellingPrice = 0.0;

    @Column(name = "free_of_charge_ind", nullable = false)
    private String freeOfChargeInd = "N";

    @Column(name = "erp_set_ind", nullable = false)
    private String erpSetInd = "N";

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "eml_id")
    private Integer emlId;

    @Column(name = "moc_id")
    private Integer mocId;

    @Column(name = "currency_id")
    private Integer currencyId;

    @Column(name = "collection_point")
    private String collectionPoint;

    @Column(name = "ready_date")
    private Date readyDate;

    @Column(name = "rel_to_forwarder_date")
    private Date relToForwarderDate;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_date")
    private Date approvedDate;

    @Column(name = "submitted_by")
    private String submittedBy;

    @Column(name = "submitted_date")
    private Date submittedDate;

    @Column(name = "pck_approved_by")
    private String pckApprovedBy;

    @Column(name = "pck_approved_date")
    private Date pckApprovedDate;

    @Column(name = "coll_point_contact_person")
    private String collPointContactPerson;

    @Column(name = "coll_point_telephone")
    private String collPointTelephone;

    @Column(name = "coll_point_email")
    private String collPointEmail;

    @Column(name = "coll_point_address")
    private String collPointAddress;

    @Column(name = "coll_point_internal_ref")
    private String collPointInternalRef;

    @Column(name = "target_on_site_date")
    private Date targetOnSiteDate;

    @Column(name = "tc_comments")
    private String tcComments;

    @Column(name = "tc_req_site_date")
    private Date tcReqSiteDate;

    @Column(name = "pck_approve_reject_remarks")
    private String pckApproveRejectRemarks;

    @Column(name = "pck_rejected_date")
    private Date pckRejectedDate;

    @Column(name = "pck_rejected_by")
    private String pckRejectedBy;

    @Column(name = "tc_edit_ind", nullable = false)
    private String tcEditInd = "N";

    @Column(name = "out_of_gauge_ind", nullable = false)
    private String outOfGaugeInd = "N";

    @Column(name = "cont_defined_at_reln")
    private String contDefinedAtReln;

    @Column(name = "tcnt_id")
    private Integer tcntId;

    @Column(name = "shl_id")
    private Integer shlId;

    @Column(name = "reln_comment")
    private String relnComment;

    @Column(name = "as_id")
    private Integer asId;

    @Column(name = "ttrk_id")
    private Integer ttrkId;

    @Column(name = "trk_defined_at_reln")
    private String trkDefinedAtReln;

    public Integer getRelnId() {
        return relnId;
    }

    public void setRelnId(Integer relnId) {
        this.relnId = relnId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getRelnNumber() {
        return relnNumber;
    }

    public void setRelnNumber(String relnNumber) {
        this.relnNumber = relnNumber;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getPohId() {
        return pohId;
    }

    public void setPohId(Integer pohId) {
        this.pohId = pohId;
    }

    public Integer getWeightUnitId() {
        return weightUnitId;
    }

    public void setWeightUnitId(Integer weightUnitId) {
        this.weightUnitId = weightUnitId;
    }

    public Integer getRmId() {
        return rmId;
    }

    public void setRmId(Integer rmId) {
        this.rmId = rmId;
    }

    public Integer getDdId() {
        return ddId;
    }

    public void setDdId(Integer ddId) {
        this.ddId = ddId;
    }

    public Integer getFrtId() {
        return frtId;
    }

    public void setFrtId(Integer frtId) {
        this.frtId = frtId;
    }

    public Date getRelnDate() {
        return relnDate;
    }

    public void setRelnDate(Date relnDate) {
        this.relnDate = relnDate;
    }

    public Date getDelvDate() {
        return delvDate;
    }

    public void setDelvDate(Date delvDate) {
        this.delvDate = delvDate;
    }

    public Integer getCountRevision() {
        return countRevision;
    }

    public void setCountRevision(Integer countRevision) {
        this.countRevision = countRevision;
    }

    public String getFreightValue() {
        return freightValue;
    }

    public void setFreightValue(String freightValue) {
        this.freightValue = freightValue;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getFreeOfChargeInd() {
        return freeOfChargeInd;
    }

    public void setFreeOfChargeInd(String freeOfChargeInd) {
        this.freeOfChargeInd = freeOfChargeInd;
    }

    public String getErpSetInd() {
        return erpSetInd;
    }

    public void setErpSetInd(String erpSetInd) {
        this.erpSetInd = erpSetInd;
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

    public Integer getEmlId() {
        return emlId;
    }

    public void setEmlId(Integer emlId) {
        this.emlId = emlId;
    }

    public Integer getMocId() {
        return mocId;
    }

    public void setMocId(Integer mocId) {
        this.mocId = mocId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCollectionPoint() {
        return collectionPoint;
    }

    public void setCollectionPoint(String collectionPoint) {
        this.collectionPoint = collectionPoint;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public Date getRelToForwarderDate() {
        return relToForwarderDate;
    }

    public void setRelToForwarderDate(Date relToForwarderDate) {
        this.relToForwarderDate = relToForwarderDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getPckApprovedBy() {
        return pckApprovedBy;
    }

    public void setPckApprovedBy(String pckApprovedBy) {
        this.pckApprovedBy = pckApprovedBy;
    }

    public Date getPckApprovedDate() {
        return pckApprovedDate;
    }

    public void setPckApprovedDate(Date pckApprovedDate) {
        this.pckApprovedDate = pckApprovedDate;
    }

    public String getCollPointContactPerson() {
        return collPointContactPerson;
    }

    public void setCollPointContactPerson(String collPointContactPerson) {
        this.collPointContactPerson = collPointContactPerson;
    }

    public String getCollPointTelephone() {
        return collPointTelephone;
    }

    public void setCollPointTelephone(String collPointTelephone) {
        this.collPointTelephone = collPointTelephone;
    }

    public String getCollPointEmail() {
        return collPointEmail;
    }

    public void setCollPointEmail(String collPointEmail) {
        this.collPointEmail = collPointEmail;
    }

    public String getCollPointAddress() {
        return collPointAddress;
    }

    public void setCollPointAddress(String collPointAddress) {
        this.collPointAddress = collPointAddress;
    }

    public String getCollPointInternalRef() {
        return collPointInternalRef;
    }

    public void setCollPointInternalRef(String collPointInternalRef) {
        this.collPointInternalRef = collPointInternalRef;
    }

    public Date getTargetOnSiteDate() {
        return targetOnSiteDate;
    }

    public void setTargetOnSiteDate(Date targetOnSiteDate) {
        this.targetOnSiteDate = targetOnSiteDate;
    }

    public String getTcComments() {
        return tcComments;
    }

    public void setTcComments(String tcComments) {
        this.tcComments = tcComments;
    }

    public Date getTcReqSiteDate() {
        return tcReqSiteDate;
    }

    public void setTcReqSiteDate(Date tcReqSiteDate) {
        this.tcReqSiteDate = tcReqSiteDate;
    }

    public String getPckApproveRejectRemarks() {
        return pckApproveRejectRemarks;
    }

    public void setPckApproveRejectRemarks(String pckApproveRejectRemarks) {
        this.pckApproveRejectRemarks = pckApproveRejectRemarks;
    }

    public Date getPckRejectedDate() {
        return pckRejectedDate;
    }

    public void setPckRejectedDate(Date pckRejectedDate) {
        this.pckRejectedDate = pckRejectedDate;
    }

    public String getPckRejectedBy() {
        return pckRejectedBy;
    }

    public void setPckRejectedBy(String pckRejectedBy) {
        this.pckRejectedBy = pckRejectedBy;
    }

    public String getTcEditInd() {
        return tcEditInd;
    }

    public void setTcEditInd(String tcEditInd) {
        this.tcEditInd = tcEditInd;
    }

    public String getOutOfGaugeInd() {
        return outOfGaugeInd;
    }

    public void setOutOfGaugeInd(String outOfGaugeInd) {
        this.outOfGaugeInd = outOfGaugeInd;
    }

    public String getContDefinedAtReln() {
        return contDefinedAtReln;
    }

    public void setContDefinedAtReln(String contDefinedAtReln) {
        this.contDefinedAtReln = contDefinedAtReln;
    }

    public Integer getTcntId() {
        return tcntId;
    }

    public void setTcntId(Integer tcntId) {
        this.tcntId = tcntId;
    }

    public Integer getShlId() {
        return shlId;
    }

    public void setShlId(Integer shlId) {
        this.shlId = shlId;
    }

    public String getRelnComment() {
        return relnComment;
    }

    public void setRelnComment(String relnComment) {
        this.relnComment = relnComment;
    }

    public Integer getAsId() {
        return asId;
    }

    public void setAsId(Integer asId) {
        this.asId = asId;
    }

    public Integer getTtrkId() {
        return ttrkId;
    }

    public void setTtrkId(Integer ttrkId) {
        this.ttrkId = ttrkId;
    }

    public String getTrkDefinedAtReln() {
        return trkDefinedAtReln;
    }

    public void setTrkDefinedAtReln(String trkDefinedAtReln) {
        this.trkDefinedAtReln = trkDefinedAtReln;
    }
}
