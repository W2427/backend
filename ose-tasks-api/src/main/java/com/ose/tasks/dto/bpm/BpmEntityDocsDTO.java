package com.ose.tasks.dto.bpm;

import java.util.Date;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.bpm.ActInstDocType;
import io.swagger.v3.oas.annotations.media.Schema;


public class BpmEntityDocsDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;


    @Schema(description = "报告 QrCode")
    private String reportQrCode;

    @Schema(description = "报告名称")
    private String reportName;

    @Schema(description = "文件Id")
    private Long fileId;

    @Schema(description = "报告NO")
    private String reportNo;

    @Schema(description = "文件路径")
    private String filePath;

    @Enumerated(EnumType.STRING)
    private ActInstDocType type;

    @Schema(description = "更新时间")
    private Date createTime;

    @Schema(description = "更新人")
    private String operator;

    public String getReportQrCode() {
        return reportQrCode;
    }

    public void setReportQrCode(String reportQrCode) {
        this.reportQrCode = reportQrCode;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ActInstDocType getType() {
        return type;
    }

    public void setType(ActInstDocType type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


}
