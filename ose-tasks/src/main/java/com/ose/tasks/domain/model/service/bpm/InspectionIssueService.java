package com.ose.tasks.domain.model.service.bpm;

import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ValidationError;
import com.ose.issues.api.IssueFeignAPI;
import com.ose.issues.dto.IssueCreateDTO;
import com.ose.issues.dto.IssueCriteriaDTO;
import com.ose.issues.dto.IssueUpdateDTO;
import com.ose.issues.entity.Issue;
import com.ose.issues.vo.IssueSource;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.HierarchyNodeDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 用户服务。
 */
@Component
public class InspectionIssueService implements InspectionIssueInterface {

    private final HierarchyInterface hierarchyService;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmProcessRepository processRepository;

    private final ProjectInterface projectService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final IssueFeignAPI issueFeignAPI;

    private final ActivityTaskInterface activityTaskService;

    private final UserFeignAPI userFeignAPI;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmActivityInstanceBlobRepository bpmActivityInstanceBlobRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public InspectionIssueService(BpmActivityInstanceRepository bpmActInstRepository,
                                  HierarchyInterface hierarchyService,
                                  BpmProcessRepository processRepository,
                                  ProjectInterface projectService,
                                  TodoTaskBaseInterface todoTaskBaseService,
                                  TodoTaskDispatchInterface todoTaskDispatchService,
                                  TaskRuleCheckService taskRuleCheckService,
                                  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") IssueFeignAPI issueFeignAPI,
                                  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") ActivityTaskInterface activityTaskService,
                                  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
                                  BpmRuTaskRepository ruTaskRepository,
                                  BpmActivityInstanceBlobRepository bpmActivityInstanceBlobRepository,
                                  BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository) {
        this.bpmActInstRepository = bpmActInstRepository;
        this.hierarchyService = hierarchyService;
        this.processRepository = processRepository;
        this.projectService = projectService;
        this.todoTaskBaseService = todoTaskBaseService;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.issueFeignAPI = issueFeignAPI;
        this.activityTaskService = activityTaskService;
        this.userFeignAPI = userFeignAPI;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmActivityInstanceBlobRepository = bpmActivityInstanceBlobRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
    }

    @Override
    public void addInternalInspectionIssue(
        Long orgId, Long projectId,
        InternalInspectionIssueDTO dto,
        OperatorDTO operatorDTO
    ) {

        List<Long> processInstanceIDs = dto.getActInstIds();

        if (processInstanceIDs == null || processInstanceIDs.isEmpty()) {
            return;
        }

        List<BpmActivityInstanceBase> activityInstances = bpmActInstRepository
            .findByProjectIdAndIdInAndStatus(projectId, processInstanceIDs.toArray(new Long[0]),EntityStatus.ACTIVE);

        if (activityInstances.isEmpty()) {
            throw new BusinessError("指定的任务不存在");
        }

        Set<String> entities = new TreeSet<>();
        Set<Long> entityIdList = new TreeSet<>();
        Set<String> areaList = new TreeSet<>();
        Set<String> cleanPackageList = new TreeSet<>();
        Set<String> layerList = new TreeSet<>();
        Set<String> pressureTestPackageList = new TreeSet<>();
        Set<String> subSystemList = new TreeSet<>();

        for (BpmActivityInstanceBase activityInstance : activityInstances) {
            entities.add(activityInstance.getEntityNo());
            entityIdList.add(activityInstance.getEntityId());


            try {
                HierarchyNodeDTO areaParent = hierarchyService
                    .getHierarchyNodeByEntityIdAndHierarchyType(orgId, projectId, activityInstance.getEntityId(), "PIPING");
                if (areaParent != null && areaParent.getNode() != null) {
                    areaList.add(areaParent.getNode().getNo());
                }

                HierarchyNodeDTO cleanPackageParent = hierarchyService.getHierarchyNodeByEntityIdAndHierarchyType(
                    orgId,
                    projectId,
                    activityInstance.getEntityId(),
                    "CLEAN_PACKAGE");
                if (cleanPackageParent != null && cleanPackageParent.getNode() != null) {
                    cleanPackageList.add(cleanPackageParent.getNode().getNo());
                }

                HierarchyNodeDTO layerPackageParent = hierarchyService.getHierarchyNodeByEntityIdAndHierarchyType(
                    orgId,
                    projectId,
                    activityInstance.getEntityId(),
                    "PIPING");
                if (layerPackageParent != null && layerPackageParent.getNode() != null) {
                    layerList.add(layerPackageParent.getNode().getNo());
                }

                HierarchyNodeDTO ptpParent = hierarchyService.getHierarchyNodeByEntityIdAndHierarchyType(
                    orgId,
                    projectId,
                    activityInstance.getEntityId(),
                    "PRESSURE_TEST_PACKAGE");
                if (ptpParent != null && ptpParent.getNode() != null) {
                    pressureTestPackageList.add(ptpParent.getNode().getNo());
                }

                HierarchyNodeDTO ssParent = hierarchyService.getHierarchyNodeByEntityIdAndHierarchyType(
                    orgId,
                    projectId,
                    activityInstance.getEntityId(),
                    "SUB_SYSTEM");
                if (ssParent != null && ssParent.getNode() != null) {
                    subSystemList.add(ssParent.getNode().getNo());
                }
            } catch (Exception e) {

            }
        }

        String process = activityInstances.get(0).getProcessStage() + "-" + activityInstances.get(0).getProcess();

        IssueCreateDTO issueCreateDTO = new IssueCreateDTO();
        issueCreateDTO.setProjectName(projectService.get(orgId, projectId).getName());
        issueCreateDTO.setOpinionNo("ISSUE-" + new Date().getTime());
        issueCreateDTO.setAttachment(dto.getAttachment());
        issueCreateDTO.setDescription(dto.getDescription());
        issueCreateDTO.setEntities(String.join(",", entities));
        issueCreateDTO.setLevel(dto.getEntitySubType());
        issueCreateDTO.setLeader(operatorDTO.getId());
        issueCreateDTO.setProcess(process);
        issueCreateDTO.setSource(IssueSource.QC);
        issueCreateDTO.setStatus(EntityStatus.ACTIVE);
        issueCreateDTO.setArea(String.join(",", areaList));
        issueCreateDTO.setCleanPackage(String.join(",", cleanPackageList));
        issueCreateDTO.setLayer(String.join(",", layerList));
        issueCreateDTO.setPressureTestPackage(String.join(",", pressureTestPackageList));
        issueCreateDTO.setSubSystem(String.join(",", subSystemList));


        Issue issue = issueFeignAPI.create(orgId, projectId, issueCreateDTO).getData();

        /*if(issue != null) {
            issueCreateDTO.setEntityIdList(entityIdList);
            this.createIssueEntityies(orgId, projectId, issueCreateDTO, issue);
        }*/

        for (BpmActivityInstanceBase activityInstance : activityInstances) {
            BpmActivityInstanceBlob actInstBlob = bpmActivityInstanceBlobRepository.findByBaiId(activityInstance.getId());
            if(actInstBlob == null) {
                actInstBlob = new BpmActivityInstanceBlob();
                actInstBlob.setOrgId(orgId);
                actInstBlob.setProjectId(projectId);
            }

            List<Long> issueIDs = actInstBlob.getJsonInInsIssueIdsReadOnly();

            if (issueIDs == null) {
                issueIDs = new ArrayList<>();
            }

            issueIDs.add(issue.getId());

            actInstBlob.setJsonInInsIssueIds(issueIDs);
            bpmActivityInstanceBlobRepository.save(actInstBlob);
        }


    }

    @Override
    public List<Issue> internalInspectionIssueList(
        final Long orgId,
        final Long projectId,
        final InspectionIssueCriteriaDTO criteriaDTO
    ) {

        String[] processInstanceStrIDs = criteriaDTO.getActInstIds().split(",");
        Long[] processInstanceIDs = new Long[processInstanceStrIDs.length];
        for(String pi: processInstanceStrIDs) {

        }
        List<Long> issueIDs = new ArrayList<>();
        BpmActivityInstanceBlob actInstBlob;

        for (Long processInstanceId : processInstanceIDs) {
            actInstBlob = bpmActivityInstanceBlobRepository
                .findByProjectIdAndBaiId(projectId, processInstanceId);

            if (actInstBlob == null
                || actInstBlob.getJsonInInsIssueIdsReadOnly() == null
                ) {
                continue;
            }

            issueIDs.addAll(actInstBlob.getJsonInInsIssueIdsReadOnly());
        }

        if (issueIDs.isEmpty()) {
            return null;
        }

        IssueCriteriaDTO issueCriteriaDTO = new IssueCriteriaDTO();
        issueCriteriaDTO.setIssueIDs(issueIDs);

        return issueFeignAPI
            .batchGet(orgId, projectId, issueCriteriaDTO)
            .getData();
    }

    @Override
    public BpmActivityInstanceState createPunchlistTask(
        ContextDTO contextDTO,
        Long orgId,
        Long projectId,
        OperatorDTO operatorDTO,
        IssueCreateTaskDTO dto
    ) {

        List<BpmProcess> processes = processRepository.findByOrgIdAndProjectIdAndNameEnAndStatus(orgId, projectId, BpmCode.PUNCHLIST, EntityStatus.ACTIVE);
        if (processes.isEmpty()) {
            throw new ValidationError("please deploy the process PUNCHLIST");
        }
        BpmProcess process = processes.get(0);
        List<BpmEntitySubType> l = process.getEntitySubTypes();


        BpmActivityInstanceBase actInstDB = todoTaskBaseService.getBpmActivityInstanceByEntityId(orgId, projectId, dto.getEntityId());
        if (actInstDB != null) {
            throw new ValidationError("有正在运行的流程实例");
        }


        Issue issue = issueFeignAPI.get(orgId, projectId, dto.getEntityId()).getData();
        if (issue.getPersonInChargeId() == null) {
            throw new ValidationError("请指定遗留问题责任人");
        }

        ActivityInstanceDTO taskDTO = new ActivityInstanceDTO();
        taskDTO.setEntityId(dto.getEntityId());
        taskDTO.setEntityNo(dto.getEntityNo());
        taskDTO.setProcessId(process.getId());
        taskDTO.setEntitySubType(l.get(0).getNameEn());
        taskDTO.setEntitySubTypeId(l.get(0).getId());

        String existMaxVersion = bpmActInstRepository.findMaxVersionByProjectIdAndEntityId(
            projectId, dto.getEntityId());
        if (existMaxVersion == null) {
            taskDTO.setVersion("0");
        } else {
            try {
                taskDTO.setVersion("" + (Integer.parseInt(existMaxVersion) + 1));
            } catch (Exception e) {
                e.printStackTrace(System.out);
                taskDTO.setVersion(existMaxVersion + 1);
            }
        }


        CreateResultDTO createResult = todoTaskDispatchService.create(contextDTO, orgId, projectId, operatorDTO, taskDTO);
        BpmActivityInstanceState actInst = createResult.getActInstState();

        UserProfile userProfile = userFeignAPI.get(issue.getPersonInChargeId()).getData();
        Long userId = userProfile.getId();
        String username = userProfile.getName();

        List<BpmActTaskAssignee> assignees = activityTaskService.findActTaskAssigneesByActInstId(actInst.getId());
        List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInst.getId());
        BpmActTaskAssignee assignee = null;
        if(CollectionUtils.isEmpty(ruTasks)) {
            BpmRuTask ruTask = ruTasks.get(0);
            for (BpmActTaskAssignee bpmActTaskAssignee : assignees) {
                if (taskRuleCheckService.isPunchlistChargerAssignTaskNode(ruTask.getTaskType())) {
                    assignee = bpmActTaskAssignee;
                    break;
                }
            }
        }
        if (assignee != null) {


            activityTaskService.modifyTaskAssignee(assignee.getId(), userId, username);//, operatorDTO);
            activityTaskService.assignee(actInst.getId(), assignee.getTaskDefKey(),
                assignee.getTaskName(), userId, operatorDTO.getId().toString());

            todoTaskBaseService.modifyTaskAssigneeByCategory(
                actInst.getId(),
                assignee.getTaskCategory(),
                userId,
                username,
                assignee.getTeamId()
            );

            actInst.setCurrentExecutor(username);
            bpmActivityInstanceStateRepository.save(actInst);
        }


        IssueUpdateDTO issueUpdateDTO = new IssueUpdateDTO();
        BeanUtils.copyProperties(issue, issueUpdateDTO, "no", "attachment", "status", "properties");
        issueUpdateDTO.setStatus(EntityStatus.ACTIVE);
        issueFeignAPI.update(orgId, projectId, issue.getId(), issueUpdateDTO);

        return actInst;
    }

    @Override
    public BpmActivityInstanceDTO searchPunchlistTask(
        Long orgId,
        Long projectId,
        Long id
    ) {
        List<BpmActivityInstanceBase> list = bpmActInstRepository.findByProjectIdAndEntityId(projectId, id);
        if (list.isEmpty()) {
            return null;
        }
        BpmActivityInstanceBase actInst = list.get(0);
        BpmActivityInstanceDTO actInstDTO = BeanUtils.copyProperties(actInst, new BpmActivityInstanceDTO());
        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
        BpmActivityInstanceBlob actInstBlob = bpmActivityInstanceBlobRepository.findByBaiId(actInst.getId());
        if(actInstState != null) {
            actInstDTO = BeanUtils.copyProperties(actInstState, actInstDTO);
        }
        if(actInstBlob != null) {
            actInstDTO = BeanUtils.copyProperties(actInstBlob, actInstDTO);
        }
        return actInstDTO;
    }

}
