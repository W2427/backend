package com.ose.tasks.dto.qc;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.wbs.EntityTestResult;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import org.hibernate.annotations.Type;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Date;

public class ProcessTestResultDTO extends BaseDTO {

    private static final long serialVersionUID = 3370392711083959312L;

    @Schema(description = "是否通过测试")
    @Enumerated(EnumType.STRING)
    private EntityTestResult testResult;

    @Schema(description = "测试结果备注")
    @Column(columnDefinition = "text")
    private String comment;

    @Schema(description = "报告文件 ID, QR Code")
    private String reportQrCode;

    @Schema(description = "焊工 ID")
    private Long welderId;

    @Schema(description = "检验员 ID")
    private Long submittedBy;

    @Schema(description = "检验时间")
    private Date submittedAt;

    @Schema(description = "工作流实例ID")
    private Long actInstId;

    public EntityTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(EntityTestResult testResult) {
        this.testResult = testResult;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReportQrCode() {
        return reportQrCode;
    }

    public void setReportQrCode(String reportQrCode) {
        this.reportQrCode = reportQrCode;
    }

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public Long getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(Long submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

}
