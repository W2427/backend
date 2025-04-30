package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.SuggestionSearchDTO;
import com.ose.tasks.entity.Suggestion;
import org.springframework.data.domain.Page;

public interface SuggestionRepositoryCustom {

    Page<Suggestion> getList(SuggestionSearchDTO dto, Long operatorId);

//    Page<QCReport> externalInspectionViews(Long orgId, Long projectId, PageDTO pageDTO, ExInspViewCriteriaDTO criteriaDTO);
//
//    Page<QCReport> externalInspectionReports(Long orgId, Long projectId, PageDTO pageDTO, ExInspReportCriteriaDTO criteriaDTO);
}
