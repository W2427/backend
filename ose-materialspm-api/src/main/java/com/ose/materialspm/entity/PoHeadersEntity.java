package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * PO详情表
 */
@Entity
@Table(name = "m_po_headers")
@NamedQuery(name = "PoHeadersEntity.findAll", query = "SELECT a FROM PoHeadersEntity a")
public class PoHeadersEntity extends BaseDTO {

    private static final long serialVersionUID = -819524930078923998L;
    @Id
    @Column(name = "poh_id", nullable = false)
    private Integer pohId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "origin_id", nullable = false)
    private Integer originId;

    @Column(name = "order_type", nullable = false)
    private String orderType;

    @Column(name = "po_number", nullable = false)
    private String poNumber;

    @Column(name = "po_supp", nullable = false)
    private Integer poSupp;

    @Column(name = "parent_poh_id")
    private Integer parentPohId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "issued_by", nullable = false)
    private Integer issuedBy;

    @Column(name = "currency_id", nullable = false)
    private Integer currencyId;

    @Column(name = "sup_id", nullable = false)
    private Integer supId;

    @Column(name = "buyer", nullable = false)
    private String buyer;

    @Column(name = "base_poh_id", nullable = false)
    private Integer basePohId;

    @Column(name = "tot_matl_cost", nullable = false)
    private Double totMatlCost;

    @Column(name = "estimated_value", nullable = false)
    private Double estimatedValue;

    @Column(name = "ord_disc_percent", nullable = false)
    private Double ordDiscPercent;

    @Column(name = "invoice_value", nullable = false)
    private Double invoiceValue;

    @Column(name = "total_net_weight", nullable = false)
    private Double totalNetWeight;

    @Column(name = "total_gross_weight", nullable = false)
    private Double totalGrossWeight;

    @Column(name = "total_volume", nullable = false)
    private Double totalVolume;

    @Column(name = "discount_base", nullable = false)
    private String discountBase;

    @Column(name = "discount_percent", nullable = false)
    private Double discountPercent;

    @Column(name = "discount_amount", nullable = false)
    private Double discountAmount;

    @Column(name = "commitment_limit", nullable = false)
    private Double commitmentLimit;

    @Column(name = "retention_percent", nullable = false)
    private Double retentionPercent;

    @Column(name = "bond_premium_amount", nullable = false)
    private Double bondPremiumAmount;

    @Column(name = "budget", nullable = false)
    private Double budget;

    @Column(name = "shipment_group_ind", nullable = false)
    private String shipmentGroupInd;

    @Column(name = "esc_notice_ind", nullable = false)
    private String escNoticeInd;

    @Column(name = "comments_settled_ind", nullable = false)
    private String commentsSettledInd;

    @Column(name = "pre_inspect_req_ind", nullable = false)
    private String preInspectReqInd;

    @Column(name = "ready_for_approval_ind", nullable = false)
    private String readyForApprovalInd;

    @Column(name = "opi_ind", nullable = false)
    private String opiInd;

    @Column(name = "performance_bond_rqd_ind", nullable = false)
    private String performanceBondRqdInd;

    @Column(name = "payment_bond_rqd_ind", nullable = false)
    private String paymentBondRqdInd;

    @Column(name = "c_signed_ind", nullable = false)
    private String cSignedInd;

    @Column(name = "sc_signed_ind", nullable = false)
    private String scSignedInd;

    @Column(name = "bond_increase_required_ind", nullable = false)
    private String bondIncreaseRequiredInd;

    @Column(name = "amount_required", nullable = false)
    private Double amountRequired;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "scs_id")
    private Integer scsId;

    @Column(name = "rm_id")
    private Integer rmId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "inq_id")
    private Integer inqId;

    @Column(name = "ccp_id")
    private Integer ccpId;

    @Column(name = "exped_ilv_id")
    private Integer expedIlvId;

    @Column(name = "ilv_id")
    private Integer ilvId;

    @Column(name = "crit_ilv_id")
    private Integer critIlvId;

    @Column(name = "cy_id")
    private Integer cyId;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "sm_id")
    private Integer smId;

    @Column(name = "ev_id")
    private Integer evId;

    @Column(name = "sec_id")
    private Integer secId;

    @Column(name = "mg_id")
    private Integer mgId;

    @Column(name = "pty_id")
    private Integer ptyId;

    @Column(name = "sov_atpl_id")
    private Integer sovAtplId;

    @Column(name = "pps_id")
    private Integer ppsId;

    @Column(name = "expediter")
    private String expediter;

    @Column(name = "traffic")
    private String traffic;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_date")
    private Date approvedDate;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "po_issue_date")
    private Date poIssueDate;

    @Column(name = "po_close_date")
    private Date poCloseDate;

    @Column(name = "org_eta_site_date")
    private Date orgEtaSiteDate;

    @Column(name = "org_fld_req_date")
    private Date orgFldReqDate;

    @Column(name = "org_prom_ship_date")
    private Date orgPromShipDate;

    @Column(name = "rfa_date")
    private Date rfaDate;

    @Column(name = "next_contact_date")
    private Date nextContactDate;

    @Column(name = "last_contact_date")
    private Date lastContactDate;

    @Column(name = "first_prom_contr_date")
    private Date firstPromContrDate;

    @Column(name = "last_prom_contr_date")
    private Date lastPromContrDate;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "recv_acknowledge_date")
    private Date recvAcknowledgeDate;

    @Column(name = "tech_eval_comp_date")
    private Date techEvalCompDate;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "effective_date")
    private Date effectiveDate;

    @Column(name = "notice_to_proceed_date")
    private Date noticeToProceedDate;

    @Column(name = "ntp_switch_date")
    private Date ntpSwitchDate;

    @Lob
    @Column(name = "ntp_scope")
    private String ntpScope;

    @Column(name = "revision_id", nullable = false)
    private Integer revisionId;

    @Column(name = "order_sub_type")
    private String orderSubType;

    @Column(name = "point_of_origin")
    private String pointOfOrigin;

    @Column(name = "client_po_number")
    private String clientPoNumber;

    @Column(name = "performance_score", nullable = false)
    private Integer performanceScore;

    @Column(name = "inspection_responsibility")
    private String inspectionResponsibility;

    @Column(name = "pre_inspect_mom")
    private String preInspectMom;

    @Column(name = "pre_inspect_date")
    private Date preInspectDate;

    @Column(name = "inspection_coordinator")
    private String inspectionCoordinator;

    @Column(name = "attr_char1")
    private String attrChar1;

    @Column(name = "attr_char2")
    private String attrChar2;

    @Column(name = "attr_char3")
    private String attrChar3;

    @Column(name = "attr_char4")
    private String attrChar4;

    @Column(name = "attr_char5")
    private String attrChar5;

    @Column(name = "attr_num1")
    private Double attrNum1;

    @Column(name = "attr_num2")
    private Double attrNum2;

    @Column(name = "attr_num3")
    private Double attrNum3;

    @Column(name = "attr_num4")
    private Double attrNum4;

    @Column(name = "attr_num5")
    private Double attrNum5;

    @Column(name = "comments")
    private String comments;

    @Column(name = "retention_comment")
    private String retentionComment;

    @Column(name = "c_signed_by")
    private String cSignedBy;

    @Column(name = "c_title")
    private String cTitle;

    public Integer getPohId() {
        return pohId;
    }

    public void setPohId(Integer pohId) {
        this.pohId = pohId;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Integer getPoSupp() {
        return poSupp;
    }

    public void setPoSupp(Integer poSupp) {
        this.poSupp = poSupp;
    }

    public Integer getParentPohId() {
        return parentPohId;
    }

    public void setParentPohId(Integer parentPohId) {
        this.parentPohId = parentPohId;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(Integer issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getSupId() {
        return supId;
    }

    public void setSupId(Integer supId) {
        this.supId = supId;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Integer getBasePohId() {
        return basePohId;
    }

    public void setBasePohId(Integer basePohId) {
        this.basePohId = basePohId;
    }

    public Double getTotMatlCost() {
        return totMatlCost;
    }

    public void setTotMatlCost(Double totMatlCost) {
        this.totMatlCost = totMatlCost;
    }

    public Double getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(Double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public Double getOrdDiscPercent() {
        return ordDiscPercent;
    }

    public void setOrdDiscPercent(Double ordDiscPercent) {
        this.ordDiscPercent = ordDiscPercent;
    }

    public Double getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(Double invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public Double getTotalNetWeight() {
        return totalNetWeight;
    }

    public void setTotalNetWeight(Double totalNetWeight) {
        this.totalNetWeight = totalNetWeight;
    }

    public Double getTotalGrossWeight() {
        return totalGrossWeight;
    }

    public void setTotalGrossWeight(Double totalGrossWeight) {
        this.totalGrossWeight = totalGrossWeight;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getDiscountBase() {
        return discountBase;
    }

    public void setDiscountBase(String discountBase) {
        this.discountBase = discountBase;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getCommitmentLimit() {
        return commitmentLimit;
    }

    public void setCommitmentLimit(Double commitmentLimit) {
        this.commitmentLimit = commitmentLimit;
    }

    public Double getRetentionPercent() {
        return retentionPercent;
    }

    public void setRetentionPercent(Double retentionPercent) {
        this.retentionPercent = retentionPercent;
    }

    public Double getBondPremiumAmount() {
        return bondPremiumAmount;
    }

    public void setBondPremiumAmount(Double bondPremiumAmount) {
        this.bondPremiumAmount = bondPremiumAmount;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getShipmentGroupInd() {
        return shipmentGroupInd;
    }

    public void setShipmentGroupInd(String shipmentGroupInd) {
        this.shipmentGroupInd = shipmentGroupInd;
    }

    public String getEscNoticeInd() {
        return escNoticeInd;
    }

    public void setEscNoticeInd(String escNoticeInd) {
        this.escNoticeInd = escNoticeInd;
    }

    public String getCommentsSettledInd() {
        return commentsSettledInd;
    }

    public void setCommentsSettledInd(String commentsSettledInd) {
        this.commentsSettledInd = commentsSettledInd;
    }

    public String getPreInspectReqInd() {
        return preInspectReqInd;
    }

    public void setPreInspectReqInd(String preInspectReqInd) {
        this.preInspectReqInd = preInspectReqInd;
    }

    public String getReadyForApprovalInd() {
        return readyForApprovalInd;
    }

    public void setReadyForApprovalInd(String readyForApprovalInd) {
        this.readyForApprovalInd = readyForApprovalInd;
    }

    public String getOpiInd() {
        return opiInd;
    }

    public void setOpiInd(String opiInd) {
        this.opiInd = opiInd;
    }

    public String getPerformanceBondRqdInd() {
        return performanceBondRqdInd;
    }

    public void setPerformanceBondRqdInd(String performanceBondRqdInd) {
        this.performanceBondRqdInd = performanceBondRqdInd;
    }

    public String getPaymentBondRqdInd() {
        return paymentBondRqdInd;
    }

    public void setPaymentBondRqdInd(String paymentBondRqdInd) {
        this.paymentBondRqdInd = paymentBondRqdInd;
    }

    public String getcSignedInd() {
        return cSignedInd;
    }

    public void setcSignedInd(String cSignedInd) {
        this.cSignedInd = cSignedInd;
    }

    public String getScSignedInd() {
        return scSignedInd;
    }

    public void setScSignedInd(String scSignedInd) {
        this.scSignedInd = scSignedInd;
    }

    public String getBondIncreaseRequiredInd() {
        return bondIncreaseRequiredInd;
    }

    public void setBondIncreaseRequiredInd(String bondIncreaseRequiredInd) {
        this.bondIncreaseRequiredInd = bondIncreaseRequiredInd;
    }

    public Double getAmountRequired() {
        return amountRequired;
    }

    public void setAmountRequired(Double amountRequired) {
        this.amountRequired = amountRequired;
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

    public Integer getScsId() {
        return scsId;
    }

    public void setScsId(Integer scsId) {
        this.scsId = scsId;
    }

    public Integer getRmId() {
        return rmId;
    }

    public void setRmId(Integer rmId) {
        this.rmId = rmId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getInqId() {
        return inqId;
    }

    public void setInqId(Integer inqId) {
        this.inqId = inqId;
    }

    public Integer getCcpId() {
        return ccpId;
    }

    public void setCcpId(Integer ccpId) {
        this.ccpId = ccpId;
    }

    public Integer getExpedIlvId() {
        return expedIlvId;
    }

    public void setExpedIlvId(Integer expedIlvId) {
        this.expedIlvId = expedIlvId;
    }

    public Integer getIlvId() {
        return ilvId;
    }

    public void setIlvId(Integer ilvId) {
        this.ilvId = ilvId;
    }

    public Integer getCritIlvId() {
        return critIlvId;
    }

    public void setCritIlvId(Integer critIlvId) {
        this.critIlvId = critIlvId;
    }

    public Integer getCyId() {
        return cyId;
    }

    public void setCyId(Integer cyId) {
        this.cyId = cyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getSmId() {
        return smId;
    }

    public void setSmId(Integer smId) {
        this.smId = smId;
    }

    public Integer getEvId() {
        return evId;
    }

    public void setEvId(Integer evId) {
        this.evId = evId;
    }

    public Integer getSecId() {
        return secId;
    }

    public void setSecId(Integer secId) {
        this.secId = secId;
    }

    public Integer getMgId() {
        return mgId;
    }

    public void setMgId(Integer mgId) {
        this.mgId = mgId;
    }

    public Integer getPtyId() {
        return ptyId;
    }

    public void setPtyId(Integer ptyId) {
        this.ptyId = ptyId;
    }

    public Integer getSovAtplId() {
        return sovAtplId;
    }

    public void setSovAtplId(Integer sovAtplId) {
        this.sovAtplId = sovAtplId;
    }

    public Integer getPpsId() {
        return ppsId;
    }

    public void setPpsId(Integer ppsId) {
        this.ppsId = ppsId;
    }

    public String getExpediter() {
        return expediter;
    }

    public void setExpediter(String expediter) {
        this.expediter = expediter;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getPoIssueDate() {
        return poIssueDate;
    }

    public void setPoIssueDate(Date poIssueDate) {
        this.poIssueDate = poIssueDate;
    }

    public Date getPoCloseDate() {
        return poCloseDate;
    }

    public void setPoCloseDate(Date poCloseDate) {
        this.poCloseDate = poCloseDate;
    }

    public Date getOrgEtaSiteDate() {
        return orgEtaSiteDate;
    }

    public void setOrgEtaSiteDate(Date orgEtaSiteDate) {
        this.orgEtaSiteDate = orgEtaSiteDate;
    }

    public Date getOrgFldReqDate() {
        return orgFldReqDate;
    }

    public void setOrgFldReqDate(Date orgFldReqDate) {
        this.orgFldReqDate = orgFldReqDate;
    }

    public Date getOrgPromShipDate() {
        return orgPromShipDate;
    }

    public void setOrgPromShipDate(Date orgPromShipDate) {
        this.orgPromShipDate = orgPromShipDate;
    }

    public Date getRfaDate() {
        return rfaDate;
    }

    public void setRfaDate(Date rfaDate) {
        this.rfaDate = rfaDate;
    }

    public Date getNextContactDate() {
        return nextContactDate;
    }

    public void setNextContactDate(Date nextContactDate) {
        this.nextContactDate = nextContactDate;
    }

    public Date getLastContactDate() {
        return lastContactDate;
    }

    public void setLastContactDate(Date lastContactDate) {
        this.lastContactDate = lastContactDate;
    }

    public Date getFirstPromContrDate() {
        return firstPromContrDate;
    }

    public void setFirstPromContrDate(Date firstPromContrDate) {
        this.firstPromContrDate = firstPromContrDate;
    }

    public Date getLastPromContrDate() {
        return lastPromContrDate;
    }

    public void setLastPromContrDate(Date lastPromContrDate) {
        this.lastPromContrDate = lastPromContrDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getRecvAcknowledgeDate() {
        return recvAcknowledgeDate;
    }

    public void setRecvAcknowledgeDate(Date recvAcknowledgeDate) {
        this.recvAcknowledgeDate = recvAcknowledgeDate;
    }

    public Date getTechEvalCompDate() {
        return techEvalCompDate;
    }

    public void setTechEvalCompDate(Date techEvalCompDate) {
        this.techEvalCompDate = techEvalCompDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getNoticeToProceedDate() {
        return noticeToProceedDate;
    }

    public void setNoticeToProceedDate(Date noticeToProceedDate) {
        this.noticeToProceedDate = noticeToProceedDate;
    }

    public Date getNtpSwitchDate() {
        return ntpSwitchDate;
    }

    public void setNtpSwitchDate(Date ntpSwitchDate) {
        this.ntpSwitchDate = ntpSwitchDate;
    }

    public String getNtpScope() {
        return ntpScope;
    }

    public void setNtpScope(String ntpScope) {
        this.ntpScope = ntpScope;
    }

    public Integer getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Integer revisionId) {
        this.revisionId = revisionId;
    }

    public String getOrderSubType() {
        return orderSubType;
    }

    public void setOrderSubType(String orderSubType) {
        this.orderSubType = orderSubType;
    }

    public String getPointOfOrigin() {
        return pointOfOrigin;
    }

    public void setPointOfOrigin(String pointOfOrigin) {
        this.pointOfOrigin = pointOfOrigin;
    }

    public String getClientPoNumber() {
        return clientPoNumber;
    }

    public void setClientPoNumber(String clientPoNumber) {
        this.clientPoNumber = clientPoNumber;
    }

    public Integer getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(Integer performanceScore) {
        this.performanceScore = performanceScore;
    }

    public String getInspectionResponsibility() {
        return inspectionResponsibility;
    }

    public void setInspectionResponsibility(String inspectionResponsibility) {
        this.inspectionResponsibility = inspectionResponsibility;
    }

    public String getPreInspectMom() {
        return preInspectMom;
    }

    public void setPreInspectMom(String preInspectMom) {
        this.preInspectMom = preInspectMom;
    }

    public Date getPreInspectDate() {
        return preInspectDate;
    }

    public void setPreInspectDate(Date preInspectDate) {
        this.preInspectDate = preInspectDate;
    }

    public String getInspectionCoordinator() {
        return inspectionCoordinator;
    }

    public void setInspectionCoordinator(String inspectionCoordinator) {
        this.inspectionCoordinator = inspectionCoordinator;
    }

    public String getAttrChar1() {
        return attrChar1;
    }

    public void setAttrChar1(String attrChar1) {
        this.attrChar1 = attrChar1;
    }

    public String getAttrChar2() {
        return attrChar2;
    }

    public void setAttrChar2(String attrChar2) {
        this.attrChar2 = attrChar2;
    }

    public String getAttrChar3() {
        return attrChar3;
    }

    public void setAttrChar3(String attrChar3) {
        this.attrChar3 = attrChar3;
    }

    public String getAttrChar4() {
        return attrChar4;
    }

    public void setAttrChar4(String attrChar4) {
        this.attrChar4 = attrChar4;
    }

    public String getAttrChar5() {
        return attrChar5;
    }

    public void setAttrChar5(String attrChar5) {
        this.attrChar5 = attrChar5;
    }

    public Double getAttrNum1() {
        return attrNum1;
    }

    public void setAttrNum1(Double attrNum1) {
        this.attrNum1 = attrNum1;
    }

    public Double getAttrNum2() {
        return attrNum2;
    }

    public void setAttrNum2(Double attrNum2) {
        this.attrNum2 = attrNum2;
    }

    public Double getAttrNum3() {
        return attrNum3;
    }

    public void setAttrNum3(Double attrNum3) {
        this.attrNum3 = attrNum3;
    }

    public Double getAttrNum4() {
        return attrNum4;
    }

    public void setAttrNum4(Double attrNum4) {
        this.attrNum4 = attrNum4;
    }

    public Double getAttrNum5() {
        return attrNum5;
    }

    public void setAttrNum5(Double attrNum5) {
        this.attrNum5 = attrNum5;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRetentionComment() {
        return retentionComment;
    }

    public void setRetentionComment(String retentionComment) {
        this.retentionComment = retentionComment;
    }

    public String getcSignedBy() {
        return cSignedBy;
    }

    public void setcSignedBy(String cSignedBy) {
        this.cSignedBy = cSignedBy;
    }

    public String getcTitle() {
        return cTitle;
    }

    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }
}
