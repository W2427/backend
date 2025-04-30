package com.ose.report.domain.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.report.entity.ReportHistory;

/**
 * 检查单生成历史记录 CRUD 操作接口。
 */
@Transactional
public interface ReportHistoryRepository extends PagingAndSortingWithCrudRepository<ReportHistory, String> {
}
