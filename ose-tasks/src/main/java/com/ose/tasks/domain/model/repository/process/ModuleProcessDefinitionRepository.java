package com.ose.tasks.domain.model.repository.process;

import com.ose.tasks.entity.process.ModuleProcessDefinition;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 模块工作流定义数据仓库。
 */
@Transactional
public interface ModuleProcessDefinitionRepository extends CrudRepository<ModuleProcessDefinition, Long> {

    /**
     * 取得模块工作流定义。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 模块工作流定义列表
     */
    List<ModuleProcessDefinition> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId);


    /**
     * 取得指定项目的指定类型模块的工作流部署信息。
     *
     * @param projectId  项目 ID
     * @param funcPart   功能区
     * @return 工作流定义部署信息
     */
    Optional<ModuleProcessDefinition> findByProjectIdAndFuncPartAndDeletedIsFalse(
        Long projectId,
        String funcPart);



    @Transactional
    @Procedure(name = "delete_bpm_instance")
    void deleteBpmInstance(@Param("act_inst_id") String processInstanceId);

    /**
     * 取得模块工作流定义。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 模块工作流定义列表
     */
    ModuleProcessDefinition findFirstByOrgIdAndProjectIdAndFuncPartAndDeletedIsFalseOrderByVersionDesc(
        Long orgId,
        Long projectId,
        String moduleType);

    ModuleProcessDefinition findByOrgIdAndProjectIdAndFuncPartAndDeletedIsFalse(Long orgId, Long projectId, String funcPart);
}
