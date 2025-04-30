package com.ose.tasks.domain.model.service.delegate;

import java.text.ParseException;
import java.util.Map;

import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmRuTask;

import jakarta.mail.internet.AddressException;

/**
 * 工作流任务代理接口
 */
public interface BaseBpmTaskInterfaceDelegate {

    /*--
    调用顺序，执行 主程序的
    1. Delegate preCreateActInst
    2. 主程序 preCreateActInst
    3. Delegate createActInst
    4. 主程序 createActInst
    5. 主程序 postCreateActInst
    6. Delegate postCreateActInst
     */


    default CreateResultDTO preCreateActInst(CreateResultDTO createResult) {
        return createResult;
    }


    default CreateResultDTO createActInst(CreateResultDTO createResult) {
        return createResult;
    }


    default CreateResultDTO postCreateActInst(CreateResultDTO createResult) {
        return createResult;
    }


    default void prepareExecute(ContextDTO contextDTO, Map<String, Object> data, BpmActivityInstanceBase actInst, BpmRuTask ruTask, TodoTaskDTO todoDTO) {

    }


    default ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) throws ParseException {
        return execResult;
    }


    default ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) throws AddressException {
        return execResult;
    }


    default ExecResultDTO completeExecute(ExecResultDTO execResult) {
        return execResult;
    }


    default <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPrepareExecute(ContextDTO contextDTO,
                                                                                      Map<String, Object> data,
                                                                                      P todoBatchTaskCriteriaDTO,
                                                                                      TodoBatchTaskDTO todoBatchTaskDTO) {
        return todoBatchTaskDTO;

    }



    default <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPreExecute(ContextDTO contextDTO,
                                                                                  Map<String, Object> data,
                                                                                  P todoBatchTaskCriteriaDTO,
                                                                                  TodoBatchTaskDTO todoBatchTaskDTO) {
        return todoBatchTaskDTO;

    }



    default <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPostExecute(ContextDTO contextDTO,
                                                                                   Map<String, Object> data,
                                                                                   P todoBatchTaskCriteriaDTO,
                                                                                   TodoBatchTaskDTO todoBatchTaskDTO) {
        return todoBatchTaskDTO;

    }



    default <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchCompleteExecute(ContextDTO contextDTO,
                                                                                       Map<String, Object> data,
                                                                                       P todoBatchTaskCriteriaDTO,
                                                                                       TodoBatchTaskDTO todoBatchTaskDTO) {
        return todoBatchTaskDTO;

    }


    default RevocationDTO revocationExecute(ContextDTO contextDTO,
                                            Long orgId,
                                            Long projectId,
                                            ActInstSuspendDTO actInstSuspendDTO) {
        return new RevocationDTO();

    }


    default SuspendDTO suspendExecute(ContextDTO contextDTO,
                                      Long orgId,
                                      Long projectId,
                                      ActInstSuspendDTO actInstSuspendDTO) {
        return new SuspendDTO();

    }


    default RevocationDTO batchRevocationPreExecute(ContextDTO contextDTO,
                                                    Long orgId,
                                                    Long projectId,
                                                    ActInstSuspendDTO actInstSuspendDTO) {
        return new RevocationDTO();

    }


    default RevocationDTO batchRevocationPostExecute(ContextDTO contextDTO,
                                                     Long orgId,
                                                     Long projectId,
                                                     ActInstSuspendDTO actInstSuspendDTO) {
        return new RevocationDTO();

    }


}
