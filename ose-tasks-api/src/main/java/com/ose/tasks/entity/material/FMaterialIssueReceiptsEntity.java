package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 入库报告实体类。
 */
@Entity
@Table(name = "mat_f_material_issue_receipt")
public class FMaterialIssueReceiptsEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "出库清单号")
    private String fmirCode;

    @Schema(description = "SPM是否已经出库")
    private Boolean spmSavedFlg = false;

    @Schema(description = "是否已经出库")
    private Boolean finished = false;

    @Schema(description = "是否已经入余料库")
    private Boolean issueSurplusStatus = false;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "领料单ID")
    private Long fmreqId;

    @Schema(description = "默认值：N")
    private String esiStatus;

    @Schema(description = "SPM 出库单ID")
    private String spmMirId;

    @Schema(description = "SPM 出库单号")
    private String spmMirNumber;

    @Schema(description = "版本")
    private String revisionId;

    @Schema(description = "预测预留ID")
    private String fahId;

    @Schema(description = "预测预留")
    private String fahCode;

    @Schema(description = "发布时间")
    private Date issueDate;

    @Schema(description = "发料人")
    private String issueBy;

    @Schema(description = "施工单位ID")
    private String companyId;

    @Schema(description = "施工单位")
    private String companyCode;

    @Schema(description = "仓库ID")
    private Integer whId;

    @Schema(description = "仓库")
    private String whCode;

    @Schema(description = "库位ID")
    private Integer locId;

    @Schema(description = "库位")
    private String locCode;

    @Schema(description = "预测预留:R 直接发料:D")
    private String mirType;

    @Schema(description = "预测预留:R 直接发料:R")
    private String issueType;

    @Schema(description = "默认值：N")
    private String poplIssByProc;

    @Schema(description = "默认值：N")
    private String siteStatInd;

    @Schema(description = "默认值：NULL")
    private String bnlId;

    @Schema(description = "短描述")
    private String shortDesc;

    @Schema(description = "长描述")
    private String description;

    @Schema(description = "")
    private String spmRunNumber;

    @Transient
    private String taskId;

    @Transient
    private String taskDefKey;

    @Transient
    @Schema(description = "材料准备单ID")
    private Long fmpId;

    @Schema(description = "结构套料编号")
    private String materialStructureNestingNo;

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

    public String getFmirCode() {
        return fmirCode;
    }

    public void setFmirCode(String fmirCode) {
        this.fmirCode = fmirCode;
    }

    public Boolean getSpmSavedFlg() {
        return spmSavedFlg;
    }

    public void setSpmSavedFlg(Boolean spmSavedFlg) {
        this.spmSavedFlg = spmSavedFlg;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Long getFmreqId() {
        return fmreqId;
    }

    public void setFmreqId(Long fmreqId) {
        this.fmreqId = fmreqId;
    }

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public String getSpmMirId() {
        return spmMirId;
    }

    public void setSpmMirId(String spmMirId) {
        this.spmMirId = spmMirId;
    }

    public String getSpmMirNumber() {
        return spmMirNumber;
    }

    public void setSpmMirNumber(String spmMirNumber) {
        this.spmMirNumber = spmMirNumber;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public String getFahId() {
        return fahId;
    }

    public void setFahId(String fahId) {
        this.fahId = fahId;
    }

    public String getFahCode() {
        return fahCode;
    }

    public void setFahCode(String fahCode) {
        this.fahCode = fahCode;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public String getMirType() {
        return mirType;
    }

    public void setMirType(String mirType) {
        this.mirType = mirType;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getPoplIssByProc() {
        return poplIssByProc;
    }

    public void setPoplIssByProc(String poplIssByProc) {
        this.poplIssByProc = poplIssByProc;
    }

    public String getSiteStatInd() {
        return siteStatInd;
    }

    public void setSiteStatInd(String siteStatInd) {
        this.siteStatInd = siteStatInd;
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

    public String getSpmRunNumber() {
        return spmRunNumber;
    }

    public void setSpmRunNumber(String spmRunNumber) {
        this.spmRunNumber = spmRunNumber;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public Long getFmpId() {
        return fmpId;
    }

    public void setFmpId(Long fmpId) {
        this.fmpId = fmpId;
    }

    @Schema(description = "领料单信息")
    @JsonProperty(value = "fmreqId")
    public ReferenceData getFmreqIdRef() {
        return this.fmreqId == null
            ? null
            : new ReferenceData(this.fmreqId);
    }

    @Schema(description = "材料准备信息")
    @JsonProperty(value = "fmpId")
    public ReferenceData getFmpIdRef() {
        return this.fmpId == null
            ? null
            : new ReferenceData(this.fmpId);
    }

    public String getMaterialStructureNestingNo() {
        return materialStructureNestingNo;
    }

    public void setMaterialStructureNestingNo(String materialStructureNestingNo) {
        this.materialStructureNestingNo = materialStructureNestingNo;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Boolean getIssueSurplusStatus() {
        return issueSurplusStatus;
    }

    public void setIssueSurplusStatus(Boolean issueSurplusStatus) {
        this.issueSurplusStatus = issueSurplusStatus;
    }
}
