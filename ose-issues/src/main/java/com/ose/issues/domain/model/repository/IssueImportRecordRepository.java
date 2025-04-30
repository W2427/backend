package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.IssueImportRecord;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IssueImportRecordRepository extends PagingAndSortingWithCrudRepository<IssueImportRecord, Long> {
    Page<IssueImportRecord> findByProjectId(Long projectId, Pageable pageable);
}
