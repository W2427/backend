package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.taskpackage.*;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.AsyncPlanInterface;
import com.ose.tasks.domain.model.service.bpm.TodoIndividualTaskInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseService;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskExecInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.WBSEntryTeamWorkSiteDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.HierarchyNodeRelation;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.taskpackage.*;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 建造 工序节点生成LOG 的代理服务。
 */
@Component
public class ConstructionMonoDelegate extends ConstructionDelegate implements BaseBpmTaskInterfaceDelegate {


    private final static Set<String> NDTS =
        new HashSet<String>(){{
        add("RT");
        add("UT");
        add("MT");
        add("PT");
        add("UT_MT");
    }};

    private final TodoTaskBaseService todoTaskBaseService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final TaskExecInterface taskExecService;

    private final TodoIndividualTaskInterface todoIndividualTaskService;

    private final TaskPackageCategoryProcessRelationRepository taskPackageCategoryProcessRelationRepository;

    private final TaskPackageCategoryRepository taskPackageCategoryRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private final TaskPackageEntityRelationRepository taskPackageEntityRelationRepository;

    private final TaskPackageBasicRepository taskPackageBasicRepository;

    private final TaskPackageAssignSiteTeamsRepository assignSiteTeamsRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final WBSEntryRepository wbsEntryRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public ConstructionMonoDelegate(
        TodoTaskBaseService todoTaskBaseService,
        TaskRuleCheckService taskRuleCheckService,
        BpmActivityInstanceRepository bpmActInstRepository,
        ProjectNodeRepository projectNodeRepository,
        AsyncPlanInterface asyncPlanService,
        TodoIndividualTaskInterface todoIndividualTaskService,
        ApplicationEventPublisher applicationEventPublisher,
        BpmRuTaskRepository ruTaskRepository,
        TaskExecInterface taskExecService,
        TaskPackageCategoryProcessRelationRepository taskPackageCategoryProcessRelationRepository,
        TaskPackageCategoryRepository taskPackageCategoryRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        TaskPackageEntityRelationRepository taskPackageEntityRelationRepository,
        TaskPackageBasicRepository taskPackageBasicRepository,
        TaskPackageAssignSiteTeamsRepository assignSiteTeamsRepository,
        StringRedisTemplate stringRedisTemplate,
        WBSEntryStateRepository wbsEntryStateRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        WBSEntryRepository wbsEntryRepository) {
        super(
            todoTaskBaseService,
            taskRuleCheckService,
            bpmActInstRepository,
            asyncPlanService,
            todoIndividualTaskService,
            applicationEventPublisher,
            stringRedisTemplate,
            bpmActivityInstanceStateRepository,
            ruTaskRepository);
        this.todoTaskBaseService = todoTaskBaseService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.bpmActInstRepository = bpmActInstRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.taskExecService = taskExecService;
        this.todoIndividualTaskService = todoIndividualTaskService;
        this.taskPackageCategoryProcessRelationRepository = taskPackageCategoryProcessRelationRepository;
        this.taskPackageCategoryRepository = taskPackageCategoryRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.taskPackageEntityRelationRepository = taskPackageEntityRelationRepository;
        this.taskPackageBasicRepository = taskPackageBasicRepository;
        this.assignSiteTeamsRepository = assignSiteTeamsRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.wbsEntryRepository = wbsEntryRepository;
    }


    @Override
    public CreateResultDTO preCreateActInst(CreateResultDTO createResult) {
        Long orgId = createResult.getOrgId();
        Long projectId = createResult.getProjectId();
        ActivityInstanceDTO actInstDTO = createResult.getActInstDTO();
        BpmProcess bpmProcess = createResult.getProcess();
        String process = bpmProcess.getNameEn();
        Long processId = bpmProcess.getId();



        BpmActivityInstanceBase actInst = bpmActInstRepository.
            findFirstByProjectIdAndEntityIdAndProcessIdAndFinishStateAndSuspensionState(
                projectId, actInstDTO.getEntityId(), actInstDTO.getProcessId(), ActInstFinishState.NOT_FINISHED, SuspensionState.ACTIVE);
        if (actInst != null) {
            createResult.setErrorDesc("The instance is created");
            createResult.setCreateResult(false);
            return createResult;
        }
        actInst= createResult.getActInst();
        BpmActivityInstanceState actInstState = createResult.getActInstState();


            ProjectNode pn = projectNodeRepository
                .findByProjectIdAndEntityIdAndStatus(projectId, actInstDTO.getEntityId(), EntityStatus.ACTIVE).orElse(null);

            if (pn == null) {
                createResult.setErrorDesc("The entity is deleted or not exist");
                createResult.setCreateResult(false);
                return createResult;
            }


        if(NDTS.contains(process)) {


            TaskPackageCategoryProcessRelation taskPackageCategoryProcessRelation = taskPackageCategoryProcessRelationRepository
                .findByOrgIdAndProjectIdAndProcessIdAndStatus(
                    orgId,
                    projectId,
                    processId,
                    EntityStatus.ACTIVE);

            if (taskPackageCategoryProcessRelation == null) {
                return createResult;
            }
            Long taskPackageCategoryId = taskPackageCategoryProcessRelation.getCategoryId();
            TaskPackageCategory taskPackageCategory = taskPackageCategoryRepository
                .findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(
                    orgId,
                    projectId,
                    taskPackageCategoryId).orElse(null);

            if(taskPackageCategory == null) {
                createResult.setErrorDesc("There is no task Package Category");
                createResult.setCreateResult(false);
                return createResult;
            }


            String wbsEntityType = taskPackageCategory.getEntityType();


            ProjectNode projectNode = projectNodeRepository.findByProjectIdAndEntityIdAndStatus(
                projectId,
                actInstDTO.getEntityId(),
                EntityStatus.ACTIVE
            ).orElse(null);

            if(projectNode == null) {
                createResult.setErrorDesc("There is no Project Node");
                createResult.setCreateResult(false);
                return createResult;
            }

            HierarchyNodeRelation hierarchyNodeRelation = hierarchyNodeRelationRepository.
                findFirstByOrgIdAndProjectIdAndNodeIdAndAncestorEntityTypeOrderByDepthAsc(
                    orgId,
                    projectId,
                    projectNode.getId(),
                    wbsEntityType
                );


            if (hierarchyNodeRelation== null || hierarchyNodeRelation.getAncestorEntityId() == null) {
                return createResult;
            }

            Long parentEntityId = hierarchyNodeRelation.getAncestorEntityId();


            TaskPackageEntityRelation taskPackageEntityRelation = taskPackageEntityRelationRepository
                .findByProjectIdAndEntityIdAndTaskPackageCatogoryId(
                    projectId,
                    parentEntityId,
                    taskPackageCategoryId);

            if (taskPackageEntityRelation == null) {
                return createResult;
            }

            actInstState.setTaskPackageId(taskPackageEntityRelation.getTaskPackageId());
            TaskPackageBasic taskPackageBasic = taskPackageBasicRepository.findById(taskPackageEntityRelation.getTaskPackageId()).orElse(null);
            if (taskPackageBasic != null) {
                actInstState.setTaskPackageName(taskPackageBasic.getName());
            }


            TaskPackageAssignSiteTeams taskPackageAssignSiteTeams = assignSiteTeamsRepository
                .findByProjectIdAndTaskPackageIdAndProcessId(projectId,
                    taskPackageEntityRelation.getTaskPackageId(),
                    processId);
            if (taskPackageAssignSiteTeams != null){
                actInstState.setTeamId(taskPackageAssignSiteTeams.getTeamId());
                actInstState.setTeamName(taskPackageAssignSiteTeams.getTeamName());
                Long workSiteId = taskPackageAssignSiteTeams.getWorkSiteId();
                actInstState.setWorkSiteId(workSiteId);
                actInstState.setWorkSiteName(taskPackageAssignSiteTeams.getWorkSiteName());
                actInstState.setWorkSiteAddress(taskPackageAssignSiteTeams.getWorkSiteAddress());
            }
            bpmActivityInstanceStateRepository.save(actInstState);
        } else {

            WBSEntryState wbsEntryState = wbsEntryStateRepository.findByProjectIdAndEntityIdAndStageAndProcessAndDeletedIsFalse(
                projectId, actInstDTO.getEntityId(), actInstDTO.getProcessStage(), actInstDTO.getProcess()).orElse(null);

            if (wbsEntryState != null) {

                WBSEntryTeamWorkSiteDTO teamWorkSiteDTO = todoTaskBaseService.getWBSEntryTeamWorkSiteInfo(orgId, projectId, null, wbsEntryState, actInst.getProcessId());
                actInstState.setWorkSiteId(teamWorkSiteDTO.getWorkSiteId());
                actInstState.setWorkSiteName(teamWorkSiteDTO.getWorkSiteName());
                actInstState.setWorkSiteAddress(teamWorkSiteDTO.getWorkSiteAddress());
                actInstState.setTeamId(teamWorkSiteDTO.getTeamId());
                actInstState.setTeamName(teamWorkSiteDTO.getTeamName());

                actInstState.setTaskPackageId(wbsEntryState.getTaskPackageId());
                Long taskPackageId = wbsEntryState.getTaskPackageId();
                if (taskPackageId != null) {
                    TaskPackageBasic taskPackageBasic = taskPackageBasicRepository.findById(taskPackageId).orElse(null);
                    if (taskPackageBasic != null) {
                        actInstState.setTaskPackageName(taskPackageBasic.getName());
                    }
                }

                bpmActivityInstanceStateRepository.save(actInstState);
            }
        }

        return createResult;
    }

    @Override
    public void prepareExecute(ContextDTO contextDTO, Map<String, Object> data,
                               BpmActivityInstanceBase actInst, BpmRuTask ruTask, TodoTaskDTO todoDTO) {
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
//        WeldHistory weldHis = todoIndividualTaskService.getWeldHis(actInst.getEntityId());
//        if (weldHis != null)
//            todoDTO.setWeldCount(weldHis.getWeldCount());
//        todoDTO.setWeldEntity(todoIndividualTaskService.getWeldEntity(orgId, projectId, actInst));
//
        if (taskRuleCheckService.isWeldExecuteTaskNode(ruTask.getTaskType())
            || taskRuleCheckService.isFormanAssignWorkerTaskNode(ruTask.getTaskType())) {
            TaskMaterialDTO materialDTO = todoTaskBaseService.getMaterial(ruTask.getActInstId());
            todoDTO.setMaterialDTO(materialDTO);
        }

    }


    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        BpmProcess bpmProcess = execResult.getBpmProcess();
        Long projectId = bpmProcess.getProjectId();
        Long orgId = bpmProcess.getOrgId();
        BpmRuTask ruTask = execResult.getRuTask();

        Long taskId = ruTask.getId();
        String taskDefKey = ruTask.getTaskDefKey();
        String taskType = ruTask.getTaskType();
        Long entityId = execResult.getActInst().getEntityId();
        ActInstSuspendDTO dto = new ActInstSuspendDTO();
        dto.setMemo("ENTITY IS NOT EXIST or DELETED");
        dto.setTaskType(taskType);
        dto.setTaskDefKey(taskDefKey);
        ProcessType processType = bpmProcess.getProcessType();
        if(ProcessType.MONO_CONSTRUCT.equals(processType)) {
            ProjectNode pn = projectNodeRepository
                .findByProjectIdAndEntityIdAndStatus(projectId, entityId, EntityStatus.ACTIVE).orElse(null);

            if (pn == null) {
                taskExecService.suspendTask(contextDTO, orgId, projectId, taskId, dto);
                execResult.setErrorDesc("The entity is deleted or not exist");
                execResult.setExecResult(false);
                return execResult;
            }
        }

        return execResult;
    }

}
