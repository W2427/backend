package com.ose.tasks.domain.model.service.bpm.taskexec;

import com.ose.dto.ContextDTO;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.*;

import java.util.Map;

/**
 * 工作流执行 接口。调用关系如下：
 * 通过工作流的 actInstance（流程实例）的大类型(act_category)和工序名称（process_name)，实体类型和子类型，任务节点名称（task_def_key_）以及人员权限 category_ 来确定 任务类型 Task_type
 * 定义任务类型 枚举 TASK_TYPE。
 * 定义 流程节点 类型对照表，包括以上字段，再增加顺序字段 order
 * 实现 AbstractTaskExecService 抽象类，这个类里边定义一个 抽象类，并实现泛型的构造方法
 * 对照每一个 TASK_TYPE 定义一个 Task_Type_Exec的类，并实现 TaskExecInterface接口。继承AbstractTaskExecService类
 * 要首先调用 preExec初始化变量
 */
public interface TaskExecInterface {


    /**
     * 处理工作流 执行 的操作
     *
     * @param bpmProcTaskDTO 流程信息
     * @return 工作流背景信息类
     */
    ExecResultDTO exec(BpmProcTaskDTO bpmProcTaskDTO);

    /**
     * 创建流程前的数据准备
     *
     * @return
     */
    CreateResultDTO preCreateActInst(CreateResultDTO createResult);

    /**
     * 创建流程
     *
     * @return
     */
    CreateResultDTO createActInst(CreateResultDTO createResult);

    /**
     * 创建流程后的数据处理
     *
     * @return
     */
    CreateResultDTO postCreateActInst(CreateResultDTO createResult);


    /**
     * 批处理任务执行
     *
     * @param contextDTO               执行上下文
     * @param data                     辅助信息集合
     * @param todoBatchTaskCriteriaDTO 输出DTO
     * @return 执行结果
     */

    <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchExec(ContextDTO contextDTO,
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

    Boolean revocationAny(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO);
}

