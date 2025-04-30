package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * qc报告 确认 传输对象
 */
public class BpmExInspConfirmDTO extends BaseDTO {


    private static final long serialVersionUID = -3489511939555939078L;

    @Schema(description = "确认的id QC报告ID")
    private Long qcReportId;

    @Schema(description = "确认的id QC报告二维码")
    private String qcReportQrCode;

    @Schema(description = "报告确认的ID exInspConfirmId")
    private Long exInspConfirmId;


    @Schema(description = "报告上传历史ID exInspUploadHistory")
    private Long exInspUploadHistoryId;

    @Schema(description = "状态：REJECTED，APPROVED")
    private EntityStatus status;

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public Long getQcReportId() {
        return qcReportId;
    }

    public void setQcReportId(Long qcReportId) {
        this.qcReportId = qcReportId;
    }

    public String getQcReportQrCode() {
        return qcReportQrCode;
    }

    public void setQcReportQrCode(String qcReportQrCode) {
        this.qcReportQrCode = qcReportQrCode;
    }

    public Long getExInspConfirmId() {
        return exInspConfirmId;
    }

    public void setExInspConfirmId(Long exInspConfirmId) {
        this.exInspConfirmId = exInspConfirmId;
    }

    public Long getExInspUploadHistoryId() {
        return exInspUploadHistoryId;
    }

    public void setExInspUploadHistoryId(Long exInspUploadHistoryId) {
        this.exInspUploadHistoryId = exInspUploadHistoryId;
    }
}
