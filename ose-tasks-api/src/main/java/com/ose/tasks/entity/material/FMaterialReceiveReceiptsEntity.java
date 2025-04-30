package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 入库报告实体类。
 */
@Entity
@Table(name = "mat_f_material_receive_receipt")
public class FMaterialReceiveReceiptsEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "入库报告")
    private String fmrrCode;

    @Schema(description = "是否已经入库")
    private Boolean savedFlag;

    @Schema(description = "是否反写spm")
    private Boolean spmFlag;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "专业")
    private String dpCode;

    @Schema(description = "SPM入库单ID")
    private String spmMrrId;

    @Schema(description = "默认值:0")
    private String spmRevisonId;

    @Schema(description = "开箱检验单ID")
    private Long obirId;

    @Schema(description = "默认值:'O'")
    private String recvType;

    @Schema(description = "仓库ID")
    private String whId;

    @Schema(description = "仓库")
    private String whCode;

    @Schema(description = "库位ID")
    private String locId;

    @Schema(description = "库位")
    private String locCode;

    @Schema(description = "材料状态ID")
    private String smstId;

    @Schema(description = "材料状态")
    private String smstCode;

    @Schema(description = "默认值:NULL")
    private String sgId;

    @Schema(description = "合同ID")
    private String spmPohId;

    @Schema(description = "计划放行单ID")
    private String spmRelnId;

    @Schema(description = "合同号")
    private String spmPohNumber;

    @Schema(description = "计划放行单号")
    private String spmRelnNumber;

    @Schema(description = "材料接收日期")
    private Date matlRecvDate;

    @Schema(description = "接收人（可空）")
    private String recvBy;

    @Schema(description = "运输人（可空）")
    private String shipper;

    @Schema(description = "运输单号（可空）")
    private String shipperRefNo;

    @Schema(description = "默认值:'N'")
    private String poplIshByProc;

    @Schema(description = "默认值:'N'")
    private String esiStatus;

    @Schema(description = "默认值:NULL")
    private String bnlId;

    @Schema(description = "短描述")
    @Column(length = 500)
    private String shortDesc;

    @Schema(description = "长描述")
    private String description;

    @Transient
    private Long relnId;

    @Transient
    @Schema(description = "材料盘点 ID")
    private Long fmstId;

    @Transient
    @Schema(description = "材料批次号")
    private String materialBatchNumber;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getFmrrCode() {
        return fmrrCode;
    }

    public void setFmrrCode(String fmrrCode) {
        this.fmrrCode = fmrrCode;
    }

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public String getRecvType() {
        return recvType;
    }

    public void setRecvType(String recvType) {
        this.recvType = recvType;
    }

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
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

    public Long getObirId() {
        return obirId;
    }

    public void setObirId(Long obirId) {
        this.obirId = obirId;
    }

    public String getWhId() {
        return whId;
    }

    public void setWhId(String whId) {
        this.whId = whId;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getSpmMrrId() {
        return spmMrrId;
    }

    public void setSpmMrrId(String spmMrrId) {
        this.spmMrrId = spmMrrId;
    }

    public String getSpmRevisonId() {
        return spmRevisonId;
    }

    public void setSpmRevisonId(String spmRevisonId) {
        this.spmRevisonId = spmRevisonId;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    @JsonProperty(value = "obirId", access = READ_ONLY)
    public ReferenceData getObirIdRef() {
        return this.obirId == null
            ? null
            : new ReferenceData(this.obirId);
    }

    @JsonProperty(value = "fmstId", access = READ_ONLY)
    public ReferenceData getFmstIdRef() {
        return this.fmstId == null
            ? null
            : new ReferenceData(this.fmstId);
    }

    @JsonProperty(value = "relnId", access = READ_ONLY)
    public ReferenceData getRelnIdRef() {
        return this.relnId == null
            ? null
            : new ReferenceData(this.relnId);
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

    public String getSpmPohId() {
        return spmPohId;
    }

    public void setSpmPohId(String spmPohId) {
        this.spmPohId = spmPohId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
    }

    public Boolean getSavedFlag() {
        return savedFlag;
    }

    public void setSavedFlag(Boolean savedFlag) {
        this.savedFlag = savedFlag;
    }

    public Boolean getSpmFlag() {
        return spmFlag;
    }

    public void setSpmFlag(Boolean spmFlag) {
        this.spmFlag = spmFlag;
    }
}
