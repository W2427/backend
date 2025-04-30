package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 入库单表
 */
@Entity
@Table(name = "m_matl_recv_rpts")
@NamedQuery(name = "MatlRecvRptsEntity.findAll", query = "SELECT a FROM MatlRecvRptsEntity a")
public class MatlRecvRptsEntity extends BaseDTO {

    private static final long serialVersionUID = -2544195386498526576L;
    @Id
    @Column(name = "mrr_id", nullable = false)
    private Integer mrrId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "mrr_number", nullable = false)
    private String mrrNumber;

    @Column(name = "revision_id", nullable = false)
    private Integer revisionId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "mrr_create_date", nullable = false)
    private Date mrrCreateDate;

    @Column(name = "recv_type", nullable = false)
    private String recvType;

    @Column(name = "selling_price", nullable = false)
    private Double sellingPrice;

    @Column(name = "free_of_charge_ind", nullable = false)
    private String freeOfChargeInd;

    @Column(name = "erp_set_ind", nullable = false)
    private String erpSetInd;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "currency_id")
    private Integer currencyId;

    @Column(name = "wh_id")
    private Integer whId;

    @Column(name = "bnl_id")
    private Integer bnlId;

    @Column(name = "loc_id")
    private Integer locId;

    @Column(name = "smst_id")
    private Integer smstId;

    @Column(name = "sg_id")
    private Integer sgId;

    @Column(name = "poh_id")
    private Integer pohId;

    @Column(name = "tmr_id")
    private Integer tmrId;

    @Column(name = "reln_id")
    private Integer relnId;

    @Column(name = "pck_id")
    private Integer pckId;

    @Column(name = "mtr_id")
    private Integer mtrId;

    @Column(name = "posted_date")
    private Date postedDate;

    @Column(name = "matl_recv_date")
    private Date matlRecvDate;

    @Column(name = "recv_by")
    private String recvBy;

    @Column(name = "packing_list_number")
    private String packingListNumber;

    @Column(name = "shipper")
    private String shipper;

    @Column(name = "shipper_ref_no")
    private String shipperRefNo;

    @Column(name = "rental_ind", nullable = false)
    private String rentalInd;

    @Column(name = "ready_for_approval_ind", nullable = false)
    private String readyForApprovalInd;

    @Column(name = "plate_ind", nullable = false)
    private String plateInd;

    public Integer getMrrId() {
        return mrrId;
    }

    public void setMrrId(Integer mrrId) {
        this.mrrId = mrrId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getMrrNumber() {
        return mrrNumber;
    }

    public void setMrrNumber(String mrrNumber) {
        this.mrrNumber = mrrNumber;
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

    public Date getMrrCreateDate() {
        return mrrCreateDate;
    }

    public void setMrrCreateDate(Date mrrCreateDate) {
        this.mrrCreateDate = mrrCreateDate;
    }

    public String getRecvType() {
        return recvType;
    }

    public void setRecvType(String recvType) {
        this.recvType = recvType;
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

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
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

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public Integer getSmstId() {
        return smstId;
    }

    public void setSmstId(Integer smstId) {
        this.smstId = smstId;
    }

    public Integer getSgId() {
        return sgId;
    }

    public void setSgId(Integer sgId) {
        this.sgId = sgId;
    }

    public Integer getPohId() {
        return pohId;
    }

    public void setPohId(Integer pohId) {
        this.pohId = pohId;
    }

    public Integer getTmrId() {
        return tmrId;
    }

    public void setTmrId(Integer tmrId) {
        this.tmrId = tmrId;
    }

    public Integer getRelnId() {
        return relnId;
    }

    public void setRelnId(Integer relnId) {
        this.relnId = relnId;
    }

    public Integer getPckId() {
        return pckId;
    }

    public void setPckId(Integer pckId) {
        this.pckId = pckId;
    }

    public Integer getMtrId() {
        return mtrId;
    }

    public void setMtrId(Integer mtrId) {
        this.mtrId = mtrId;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public Date getMatlRecvDate() {
        return matlRecvDate;
    }

    public void setMatlRecvDate(Date matlRecvDate) {
        this.matlRecvDate = matlRecvDate;
    }

    public String getRecvBy() {
        return recvBy;
    }

    public void setRecvBy(String recvBy) {
        this.recvBy = recvBy;
    }

    public String getPackingListNumber() {
        return packingListNumber;
    }

    public void setPackingListNumber(String packingListNumber) {
        this.packingListNumber = packingListNumber;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperRefNo() {
        return shipperRefNo;
    }

    public void setShipperRefNo(String shipperRefNo) {
        this.shipperRefNo = shipperRefNo;
    }

    public String getRentalInd() {
        return rentalInd;
    }

    public void setRentalInd(String rentalInd) {
        this.rentalInd = rentalInd;
    }

    public String getReadyForApprovalInd() {
        return readyForApprovalInd;
    }

    public void setReadyForApprovalInd(String readyForApprovalInd) {
        this.readyForApprovalInd = readyForApprovalInd;
    }

    public String getPlateInd() {
        return plateInd;
    }

    public void setPlateInd(String plateInd) {
        this.plateInd = plateInd;
    }
}
