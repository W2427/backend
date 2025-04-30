package com.ose.tasks.dto.bpm;

import com.ose.tasks.entity.report.ReportConfig;

public class ReportConfigResponseDTO extends ReportConfig {

    private static final long serialVersionUID = 1748435049448746575L;

    public ReportConfigResponseDTO(ReportConfig reportConfig) {
        if (reportConfig != null) {
            this.setCreatedAt(reportConfig.getCreatedAt());
            this.setId(reportConfig.getId());
            this.setLastModifiedAt(reportConfig.getLastModifiedAt());
            this.setOrgId(reportConfig.getOrgId());
            this.setProjectId(reportConfig.getProjectId());
            this.setStatus(reportConfig.getStatus());
            this.setReportName(reportConfig.getReportName());
            this.setReportCode(reportConfig.getReportCode());
            this.setCover(reportConfig.getCover());
            this.setProcessId(reportConfig.getProcessId());
            this.setReportGenerateClass(reportConfig.getReportGenerateClass());
            this.setReportSubType(reportConfig.getReportSubType());
            this.setReportType(reportConfig.getReportType());
        }
    }
}
