package com.ose.test.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

/**
 * 入库清单DTO
 */
public class FMaterialReceiveReceiptDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    private Long fmstId;

    private Long relnId;

    @Schema(description = "默认值：N")
    private String esiStatus;

    private String mrrNumber;

    private String revisonId;

    private String recvType;

    //仓库
    private String whId;

    private String whCode;

    //库位
    private String locId;

    private String locCode;

    //材料状态
    private String smstId;

    private String smstCode;

    //null
    private String sgId;

    //合同主键号
    private String spmPohId;

    private String spmPohNumber;

    //放行单号
    private String spmRelnId;

    private String spmRelnNumber;

    //材料接收日期
    private Date matlRecvDate;

    //接收人（可空）
    private String recvBy;

    //运输人（可空）
    private String shipper;

    //运输单号（可空）
    private String shipperRefNo;

    //'N'
    private String poplIshByProc;

    //NULL
    private String bnlId;

    private String shortDesc;

    //长描述
    private String description;

    private List<FMaterialReceiveReceiptDetailDTO> details;

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public String getMrrNumber() {
        return mrrNumber;
    }

    public void setMrrNumber(String mrrNumber) {
        this.mrrNumber = mrrNumber;
    }

    public String getRevisonId() {
        return revisonId;
    }

    public void setRevisonId(String revisonId) {
        this.revisonId = revisonId;
    }

    public String getRecvType() {
        return recvType;
    }

    public void setRecvType(String recvType) {
        this.recvType = recvType;
    }

    public String getWhId() {
        return whId;
    }

    public void setWhId(String whId) {
        this.whId = whId;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getSmstId() {
        return smstId;
    }

    public void setSmstId(String smstId) {
        this.smstId = smstId;
    }

    public String getSmstCode() {
        return smstCode;
    }

    public void setSmstCode(String smstCode) {
        this.smstCode = smstCode;
    }

    public String getSgId() {
        return sgId;
    }

    public void setSgId(String sgId) {
        this.sgId = sgId;
    }

    public String getSpmRelnId() {
        return spmRelnId;
    }

    public void setSpmRelnId(String spmRelnId) {
        this.spmRelnId = spmRelnId;
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

    public String getBnlId() {
        return bnlId;
    }

    public void setBnlId(String bnlId) {
        this.bnlId = bnlId;
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

    public List<FMaterialReceiveReceiptDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<FMaterialReceiveReceiptDetailDTO> details) {
        this.details = details;
    }

    public String getSpmPohId() {
        return spmPohId;
    }

    public void setSpmPohId(String spmPohId) {
        this.spmPohId = spmPohId;
    }

    public String getSpmPohNumber() {
        return spmPohNumber;
    }

    public void setSpmPohNumber(String spmPohNumber) {
        this.spmPohNumber = spmPohNumber;
    }

    public String getSpmRelnNumber() {
        return spmRelnNumber;
    }

    public void setSpmRelnNumber(String spmRelnNumber) {
        this.spmRelnNumber = spmRelnNumber;
    }

    public Long getFmstId() {
        return fmstId;
    }

    public void setFmstId(Long fmstId) {
        this.fmstId = fmstId;
    }

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
    }

}
