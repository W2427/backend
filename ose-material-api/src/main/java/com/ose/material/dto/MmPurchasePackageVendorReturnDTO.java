package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 采购包里供货商返回对象
 */
public class MmPurchasePackageVendorReturnDTO extends BaseDTO {

    private static final long serialVersionUID = 6642152058457713813L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "请购单ID")
    private Long companyId;

    @Schema(description = "采购包和供应商关系表ID")
    private Long id;

    @Schema(description = "采购包ID")
    private Long purchasePackageId;

    @Schema(description = "采购包编号")
    private String pwpsNumber;

    @Schema(description = "供货商ID")
    private Long vendorId;

    @Schema(description = "供货商No")
    private String vendorNo;

    @Schema(description = "供货商Name")
    private String vendorName;

    @Schema(description = "确认状态")
    private String confirmStatus;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "得分")
    private Integer score;

    @Schema(description = "反馈")
    private Boolean feedBack;

    @Schema(description = "评审")
    private String review;

    @Schema(description = "")
    private Boolean bidStatus;

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getPurchasePackageId() {
        return purchasePackageId;
    }

    public void setPurchasePackageId(Long purchasePackageId) {
        this.purchasePackageId = purchasePackageId;
    }

    public String getPwpsNumber() {
        return pwpsNumber;
    }

    public void setPwpsNumber(String pwpsNumber) {
        this.pwpsNumber = pwpsNumber;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(Boolean feedBack) {
        this.feedBack = feedBack;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Boolean getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(Boolean bidStatus) {
        this.bidStatus = bidStatus;
    }
}
