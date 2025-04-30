package com.ose.tasks.domain.model.repository.process;

import com.ose.tasks.entity.process.ModuleProcessDefinitionBasic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 模块工作流定义数据仓库。
 */
@Transactional
public interface ModuleProcessDefinitionBasicRepository extends CrudRepository<ModuleProcessDefinitionBasic, Long> {

    /**
     * 取得指定项目的指定类型模块的工作流部署信息。
     *
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param definitionId 工作流定义 ID
     * @return 工作流定义部署信息
     */
    Optional<ModuleProcessDefinitionBasic> findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long definitionId);

    /**
     * 取得项目的模块工作流部署信息列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 项目的模块工作流部署信息列表
     */
    List<ModuleProcessDefinitionBasic> findByOrgIdAndProjectIdAndDeletedIsFalseOrderByIdDesc(Long orgId, Long projectId);

    /**
     * 取得模块的工作流部署信息历史记录列表。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param moduleType 模块类型
     * @return 模块的工作流部署信息历史记录列表
     */
    List<ModuleProcessDefinitionBasic> findByOrgIdAndProjectIdAndFuncPartAndDeletedIsTrueOrderByIdDesc(Long orgId, Long projectId, String moduleType);

}
