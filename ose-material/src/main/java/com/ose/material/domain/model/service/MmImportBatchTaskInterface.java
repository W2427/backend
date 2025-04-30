package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.material.dto.MmImportBatchResultDTO;
import com.ose.material.entity.MmImportBatchTask;
import com.ose.vo.DisciplineCode;
import com.ose.material.vo.MaterialImportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * 批处理服务接口。
 */
public interface MmImportBatchTaskInterface {

    /**
     * 批处理任务执行接口。
     */
    interface BatchTaskFunction {
        MmImportBatchResultDTO exec(MmImportBatchTask batchTask);
    }

    /**
     * 检查是否存在冲突的任务。
     *
     * @param projectId      项目 ID
     * @param batchTaskCodes 批处理任务代码列表
     * @return 批处理服务接口实例
     */
    MmImportBatchTaskInterface conflictWith(Long projectId, MaterialImportType... batchTaskCodes);

    /**
     * 开始批处理任务。
     *
     * @param context       上下文对象
     * @param batchTaskCode 批处理任务代码
     * @param function      执行逻辑
     */
    void run(
        final Long orgId,
        final Long projectId,
        final Long entityId,
        final String entityNo,
        final ContextDTO context,
        final MaterialImportType batchTaskCode,
        final DisciplineCode discipline,
        final BatchTaskFunction function
    );

    /**
     * 查询批处理任务。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 批处理任务分页参数
     */
    Page<MmImportBatchTask> search(
        Long orgId,
        Long projectId,
        Pageable pageable
    );

    /**
     * 取得批处理任务详细信息。
     *
     * @param orgId  组织 ID
     * @param taskId 批处理任务 ID
     * @return 批处理任务数据实体
     */
    MmImportBatchTask get(
        Long orgId,
        Long projectId,
        Long taskId
    );

    /**
     * 手动停止批处理任务。
     *
     * @param orgId    组织 ID
     * @param operator 操作者信息
     * @param taskId   批处理任务 ID
     */
    void stop(Long orgId,
              Long projectId,
              Long taskId,
              OperatorDTO operator);


}
