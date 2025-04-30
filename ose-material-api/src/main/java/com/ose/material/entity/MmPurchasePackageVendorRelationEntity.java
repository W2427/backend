package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 采购包和供货商关系表
 */
@Entity
@Table(name = "mm_purchase_package_vendor_relation")
public class MmPurchasePackageVendorRelationEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 7147382289207594949L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "公司ID")
    @Column
    public Long companyId;

    @Schema(description = "供货商ID")
    @Column
    public Long vendorId;

    @Schema(description = "采购包ID")
    @Column
    public Long purchasePackageId;

    @Schema(description = "保密协议文件名")
    @Column
    public String confidentialityAgreementFileName;

    @Schema(description = "保密协议文件ID")
    @Column
    public Long confidentialityAgreementFileId;

    @Schema(description = "中标状态")
    @Column
    public Boolean bidStatus = false;

    @Schema(description = "供货商名称")
    @Column
    public String supplierName;

    @Schema(description = "厂家回传文件名")
    @Column
    public String manufacturerReturnsAttachmentFileName;

    @Schema(description = "厂家回传文件ID")
    @Column
    public Long manufacturerReturnsAttachmentFileId;

    @Schema(description = "确认状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private EntityStatus confirmStatus = EntityStatus.INIT;

    @Schema(description = "备注")
    @Column
    public String remark;

    @Schema(description = "打分")
    @Column
    public Integer score;

    @Schema(description = "是否回传标书")
    @Column
    public Boolean feedBack = false;

    @Schema(description = "评审信息")
    @Column
    public String review;

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

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getPurchasePackageId() {
        return purchasePackageId;
    }

    public void setPurchasePackageId(Long purchasePackageId) {
        this.purchasePackageId = purchasePackageId;
    }

    public String getConfidentialityAgreementFileName() {
        return confidentialityAgreementFileName;
    }

    public void setConfidentialityAgreementFileName(String confidentialityAgreementFileName) {
        this.confidentialityAgreementFileName = confidentialityAgreementFileName;
    }

    public Long getConfidentialityAgreementFileId() {
        return confidentialityAgreementFileId;
    }

    public void setConfidentialityAgreementFileId(Long confidentialityAgreementFileId) {
        this.confidentialityAgreementFileId = confidentialityAgreementFileId;
    }

    public Boolean getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(Boolean bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getManufacturerReturnsAttachmentFileName() {
        return manufacturerReturnsAttachmentFileName;
    }

    public void setManufacturerReturnsAttachmentFileName(String manufacturerReturnsAttachmentFileName) {
        this.manufacturerReturnsAttachmentFileName = manufacturerReturnsAttachmentFileName;
    }

    public Long getManufacturerReturnsAttachmentFileId() {
        return manufacturerReturnsAttachmentFileId;
    }

    public void setManufacturerReturnsAttachmentFileId(Long manufacturerReturnsAttachmentFileId) {
        this.manufacturerReturnsAttachmentFileId = manufacturerReturnsAttachmentFileId;
    }

    public EntityStatus getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(EntityStatus confirmStatus) {
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
}
