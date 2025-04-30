package com.ose.tasks.domain.model.service.plan;

import com.ose.auth.entity.Organization;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.PlanQueueDTO;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import com.ose.tasks.dto.drawing.UploadDrawingFileResultDTO;
import com.ose.tasks.dto.wbs.WBSEntriesDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryWithRelations;
import com.ose.tasks.entity.worksite.WorkSite;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * 计划管理服务接口。
 */
public interface PlanExecutionInterface extends EntityInterface {

    /**
     * 指派负责组织。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param team       工作组信息
     * @param workSite   工作场地信息
     */
    void dispatch(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long wbsEntryId,
        Organization team,
        WorkSite workSite
    );

    /**
     * 指派人。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param wbsEntryId WBS条目ID
     * @param teamId     工作组ID
     * @param userId     用户ID
     * @param privilege  权限
     */
    void dispatch(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long wbsEntryId,
        Long teamId,
        Long userId,
        Long delegateId,
        UserPrivilege privilege
    );

    /**
     * 取得模块工序定义信息。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param moduleType 模块类型
     * @return 模块工序定义信息
     */
    ModuleProcessDefinition getModuleProcessDefinition(
        Long orgId,
        Long projectId,
        String moduleType
    );


    /**
     * 取得模块工序定义信息。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param moduleType 模块类型
     * @param funcPart   功能块
     * @return 模块工序定义信息
     */
    ModuleProcessDefinition getModuleProcessDefinition(
        Long orgId,
        Long projectId,
        String moduleType,
        String funcPart
    );

    /**
     * 判断指定四级计划条目的所有前置任务是否均已完成且被接受（APPROVED），
     * 若均已完成且被接受则尝试启动该四级计划。
     *
     * @param todoTaskDispatchService 工作流任务服务
     * @param operator                操作者信息
     * @param orgId                   组织 ID
     * @param projectId               项目 ID
     * @param moduleProcessDefinition 模块工序工作流定义
     * @param processes               项目工序映射表（工序阶段名称/工序名称）
     * @param wbsEntry                四级计划条目
     * @return 是否已启动
     */
    boolean startWBSEntryWhenPredecessorsApproved(
        ContextDTO contextDTO,
        TodoTaskDispatchInterface todoTaskDispatchService,
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        ModuleProcessDefinition moduleProcessDefinition,
        Map<String, Long> processes,
        boolean optional,
        String predecessorElementId,
        WBSEntry wbsEntry
    );

    /**
     * 启动四级计划工作流。
     *
     * @param todoTaskDispatchService 任务服务
     * @param operator                操作者信息
     * @param orgId                   组织 ID
     * @param projectId               项目 ID
     * @param wbsEntryId              WBS 条目 ID
     * @param forceStart              是否强制启动
     * @return 是否已启动
     */
    boolean startWBSEntry(
        ContextDTO contextDTO,
        TodoTaskDispatchInterface todoTaskDispatchService,
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long wbsEntryId,
        boolean forceStart
    );



    void batchStartWBSEntry(ContextDTO context, Long orgId, Long projectId, OperatorDTO operator, WBSEntriesDTO wBSEntryDTO);

    /**
     * 启动四级计划工作流。
     *
     * @param todoTaskDispatchService 任务服务
     * @param operator                操作者信息
     * @param orgId                   组织 ID
     * @param projectId               项目 ID
     * @param wbsEntityEntry          WBS 实体工序条目
     * @param processId               工序 ID
     * @param forceStart              是否强制启动
     * @return 是否已启动
     */
    boolean startWBSEntry(ContextDTO contextDTO,
                          TodoTaskDispatchInterface todoTaskDispatchService,
                          OperatorDTO operator,
                          Long orgId,
                          Long projectId,
                          WBSEntry wbsEntityEntry,
                          Long processId,
                          boolean forceStart
    );

    /**
     * 将 WBS 条目标记为挂起。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId 条目 ID
     */
    void suspend(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long wbsEntryId
    );

    /**
     * 恢复挂起的 WBS 条目。
     *
     * @param todoTaskDispatchService 任务服务
     * @param operator                操作者信息
     * @param orgId                   组织 ID
     * @param projectId               项目 ID
     * @param wbsEntryId              条目 ID
     */
    void resume(
        ContextDTO contextDTO,
        TodoTaskDispatchInterface todoTaskDispatchService,
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long wbsEntryId
    );

    /**
     * 将 WBS 条目标记为已完成。
     *
     * @param todoTaskDispatchService 任务服务
     * @param operator                操作者信息
     * @param orgId                   组织 ID
     * @param projectId               项目 ID
     * @param entityId                实体 ID
     * @param processId               工序 ID
     * @param approved                是否通过
     * @param hours                   实际工时
     * @param isHalt                  是否启动后续任务
     * @param isPartOut               是否是外协导入
     */
    WBSEntry finish(
        ContextDTO contextDTO,
        TodoTaskDispatchInterface todoTaskDispatchService,
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long entityId,
        Long processId,
        boolean approved,
        double hours,
        Boolean isHalt,
        boolean isPartOut,
        Boolean forceStart
    );

    /**
     * 将 WBS 条目标记为已完成。
     *
     * @param todoTaskDispatchService 任务服务
     * @param operator                操作者信息
     * @param orgId                   组织 ID
     * @param projectId               项目 ID
     * @param wbsEntryId              条目 ID
     * @param approved                是否通过
     * @param hours                   实际工时
     * @param isHalt                  是否启动后续任务
     */
    WBSEntry finish(
        ContextDTO contextDTO,
        final TodoTaskDispatchInterface todoTaskDispatchService,
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long wbsEntryId,
        boolean approved,
        double hours,
        Boolean isHalt,
        Boolean forceStart
    );

    /**
     * 取得 WBS 条目详细信息。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     */
    WBSEntryWithRelations get(Long projectId, Long wbsEntryId);

    void pushQueue(ContextDTO context, Long projectId, Long wbsEntryId);

    void resetPlanQueue(Long orgId, Long projectId);

    /**
     * 获取四级计划队列信息。
     *
     * @param projectId
     * @return
     */
    Page<PlanQueueDTO> getQueue(Long projectId, PageDTO pageDTO);

    void pushAddedEntityQueue(ContextDTO context, Long projectId, Long wbsEntryId);
}
