package com.ose.tasks.domain.model.repository;

import com.ose.tasks.entity.ProjectNodeModuleType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

/**
 * 项目节点所在模块类型视图操作接口。
 */
public interface ProjectNodeModuleTypeRepository extends CrudRepository<ProjectNodeModuleType, Long> {

    /**
     * 根据项目节点 ID 取得项目节点的模块类型信息。
     * @param projectNodeId 项目节点 ID
     * @return 项目节点的模块类型信息
     */


    /**
     * 取得项目节点模块类型信息。
     *
     * @param projectId 项目 ID
     * @param moduleId        层级节点 ID
     * @param moduleEntityTypes  层级节点类型
     * @return 项目节点模块类型信息
     */
    Optional<ProjectNodeModuleType> findByProjectIdAndIdAndEntityTypeIn(Long projectId, Long moduleId, Set<String> moduleEntityTypes);

}
