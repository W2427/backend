package com.ose.tasks.domain.model.repository.workinghour;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourCategoryEntity;
import com.ose.tasks.vo.workinghour.ProjectWorkingHourCategoryType;
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
public interface ProjectWorkingHourCategoryRepository extends PagingAndSortingWithCrudRepository<ProjectWorkingHourCategoryEntity, Long> {

    Optional<ProjectWorkingHourCategoryEntity> findByOrgIdAndProjectIdAndProjectWorkingHourCategoryTypeAndName(
        Long orgId,
        Long projectId,
        ProjectWorkingHourCategoryType projectWorkingHourCategoryType,
        String name);

    Optional<ProjectWorkingHourCategoryEntity> findByOrgIdAndProjectIdAndProjectWorkingHourCategoryTypeAndParentIdAndName(
        Long orgId,
        Long projectId,
        ProjectWorkingHourCategoryType projectWorkingHourCategoryType,
        Long parentId,
        String name);

    Page<ProjectWorkingHourCategoryEntity> findByOrgIdAndProjectIdAndProjectWorkingHourCategoryType(
        Long orgId,
        Long projectId,
        ProjectWorkingHourCategoryType projectWorkingHourCategoryType,
        Pageable pageable);

    List<ProjectWorkingHourCategoryEntity> findByOrgIdAndProjectIdAndProjectWorkingHourCategoryType(
        Long orgId,
        Long projectId,
        ProjectWorkingHourCategoryType projectWorkingHourCategoryType);

    Page<ProjectWorkingHourCategoryEntity> findByOrgIdAndProjectIdAndProjectWorkingHourCategoryTypeAndParentId(
        Long orgId,
        Long projectId,
        ProjectWorkingHourCategoryType projectWorkingHourCategoryType,
        Long parentId,
        Pageable pageable);

    List<ProjectWorkingHourCategoryEntity> findByOrgIdAndProjectIdAndProjectWorkingHourCategoryTypeAndParentId(
        Long orgId,
        Long projectId,
        ProjectWorkingHourCategoryType projectWorkingHourCategoryType,
        Long parentId);

    Optional<ProjectWorkingHourCategoryEntity> findByOrgIdAndProjectIdAndId(
        Long orgId,
        Long projectId,
        Long id);

}
