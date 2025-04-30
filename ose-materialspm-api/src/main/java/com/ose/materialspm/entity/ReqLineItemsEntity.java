package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 请购单详情表
 */
@Entity
@Table(name = "m_req_line_items")
@NamedQuery(name = "ReqLineItemsEntity.findAll", query = "SELECT a FROM ReqLineItemsEntity a")
public class ReqLineItemsEntity extends BaseDTO {

    private static final long serialVersionUID = -1616087590286800155L;
    @Id
    @Column(name = "rli_id", nullable = false)
    private Integer rliId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "r_id", nullable = false)
    private Integer rId;

    @Column(name = "rli_pos", nullable = false)
    private Integer rliPos;

    @Column(name = "rli_sub_pos", nullable = false)
    private Integer rliSubPos = 1;

    @Column(name = "revision_id", nullable = false)
    private Integer revisionId = 0;

    @Column(name = "revision_type", nullable = false)
    private String revisionType = "O";

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "currency_id", nullable = false)
    private Integer currencyId;

    @Column(name = "budget", nullable = false)
    private Double budget = 0.0;

    @Column(name = "increment_release_qty", nullable = false)
    private Double incrementReleaseQty = 0.0;

    @Column(name = "total_release_qty", nullable = false)
    private Double totalReleaseQty = 0.0;

    @Column(name = "last_total_release_qty", nullable = false)
    private Double lastTotalReleaseQty = 0.0;

    @Column(name = "total_list_qty", nullable = false)
    private Double totalListQty = 0.0;

    @Column(name = "last_total_list_qty", nullable = false)
    private Double lastTotalListQty = 0.0;

    @Column(name = "recommend_qty", nullable = false)
    private Double recommendQty = 0.0;

    @Column(name = "assembly_qty", nullable = false)
    private Double assemblyQty = 0.0;

    @Column(name = "inquiry_qty", nullable = false)
    private Double inquiryQty = 0.0;

    @Column(name = "po_qty", nullable = false)
    private Double poQty = 0.0;

    @Column(name = "po_maintain_qty", nullable = false)
    private Double poMaintainQty = 0.0;

    @Column(name = "rli_unit_weight", nullable = false)
    private Double rliUnitWeight = 0.0;

    @Column(name = "tech_eval_required_ind", nullable = false)
    private String techEvalRequiredInd = "N";

    @Column(name = "allow_rtp_ind", nullable = false)
    private String allowRtpInd = "Y";

    @Column(name = "activ_ind", nullable = false)
    private String activInd = "Y";

    @Column(name = "released_ind", nullable = false)
    private String releasedInd = "N";

    @Column(name = "force_rtp_ind", nullable = false)
    private String forceRtpInd = "N";

    @Column(name = "manual_ind", nullable = false)
    private String manualInd = "Y";

    @Column(name = "marking_for_delete", nullable = false)
    private String markingForDelete = "N";

    @Column(name = "tag_number", nullable = false)
    private String tagNumber = "---";

    @Column(name = "attr_sum1", nullable = false)
    private Double attrSum1 = 0.0;

    @Column(name = "attr_sum2", nullable = false)
    private Double attrSum2 = 0.0;

    @Column(name = "attr_sum3", nullable = false)
    private Double attrSum3 = 0.0;

    @Column(name = "attr_group1", nullable = false)
    private String attrGroup1 = "0";

    @Column(name = "attr_group2", nullable = false)
    private String attrGroup2 = "0";

    @Column(name = "attr_group3", nullable = false)
    private String attrGroup3 = "0";

    @Column(name = "attr_group4", nullable = false)
    private String attrGroup4 = "0";

    @Column(name = "attr_group5", nullable = false)
    private String attrGroup5 = "0";

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "incl_man_qty", nullable = false)
    private Double inclManQty = 0.0;

    @Column(name = "keep_man_ind", nullable = false)
    private String keepManInd = "N";

    @Column(name = "ident")
    private Integer ident;

    @Column(name = "last_rli_id")
    private Integer lastRliId;

    @Column(name = "parent_rli_id")
    private Integer parentRliId;

    @Column(name = "qty_unit_id")
    private Integer qtyUnitId;

    @Column(name = "release_qty_unit_id")
    private Integer releaseQtyUnitId;

    @Column(name = "weight_unit_id")
    private Integer weightUnitId;

    @Column(name = "ref_mat_weight_unit_id")
    private Integer refMatWeightUnitId;

    @Column(name = "rst_id")
    private Integer rstId;

    @Column(name = "rbc_id")
    private Integer rbcId;

    @Column(name = "ity_id")
    private Integer ityId;

    @Column(name = "inq_id")
    private Integer inqId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "dd_id")
    private Integer ddId;

    @Column(name = "refer_rli_id")
    private Integer referRliId;

    @Column(name = "inq_rli_id")
    private Integer inqRliId;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "qd_id")
    private Integer qdId;

    @Column(name = "estimated_ros_date")
    private Date estimatedRosDate;

    @Column(name = "revision_date")
    private Date revisionDate;

    @Column(name = "procure_channel")
    private String procureChannel;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "req_progress_id")
    private Integer reqProgressId;

    @Column(name = "req_progress_comment")
    private String reqProgressComment;

    @Column(name = "rli_comment")
    private String rliComment;

    @Column(name = "mscm_comment")
    private String mscmComment;

    @Column(name = "ref_mat_weight")
    private Double refMatWeight = 0.0;

    @Column(name = "non_id_collapse_crit")
    private String nonIdCollapseCrit;

    @Column(name = "inq_order_seq")
    private Integer inqOrderSeq;

    @Column(name = "proc_order_seq")
    private Integer procOrderSeq;

    @Column(name = "where_condition_based")
    private Integer whereConditionBased;

    @Column(name = "forced_req")
    private Integer forcedReq;

    @Column(name = "rental_start_date")
    private Date rentalStartDate;

    @Column(name = "rental_end_date")
    private Date rentalEndDate;

    @Column(name = "interval")
    private String interval;

    @Column(name = "valid_period")
    private Integer validPeriod;

    public Integer getRliId() {
        return rliId;
    }

    public void setRliId(Integer rliId) {
        this.rliId = rliId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public Integer getRliPos() {
        return rliPos;
    }

    public void setRliPos(Integer rliPos) {
        this.rliPos = rliPos;
    }

    public Integer getRliSubPos() {
        return rliSubPos;
    }

    public void setRliSubPos(Integer rliSubPos) {
        this.rliSubPos = rliSubPos;
    }

    public Integer getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Integer revisionId) {
        this.revisionId = revisionId;
    }

    public String getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(String revisionType) {
        this.revisionType = revisionType;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getIncrementReleaseQty() {
        return incrementReleaseQty;
    }

    public void setIncrementReleaseQty(Double incrementReleaseQty) {
        this.incrementReleaseQty = incrementReleaseQty;
    }

    public Double getTotalReleaseQty() {
        return totalReleaseQty;
    }

    public void setTotalReleaseQty(Double totalReleaseQty) {
        this.totalReleaseQty = totalReleaseQty;
    }

    public Double getLastTotalReleaseQty() {
        return lastTotalReleaseQty;
    }

    public void setLastTotalReleaseQty(Double lastTotalReleaseQty) {
        this.lastTotalReleaseQty = lastTotalReleaseQty;
    }

    public Double getTotalListQty() {
        return totalListQty;
    }

    public void setTotalListQty(Double totalListQty) {
        this.totalListQty = totalListQty;
    }

    public Double getLastTotalListQty() {
        return lastTotalListQty;
    }

    public void setLastTotalListQty(Double lastTotalListQty) {
        this.lastTotalListQty = lastTotalListQty;
    }

    public Double getRecommendQty() {
        return recommendQty;
    }

    public void setRecommendQty(Double recommendQty) {
        this.recommendQty = recommendQty;
    }

    public Double getAssemblyQty() {
        return assemblyQty;
    }

    public void setAssemblyQty(Double assemblyQty) {
        this.assemblyQty = assemblyQty;
    }

    public Double getInquiryQty() {
        return inquiryQty;
    }

    public void setInquiryQty(Double inquiryQty) {
        this.inquiryQty = inquiryQty;
    }

    public Double getPoQty() {
        return poQty;
    }

    public void setPoQty(Double poQty) {
        this.poQty = poQty;
    }

    public Double getPoMaintainQty() {
        return poMaintainQty;
    }

    public void setPoMaintainQty(Double poMaintainQty) {
        this.poMaintainQty = poMaintainQty;
    }

    public Double getRliUnitWeight() {
        return rliUnitWeight;
    }

    public void setRliUnitWeight(Double rliUnitWeight) {
        this.rliUnitWeight = rliUnitWeight;
    }

    public String getTechEvalRequiredInd() {
        return techEvalRequiredInd;
    }

    public void setTechEvalRequiredInd(String techEvalRequiredInd) {
        this.techEvalRequiredInd = techEvalRequiredInd;
    }

    public String getAllowRtpInd() {
        return allowRtpInd;
    }

    public void setAllowRtpInd(String allowRtpInd) {
        this.allowRtpInd = allowRtpInd;
    }

    public String getActivInd() {
        return activInd;
    }

    public void setActivInd(String activInd) {
        this.activInd = activInd;
    }

    public String getReleasedInd() {
        return releasedInd;
    }

    public void setReleasedInd(String releasedInd) {
        this.releasedInd = releasedInd;
    }

    public String getForceRtpInd() {
        return forceRtpInd;
    }

    public void setForceRtpInd(String forceRtpInd) {
        this.forceRtpInd = forceRtpInd;
    }

    public String getManualInd() {
        return manualInd;
    }

    public void setManualInd(String manualInd) {
        this.manualInd = manualInd;
    }

    public String getMarkingForDelete() {
        return markingForDelete;
    }

    public void setMarkingForDelete(String markingForDelete) {
        this.markingForDelete = markingForDelete;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Double getAttrSum1() {
        return attrSum1;
    }

    public void setAttrSum1(Double attrSum1) {
        this.attrSum1 = attrSum1;
    }

    public Double getAttrSum2() {
        return attrSum2;
    }

    public void setAttrSum2(Double attrSum2) {
        this.attrSum2 = attrSum2;
    }

    public Double getAttrSum3() {
        return attrSum3;
    }

    public void setAttrSum3(Double attrSum3) {
        this.attrSum3 = attrSum3;
    }

    public String getAttrGroup1() {
        return attrGroup1;
    }

    public void setAttrGroup1(String attrGroup1) {
        this.attrGroup1 = attrGroup1;
    }

    public String getAttrGroup2() {
        return attrGroup2;
    }

    public void setAttrGroup2(String attrGroup2) {
        this.attrGroup2 = attrGroup2;
    }

    public String getAttrGroup3() {
        return attrGroup3;
    }

    public void setAttrGroup3(String attrGroup3) {
        this.attrGroup3 = attrGroup3;
    }

    public String getAttrGroup4() {
        return attrGroup4;
    }

    public void setAttrGroup4(String attrGroup4) {
        this.attrGroup4 = attrGroup4;
    }

    public String getAttrGroup5() {
        return attrGroup5;
    }

    public void setAttrGroup5(String attrGroup5) {
        this.attrGroup5 = attrGroup5;
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

    public Double getInclManQty() {
        return inclManQty;
    }

    public void setInclManQty(Double inclManQty) {
        this.inclManQty = inclManQty;
    }

    public String getKeepManInd() {
        return keepManInd;
    }

    public void setKeepManInd(String keepManInd) {
        this.keepManInd = keepManInd;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public Integer getLastRliId() {
        return lastRliId;
    }

    public void setLastRliId(Integer lastRliId) {
        this.lastRliId = lastRliId;
    }

    public Integer getParentRliId() {
        return parentRliId;
    }

    public void setParentRliId(Integer parentRliId) {
        this.parentRliId = parentRliId;
    }

    public Integer getQtyUnitId() {
        return qtyUnitId;
    }

    public void setQtyUnitId(Integer qtyUnitId) {
        this.qtyUnitId = qtyUnitId;
    }

    public Integer getReleaseQtyUnitId() {
        return releaseQtyUnitId;
    }

    public void setReleaseQtyUnitId(Integer releaseQtyUnitId) {
        this.releaseQtyUnitId = releaseQtyUnitId;
    }

    public Integer getWeightUnitId() {
        return weightUnitId;
    }

    public void setWeightUnitId(Integer weightUnitId) {
        this.weightUnitId = weightUnitId;
    }

    public Integer getRefMatWeightUnitId() {
        return refMatWeightUnitId;
    }

    public void setRefMatWeightUnitId(Integer refMatWeightUnitId) {
        this.refMatWeightUnitId = refMatWeightUnitId;
    }

    public Integer getRstId() {
        return rstId;
    }

    public void setRstId(Integer rstId) {
        this.rstId = rstId;
    }

    public Integer getRbcId() {
        return rbcId;
    }

    public void setRbcId(Integer rbcId) {
        this.rbcId = rbcId;
    }

    public Integer getItyId() {
        return ityId;
    }

    public void setItyId(Integer ityId) {
        this.ityId = ityId;
    }

    public Integer getInqId() {
        return inqId;
    }

    public void setInqId(Integer inqId) {
        this.inqId = inqId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getDdId() {
        return ddId;
    }

    public void setDdId(Integer ddId) {
        this.ddId = ddId;
    }

    public Integer getReferRliId() {
        return referRliId;
    }

    public void setReferRliId(Integer referRliId) {
        this.referRliId = referRliId;
    }

    public Integer getInqRliId() {
        return inqRliId;
    }

    public void setInqRliId(Integer inqRliId) {
        this.inqRliId = inqRliId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getQdId() {
        return qdId;
    }

    public void setQdId(Integer qdId) {
        this.qdId = qdId;
    }

    public Date getEstimatedRosDate() {
        return estimatedRosDate;
    }

    public void setEstimatedRosDate(Date estimatedRosDate) {
        this.estimatedRosDate = estimatedRosDate;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getProcureChannel() {
        return procureChannel;
    }

    public void setProcureChannel(String procureChannel) {
        this.procureChannel = procureChannel;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public Integer getReqProgressId() {
        return reqProgressId;
    }

    public void setReqProgressId(Integer reqProgressId) {
        this.reqProgressId = reqProgressId;
    }

    public String getReqProgressComment() {
        return reqProgressComment;
    }

    public void setReqProgressComment(String reqProgressComment) {
        this.reqProgressComment = reqProgressComment;
    }

    public String getRliComment() {
        return rliComment;
    }

    public void setRliComment(String rliComment) {
        this.rliComment = rliComment;
    }

    public String getMscmComment() {
        return mscmComment;
    }

    public void setMscmComment(String mscmComment) {
        this.mscmComment = mscmComment;
    }

    public Double getRefMatWeight() {
        return refMatWeight;
    }

    public void setRefMatWeight(Double refMatWeight) {
        this.refMatWeight = refMatWeight;
    }

    public String getNonIdCollapseCrit() {
        return nonIdCollapseCrit;
    }

    public void setNonIdCollapseCrit(String nonIdCollapseCrit) {
        this.nonIdCollapseCrit = nonIdCollapseCrit;
    }

    public Integer getInqOrderSeq() {
        return inqOrderSeq;
    }

    public void setInqOrderSeq(Integer inqOrderSeq) {
        this.inqOrderSeq = inqOrderSeq;
    }

    public Integer getProcOrderSeq() {
        return procOrderSeq;
    }

    public void setProcOrderSeq(Integer procOrderSeq) {
        this.procOrderSeq = procOrderSeq;
    }

    public Integer getWhereConditionBased() {
        return whereConditionBased;
    }

    public void setWhereConditionBased(Integer whereConditionBased) {
        this.whereConditionBased = whereConditionBased;
    }

    public Integer getForcedReq() {
        return forcedReq;
    }

    public void setForcedReq(Integer forcedReq) {
        this.forcedReq = forcedReq;
    }

    public Date getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(Date rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public Date getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(Date rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Integer getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(Integer validPeriod) {
        this.validPeriod = validPeriod;
    }
}
