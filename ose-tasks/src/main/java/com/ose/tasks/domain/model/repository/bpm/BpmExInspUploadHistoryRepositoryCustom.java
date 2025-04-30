package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.entity.report.QCReport;
import org.springframework.data.domain.Page;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.ExInspReportCriteriaDTO;
import com.ose.tasks.dto.bpm.ExInspUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.ExInspViewCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmExInspUploadHistory;

public interface BpmExInspUploadHistoryRepositoryCustom {

    Page<BpmExInspUploadHistory> externalInspectionUploadHistories(Long orgId, Long projectId,
                                                                   ExInspUploadHistorySearchDTO pageDTO, Long operatorId);

    Page<QCReport> externalInspectionViews(Long orgId, Long projectId, PageDTO pageDTO, ExInspViewCriteriaDTO criteriaDTO);

    Page<QCReport> externalInspectionReports(Long orgId, Long projectId, PageDTO pageDTO, ExInspReportCriteriaDTO criteriaDTO);
}
