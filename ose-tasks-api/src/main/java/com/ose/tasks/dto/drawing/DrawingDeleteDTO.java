package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DrawingDeleteDTO extends BaseDTO {

    private static final long serialVersionUID = 719857250335239833L;
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

    @Schema(description = "dcrNo")
    private String dcrNo;

    @Schema(description = "dcrOutgoingDate")
    private Date dcrOutgoingDate;

    @Schema(description = "dcrReplyDate")
    private Date dcrReplyDate;

    @Schema(description = "dcrRequest")
    private String dcrRequest;

    @Schema(description = "dcrStatus")
    private String dcrStatus;

    @Schema(description = "taskId")
    private Long taskId;

    public String getProgressStage() {
        return progressStage;
    }

    public void setProgressStage(String progressStage) {
        this.progressStage = progressStage;
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

    public String getDcrNo() {
        return dcrNo;
    }

    public void setDcrNo(String dcrNo) {
        this.dcrNo = dcrNo;
    }

    public Date getDcrOutgoingDate() {
        return dcrOutgoingDate;
    }

    public void setDcrOutgoingDate(Date dcrOutgoingDate) {
        this.dcrOutgoingDate = dcrOutgoingDate;
    }

    public Date getDcrReplyDate() {
        return dcrReplyDate;
    }

    public void setDcrReplyDate(Date dcrReplyDate) {
        this.dcrReplyDate = dcrReplyDate;
    }

    public String getDcrRequest() {
        return dcrRequest;
    }

    public void setDcrRequest(String dcrRequest) {
        this.dcrRequest = dcrRequest;
    }

    public String getDcrStatus() {
        return dcrStatus;
    }

    public void setDcrStatus(String dcrStatus) {
        this.dcrStatus = dcrStatus;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
