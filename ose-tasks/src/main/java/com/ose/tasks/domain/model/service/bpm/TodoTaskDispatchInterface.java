package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.bpm.*;

import java.util.Map;

/**
 * 工作流执行 控制分发接口。调用关系如下：
 * TodoTaskController --> TodoTaskDispatch ----
 * |- getBpm Info 取得工作流程实例的信息
 * |- preExec 处理流程执行前的信息
 * |- exec 执行流程
 * |- postExec 执行流程之后的信息
 * 基础支持类
 * |- TodoTask
 * |- Ndt
 * |- Pmi
 * |- Drawing
 * |- .....
 * |- TodoTaskBase
 * <p>
 * 通过 execResultDTO 中的 nextTask flag 来确定是否 继续启动 下一个流程。
 */
public interface TodoTaskDispatchInterface {

    /**
     * 取得工作流执行的背景信息
     *
     * @param taskId 任务ID
     * @return 工作流背景信息类
     */
    BpmProcTaskDTO getBpmInfo(Long taskId);


    /**
     * 取得Spring 容器中注册的任务处理的bean
     * @param bpmProcTaskDTO       环境信息
     * @return 工作流背景信息类
     */
//    BpmTaskType setServiceClazz(BpmProcTaskDTO bpmProcTaskDTO);

    /**
     * 处理工作流执行前的操作
     * @param context       环境信息
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param actTaskId     任务ID
     * @param toDoTaskDTO   任务执行DTO
     * @param operator      操作者信息
     * @return 工作流背景信息类
     */
/*    Map<String, Object> preExec(ContextDTO context,
                                   Long orgId,
                                   Long projectId,
                                   String actTaskId,
                                   TodoTaskExecuteDTO toDoTaskDTO,
                                   OperatorDTO operator);
*/


    /**
     * 设置创建流程的类
     * @param createResult
     * @return
     */
//    boolean setCreateClazz(CreateResultDTO createResult);


    /**
     * 处理工作流 创建 的操作
     *
     * @param context    环境信息
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param actInstDTO 任务执行DTO
     * @param operator   操作者信息
     * @return 工作流背景信息类
     */
    CreateResultDTO create(ContextDTO context,
                           Long orgId,
                           Long projectId,
                           OperatorDTO operator,
                           ActivityInstanceDTO actInstDTO);


    /**
     * 处理工作流 执行 的操作
     *
     * @param context     环境信息
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param toDoTaskDTO 任务执行DTO
     * @param operator    操作者信息
     * @return 工作流背景信息类
     */
    ExecResultDTO exec(ContextDTO context,
                       Long orgId,
                       Long projectId,
                       TodoTaskExecuteDTO toDoTaskDTO,
                       OperatorDTO operator);


    /**
     * 处理工作流 执行 的操作
     *
     * @param context     环境信息
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param actTaskId   任务ID
     * @param toDoTaskDTO 任务执行DTO
     * @param operator    操作者信息
     * @param taskType    任务类型
     * @return 工作流背景信息类
     */
    ExecResultDTO exec(ContextDTO context,
                       Long orgId,
                       Long projectId,
                       Long taskId,
                       TodoTaskExecuteDTO toDoTaskDTO,
                       OperatorDTO operator,
                       String taskType);

    /**
     * 处理工作流执行后的操作
     * @param context       环境信息
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param actTaskId     任务ID
     * @param toDoTaskDTO   任务执行DTO
     * @param operator      操作者信息
     * @return 工作流背景信息类
     */
/*    Map<String, Object> postExec(ContextDTO context,
                                Long orgId,
                                Long projectId,
                                String actTaskId,
                                TodoTaskExecuteDTO toDoTaskDTO,
                                OperatorDTO operator);
*/

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
     * 处理工作流 执行 的操作
     *
     * @param context     环境信息
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param actTaskId   任务ID
     * @param todoTaskDTO 任务执行DTO
     * @param operator    操作者信息
     * @return 工作流背景信息类
     */
    ExecResultDTO exec(ContextDTO context,
                       Long orgId,
                       Long projectId,
                       Long taskId,
                       TodoTaskExecuteDTO todoTaskDTO,
                       OperatorDTO operator);

    /**
     * 设置 任务处理的 界面 预加载的数据
     *
     * @param orgId
     * @param projectId
     * @param actTaskIds
     * @return
     */
    TodoTaskDTO setTaskUIHandleData(ContextDTO contextDTO, Long orgId, Long projectId, String[] actTaskIds);

    /**
     * 返回 批处理任务 处理画面需要的数据
     *
     * @param orgId
     * @param projectId
     * @param todoBatchTaskCriteriaDTO
     * @return
     */
    <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO setBatchTaskUIHandleData(ContextDTO contextDTO,
                                                                                   Long orgId,
                                                                                   Long projectId,
                                                                                   P todoBatchTaskCriteriaDTO);


    /**
     * 工人认领任务
     *
     * @param context
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param toDoTaskDTO
     */
    void workerClaimTask(
        ContextDTO context,
        Long orgId,
        Long projectId,
        TodoTaskExecuteDTO toDoTaskDTO
    );

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

    Boolean revocationAny(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO);
}

