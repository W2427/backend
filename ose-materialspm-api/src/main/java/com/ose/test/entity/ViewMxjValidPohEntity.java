package com.ose.test.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.ose.dto.BaseDTO;

/**
 * The persistent class for the v_mxj_validPOH database table.
 */
@Entity
@Table(name = "V_MXJ_VALIDPOH")
@NamedQuery(name = "ViewMxjValidPohEntity.findAll", query = "SELECT a FROM ViewMxjValidPohEntity a")
public class ViewMxjValidPohEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    public ViewMxjValidPohEntity() {

    }

    @Id
    @Column(name = "POH_ID")
    private String id;

    @Column(name = "BUYER")
    private String buyer;

    @Column(name = "PO_NUMBER")
    private String poNumber;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "ORIGIN_ID")
    private String originId;

    @Column(name = "ORDER_TYPE")
    private String orderType;

    @Column(name = "PO_SUPP")
    private String poSupp;

    @Column(name = "DP_ID")
    private String dpId;

    @Column(name = "ISSUED_BY")
    private String issuedBy;

    @Column(name = "CURRENCY_ID")
    private String currencyId;

    @Column(name = "SUP_ID")
    private String supId;

    @Column(name = "BASE_POH_ID")
    private String basePohId;

    @Column(name = "TOT_MATL_COST")
    private String totMatlCost;

    @Column(name = "ESTIMATED_VALUE")
    private String estimatedValue;

    @Column(name = "ORD_DISC_PERCENT")
    private String ordDiscPercent;

    @Column(name = "INVOICE_VALUE")
    private String invoiceValue;

    @Column(name = "TOTAL_NET_WEIGHT")
    private String totalNetWeight;

    @Column(name = "TOTAL_GROSS_WEIGHT")
    private String totalGrossWeight;

    @Column(name = "TOTAL_VOLUME")
    private String totalVolume;

    @Column(name = "DISCOUNT_BASE")
    private String discountBase;

    @Column(name = "DISCOUNT_PERCENT")
    private String discountPercent;

    @Column(name = "DISCOUNT_AMOUNT")
    private String discountAmount;

    @Column(name = "COMMITMENT_LIMIT")
    private String commitmentLimit;

    @Column(name = "RETENTION_PERCENT")
    private String retentionPercent;

    @Column(name = "BOND_PREMIUM_AMOUNT")
    private String bondPremiumAmount;

    @Column(name = "BUDGET")
    private String budget;

    @Column(name = "SHIPMENT_GROUP_IND")
    private String shipmentGroupInd;

    @Column(name = "ESC_NOTICE_IND")
    private String escNoticeInd;

    @Column(name = "COMMENTS_SETTLED_IND")
    private String commentsSettledInd;

    @Column(name = "PRE_INSPECT_REQ_IND")
    private String preInspectReqInd;

    @Column(name = "READY_FOR_APPROVAL_IND")
    private String readyForApprovalInd;

    @Column(name = "OPI_IND")
    private String opiInd;

    @Column(name = "PERFORMANCE_BOND_RQD_IND")
    private String performanceBondRqdInd;

    @Column(name = "PAYMENT_BOND_RQD_IND")
    private String paymentBondRqdInd;

    @Column(name = "C_SIGNED_IND")
    private String cSignedInd;

    @Column(name = "SC_SIGNED_IND")
    private String scSignedInd;

    @Column(name = "BOND_INCREASE_REQUIRED_IND")
    private String bondIncreaseRequiredInd;

    @Column(name = "AMOUNT_REQUIRED")
    private String amountRequired;

    @Column(name = "USR_ID")
    private String usrId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LMOD")
    private Date lmod;

    @Column(name = "INT_REV")
    private String intRev;

    @Column(name = "RM_ID")
    private String rmId;

    @Column(name = "JOB_ID")
    private String jobId;

    @Column(name = "CCP_ID")
    private String ccpId;

    @Column(name = "CY_ID")
    private String cyId;

    @Column(name = "COMPANY_ID")
    private Long companyId;

    @Column(name = "EXPEDITER")
    private String expediter;

    @Column(name = "TRAFFIC")
    private String traffic;

    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ORG_ETA_SITE_DATE")
    private Date orgEtaSiteDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ORG_FLD_REQ_DATE")
    private Date orgFldReqDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RFA_DATE")
    private Date rfaDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_PROM_CONTR_DATE")
    private Date firstPromContrDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_PROM_CONTR_DATE")
    private Date lastPromContrDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ORDER_DATE")
    private Date orderDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TECH_EVAL_COMP_DATE")
    private Date techEvalCompDate;

    @Column(name = "REVISION_ID")
    private String revisionId;

    @Column(name = "CLIENT_PO_NUMBER")
    private String clientPoNumber;

    @Column(name = "PERFORMANCE_SCORE")
    private String performanceScore;

    @Column(name = "ACTIVE_IND")
    private String activeInd;

    @Column(name = "SHORT_DESC")
    private String shortDesc;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
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

    public String getPoSupp() {
        return poSupp;
    }

    public void setPoSupp(String poSupp) {
        this.poSupp = poSupp;
    }

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBasePohId() {
        return basePohId;
    }

    public void setBasePohId(String basePohId) {
        this.basePohId = basePohId;
    }

    public String getTotMatlCost() {
        return totMatlCost;
    }

    public void setTotMatlCost(String totMatlCost) {
        this.totMatlCost = totMatlCost;
    }

    public String getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(String estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getOrdDiscPercent() {
        return ordDiscPercent;
    }

    public void setOrdDiscPercent(String ordDiscPercent) {
        this.ordDiscPercent = ordDiscPercent;
    }

    public String getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(String invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getTotalNetWeight() {
        return totalNetWeight;
    }

    public void setTotalNetWeight(String totalNetWeight) {
        this.totalNetWeight = totalNetWeight;
    }

    public String getTotalGrossWeight() {
        return totalGrossWeight;
    }

    public void setTotalGrossWeight(String totalGrossWeight) {
        this.totalGrossWeight = totalGrossWeight;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getDiscountBase() {
        return discountBase;
    }

    public void setDiscountBase(String discountBase) {
        this.discountBase = discountBase;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCommitmentLimit() {
        return commitmentLimit;
    }

    public void setCommitmentLimit(String commitmentLimit) {
        this.commitmentLimit = commitmentLimit;
    }

    public String getRetentionPercent() {
        return retentionPercent;
    }

    public void setRetentionPercent(String retentionPercent) {
        this.retentionPercent = retentionPercent;
    }

    public String getBondPremiumAmount() {
        return bondPremiumAmount;
    }

    public void setBondPremiumAmount(String bondPremiumAmount) {
        this.bondPremiumAmount = bondPremiumAmount;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
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

    public String getAmountRequired() {
        return amountRequired;
    }

    public void setAmountRequired(String amountRequired) {
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

    public String getIntRev() {
        return intRev;
    }

    public void setIntRev(String intRev) {
        this.intRev = intRev;
    }

    public String getRmId() {
        return rmId;
    }

    public void setRmId(String rmId) {
        this.rmId = rmId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCcpId() {
        return ccpId;
    }

    public void setCcpId(String ccpId) {
        this.ccpId = ccpId;
    }

    public String getCyId() {
        return cyId;
    }

    public void setCyId(String cyId) {
        this.cyId = cyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public Date getRfaDate() {
        return rfaDate;
    }

    public void setRfaDate(Date rfaDate) {
        this.rfaDate = rfaDate;
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

    public Date getTechEvalCompDate() {
        return techEvalCompDate;
    }

    public void setTechEvalCompDate(Date techEvalCompDate) {
        this.techEvalCompDate = techEvalCompDate;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public String getClientPoNumber() {
        return clientPoNumber;
    }

    public void setClientPoNumber(String clientPoNumber) {
        this.clientPoNumber = clientPoNumber;
    }

    public String getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(String performanceScore) {
        this.performanceScore = performanceScore;
    }

    public String getActiveInd() {
        return activeInd;
    }

    public void setActiveInd(String activeInd) {
        this.activeInd = activeInd;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
