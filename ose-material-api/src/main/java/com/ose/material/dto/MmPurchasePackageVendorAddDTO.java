package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 采购包添加供货商DTO
 */
public class MmPurchasePackageVendorAddDTO extends BaseDTO {

    private static final long serialVersionUID = -6367911889722744733L;

    @Schema(description = "Vendor ID列表")
    public List<Long> vendorIds;

    @Schema(description = "Vendor关系表 ID列表")
    public List<Long> vendorRelationIds;

    @Schema(description = "确认状态")
    public String confirmStatus;

    @Schema(description = "备注")
    public String remark;

    @Schema(description = "打分")
    public Integer score;

    @Schema(description = "反馈")
    public Boolean feedBack;

    @Schema(description = "评审")
    public String review;

    @Schema(description = "")
    public Boolean bidStatus;

    public List<Long> getVendorIds() {
        return vendorIds;
    }

    public void setVendorIds(List<Long> vendorIds) {
        this.vendorIds = vendorIds;
    }

    public List<Long> getVendorRelationIds() {
        return vendorRelationIds;
    }

    public void setVendorRelationIds(List<Long> vendorRelationIds) {
        this.vendorRelationIds = vendorRelationIds;
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
