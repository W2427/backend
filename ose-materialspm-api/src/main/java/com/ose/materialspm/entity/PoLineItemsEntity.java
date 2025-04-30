package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * PO详情表
 */
@Entity
@Table(name = "m_po_line_items")
@NamedQuery(name = "PoLineItemsEntity.findAll", query = "SELECT a FROM PoLineItemsEntity a")
public class PoLineItemsEntity extends BaseDTO {

    private static final long serialVersionUID = -5983586386719759562L;
    @Id
    @Column(name = "poli_id", nullable = false)
    private Integer poliId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "poh_id", nullable = false)
    private Integer pohId;

    @Column(name = "poli_pos", nullable = false)
    private Integer poliPos;

    @Column(name = "poli_sub_pos", nullable = false)
    private Integer poliSubPos;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "ident", nullable = false)
    private Integer ident;

    @Column(name = "qty_unit_id", nullable = false)
    private Integer qtyUnitId;

    @Column(name = "weight_unit_id", nullable = false)
    private Integer weightUnitId;

    @Column(name = "currency_id", nullable = false)
    private Integer currencyId;

    @Column(name = "poli_qty", nullable = false)
    private Double poliQty;

    @Column(name = "poli_unit_weight", nullable = false)
    private Double poliUnitWeight;

    @Column(name = "poli_volume", nullable = false)
    private Double poliVolume;

    @Column(name = "poli_unit_price", nullable = false)
    private Double poliUnitPrice;

    @Column(name = "supply_price", nullable = false)
    private Double supplyPrice;

    @Column(name = "install_price", nullable = false)
    private Double installPrice;

    @Column(name = "price_base", nullable = false)
    private String priceBase;

    @Column(name = "discount_percent", nullable = false)
    private Double discountPercent;

    @Column(name = "discount_amount", nullable = false)
    private Double discountAmount;

    @Column(name = "discount_base", nullable = false)
    private String discountBase;

    @Column(name = "cert_sheet_qty", nullable = false)
    private Double certSheetQty;

    @Column(name = "export_license_req_ind", nullable = false)
    private String exportLicenseReqInd;

    @Column(name = "update_ind", nullable = false)
    private String updateInd;

    @Column(name = "accepted_ind", nullable = false)
    private String acceptedInd;

    @Column(name = "split_post_approval_ind", nullable = false)
    private String splitPostApprovalInd;

    @Column(name = "tech_eval_required_ind", nullable = false)
    private String techEvalRequiredInd;

    @Column(name = "tec_passed_ind", nullable = false)
    private String tecPassedInd;

    @Column(name = "csh_required_ind", nullable = false)
    private String cshRequiredInd;

    @Column(name = "force_complete_ind", nullable = false)
    private String forceCompleteInd;

    @Column(name = "cancel_req_ind", nullable = false)
    private String cancelReqInd;

    @Column(name = "expediting_ind", nullable = false)
    private String expeditingInd;

    @Column(name = "percent_us_content", nullable = false)
    private Double percentUsContent;

    @Column(name = "last_ship", nullable = false)
    private Integer lastShip;

    @Column(name = "final_approved_amount", nullable = false)
    private Double finalApprovedAmount;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "approved_pty_id")
    private Integer approvedPtyId;

    @Column(name = "rli_id")
    private Integer rliId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "parent_poli_id")
    private Integer parentPoliId;

    @Column(name = "master_poli_id")
    private Integer masterPoliId;

    @Column(name = "ids_id")
    private Integer idsId;

    @Column(name = "lp_id")
    private Integer lpId;

    @Column(name = "qd_id")
    private Integer qdId;

    @Column(name = "volume_unit_id")
    private Integer volumeUnitId;

    @Column(name = "cy_id")
    private Integer cyId;

    @Column(name = "frt_id")
    private Integer frtId;

    @Column(name = "cnr_id")
    private Integer cnrId;

    @Column(name = "export_license_number")
    private String exportLicenseNumber;

    @Column(name = "tag_number", nullable = false)
    private String tagNumber;

    @Column(name = "revised_promise_date")
    private Date revisedPromiseDate;

    @Column(name = "poli_dimensions")
    private String poliDimensions;

    @Column(name = "export_license_received_date")
    private Date exportLicenseReceivedDate;

    @Column(name = "poli_close_date")
    private Date poliCloseDate;

    @Column(name = "notice_to_proceed_date")
    private Date noticeToProceedDate;

    @Column(name = "first_ship")
    private Integer firstShip;

    @Column(name = "final_approved_sched_change")
    private Integer finalApprovedSchedChange;

    @Column(name = "catalog_number")
    private String catalogNumber;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "freight_value")
    private String freightValue;

    @Column(name = "poli_comment")
    private String poliComment;

    @Column(name = "task_number")
    private String taskNumber;

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

    @Column(name = "inv_chk_ind")
    private String invChkInd;

    @Column(name = "rental_start_date")
    private Date rentalStartDate;

    @Column(name = "rental_end_date")
    private Date rentalEndDate;

    @Column(name = "interval")
    private String interval;

    @Column(name = "valid_period")
    private Integer validPeriod;

    @Column(name = "ppps_id")
    private Integer pppsId;

    public Integer getPoliId() {
        return poliId;
    }

    public void setPoliId(Integer poliId) {
        this.poliId = poliId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getPohId() {
        return pohId;
    }

    public void setPohId(Integer pohId) {
        this.pohId = pohId;
    }

    public Integer getPoliPos() {
        return poliPos;
    }

    public void setPoliPos(Integer poliPos) {
        this.poliPos = poliPos;
    }

    public Integer getPoliSubPos() {
        return poliSubPos;
    }

    public void setPoliSubPos(Integer poliSubPos) {
        this.poliSubPos = poliSubPos;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public Integer getQtyUnitId() {
        return qtyUnitId;
    }

    public void setQtyUnitId(Integer qtyUnitId) {
        this.qtyUnitId = qtyUnitId;
    }

    public Integer getWeightUnitId() {
        return weightUnitId;
    }

    public void setWeightUnitId(Integer weightUnitId) {
        this.weightUnitId = weightUnitId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Double getPoliQty() {
        return poliQty;
    }

    public void setPoliQty(Double poliQty) {
        this.poliQty = poliQty;
    }

    public Double getPoliUnitWeight() {
        return poliUnitWeight;
    }

    public void setPoliUnitWeight(Double poliUnitWeight) {
        this.poliUnitWeight = poliUnitWeight;
    }

    public Double getPoliVolume() {
        return poliVolume;
    }

    public void setPoliVolume(Double poliVolume) {
        this.poliVolume = poliVolume;
    }

    public Double getPoliUnitPrice() {
        return poliUnitPrice;
    }

    public void setPoliUnitPrice(Double poliUnitPrice) {
        this.poliUnitPrice = poliUnitPrice;
    }

    public Double getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(Double supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public Double getInstallPrice() {
        return installPrice;
    }

    public void setInstallPrice(Double installPrice) {
        this.installPrice = installPrice;
    }

    public String getPriceBase() {
        return priceBase;
    }

    public void setPriceBase(String priceBase) {
        this.priceBase = priceBase;
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

    public String getDiscountBase() {
        return discountBase;
    }

    public void setDiscountBase(String discountBase) {
        this.discountBase = discountBase;
    }

    public Double getCertSheetQty() {
        return certSheetQty;
    }

    public void setCertSheetQty(Double certSheetQty) {
        this.certSheetQty = certSheetQty;
    }

    public String getExportLicenseReqInd() {
        return exportLicenseReqInd;
    }

    public void setExportLicenseReqInd(String exportLicenseReqInd) {
        this.exportLicenseReqInd = exportLicenseReqInd;
    }

    public String getUpdateInd() {
        return updateInd;
    }

    public void setUpdateInd(String updateInd) {
        this.updateInd = updateInd;
    }

    public String getAcceptedInd() {
        return acceptedInd;
    }

    public void setAcceptedInd(String acceptedInd) {
        this.acceptedInd = acceptedInd;
    }

    public String getSplitPostApprovalInd() {
        return splitPostApprovalInd;
    }

    public void setSplitPostApprovalInd(String splitPostApprovalInd) {
        this.splitPostApprovalInd = splitPostApprovalInd;
    }

    public String getTechEvalRequiredInd() {
        return techEvalRequiredInd;
    }

    public void setTechEvalRequiredInd(String techEvalRequiredInd) {
        this.techEvalRequiredInd = techEvalRequiredInd;
    }

    public String getTecPassedInd() {
        return tecPassedInd;
    }

    public void setTecPassedInd(String tecPassedInd) {
        this.tecPassedInd = tecPassedInd;
    }

    public String getCshRequiredInd() {
        return cshRequiredInd;
    }

    public void setCshRequiredInd(String cshRequiredInd) {
        this.cshRequiredInd = cshRequiredInd;
    }

    public String getForceCompleteInd() {
        return forceCompleteInd;
    }

    public void setForceCompleteInd(String forceCompleteInd) {
        this.forceCompleteInd = forceCompleteInd;
    }

    public String getCancelReqInd() {
        return cancelReqInd;
    }

    public void setCancelReqInd(String cancelReqInd) {
        this.cancelReqInd = cancelReqInd;
    }

    public String getExpeditingInd() {
        return expeditingInd;
    }

    public void setExpeditingInd(String expeditingInd) {
        this.expeditingInd = expeditingInd;
    }

    public Double getPercentUsContent() {
        return percentUsContent;
    }

    public void setPercentUsContent(Double percentUsContent) {
        this.percentUsContent = percentUsContent;
    }

    public Integer getLastShip() {
        return lastShip;
    }

    public void setLastShip(Integer lastShip) {
        this.lastShip = lastShip;
    }

    public Double getFinalApprovedAmount() {
        return finalApprovedAmount;
    }

    public void setFinalApprovedAmount(Double finalApprovedAmount) {
        this.finalApprovedAmount = finalApprovedAmount;
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

    public Integer getApprovedPtyId() {
        return approvedPtyId;
    }

    public void setApprovedPtyId(Integer approvedPtyId) {
        this.approvedPtyId = approvedPtyId;
    }

    public Integer getRliId() {
        return rliId;
    }

    public void setRliId(Integer rliId) {
        this.rliId = rliId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getParentPoliId() {
        return parentPoliId;
    }

    public void setParentPoliId(Integer parentPoliId) {
        this.parentPoliId = parentPoliId;
    }

    public Integer getMasterPoliId() {
        return masterPoliId;
    }

    public void setMasterPoliId(Integer masterPoliId) {
        this.masterPoliId = masterPoliId;
    }

    public Integer getIdsId() {
        return idsId;
    }

    public void setIdsId(Integer idsId) {
        this.idsId = idsId;
    }

    public Integer getLpId() {
        return lpId;
    }

    public void setLpId(Integer lpId) {
        this.lpId = lpId;
    }

    public Integer getQdId() {
        return qdId;
    }

    public void setQdId(Integer qdId) {
        this.qdId = qdId;
    }

    public Integer getVolumeUnitId() {
        return volumeUnitId;
    }

    public void setVolumeUnitId(Integer volumeUnitId) {
        this.volumeUnitId = volumeUnitId;
    }

    public Integer getCyId() {
        return cyId;
    }

    public void setCyId(Integer cyId) {
        this.cyId = cyId;
    }

    public Integer getFrtId() {
        return frtId;
    }

    public void setFrtId(Integer frtId) {
        this.frtId = frtId;
    }

    public Integer getCnrId() {
        return cnrId;
    }

    public void setCnrId(Integer cnrId) {
        this.cnrId = cnrId;
    }

    public String getExportLicenseNumber() {
        return exportLicenseNumber;
    }

    public void setExportLicenseNumber(String exportLicenseNumber) {
        this.exportLicenseNumber = exportLicenseNumber;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Date getRevisedPromiseDate() {
        return revisedPromiseDate;
    }

    public void setRevisedPromiseDate(Date revisedPromiseDate) {
        this.revisedPromiseDate = revisedPromiseDate;
    }

    public String getPoliDimensions() {
        return poliDimensions;
    }

    public void setPoliDimensions(String poliDimensions) {
        this.poliDimensions = poliDimensions;
    }

    public Date getExportLicenseReceivedDate() {
        return exportLicenseReceivedDate;
    }

    public void setExportLicenseReceivedDate(Date exportLicenseReceivedDate) {
        this.exportLicenseReceivedDate = exportLicenseReceivedDate;
    }

    public Date getPoliCloseDate() {
        return poliCloseDate;
    }

    public void setPoliCloseDate(Date poliCloseDate) {
        this.poliCloseDate = poliCloseDate;
    }

    public Date getNoticeToProceedDate() {
        return noticeToProceedDate;
    }

    public void setNoticeToProceedDate(Date noticeToProceedDate) {
        this.noticeToProceedDate = noticeToProceedDate;
    }

    public Integer getFirstShip() {
        return firstShip;
    }

    public void setFirstShip(Integer firstShip) {
        this.firstShip = firstShip;
    }

    public Integer getFinalApprovedSchedChange() {
        return finalApprovedSchedChange;
    }

    public void setFinalApprovedSchedChange(Integer finalApprovedSchedChange) {
        this.finalApprovedSchedChange = finalApprovedSchedChange;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFreightValue() {
        return freightValue;
    }

    public void setFreightValue(String freightValue) {
        this.freightValue = freightValue;
    }

    public String getPoliComment() {
        return poliComment;
    }

    public void setPoliComment(String poliComment) {
        this.poliComment = poliComment;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
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

    public String getInvChkInd() {
        return invChkInd;
    }

    public void setInvChkInd(String invChkInd) {
        this.invChkInd = invChkInd;
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

    public Integer getPppsId() {
        return pppsId;
    }

    public void setPppsId(Integer pppsId) {
        this.pppsId = pppsId;
    }
}
