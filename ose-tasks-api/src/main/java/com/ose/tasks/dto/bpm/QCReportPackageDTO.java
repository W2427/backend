package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class QCReportPackageDTO extends BaseDTO {

    private static final long serialVersionUID = -6703341880392268947L;

    @Schema(description = "报告")
    private String parentEntityId;

    @Schema(description = "报告")
    private String reportNo;

    @Schema(description = "报告Id")
    private Long qcReportId;

    @Schema(description = "工序")
    private String process;

    public String getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(String parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Long getQcReportId() {
        return qcReportId;
    }

    public void setQcReportId(Long qcReportId) {
        this.qcReportId = qcReportId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
