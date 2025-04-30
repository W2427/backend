package com.ose.tasks.domain.model.service.wbs;

import com.ose.service.EntityInterface;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entry.WBSEntryExecutionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;


/**
 * 实体管理服务接口。
 */
public interface WBSEntityInterface extends EntityInterface {

    /**
     * 导入实体。
     *
     * @param batchTask     批处理任务信息
     * @param operator      操作者信息
     * @param project       项目信息
     * @param nodeImportDTO 节点导入操作数据传输对象
     * @return 批处理执行结果
     */
    BatchResultDTO importEntities(
        BatchTask batchTask,
        OperatorDTO operator,
        Project project,
        HierarchyNodeImportDTO nodeImportDTO
    );

    /**
     * 取得未挂载到层级结构的实体。
     *
     * @param projectId  项目 ID
     * @param entityType 实体类型
     * @param pageable   分页参数
     * @return 实体节点分页数据
     */
    Page<ProjectNode> unmountedEntities(
        Long projectId,
        String entityType,
        Pageable pageable
    );

    /**
     * 根据实体ID判断是否已经启动工作流
     *
     * @param entityId  实体ID
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return 实体存在已启动的工作流，返回true，不存在返回false
     */
    boolean isInWorkFlow(Long entityId,
                         Long projectId,
                         Long orgId);

    /**
     * 将增加的实体插入到 待更新计划表中 wbs_entry_execution_history
     *
     * @param operatorId
     * @param projectId  项目ID
     * @param entityId
     * @return
     */
    WBSEntryExecutionHistory insertExecutionHistory(
        Long operatorId,
        Long projectId,
        Long entityId
    );

    /**
     * 设置管件实体类型。
     *
     * @param shortCode 管件shortCode
     * @param rules     管件实体类型设置规则
     * @return EntitySubTypeRule 实体类型设置规则
     */
    EntitySubTypeRule getComponentEntityTypeRuleBySetting(
        String shortCode,
        Set<EntitySubTypeRule> rules);
}
