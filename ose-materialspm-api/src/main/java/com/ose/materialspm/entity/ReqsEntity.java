package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 请购单表
 */
@Entity
@Table(name = "m_reqs")
@NamedQuery(name = "ReqsEntity.findAll", query = "SELECT a FROM ReqsEntity a")
public class ReqsEntity extends BaseDTO {

    private static final long serialVersionUID = 134952800512945005L;

    @Id
    @Column(name = "r_id", nullable = false)
    private Integer rId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "origin_id", nullable = false)
    private Integer originId;

    @Column(name = "r_code", nullable = false)
    private String rCode;

    @Column(name = "r_supp", nullable = false)
    private Integer rSupp = 0;

    @Column(name = "actual_ind", nullable = false)
    private String actualInd = "Y";

    @Column(name = "revision_id", nullable = false)
    private Integer revisionId = 0;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "mty_id", nullable = false)
    private Integer mtyId = 0;

    @Column(name = "rsd_id", nullable = false)
    private Integer rsdId = 0;

    @Column(name = "rtp_id", nullable = false)
    private Integer rtpId = 0;

    @Column(name = "rnd_id", nullable = false)
    private Integer rndId;

    @Column(name = "rst_id", nullable = false)
    private Integer rstId;

    @Column(name = "mj_id", nullable = false)
    private Integer mjId = 0;

    @Column(name = "currency_id", nullable = false)
    private Integer currencyId;

    @Column(name = "original_budget", nullable = false)
    private Double originalBudget = 0.0;

    @Column(name = "add_pos_allow_ind", nullable = false)
    private String addPosAllowInd = "Y";

    @Column(name = "req_candidate_ind", nullable = false)
    private String reqCandidateInd = "Y";

    @Column(name = "tech_eval_required_ind", nullable = false)
    private String techEvalRequiredInd = "N";

    @Column(name = "ready_for_approval_ind", nullable = false)
    private String readyForApprovalInd = "N";

    @Column(name = "inq_complete_ind", nullable = false)
    private String inqCompleteInd = "N";

    @Column(name = "po_complete_ind", nullable = false)
    private String poCompleteInd = "N";

    @Column(name = "general_cip_ind", nullable = false)
    private String generalCipInd = "N";

    @Column(name = "calc_account_ind", nullable = false)
    private String calcAccountInd = "N";

    @Column(name = "get_budget_ind", nullable = false)
    private String getBudgetInd = "N";

    @Column(name = "get_desig_ind", nullable = false)
    private String getDesigInd = "N";

    @Column(name = "fast_progress_ind", nullable = false)
    private String fastProgressInd = "N";

    @Column(name = "calc_ros_date_ind", nullable = false)
    private String calcRosDateInd = "N";

    @Column(name = "calc_weight_ind", nullable = false)
    private String calcWeightInd = "N";

    @Column(name = "handle_bom_doc_ind", nullable = false)
    private String handleBomDocInd = "N";

    @Column(name = "originator", nullable = false)
    private String originator;

    @Column(name = "release_context", nullable = false)
    private String releaseContext = "ALL";

    @Column(name = "r_type", nullable = false)
    private String rType = "O";

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "emts_id")
    private Integer emtsId;

    @Column(name = "rbc_id")
    private Integer rbcId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "aqc_id")
    private Integer aqcId;

    @Column(name = "mset_id")
    private Integer msetId;

    @Column(name = "mg_id")
    private Integer mgId;

    @Column(name = "rcat_id")
    private Integer rcatId;

    @Column(name = "last_r_id")
    private Integer lastRId;

    @Column(name = "sec_id")
    private Integer secId;

    @Column(name = "pty_id")
    private Integer ptyId;

    @Column(name = "exped_ilv_id")
    private Integer expedIlvId;

    @Column(name = "insp_ilv_id")
    private Integer inspIlvId;

    @Column(name = "crit_ilv_id")
    private Integer critIlvId;

    @Column(name = "balance_r_id")
    private Integer balanceRId;

    @Column(name = "buyer")
    private String buyer;

    @Column(name = "approver")
    private String approver;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_date")
    private Date approvedDate;

    @Column(name = "order_seq")
    private Integer orderSeq;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "order_sub_type")
    private String orderSubType;

    @Column(name = "rel_to_proc_date")
    private Date relToProcDate;

    @Column(name = "estimated_ros_date")
    private Date estimatedRosDate;

    @Column(name = "req_progress_id")
    private Integer reqProgressId;

    @Column(name = "req_progress_comment")
    private String reqProgressComment;

    @Column(name = "proc_acknowledge_date")
    private Date procAcknowledgeDate;

    @Column(name = "revision_date")
    private Date revisionDate;

    @Column(name = "mr_revision")
    private String mrRevision;

    @Column(name = "sr_revision")
    private String srRevision;

    @Column(name = "transfer_date")
    private Date transferDate;

    @Column(name = "mscm_comment")
    private String mscmComment;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "mr_rev_sch")
    private String mrRevSch;

    @Column(name = "sr_rev_sch")
    private String srRevSch;

    @Column(name = "keep_nls_desc_ind", nullable = false)
    private String keepNlsDescInd = "N";

    @Column(name = "rel_to_proc_by")
    private String relToProcBy;

    @Column(name = "erp_managed_ind", nullable = false)
    private String erpManagedInd = "N";

    @Column(name = "qs_id")
    private Integer qsId;

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public String getrCode() {
        return rCode;
    }

    public void setrCode(String rCode) {
        this.rCode = rCode;
    }

    public Integer getrSupp() {
        return rSupp;
    }

    public void setrSupp(Integer rSupp) {
        this.rSupp = rSupp;
    }

    public String getActualInd() {
        return actualInd;
    }

    public void setActualInd(String actualInd) {
        this.actualInd = actualInd;
    }

    public Integer getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Integer revisionId) {
        this.revisionId = revisionId;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getMtyId() {
        return mtyId;
    }

    public void setMtyId(Integer mtyId) {
        this.mtyId = mtyId;
    }

    public Integer getRsdId() {
        return rsdId;
    }

    public void setRsdId(Integer rsdId) {
        this.rsdId = rsdId;
    }

    public Integer getRtpId() {
        return rtpId;
    }

    public void setRtpId(Integer rtpId) {
        this.rtpId = rtpId;
    }

    public Integer getRndId() {
        return rndId;
    }

    public void setRndId(Integer rndId) {
        this.rndId = rndId;
    }

    public Integer getRstId() {
        return rstId;
    }

    public void setRstId(Integer rstId) {
        this.rstId = rstId;
    }

    public Integer getMjId() {
        return mjId;
    }

    public void setMjId(Integer mjId) {
        this.mjId = mjId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Double getOriginalBudget() {
        return originalBudget;
    }

    public void setOriginalBudget(Double originalBudget) {
        this.originalBudget = originalBudget;
    }

    public String getAddPosAllowInd() {
        return addPosAllowInd;
    }

    public void setAddPosAllowInd(String addPosAllowInd) {
        this.addPosAllowInd = addPosAllowInd;
    }

    public String getReqCandidateInd() {
        return reqCandidateInd;
    }

    public void setReqCandidateInd(String reqCandidateInd) {
        this.reqCandidateInd = reqCandidateInd;
    }

    public String getTechEvalRequiredInd() {
        return techEvalRequiredInd;
    }

    public void setTechEvalRequiredInd(String techEvalRequiredInd) {
        this.techEvalRequiredInd = techEvalRequiredInd;
    }

    public String getReadyForApprovalInd() {
        return readyForApprovalInd;
    }

    public void setReadyForApprovalInd(String readyForApprovalInd) {
        this.readyForApprovalInd = readyForApprovalInd;
    }

    public String getInqCompleteInd() {
        return inqCompleteInd;
    }

    public void setInqCompleteInd(String inqCompleteInd) {
        this.inqCompleteInd = inqCompleteInd;
    }

    public String getPoCompleteInd() {
        return poCompleteInd;
    }

    public void setPoCompleteInd(String poCompleteInd) {
        this.poCompleteInd = poCompleteInd;
    }

    public String getGeneralCipInd() {
        return generalCipInd;
    }

    public void setGeneralCipInd(String generalCipInd) {
        this.generalCipInd = generalCipInd;
    }

    public String getCalcAccountInd() {
        return calcAccountInd;
    }

    public void setCalcAccountInd(String calcAccountInd) {
        this.calcAccountInd = calcAccountInd;
    }

    public String getGetBudgetInd() {
        return getBudgetInd;
    }

    public void setGetBudgetInd(String getBudgetInd) {
        this.getBudgetInd = getBudgetInd;
    }

    public String getGetDesigInd() {
        return getDesigInd;
    }

    public void setGetDesigInd(String getDesigInd) {
        this.getDesigInd = getDesigInd;
    }

    public String getFastProgressInd() {
        return fastProgressInd;
    }

    public void setFastProgressInd(String fastProgressInd) {
        this.fastProgressInd = fastProgressInd;
    }

    public String getCalcRosDateInd() {
        return calcRosDateInd;
    }

    public void setCalcRosDateInd(String calcRosDateInd) {
        this.calcRosDateInd = calcRosDateInd;
    }

    public String getCalcWeightInd() {
        return calcWeightInd;
    }

    public void setCalcWeightInd(String calcWeightInd) {
        this.calcWeightInd = calcWeightInd;
    }

    public String getHandleBomDocInd() {
        return handleBomDocInd;
    }

    public void setHandleBomDocInd(String handleBomDocInd) {
        this.handleBomDocInd = handleBomDocInd;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getReleaseContext() {
        return releaseContext;
    }

    public void setReleaseContext(String releaseContext) {
        this.releaseContext = releaseContext;
    }

    public String getrType() {
        return rType;
    }

    public void setrType(String rType) {
        this.rType = rType;
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

    public Integer getEmtsId() {
        return emtsId;
    }

    public void setEmtsId(Integer emtsId) {
        this.emtsId = emtsId;
    }

    public Integer getRbcId() {
        return rbcId;
    }

    public void setRbcId(Integer rbcId) {
        this.rbcId = rbcId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getAqcId() {
        return aqcId;
    }

    public void setAqcId(Integer aqcId) {
        this.aqcId = aqcId;
    }

    public Integer getMsetId() {
        return msetId;
    }

    public void setMsetId(Integer msetId) {
        this.msetId = msetId;
    }

    public Integer getMgId() {
        return mgId;
    }

    public void setMgId(Integer mgId) {
        this.mgId = mgId;
    }

    public Integer getRcatId() {
        return rcatId;
    }

    public void setRcatId(Integer rcatId) {
        this.rcatId = rcatId;
    }

    public Integer getLastRId() {
        return lastRId;
    }

    public void setLastRId(Integer lastRId) {
        this.lastRId = lastRId;
    }

    public Integer getSecId() {
        return secId;
    }

    public void setSecId(Integer secId) {
        this.secId = secId;
    }

    public Integer getPtyId() {
        return ptyId;
    }

    public void setPtyId(Integer ptyId) {
        this.ptyId = ptyId;
    }

    public Integer getExpedIlvId() {
        return expedIlvId;
    }

    public void setExpedIlvId(Integer expedIlvId) {
        this.expedIlvId = expedIlvId;
    }

    public Integer getInspIlvId() {
        return inspIlvId;
    }

    public void setInspIlvId(Integer inspIlvId) {
        this.inspIlvId = inspIlvId;
    }

    public Integer getCritIlvId() {
        return critIlvId;
    }

    public void setCritIlvId(Integer critIlvId) {
        this.critIlvId = critIlvId;
    }

    public Integer getBalanceRId() {
        return balanceRId;
    }

    public void setBalanceRId(Integer balanceRId) {
        this.balanceRId = balanceRId;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
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

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderSubType() {
        return orderSubType;
    }

    public void setOrderSubType(String orderSubType) {
        this.orderSubType = orderSubType;
    }

    public Date getRelToProcDate() {
        return relToProcDate;
    }

    public void setRelToProcDate(Date relToProcDate) {
        this.relToProcDate = relToProcDate;
    }

    public Date getEstimatedRosDate() {
        return estimatedRosDate;
    }

    public void setEstimatedRosDate(Date estimatedRosDate) {
        this.estimatedRosDate = estimatedRosDate;
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

    public Date getProcAcknowledgeDate() {
        return procAcknowledgeDate;
    }

    public void setProcAcknowledgeDate(Date procAcknowledgeDate) {
        this.procAcknowledgeDate = procAcknowledgeDate;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getMrRevision() {
        return mrRevision;
    }

    public void setMrRevision(String mrRevision) {
        this.mrRevision = mrRevision;
    }

    public String getSrRevision() {
        return srRevision;
    }

    public void setSrRevision(String srRevision) {
        this.srRevision = srRevision;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getMscmComment() {
        return mscmComment;
    }

    public void setMscmComment(String mscmComment) {
        this.mscmComment = mscmComment;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getMrRevSch() {
        return mrRevSch;
    }

    public void setMrRevSch(String mrRevSch) {
        this.mrRevSch = mrRevSch;
    }

    public String getSrRevSch() {
        return srRevSch;
    }

    public void setSrRevSch(String srRevSch) {
        this.srRevSch = srRevSch;
    }

    public String getKeepNlsDescInd() {
        return keepNlsDescInd;
    }

    public void setKeepNlsDescInd(String keepNlsDescInd) {
        this.keepNlsDescInd = keepNlsDescInd;
    }

    public String getRelToProcBy() {
        return relToProcBy;
    }

    public void setRelToProcBy(String relToProcBy) {
        this.relToProcBy = relToProcBy;
    }

    public String getErpManagedInd() {
        return erpManagedInd;
    }

    public void setErpManagedInd(String erpManagedInd) {
        this.erpManagedInd = erpManagedInd;
    }

    public Integer getQsId() {
        return qsId;
    }

    public void setQsId(Integer qsId) {
        this.qsId = qsId;
    }
}
