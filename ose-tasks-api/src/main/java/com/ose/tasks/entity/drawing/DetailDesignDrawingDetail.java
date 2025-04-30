package com.ose.tasks.entity.drawing;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 详细设计图纸详情
 */
@Entity
@Table(name = "detail_design_drawing_detail")
public class DetailDesignDrawingDetail extends BaseBizEntity {

    private static final long serialVersionUID = -7837891255273680991L;

    @Schema(description = "详细设计清单id")
    private Long detailDesignListId;

    @Schema(description = "设计变更号")
    private String designChangeNumber;

    @Schema(description = "日期")
    private String date;

    @Schema(description = "旧文件号")
    private String oldDocumentNumber;

    @Schema(description = "文档标题")
    private String documentTitle;

    @Schema(description = "有效版本")
    private String activeRevision;

    @Schema(description = "版本日期")
    private String revDate;

    @Schema(description = "文档状态")
    private String documentStatus;

    @Schema(description = "计划要求提交时间")
    private String planRequiredTime;

    @Schema(description = "详细设计实际出图时间")
    private String actualDrawingTime;

    public Long getDetailDesignListId() {
        return detailDesignListId;
    }

    public void setDetailDesignListId(Long detailDesignListId) {
        this.detailDesignListId = detailDesignListId;
    }

    public String getDesignChangeNumber() {
        return designChangeNumber;
    }

    public void setDesignChangeNumber(String designChangeNumber) {
        this.designChangeNumber = designChangeNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOldDocumentNumber() {
        return oldDocumentNumber;
    }

    public void setOldDocumentNumber(String oldDocumentNumber) {
        this.oldDocumentNumber = oldDocumentNumber;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getActiveRevision() {
        return activeRevision;
    }

    public void setActiveRevision(String activeRevision) {
        this.activeRevision = activeRevision;
    }

    public String getRevDate() {
        return revDate;
    }

    public void setRevDate(String revDate) {
        this.revDate = revDate;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getPlanRequiredTime() {
        return planRequiredTime;
    }

    public void setPlanRequiredTime(String planRequiredTime) {
        this.planRequiredTime = planRequiredTime;
    }

    public String getActualDrawingTime() {
        return actualDrawingTime;
    }

    public void setActualDrawingTime(String actualDrawingTime) {
        this.actualDrawingTime = actualDrawingTime;
    }


}
