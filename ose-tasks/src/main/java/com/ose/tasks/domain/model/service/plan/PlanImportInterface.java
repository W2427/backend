package com.ose.tasks.domain.model.service.plan;

import com.ose.dto.OperatorDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.dto.process.EntityProcessRelationsDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.Project;

import java.util.List;

/**
 * 计划管理服务接口。
 */
public interface PlanImportInterface extends EntityInterface {

    /**
     * 导入计划。
     *
     * @param batchTask                 批处理任务信息
     * @param operator                  操作者信息
     * @param project                   项目信息
     * @param rootNodeId                根节点 ID
     * @param nodeImportDTO             节点导入操作数据传输对象
     * @param entityProcessRelationsDTO 工序-实体类型映射表
     * @return 批处理执行结果
     */
    BatchResultDTO importPlan(
        BatchTask batchTask,
        OperatorDTO operator,
        Project project,
        Long rootNodeId,
        HierarchyNodeImportDTO nodeImportDTO,
        EntityProcessRelationsDTO entityProcessRelationsDTO
    );

    /**
     * 重新生成四级计划前后置关系。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    void regenerateWBSEntryRelations(
        OperatorDTO operator,
        Long orgId,
        Long projectId
    );

    /**
     * 自动绑定实体资源。
     *
     * @param batchTask    批处理任务信息
     * @param operator     操作者信息
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param wbsEntryID   WBS 条目 ID
     * @param moduleUpdate 更新此模块，boolean
     * @return 批处理任务执行结果
     */
    BatchResultDTO generateEntityProcessWBSEntries(
        final BatchTask batchTask,
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long wbsEntryID,
        final boolean moduleUpdate
    );


    /**
     * 更新由于实体修改导致的计划变动。
     *
     * @param batchTask 批处理任务信息
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 批处理任务执行结果
     */
    BatchResultDTO generateWBSEntryFromEntity(
        final BatchTask batchTask,
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId);


    /**
     * 检查实体对应的四级计划状态，确定实体是否能够合并或拆分
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entityId
     * @return
     */
    String checkChange(
        Long orgId,
        Long projectId,
        Long entityId
    );


    /**
     * 根据提供的实体ID删除对应的四级计划。如果成功返回true。如果不能删除返回false
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entityId
     * @return
     */
    boolean deleteWBSEntries(
        Long orgId,
        Long projectId,
        Long entityId
    );

    /**
     * 对新增加的 实体生成四级计划 及相互关系
     * @param batchTask
     * @param operator
     * @param orgId
     * @param projectId
     * @param wbsEntryID
     * @return
     */
    BatchResultDTO generateWbsEntryFromAddedWbsEntity(final BatchTask batchTask,
                                                             final OperatorDTO operator,
                                                             final Long orgId,
                                                             final Long projectId,
                                                             final Long wbsEntryID);

    BatchResultDTO generateWbsEntryFromTaskPackageWbsEntity(final BatchTask batchTask,
                                                      final OperatorDTO operator,
                                                      final Long orgId,
                                                      final Long projectId,
                                                      final Long taskPackageId);


    BatchResultDTO deleteGenerateWbsEntryFromAddedWbsEntity(final BatchTask batchTask,
                                                            final OperatorDTO operator,
                                                            final Long orgId,
                                                            final Long projectId,
                                                            final Long originalBatchTaskId);

    BatchResultDTO generateWbsEntryFromSelectedEntities(final BatchTask batchTask,
                                                               final OperatorDTO operator,
                                                               final Long orgId,
                                                               final Long projectId,
                                                               final List<Long> entityIds);
}
