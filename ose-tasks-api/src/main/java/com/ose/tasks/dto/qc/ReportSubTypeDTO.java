package com.ose.tasks.dto.qc;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class ReportSubTypeDTO extends BaseDTO {

    private static final long serialVersionUID = 3027785084926490217L;

    @Schema(description = "报告类型")
    private String reportType;

    @Schema(description = "子报告类型")
    private String subReportType;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getSubReportType() {
        return subReportType;
    }

    public void setSubReportType(String subReportType) {
        this.subReportType = subReportType;
    }
}
