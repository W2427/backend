package com.ose.tasks.domain.model.repository;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.DocumentUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.ExInspReportCriteriaDTO;
import com.ose.tasks.dto.bpm.ExInspViewCriteriaDTO;
import com.ose.tasks.entity.DocumentHistory;
import com.ose.tasks.entity.report.QCReport;
import org.springframework.data.domain.Page;

public interface DocumentHistoryRepositoryCustom {

    Page<DocumentHistory> uploadHistories(DocumentUploadHistorySearchDTO pageDTO, Long operatorId);

    Page<QCReport> externalInspectionViews(Long orgId, Long projectId, PageDTO pageDTO, ExInspViewCriteriaDTO criteriaDTO);

    Page<QCReport> externalInspectionReports(Long orgId, Long projectId, PageDTO pageDTO, ExInspReportCriteriaDTO criteriaDTO);
}
