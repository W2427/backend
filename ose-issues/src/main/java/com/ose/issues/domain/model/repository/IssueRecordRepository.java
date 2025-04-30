package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.IssueRecord;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IssueRecordRepository extends
    PagingAndSortingWithCrudRepository<IssueRecord, Long>,
    IssueRecordCustomRepository {
}
