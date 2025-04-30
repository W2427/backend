package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.ExportFileDTO;
import com.ose.tasks.dto.bpm.TodoTaskCriteriaDTO;
import com.ose.tasks.dto.bpm.TodoTaskExecuteDTO;
import com.ose.tasks.dto.bpm.TodoTaskForemanDispatchDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 代办任务的基础业务服务接口， TodoTask Individual task
 */
public interface TodoIndividualTaskInterface {


    /**
     * 解锁图纸
     *
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @param actInst
     */
    void unlockDrawing(Long orgId, Long projectId, BpmActivityInstanceBase actInst);


    /**
     * 更新焊口实体的NDT执行flg
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entityId
     */

    /**
     * 查询图纸设计变更流程列表
     *
     * @param id
     * @return
     */
    List<BpmActivityInstanceBase> getSubActInstFromModificationReviewRegister(Long projectId, Long id);

    /**
     * 查询建造变更引起的图纸变更流程列表
     *
     * @param id
     * @return
     */
    List<BpmActivityInstanceBase> getSubActInstFromConstructionChangeRegister(Long projectId, Long id);




    /**
     * 预生成ndt报告
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param toDoTaskDTO
     * @return
     */
    ExportFileDTO preBuildReport(Long orgId, Long projectId, TodoTaskExecuteDTO toDoTaskDTO);


    /**
     * 查询待办任务-班长分配任务列表
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param assignee
     * @param taskCriteria
     * @param pageDTO      分页DTO
     * @return
     */
    Page<TodoTaskForemanDispatchDTO> searchForemanDispatchTodo(Long orgId, Long projectId, Long assignee,
                                                               TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO);


}

