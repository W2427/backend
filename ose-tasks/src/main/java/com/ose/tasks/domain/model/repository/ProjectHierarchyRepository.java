package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.ProjectHierarchy;

import java.util.List;

/**
 * 项目配置层级 CRUD 操作接口。
 */
public interface ProjectHierarchyRepository extends PagingAndSortingWithCrudRepository<ProjectHierarchy, String> {

    List<ProjectHierarchy> findByProjectIdOrderByHierarchyLevel(Long projectId);

}
