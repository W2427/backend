package com.ose.tasks.domain.model.service.plan;

import com.ose.dto.OperatorDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.entity.BpmnTaskRelation;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.wbs.entry.WBSEntry;

import java.util.List;

/**
 * 结构计划前置任务管理服务接口。
 */
public interface PlanRelationInterface extends EntityInterface {

    /**
     * 生成四级计划前置任务关系。
     *
     * @param operator                操作者信息
     * @param moduleProcessDefinition 模块工作流定义
     * @param successor               实体工序计划条目（四级计划）
     */
    void generateEntityProcessPredecessorRelations(
        final OperatorDTO operator,
        final ModuleProcessDefinition moduleProcessDefinition,
        final WBSEntry successor
    );

    /**
     * 取得 REDIS 中的 entitySubType 对应的 规则
     *@param orgId
     * @param projectId
     * @param entitySubType
     * @return
     */
    EntitySubTypeRule getEntityRuleFromRedis(Long orgId,
                                                     Long projectId,
                                                     String entitySubType);

    /**
     * 取得 REDIS 中的 bpmnTaskRelation
     *@param orgId
     * @param projectId
     * @param moduleProcessDefinitionId
     * @param category
     * @return
     */
    BpmnTaskRelation getBpmnTaskRelationFromRedis(Long orgId,
                                                  Long projectId,
                                                  Long moduleProcessDefinitionId,
                                                  String category);

    /**
     * bpmn文件部署后 刷新内存redis中的bpmnTaskRelation
     * @param bpmnTaskRelations 部署的bpmn
     */
    void redeployBpmnInRedis(Long orgId,
                                    Long projectId,
                                    Long moduleProcessDefinitionId,
                                    List<BpmnTaskRelation> bpmnTaskRelations);

}
