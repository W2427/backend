package com.ose.tasks.domain.model.repository;

import com.ose.tasks.entity.WorkCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface WorkStepFollowUpRepository extends PagingAndSortingRepository<WorkCode, Long> {

    Page<WorkCode> findAllByOrgIdAndProjectIdOrderByWorkCode(Long orgId, Long projectId, Pageable pageable);

    Optional<WorkCode> findAllByIdAndOrgIdAndProjectId(Long id, Long orgId, Long projectId);
}
