package com.ose.materialspm.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

/**
 * 出库清单DTO
 */
public class FMaterialIssueReceiptDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "默认值：N")
    private String esiStatus;

    @Schema(description = "出库单号")
    private String mirNumber;

    @Schema(description = "版本")
    private String revisionId;

    @Schema(description = "预测预留ID")
    private String fahId;

    @Schema(description = "单据创建时间")
    private Date mirCreateDate;

    @Schema(description = "发布时间")
    private Date issueDate;

    @Schema(description = "发料人")
    private String issueBy;

    @Schema(description = "施工单位ID")
    private String companyId;

    @Schema(description = "仓库ID")
    private Integer whId;

    @Schema(description = "库位ID")
    private Integer locId;

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

    private List<FMaterialIssueReceiptDetailDTO> details;

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public String getMirNumber() {
        return mirNumber;
    }

    public void setMirNumber(String mirNumber) {
        this.mirNumber = mirNumber;
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

    public Date getMirCreateDate() {
        return mirCreateDate;
    }

    public void setMirCreateDate(Date mirCreateDate) {
        this.mirCreateDate = mirCreateDate;
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

    public List<FMaterialIssueReceiptDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<FMaterialIssueReceiptDetailDTO> details) {
        this.details = details;
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
}
