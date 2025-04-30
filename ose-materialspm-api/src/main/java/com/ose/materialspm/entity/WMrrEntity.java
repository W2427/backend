package com.ose.materialspm.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.ose.dto.BaseDTO;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "W_MRR")
@NamedQuery(name = "WMrrEntity.findAll", query = "SELECT a FROM WMrrEntity a")
//@NamedStoredProcedureQuery(name = "TransMrr", procedureName = "W_PCK_SPM_OSE.TRANS_MRR", parameters = {
//    @StoredProcedureParameter(mode = ParameterMode.IN, name = "P_MRR_NUMBER", type = String.class),
//    @StoredProcedureParameter(mode = ParameterMode.IN, name = "P_PROJ_ID", type = String.class),
//    @StoredProcedureParameter(mode = ParameterMode.OUT, name = "P_RESULT", type = Integer.class),
//    @StoredProcedureParameter(mode = ParameterMode.OUT, name = "P_MESSAGE", type = String.class)})
public class WMrrEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "MRR_NUMBER")
    private String mrrNumber;

    @Column(name = "REVISION_ID")
    private int revisionId;

    @Column(name = "ESI_STATUS")
    private String esiStatus;

    @Column(name = "MRR_CREATE_DATE")
    private Date mrrCreateDate;

    @Column(name = "RECV_TYPE")
    private String recvType;

    @Column(name = "WH_ID")
    private int whId;

    @Column(name = "LOC_ID")
    private int locId;

    @Column(name = "SMST_ID")
    private int smstId;

    @Column(name = "SG_ID")
    private Integer sgId;

    @Column(name = "POH_ID")
    private int pohId;

    @Column(name = "RELN_ID")
    private int relnId;

    @Column(name = "MATL_RECV_DATE")
    private Date matlRecvDate;

    @Column(name = "RECV_BY")
    private String recvBy;

    @Column(name = "SHIPPER")
    private String shipper;

    @Column(name = "SHIPPER_REF_NO")
    private String shipperRefNo;

    @Column(name = "POPL_ISH_BY_PROC")
    private String poplIshByProc;

    @Column(name = "BNL_ID")
    private Integer bnlId;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "SHORT_DESC")
    private String shortDesc;

    @Column(name = "DESCRIPTION")
    private String description;

    public WMrrEntity() {

    }

    public String getMrrNumber() {
        return mrrNumber;
    }

    public void setMrrNumber(String mrrNumber) {
        this.mrrNumber = mrrNumber;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
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

    public int getWhId() {
        return whId;
    }

    public void setWhId(int whId) {
        this.whId = whId;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }

    public int getSmstId() {
        return smstId;
    }

    public void setSmstId(int smstId) {
        this.smstId = smstId;
    }

    public Integer getSgId() {
        return sgId;
    }

    public void setSgId(Integer sgId) {
        this.sgId = sgId;
    }

    public int getPohId() {
        return pohId;
    }

    public void setPohId(int pohId) {
        this.pohId = pohId;
    }

    public int getRelnId() {
        return relnId;
    }

    public void setRelnId(int relnId) {
        this.relnId = relnId;
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

    public String getPoplIshByProc() {
        return poplIshByProc;
    }

    public void setPoplIshByProc(String poplIshByProc) {
        this.poplIshByProc = poplIshByProc;
    }

    public Integer getBnlId() {
        return bnlId;
    }

    public void setBnlId(Integer bnlId) {
        this.bnlId = bnlId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
