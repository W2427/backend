package com.ose.tasks.domain.model.service.report;

import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.vo.qc.ReportSubType;

public interface QCReportNoInterface {

    String getReportSn(Long orgId, Long projectId);

    String seriesNo(
        Long orgId,
        Long projectId,
        ReportSubType type,
        String moduleName,
        Integer n,
        String projectName,
        int retryTimes
    );

    String reportNo(String operatorName,
                                        ReportSubType type,
                                        String moduleName,
                                        String result,
                                        String projectName,
                                        ReportConfig reportConfig);
}
