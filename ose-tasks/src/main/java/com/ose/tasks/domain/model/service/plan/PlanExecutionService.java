package com.ose.tasks.domain.model.service.plan;


import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.auth.entity.Organization;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.ContextDTO;
import com.ose.dto.EventModel;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.BatchTaskRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.process.BpmnTaskRelationRepository;
import com.ose.tasks.domain.model.repository.process.EntityProcessRepository;
import com.ose.tasks.domain.model.repository.process.ModuleProcessDefinitionRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.plan.business.PlanBusinessInterface;
import com.ose.tasks.domain.model.service.taskpackage.TaskPackageInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.BpmnSequenceNodeDTO;
import com.ose.tasks.dto.PlanQueueDTO;
import com.ose.tasks.dto.bpm.ActivityInstanceDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.drawing.UploadDrawingFileResultDTO;
import com.ose.tasks.dto.wbs.WBSEntriesDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.BpmnTaskRelation;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.process.EntityProcess;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.entity.worksite.WorkSite;
import com.ose.tasks.util.wbs.WorkflowEvaluator;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.tasks.vo.wbs.WBSEntryType;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.EventType;
import com.ose.vo.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ose.tasks.vo.setting.BatchTaskCode.PROJECT_TASK_GENERATE;

/**
 * 计划管理服务。
 */
@Component
public class PlanExecutionService extends StringRedisService implements PlanExecutionInterface {

    // 启动后续任务时排除的执行状态
    // 一个四级计划完成并准备启动后续四级计划时，将排除已中止、执行中、已忽略的后续任务
    // 已通过及已驳回的后续任务也将会被启动（只要根据流程能够推荐到该四级计划）
    private static final Set<WBSEntryRunningStatus> SUCCESSOR_EXCLUDED_STATUS = new HashSet<>();

    // 非同一实体后续任务排除的执行状态
    private static final Set<WBSEntryRunningStatus> SUCCESSOR_FINISHED_STATUS = new HashSet<>();

    // 批量启动时排除的执行状态
    // 通过指定三级计划批量启动四级计划时，将从四级计划中排除已中止、执行中、已通过、已驳回、已忽略的四级计划
    // 即仅启动等待中的四级计划
    private static final Set<WBSEntryRunningStatus> BATCH_START_EXCLUDED_STATUS = new HashSet<>();

    private final BpmProcessRepository processRepository;

    static {
        SUCCESSOR_EXCLUDED_STATUS.add(WBSEntryRunningStatus.SUSPENDED);
        SUCCESSOR_EXCLUDED_STATUS.add(WBSEntryRunningStatus.RUNNING);
        SUCCESSOR_EXCLUDED_STATUS.add(WBSEntryRunningStatus.IGNORED);
        SUCCESSOR_FINISHED_STATUS.add(WBSEntryRunningStatus.APPROVED);
        SUCCESSOR_FINISHED_STATUS.add(WBSEntryRunningStatus.REJECTED);
        BATCH_START_EXCLUDED_STATUS.add(WBSEntryRunningStatus.SUSPENDED);
        BATCH_START_EXCLUDED_STATUS.add(WBSEntryRunningStatus.RUNNING);
        BATCH_START_EXCLUDED_STATUS.add(WBSEntryRunningStatus.APPROVED);
        BATCH_START_EXCLUDED_STATUS.add(WBSEntryRunningStatus.REJECTED);
        BATCH_START_EXCLUDED_STATUS.add(WBSEntryRunningStatus.IGNORED);
    }

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // WBS 条目数据仓库
    private final WBSEntryRepository wbsEntryRepository;

    private final TaskPackageInterface taskPackageService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    // WBS 条目数据仓库
    private final WBSEntryWithRelationsRepository wbsEntryWithRelationsRepository;

    // WBS 条目关系数据仓库
    private final WBSEntryRelationRepository wbsEntryRelationRepository;

    // 实体工序视图数据仓库
    private final EntityProcessRepository entityProcessRepository;

    // 模块工作流定义数据仓库
    private final ModuleProcessDefinitionRepository moduleProcessDefinitionRepository;

    // 四级计划前置任务统计视图
    private final WBSEntryPredecessorStatisticsRepository wbsEntryPredecessorStatisticsRepository;

    private final BpmnTaskRelationRepository bpmnTaskRelationRepository;

    // 任务分配记录仓库
    private final WBSEntryDelegateRepository wbsEntryDelegateRepository;

    // 计划业务
    private final PlanBusinessInterface planBusiness;

    private final BatchTaskRepository batchTaskRepository;

    // EntityManager
    private final EntityManager entityManager;
    private final PlanRelationInterface planRelationService;
    private final BpmEntitySubTypeRepository bpmEntitySubTypeRepository;
    private final ProcessInterface processService;
    private final WBSEntryBlobRepository wbsEntryBlobRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final DrawingRepository drawingRepository;

    private final BpmActivityInstanceRepository activityInstanceRepository;

    private final BatchTaskInterface batchTaskService;

    private final ProjectInterface projectService;

    /**
     * 构造方法。
     */
    @Autowired
    public PlanExecutionService(
        WBSEntryRepository wbsEntryRepository,
        TaskPackageInterface taskPackageService,
        WBSEntryWithRelationsRepository wbsEntryWithRelationsRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        EntityProcessRepository entityProcessRepository,
        ModuleProcessDefinitionRepository moduleProcessDefinitionRepository,
        WBSEntryPredecessorStatisticsRepository wbsEntryPredecessorStatisticsRepository,
        BpmnTaskRelationRepository bpmnTaskRelationRepository,
        WBSEntryDelegateRepository wbsEntryDelegateRepository,
        PlanBusinessInterface planBusinessImpl,
        EntityManager entityManager,
        PlanRelationInterface planRelationService,
        BpmProcessRepository processRepository,
        BpmEntitySubTypeRepository bpmEntitySubTypeRepository,
        StringRedisTemplate stringRedisTemplate,
        BatchTaskRepository batchTaskRepository, ProcessInterface processService,
        WBSEntryBlobRepository wbsEntryBlobRepository, WBSEntryStateRepository wbsEntryStateRepository,
        DrawingRepository drawingRepository,
        TodoTaskDispatchInterface todoTaskDispatchService,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService,
        BpmActivityInstanceRepository activityInstanceRepository) {
        super(stringRedisTemplate);
        this.wbsEntryRepository = wbsEntryRepository;
        this.taskPackageService = taskPackageService;
        this.wbsEntryWithRelationsRepository = wbsEntryWithRelationsRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.entityProcessRepository = entityProcessRepository;
        this.moduleProcessDefinitionRepository = moduleProcessDefinitionRepository;
        this.wbsEntryPredecessorStatisticsRepository = wbsEntryPredecessorStatisticsRepository;
        this.bpmnTaskRelationRepository = bpmnTaskRelationRepository;
        this.wbsEntryDelegateRepository = wbsEntryDelegateRepository;
        this.planBusiness = planBusinessImpl;
        this.entityManager = entityManager;
        this.planRelationService = planRelationService;
        this.processRepository = processRepository;
        this.bpmEntitySubTypeRepository = bpmEntitySubTypeRepository;
        this.batchTaskRepository = batchTaskRepository;
        this.processService = processService;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.drawingRepository = drawingRepository;
        this.activityInstanceRepository = activityInstanceRepository;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
    }

    /**
     * 取得 WBS 条目详细信息。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     */
    @Override
    public WBSEntryWithRelations get(Long projectId, Long wbsEntryId) {
        return wbsEntryWithRelationsRepository
            .findByProjectIdAndIdAndDeletedIsFalse(projectId, wbsEntryId)
            .orElse(null);
    }


    /**
     * 将 WBS 条目标记为挂起。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId 条目 ID
     */
    @Override
    @Transactional
    public void suspend(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long wbsEntryId
    ) {

        WBSEntryState wbsEntryState = wbsEntryStateRepository
            .findByWbsEntryId(wbsEntryId);

        if (wbsEntryState == null) {
            throw new NotFoundError("error.wbs.entity-process.not-found"); // TODO error.wbs.entry.not-found
        }

        // 若计划已结束或已挂起则结束
        if (wbsEntryState.isFinished()
            || wbsEntryState.getRunningStatus() == WBSEntryRunningStatus.SUSPENDED) {
            return;
        }

        wbsEntryState.setRunningStatus(WBSEntryRunningStatus.SUSPENDED);
        wbsEntryState.setLastModifiedAt(new Date());
        wbsEntryState.setLastModifiedBy(operator.getId());

        wbsEntryStateRepository.save(wbsEntryState);
        WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntryId);
        if (wbsEntryBlob == null) return;
        // 更新所有未完成且未挂起的子节点的状态
        wbsEntryRepository.updateChildrenRunningStatus(
            projectId,
            wbsEntryId,
            WBSEntryRunningStatus.SUSPENDED,
            new Date(),
            operator.getId()
        );

    }

    /**
     * 恢复挂起的 WBS 条目。
     *
     * @param todoTaskDispatchService 任务服务
     * @param operator                操作者信息
     * @param orgId                   组织 ID
     * @param projectId               项目 ID
     * @param wbsEntryId              条目 ID
     */
    @Override
    @Transactional
    public void resume(
        final ContextDTO contextDTO,
        final TodoTaskDispatchInterface todoTaskDispatchService,
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long wbsEntryId
    ) {

        // 取得选定的节点
        WBSEntry wbsEntry = wbsEntryRepository
            .findByProjectIdAndIdAndActiveIsTrueAndDeletedIsFalse(projectId, wbsEntryId)
            .orElse(null);

        if (wbsEntry == null) {
            throw new NotFoundError("error.wbs.entity-process.not-found"); // TODO error.wbs.entry.not-found
        }

        final Map<String, Long> processes = new HashMap<>();//planBusiness.getProcesses(projectId);

        // 取得选定节点的所有挂起状态的子节点
        List<WBSEntry> wbsEntries = wbsEntryRepository
            .findByPathLikeAndRunningStatusIsAndFinishedIsFalseAndActiveIsTrueAndDeletedIsFalse(
                wbsEntry.getId(),
                WBSEntryRunningStatus.SUSPENDED
            );

//        if (wbsEntry.getRunningStatus() == WBSEntryRunningStatus.SUSPENDED) {
        wbsEntries.add(wbsEntry);
//        }

        // 尝试启动取得的计划条目
        wbsEntries.forEach(entry -> startWBSEntryWhenPredecessorsApproved(contextDTO,
            todoTaskDispatchService,
            operator,
            orgId,
            projectId,
            null,
            processes,
            false,
            null,
            entry
        ));

    }

    /**
     * 取得模块工序定义信息。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param moduleType 模块类型
     * @return 模块工序定义信息
     */
    @Override
    public ModuleProcessDefinition getModuleProcessDefinition(Long orgId, Long projectId, String moduleType) {
//        return moduleProcessDefinitionRepository
//            .findByOrgIdAndProjectIdAndModuleTypeAndDeletedIsFalse(orgId, projectId, moduleType)
//            .orElse(null);
        return getModuleProcessDefinition(orgId, projectId, moduleType, "PIPING");

    }

    /**
     * 取得模块工序定义信息。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param moduleType 模块类型
     * @param funcPart   功能块
     * @return 模块工序定义信息
     */
    @Override
    public ModuleProcessDefinition getModuleProcessDefinition(
        Long orgId,
        Long projectId,
        String moduleType,
        String funcPart
    ) {
        return moduleProcessDefinitionRepository
            .findByOrgIdAndProjectIdAndFuncPartAndDeletedIsFalse(
                orgId, projectId, funcPart);
    }

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
     * @return 计划条目
     */
    @Override
    public WBSEntry finish(
        final ContextDTO contextDTO,
        final TodoTaskDispatchInterface todoTaskDispatchService,
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long entityId,
        final Long processId,
        final boolean approved,
        final double hours,
        final Boolean isHalt,
        final boolean isPartOut,
        final Boolean forceStart
    ) {

        EntityProcess entityProcess = entityProcessRepository
            .findByProjectIdAndProcessId(projectId, processId)
            .orElse(null);

        if (entityProcess == null) {
            throw new NotFoundError("error.entity-process.not-found");
        }

        WBSEntry entry = null;
        if (isPartOut) {
            entry = wbsEntryRepository
                .findByProjectIdAndEntityIdAndProcessIdAndDeletedIsFalseAndFinishStateAndIsPartOut(
                    projectId,
                    entityId,
                    entityProcess.getProcessId()
                )
                .orElse(null);
        } else {
            entry = wbsEntryRepository
                .findByProjectIdAndEntityIdAndProcessIdAndDeletedIsFalseAndFinishState(
                    projectId,
                    entityId,
                    entityProcess.getProcessId()
                )
                .orElse(null);
        }

        if (entry == null) {
            return null;
        }
//        if(isHalt == null) {isHalt = false;}

        finish(contextDTO,
            todoTaskDispatchService,
            operator,
            orgId,
            projectId,
            entry,
            approved,
            hours,
            isHalt,
            forceStart
        );

        return entry;
    }

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
     * @return 计划条目
     */
    @Override
    public WBSEntry finish(
        final ContextDTO contextDTO,
        final TodoTaskDispatchInterface todoTaskDispatchService,
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long wbsEntryId,
        final boolean approved,
        final double hours,
        final Boolean isHalt,
        final Boolean forceStart
    ) {

        WBSEntry wbsEntry = wbsEntryRepository
            .findByProjectIdAndIdAndActiveIsTrueAndDeletedIsFalse(projectId, wbsEntryId)
            .orElse(null);

        finish(contextDTO,
            todoTaskDispatchService,
            operator,
            orgId,
            projectId,
            wbsEntry,
            approved,
            hours,
            isHalt,
            forceStart
        );

        return wbsEntry;
    }

    /**
     * 将 WBS 条目标记为已完成。
     *
     * @param todoTaskDispatchService 任务服务
     * @param operator                操作者信息
     * @param orgId                   组织 ID
     * @param projectId               项目 ID
     * @param wbsEntry                条目
     * @param approved                是否通过
     * @param hours                   实际工时
     * @param isHalt                  是否启动后置任务
     */
    private Boolean finish(
        final ContextDTO contextDTO,
        final TodoTaskDispatchInterface todoTaskDispatchService,
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final WBSEntry wbsEntry,
        final boolean approved,
        final double hours,
        final Boolean isHalt,
        final Boolean forceStart
    ) {

        if (wbsEntry == null) {
            throw new NotFoundError("error.wbs.entity-process.not-found"); // TODO error.wbs.entry.not-found
        }
        WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
        if (wbsEntryState == null) {
            throw new NotFoundError("error.wbs.entity-process.not-found"); // TODO error.wbs.entry.not-found
        }
        // TODO 暂不考虑无条件中止需求
        if (approved) {
            wbsEntryState.setFinishedScore(wbsEntryState.getTotalScore());
        }
//        wbsEntry.setFinishedScore(wbsEntry.getTotalScore());

        // 保存条目信息
        wbsEntryState.setFinishedAt(new Date());
        //wbsEntry.setActualDuration(hours);
        wbsEntryState.setActualManHours(hours);
        wbsEntryState.setFinished(true);
        //FTJ 如果是 list，则 approved 决定 是 approved 或 running；如果不是，则 决定为 REJECT 或 APPROVED
        Long processId = wbsEntry.getProcessId();
        if (processId == null) {
            throw new NotFoundError("error.wbs.entity-process.not-found"); // TODO error.wbs.entry.not-found
        }
        BpmProcess bpmProcess = processService.getBpmProcess(processId);
        if (!approved) {
            if (ProcessType.CUT_LIST.equals(bpmProcess.getProcessType()) ||
                ProcessType.DELIVERY_LIST.equals(bpmProcess.getProcessType())) {

                wbsEntryState.setRunningStatus(WBSEntryRunningStatus.RUNNING);
            } else {
                wbsEntryState.setRunningStatus(WBSEntryRunningStatus.REJECTED);
            }
        } else {
            wbsEntryState.setRunningStatus(WBSEntryRunningStatus.APPROVED);
        }
//        wbsEntry.setRunningStatus(approved ? WBSEntryRunningStatus.APPROVED : WBSEntryRunningStatus.RUNNING);
        wbsEntryState.setLastModifiedBy(operator.getId());
        wbsEntryState.setLastModifiedAt(new Date());
//        entityManager.flush();
//        try {
        wbsEntryStateRepository.save(wbsEntryState);

        //更新 TaskPackage 的 count 和work load
        Long taskPackageId = wbsEntryState.getTaskPackageId();
        if (taskPackageId != null) {
            taskPackageService.updateTaskPackageCount(taskPackageId, wbsEntryState.getWorkLoad());
        }

        //
//        } catch (org.springframework.dao.CannotAcquireLockException e) {
//
//        }
//        if(entityManager.getTransaction() != null) {
//            entityManager.flush();
//        }
        //刷新 持久化上下文

        // TODO 暂不考虑无条件中止需求
        if (!approved && isHalt != null && isHalt) {

            return true;
        }

        // 更新上级条目已完成权重之和
        planBusiness.updateFinishedScoreOfParents(wbsEntry);

        if (wbsEntry.getType() != WBSEntryType.ENTITY) {
            return false;
        }

//        if (!FuncPart.isSupportedFuncPart(wbsEntry.getFuncPart())) {
//            return false;
//        }
        // 取得模块工作流定义
        final ModuleProcessDefinition moduleProcessDefinition
            = getModuleProcessDefinition(orgId, projectId, wbsEntry.getModuleType(), wbsEntry.getFuncPart());

        if (moduleProcessDefinition == null) {
            throw new BusinessError("error.module-workflow.not-deployed");
        }

        // 当前条目实体 ID
        final Long predecessorEntityId = wbsEntry.getEntityId();

        // 取得实体的工序信息并生成工序-工序 ID 映射表
        final Map<String, Long> processes = new HashMap<>();//planBusiness.getProcesses(projectId);

        final String predecessorCategoryId = wbsEntry.getStage() + "/" +
            wbsEntry.getProcess() + "/" +
            wbsEntry.getEntityType() + "/" +
            wbsEntry.getEntitySubType();

        // TODO 取得所有后续任务关系，并启动四级计划及任务
        List<WBSEntryRelation> wBSEntryRelationList = wbsEntryRelationRepository
            .findByProjectIdAndPredecessorId(projectId, wbsEntry.getGuid());
//        Boolean started = true;
        for (WBSEntryRelation entryRelation : wBSEntryRelationList) {
            // 取得后续任务信息
            WBSEntry successor = wbsEntryRepository
                .findByProjectIdAndGuid(projectId, entryRelation.getSuccessorId())
                .orElse(null);
            if (successor == null) continue;
            WBSEntryState successorState = wbsEntryStateRepository.findByWbsEntryId(successor.getId());
            // 若后续任务不存在或未激活或已启动则略过
            if (!forceStart && (
                successorState == null
                    || successor.getDeleted()
                    || !successor.getActive()
                    || SUCCESSOR_EXCLUDED_STATUS.contains(successorState.getRunningStatus())
                    || (SUCCESSOR_FINISHED_STATUS.contains(successorState.getRunningStatus())
                    && !predecessorEntityId.equals(successor.getEntityId())))
            ) {
//                started = false;
                continue;
            }

            // 启动后续任务
            Boolean finished = startWBSEntryWhenPredecessorsApproved(
                contextDTO,
                todoTaskDispatchService,
                operator,
                orgId,
                projectId,
                moduleProcessDefinition,
                processes,
                entryRelation.getOptional(),
                predecessorCategoryId,
                successor
            );
//            if (!finished) {
//                started = false;
//            }

        }
        // TODO 如果后续任务有未启动的则将四级计划状态改为running，用来作为后续任务是否启动判断
//        if (!started) {
//            wbsEntry.setFinishedAt(null);
//            //wbsEntry.setActualDuration(hours);
//            wbsEntry.setActualManHours(hours);
//            wbsEntry.setFinished(false);
//            wbsEntry.setRunningStatus(WBSEntryRunningStatus.RUNNING);
//            wbsEntry.setLastModifiedBy(operator.getId());
//            wbsEntry.setLastModifiedAt();
//            wbsEntryRepository.save(wbsEntry);
//        }
        return true;
    }


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
    @Override
    public boolean startWBSEntryWhenPredecessorsApproved(
        final ContextDTO contextDTO,
        final TodoTaskDispatchInterface todoTaskDispatchService,
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        ModuleProcessDefinition moduleProcessDefinition,
        final Map<String, Long> processes,
        final boolean optional,
        final String predecessorCategoryId,
        final WBSEntry wbsEntry
    ) {

        if (wbsEntry.getType() != WBSEntryType.ENTITY) {
            return false;
        }

//        if (!FuncPart.isSupportedFuncPart(wbsEntry.getFuncPart())) {
//            return false;
//        }
        // 取得模块工作流定义
        if (moduleProcessDefinition == null) {
            moduleProcessDefinition
                = getModuleProcessDefinition(orgId, projectId, wbsEntry.getModuleType(), wbsEntry.getFuncPart());
        }

        if (moduleProcessDefinition == null) {
            throw new BusinessError("error.module-workflow.not-deployed");
        }

        entityManager.clear();

        // 取得前置任务的统计信息
        WBSEntryPredecessorStatistics statistics = wbsEntryPredecessorStatisticsRepository
            .findStatisticByProjectIdAndId(projectId, wbsEntry.getGuid());

        // 若存在尚未完成的前置任务则不启动该任务
        if (statistics != null && (statistics.getNotFinished() > 0 || statistics.getNotApproved() > 0)) {
            WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());

            if (wbsEntryState != null && wbsEntryState.getRunningStatus() == WBSEntryRunningStatus.SUSPENDED) {
                wbsEntryState.setRunningStatus(null);
                wbsEntryState.setLastModifiedAt(new Date());
                wbsEntryState.setLastModifiedBy(operator.getId());
                wbsEntryStateRepository.save(wbsEntryState);
            }

            return false;
        }

        boolean doStartTask = false;
        String successorEntityType = wbsEntry.getEntityType() == null ? null : wbsEntry.getEntityType();
        String predecessorEntityType = statistics == null ? null : statistics.getPredecessorEntityTypes();
        boolean entityTypeMatched = false;
        if (successorEntityType != null && successorEntityType.equalsIgnoreCase(predecessorEntityType)) {
            entityTypeMatched = true;
        }

        // 若与前置任务为不同类型的实体的工序则直接启动任务
        if (statistics != null && !entityTypeMatched && !optional) {
            doStartTask = true;
            // 否则将实体信息作为参数检查该任务是否可以启动
        } else {

            WorkflowProcessVariable variable = planBusiness.getWorkflowEntityVariable(
                wbsEntry.getProjectId(),
                wbsEntry.getEntityType(),
                wbsEntry.getEntityId(),
                wbsEntry.getFuncPart()
            );

            if (variable == null) {
                return false; // TODO throw not found error
            }

            Map<String, Object> variables = new HashMap<>();
            variables.put(variable.getVariableName(), variable);


//            if (!FuncPart.isSupportedFuncPart(wbsEntry.getFuncPart())) {
//                return false;
//            }

            BpmnTaskRelation bpmnTaskRelation;

            String category = wbsEntry.getStage() + "/" +
                wbsEntry.getProcess() + "/" +
                wbsEntry.getEntityType() + "/" +
                wbsEntry.getEntitySubType();
            Long moduleProcessDefinitionId = moduleProcessDefinition.getId();
            bpmnTaskRelation = planRelationService.getBpmnTaskRelationFromRedis(orgId, projectId, moduleProcessDefinitionId, category);

//            bpmnTaskRelation = bpmnTaskRelationRepository.
//                findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndCategoryAndDeletedIsFalse(
//                    wbsEntry.getOrgId(),
//                    wbsEntry.getProjectId(),
//                    moduleProcessDefinition.getId(),
//                    category
//                );

            if (bpmnTaskRelation == null) {
                return false;
            }
            //如果指定了起始节点, 并且 存在这个节点
            if (predecessorCategoryId != null && //有前置节点
                bpmnTaskRelation != null && !optional) {  //有后置节点

                BpmnTaskRelation preBpmnTaskRelation = bpmnTaskRelationRepository.
                    findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndCategoryAndDeletedIsFalse(
                        orgId,
                        projectId,
                        moduleProcessDefinition.getId(),
                        predecessorCategoryId
                    );

                if (preBpmnTaskRelation != null &&
                    evaluateFlow(preBpmnTaskRelation, bpmnTaskRelation, variables)) {
                    doStartTask = true;
                }
            } else if (bpmnTaskRelation != null &&
                WorkflowEvaluator.evaluate(bpmnTaskRelation, variables, true, predecessorCategoryId)) {
                //从默认的前置任务开始演算
                doStartTask = true;
            }


        }

        // 启动工作流
        if (doStartTask) {
            return startWBSEntry(
                contextDTO,
                todoTaskDispatchService,
                operator,
                orgId,
                projectId,
                wbsEntry,
                wbsEntry.getProcessId(),//processes.get(wbsEntry.getStage() + "/" + wbsEntry.getProcess()),
                false
            );
        }

        return false;
    }


    private Boolean evaluateFlow(BpmnTaskRelation preBpmnTaskRelation,
                                 BpmnTaskRelation bpmnTaskRelation,
                                 Map<String, Object> variables) {

        /* ---------------------------
        1.0 开始点和结束点的 entityType相同，或者 为START/END 节点
            1.1 如果 predecessor 的 nodeId 在 sucessor 中的 predecessorNodes 中，开始演算检查 调用 WrokflowEvaluator
            1.2 如果 predecessor 的 nodeId 不在 sucessor 中的。由 predecessor节点往下查找。
                查找 同一个部署Id，（实体类型相同 或类型为END），节点Id 为 sucessor 中的节点。之后重复1.0开始的过程
        2.0 实体类型不一致，返回 false
         ---------------------------------------- */

        Set<String> predecessorIds = new HashSet<>();
        Set<String> allPredecessorIds = new HashSet<>();
        Set<String> handledIds = new HashSet<>();
        //保存 nodeId->BpmnTaskRelation
        Set<BpmnTaskRelation> bpmnTaskRelationSet = new HashSet<>();
        BpmnTaskRelation evaluateBpmTaskRelation = new BpmnTaskRelation();
        List<BpmnSequenceNodeDTO> evaluatePredecessorNodes = new ArrayList<>();

        String predecessorId = preBpmnTaskRelation.getNodeId();

        //开始节点的实体类型
        String startEntityType = "";
        //终止点的实体类型
        String endEntityType = "";

        if (!StringUtils.isEmpty(preBpmnTaskRelation.getCategory())) {
            startEntityType = preBpmnTaskRelation.getCategory().split("/")[2];
        }

        if (!StringUtils.isEmpty(bpmnTaskRelation.getCategory())) {
            endEntityType = bpmnTaskRelation.getCategory().split("/")[2];
        }

        bpmnTaskRelationSet.add(bpmnTaskRelation);

        //1.0 如果实体类型相同
        if (preBpmnTaskRelation.getNodeType().equalsIgnoreCase("START") ||
            bpmnTaskRelation.getNodeType().equalsIgnoreCase("END") ||
            startEntityType.equalsIgnoreCase(endEntityType)) {

            do {

                for (BpmnTaskRelation bpmnTaskR : bpmnTaskRelationSet) {
                    List<BpmnSequenceNodeDTO> predecessorNodes = bpmnTaskR.getJsonPredecessorNodes();

//                        BPMNUtils.expensionNodes(
//                        bpmnTaskR.getJsonPredecessorNodes()
//                    );
                    predecessorNodes.forEach(predecessorNode -> {

                        predecessorIds.add(predecessorNode.getNodeId());
                        if (predecessorId.equals(predecessorNode.getNodeId())) {
                            evaluatePredecessorNodes.add(predecessorNode);
                        }
                    });
                    allPredecessorIds.addAll(predecessorIds);

                    //1.1 前节点的后置任务 包含前置任务
                    if (predecessorIds.contains(predecessorId)) {
                        evaluateBpmTaskRelation.setJsonPredecessorNodes(evaluatePredecessorNodes);

                        return WorkflowEvaluator.evaluate(evaluateBpmTaskRelation, variables, true);

                        //1.2 如果不包含则遍历
                    }
                    handledIds.add(bpmnTaskR.getNodeId());
                }


                Set<BpmnTaskRelation> bpmnTaskRelations = bpmnTaskRelationRepository.
                    findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndNodeIdInAndDeletedIsFalse(
                        bpmnTaskRelation.getOrgId(),
                        bpmnTaskRelation.getProjectId(),
                        bpmnTaskRelation.getModuleProcessDefinitionId(),
                        allPredecessorIds
                    );

                predecessorIds.clear();
                allPredecessorIds.clear();
                bpmnTaskRelationSet.clear();

                for (BpmnTaskRelation bpmnTaskRe : bpmnTaskRelations) {
                    if (handledIds.contains(bpmnTaskRe.getNodeId())) {
                        continue;
                    }
                    String currentEntityType = null;
                    if (!StringUtils.isEmpty(bpmnTaskRe.getCategory())) {
                        currentEntityType = bpmnTaskRe.getCategory().split("/")[2];
                    }

                    if (!bpmnTaskRe.getNodeType().equalsIgnoreCase("START") &&
                        !startEntityType.equalsIgnoreCase(currentEntityType)) {
                        continue;
                    }
                    bpmnTaskRelationSet.add(bpmnTaskRe);
                }


            } while (!CollectionUtils.isEmpty(bpmnTaskRelationSet));

        } else {
            return false;

        }

        return false;

    }

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
    @Override
    public boolean startWBSEntry(
        final ContextDTO contextDTO,
        final TodoTaskDispatchInterface todoTaskDispatchService,
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long wbsEntryId,
        final boolean forceStart
    ) {

        //判断四级计划条目是否存在
        WBSEntry entry = wbsEntryRepository
            .findByProjectIdAndIdAndActiveIsTrueAndDeletedIsFalse(
                projectId,
                wbsEntryId
            )
            .orElse(null);
        if (entry == null) {
            throw new NotFoundError("error.wbs.not-found");
        }

        // 若不为实体工序则启动其下所有尚未开始的实体工序(循环调用本方法）
        if (entry.getType() != WBSEntryType.ENTITY) {
            AtomicInteger started = new AtomicInteger();

            wbsEntryStateRepository
                .findByProjectIdAndPathLikeAndTypeInAndDeletedIsFalse(
                    projectId,
                    entry.getId(),
                    Collections.singleton(WBSEntryType.ENTITY),
                    BATCH_START_EXCLUDED_STATUS
                )
                .forEach(entityWBS -> {

//                    if (!entityWBS.getActive()
//                        || BATCH_START_EXCLUDED_STATUS.contains(entityWBS.getRunningStatus())) {
//                        return;
//                    }

                    started.addAndGet(startWBSEntry(contextDTO,
                        todoTaskDispatchService,
                        operator,
                        orgId,
                        projectId,
                        entityWBS.getId(),
                        forceStart
                    ) ? 1 : 0);
                });

            return started.intValue() > 0;
        }

        // 判断四级计划条目当前状态，如果是running，则报错，不启动
        WBSEntryState entryState = wbsEntryStateRepository
            .findByWbsEntryId(
                wbsEntryId
            );
        if (entryState != null && entryState.getRunningStatus() == WBSEntryRunningStatus.RUNNING) {
            throw new ConflictError("error.wbs.running");
        }

        // 判断四级计划条目是否有未完成的任务，如果有则报错，不启动
        List<BpmActivityInstanceBase> tasks = activityInstanceRepository.findByProjectIdAndEntityNoAndFinishStateIsRunning(projectId, entry.getName());
        if (tasks.size() > 0) {
            throw new BusinessError(entry.getName() + " has unfinished task!");
        }

        //开始启动四级计划
        return startWBSEntry(contextDTO,
            todoTaskDispatchService,
            operator,
            orgId,
            projectId,
            entry,
            entry.getProcessId(),
            forceStart
        );

    }

    @Override
    public void batchStartWBSEntry(ContextDTO context, Long orgId, Long projectId, OperatorDTO operator, WBSEntriesDTO wBSEntryDTO) {

        final Project project = projectService.get(orgId, projectId);
        batchTaskService.run(context,
            project,
            PROJECT_TASK_GENERATE,
            batchTask -> {
                BatchResultDTO result = new BatchResultDTO(batchTask);
                for (Long entryId : wBSEntryDTO.getWbsEntryIds()) {
                    try {
                        startWBSEntry(
                            context,
                            todoTaskDispatchService,
                            operator,
                            orgId,
                            projectId,
                            entryId,
                            true
                        );
                        result.addProcessedCount(1);
                    } catch (Exception e) {
                        WBSEntry wbsEntry = wbsEntryRepository.findById(entryId).get();
                        result.addErrorCount(1);
                        result.addLog(wbsEntry.getName() + " : " + e.getMessage());
                    }
                }
                result.addTotalCount(wBSEntryDTO.getWbsEntryIds().size());
                return result;
            });
    }

    /**
     * 启动四级计划工作流。
     *
     * @param todoTaskDispatchService 任务创建服务
     * @param operator                操作者信息
     * @param orgId                   组织 ID
     * @param projectId               项目 ID
     * @param wbsEntityEntry          WBS 实体工序条目
     * @param forceStart              是否强制启动
     * @return 是否已启动
     */
    @Override
    public boolean startWBSEntry(ContextDTO contextDTO,
                                 final TodoTaskDispatchInterface todoTaskDispatchService,
                                 final OperatorDTO operator,
                                 final Long orgId,
                                 final Long projectId,
                                 final WBSEntry wbsEntityEntry,
                                 final Long processId,
                                 final boolean forceStart
    ) {
        Date startAt = wbsEntityEntry.getStartAt();
        Integer startBeforeHours = wbsEntityEntry.getStartBeforeHours();

        // 判断时间，若未到计划开始时间则将四级计划标记为【就绪PENDING】
//        if (!forceStart
//            && startAt != null
//            && (startAt.getTime() - System.currentTimeMillis()) > startBeforeHours * 3600000L) {
//            wbsEntityEntry.setRunningStatus(WBSEntryRunningStatus.PENDING);
//            wbsEntryRepository.save(wbsEntityEntry);
//            return false;
//        }

        // 获取工序类型、实体子类型
        BpmProcess bpmProcess = processRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, processId, EntityStatus.ACTIVE);
        BpmEntitySubType bpmEntitySubType = bpmEntitySubTypeRepository.findByProjectIdAndNameEnAndStatus(
            projectId,
            wbsEntityEntry.getEntitySubType(),
            EntityStatus.ACTIVE
        ).orElse(null);


        // 开始创建变量，启动实体工序工作流
        WBSEntryState wbsEntryState = null;
        boolean started = false;

        // 先判断当前计划是否含有任务包信息，如没有，状态置为PENDING，等有任务包后再启动
        WBSEntryState state = wbsEntryStateRepository.findByWbsEntryId(wbsEntityEntry.getId());
        if (state == null) {
            throw new NotFoundError("error.wbs.not-found");
        }
        if (state.getTaskPackageId() == null && false) {
            state.setRunningStatus(WBSEntryRunningStatus.PENDING);
            wbsEntryStateRepository.save(state);
        } else {
            // 如果有任务包，则开始创建流程实例
            try {
                ActivityInstanceDTO instanceDTO = new ActivityInstanceDTO();
                instanceDTO.setPlanStart(wbsEntityEntry.getStartAt());
                instanceDTO.setPlanEnd(wbsEntityEntry.getFinishAt());
                instanceDTO.setPlanHour(wbsEntityEntry.getDuration() == null ? 0 : wbsEntityEntry.getDuration().intValue());
                instanceDTO.setEntitySubType(wbsEntityEntry.getEntitySubType());
                instanceDTO.setEntitySubTypeId(bpmEntitySubType.getId());
                instanceDTO.setProcess(wbsEntityEntry.getProcess());
                instanceDTO.setProcessId(processId);
                instanceDTO.setEntityId(wbsEntityEntry.getEntityId());
                instanceDTO.setEntityNo(wbsEntityEntry.getName());
                instanceDTO.setEntityNo1(wbsEntityEntry.getName1());
                instanceDTO.setEntityNo2(wbsEntityEntry.getName2());
                Optional<Drawing> drawing = drawingRepository.findById(wbsEntityEntry.getEntityId());
                if (drawing.isPresent()) {
                    instanceDTO.setDrawingTitle(drawing.get().getDocumentTitle());
                }
                instanceDTO.setVersion("0");
                instanceDTO.setAssignee(operator.getId());
                instanceDTO.setAssigneeName(operator.getName());
                instanceDTO.setPlanStart(wbsEntityEntry.getStartAt());
                instanceDTO.setPlanEnd(wbsEntityEntry.getFinishAt());
//                instanceDTO.setEntityType(wbsEntityEntry.getEntityType() == null ? null : wbsEntityEntry.getEntityType());
                instanceDTO.setEntityType(wbsEntityEntry.getEntityType());
                instanceDTO.setPlanHour(wbsEntityEntry.getDuration() == null ? 0 : wbsEntityEntry.getDuration().intValue());

                //创建流程实例
                CreateResultDTO createResult = todoTaskDispatchService.create(contextDTO, orgId, projectId, operator, instanceDTO);
                if (createResult == null) {
                    throw new BusinessError("error.wbs.create-instance-failed ");
                } else if (createResult.getErrorDesc() != null) {
                    throw new BusinessError(createResult.getErrorDesc());
                }
                //创建后，回来对四级计划进行更新
                wbsEntryState = setProcessInstanceId(
                    operator,
                    wbsEntityEntry,
                    createResult.getActInst()
                );

                //更新四级计划状态
                if (wbsEntryState != null) {
                    // 若根据工序的流程实例信息判断，如果该任务已经启动，标记为【进行中】，否则将其标记为【待启动】
                    if (LongUtils.isEmpty(wbsEntryState.getProcessInstanceId())) {
                        // 但是，下料和配送类型的工序除外（因为在缓冲池，本身暂时没有流程实例）
                        if ((bpmProcess != null && ProcessType.DELIVERY_LIST.equals(bpmProcess.getProcessType()))
                            || (bpmProcess != null && ProcessType.CUT_LIST.equals(bpmProcess.getProcessType()))) {
                            wbsEntryState.setStartedAt(new Date());
                            wbsEntryState.setRunningStatus(WBSEntryRunningStatus.RUNNING);
                            started = true;
                        } else {
                            wbsEntryState.setRunningStatus(WBSEntryRunningStatus.PENDING);
                            started = false;
                        }
                    } else {
                        wbsEntryState.setStartedAt(new Date());
                        wbsEntryState.setRunningStatus(WBSEntryRunningStatus.RUNNING);
                        started = true;
                    }

                    wbsEntryStateRepository.save(wbsEntryState);
                }
            } catch (ValidationError e) {
                // TODO check error code
                throw e;
            }
        }

        return started;
    }

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
    @Override
    @Transactional
    public void dispatch(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long wbsEntryId,
        Organization team,
        WorkSite workSite
    ) {

        WBSEntryState wbsEntryState = wbsEntryStateRepository
            .findByWbsEntryId(
                wbsEntryId
            );

        if (wbsEntryState == null) {
            throw new NotFoundError();
        }

        if (team != null) {
            wbsEntryState.setTeamId(team.getId());
            wbsEntryState.setTeamPath(team.getPath());
            wbsEntryState.setTeamName(team.getName());
        } else {
            wbsEntryState.setTeamId(null);
            wbsEntryState.setTeamPath(null);
            wbsEntryState.setTeamName(null);
        }

        if (workSite != null) {
            wbsEntryState.setWorkSiteId(workSite.getId());
            wbsEntryState.setWorkSiteName(workSite.getName());
        } else {
            wbsEntryState.setWorkSiteId(null);
            wbsEntryState.setWorkSiteName(null);
        }

        wbsEntryState.setLastModifiedBy(operator.getId());
        wbsEntryState.setLastModifiedAt(new Date());

        wbsEntryStateRepository.save(wbsEntryState);

        Long teamId = team == null ? null : team.getId();
        Long workSiteId = workSite == null ? null : workSite.getId();
        String teamName = team == null ? null : team.getName();
        String teamPath = team == null ? null : team.getPath();
        String workSiteName = workSite == null ? null : workSite.getName();
        wbsEntryStateRepository.updateTeamAndSite(wbsEntryState.getId(),
            teamId,
            teamPath,
            teamName,
            workSiteId,
            workSiteName
        );

//        if (team != null) {
//            wbsEntryRepository.updateTeam(
//                wbsEntry.getId(),
//                team.getId(),
//                team.getPath(),
//                team.getName()
//            );
//        }
//
//        if (workSite != null) {
//            wbsEntryRepository.updateWorkSite(
//                wbsEntry.getId(),
//                workSite.getId(),
//                workSite.getName()
//            );
//        }

    }

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
    @Override
    @Transactional
    public void dispatch(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long wbsEntryId,
        Long teamId,
        Long userId,
        Long delegateId,
        UserPrivilege privilege
    ) {

        WBSEntry wbsEntry = wbsEntryRepository
            .findByProjectIdAndIdAndDeletedIsFalse(projectId, wbsEntryId)
            .orElse(null);

        if (wbsEntry == null) {
            throw new NotFoundError();
        }

        WBSEntryDelegate wbsEntryDelegate = null;

        if (delegateId != null) {
            wbsEntryDelegate = wbsEntryDelegateRepository
                .findByWbsEntryIdAndPrivilegeAndDeletedIsFalse(wbsEntryId, privilege);
        }

        // 更新WBS任务分配记录
        if (wbsEntryDelegate != null) {

            if (teamId != null) {
                wbsEntryDelegate.setTeamId(teamId);
            }

            if (userId != null) {
                wbsEntryDelegate.setUserId(userId);
            }

            Date now = new Date();

            wbsEntryDelegate.setLastModifiedAt(now);
            wbsEntryDelegate.setLastModifiedBy(operatorId);

            // 创建
        } else {

            if (privilege == null) {
                throw new BusinessError("privilege is required");
            }

            if (wbsEntryDelegateRepository
                .findByWbsEntryIdAndPrivilegeAndDeletedIsFalse(wbsEntryId, privilege) != null) {
                throw new BusinessError("wbs_entry privilege already dispatch user");
            }

            wbsEntryDelegate = new WBSEntryDelegate();

            wbsEntryDelegate.setWbsEntryId(wbsEntryId);

            if (teamId != null) {
                wbsEntryDelegate.setTeamId(teamId);
            }

            wbsEntryDelegate.setUserId(userId);
            wbsEntryDelegate.setPrivilege(privilege);

            Date now = new Date();
            wbsEntryDelegate.setCreatedAt(now);
            wbsEntryDelegate.setCreatedBy(operatorId);
            wbsEntryDelegate.setLastModifiedAt(now);
            wbsEntryDelegate.setLastModifiedBy(operatorId);

            wbsEntryDelegate.setStatus(EntityStatus.ACTIVE);
        }

        wbsEntryDelegateRepository.save(wbsEntryDelegate);
    }

    /**
     * 设置工作流实例信息。
     *
     * @param operator        操作者信息
     * @param wbsEntityEntry  实体工序计划条目
     * @param processInstance 工作流实例
     */
    private WBSEntryState setProcessInstanceId(
        final OperatorDTO operator,
        final WBSEntry wbsEntityEntry,
        final BpmActivityInstanceBase processInstance
    ) {
        if (wbsEntityEntry == null
            || processInstance == null) {
            return null;
        }
        WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntityEntry.getId());
        if (wbsEntryState == null) return null;

        wbsEntryState.setStartedBy(operator.getId());
        wbsEntryState.setStartedAt(processInstance.getStartDate());
//        wbsEntityEntry.setProcessId(processInstance.getProcessId());
        wbsEntryState.setProcessInstanceId(processInstance.getId());
//        wbsEntryStateRepository.save(wbsEntryState);

        WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntityEntry.getId());
        if (wbsEntryBlob == null) return wbsEntryState;
        Set<Long> parentIDs = wbsEntryBlob.getParentIDs();

        if (parentIDs == null || parentIDs.size() == 0) {
            return wbsEntryState;
        }

        List<WBSEntryState> workWbsEntryStates = wbsEntryStateRepository.
            findByProjectIdAndWbsEntryIdIn(wbsEntityEntry.getProjectId(), parentIDs);
        workWbsEntryStates.forEach(wwState -> {
            if (wwState.getStartedAt() == null) {
                wwState.setStartedAt(processInstance.getStartDate());
                wwState.setStartedBy(operator.getId());
                wbsEntryStateRepository.save(wwState);
            }
        });
        return wbsEntryState;
//        wbsEntryRepository.setStartedAt(
//            wbsEntityEntry.getProjectId(),
//            parentIDs,
//            operator.getId(),
//            processInstance.getStartDate()
//        );
    }

    @Override
    public Page<PlanQueueDTO> getQueue(Long projectId, PageDTO pageDTO) {

        String key = String.format(RedisKey.PLAN_QUEUE.getDisplayName(), projectId.toString());
        List<String> totalQueue = lrange(key, 0, -1);

        List<String> queues;
        // 判断分页数*页面个数 是否大于 记录总数
        if ((pageDTO.getPage().getNo() * pageDTO.getPage().getSize()) > totalQueue.size()) {
            queues = lrange(
                key,
                (pageDTO.getPage().getNo() - 1) * pageDTO.getPage().getSize(),
                -1
            );
        } else {
            queues = lrange(
                key,
                (pageDTO.getPage().getNo() - 1) * pageDTO.getPage().getSize(),
                (pageDTO.getPage().getNo() - 1) * pageDTO.getPage().getSize() + pageDTO.getPage().getSize() - 1
            );
        }
        List<PlanQueueDTO> planQueueDTOS = new ArrayList<>();
        for (String queue : queues) {
            EventModel eventModel = StringUtils.decode(queue, new TypeReference<EventModel>() {
            });
            if (eventModel == null) continue;
            PlanQueueDTO planQueueDTO = new PlanQueueDTO();
            planQueueDTO.setEntityId(eventModel.getEntityId());
            planQueueDTO.setEntityType(eventModel.getEntityType());
            planQueueDTO.setOperatorId(eventModel.getOperatorId());
            planQueueDTO.setTaskDefKey(eventModel.getTaskDefKey());
            planQueueDTO.setProjectId(eventModel.getProjectId());
            planQueueDTO.setAuthorization(eventModel.getAuthorization());
            planQueueDTO.setUserAgent(eventModel.getUserAgent());
            planQueueDTOS.add(planQueueDTO);
        }

        return new PageImpl<>(planQueueDTOS, pageDTO.toPageable(), totalQueue.size());
    }

    @Override
    public void pushAddedEntityQueue(ContextDTO context, Long projectId, Long wbsEntryId) {
        String key = String.format(RedisKey.PLAN_QUEUE.getDisplayName(), projectId.toString());

        List<String> queues = lrange(key, 0, -1);
        if (queues != null) {
            for (String queueStr : queues) {
                EventModel queue = StringUtils.decode(queueStr, new TypeReference<EventModel>() {
                });
                if (queue == null) {
                    continue;
                }
                if (wbsEntryId.toString().equals(queue.getEntityId())) {
                    return;
                }
            }
        }

        WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntryId);


        String authorization = context.getAuthorization();
        String userAgent = context.getUserAgent();
        String operatorId = context.getOperator().getId().toString();

        EventModel eventModel = new EventModel();
        if (wbsEntryBlob != null) {
            eventModel.setEntityType(wbsEntryBlob.getWbs());
        }
        eventModel.setType(EventType.PLAN_QUEUE.name());
        eventModel.setProjectId(projectId);
        eventModel.setEntityId(wbsEntryId);
        eventModel.setAuthorization(authorization);
        eventModel.setUserAgent(userAgent);
        eventModel.setOperatorId(LongUtils.parseLong(operatorId));
        eventModel.setTaskDefKey("ADDED");
        lpush(key, StringUtils.toJSON(eventModel));
    }

    @Override
    public void pushQueue(ContextDTO context, Long projectId, Long wbsEntryId) {

        String key = String.format(RedisKey.PLAN_QUEUE.getDisplayName(), projectId.toString());

        List<String> queues = lrange(key, 0, -1);
        if (queues != null) {
            for (String queueStr : queues) {
                EventModel queue = StringUtils.decode(queueStr, new TypeReference<EventModel>() {
                });
                if (queue == null) {
                    continue;
                }
                if (wbsEntryId.toString().equals(queue.getEntityId())) {
                    return;
                }
            }
        }

        WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntryId);


        String authorization = context.getAuthorization();
        String userAgent = context.getUserAgent();
        String operatorId = context.getOperator().getId().toString();

        EventModel eventModel = new EventModel();
        if (wbsEntryBlob != null) {
            eventModel.setEntityType(wbsEntryBlob.getWbs());
        }
        eventModel.setType(EventType.PLAN_QUEUE.name());
        eventModel.setProjectId(projectId);
        eventModel.setEntityId(wbsEntryId);
        eventModel.setAuthorization(authorization);
        eventModel.setUserAgent(userAgent);
        eventModel.setOperatorId(LongUtils.parseLong(operatorId));
        lpush(key, StringUtils.toJSON(eventModel));
    }

    @Override
    public void resetPlanQueue(Long orgId, Long projectId) {

//        Boolean existPlan = batchTaskRepository.existsByProjectIdAndCodeAndRunning(projectId, BatchTaskCode.ENTITY_PROCESS_WBS_GENERATE, true);
        String statusKey = String.format(RedisKey.PLAN_QUEUE_STATUS.getDisplayName(), projectId.toString());
        Boolean existPlan = batchTaskRepository.existsByProjectIdAndCodeAndRunning(projectId, BatchTaskCode.ENTITY_PROCESS_WBS_GENERATE, true);
        if (existPlan)
            setRedisKey(statusKey, "HOT");
        else setRedisKey(statusKey, "COLD");
        String key = String.format(RedisKey.PLAN_QUEUE.getDisplayName(), projectId.toString());
        deleteRedisKey(key);
    }

}
