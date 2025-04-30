package com.ose.tasks.domain.model.service.bpm.taskexec;

import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.bpm.TodoTaskExecuteDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmRuTask;

import java.util.List;

/**
 * 工作流执行 接口。调用关系如下：
 * 通过工作流的 actInstance（流程实例）的大类型(act_category)和工序名称（process_name)，实体类型和子类型，任务节点名称（task_def_key_）以及人员权限 category_ 来确定 任务类型 Task_type
 * 定义任务类型 枚举 TASK_TYPE。
 * 定义 流程节点 类型对照表，包括以上字段，再增加顺序字段 order
 * 实现 AbstractTaskExecService 抽象类，这个类里边定义一个 抽象类，并实现泛型的构造方法
 * 对照每一个 TASK_TYPE 定义一个 Task_Type_Exec的类，并实现 TaskExecInterface接口。继承AbstractTaskExecService类
 */
public interface DrawingExecInterface {

    /**
     * 修改子图纸的reviewStatus
     *
     * @param actInst
     * @param toDoTaskDTO
     * @param toDoTaskDTO
     */
    void modifySubDrawingReviewStatus(
        BpmActivityInstanceBase actInst,
        TodoTaskExecuteDTO toDoTaskDTO
    );

    /**
     * 会签
     *
     * @param actTaskId
     * @param userid
     * @param toDoTaskDTO
     * @param actInst
     * @return
     */
    boolean signAssign(Long taskId, Long userid, TodoTaskExecuteDTO toDoTaskDTO, BpmActivityInstanceBase actInst);


    /**
     * 图纸remark操作
     *
     * @param projectId             项目ID
     * @param orgId                 组织ID
     * @param actInst
     * @param bpmRuTask
     * @param project
     * @param userId
     * @return
     */
    boolean drawingRemark(
        Long orgId,
        Long projectId,
        BpmActivityInstanceBase actInst,
        BpmRuTask bpmRuTask,
        Long userId,
        Project project
    );

    /**
     * RED-MARK 流程 并且节点为 USER-DRAWING-DESIGN，取得图纸信息。
     */
    List<ActReportDTO> getJsonDocument(ExecResultDTO execResult);

}

