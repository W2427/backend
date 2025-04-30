package com.ose.tasks.domain.model.repository.bpm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ose.tasks.dto.bpm.QCReportCriteriaDTO;
import com.ose.tasks.entity.report.QCReport;

/**
 * QC  报告 查询接口。
 */
public interface QCReportRepositoryCustom {

    Page<QCReport> getList(Long orgId, Long projectId, Pageable pageable, QCReportCriteriaDTO criteriaDTO);

}
