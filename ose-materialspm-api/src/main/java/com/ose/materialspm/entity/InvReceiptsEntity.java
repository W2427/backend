package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 库存接收表
 */
@Entity
@Table(name = "m_inv_receipts")
@NamedQuery(name = "InvReceiptsEntity.findAll", query = "SELECT a FROM InvReceiptsEntity a")
public class InvReceiptsEntity extends BaseDTO {

    private static final long serialVersionUID = -5926788978418529373L;
    @Id
    @Column(name = "inv_receipt_id", nullable = false)
    private Integer invReceiptId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "mrr_id", nullable = false)
    private Integer mrrId;

    @Column(name = "irp_id")
    private Integer irpId;

    @Column(name = "loc_id", nullable = false)
    private Integer locId;

    @Column(name = "wh_id", nullable = false)
    private Integer whId;

    @Column(name = "bnl_id")
    private Integer bnlId;

    @Column(name = "smst_id", nullable = false)
    private Integer smstId;

    @Column(name = "ident", nullable = false)
    private Integer ident;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "recv_qty", nullable = false)
    private Double recvQty;

    @Column(name = "recv_date", nullable = false)
    private Date recvDate;

    @Column(name = "xdim", nullable = false)
    private Double xdim;

    @Column(name = "ydim", nullable = false)
    private Double ydim;

    @Column(name = "upd_rev_ind", nullable = false)
    private String updRevInd;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "tag_number", nullable = false)
    private String tagNumber;

    @Column(name = "heat_id")
    private Integer heatId;

    @Column(name = "itr_id")
    private Integer itrId;

    @Column(name = "item_ship_id")
    private Integer itemShipId;

    @Column(name = "plate_id")
    private Integer plateId;

    @Column(name = "ivi_id")
    private Integer iviId;

    @Column(name = "last_inv_receipt_id")
    private Integer lastInvReceiptId;

    @Column(name = "ident_deviation")
    private String identDeviation;

    @Column(name = "original_loc_id")
    private Integer originalLocId;

    @Column(name = "comments")
    private String comments;

    @Column(name = "original_item_ship_id")
    private Integer originalItemShipId;

    @Column(name = "site_created_ship_ind")
    private String siteCreatedShipInd;

    @Column(name = "ready_to_post", nullable = false)
    private String readyToPost;

    @Column(name = "pp_inv_receipt_id")
    private Integer ppInvReceiptId;

    @Column(name = "pck_id")
    private Integer pckId;

    public Integer getInvReceiptId() {
        return invReceiptId;
    }

    public void setInvReceiptId(Integer invReceiptId) {
        this.invReceiptId = invReceiptId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getMrrId() {
        return mrrId;
    }

    public void setMrrId(Integer mrrId) {
        this.mrrId = mrrId;
    }

    public Integer getIrpId() {
        return irpId;
    }

    public void setIrpId(Integer irpId) {
        this.irpId = irpId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getBnlId() {
        return bnlId;
    }

    public void setBnlId(Integer bnlId) {
        this.bnlId = bnlId;
    }

    public Integer getSmstId() {
        return smstId;
    }

    public void setSmstId(Integer smstId) {
        this.smstId = smstId;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Double getRecvQty() {
        return recvQty;
    }

    public void setRecvQty(Double recvQty) {
        this.recvQty = recvQty;
    }

    public Date getRecvDate() {
        return recvDate;
    }

    public void setRecvDate(Date recvDate) {
        this.recvDate = recvDate;
    }

    public Double getXdim() {
        return xdim;
    }

    public void setXdim(Double xdim) {
        this.xdim = xdim;
    }

    public Double getYdim() {
        return ydim;
    }

    public void setYdim(Double ydim) {
        this.ydim = ydim;
    }

    public String getUpdRevInd() {
        return updRevInd;
    }

    public void setUpdRevInd(String updRevInd) {
        this.updRevInd = updRevInd;
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

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Integer getHeatId() {
        return heatId;
    }

    public void setHeatId(Integer heatId) {
        this.heatId = heatId;
    }

    public Integer getItrId() {
        return itrId;
    }

    public void setItrId(Integer itrId) {
        this.itrId = itrId;
    }

    public Integer getItemShipId() {
        return itemShipId;
    }

    public void setItemShipId(Integer itemShipId) {
        this.itemShipId = itemShipId;
    }

    public Integer getPlateId() {
        return plateId;
    }

    public void setPlateId(Integer plateId) {
        this.plateId = plateId;
    }

    public Integer getIviId() {
        return iviId;
    }

    public void setIviId(Integer iviId) {
        this.iviId = iviId;
    }

    public Integer getLastInvReceiptId() {
        return lastInvReceiptId;
    }

    public void setLastInvReceiptId(Integer lastInvReceiptId) {
        this.lastInvReceiptId = lastInvReceiptId;
    }

    public String getIdentDeviation() {
        return identDeviation;
    }

    public void setIdentDeviation(String identDeviation) {
        this.identDeviation = identDeviation;
    }

    public Integer getOriginalLocId() {
        return originalLocId;
    }

    public void setOriginalLocId(Integer originalLocId) {
        this.originalLocId = originalLocId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getOriginalItemShipId() {
        return originalItemShipId;
    }

    public void setOriginalItemShipId(Integer originalItemShipId) {
        this.originalItemShipId = originalItemShipId;
    }

    public String getSiteCreatedShipInd() {
        return siteCreatedShipInd;
    }

    public void setSiteCreatedShipInd(String siteCreatedShipInd) {
        this.siteCreatedShipInd = siteCreatedShipInd;
    }

    public String getReadyToPost() {
        return readyToPost;
    }

    public void setReadyToPost(String readyToPost) {
        this.readyToPost = readyToPost;
    }

    public Integer getPpInvReceiptId() {
        return ppInvReceiptId;
    }

    public void setPpInvReceiptId(Integer ppInvReceiptId) {
        this.ppInvReceiptId = ppInvReceiptId;
    }

    public Integer getPckId() {
        return pckId;
    }

    public void setPckId(Integer pckId) {
        this.pckId = pckId;
    }
}
