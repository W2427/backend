package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.ProjectInfo;

/**
 * 项目 INFO CRUD 操作接口。
 */
public interface ProjectInfoRepository extends PagingAndSortingWithCrudRepository<ProjectInfo, Long> {

}
