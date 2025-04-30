package com.ose.tasks.dto.drawing;

import java.util.Date;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DrawingDetailDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "实际发图日期")
    private Date acturalDrawingIssueDate;

    @Schema(description = "版本号")
    private String rev;

    @Schema(description = "生产接收日期")
    private Date productionReceivingDate;

    @Schema(description = "发放记录卡编号")
    private String issueCardNo;

    @Schema(description = "回收记录")
    private String returnRecord;

    @Schema(description = "设计评审变更单号")
    private String designChangeReviewForm;

    @Schema(description = "变更通知单号")
    private String changeNoticeNo;

    @Schema(description = "配布份数")
    private int quantity;

    @Schema(description = "打印份数 (配布份数-1)")
    private int printing;

    @Schema(description = "用纸量(A3)")
    private float paperUse;

    @Schema(description = "纸张数量 (打印份数*用纸量)")
    private float paperAmount;

    @Schema(description = "图纸审核编号")
    private String auditNo;

    public Date getActuralDrawingIssueDate() {
        return acturalDrawingIssueDate;
    }

    public void setActuralDrawingIssueDate(Date acturalDrawingIssueDate) {
        this.acturalDrawingIssueDate = acturalDrawingIssueDate;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public Date getProductionReceivingDate() {
        return productionReceivingDate;
    }

    public void setProductionReceivingDate(Date productionReceivingDate) {
        this.productionReceivingDate = productionReceivingDate;
    }

    public String getIssueCardNo() {
        return issueCardNo;
    }

    public void setIssueCardNo(String issueCardNo) {
        this.issueCardNo = issueCardNo;
    }

    public String getReturnRecord() {
        return returnRecord;
    }

    public void setReturnRecord(String returnRecord) {
        this.returnRecord = returnRecord;
    }

    public String getDesignChangeReviewForm() {
        return designChangeReviewForm;
    }

    public void setDesignChangeReviewForm(String designChangeReviewForm) {
        this.designChangeReviewForm = designChangeReviewForm;
    }

    public String getChangeNoticeNo() {
        return changeNoticeNo;
    }

    public void setChangeNoticeNo(String changeNoticeNo) {
        this.changeNoticeNo = changeNoticeNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrinting() {
        return printing;
    }

    public void setPrinting(int printing) {
        this.printing = printing;
    }

    public float getPaperUse() {
        return paperUse;
    }

    public void setPaperUse(float paperUse) {
        this.paperUse = paperUse;
    }

    public float getPaperAmount() {
        return paperAmount;
    }

    public void setPaperAmount(float paperAmount) {
        this.paperAmount = paperAmount;
    }

    public String getAuditNo() {
        return auditNo;
    }

    public void setAuditNo(String auditNo) {
        this.auditNo = auditNo;
    }

}
