package com.ose.tasks.domain.model.service.bpm.taskexec;

import com.ose.dto.ContextDTO;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.ProcessBpmnRelation;

import java.util.Map;
import java.util.Set;

/**
 * 执行原来BPM 服务中的功能
 */
public interface BpmExecInterface {


    /**
     * 处理工作流 执行 的操作
     *
     * @param bpmProcTaskDTO 流程信息
     * @return 工作流背景信息类
     */
    ExecResultDTO complete(BpmProcTaskDTO bpmProcTaskDTO);

    /**
     * 创建流程前的数据准备
     *
     * @return
     */
    CreateResultDTO create(CreateResultDTO createResult);


    /**
     * 批处理任务执行
     *
     * @param contextDTO               执行上下文
     * @param data                     辅助信息集合
     * @param todoBatchTaskCriteriaDTO 输出DTO
     * @return 执行结果
     */

    <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchComplete(ContextDTO contextDTO,
                                                                    Map<String, Object> data,
                                                                    P todoBatchTaskCriteriaDTO);


    /**
     * 任务节点撤回操作
     *
     * @param orgId
     * @param projectId
     * @param actInstSuspendDTO
     * @return
     */
    RevocationDTO revocation(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO);

    /**
     * 任务节点批量撤回操作
     *
     * @param orgId
     * @param projectId
     * @param actInstSuspendDTO
     * @return
     */
    RevocationDTO batchRevocation(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO);


    JsonResponseBody suspendTask(ContextDTO context, Long orgId, Long projectId, Long taskId, ActInstSuspendDTO dto);

    /**
     * 演算。
     *
     * @param processBpmnRelation  需要演算的 流程节点关系DTO
     * @param variables         实体对应的流程变量 WELD_JOINT -> weldEntityMap
     * @return 是否可以启动目标任务节点
     */
    Set<NextTaskDTO> evaluate(
        final ProcessBpmnRelation processBpmnRelation,
        final Map<String, Object> variables
    );
}

