package com.ose.tasks.domain.model.repository.workinghour;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * CRUD 操作接口。
 */
@Transactional
public interface ProjectWorkingHourHistoryRepository extends PagingAndSortingWithCrudRepository<ProjectWorkingHourHistoryEntity, Long> {

    Page<ProjectWorkingHourHistoryEntity> findByOrgIdAndProjectIdAndProjectWorkingHourId(Long orgId, Long projectId, Long projectWorkingHourId, Pageable pageable);

    List<ProjectWorkingHourHistoryEntity> findByOrgIdAndProjectIdAndProjectWorkingHourId(Long orgId, Long projectId, Long projectWorkingHourId);

    Optional<ProjectWorkingHourHistoryEntity> findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long id);

}
