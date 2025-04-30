package com.ose.tasks.domain.model.service.bpm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.entity.UserBasic;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.domain.model.repository.drawing.DrawingRecordRepository;
import com.ose.tasks.entity.drawing.DrawingRecord;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.Organization;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.HierarchyRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.process.ProcessBpmnRelationRepository;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.repository.qc.ExInspDetailActInstRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.ModuleProcessService;
import com.ose.tasks.domain.model.service.ProjectNodeInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.domain.model.service.worksite.WorkSiteInterface;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.bpm.ActInstTeamWorkSiteDTO.TeamDTO;
import com.ose.tasks.dto.bpm.ActInstTeamWorkSiteDTO.WorkSiteDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.ProcessBpmnRelation;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.entity.worksite.WorkSite;
import com.ose.tasks.util.BPMNUtils;
import com.ose.tasks.vo.SuspensionState;
import com.ose.vo.BpmTaskDefKey;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务。
 */
@Component
//@Transactional
public class ActivityTaskService extends StringRedisService implements ActivityTaskInterface {

    // 受保护文件路径
    @Value("${application.files.protected}")
    private String protectedDir;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private final BpmActTaskRepository bpmActTaskRepository;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final BpmProcessRepository processRepository;

    private final BpmEntitySubTypeRepository entitySubTypeRepository;

    private final BpmProcessStageRepository processStageRepository;

    private final BpmEntityTypeRepository entityTypeRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmActivityInstanceShiftRepository bpmActivityInstanceShiftRepository;

    private final BpmReDeploymentRepository bpmReDeploymentRepository;

    private final BpmActInstVariableConfigRepository bpmActInstVariableRepository;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final BpmActInstVariableValueRepository bpmActInstVariableValueRepository;

    private final HierarchyRepository hierarchyRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final BpmProcessCategoryRepository bpmProcessCategoryRepository;

    private final BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final TaskRuleCheckService taskRuleCheckService;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final ProjectNodeInterface projectNodeService;

    private final OrganizationFeignAPI organizationFeignAPI;

    private final WorkSiteInterface workSiteService;

    private final UserFeignAPI userFeignAPI;

    private final DrawingRepository drawingRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final ExInspDetailActInstRelationRepository exInspDetailActInstRelationRepository;

    private final ProcessInterface processService;

    private final ProcessBpmnRelationRepository processBpmnRelationRepository;

    private final ProcessStageInterface processStageService;

    private final DrawingDetailRepository drawingDetailRepository;

    private final DrawingRecordRepository drawingRecordRepository;

    private final WBSEntryRepository wbsEntryRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final BpmActivityInstanceLogRepository bpmActivityInstanceLogRepository;

    private final static Logger logger = LoggerFactory.getLogger(ModuleProcessService.class);

    /**
     * 构造方法。
     */
    @Autowired
    public ActivityTaskService(BpmActivityInstanceRepository bpmActInstRepository,
                               BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository,
                               BpmActTaskRepository bpmActTaskRepository,
                               BpmEntityDocsMaterialsRepository docsMaterialsRepository,
                               BpmProcessRepository processRepository,
                               BpmEntitySubTypeRepository entitySubTypeRepository,
                               BpmProcessStageRepository processStageRepository,
                               BpmEntityTypeRepository entityTypeRepository,
                               BpmRuTaskRepository ruTaskRepository,
                               BpmReDeploymentRepository bpmReDeploymentRepository,
                               BpmActInstVariableConfigRepository bpmActInstVariableRepository,
                               BpmHiTaskinstRepository hiTaskinstRepository,
                               BpmActInstVariableValueRepository bpmActInstVariableValueRepository,
                               HierarchyRepository hierarchyRepository,
                               ProjectNodeRepository projectNodeRepository,
                               BpmProcessCategoryRepository bpmProcessCategoryRepository,
                               BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository,
                               BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                               TaskRuleCheckService taskRuleCheckService,
                               TodoTaskBaseInterface todoTaskBaseService,
                               ProjectNodeInterface projectNodeService,
                               StringRedisTemplate stringRedisTemplate,
                               @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") OrganizationFeignAPI organizationFeignAPI,
                               WorkSiteInterface workSiteService,
                               @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
                               DrawingRepository drawingRepository,
                               ExInspActInstRelationRepository exInspActInstRelationRepository,
                               ExInspDetailActInstRelationRepository exInspDetailActInstRelationRepository,
                               ProcessInterface processService,
                               ProcessBpmnRelationRepository processBpmnRelationRepository,
                               ProcessStageInterface processStageService,
                               BpmActivityInstanceShiftRepository bpmActivityInstanceShiftRepository,
                               DrawingDetailRepository drawingDetailRepository,
                               WBSEntryRepository wbsEntryRepository,
                               DrawingRecordRepository drawingRecordRepository,
                               WBSEntryStateRepository wbsEntryStateRepository,
                               BpmActivityInstanceLogRepository bpmActivityInstanceLogRepository) {
        super(stringRedisTemplate);
        this.bpmActInstRepository = bpmActInstRepository;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
        this.bpmActTaskRepository = bpmActTaskRepository;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.processRepository = processRepository;
        this.entitySubTypeRepository = entitySubTypeRepository;
        this.processStageRepository = processStageRepository;
        this.entityTypeRepository = entityTypeRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmReDeploymentRepository = bpmReDeploymentRepository;
        this.bpmActInstVariableRepository = bpmActInstVariableRepository;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.bpmActInstVariableValueRepository = bpmActInstVariableValueRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.bpmProcessCategoryRepository = bpmProcessCategoryRepository;
        this.bpmDrawingSignTaskRepository = bpmDrawingSignTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.taskRuleCheckService = taskRuleCheckService;
        this.todoTaskBaseService = todoTaskBaseService;
        this.projectNodeService = projectNodeService;
        this.organizationFeignAPI = organizationFeignAPI;
        this.workSiteService = workSiteService;
        this.userFeignAPI = userFeignAPI;
        this.drawingRepository = drawingRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.exInspDetailActInstRelationRepository = exInspDetailActInstRelationRepository;
        this.processService = processService;
        this.processBpmnRelationRepository = processBpmnRelationRepository;
        this.processStageService = processStageService;
        this.bpmActivityInstanceShiftRepository = bpmActivityInstanceShiftRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.drawingRecordRepository = drawingRecordRepository;
        this.bpmActivityInstanceLogRepository = bpmActivityInstanceLogRepository;
    }

    /**
     * 保存流程实例
     */
    @Override
    public BpmActivityInstanceBase saveActInst(BpmActivityInstanceBase actInst) {
        Optional<BpmProcess> opBpmProcess = processRepository.findById(actInst.getProcessId());
        if (opBpmProcess.isPresent()) {
            BpmProcessCategory category = opBpmProcess.get().getProcessCategory();
            if (category != null) {
                actInst.setActCategory(category.getNameEn());
                actInst.setProcessCategoryId(category.getId());
            }
        }
        return bpmActInstRepository.save(actInst);
    }

    /**
     * 创建任务分配记录
     */
    @Override
    public BpmActTaskAssignee saveActTaskAssignee(BpmActTaskAssignee assignee) {
        return bpmActTaskAssigneeRepository.save(assignee);
    }

    /**
     * 查询任务流程实例列表
     */
    @Override
    public Page<BpmActivityInstanceDTO> actInstList(Long orgId, Long projectId, ActInstCriteriaDTO criteria) {
        Page<BpmActivityInstanceDTO> list = bpmActInstRepository.actInstList(orgId, projectId, criteria);
        for (BpmActivityInstanceDTO actInst : list.getContent()) {
            List<BpmRuTask> tasks = ruTaskRepository.findByActInstId(actInst.getId());
            List<String> taskNames = new ArrayList<>();
            for (BpmRuTask task : tasks) {
                if (!taskNames.contains(task.getName())) {
                    taskNames.add(task.getName());
                    actInst.setCurrentTaskNodeDate(task.getCreateTime());
                }
            }
            actInst.setCurrentTaskNode(String.join(",", taskNames));
        }

        return list;
    }

    /**
     * 根据id查找任务流程实例
     */
    @Override
    public BpmActivityInstanceBase findActInstById(Long id) {
        Optional<BpmActivityInstanceBase> op = bpmActInstRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    /**
     * 根据id查找任务流程实例
     */
    @Override
    public BpmActivityInstanceBase findByProjectIdAndActInstId(Long orgId, Long projectId, Long actInstId) {
        Optional<BpmActivityInstanceBase> op = bpmActInstRepository.findByProjectIdAndId(projectId, actInstId);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    /**
     * 根据任务id查找任务记录
     */
    @Override
    public BpmActTask findActTaskByTaskId(String taskId) {
        return bpmActTaskRepository.findByTaskId(LongUtils.parseLong(taskId));
    }

    /**
     * 根据流程实例id获取任务分配记录
     */
    @Override
    public List<BpmActTaskAssignee> findActTaskAssigneesByActInstId(Long actInstId) {
        List<BpmActTaskAssignee> assignees = bpmActTaskAssigneeRepository.findByActInstId(actInstId);
        for (BpmActTaskAssignee assignee : assignees) {
            if (assignee.getTaskCategory() != null) {
                try {
                    assignee.setTaskCategoryName(UserPrivilege.getByName(assignee.getTaskCategory()).getDisplayName());
                } catch (Error e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return assignees;
    }

    /**
     * 修改任务分配记录
     */
    @Override
    public BpmActTaskAssignee modifyTaskAssignee(Long taskAssigneeId, Long userid, String userName) {
        Optional<BpmActTaskAssignee> assigneeOp = bpmActTaskAssigneeRepository.findById(taskAssigneeId);
        if (assigneeOp.isPresent()) {
            BpmActTaskAssignee assignee = assigneeOp.get();
            assignee.setAssignee(userid);
            assignee.setAssigneeName(userName);
            assignee.setLastModifiedAt();
            System.out.println("修改分配记录 ： " + "用户名：" + userName + "用户id：" + userid);
            return bpmActTaskAssigneeRepository.save(assignee);
        }
        return null;
    }

    /**
     * 根据分配id获取任务分配信息
     */
    @Override
    public BpmActTaskAssignee findActTaskAssigneesById(Long taskAssigneeId) {
        Optional<BpmActTaskAssignee> assigneeOp = bpmActTaskAssigneeRepository.findById(taskAssigneeId);
        if (assigneeOp.isPresent()) {
            return assigneeOp.get();
        }
        return null;
    }

    /**
     * 获取任务管理页面层级数据-实体类型
     */
    @Override
    public List<HierarchyBaseDTO> getEntitiyCategoriesInActivity(Long orgId,
                                                                 Long projectId,
                                                                 Long processStageId,
                                                                 Long processId,
                                                                 Long entityTypeId
    ) {
        return bpmActInstRepository.findEntitiyCategoriesInActivity(
            projectId,
            processStageId,
            processId,
            entityTypeId
        );
    }

    /**
     * 获取任务管理页面层级数据-工序阶段
     */
    @Override
    public List<HierarchyBaseDTO> getProcessStagesInActivity(Long orgId, Long projectId) {
        List<HierarchyBaseDTO> hierarchyBaseDTOS = new ArrayList<>();
        Set<Long> processStageIds = bpmActInstRepository.findDistinctProcessStageId(projectId);
        processStageIds.forEach(processStageId -> {
            BpmProcessStage processStage = processStageService.getStage(processStageId, projectId, orgId);
            if (processStage != null) {
                HierarchyBaseDTO hierarchyBaseDTO = new HierarchyBaseDTO();
                hierarchyBaseDTO.setNameEn(processStage.getNameEn());
                hierarchyBaseDTO.setNameCn(processStage.getNameCn());
                hierarchyBaseDTO.setId(processStageId);
                hierarchyBaseDTOS.add(hierarchyBaseDTO);
            }
        });
        return hierarchyBaseDTOS;//bpmActInstRepository.findProcessStagesInActivity(projectId);
    }

    /**
     * 获取任务管理页面层级数据-工序
     */
    @Override
    public List<HierarchyBaseDTO> getProcessesInActivity(
        Long orgId,
        Long projectId,
        HierarchyCriteriaDTO criteriaDTO
    ) {
        return bpmActInstRepository.findProcessesInActivity(
            projectId,
            criteriaDTO.getProcessStageId()
        );
    }

    /**
     * 保存任务材料文档
     */
    @Override
    public BpmEntityDocsMaterials saveDocsMaterials(BpmEntityDocsMaterials bedm) {
        return docsMaterialsRepository.save(bedm);
    }

    /**
     * 根据id获取实体类型
     */
    @Override
    public BpmEntitySubType findEntitySubTypeById(Long id) {
        Optional<BpmEntitySubType> op = entitySubTypeRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    /**
     * 根据id获取工序类型
     */
    @Override
    public BpmProcessStage findProcessStageById(Long id) {
        Optional<BpmProcessStage> op = processStageRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    /**
     * 根据id获取工序
     */
    @Override
    public BpmProcess findProcessById(Long id) {
        Optional<BpmProcess> op = processRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    /**
     * 保存运行任务信息
     */
    @Override
    public BpmRuTask saveBpmRuTask(BpmRuTask bpmRuTask) {
        return ruTaskRepository.save(bpmRuTask);
    }

    /**
     * 修改运行时任务担当人
     */
    @Override
    public boolean assignee(Long actInstId, String taskDefKey, String taskName, Long userid, String assignee) {
        BpmRuTask ruTask = new BpmRuTask();
        if (taskName.equals("CO-SIGN")) {
            ruTask = ruTaskRepository.findByActInstIdAndTaskDefKeyAndNameAndAssignee(actInstId, taskDefKey, taskName, assignee);
        } else {
            ruTask = ruTaskRepository.findByActInstIdAndTaskDefKeyAndName(actInstId, taskDefKey, taskName);
        }

        if (ruTask != null) {
            ruTask.setAssignee(userid.toString());
            ruTaskRepository.save(ruTask);
            System.out.println("修改运行任务人员： " + "用户id：" + userid.toString());
            return true;
        }
        return false;
    }

    /**
     * 获取任务管理页面层级数据-实体类型分类
     */
    @Override
    public List<HierarchyBaseDTO> getEntitiyCategoryTypesInActivity(
        Long orgId,
        Long projectId,
        Long processStageId,
        Long processId
    ) {
        return bpmActInstRepository.findEntitiyCategoryTypesInActivity(
            projectId,
            processStageId,
            processId
        );
    }

    /**
     * 根据id查询实体类型分类
     */
    @Override
    public BpmEntityType findEntityTypeById(Long id) {
        Optional<BpmEntityType> op = entityTypeRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    @Override
    public BpmEntitySubType getEntitiySubTypeById(Long entitySubTypeId) {
        Optional<BpmEntitySubType> op = entitySubTypeRepository.findById(entitySubTypeId);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    /**
     * 根据processName获取流程模型变量
     */
    @Override
    public List<BpmActInstVariableConfig> getActInstVariablesByProcessId(Long orgId, Long projectId,
                                                                         Long processId) {
        BpmReDeployment deployment = bpmReDeploymentRepository.findLastVerisonByProcessId(orgId, projectId,
            processId);
        if (deployment != null) {
            processId = deployment.getProcessId();
            Optional<BpmProcess> optinal = processRepository.findById(processId);
            if (optinal.isPresent()) {
                BpmProcess process = optinal.get();
                String processStageNameEn = process.getProcessStage().getNameEn();
                return bpmActInstVariableRepository.findByProcessKey(processStageNameEn + "-" + process.getNameEn());
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 保存历史人物节点
     */
    @Override
    public BpmHiTaskinst saveBpmHiTaskinst(BpmHiTaskinst hiTask) {
        return hiTaskinstRepository.save(hiTask);
    }

    /**
     * 根据procInstId查询历史任务节点
     */
    @Override
    public List<BpmHiTaskinst> findHiTaskinstByActInstId(Long actInstId) {
        return hiTaskinstRepository.findByActInstIdOrderByStartTimeAsc(actInstId);
    }

    /**
     * 获取当前流程待办任务
     */
    @Override
    public List<BpmRuTask> findBpmRuTaskByActInstId(Long actInstId) {
        return ruTaskRepository.findByActInstId(actInstId);
    }

    /**
     * 删除运行中的任务
     */
    @Override
    public boolean deleteBpmRuTask(Long taskId) {
        BpmRuTask ruTask = ruTaskRepository.findById(taskId).orElse(null);
        if (ruTask != null) {
            ruTaskRepository.delete(ruTask);
            return true;
        }
        return false;
    }

    /**
     * 根据流程定义key查询流程变量
     */
    @Override
    public List<BpmActInstVariableConfig> findActInstVariables(Long orgId, Long projectId, Long processKey) {
        Optional<BpmProcess> optinal = processRepository.findById(processKey);
        if (optinal.isPresent()) {
            BpmProcess process = optinal.get();
            String processStageNameEn = process.getProcessStage().getNameEn();
            return bpmActInstVariableRepository.findByProcessKey(processStageNameEn + "-" + process.getNameEn());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 根据流程实例id及变量名称查询流程值
     */
    @Override
    public BpmActInstVariableValue findBpmActInstVariableValue(Long orgId, Long projectId, Long actInstId,
                                                               String name) {
        return bpmActInstVariableValueRepository.findByOrgIdAndProjectIdAndActInstIdAndVariableName(orgId, projectId,
            actInstId, name);
    }

    //
    @Override
    public List<ProjectNode> getParentEntitiesByEntityId(Long projectId, Long entityId) {
        // TODO Auto-generated method stub
        Optional<ProjectNode> projectNodeOpt = projectNodeRepository
            .findByProjectIdAndEntityIdAndStatus(projectId, entityId, EntityStatus.ACTIVE);
        if (projectNodeOpt.isPresent()) {
            Long projectNodeId = projectNodeOpt.get().getId();
            List<HierarchyNode> nodes = hierarchyRepository.findByNodeIdAndProjectIdAndDeletedIsFalse(projectNodeId,
                projectId);
            List<Long> hierarchyIds = new ArrayList<>();
            for (HierarchyNode node : nodes) {
                String path = node.getPath();
                String[] hierarchyIdArray = path.split("/");// List<String> stringA = Arrays.asList
                if (hierarchyIdArray != null && hierarchyIdArray.length > 0) {
                    Arrays.asList(hierarchyIdArray).forEach(
                        hierarchyId -> {
                            hierarchyIds.add(LongUtils.parseLong(hierarchyId));
                        }
                    );
                }
            }
            if (hierarchyIds.size() > 0) {
                List<HierarchyNode> parentNodes = hierarchyRepository
                    .findByIdInAndDeletedIsFalseOrderBySortAsc(hierarchyIds);
                List<Long> projectNodeIds = new ArrayList<>();
                for (HierarchyNode parent : parentNodes) {
                    projectNodeIds.add(parent.getNode().getId());
                }
                if (projectNodeIds.size() > 0) {
                    Long[] idsss = projectNodeIds.toArray(new Long[0]);
                    return projectNodeRepository.findByIdInAndDeletedIsFalse(idsss);
                }
            }
        }

        return new ArrayList<>();
    }

    @Override
    public boolean deleteActInst(Long actInstId, OperatorDTO operator) {
        BpmActivityInstanceBase actInst = this.findActInstById(actInstId);
        if (actInst != null) {
//            JsonResponseBody response = actTaskFeignAPI.deleted(actInst.getId());
//            if (response.getSuccess()) {
            List<BpmActTaskAssignee> assignees = bpmActTaskAssigneeRepository
                .findByActInstId(actInst.getId());
            bpmActTaskAssigneeRepository.deleteAll(assignees);

            List<BpmActTask> actTasks = bpmActTaskRepository.findByActInstId(actInst.getId());
            bpmActTaskRepository.deleteAll(actTasks);

            List<BpmHiTaskinst> hiTasks = hiTaskinstRepository
                .findByActInstIdOrderByStartTimeAsc(actInst.getId());
            hiTaskinstRepository.deleteAll(hiTasks);

            exInspActInstRelationRepository.deleteByActInstId(actInst.getId());
            exInspDetailActInstRelationRepository.deleteByActInstId(actInst.getId());

            List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInst.getId());
            ruTaskRepository.deleteAll(ruTasks);

            List<BpmActInstVariableValue> values = bpmActInstVariableValueRepository
                .findByActInstId(actInst.getId());
            bpmActInstVariableValueRepository.deleteAll(values);


            List<DrawingDetail> drawingDetails = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(actInst.getEntityId(), actInstId);
            drawingDetailRepository.deleteAll(drawingDetails);

            //修改图纸版本号,恢复到之前的状态
            List<DrawingDetail> drawingDetailsAfterDel = drawingDetailRepository.findByDrawingIdOrderByCreatedAtDesc(actInst.getEntityId());
            Optional<Drawing> optional = drawingRepository.findById(actInst.getEntityId());
            if (!optional.isPresent()) {
                throw new BusinessError("drawing does not exist");
            }
            Drawing drawing = optional.get();
            if (drawingDetailsAfterDel.size() > 0) {
                drawing.setLatestRev(drawingDetailsAfterDel.get(0).getRevNo());
            } else {
                drawing.setLatestRev(null);
            }
            drawingRepository.save(drawing);

            List<WBSEntry> wbsEntries = wbsEntryRepository.findByOrgIdAndProjectIdAndEntityIdAndProcessAndDeletedIsFalse(
                actInst.getOrgId(),
                actInst.getProjectId(),
                actInst.getEntityId(),
                actInst.getProcess()
            );
            for (WBSEntry wbsEntry : wbsEntries) {
                WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
                wbsEntryState.setRunningStatus(null);
                wbsEntryStateRepository.save(wbsEntryState);
            }

            //生成log
            BpmActivityInstanceLog log = new BpmActivityInstanceLog();
            log.setOrgId(actInst.getOrgId());
            log.setProjectId(actInst.getProjectId());
            log.setEntityNo(actInst.getEntityNo());
            log.setDrawingTitle(actInst.getDrawingTitle());
            log.setProcess(actInst.getProcess());
            log.setCreatedAt(new Date());
            log.setCreatedBy(operator.getId());
            log.setStatus(EntityStatus.FINISHED);
            bpmActivityInstanceLogRepository.save(log);

            bpmActInstRepository.delete(actInst);

            return true;
        }
//        }
        return false;
    }

    @Override
    public List<HierarchyBaseDTO> getProcessCategoryInActivity(Long orgId, Long projectId) {
        return bpmActInstRepository.findProcessCategoryByProjectId(projectId);
    }

    @Override
    public BpmProcessCategory findProcessCategoryById(Long id) {
        Optional<BpmProcessCategory> op = bpmProcessCategoryRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }


    /*
     * 根据材料编码取得实体信息 (non-Javadoc)
     *
     * @see com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface#
     * getEntityMaterialByEntityIdAndEntityType(java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, Object> getEntityMaterialByEntityIdAndEntityType(Long orgId, Long projectId, Long entityId,
                                                                        String entityType) {
        // TODO Auto-generated method stub
        Map<String, Object> entityObject = new HashMap<>();

        if (entityType == null) {
            return entityObject;
        }

        // 管段
        if (entityType.equals("PIPE_PIECE")) {
            Optional<PipePieceEntity> opt = null;
            if (opt != null) {
                PipePieceEntity entity = opt.get();
                entityObject.put("Bevel Code1", entity.getBevelCode1());
                entityObject.put("Bevel Code2", entity.getBevelCode2());
                entityObject.put("Bevel Info", entity.getBendInfo());
                entityObject.put("Cut Drawing", entity.getCutDrawing());
                entityObject.put("Revision", entity.getRevision());
                entityObject.put("Material Code", entity.getMaterialCode());
                entityObject.put("Material", entity.getMaterial());
                entityObject.put("Nps Text", entity.getNpsText());
                entityObject.put("Nps Unit", entity.getNpsUnit());
                entityObject.put("Nps", entity.getNps());
                entityObject.put("Length Text", entity.getLengthText());
                entityObject.put("Length Unit", entity.getLengthUnit());
                entityObject.put("Length", entity.getLength());
            }
            // 焊口
        } else if (entityType.equals("WELD_JOINT")) {
            Optional<WeldEntity> opt = null;
            if (opt != null) {
                WeldEntity entity = opt.get();
                entityObject.put("Sheet No", entity.getSheetNo());
                entityObject.put("Sheet Total", entity.getSheetTotal());
                entityObject.put("Revision", entity.getRevision());
                entityObject.put("WpsNo", entity.getWpsNo());
                entityObject.put("Nde", entity.getNde());
                entityObject.put("NdeRatio", entity.getNdeRatio());
                entityObject.put("PWHT", entity.getPwht());
                entityObject.put("Hard NessTest", entity.getHardnessTest());
                entityObject.put("PMI Ratio", entity.getPmiRatio());
                entityObject.put("Pipe Class", entity.getPipeClass());
                entityObject.put("NPS Text", entity.getNpsText());
                entityObject.put("NPS Unit", entity.getNpsUnit());
                entityObject.put("NPS", entity.getNps());
                entityObject.put("Thickness", entity.getThickness());
                entityObject.put("TagNo1", entity.getTagNo1());
                entityObject.put("MaterialCode1", entity.getMaterialCode1());
                entityObject.put("Material1", entity.getMaterial1());
                entityObject.put("Remarks1", entity.getRemarks1());
                entityObject.put("TagNo2", entity.getTagNo2());
                entityObject.put("MaterialCode2", entity.getMaterialCode2());
                entityObject.put("Material2", entity.getMaterial2());
                entityObject.put("Remarks2", entity.getRemarks2());

            }

            // SPOOL
        } else if (entityType.equals("SPOOL")) {

            // ISO
        } else if (entityType.equals("ISO")) {

            // 组件
        } else if (entityType.equals("COMPONENT")) {

        }
        return entityObject;
    }

    /**
     * 根据材料编码取得实体信息 (non-Javadoc)
     *
     * @see com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface#
     * getEntityMaterialByEntityIdAndEntityType(java.lang.String, java.lang.String)
     */
    @Override
    public List<SubDrawing> getEntityDrawingByEntityIdAndEntityType(Long orgId, Long projectId,
                                                                    Long entityId, String entityType, BpmActivityInstanceBase actInst) {

        Set<Long> entityIds = new HashSet<>();
        //1.0  取得工序 bpmProcess
        if (actInst != null && actInst.getProcessId() != null) {
            BpmProcess bpmProcess = processRepository.findById(actInst.getProcessId()).orElse(null);
            if (bpmProcess == null) {
                return new ArrayList<>();
            }

            switch (bpmProcess.getProcessType()) {
                case CUT_LIST:
                    if (bpmProcess.getDiscipline() == "STRUCTURE") {
                    } else if (bpmProcess.getDiscipline() == "PIPING") {
                    }
                    break;

                case DELIVERY_LIST:
                    break;

                default:
                    entityIds.add(entityId);

            }

        }


        //3.0 分别查找每个实体对应的图纸
        return projectNodeService.getDrawingsInfo(orgId, projectId, entityIds);
    }

    @Override
    public List<BpmDrawingSignTask> findBpmDrawingSignTask(Long taskId) {
        return bpmDrawingSignTaskRepository.findByTaskId(taskId);
    }

    @Override
    public List<BpmEntityDocsMaterials> getDocsMaterialsByProcessIdAndEntityIdAndActInstanceId(Long processId, Long entityId, Long actInstanceId) {
        return docsMaterialsRepository.findByProcessIdAndEntityIdAndActInstanceId(processId, entityId, actInstanceId);
    }

    @Override
    public List<BpmProcess> getProcessByNameEN(Long orgId, Long projectId, String nameEN) {
        return processRepository.findByOrgIdAndProjectIdAndNameEnAndStatus(orgId, projectId, nameEN, EntityStatus.ACTIVE);
    }

    @Override
    public BpmEntitySubType getEntitiySubTypeByNameEN(Long orgId, Long projectId, String nameEn) {
        BpmEntitySubType op = entitySubTypeRepository.findByProjectIdAndNameEn(projectId, nameEn);
        if (op != null) {
            return op;
        }
        return null;
    }

    @Override
    public List<BpmActivityInstanceBase> findActInst(Long orgId, Long projectId, Long entityId) {
        return bpmActInstRepository.findByProjectIdAndEntityId(projectId, entityId);
    }

    @Override
    public BatchTasksCategorySearchResultDTO batchTasksCategorySearch(Long orgId, Long projectId,
                                                                      BatchTasksCategorySearchDTO searchDTO) {

        BatchTasksCategorySearchResultDTO result = new BatchTasksCategorySearchResultDTO();
        result.setActInstIds(searchDTO.getActInstIds());

        List<TasksCategoryAssigneeDTO> assignees = bpmActInstRepository.batchFindTaskCategoryAssignee(searchDTO.getActInstIds());


        for (TasksCategoryAssigneeDTO dto : assignees) {
            String displayName = UserPrivilege.getByName(dto.getCategory()).getDisplayName();
            if (displayName.endsWith("执行")) {
                displayName = displayName.substring(0, displayName.length() - 2);
            }
            dto.setName(displayName);
        }

        result.setAssignees(assignees);


        return result;
    }

    @Override
    public boolean batchTasksCategoryAssignee(Long orgId, Long projectId,
                                              BatchTasksCategoryAssigneeDTO assigneeDTO) {
        List<BpmActTaskAssignee> assignees = bpmActTaskAssigneeRepository.findByTaskCategoryAndActInstIdIn(assigneeDTO.getCategory(), assigneeDTO.getActInstIds());
        for (BpmActTaskAssignee assignee : assignees) {
            if (assignee.getAssignee() == null
                || assigneeDTO.isCovered()) {
                assignee.setAssignee(assigneeDTO.getAssignee());
                String assigneeName = getRedisKey(String.format(RedisKey.USER.getDisplayName(), assigneeDTO.getAssignee()), 60 * 60 * 8);
                if (StringUtils.isEmpty(assigneeName)) {
                    assigneeName = userFeignAPI.get(assigneeDTO.getAssignee()).getData().getName();
                    setRedisKey(String.format(RedisKey.USER.getDisplayName(), assigneeDTO.getAssignee()), assigneeName);
                }
                assignee.setAssigneeName(assigneeName);
                bpmActTaskAssigneeRepository.save(assignee);
            }


            BpmRuTask ruTask = ruTaskRepository.findByActInstIdAndTaskDefKey(assignee.getActInstId(), assignee.getTaskDefKey());
            if (ruTask != null && assigneeDTO.isCovered()) {
                ruTask.setAssignee(String.valueOf(assigneeDTO.getAssignee()));
                ruTaskRepository.save(ruTask);

                List<BpmRuTask> ruTaskList = this.findBpmRuTaskByActInstId(assignee.getActInstId());
                String currentExecutor = todoTaskBaseService.setTaskAssignee(ruTaskList);

                BpmActivityInstanceBase actInst = bpmActInstRepository.findById(ruTask.getActInstId()).orElse(null);
                if (actInst == null) return false;
                BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());

                actInstState.setCurrentExecutor(currentExecutor);
                bpmActivityInstanceStateRepository.save(actInstState);

                if (taskRuleCheckService.isDrawingDesignTaskNode(ruTask.getTaskDefKey())
                    && taskRuleCheckService.DESIGN_PROCESSES.contains(actInst.getProcess())) {

                    drawingRepository.findByOrgIdAndProjectIdAndDwgNoAndStatus(orgId, projectId,
                            actInst.getEntityNo(), EntityStatus.ACTIVE)
                        .ifPresent(dwg -> {
                            String assigneeName = getRedisKey(String.format(RedisKey.USER.getDisplayName(), assigneeDTO.getAssignee()), 60 * 60 * 8);
                            if (StringUtils.isEmpty(assigneeName)) {
                                assigneeName = userFeignAPI.get(assigneeDTO.getAssignee()).getData().getName();
                                setRedisKey(String.format(RedisKey.USER.getDisplayName(), assignee), assigneeName);
                            }
//                            dwg.setDrawer(assigneeName);
//                            dwg.setDrawerId(assigneeDTO.getAssignee());
                            drawingRepository.save(dwg);
                        });
                }

//                actTaskFeignAPI.assignee(actInst.getId(), assignee.getTaskDefKey(),
//                    assignee.getTaskName(), assigneeDTO.getAssignee());
            }

        }

        return true;
    }

    @Override
    public void createSuspensionTaskNode(BpmActivityInstanceBase actInst, ActInstSuspendDTO dto, SuspensionState suspend, OperatorDTO operatorDTO) {
        BpmActTask task = new BpmActTask();
        task.setProjectId(actInst.getProjectId());
        task.setComment(dto.getMemo());
        task.setOperatorId(operatorDTO.getId());
        task.setOperatorName(operatorDTO.getName());
        task.setActInstId(actInst.getId());
        task.setTaskId(0L);
        task.setCreatedAt();
        task.setStatus(EntityStatus.ACTIVE);
        bpmActTaskRepository.save(task);
    }

    @Override
    public List<BpmActTask> findBpmActTaskByActInstIdOrderByCreatedAtAsc(Long actInstId) {
        return bpmActTaskRepository.findByActInstIdOrderByCreatedAtAsc(actInstId);
    }

    @Override
    public BpmHiTaskinst findHiTaskinstByTaskId(Long taskId) {
        return hiTaskinstRepository.findByTaskId(taskId);
    }


    @Override
    public ActInstTeamWorkSiteDTO getTeamWorkSite(Long orgId, Long projectId,
                                                  BatchTasksCategorySearchDTO searchDTO) {
        ActInstTeamWorkSiteDTO resultDTO = new ActInstTeamWorkSiteDTO();
        resultDTO.setActInstIds(searchDTO.getActInstIds());

        Map<Long, String> teamsMap = new HashMap<>();
        Map<Long, String> workSitesMap = new HashMap<>();

        Iterable<BpmActivityInstanceState> list = bpmActivityInstanceStateRepository.findByBaiIdIn(searchDTO.getActInstIds());
        Iterator<BpmActivityInstanceState> iterator = list.iterator();
        while (iterator.hasNext()) {
            BpmActivityInstanceState actInst = iterator.next();
            if (actInst.getTeamId() != null) {
                teamsMap.put(actInst.getTeamId(), actInst.getTeamName());
            }
            if (actInst.getWorkSiteId() != null) {
                workSitesMap.put(actInst.getWorkSiteId(), actInst.getWorkSiteName());
            }
        }

        List<TeamDTO> teams = new ArrayList<>();
        Iterator<Long> i = teamsMap.keySet().iterator();
        while (i.hasNext()) {
            Long key = i.next();
            teams.add(resultDTO.new TeamDTO(key, teamsMap.get(key)));
        }
        resultDTO.setTeams(teams);

        List<WorkSiteDTO> workSites = new ArrayList<>();
        i = workSitesMap.keySet().iterator();
        while (i.hasNext()) {
            Long key = i.next();
            workSites.add(resultDTO.new WorkSiteDTO(key, workSitesMap.get(key)));
        }
        resultDTO.setWorkSites(workSites);

        return resultDTO;
    }

    @Override
    public void setTeamWorkSite(Long orgId, Long projectId, ActInstTeamWorkSiteAssigneeDTO assigneeDTO) {

        Long companyId = null;
        Organization team = null;

        // 取得工作组信息
        if (assigneeDTO.getTeamId() != null) {
            team = organizationFeignAPI
                .details(assigneeDTO.getTeamId(), orgId)
                .getData();
            if (team == null) {
                throw new BusinessError("error.work-team.not-found");
            }
            companyId = team.getCompanyId();
        }

        // 取得公司 ID
        if (companyId == null) {
            Organization org = organizationFeignAPI.details(orgId, null).getData();
            if (org == null) {
                throw new NotFoundError();
            }
            companyId = org.getCompanyId();
        }

        WorkSite workSite = null;

        // 取得场地信息
        if (assigneeDTO.getWorkSiteId() != null) {
            workSite = workSiteService.get(companyId, projectId, assigneeDTO.getWorkSiteId());
            if (workSite == null) {
                throw new BusinessError("工作场地不存在"); // TODO 工作场地不存在
            }
        }


        Iterable<BpmActivityInstanceState> list = bpmActivityInstanceStateRepository.findByBaiIdIn(assigneeDTO.getActInstIds());
        Iterator<BpmActivityInstanceState> iterator = list.iterator();
        while (iterator.hasNext()) {
            BpmActivityInstanceState actInst = iterator.next();
            if (assigneeDTO.getTeamId() != null) {
                if (actInst.getTeamId() == null || assigneeDTO.isCovered()) {
                    actInst.setTeamId(team.getId());
                    actInst.setTeamName(team.getName());
                }
            }
            if (assigneeDTO.getWorkSiteId() != null) {
                if (actInst.getWorkSiteId() == null || assigneeDTO.isCovered()) {
                    actInst.setWorkSiteId(workSite.getId());
                    actInst.setWorkSiteName(workSite.getName());
                    actInst.setWorkSiteAddress(workSite.getAddress());
                }
            }
            bpmActivityInstanceStateRepository.save(actInst);
        }
    }

    @Override
    public List<HierarchyBaseDTO> getTaskDefKeys(Long orgId, Long projectId, Long processStageId, Long processId) {

        return bpmActInstRepository.findTaskDefKeyInActivity(
            projectId,
            processStageId,
            processId
        );
    }


    /**
     * 获取流程图片base64字符串
     * <p>
     * //     * @param procInctId 流程实例id
     *
     * @return String
     */
    @Override
    public DiagramResourceDTO getDiagramResource(Long projectId, Long processId, int bpmnVersion) {

        // 获取当前任务流程图片
        BpmReDeployment reDeployment = bpmReDeploymentRepository.findByProjectIdAndProcessIdAndVersion(projectId, processId, bpmnVersion);
        if (reDeployment == null) {
            throw new NotFoundError();
        }

        BpmnModel bpmnModel = null;
        InputStream bpmnStream;
        final File bpmnFile = new File(protectedDir, reDeployment.getFilePath());

//        /Users/Macbook/eclipse-cms/import/08-12-焊接 v2.0.bpmn

        // 取得工作流定义文件的输入流
        try {
            bpmnStream = new FileInputStream(bpmnFile);

            //根据 上传的 bpmn文件 取得 bpmn 模型
            bpmnModel = BPMNUtils.readBpmnFile(bpmnStream);
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }

        if (bpmnModel != null) {

            InputStream imageStream = (new DefaultProcessDiagramGenerator()).generateDiagram(bpmnModel, "jpg", Collections.<String>emptyList(), Collections.<String>emptyList(), "宋体", "微软雅黑", "黑体", null, 1.0);

            DiagramResourceDTO diagramResourceDTO = new DiagramResourceDTO();
            diagramResourceDTO.setDiagramResource("data:image/jpeg;base64," + getImageStr(imageStream));
            return diagramResourceDTO;
        }
        return null;
    }

    @Override
    public DiagramResourceDTO getDiagramResource(Long projectId, Long actInstId, Long processId, int bpmnVersion) {
        // 获取当前任务流程图片
        List<BpmHiTaskinst> executedActivityList = new ArrayList<>(); // 构造已执行的节点ID集合
        List<String> executedActivityIdList = new ArrayList<>(); // 构造已执行的节点ID集合
        executedActivityList = hiTaskinstRepository.findByActInstIdOrderByStartTimeAsc(actInstId);
        boolean endIncluded = true;
        List<BpmHiTaskinst> notEndEntitys = new ArrayList<>();

        for (BpmHiTaskinst ht : executedActivityList) {
            if (ht.getEndTime() == null) {
                endIncluded = false;
                notEndEntitys.add(ht);
            } else {

                executedActivityIdList.add(ht.getTaskDefKey());
            }

        }
        if (!endIncluded) {
            executedActivityIdList.addAll(notEndEntitys.stream().map(BpmHiTaskinst::getTaskDefKey).collect(Collectors.toList()));
        }
        if (CollectionUtils.isEmpty(executedActivityIdList)) {
            return new DiagramResourceDTO();
        }
//        if(executedActivityList.get(executedActivityList.size()-1).getEndTime() != null) {
//            endIncluded = true;
//        }

        BpmReDeployment reDeployment = bpmReDeploymentRepository.findByProjectIdAndProcessIdAndVersion(projectId, processId, bpmnVersion);
        if (reDeployment == null) {
            throw new NotFoundError();
        }

        InputStream diagramStream = null;
        BpmnModel bpmnModel = null;
        InputStream bpmnStream;
        File bpmnFile = new File(protectedDir, reDeployment.getFilePath());

        if (!bpmnFile.exists()) {
            bpmnFile = new File("/var/www/bpmnfile/" + reDeployment.getFileId().toString() + "d.bpmn");
        }
        if (!bpmnFile.exists()) {
            return new DiagramResourceDTO();

        }

//        /Users/Macbook/eclipse-cms/import/08-12-焊接 v2.0.bpmn

        // 取得工作流定义文件的输入流
        try {
            bpmnStream = new FileInputStream(bpmnFile);

            //根据 上传的 bpmn文件 取得 bpmn 模型
            bpmnModel = BPMNUtils.readBpmnFile(bpmnStream);
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }

        List<String> flowIds = new ArrayList<>();//
        if (bpmnModel != null) {
            Map<String, List<String>> flowMap = getExecutedFlows(bpmnModel, executedActivityIdList, endIncluded); // 获取流程已发生流转的线ID集合
            executedActivityIdList.addAll(flowMap.get("POINT"));
            flowIds = flowMap.get("LINE");

            InputStream imageStream = (new DefaultProcessDiagramGenerator()).generateDiagram(bpmnModel, "png", executedActivityIdList,
                flowIds, "宋体", "微软雅黑", "黑体", null, 2.0); // 使用默认配置获得流程图表生成器，并生成追踪图片字符流

//            InputStream imageStream = (new DefaultProcessDiagramGenerator())
//                .generateDiagram(bpmnModel, "jpg", Collections.<String>emptyList(), Collections.<String>emptyList(), "宋体", "微软雅黑", "黑体", null, 1.0);

            DiagramResourceDTO diagramResourceDTO = new DiagramResourceDTO();
            diagramResourceDTO.setDiagramResource("data:image/jpeg;base64," + getImageStr(imageStream));
//            diagramResourceDTO.setDiagramResourceName("bpmnImage");
            diagramResourceDTO.setFileId(reDeployment.getFileId());

            return diagramResourceDTO;
        }
        return null;
    }

    @Override
    public List<TaskGatewayDTO> getTaskGateway(Long projectId, Long processId, int bpmnVersion, String taskDefKey) {

        ProcessBpmnRelation processBpmnRelation = processService.getBpmnRelation(projectId, processId, bpmnVersion, taskDefKey);
        String taskGateWaysStr = processBpmnRelation.getTaskGateWays();
        List<TaskGatewayDTO> taskGatewayDTOs = StringUtils.decode(taskGateWaysStr, new TypeReference<List<TaskGatewayDTO>>() {
        });
        return taskGatewayDTOs;
    }

    @Override
    public BpmHiTaskinst findHiTaskinstByTaskIdAndSeq(Long taskId, int seq) {
        return hiTaskinstRepository.findHiTaskinstByTaskIdAndSeq(taskId, seq);
    }

    @Override
    public List<BpmActivityInstanceDTO> searchFunction(Long orgId, Long projectId) {
        Set<String> functions = projectNodeRepository.findFunctions(orgId, projectId);
        List<BpmActivityInstanceDTO> result = new ArrayList<>();
        for (String no : functions) {
            BpmActivityInstanceDTO dto = new BpmActivityInstanceDTO();
            dto.setFunctionNo(no);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<BpmActivityInstanceDTO> searchType(Long orgId, Long projectId) {
        Set<String> types = projectNodeRepository.findTypes(orgId, projectId);
        List<BpmActivityInstanceDTO> result = new ArrayList<>();
        for (String no : types) {
            BpmActivityInstanceDTO dto = new BpmActivityInstanceDTO();
            dto.setTypeNo(no);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<BpmActivityInstanceShiftLog> getShiftLogs(Long orgId, Long projectId, Long id) {
        return bpmActivityInstanceShiftRepository.findAllByBaiId(id);
    }

    @Override
    public List<BpmActTaskAssignee> getTaskSupport(Long orgId, Long projectId, Long actInstId) {
        return bpmActTaskAssigneeRepository.findAllByActInstIdAndTaskDefKeyAndStatus(actInstId, "usertask-SUPPORT", EntityStatus.ACTIVE);
    }

    @Override
    public void addTaskSupport(Long orgId, Long projectId, Long actInstId, TaskSupportDTO dto) {

        //查询已存在的数据
        List<BpmActTaskAssignee> existedAssignee = bpmActTaskAssigneeRepository.findAllByActInstIdAndTaskDefKeyAndStatus(actInstId, "usertask-SUPPORT", EntityStatus.ACTIVE);

        List<Long> assigneeIds = existedAssignee.stream().map(BpmActTaskAssignee::getAssignee).toList();
        if (!existedAssignee.isEmpty()) {

            //找到在assigneeIds中，但不在dto.getCounterIds()中的数据
            List<BpmActTaskAssignee> toDeleteAssignee = existedAssignee.stream()
                .filter(assignee -> !dto.getCounterIds().contains(String.valueOf(assignee.getAssignee())))
                .toList();

            BpmActivityInstanceBase bpmActivity = bpmActInstRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, actInstId);

            //查询是否填写工时，填了就不允许删除
            for (BpmActTaskAssignee assignee : toDeleteAssignee) {
                List<DrawingRecord> drawingRecords = drawingRecordRepository.findAllByEngineerIdAndDrawingIdAndDeletedIsFalse(assignee.getAssignee(), bpmActivity.getEntityId());
                if (!drawingRecords.isEmpty()) {
                    throw new BusinessError(assignee.getAssigneeName() + " already filled in working hours on this drawing!Please reselect support");
                }
            }

            //将现有的数据删除
            bpmActTaskAssigneeRepository.deleteAll(toDeleteAssignee);
        }
        if (!dto.getCounterIds().isEmpty()) {
            BatchGetDTO batchGetDTO = new BatchGetDTO();
            batchGetDTO.setEntityIDs(dto.getCounterIds().stream()
                .map(Long::valueOf)
                .collect(Collectors.toCollection(HashSet::new)));
            Map<Long, String> userNameMap = new HashMap<>();

            JsonListResponseBody<UserBasic> users = userFeignAPI.batchGet(batchGetDTO);

            if (users != null && users.getData() != null) {
                for (UserBasic userBasic : users.getData()) {
                    userNameMap.put(userBasic.getId(), userBasic.getName());
                }
            }
            //找到在dto.getCounterIds()中，但不在assigneeIds中的id数据
            List<String> toAddIds = dto.getCounterIds().stream()
                .filter(id -> !assigneeIds.contains(Long.valueOf(id)))
                .toList();

            for (String userId : toAddIds) {
                BpmActTaskAssignee assignee = new BpmActTaskAssignee();
                assignee.setSeq(0);
                assignee.setTaskDefKey("usertask-SUPPORT");
                assignee.setTaskName("SUPPORT");
                assignee.setStatus(EntityStatus.ACTIVE);
                assignee.setOrderNo(0);
                assignee.setActInstId(actInstId);
                assignee.setAssignee(Long.valueOf(userId));
                assignee.setAssigneeName(userNameMap.get(Long.valueOf(userId)));
                bpmActTaskAssigneeRepository.save(assignee);
            }
        }

    }

    @Override
    public List<ActTaskNodeDTO> getModelNodes(Long projectId, Long processId, int version) {
        List<ProcessBpmnRelation> processBpmnRelations = processBpmnRelationRepository.
            findByProjectIdAndProcessIdAndBpmnVersion(projectId, processId, version);
        List<ActTaskNodeDTO> modelNodes = new ArrayList<>();

        int i = 1;
        for (ProcessBpmnRelation processBpmnRelation : processBpmnRelations) {
            ActTaskNodeDTO taskNodeDTO = new ActTaskNodeDTO();
            taskNodeDTO.setOrderNo(i++);
            taskNodeDTO.setTaskCategory(processBpmnRelation.getCategory());
            taskNodeDTO.setTaskDefKey(processBpmnRelation.getNodeId());
            taskNodeDTO.setTaskNodeName(processBpmnRelation.getNodeName());
            modelNodes.add(taskNodeDTO);
        }

        return modelNodes;
    }


    private Map<String, List<String>> getExecutedFlows(BpmnModel bpmnModel,
                                                       List<String> historicActivityIds, boolean endIncluded) {
        StartEvent startEvent = null;
        EndEvent endEvent = null;
        List<String> linkedIds = new ArrayList<>();

        List<String> points = new ArrayList<>();
        Set<String> flowIds = new HashSet<>();
        Process mainProcess = bpmnModel.getMainProcess();
        Map<String, Set<String>> flowPoints = new HashMap<>();
        for (FlowElement flowElement : mainProcess.getFlowElements()) {
            if (flowElement instanceof StartEvent) {
                startEvent = (StartEvent) flowElement;
//                break;
            } else if (flowElement instanceof SequenceFlow) {
                Set<String> pts = new HashSet<>();
                pts.add(((SequenceFlow) flowElement).getSourceRef());
                pts.add(((SequenceFlow) flowElement).getTargetRef());
                flowPoints.put(flowElement.getId(), pts);
            } else if (flowElement instanceof EndEvent && endIncluded) {
                endEvent = (EndEvent) flowElement;
            }
        }

        linkedIds.add(startEvent.getId());
        linkedIds.addAll(historicActivityIds);
        if (endIncluded) {
            linkedIds.add(endEvent.getId());
        }


        //存储前一个 后一个节点路径 gateway+userTask
//        Map<String, BpmnSequenceNodeDTO> pathMap = new HashMap<>();

//        Map<String, String> parentPathMap = new HashMap<>(); // path -> parentPath

        //输出流程线 SequenceFlow
        List<String> paths = new ArrayList<>();


        //////////////
        //遍历完需要移除的节点path集合
        String parentPath = "/";
        List<String> remainedIds = new ArrayList<>();
        remainedIds.addAll(linkedIds);
        remainedIds.remove(startEvent.getId());
        List<String> usedGateWays = new ArrayList<>();

        for (int i = 0; i < linkedIds.size(); i++) {

//            String path = "/";
            FlowElement fe = mainProcess.getFlowElement(linkedIds.get(i));
//            remainedIds.remove(fe.getId());
            List<SequenceFlow> outGoingFlows = ((FlowNode) fe).getOutgoingFlows();
            for (SequenceFlow sf : outGoingFlows) {
                String path = parentPath + sf.getId() + "/";
                if (i < linkedIds.size() - 1 && sf.getTargetRef().equals(linkedIds.get(i + 1))) {
                    paths.add(path);
                    remainedIds.remove(sf.getTargetRef());
                } else if (sf.getTargetFlowElement() instanceof Gateway) {
                    Map<String, Gateway> gateWayMap = new HashMap<>();
                    Map<String, Gateway> addedGateWayMap = new HashMap<>();
                    gateWayMap.put(path, (Gateway) sf.getTargetFlowElement());
                    Set<String> finishedGateWays = new HashSet<>();
                    Stack<String> gateWayStack = new Stack<>();

                    do {
                        gateWayMap.forEach((gPath, gateway) -> {
                            boolean isSkipEnd = false;
                            List<String> skipEndPaths = new ArrayList<>();
                            for (SequenceFlow outGoingFlow : gateway.getOutgoingFlows()) {
                                FlowNode currentFlowNode = (FlowNode) outGoingFlow.getTargetFlowElement();
                                gateWayStack.push(gateway.getId());
//                                if (Arrays.asList(gPath.split("/")).contains(outGoingFlow.getId())) continue;
                                if (currentFlowNode instanceof UserTask && remainedIds.size() > 0 && remainedIds.get(0).equals(currentFlowNode.getId())) {
                                    isSkipEnd = true;
                                    paths.add(gPath + outGoingFlow.getId() + "/");
                                    remainedIds.remove(currentFlowNode.getId());
                                    usedGateWays.addAll(gateWayStack);
                                    finishedGateWays.add(gateway.getId());
                                    gateWayStack.pop();
                                    if (gateway instanceof ExclusiveGateway) {
                                        break;
                                    }

                                } else if (currentFlowNode instanceof Gateway) {
                                    addedGateWayMap.put(gPath + outGoingFlow.getId() + "/", (Gateway) currentFlowNode);
                                    gateWayStack.push(gateway.getId());
                                } else if (endIncluded && currentFlowNode instanceof EndEvent && !isSkipEnd) {
                                    paths.add(gPath + outGoingFlow.getId() + "/");
                                    remainedIds.remove(currentFlowNode.getId());
                                    skipEndPaths.add(gPath + outGoingFlow.getId() + "/");
                                    usedGateWays.addAll(gateWayStack);
                                    finishedGateWays.add(gateway.getId());
                                    gateWayStack.pop();
                                }
                            }
                            if (isSkipEnd) {
                                paths.removeAll(skipEndPaths);
                            }

                        });

                        finishedGateWays.addAll(gateWayMap.keySet());
                        gateWayMap.clear();
                        gateWayMap.putAll(addedGateWayMap);
                        addedGateWayMap.clear();

                    } while (!MapUtils.isEmpty(gateWayMap));

                }

            }

        }


        ///////////////
        for (String mp : paths) {
            flowIds.addAll(Arrays.asList(mp.split("/")));
        }
        flowIds = flowIds.stream().filter(fl -> !StringUtils.isEmpty(fl)).collect(Collectors.toSet());
        points.addAll(usedGateWays);
        points.add(startEvent.getId());
        if (endIncluded) points.add(endEvent.getId());
        Map<String, List<String>> flowMarks = new HashMap<>();
        flowMarks.put("LINE", new ArrayList<>(flowIds));
        flowMarks.put("POINT", points);

        return flowMarks;
    }

    /**
     * 获取图片base64编码字符串
     *
     * @param in InputStream
     * @return String
     */
    private String getImageStr(InputStream in) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        // 对字节数组Base64编码 返回Base64编码过的字节数组字符串
        Base64 base64 = new Base64();
        return new String(base64.encode(data));
    }


}
