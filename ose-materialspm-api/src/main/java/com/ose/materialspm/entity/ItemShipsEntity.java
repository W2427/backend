package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 运输清单表
 */
@Entity
@Table(name = "m_item_ships")
@NamedQuery(name = "ItemShipsEntity.findAll", query = "SELECT a FROM ItemShipsEntity a")
public class ItemShipsEntity extends BaseDTO {

    private static final long serialVersionUID = 3864368517355699961L;
    @Id
    @Column(name = "item_ship_id", nullable = false)
    private Integer itemShipId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "poli_id", nullable = false)
    private Integer poliId;

    @Column(name = "item_ship_pos", nullable = false)
    private Integer itemShipPos = 1;

    @Column(name = "item_ship_sub_pos", nullable = false)
    private Integer itemShipSubPos = 1;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "dd_id", nullable = false)
    private Integer ddId;

    @Column(name = "ident", nullable = false)
    private Integer ident;

    @Column(name = "item_ship_qty", nullable = false)
    private Double itemShipQty = 0.0;

    @Column(name = "item_ship_resv_qty", nullable = false)
    private Double itemShipResvQty = 0.0;

    @Column(name = "item_ship_weight", nullable = false)
    private Double itemShipWeight = 0.0;

    @Column(name = "qty_unit_id", nullable = false)
    private Integer qtyUnitId;

    @Column(name = "item_ship_volume", nullable = false)
    private Double itemShipVolume = 0.0;

    @Column(name = "mcs_qty", nullable = false)
    private Double mcsQty = 0.0;

    @Column(name = "mcs_weight", nullable = false)
    private Double mcsWeight = 0.0;

    @Column(name = "irc_qty", nullable = false)
    private Double ircQty = 0.0;

    @Column(name = "irc_weight", nullable = false)
    private Double ircWeight = 0.0;

    @Column(name = "reln_qty", nullable = false)
    private Double relnQty = 0.0;

    @Column(name = "reln_weight", nullable = false)
    private Double relnWeight = 0.0;

    @Column(name = "po_req_delta", nullable = false)
    private Double poReqDelta = 0.0;

    @Column(name = "recv_on_site_qty", nullable = false)
    private Double recvOnSiteQty = 0.0;

    @Column(name = "fab_complete_percent", nullable = false)
    private Double fabCompletePercent = 0.0;

    @Column(name = "mcs_required_ind", nullable = false)
    private String mcsRequiredInd = "N";

    @Column(name = "irc_required_ind", nullable = false)
    private String ircRequiredInd = "N";

    @Column(name = "owl_comp_ind", nullable = false)
    private String owlCompInd = "N";

    @Column(name = "ncr_comp_ind", nullable = false)
    private String ncrCompInd = "N";

    @Column(name = "invoice_ok_ind", nullable = false)
    private String invoiceOkInd = "N";

    @Column(name = "split_by_traffic_ind", nullable = false)
    private String splitByTrafficInd = "N";

    @Column(name = "split_by_site_ind", nullable = false)
    private String splitBySiteInd = "N";

    @Column(name = "export_license_req_ind", nullable = false)
    private String exportLicenseReqInd = "Y";

    @Column(name = "inspectable_ind", nullable = false)
    private String inspectableInd = "N";

    @Column(name = "insp_release_note_ind", nullable = false)
    private String inspReleaseNoteInd = "Y";

    @Column(name = "insp_waiver_ind", nullable = false)
    private String inspWaiverInd = "N";

    @Column(name = "actual_ind", nullable = false)
    private String actualInd = "N";

    @Column(name = "acknowledge_ind", nullable = false)
    private String acknowledgeInd = "N";

    @Column(name = "x_dim", nullable = false)
    private Double xDim = 0.0;

    @Column(name = "y_dim", nullable = false)
    private Double yDim = 0.0;

    @Column(name = "z_dim", nullable = false)
    private Double zDim = 0.0;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "sg_id")
    private Integer sgId;

    @Column(name = "iap_id")
    private Integer iapId;

    @Column(name = "lp_id")
    private Integer lpId;

    @Column(name = "master_ish_id")
    private Integer masterIshId;

    @Column(name = "weight_unit_id")
    private Integer weightUnitId;

    @Column(name = "volume_unit_id")
    private Integer volumeUnitId;

    @Column(name = "dim_unit_id")
    private Integer dimUnitId;

    @Column(name = "ie_id")
    private Integer ieId;

    @Column(name = "ids_id")
    private Integer idsId;

    @Column(name = "csh_id")
    private Integer cshId;

    @Column(name = "frt_id")
    private Integer frtId;

    @Column(name = "sel_id")
    private Integer selId;

    @Column(name = "mcs_id")
    private Integer mcsId;

    @Column(name = "irct_id")
    private Integer irctId;

    @Column(name = "reln_id")
    private Integer relnId;

    @Column(name = "rm_id")
    private Integer rmId;

    @Column(name = "exped_par_id")
    private Integer expedParId;

    @Column(name = "insp_par_id")
    private Integer inspParId;

    @Column(name = "exped_sf_id")
    private Integer expedSfId;

    @Column(name = "insp_sf_id")
    private Integer inspSfId;

    @Column(name = "item_ship_dimensions")
    private String itemShipDimensions;

    @Column(name = "tag_number", nullable = false)
    private String tagNumber = "---";

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "fab_location")
    private String fabLocation;

    @Column(name = "fab_shop_cntrl_number")
    private String fabShopCntrlNumber;

    @Column(name = "insp_location")
    private String inspLocation;

    @Column(name = "owl")
    private String owl;

    @Column(name = "ncr")
    private String ncr;

    @Column(name = "insp_ncr_remarks")
    private String inspNcrRemarks;

    @Column(name = "rev_freight_value")
    private String revFreightValue;

    @Column(name = "inv_number")
    private String invNumber;

    @Column(name = "comments")
    private String comments;

    @Column(name = "rev_prom_ship_date")
    private Date revPromShipDate;

    @Column(name = "forecast_eta_site_date")
    private Date forecastEtaSiteDate;

    @Column(name = "req_site_date")
    private Date reqSiteDate;

    @Column(name = "req_ship_date")
    private Date reqShipDate;

    @Column(name = "actual_contract_date")
    private Date actualContractDate;

    @Column(name = "maj_matl_order_date")
    private Date majMatlOrderDate;

    @Column(name = "maj_matl_recv_date")
    private Date majMatlRecvDate;

    @Column(name = "actual_fab_start_date")
    private Date actualFabStartDate;

    @Column(name = "prom_fab_comp_date")
    private Date promFabCompDate;

    @Column(name = "final_inspect_date")
    private Date finalInspectDate;

    @Column(name = "actual_fab_comp_date")
    private Date actualFabCompDate;

    @Column(name = "actual_ship_date")
    private Date actualShipDate;

    @Column(name = "ready_predicted")
    private Date readyPredicted;

    @Column(name = "ready_actual")
    private Date readyActual;

    @Column(name = "fifty_percent_fab_comp")
    private Date fiftyPercentFabComp;

    @Column(name = "pred_ship_date")
    private Date predShipDate;

    @Column(name = "prom_ship_date")
    private Date promShipDate;

    @Column(name = "pred_stage_ready_date")
    private Date predStageReadyDate;

    @Column(name = "actual_stage_ready_date")
    private Date actualStageReadyDate;

    @Column(name = "pred_loaded_date")
    private Date predLoadedDate;

    @Column(name = "actual_loaded_date")
    private Date actualLoadedDate;

    @Column(name = "pred_on_site_date")
    private Date predOnSiteDate;

    @Column(name = "prom_contract_date")
    private Date promContractDate;

    @Column(name = "prom_inspect_date")
    private Date promInspectDate;

    @Column(name = "actual_on_site_date")
    private Date actualOnSiteDate;

    @Column(name = "last_info_date", nullable = false)
    private Date lastInfoDate = new Date();

    @Column(name = "insp_rel_waiver_date")
    private Date inspRelWaiverDate;

    @Column(name = "insp_ncr_date")
    private Date inspNcrDate;

    @Column(name = "insp_ncr_release_date")
    private Date inspNcrReleaseDate;

    @Column(name = "pred_traffic_arrival_date")
    private Date predTrafficArrivalDate;

    @Column(name = "actual_traffic_arrival_date")
    private Date actualTrafficArrivalDate;

    @Column(name = "ivi_id")
    private Integer iviId;

    @Column(name = "dl_id")
    private Integer dlId;

    @Column(name = "pick_up_date")
    private Date pickUpDate;

    public Integer getItemShipId() {
        return itemShipId;
    }

    public void setItemShipId(Integer itemShipId) {
        this.itemShipId = itemShipId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getPoliId() {
        return poliId;
    }

    public void setPoliId(Integer poliId) {
        this.poliId = poliId;
    }

    public Integer getItemShipPos() {
        return itemShipPos;
    }

    public void setItemShipPos(Integer itemShipPos) {
        this.itemShipPos = itemShipPos;
    }

    public Integer getItemShipSubPos() {
        return itemShipSubPos;
    }

    public void setItemShipSubPos(Integer itemShipSubPos) {
        this.itemShipSubPos = itemShipSubPos;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getDdId() {
        return ddId;
    }

    public void setDdId(Integer ddId) {
        this.ddId = ddId;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public Double getItemShipQty() {
        return itemShipQty;
    }

    public void setItemShipQty(Double itemShipQty) {
        this.itemShipQty = itemShipQty;
    }

    public Double getItemShipResvQty() {
        return itemShipResvQty;
    }

    public void setItemShipResvQty(Double itemShipResvQty) {
        this.itemShipResvQty = itemShipResvQty;
    }

    public Double getItemShipWeight() {
        return itemShipWeight;
    }

    public void setItemShipWeight(Double itemShipWeight) {
        this.itemShipWeight = itemShipWeight;
    }

    public Integer getQtyUnitId() {
        return qtyUnitId;
    }

    public void setQtyUnitId(Integer qtyUnitId) {
        this.qtyUnitId = qtyUnitId;
    }

    public Double getItemShipVolume() {
        return itemShipVolume;
    }

    public void setItemShipVolume(Double itemShipVolume) {
        this.itemShipVolume = itemShipVolume;
    }

    public Double getMcsQty() {
        return mcsQty;
    }

    public void setMcsQty(Double mcsQty) {
        this.mcsQty = mcsQty;
    }

    public Double getMcsWeight() {
        return mcsWeight;
    }

    public void setMcsWeight(Double mcsWeight) {
        this.mcsWeight = mcsWeight;
    }

    public Double getIrcQty() {
        return ircQty;
    }

    public void setIrcQty(Double ircQty) {
        this.ircQty = ircQty;
    }

    public Double getIrcWeight() {
        return ircWeight;
    }

    public void setIrcWeight(Double ircWeight) {
        this.ircWeight = ircWeight;
    }

    public Double getRelnQty() {
        return relnQty;
    }

    public void setRelnQty(Double relnQty) {
        this.relnQty = relnQty;
    }

    public Double getRelnWeight() {
        return relnWeight;
    }

    public void setRelnWeight(Double relnWeight) {
        this.relnWeight = relnWeight;
    }

    public Double getPoReqDelta() {
        return poReqDelta;
    }

    public void setPoReqDelta(Double poReqDelta) {
        this.poReqDelta = poReqDelta;
    }

    public Double getRecvOnSiteQty() {
        return recvOnSiteQty;
    }

    public void setRecvOnSiteQty(Double recvOnSiteQty) {
        this.recvOnSiteQty = recvOnSiteQty;
    }

    public Double getFabCompletePercent() {
        return fabCompletePercent;
    }

    public void setFabCompletePercent(Double fabCompletePercent) {
        this.fabCompletePercent = fabCompletePercent;
    }

    public String getMcsRequiredInd() {
        return mcsRequiredInd;
    }

    public void setMcsRequiredInd(String mcsRequiredInd) {
        this.mcsRequiredInd = mcsRequiredInd;
    }

    public String getIrcRequiredInd() {
        return ircRequiredInd;
    }

    public void setIrcRequiredInd(String ircRequiredInd) {
        this.ircRequiredInd = ircRequiredInd;
    }

    public String getOwlCompInd() {
        return owlCompInd;
    }

    public void setOwlCompInd(String owlCompInd) {
        this.owlCompInd = owlCompInd;
    }

    public String getNcrCompInd() {
        return ncrCompInd;
    }

    public void setNcrCompInd(String ncrCompInd) {
        this.ncrCompInd = ncrCompInd;
    }

    public String getInvoiceOkInd() {
        return invoiceOkInd;
    }

    public void setInvoiceOkInd(String invoiceOkInd) {
        this.invoiceOkInd = invoiceOkInd;
    }

    public String getSplitByTrafficInd() {
        return splitByTrafficInd;
    }

    public void setSplitByTrafficInd(String splitByTrafficInd) {
        this.splitByTrafficInd = splitByTrafficInd;
    }

    public String getSplitBySiteInd() {
        return splitBySiteInd;
    }

    public void setSplitBySiteInd(String splitBySiteInd) {
        this.splitBySiteInd = splitBySiteInd;
    }

    public String getExportLicenseReqInd() {
        return exportLicenseReqInd;
    }

    public void setExportLicenseReqInd(String exportLicenseReqInd) {
        this.exportLicenseReqInd = exportLicenseReqInd;
    }

    public String getInspectableInd() {
        return inspectableInd;
    }

    public void setInspectableInd(String inspectableInd) {
        this.inspectableInd = inspectableInd;
    }

    public String getInspReleaseNoteInd() {
        return inspReleaseNoteInd;
    }

    public void setInspReleaseNoteInd(String inspReleaseNoteInd) {
        this.inspReleaseNoteInd = inspReleaseNoteInd;
    }

    public String getInspWaiverInd() {
        return inspWaiverInd;
    }

    public void setInspWaiverInd(String inspWaiverInd) {
        this.inspWaiverInd = inspWaiverInd;
    }

    public String getActualInd() {
        return actualInd;
    }

    public void setActualInd(String actualInd) {
        this.actualInd = actualInd;
    }

    public String getAcknowledgeInd() {
        return acknowledgeInd;
    }

    public void setAcknowledgeInd(String acknowledgeInd) {
        this.acknowledgeInd = acknowledgeInd;
    }

    public Double getxDim() {
        return xDim;
    }

    public void setxDim(Double xDim) {
        this.xDim = xDim;
    }

    public Double getyDim() {
        return yDim;
    }

    public void setyDim(Double yDim) {
        this.yDim = yDim;
    }

    public Double getzDim() {
        return zDim;
    }

    public void setzDim(Double zDim) {
        this.zDim = zDim;
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

    public Integer getSgId() {
        return sgId;
    }

    public void setSgId(Integer sgId) {
        this.sgId = sgId;
    }

    public Integer getIapId() {
        return iapId;
    }

    public void setIapId(Integer iapId) {
        this.iapId = iapId;
    }

    public Integer getLpId() {
        return lpId;
    }

    public void setLpId(Integer lpId) {
        this.lpId = lpId;
    }

    public Integer getMasterIshId() {
        return masterIshId;
    }

    public void setMasterIshId(Integer masterIshId) {
        this.masterIshId = masterIshId;
    }

    public Integer getWeightUnitId() {
        return weightUnitId;
    }

    public void setWeightUnitId(Integer weightUnitId) {
        this.weightUnitId = weightUnitId;
    }

    public Integer getVolumeUnitId() {
        return volumeUnitId;
    }

    public void setVolumeUnitId(Integer volumeUnitId) {
        this.volumeUnitId = volumeUnitId;
    }

    public Integer getDimUnitId() {
        return dimUnitId;
    }

    public void setDimUnitId(Integer dimUnitId) {
        this.dimUnitId = dimUnitId;
    }

    public Integer getIeId() {
        return ieId;
    }

    public void setIeId(Integer ieId) {
        this.ieId = ieId;
    }

    public Integer getIdsId() {
        return idsId;
    }

    public void setIdsId(Integer idsId) {
        this.idsId = idsId;
    }

    public Integer getCshId() {
        return cshId;
    }

    public void setCshId(Integer cshId) {
        this.cshId = cshId;
    }

    public Integer getFrtId() {
        return frtId;
    }

    public void setFrtId(Integer frtId) {
        this.frtId = frtId;
    }

    public Integer getSelId() {
        return selId;
    }

    public void setSelId(Integer selId) {
        this.selId = selId;
    }

    public Integer getMcsId() {
        return mcsId;
    }

    public void setMcsId(Integer mcsId) {
        this.mcsId = mcsId;
    }

    public Integer getIrctId() {
        return irctId;
    }

    public void setIrctId(Integer irctId) {
        this.irctId = irctId;
    }

    public Integer getRelnId() {
        return relnId;
    }

    public void setRelnId(Integer relnId) {
        this.relnId = relnId;
    }

    public Integer getRmId() {
        return rmId;
    }

    public void setRmId(Integer rmId) {
        this.rmId = rmId;
    }

    public Integer getExpedParId() {
        return expedParId;
    }

    public void setExpedParId(Integer expedParId) {
        this.expedParId = expedParId;
    }

    public Integer getInspParId() {
        return inspParId;
    }

    public void setInspParId(Integer inspParId) {
        this.inspParId = inspParId;
    }

    public Integer getExpedSfId() {
        return expedSfId;
    }

    public void setExpedSfId(Integer expedSfId) {
        this.expedSfId = expedSfId;
    }

    public Integer getInspSfId() {
        return inspSfId;
    }

    public void setInspSfId(Integer inspSfId) {
        this.inspSfId = inspSfId;
    }

    public String getItemShipDimensions() {
        return itemShipDimensions;
    }

    public void setItemShipDimensions(String itemShipDimensions) {
        this.itemShipDimensions = itemShipDimensions;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFabLocation() {
        return fabLocation;
    }

    public void setFabLocation(String fabLocation) {
        this.fabLocation = fabLocation;
    }

    public String getFabShopCntrlNumber() {
        return fabShopCntrlNumber;
    }

    public void setFabShopCntrlNumber(String fabShopCntrlNumber) {
        this.fabShopCntrlNumber = fabShopCntrlNumber;
    }

    public String getInspLocation() {
        return inspLocation;
    }

    public void setInspLocation(String inspLocation) {
        this.inspLocation = inspLocation;
    }

    public String getOwl() {
        return owl;
    }

    public void setOwl(String owl) {
        this.owl = owl;
    }

    public String getNcr() {
        return ncr;
    }

    public void setNcr(String ncr) {
        this.ncr = ncr;
    }

    public String getInspNcrRemarks() {
        return inspNcrRemarks;
    }

    public void setInspNcrRemarks(String inspNcrRemarks) {
        this.inspNcrRemarks = inspNcrRemarks;
    }

    public String getRevFreightValue() {
        return revFreightValue;
    }

    public void setRevFreightValue(String revFreightValue) {
        this.revFreightValue = revFreightValue;
    }

    public String getInvNumber() {
        return invNumber;
    }

    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getRevPromShipDate() {
        return revPromShipDate;
    }

    public void setRevPromShipDate(Date revPromShipDate) {
        this.revPromShipDate = revPromShipDate;
    }

    public Date getForecastEtaSiteDate() {
        return forecastEtaSiteDate;
    }

    public void setForecastEtaSiteDate(Date forecastEtaSiteDate) {
        this.forecastEtaSiteDate = forecastEtaSiteDate;
    }

    public Date getReqSiteDate() {
        return reqSiteDate;
    }

    public void setReqSiteDate(Date reqSiteDate) {
        this.reqSiteDate = reqSiteDate;
    }

    public Date getReqShipDate() {
        return reqShipDate;
    }

    public void setReqShipDate(Date reqShipDate) {
        this.reqShipDate = reqShipDate;
    }

    public Date getActualContractDate() {
        return actualContractDate;
    }

    public void setActualContractDate(Date actualContractDate) {
        this.actualContractDate = actualContractDate;
    }

    public Date getMajMatlOrderDate() {
        return majMatlOrderDate;
    }

    public void setMajMatlOrderDate(Date majMatlOrderDate) {
        this.majMatlOrderDate = majMatlOrderDate;
    }

    public Date getMajMatlRecvDate() {
        return majMatlRecvDate;
    }

    public void setMajMatlRecvDate(Date majMatlRecvDate) {
        this.majMatlRecvDate = majMatlRecvDate;
    }

    public Date getActualFabStartDate() {
        return actualFabStartDate;
    }

    public void setActualFabStartDate(Date actualFabStartDate) {
        this.actualFabStartDate = actualFabStartDate;
    }

    public Date getPromFabCompDate() {
        return promFabCompDate;
    }

    public void setPromFabCompDate(Date promFabCompDate) {
        this.promFabCompDate = promFabCompDate;
    }

    public Date getFinalInspectDate() {
        return finalInspectDate;
    }

    public void setFinalInspectDate(Date finalInspectDate) {
        this.finalInspectDate = finalInspectDate;
    }

    public Date getActualFabCompDate() {
        return actualFabCompDate;
    }

    public void setActualFabCompDate(Date actualFabCompDate) {
        this.actualFabCompDate = actualFabCompDate;
    }

    public Date getActualShipDate() {
        return actualShipDate;
    }

    public void setActualShipDate(Date actualShipDate) {
        this.actualShipDate = actualShipDate;
    }

    public Date getReadyPredicted() {
        return readyPredicted;
    }

    public void setReadyPredicted(Date readyPredicted) {
        this.readyPredicted = readyPredicted;
    }

    public Date getReadyActual() {
        return readyActual;
    }

    public void setReadyActual(Date readyActual) {
        this.readyActual = readyActual;
    }

    public Date getFiftyPercentFabComp() {
        return fiftyPercentFabComp;
    }

    public void setFiftyPercentFabComp(Date fiftyPercentFabComp) {
        this.fiftyPercentFabComp = fiftyPercentFabComp;
    }

    public Date getPredShipDate() {
        return predShipDate;
    }

    public void setPredShipDate(Date predShipDate) {
        this.predShipDate = predShipDate;
    }

    public Date getPromShipDate() {
        return promShipDate;
    }

    public void setPromShipDate(Date promShipDate) {
        this.promShipDate = promShipDate;
    }

    public Date getPredStageReadyDate() {
        return predStageReadyDate;
    }

    public void setPredStageReadyDate(Date predStageReadyDate) {
        this.predStageReadyDate = predStageReadyDate;
    }

    public Date getActualStageReadyDate() {
        return actualStageReadyDate;
    }

    public void setActualStageReadyDate(Date actualStageReadyDate) {
        this.actualStageReadyDate = actualStageReadyDate;
    }

    public Date getPredLoadedDate() {
        return predLoadedDate;
    }

    public void setPredLoadedDate(Date predLoadedDate) {
        this.predLoadedDate = predLoadedDate;
    }

    public Date getActualLoadedDate() {
        return actualLoadedDate;
    }

    public void setActualLoadedDate(Date actualLoadedDate) {
        this.actualLoadedDate = actualLoadedDate;
    }

    public Date getPredOnSiteDate() {
        return predOnSiteDate;
    }

    public void setPredOnSiteDate(Date predOnSiteDate) {
        this.predOnSiteDate = predOnSiteDate;
    }

    public Date getPromContractDate() {
        return promContractDate;
    }

    public void setPromContractDate(Date promContractDate) {
        this.promContractDate = promContractDate;
    }

    public Date getPromInspectDate() {
        return promInspectDate;
    }

    public void setPromInspectDate(Date promInspectDate) {
        this.promInspectDate = promInspectDate;
    }

    public Date getActualOnSiteDate() {
        return actualOnSiteDate;
    }

    public void setActualOnSiteDate(Date actualOnSiteDate) {
        this.actualOnSiteDate = actualOnSiteDate;
    }

    public Date getLastInfoDate() {
        return lastInfoDate;
    }

    public void setLastInfoDate(Date lastInfoDate) {
        this.lastInfoDate = lastInfoDate;
    }

    public Date getInspRelWaiverDate() {
        return inspRelWaiverDate;
    }

    public void setInspRelWaiverDate(Date inspRelWaiverDate) {
        this.inspRelWaiverDate = inspRelWaiverDate;
    }

    public Date getInspNcrDate() {
        return inspNcrDate;
    }

    public void setInspNcrDate(Date inspNcrDate) {
        this.inspNcrDate = inspNcrDate;
    }

    public Date getInspNcrReleaseDate() {
        return inspNcrReleaseDate;
    }

    public void setInspNcrReleaseDate(Date inspNcrReleaseDate) {
        this.inspNcrReleaseDate = inspNcrReleaseDate;
    }

    public Date getPredTrafficArrivalDate() {
        return predTrafficArrivalDate;
    }

    public void setPredTrafficArrivalDate(Date predTrafficArrivalDate) {
        this.predTrafficArrivalDate = predTrafficArrivalDate;
    }

    public Date getActualTrafficArrivalDate() {
        return actualTrafficArrivalDate;
    }

    public void setActualTrafficArrivalDate(Date actualTrafficArrivalDate) {
        this.actualTrafficArrivalDate = actualTrafficArrivalDate;
    }

    public Integer getIviId() {
        return iviId;
    }

    public void setIviId(Integer iviId) {
        this.iviId = iviId;
    }

    public Integer getDlId() {
        return dlId;
    }

    public void setDlId(Integer dlId) {
        this.dlId = dlId;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }
}
