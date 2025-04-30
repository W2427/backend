package com.ose.tasks.domain.model.repository.workinghour;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourEntity;
import com.ose.tasks.vo.workinghour.ProjectWorkingHourStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * CRUD 操作接口。
 */
@Transactional
public interface ProjectWorkingHourRepository extends PagingAndSortingWithCrudRepository<ProjectWorkingHourEntity, Long> {

    List<ProjectWorkingHourEntity> findByOrgIdAndProjectIdAndCreatedByAndProjectWorkingHourDateBetweenOrderByProjectWorkingHourDateAscCreatedAtAsc(
        Long orgId, Long projectId, Long createdBy, Date startDate, Date endDate
    );

    Optional<ProjectWorkingHourEntity> findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long id);

    Page<ProjectWorkingHourEntity> findByOrgIdAndProjectIdAndApprovalIdAndWorkingHourStatusOrderByProjectWorkingHourDateAscCreatedAtAsc(
        Long orgId, Long projectId, Long approvalId, ProjectWorkingHourStatusType workingHourStatus, Pageable pageable
    );
}
