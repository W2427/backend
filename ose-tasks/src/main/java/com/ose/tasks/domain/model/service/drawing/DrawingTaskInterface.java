package com.ose.tasks.domain.model.service.drawing;

import java.util.List;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.BpmActivityInstanceDTO;
import com.ose.tasks.dto.bpm.DrawingAppointDTO;
import com.ose.tasks.entity.BatchDrawingTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import org.springframework.data.domain.Page;

import com.ose.tasks.dto.bpm.ActInstCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmActTaskAssignee;
import com.ose.tasks.entity.bpm.BpmReDeployment;
import com.ose.tasks.entity.bpm.BpmRuTask;

/**
 * service接口
 */
public interface DrawingTaskInterface {

    /**
     * 获取管线设计流程模型
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<BpmReDeployment> getModels(Long orgId, Long projectId);

    /**
     * 根据图号获取流程实例
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param dwgNo
     * @return
     */
    List<BpmActivityInstanceBase> findByEntityNo(Long orgId, Long projectId, String dwgNo);

    /**
     * 保存流程实例
     *
     * @param actInst
     * @return
     */
    BpmActivityInstanceBase saveActInst(BpmActivityInstanceBase actInst);

    /**
     * 保存任务分配信息
     *
     * @param assignee
     * @return
     */
    BpmActTaskAssignee saveActTaskAssignee(BpmActTaskAssignee assignee);

    /**
     * 保存运行时任务信息
     *
     * @param bpmRuTask
     * @return
     */
    BpmRuTask saveBpmRuTask(BpmRuTask bpmRuTask);

    /**
     * 获取图纸任务列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param criteria
     * @return
     */
    Page<BpmActivityInstanceDTO> actInstList(Long orgId, Long projectId, ActInstCriteriaDTO criteria);

    /**
     * 图纸打包历史。
     *
     * @param orgId
     * @param projectId
     * @param page
     * @return
     */
    Page<BatchDrawingTask> batchTaskDrawingList (
        Long orgId,
        Long projectId,
        PageDTO page
    );

    /**
     * 图纸打包停止。
     *
     * @param orgId
     * @param projectId
     * @param batchTaskDrawingId
     * @return
     */
    void stopBatchTaskDrawing (
        Long orgId,
        Long projectId,
        Long batchTaskDrawingId
    );

    /**
     * 图纸重新打包。
     *
     * @param orgId
     * @param projectId
     * @param batchTaskDrawingId
     * @return
     */
    void startBatchTaskDrawing (
        ContextDTO contextDTO,
        Project project,
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        Long batchTaskDrawingId
    );
    /**
     * 给所有有效图纸打包二维码并生成pdf。
     *
     * @param orgId
     * @param projectId
     * @param batchTaskDrawingId
     * @return
     */
     void startBatchTask(
        ContextDTO contextDTO,
        Project project,
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        Long batchTaskDrawingId
    );

    void startSubDrawingTask(
        ContextDTO contextDTO,
        Project project,
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        Long subDrawingId
    );

    void appoint(
        Long orgId,
        Long projectId,
        Long drawingId,
        DrawingAppointDTO appointDTO,
        OperatorDTO operator
    );

    void workHour(
        Long orgId,
        Long projectId,
        Long drawingId,
        Double workhour
    );

}
