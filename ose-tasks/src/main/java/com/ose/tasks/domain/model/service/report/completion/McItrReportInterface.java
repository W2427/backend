package com.ose.tasks.domain.model.service.report.completion;

import com.ose.report.entity.ReportHistory;

import java.util.List;

public interface McItrReportInterface {

    List<ReportHistory> generateReport(Long orgId,
                                              Long projectId,
                                              Long entityId,
                                              Long entitySubTypeId,
                                              Long processId);


    }
