package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.IssueEntity;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IssueEntityRepository extends PagingAndSortingWithCrudRepository<IssueEntity, Long> {

    List<IssueEntity> findByIssueId(Long id);

}
