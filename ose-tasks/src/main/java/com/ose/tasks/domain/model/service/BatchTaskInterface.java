package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import com.ose.tasks.dto.BatchTaskThreadPoolDTO;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingHistory;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.vo.setting.BatchTaskCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

/**
 * 批处理服务接口。
 */
public interface BatchTaskInterface {

    /**
     * 批处理任务执行接口。
     */
    interface BatchTaskFunction {
        BatchResultDTO exec(BatchTask batchTask);
    }

    /**
     * 图纸批处理任务执行接口。
     */
    interface BatchTaskDrawingFunction {
        BatchResultDTO exec(BatchDrawingTask batchDrawingTask);
    }

    /**
     * 图纸批处理任务执行接口。
     */
    interface BatchConstructFunction {
        BatchResultDTO exec(BatchConstructTask batchConstructTask);
    }

    /**
     * 检查是否存在冲突的任务。
     *
     * @param projectId      项目 ID
     * @param batchTaskCodes 批处理任务代码列表
     * @return 批处理服务接口实例
     */
    BatchTaskInterface conflictWith(Long projectId, BatchTaskCode... batchTaskCodes);

    /**
     * 开始批处理任务。
     *
     * @param context       上下文对象
     * @param project       项目信息
     * @param batchTaskCode 批处理任务代码
     * @param function      执行逻辑
     */
    void run(
        final ContextDTO context,
        final Project project,
        final BatchTaskCode batchTaskCode,
        final BatchTaskFunction function
    );

    /**
     * 查询批处理任务。
     *
     * @param companyId            公司 ID
     * @param orgId                组织 ID
     * @param projectId            项目 ID
     * @param batchTaskCriteriaDTO 查询条件
     * @param pageable             分页参数
     * @return 批处理任务分页参数
     */
    Page<BatchTaskBasic> search(
        Long companyId,
        Long orgId,
        Long projectId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        Pageable pageable
    );

    /**
     * 取得批处理任务详细信息。
     *
     * @param orgId  组织 ID
     * @param taskId 批处理任务 ID
     * @return 批处理任务数据实体
     */
    BatchTask get(Long orgId, Long taskId);

    /**
     * 手动停止批处理任务。
     *
     * @param orgId    组织 ID
     * @param operator 操作者信息
     * @param taskId   批处理任务 ID
     */
    void stop(OperatorDTO operator, Long orgId, Long taskId);

    /**
     * 开始图纸批处理任务。
     *
     * @param context       上下文对象
     * @param project       项目信息
     * @param batchTaskCode 批处理任务代码
     * @param function      执行逻辑
     */
    void runDrawingPackage(
        final ContextDTO context,
        final Project project,
        final BatchTaskCode batchTaskCode,
        final BpmActivityInstanceBase actInst,
        final BpmRuTask ruTask,
        final List<BpmRuTask> nextRuTasks,
        final Drawing drawing,
        final DrawingDetail drawingDetail,
        final DrawingHistory drawingHistory,
        final List<SubDrawing> subDrawing,
        final BpmEntityDocsMaterials docsMaterials,
        final BatchTaskDrawingFunction function
    );

    /**
     * PDF拆分线程池任务。
     *
     * @param context
     * @param project
     * @param batchTaskCode
     * @param function
     */
     void runPdfPackage(
        final ContextDTO context,
        final Project project,
        final BatchTaskCode batchTaskCode,
        final BatchTaskDrawingFunction function
    );

    /**
     * 建造流程线程池任务。
     *
     * @param project
     * @param batchTaskCode
     * @param isQueue
     * @param context
     * @param function
     */
    void runConstructTaskExecutor(
        final BpmActivityInstanceBase bpmActivityInstanceBase,
        final Project project,
        final BatchTaskCode batchTaskCode,
        final Boolean isQueue,
        final ContextDTO context,
        final BatchConstructFunction function
    );

    /**
     * 获取线程池中的线程数。
     */
    List<BatchTaskThreadPoolDTO> threadPoolTaskSchedulerCount(
    );

    File exportEntities(Long orgId, Long projectId, Long operatorId);
}
