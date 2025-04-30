package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class SubDrawingCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "主图版本")
    private String drawingVersion;

    @Schema(description = "主图详情id")
    private Long drawingDetailId;

    @Schema(description = "状态")
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @Schema(description = "状态")
    @Enumerated(EnumType.STRING)
    private EntityStatus isRedMark;

    @Schema(description = "校审状态")
    @Enumerated(EnumType.STRING)
    private DrawingReviewStatus reviewStatus;

    @Schema(description = "校审节点")
    @Enumerated(EnumType.STRING)
    private ProofreadDrawingListCriteriaDTO.ProofreadType proofreadType;

    @Schema(description = "按图号排序")
    private boolean orderByNo = false;

    @Schema(description = "工序名称")
    private String process;

    @Schema(description = "流程ID")
    private Long actInstId;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "全选")
    private boolean fetchAll = false;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isOrderByNo() {
        return orderByNo;
    }

    public void setOrderByNo(boolean orderByNo) {
        this.orderByNo = orderByNo;
    }

    public DrawingReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(DrawingReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public String getDrawingVersion() {
        return drawingVersion;
    }

    public void setDrawingVersion(String drawingVersion) {
        this.drawingVersion = drawingVersion;
    }

    public Long getDrawingDetailId() {
        return drawingDetailId;
    }

    public void setDrawingDetailId(Long drawingDetailId) {
        this.drawingDetailId = drawingDetailId;
    }

    public ProofreadDrawingListCriteriaDTO.ProofreadType getProofreadType() {
        return proofreadType;
    }

    public void setProofreadType(ProofreadDrawingListCriteriaDTO.ProofreadType proofreadType) {
        this.proofreadType = proofreadType;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public EntityStatus getIsRedMark() {
        return isRedMark;
    }

    public void setIsRedMark(EntityStatus isRedMark) {
        this.isRedMark = isRedMark;
    }

    public boolean isFetchAll() {
        return fetchAll;
    }

    public void setFetchAll(boolean fetchAll) {
        this.fetchAll = fetchAll;
    }
}
