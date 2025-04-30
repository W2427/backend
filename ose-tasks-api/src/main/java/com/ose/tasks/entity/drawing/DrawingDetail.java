package com.ose.tasks.entity.drawing;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 图纸详情
 */
@Entity
@Table(name = "drawing_detail")
public class DrawingDetail extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7837891255273680991L;

    private Long orgId;

    private Long projectId;

    private Long drawingId;

    private Long actInsId;

    //计划提交建造时间
    private Date deliveryDate;

    //实际发图日期
    private Date acturalDrawingIssueDate;

    //版本号
    private String rev;

    //版本号
    private Integer revOrder;

    //生产接收日期
    private Date productionReceivingDate;

    //发放记录卡编号
    private String issueCardNo;

    //回收记录
    private String returnRecord;

    //设计评审变更单号
    private String designChangeReviewForm;

    //变更通知单号
    private String changeNoticeNo;

    //配布份数
    private int quantity;

    //打印份数 = 配布份数-1
    private int printing;

    //用纸量（A3）
    private float paperUse;

    //纸张数量 = 打印份数*用纸量
    private float paperAmount;

    //图纸审核编号
    private String auditNo;

    private Long issueFileId;

    private String issueFileName;

    private String issueFilePath;

    private String qrCode;

    private String taskId;

    private String assignee;

    @Transient
    private List<DrawingFile> drawingFiles;

    @Transient
    private String drawingNo;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    // DCR版本更迭影响的字段
    @Schema(description = "progressStage")
    private String progressStage;

    @Schema(description = "revNo")
    private String revNo;

    @Schema(description = "progressStage")
    private Date uploadDate;

    @Schema(description = "outgoingTransmittal")
    private String outgoingTransmittal;

    @Schema(description = "incomingTransmittal")
    private String incomingTransmittal;

    @Schema(description = "replyDate")
    private Date replyDate;

    @Schema(description = "replyStatus")
    private String replyStatus;

    private Long uploaderId;

    private String uploader;

    @Transient
    private String entitySubType;

    @Schema(description = "Drawing PDF update version")
    private String pdfUpdateVersion;

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getRevOrder() {
        return revOrder;
    }

    public void setRevOrder(Integer revOrder) {
        this.revOrder = revOrder;
    }

    public Long getIssueFileId() {
        return issueFileId;
    }

    public void setIssueFileId(Long issueFileId) {
        this.issueFileId = issueFileId;
    }

    public String getIssueFileName() {
        return issueFileName;
    }

    public void setIssueFileName(String issueFileName) {
        this.issueFileName = issueFileName;
    }

    public String getIssueFilePath() {
        return issueFilePath;
    }

    public void setIssueFilePath(String issueFilePath) {
        this.issueFilePath = issueFilePath;
    }

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

    public String getRevNo() {
        return revNo;
    }

    public void setRevNo(String revNo) {
        this.revNo = revNo;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getProgressStage() {
        return progressStage;
    }

    public void setProgressStage(String progressStage) {
        this.progressStage = progressStage;
    }

    public String getOutgoingTransmittal() {
        return outgoingTransmittal;
    }

    public void setOutgoingTransmittal(String outgoingTransmittal) {
        this.outgoingTransmittal = outgoingTransmittal;
    }

    public String getIncomingTransmittal() {
        return incomingTransmittal;
    }

    public void setIncomingTransmittal(String incomingTransmittal) {
        this.incomingTransmittal = incomingTransmittal;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public List<DrawingFile> getDrawingFiles() {
        return drawingFiles;
    }

    public void setDrawingFiles(List<DrawingFile> drawingFiles) {
        this.drawingFiles = drawingFiles;
    }

    public Long getActInsId() {
        return actInsId;
    }

    public void setActInsId(Long actInsId) {
        this.actInsId = actInsId;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getPdfUpdateVersion() {
        return pdfUpdateVersion;
    }

    public void setPdfUpdateVersion(String pdfUpdateVersion) {
        this.pdfUpdateVersion = pdfUpdateVersion;
    }
}
