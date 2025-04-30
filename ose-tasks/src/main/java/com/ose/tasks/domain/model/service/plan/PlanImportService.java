package com.ose.tasks.domain.model.service.plan;

import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.*;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryAddLogRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryLogRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.process.BpmnTaskRelationRepository;
import com.ose.tasks.domain.model.repository.process.ModuleProcessDefinitionRepository;
import com.ose.tasks.domain.model.repository.repairdata.RepairDataRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.service.bpm.EntityTypeProcessRelationInterface;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.plan.business.PlanBusinessInterface;
import com.ose.tasks.domain.model.service.taskpackage.TaskPackageInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.dto.WBSEntityPostDTO;
import com.ose.tasks.dto.WBSEntryTeamWorkSiteDTO;
import com.ose.tasks.dto.process.EntityProcessRelationsDTO;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.process.*;
import com.ose.tasks.entity.taskpackage.TaskPackage;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.util.wbs.WorkflowEvaluator;
import com.ose.tasks.vo.bpm.ActivityExecuteResult;
import com.ose.tasks.vo.wbs.*;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import net.sf.mpxj.*;
import net.sf.mpxj.primavera.PrimaveraXERFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ose.tasks.vo.wbs.WBSEntryType.ENTITY;
import static com.ose.tasks.vo.wbs.WBSEntryType.WORK;

/**
 * 计划管理服务。
 */
@Component
public class PlanImportService implements PlanImportInterface {


    private static Boolean updateByNo = false;
    private static final String[] AREA_ENTITY_TYPES = new String[]{
        "AREA",
        "SUB_AREA",
        "WP01",
        "WP02",
        "WP03",
        "SECTOR",
        "SUB_SECTOR",
        "SYSTEM"
    };

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);


    private static final String[] AREA_HIERARCHY_TYPES = new String[]{
        "PIPING",
        "PRESSURE_TEST_PACKAGE",
        "CLEAN_PACKAGE",
        "SUB_SYSTEM",
        "ELECTRICAL",
        "CABLE_PULLING_PACKAGE",
        "STRUCTURE"
    };

    private static final String[] ENGINEERING_HIERARCHY_TYPES = new String[]{
        "ENGINEERING_COMMON",
        "ENGINEERING_AREA",
        "ENGINEERING_SUB_SYSTEM"
    };

    private static final Pattern WBS_PATTERN = Pattern.compile("^(.+\\.)?([^.]+)$");

    private static final Map<String, String[]> funcPartHierarchyTypeMap = new HashMap<String, String[]>() {{
        put("ENGINEERING", ENGINEERING_HIERARCHY_TYPES);
//        put(FuncPart.AREA.name(), AREA_HIERARCHY_TYPES)
    }};

    private static final Set<String> INSPECTION_PROCESSES = new HashSet<>();


    private static final int BATCH_FETCH_SIZE = 1000;


    private static final Set<WBSEntryRunningStatus> RUNNING_STATUS_CANNOT_BE_REMOVED = new HashSet<>(
        Arrays.asList(
            WBSEntryRunningStatus.RUNNING,
            WBSEntryRunningStatus.REJECTED,
            WBSEntryRunningStatus.APPROVED
        )
    );


    private static final Set<String> RUNNING_STATUS_CANNOT_BE_REMOVED_STR = new HashSet<>(
        Arrays.asList(
            WBSEntryRunningStatus.RUNNING.name(),
            WBSEntryRunningStatus.REJECTED.name(),
            WBSEntryRunningStatus.APPROVED.name()
        )
    );

    private static final Set<String> PIPING_HIERARCHY_TYPE = new HashSet<>(
        Arrays.asList(
            "PIPING",
            "CLEAN_PACKAGE",
            "PRESSURE_TEST_PACKAGE",
            "SUB_SYSTEM"
        )
    );


    private static final Set<String> STRUCTURE_HIERARCHY_TYPE = new HashSet<>(
        Arrays.asList(
            "STRUCTURE",
            "SUB_SYSTEM"
        )
    );

    private static final Set<String> ELECTRICAL_HIERARCHY_TYPE = new HashSet<>(
        Arrays.asList(
            "ELECTRICAL",
            "CABLE_PULLING_PACKAGE"
        )
    );
    private static final Set<String> ENGINEERING_HIERARCHY_TYPE = new HashSet<>(
        Arrays.asList(
            "ENGINEERING_AREA",
            "ENGINEERING_COMMON",
            "ENGINEERING_SUB_SYSTEM"
        )
    );
    private static final Map<String, Set<String>> FUNC_PART_HIERARCHY_MAP = new HashMap<>();


    static {
        INSPECTION_PROCESSES.add("FABRICATION/PMI");
        INSPECTION_PROCESSES.add("FABRICATION/PWHT");
        INSPECTION_PROCESSES.add("FABRICATION/NDT");
        INSPECTION_PROCESSES.add("FABRICATION/HD");
        FUNC_PART_HIERARCHY_MAP.put("PIPING", PIPING_HIERARCHY_TYPE);
        FUNC_PART_HIERARCHY_MAP.put("STRUCTURE", STRUCTURE_HIERARCHY_TYPE);
        FUNC_PART_HIERARCHY_MAP.put("ELECTRICAL", ELECTRICAL_HIERARCHY_TYPE);
        FUNC_PART_HIERARCHY_MAP.put("ENGINEERING", ENGINEERING_HIERARCHY_TYPE);

    }


    @Value("${application.files.temporary}")
    private String temporaryDir;


    private final ProjectRepository projectRepository;
    private final WBSEntryLogRepository wbsEntryLogRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;


    private final WBSEntryRepository wbsEntryRepository;

    private final WBSEntryAddLogRepository wbsEntryAddLogRepository;

    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;

    private final WBSEntryDelegateRepository wBSEntryDelegateRepository;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final ProjectNodeRepository projectNodeRepository;


    private final HierarchyRepository hierarchyRepository;


    private final ProcessEntityRepository processEntityRepository;


    private final ProjectNodeModuleTypeRepository projectNodeModuleTypeRepository;


    private final ModuleProcessDefinitionRepository moduleProcessDefinitionRepository;


    private final PlanBusinessInterface planBusiness;


    private final BatchTaskRepository batchTaskRepository;

    private final ProcessEntityTypeRepository processEntityTypeRepository;


    private final WBSEntryExecutionHistoryRepository wbsEntryExecutionHistoryRepository;


    private final PlanRelationInterface planRelationService;

    private final WBSEntryPlainRelationInterface wbsEntryPlainRelationService;

    private final WBSEntryBlobRepository wbsEntryBlobRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final TaskPackageRepository taskPackageRepository;
    private final BpmnTaskRelationRepository bpmnTaskRelationRepository;

    private final TodoTaskBaseInterface todoTaskBaseService;
    private final TaskPackageInterface taskPackageService;

    private final BpmEntitySubTypeRepository entitySubTypeRepository;
    private final ProcessInterface processService;

    private final RepairDataRepository repairDataRepository;

    private final EntityTypeProcessRelationInterface entityTypeProcessRelationService;


    /**
     * 构造方法。
     */
    @Autowired
    public PlanImportService(
        ProjectRepository projectRepository,
        WBSEntryLogRepository wbsEntryLogRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        WBSEntryRepository wbsEntryRepository,
        WBSEntryAddLogRepository wbsEntryAddLogRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        ProjectNodeRepository projectNodeRepository,
        HierarchyRepository hierarchyRepository,
        ProcessEntityRepository processEntityRepository,
        ProjectNodeModuleTypeRepository projectNodeModuleTypeRepository,
        ModuleProcessDefinitionRepository moduleProcessDefinitionRepository,
        PlanBusinessInterface planBusinessImpl,
        BatchTaskRepository batchTaskRepository,
        ProcessEntityTypeRepository processEntityTypeRepository,
        WBSEntryExecutionHistoryRepository wbsEntryExecutionHistoryRepository,
        PlanRelationInterface planRelationService,
        WBSEntryDelegateRepository wBSEntryDelegateRepository,
        WBSEntryPlainRelationInterface wbsEntryPlainRelationService,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        TaskPackageRepository taskPackageRepository,
        BpmnTaskRelationRepository bpmnTaskRelationRepository, TodoTaskBaseInterface todoTaskBaseService,
        TaskPackageInterface taskPackageService,
        BpmEntitySubTypeRepository entitySubTypeRepository,
        ProcessInterface processService,
        RepairDataRepository repairDataRepository,
        EntityTypeProcessRelationInterface entityTypeProcessRelationService) {
        this.projectRepository = projectRepository;
        this.wbsEntryLogRepository = wbsEntryLogRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.wbsEntryAddLogRepository = wbsEntryAddLogRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.processEntityRepository = processEntityRepository;
        this.projectNodeModuleTypeRepository = projectNodeModuleTypeRepository;
        this.moduleProcessDefinitionRepository = moduleProcessDefinitionRepository;
        this.planBusiness = planBusinessImpl;
        this.batchTaskRepository = batchTaskRepository;
        this.processEntityTypeRepository = processEntityTypeRepository;
        this.wbsEntryExecutionHistoryRepository = wbsEntryExecutionHistoryRepository;
        this.planRelationService = planRelationService;
        this.wBSEntryDelegateRepository = wBSEntryDelegateRepository;
        this.wbsEntryPlainRelationService = wbsEntryPlainRelationService;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.taskPackageRepository = taskPackageRepository;
        this.bpmnTaskRelationRepository = bpmnTaskRelationRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.taskPackageService = taskPackageService;
        this.entitySubTypeRepository = entitySubTypeRepository;
        this.processService = processService;
        this.repairDataRepository = repairDataRepository;
        this.entityTypeProcessRelationService = entityTypeProcessRelationService;
    }

    /**
     * 保存 WBS 条目。
     *
     * @param operator                  操作者信息
     * @param project                   项目信息
     * @param areaNodeNames             项目的区域节点编号集合
     * @param layerPackageNames         层/包节点编号集合
     * @param tasks                     任务列表
     * @param parent                    上级任务条目
     * @param entityProcessRelationsDTO 工序-实体类型映射表
     * @param batchResult               批处理执行结果
     * @return 保存件数
     */
    private int saveWBSEntries(
        OperatorDTO operator,
        Project project,
        Set<String> areaNodeNames,
        Set<String> layerPackageNames,
        List<Task> tasks,
        WBSEntry parent,
        EntityProcessRelationsDTO entityProcessRelationsDTO,
        BatchResultDTO batchResult,
        Map<String, Long> processFuncPartMap
    ) {

        int count = 0;

        if (tasks == null || tasks.size() == 0) {
            return count;
        }
        WBSEntryBlob parentWbsEntryBlob;


        Long projectId = project.getId();
        Long parentId = null;
        String path = "/";
        int depth = 0;
        long parentSort = 0;

        if (parent != null) {
            parentId = parent.getId();
            parentWbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(parent.getId());
            if (parentWbsEntryBlob == null) throw new NotFoundError(parentId.toString());
            path = parentWbsEntryBlob.getPath() + parentId + "/";
            depth = parent.getDepth() + 1;
            parentSort = parent.getSort();
        }

        Map<String, Set<String>> stageProcesses = planBusiness.getStageProcesses(projectId);

        WBSEntry wbsEntry;
        WBSEntryBlob wbsEntryBlob;
        WBSEntryState wbsEntryState = null;

        for (Task task : tasks) {

            count++;


            WBSEntryBase.repairWBS(task);

            String wbs = (String) task.getWBS();//TaskField.TEXT1);

            String name = task.getName();

            String no = "";

            if (wbs == null) {
                wbs = task.getWBS();
            }

            //查询是否该条目是否在系统中存在，如果存在更新条目信息
            //如果depth：0~2,查询条件no
            if (updateByNo) {
                if (task.getWBS().equals(task.getActivityID())) {
//                    String[] wbsArr = wbs.split("\\.");
//                    no = wbsArr.length == 0 ? wbs:wbsArr[wbsArr.length - 1];

                    if (
                        (wbsEntryBlob = wbsEntryBlobRepository.findFirstByProjectIdAndWbsOrderByPathAsc(
                            projectId,
                            wbs
                        ).orElse(null)) != null
                    ) {
                        wbsEntry = wbsEntryRepository.findById(wbsEntryBlob.getWbsEntryId()).orElse(null);
                        if (wbsEntry != null) {
                            wbsEntry.update(operator, task, stageProcesses, project, processFuncPartMap, task.getWBS());
//                        wbsEntry.setNo(no);
//                            wbsEntryRepository.save(wbsEntry);
                        } else {
                            wbsEntry = new WBSEntry(operator, project, task, stageProcesses, processFuncPartMap);
                        }
                    } else {
                        wbsEntry = new WBSEntry(operator, project, task, stageProcesses, processFuncPartMap);
                    }
                } else {
                    no = task.getActivityID();
                    if (
                        (wbsEntry = wbsEntryRepository.findByProjectIdAndNo(
                            projectId,
                            no
                        ).orElse(null)) != null
                    ) {
                        wbsEntry.update(operator, task, stageProcesses, project, processFuncPartMap, task.getWBS());
//                        wbsEntry.setNo(no);
//                        wbsEntryRepository.save(wbsEntry);
                    } else {
                        wbsEntry = new WBSEntry(operator, project, task, stageProcesses, processFuncPartMap);
                    }
//                    wbsEntry.setType(TaskType.FIXED_WORK);
//                    wbsEntry.setNo(no);
                }


            } else {

                if (
                    (wbsEntry = wbsEntryRepository.findByProjectIdAndGuid(
                        projectId,
                        task.getGUID().toString()
                    ).orElse(null)) == null
                ) {
                    wbsEntry = new WBSEntry(operator, project, task, stageProcesses, processFuncPartMap);

                } else {
                    wbsEntry.update(operator, task, stageProcesses, project, processFuncPartMap, task.getWBS());

                }
            }

            wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntry.getId());
            if (wbsEntryBlob == null) {
                wbsEntryBlob = new WBSEntryBlob();
                wbsEntryBlob.setOrgId(project.getOrgId());
                wbsEntryBlob.setProjectId(projectId);
                wbsEntryBlob.setWbsEntryId(wbsEntry.getId());
                wbsEntryBlob.setWbs(task.getWBS());
                wbsEntryBlob.setPath(path);
                wbsEntryBlob.setActive(true);
            }
            wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
            if (wbsEntryState == null) {
                wbsEntryState = new WBSEntryState();
                wbsEntryState.setOrgId(project.getOrgId());
                wbsEntryState.setProjectId(projectId);
                wbsEntryState.setWbsEntryId(wbsEntry.getId());
                wbsEntryState.setFinishedScore(0);
                wbsEntryState.setActive(true);
                wbsEntryState.setLastModifiedAt(new Date());
                wbsEntryState.setLastModifiedBy(operator.getId());
                wbsEntryState.setTotalScore(0);
                wbsEntryState.setLastModifiedAt(new Date());
                wbsEntryState.setLastModifiedBy(operator.getId());

            }
            wbsEntry.setParentId(parentId);
            wbsEntryBlob.setPath(path);
            wbsEntry.setDepth(depth);
            wbsEntry.setSort(parentSort + count);


            if (wbsEntry.getType() == WORK) {

                if (!areaNodeNames.contains(wbsEntry.getSector())) {
                    batchResult.addErrorCount(1);
                    batchResult.addLog(String.format(
                        "【%s/%s】的区域【%s】无效",
                        wbsEntry.getName(),
                        wbsEntry.getNo(),
                        wbsEntry.getSector()
                    ));
                }

                if (wbsEntry.getLayerPackage() != null
                    && !layerPackageNames.contains(wbsEntry.getLayerPackage())) {
                    batchResult.addErrorCount(1);
                    batchResult.addLog(String.format(
                        "【%s/%s】的层/包【%s】无效",
                        wbsEntry.getName(),
                        wbsEntry.getNo(),
                        wbsEntry.getSector()
                    ));
                }

                if (!entityProcessRelationsDTO
                    .doesProcessExist(wbsEntry.getStage(), wbsEntry.getProcess())) {
                    batchResult.addErrorCount(1);
                    batchResult.addLog(String.format(
                        "【%s/%s】的工序【%s/%s】无效",
                        wbsEntry.getName(),
                        wbsEntry.getNo(),
                        wbsEntry.getStage(),
                        wbsEntry.getProcess()
                    ));
                }

            }


            wbsEntryRepository.save(wbsEntry);
            wbsEntryStateRepository.save(wbsEntryState);
            wbsEntryBlobRepository.save(wbsEntryBlob);
            wbsEntryPlainRelationService.saveWBSEntryPath(wbsEntry.getOrgId(), projectId, wbsEntry);


            count += saveWBSEntries(
                operator,
                project,
                areaNodeNames,
                layerPackageNames,
                task.getChildTasks(),
                wbsEntry,
                entityProcessRelationsDTO,
                batchResult,
                processFuncPartMap
            );

            Set<BigInteger> predecessorWbses = wbsEntryRelationRepository.findAllByTaskGUID(projectId, wbsEntry.getGuid());
//            Set<BigInteger> successorWbses = wbsEntryRelationRepository.findSuccessorsByTaskGUID(projectId, wbsEntry.getGuid());
            if (predecessorWbses == null) predecessorWbses = new HashSet<>();
//            predecessorWbses.addAll(successorWbses);
            predecessorWbses.forEach(wrId -> {
                wbsEntryRelationRepository.deleteById(wrId.longValue());
            });


            for (Relation relation : task.getPredecessors()) {
                planBusiness.saveWBSEntryRelations(
                    operator,
                    projectId,
                    relation.getSourceTask().getGUID().toString(),
                    wbsEntry.getGuid(),
                    relation.getType(),
                    false
                );
            }


            for (Relation relation : task.getSuccessors()) {
                planBusiness.saveWBSEntryRelations(
                    operator,
                    projectId,
                    wbsEntry.getGuid(),
                    relation.getSourceTask().getGUID().toString(),
                    relation.getType(),
                    false
                );
            }

        }

        return count;
    }

    /**
     * 导入计划。
     *
     * @param batchTask                 批处理任务信息
     * @param operator                  操作者信息lo
     * @param project                   项目信息
     * @param rootNodeId                根节点 ID
     * @param nodeImportDTO             节点导入操作数据传输对象
     * @param entityProcessRelationsDTO 工序-实体类型映射表
     * @return 批处理执行结果
     */
    @Override
//    @Transactional
    public BatchResultDTO importPlan(
        BatchTask batchTask,
        OperatorDTO operator,
        Project project,
        Long rootNodeId,
        HierarchyNodeImportDTO nodeImportDTO,
        EntityProcessRelationsDTO entityProcessRelationsDTO
    ) {

        ProjectFile projectFile;

        try {

            PrimaveraXERFileReader reader = new PrimaveraXERFileReader();

            reader.setCharset(Charset.forName("GBK"));

            projectFile = reader.read(
                new File(temporaryDir, nodeImportDTO.getFilename())
            );

        } catch (MPXJException e) {
            throw new NotFoundError();
        }

        final Set<String> areaNodeNames = new HashSet<>();
        final Set<String> layerPackageNames = new HashSet<>();

        projectNodeRepository
            .findByProjectIdAndEntityTypeInAndDeletedIsFalse(project.getId(), AREA_ENTITY_TYPES)
            .forEach(projectNode -> {
                areaNodeNames.add(projectNode.getDisplayName());
            });

        BatchResultDTO batchResult = new BatchResultDTO(batchTask);


        List<Task> rootTasks = projectFile.getChildTasks();

        if (rootTasks == null || rootTasks.size() > 1) {
            batchResult.addErrorCount(1);
            batchResult.addLog("必须存在且仅可存在一个根节点");
            return batchResult;
        }

        WBSEntry root = wbsEntryRepository
            .findFirstByProjectIdAndDepthAndDeletedIsFalse(project.getId(), 0);

        if (nodeImportDTO.getUpdateByNo() != null) {
            updateByNo = nodeImportDTO.getUpdateByNo();
        }


        if (root != null && ((!rootTasks.get(0).getGUID().toString().equals(root.getGuid()) && !updateByNo)
            || (updateByNo && !rootTasks.get(0).getActivityID().equals(root.getNo())))
        ) {
            batchResult.addErrorCount(1);
            batchResult.addLog("根节点 GUID / No 不一致，请导入相同项目的导出文件");
            return batchResult;
        }

        Map<String, Long> processFuncPartMap = entityTypeProcessRelationService.getProcessFuncPartMap(project.getId());


        final int count = saveWBSEntries(
            operator,
            project,
            areaNodeNames,
            layerPackageNames,
            rootTasks,
            null,
            entityProcessRelationsDTO,
            batchResult,
            processFuncPartMap
        );


        project.setLastModifiedBy(operator.getId());
        project.setLastModifiedAt(new Date());
        projectRepository.save(project);

        batchResult.addTotalCount(count);
        batchResult.addProcessedCount(count - batchResult.getErrorCount());

        return batchResult;
    }


    /**
     * 添加实体的工序 WBS 条目。
     *
     * @param operator                操作者信息
     * @param moduleProcessDefinition 模块工作流定义
     * @param workEntry               WBS Work 条目
     * @param entity                  实体信息
     * @param entityIndex             实体序号
     */
    private WBSEntry generateEntityProcessWBSEntry(
        final OperatorDTO operator,
        final ModuleProcessDefinition moduleProcessDefinition,
        final WBSEntry workEntry,
        final ProcessEntity entity,
        final int entityIndex
    ) {


        String entitySubType = entity.getEntitySubType();
        String entityType = entity.getEntityType();
        if (StringUtils.isEmpty(entitySubType)) {
            entitySubType = entityType;
        }
        String category = entity.getStage()
            + "/" + entity.getProcess()
            + "/" + entityType
            + "/" + entitySubType;


        Long orgId = moduleProcessDefinition.getOrgId();
        Long projectId = moduleProcessDefinition.getProjectId();
        Long moduleProcessDefinitionId = moduleProcessDefinition.getId();

        BpmnTaskRelation bpmnTaskRelation = planRelationService.
            getBpmnTaskRelationFromRedis(orgId, projectId, moduleProcessDefinitionId, category);

        if (bpmnTaskRelation == null) {
            return null;
        }

        String successorId = bpmnTaskRelation.getNodeId();

        if (successorId == null) {
            return null;
        }

        WorkflowProcessVariable variable = planBusiness.getWorkflowEntityVariable(
            entity.getProjectId(),
            entity.getEntityType(),
            entity.getEntityId(),
            entity.getFuncPart()
        );

        if (variable == null) {
            return null;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put(variable.getVariableName(), variable);

        try {

//            if (!Arrays.asList().contains(workEntry.getFuncPart())) {
////                if (!FuncPart.SUPPORTED_FUNC_PART_STRS.contains(workEntry.getFuncPart())) {
//                throw new BusinessError("the Func Part is not supported");
//            }


            if (WorkflowEvaluator.evaluate(bpmnTaskRelation, variables)) {

                WBSEntityPostDTO wbsEntityPostDTO = new WBSEntityPostDTO();
                wbsEntityPostDTO.setHierarchyNodeId(entity.getHierarchyNodeId());
                wbsEntityPostDTO.setParentHierarchyNodeId(entity.getParentHierarchyNodeId());
//                wbsEntityPostDTO.setModuleHierarchyNodeId(entity.getModuleHierarchyNodeId());
                wbsEntityPostDTO.setProjectNodeId(entity.getProjectNodeId());
                wbsEntityPostDTO.setStartAt(workEntry.getStartAt());
                wbsEntityPostDTO.setFinishAt(workEntry.getFinishAt());
                wbsEntityPostDTO.setActive(true);
                wbsEntityPostDTO.setDwgShtNo(entity.getDwgShtNo());
                wbsEntityPostDTO.setWorkLoad(entity.getWorkLoad());

                String targetTaskName = bpmnTaskRelation.getNodeName();


/*                if (variable instanceof WeldEntity) {


                    if (PMI_TASK_NAME.equals(targetTaskName)) {
                        wbsEntityPostDTO.setProportion(((WeldEntity) variable).getPmiRatio());



                    } else if (NDT_TASK_NAME.equals(targetTaskName)) {
                        wbsEntityPostDTO.setProportion(((WeldEntity) variable).getNdeRatio());


                    }



                    if (wbsEntityPostDTO.getProportion() != null
                        && wbsEntityPostDTO.getProportion() == 0) {
                        wbsEntityPostDTO.setActive(false);
                    }

                }*/


                return planBusiness.addEntity(
                    operator,
                    entity.getProjectId(),
                    workEntry,
                    wbsEntityPostDTO,
                    entityIndex,
                    entity.getProcessId()
                );

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成四级计划前置任务关系。
     *
     * @param operator                操作者信息
     * @param moduleProcessDefinition 模块工作流定义
     * @param batchTask               批处理任务信息
     * @param workEntries             workEntries 工作计划集合，为null则处理整个项目
     */
    private Set<Long> generateEntityProcessPredecessorRelations(
        final OperatorDTO operator,
        final ModuleProcessDefinition moduleProcessDefinition,
        final BatchTask batchTask,
        final Set<WBSEntry> workEntries,
        final BatchResultDTO batchResult
    ) {
        int pageNo = 0;
        Set<Long> workWbsEntryIds = new HashSet<>();

        for (WBSEntry workEntry : workEntries) {


            do {
                if (batchTask != null) {
                    batchTask.setLastModifiedAt();
                    batchTaskRepository.save(batchTask);
                }

                List<WBSEntry> wbsEntries = wbsEntryRepository.findByProjectIdAndParentIdAndTypeAndActiveIsTrueAndDeletedIsFalse(
                    moduleProcessDefinition.getProjectId(),
                    workEntry.getId(),
                    WBSEntryType.ENTITY,
                    PageRequest.of(pageNo, BATCH_FETCH_SIZE));

                wbsEntries.forEach(wbsEntry -> {
                        workWbsEntryIds.add(wbsEntry.getParentId());
                        planRelationService.
                            generateEntityProcessPredecessorRelations(
                                operator,
                                moduleProcessDefinition,
                                wbsEntry
                            );

                        batchResult.addProcessedRelationCount(1);

                        if (batchResult.getProcessedRelationCount() % 10 == 0
                            && batchTask.checkRunningStatus()
                        ) {
                            batchTask.setResult(batchResult);
                            batchTask.setLastModifiedAt();
                            batchTaskRepository.save(batchTask);
                        }
                    }
                );


                if (wbsEntries.size() < BATCH_FETCH_SIZE) {
                    break;
                }

                pageNo++;
            } while (true);
        }
        return workWbsEntryIds;
    }

    /**
     * 生成四级计划前置任务关系。
     *
     * @param operator                 操作者信息
     * @param projectId                项目 ID
     * @param moduleProcessDefinitions 模块工作流定义映射表（模块类型: 工作流定义）
     * @param batchTask                批处理任务信息
     * @param workEntryMap             func_part-> WorkEntries 对应的要生成 任务的 WBS工作节点// 生成此模块范围的前置任务，还是整个项目的前置任务，如果为null生成整个项目的
     */
    private Set<Long> generateEntityProcessPredecessorRelations(
        final OperatorDTO operator,
        final Long projectId,
        final Map<String, ModuleProcessDefinition> moduleProcessDefinitions,
        final BatchTask batchTask,
        final Map<String, Set<WBSEntry>> workEntryMap,
        final BatchResultDTO batchResult
    ) {
        if (batchTask != null) {
            batchTask.setProgressText("generating WBS predecessor/successor relations");
            batchTaskRepository.save(batchTask);
        }


        Set<Long> workWbsEntryIds = new HashSet<>();
        moduleProcessDefinitions.forEach((moduleType, moduleProcessDefinition)
            -> {
            workWbsEntryIds.addAll(generateEntityProcessPredecessorRelations(operator, moduleProcessDefinition, batchTask, workEntryMap.get(moduleProcessDefinition.getFuncPart()), batchResult));
        });

        if (batchTask != null) {
            batchTask.setProgressText("deleting redundant WBS predecessor/successor relations");
            batchTaskRepository.save(batchTask);
        }


        workWbsEntryIds.forEach(workWbsEntryId -> {
            List<String> guids = wbsEntryRepository.findGuidByWorkWbsEntryId(workWbsEntryId);
            guids.forEach(guid -> {
                wbsEntryRelationRepository.getRedundantRelations(projectId, guid).forEach(wrId -> {
                    wbsEntryRelationRepository.deleteById(wrId.longValue());

                });
            });
        });


        return workWbsEntryIds;
    }


    /**
     * 针对新增加的实体，增量生成四级计划前置任务关系。
     *
     * @param operator                 操作者信息
     * @param projectId                项目 ID
     * @param moduleProcessDefinitions 模块工作流定义映射表（模块类型: 工作流定义）
     * @param batchTask                批处理任务信息
     * @param entities                 新增加的实体
     */
    private void generateEntityProcessPredecessorRelationsFromEntity(
        final OperatorDTO operator,
        final Long projectId,
        final Map<Long, ModuleProcessDefinition> moduleProcessDefinitions,
        final BatchTask batchTask,
        final List<WBSEntryExecutionHistory> entities
    ) {
        if (batchTask != null) {
            batchTask.setProgressText("generating additional WBS predecessor/successor relations");
            batchTaskRepository.save(batchTask);
        }


        Set<Long> entityIds = new HashSet<>();
        for (WBSEntryExecutionHistory entity : entities) {
            entityIds.add(entity.getEntityId());
        }


        List<Long> parentEntityIds = wbsEntryRepository.getParentIds(projectId, entityIds);

        List<Long> sonEntityIds = wbsEntryRepository.getSonIds(projectId, entityIds);

        for (Long entityId : parentEntityIds) {
            if (entityId != null) {
                entityIds.add(entityId);
            }
        }

        for (Long entityId : sonEntityIds) {
            if (entityId != null) {
                entityIds.add(entityId);
            }
        }


        wbsEntryRepository.deleteRelationsByProjectIdAndEntityIdInPre(projectId, entityIds);
        wbsEntryRepository.deleteRelationsByProjectIdAndEntityIdInSuf(projectId, entityIds);


        moduleProcessDefinitions.forEach((moduleType, moduleProcessDefinition)
            -> {

            int pageNo = 0;

            do {
                if (batchTask != null) {
                    batchTask.setLastModifiedAt();
                    batchTaskRepository.save(batchTask);
                }

                List<WBSEntry> wbsEntries;

                wbsEntries = wbsEntryRepository
                    .findByProjectIdAndFuncPartAndTypeAndEntityIdInAndActiveIsTrueAndDeletedIsFalse(
                        moduleProcessDefinition.getProjectId(),
                        moduleProcessDefinition.getFuncPart(),
                        WBSEntryType.ENTITY,
                        entityIds,
                        PageRequest.of(pageNo, BATCH_FETCH_SIZE, Sort.by(Sort.Order.desc("id")))
                    );

                wbsEntries.forEach(wbsEntry -> planRelationService.generateEntityProcessPredecessorRelations(
                    operator,
                    moduleProcessDefinition,
                    wbsEntry
                ));

                if (wbsEntries.size() < BATCH_FETCH_SIZE) {
                    break;
                }

                pageNo++;
            } while (true);
        });

        if (batchTask != null) {
            batchTask.setProgressText("deleting redundant WBS predecessor/successor relations");
            batchTaskRepository.save(batchTask);
        }


        wbsEntryRelationRepository.getRedundantRelations(projectId).forEach(wrId -> {
            wbsEntryRelationRepository.deleteById(wrId.longValue());
        });
    }


    /**
     * 重新生成四级计划前后置关系。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @Override
    public void regenerateWBSEntryRelations(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId
    ) {
        final Map<String, ModuleProcessDefinition> moduleProcessDefinitions = new HashMap<>();

        moduleProcessDefinitionRepository
            .findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId)
            .forEach(moduleProcessDefinition -> moduleProcessDefinitions.put(
                moduleProcessDefinition.getFuncPart(),
                moduleProcessDefinition
            ));

        if (moduleProcessDefinitions.size() == 0) {
            throw new BusinessError("error.module-workflow.no-module-process-defined");
        }
        List<WBSEntry> workEntries = wbsEntryRepository.findByProjectIdAndTypeAndActiveIsTrueAndDeletedIsFalse(
            projectId,
            WORK
        );
        Map<String, Set<WBSEntry>> workEntryMap = new HashMap<>();
        workEntries.forEach(workEntry -> {
            workEntryMap.computeIfAbsent(workEntry.getFuncPart(), k -> new HashSet<>()).add(workEntry);
        });

        Long version = new Date().getTime();
        Set<Long> wbsEntryRelationToBeDeletedIds = generateEntityProcessPredecessorRelations(operator, projectId, moduleProcessDefinitions, null, workEntryMap, null);


        for (Long workWbsId : wbsEntryRelationToBeDeletedIds) {
            WBSEntry wrr = wbsEntryRepository.findById(workWbsId.longValue()).orElse(null);
            if (wrr == null) continue;
            ;
            Set<BigInteger> pWbsEntryRelations = wbsEntryRelationRepository.findAllByTaskGUID(projectId, wrr.getGuid());
//            Set<BigInteger> sWbsEntryRelations = wbsEntryRelationRepository.findSGuidsLessThanVersion(orgId, projectId, workWbsId, version);
            if (pWbsEntryRelations == null) pWbsEntryRelations = new HashSet<>();
//            pWbsEntryRelations.addAll(sWbsEntryRelations);
//            Set<BigInteger> werIds = wbsEntryRelationRepository.findGuidsLessThanVersion(orgId, projectId, workWbsId, version);
            if (!CollectionUtils.isEmpty(pWbsEntryRelations)) {
                pWbsEntryRelations.forEach(werId -> {
                    wbsEntryRelationRepository.deleteById(werId.longValue());
                });
            }
        }
    }


    /**
     * 自动绑定实体资源。
     *
     * @param batchTask    批处理任务信息
     * @param operator     操作者信息
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param wbsEntryID   WBS 条目 ID
     * @param moduleUpdate 更新此模块，boolean. ftj
     * @return 批处理任务执行结果
     */
    @Override
    public BatchResultDTO generateEntityProcessWBSEntries(
        final BatchTask batchTask,
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long wbsEntryID,
        final boolean moduleUpdate
    ) {

        final BatchResultDTO batchResult = new BatchResultDTO(batchTask);
        final Set<Long> taskPackagesIds = new HashSet<>();
        WBSEntry we = wbsEntryRepository.findById(wbsEntryID).orElse(null);
        String moduleName = null;
        String funcPart = null;
        if (we != null) {
            moduleName = we.getSector();
            funcPart = we.getFuncPart();
        }
        WBSEntryBlob weBlob = wbsEntryBlobRepository.findByWbsEntryId(we.getId());
        if (weBlob == null) {
            throw new NotFoundError(we.getId().toString());
        }
        final Set<Long> workLoadWbsIds = new HashSet<>();


        /*----------------------------------------------------------------------
           1. 取得项目的所有类型模块的工作流定义，
              以获得工作流定义中【工序/实体类型】对应的节点 ID 以及节点的前置任务节点；
         ---------------------------------------------------------------------*/

        final Map<String, ModuleProcessDefinition> moduleProcessDefinitions = new HashMap<>();

        List<ModuleProcessDefinition> moduleProcessDefinitionSet = new ArrayList<>();
        if (funcPart != null) {
            moduleProcessDefinitionSet.add(moduleProcessDefinitionRepository.
                findByOrgIdAndProjectIdAndFuncPartAndDeletedIsFalse(orgId, projectId, funcPart));
        } else {
            moduleProcessDefinitionSet = moduleProcessDefinitionRepository
                .findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId);
        }

        if (CollectionUtils.isEmpty(moduleProcessDefinitionSet)) {
            throw new BusinessError("error.module-workflow.no-module-process-defined");
        }
        moduleProcessDefinitionSet.forEach(moduleProcessDefinition -> moduleProcessDefinitions.put(
            moduleProcessDefinition.getFuncPart(),
            moduleProcessDefinition
        ));


        String wbsEntryStr = "";

        Long version = new Date().getTime();
        if (we != null) wbsEntryStr = we.getName();

        batchTask.setProgressText(wbsEntryStr + "importing WBS entries");
        if (we == null) {
            System.out.println(weBlob.getWbs());
        }
        batchTask.setName(we == null ? null : weBlob.getWbs());

        batchTaskRepository.save(batchTask);

        final Set<Long> wbsEntryProcessedIDs = new HashSet<>();
        final Map<String, Set<WBSEntry>> workEntryMap = new HashMap<>();


        wbsEntryRepository.findByProjectIdAndIdInOrderByDepthAsc(
            projectId,
            new HashSet<Long>() {{
                add(wbsEntryID);
            }}
        ).forEach(wbsEntry -> {


            WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntry.getId());
            if ((new HashSet<>(StringUtils.findAll(planBusiness.ENTITY_ID_PATTERN, wbsEntryBlob.getPath())))
                .removeAll(wbsEntryProcessedIDs)) {
                return;
            }

            wbsEntryProcessedIDs.add(wbsEntry.getId());

            String parentLikeCondition = String.format(
                "%s%s/%%",
                wbsEntryBlob.getPath(),
                wbsEntry.getId()
            );

            /*------------------------------------------------------------------
               2. 清除指定 WBS 条目下的所有四级计划（类型为【ENTITY】）的条目； 按照 WORK的 WBS_ENTRY来清理 TODO
             -----------------------------------------------------------------*/











            /*------------------------------------------------------------------
               3. 取得指定 WBS 条目下的所有三级计划（类型为【WORK】）的条目；
             -----------------------------------------------------------------*/

            final Map<String, Set<HierarchyNode>> parentHierarchyNodesMap = new HashMap<>();

            wbsEntryRepository
                .findDescentWorksByProjectIdAndParentId(projectId, wbsEntry.getId())
                .forEach(workEntry -> {
                    workEntryMap.computeIfAbsent(workEntry.getFuncPart(), k -> new HashSet<>()).add(workEntry);

                    if (!workEntry.isFuncPartSupported()) {
                        return;
                    }

                    String wbsFuncPart = workEntry.getFuncPart();


                    if (StringUtils.isEmpty(workEntry.getProcess())) {
                        throw new BusinessError("error.wbs.wbs-invalid", new String[]{wbsEntryBlob.getWbs()});
                    }

                    /* -----------------
                        3.0 删除 WBS_ENTRY (WORK) 对应 于 wbs_entry_relation 表中的 所有记录 precedessor / successor
                     */

                    /*----------------------------------------------------------
                       3.1 取得四级计划所在区域的层级节点的 ID，以供后续取得其下所有实体信息；
                     ---------------------------------------------------------*/

                    String parentHierarchyNodeNo = workEntry.getLayerPackage() != null
                        ? workEntry.getLayerPackage()
                        : workEntry.getSector();

                    Set<HierarchyNode> parentHierarchyNodes = parentHierarchyNodesMap.get(parentHierarchyNodeNo);

                    if (CollectionUtils.isEmpty(parentHierarchyNodes) && workEntry.getFuncPart() != null) {
                        parentHierarchyNodes = hierarchyRepository
                            .findByNoAndProjectIdAndHierarchyTypeInAndDeletedIsFalse(
                                parentHierarchyNodeNo,
                                projectId,
                                funcPartHierarchyTypeMap.get(workEntry.getFuncPart())
                            );

                        parentHierarchyNodesMap.put(parentHierarchyNodeNo, parentHierarchyNodes);
                    }

                    if (CollectionUtils.isEmpty(parentHierarchyNodes)) {
                        parentHierarchyNodes = hierarchyRepository.
                            findByNoAndProjectIdAndHierarchyTypeInAndDeletedIsFalse(
                                parentHierarchyNodeNo,
                                projectId,
                                AREA_HIERARCHY_TYPES
                            );
                        if (CollectionUtils.isEmpty(parentHierarchyNodes))
                            return;

                    }

                    /*----------------------------------------------------------
                       3.2 取得四级计划所在模块的类型，以供后续取得模块工作流定义；
                     ---------------------------------------------------------*/

//                    ProjectNodeModuleType projectNodeModuleType = projectNodeModuleTypeRepository
//                        .findById(parentHierarchyNodes.iterator().next().getId())
//                        .orElse(null);
//
//                    if (projectNodeModuleType == null) {
//                        throw new BusinessError("error.module-workflow.module-type-not-defined");
//                    }

                    /*----------------------------------------------------------
                       3.3 取得四级计划所在模块的工作流定义，以供后续演算工作流，判断是否需要生成实体的四级计划；
                     ---------------------------------------------------------*/

                    ModuleProcessDefinition moduleProcessDefinition = moduleProcessDefinitions
                        .get(workEntry.getFuncPart());

                    if (moduleProcessDefinition == null) {
                        throw new BusinessError("error.module-workflow.not-deployed");
                    }

                    Set<ModuleProcessDefinition> sysModuleProcessDefinition = new HashSet<>();
                    for (ModuleProcessDefinition mpd : moduleProcessDefinitions.values()) {
//                        if(Arrays.asList().contains(mpd.getFuncPart())) {
//                            if(FuncPart.SUPPORTED_FUNC_PARTS.contains(mpd.getFuncPart())) {
                        sysModuleProcessDefinition.add(mpd);
//                        }
                    }


                    /*----------------------------------------------------------
                       3.4 取得三级计划下所有四级计划实体的项目节点 ID；
                           对每一个实体的工序，启动模块工作流，演算相应的部分，以判断是否需要生成四级计划；
                     ---------------------------------------------------------*/

                    List<Long> entityIDs = wbsEntryRepository
                        .findEntityIDsByParentId(projectId, workEntry.getId(), RUNNING_STATUS_CANNOT_BE_REMOVED);

                    WBSEntry lastChild = wbsEntryRepository
                        .findFirstByParentIdAndTypeAndDeletedIsFalseOrderBySortDesc(workEntry.getId(), ENTITY)
                        .orElse(null);

                    final AtomicInteger entityIndex = new AtomicInteger(lastChild == null ? 0 : lastChild.getSortNo());

                    workLoadWbsIds.add(workEntry.getId());

                    /* --------------------------------------------
                        3.5 取得所有待生成四级计划的 【实体】-【工序阶段】-【工序】的清单
                     ------------------------------------------------------------------*/

                    List<ProcessEntity> processEntities = new ArrayList<>();
                    parentHierarchyNodes.forEach(parentHierarchyNode -> {
                        Set<String> supportedHierarcyTypes = FUNC_PART_HIERARCHY_MAP.get(wbsFuncPart);
                        if (supportedHierarcyTypes == null || !supportedHierarcyTypes.contains(parentHierarchyNode.getHierarchyType())) {
                            return;
                        }
                        List<ProcessEntity> prEns = getProcessEntities(
                            workEntry.getProjectId(),
                            workEntry.getStage(),
                            workEntry.getProcess(),
                            parentHierarchyNode.getId(),
                            workEntry.getFuncPart()
                        );
                        if (!CollectionUtils.isEmpty(prEns)) {
                            processEntities.addAll(prEns);
                        }
                    });
                    processEntities.forEach(entity -> {


                        if (entityIDs.indexOf(entity.getEntityId()) >= 0) {
                            return;
                        }

                        entityIndex.getAndIncrement();
                        batchResult.addTotalCount(1);

                        if (moduleProcessDefinition != null) {
                            generateEntityProcessWBSEntry(
                                operator,
                                moduleProcessDefinition,
                                workEntry,
                                entity,
                                entityIndex.get()
                            );
                        } else {

                            sysModuleProcessDefinition.forEach(mpd -> generateEntityProcessWBSEntry(
                                operator,
                                mpd,
                                workEntry,
                                entity,
                                entityIndex.get()
                            ));
                        }

                        entityIDs.add(entity.getEntityId());
                        batchResult.addProcessedCount(1);

                        if (batchResult.getProcessedCount() % 10 == 0
                            && batchTask.checkRunningStatus()
                        ) {
                            batchTask.setResult(batchResult);
                            batchTask.setLastModifiedAt();
                            batchTaskRepository.save(batchTask);
                        }
                    });

                    // 处理计划未关联任务包的数据
                    List<BigInteger> taskPackageIds = null;
                    BpmProcess wBpmProcess = processService.getBpmProcess(projectId, workEntry.getStage(), workEntry.getProcess());
                    if (wBpmProcess != null) {
                        taskPackageIds = taskPackageRepository.
                            getTaskPackageId(projectId, workEntry.getEntityId());
                    }
                    if (!CollectionUtils.isEmpty(taskPackageIds)) {
                        BigInteger taskPackageId = taskPackageIds.get(0);

                        WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntryID);
                        //更新 工作包信息
                        wbsEntryState.setTaskPackageId(taskPackageId.longValue());
                        taskPackageService.addTaskPackageCount(taskPackageId.longValue(), wbsEntryState.getWorkLoad());
                        WBSEntryTeamWorkSiteDTO wbsEntryTeamWorkSiteDTO = todoTaskBaseService.getWBSEntryTeamWorkSiteInfo(null, projectId,
                            workEntry, wbsEntryState, workEntry.getProcessId());
                        if (wbsEntryTeamWorkSiteDTO != null) {
                            wbsEntryState.setWorkSiteId(wbsEntryTeamWorkSiteDTO.getWorkSiteId());
                            wbsEntryState.setWorkSiteName(wbsEntryTeamWorkSiteDTO.getWorkSiteName());
                            wbsEntryState.setTeamId(wbsEntryTeamWorkSiteDTO.getTeamId());
                            wbsEntryState.setTeamName(wbsEntryTeamWorkSiteDTO.getTeamName());
                            wbsEntryState.setTeamPath(wbsEntryTeamWorkSiteDTO.getTeamPath());
                        }
                        wbsEntryStateRepository.save(wbsEntryState);

                    }

                });

        });

        if (batchResult.getProcessedCount() == 0) {
            return batchResult;
        }

        /*----------------------------------------------------------------------
           4. 信息恢复；
         ---------------------------------------------------------------------*/

        batchTask.setProgressText("restoring status, work package and delegates");
        batchTaskRepository.save(batchTask);


        List<Tuple> wBSEntryDelegateList = wbsEntryRepository.searchRepeatEntityProcessDelegates(projectId);
        deleteAllByRepeatId(projectId, wBSEntryDelegateList);


        for (Long wkWbsEntryId : workLoadWbsIds) {
            List<Tuple> deletedWbsEntryIds = wbsEntryRepository.
                findToBeDeletedWbsEntryIds(projectId, version, wkWbsEntryId, RUNNING_STATUS_CANNOT_BE_REMOVED_STR);
            if (!CollectionUtils.isEmpty(deletedWbsEntryIds)) {
                deletedWbsEntryIds.forEach(deletedWbsEntryId -> {
                    Long deletedId = ((BigInteger) deletedWbsEntryId.get("id")).longValue();
                    String guid = (String) deletedWbsEntryId.get("guid");
                    Set<BigInteger> predecessorWbses = wbsEntryRelationRepository.findAllByTaskGUID(projectId, guid);
//                    Set<BigInteger> successorWbses = wbsEntryRelationRepository.findSuccessorsByTaskGUID(projectId, guid);
                    if (predecessorWbses == null) predecessorWbses = new HashSet<>();
//                    predecessorWbses.addAll(successorWbses);
                    predecessorWbses.forEach(relationId -> {
                        wbsEntryRelationRepository.deleteById(relationId.longValue());
                    });
                    wbsEntryRepository.deleteById(deletedId);
                });
            }
        }

        wbsEntryRepository.deleteDelegateWithNoEntityProcess();

        batchTask.setProgressText("setting parent ISO/SPOOL entity IDs");
        batchTaskRepository.save(batchTask);






        /*----------------------------------------------------------------------
           6. 生成四级计划的前置任务关系；先取得 当前点击 wbs下的所有类型为work的wbsID，然后循环执行
         ---------------------------------------------------------------------*/
        version = new Date().getTime();
        Set<Long> wbsEntryRelationToBeDeletedIds = new HashSet<>();
        generateEntityProcessPredecessorRelations(operator, projectId, moduleProcessDefinitions, batchTask, workEntryMap, batchResult);
//        if (moduleUpdate && moduleName != null) {
//            wbsEntryRelationToBeDeletedIds = generateEntityProcessPredecessorRelations(operator, projectId, moduleProcessDefinitions, batchTask, moduleName, batchResult);
//        } else {
//            wbsEntryRelationToBeDeletedIds = generateEntityProcessPredecessorRelations(operator, projectId, moduleProcessDefinitions, batchTask, null, batchResult);
//        }


        for (Long workWbsId : wbsEntryRelationToBeDeletedIds) {
            WBSEntry wrr = wbsEntryRepository.findById(workWbsId.longValue()).orElse(null);
            if (wrr == null) continue;
            Set<BigInteger> pWbsEntryRelations = wbsEntryRelationRepository.findAllByTaskGUID(projectId, wrr.getGuid());
//            Set<BigInteger> sWbsEntryRelations = wbsEntryRelationRepository.findSGuidsLessThanVersion(orgId, projectId, workWbsId, version);
            if (pWbsEntryRelations == null) pWbsEntryRelations = new HashSet<>();
//            pWbsEntryRelations.addAll(sWbsEntryRelations);
            if (!CollectionUtils.isEmpty(pWbsEntryRelations)) {
                pWbsEntryRelations.forEach(werId -> {
                    wbsEntryRelationRepository.deleteById(werId.longValue());
                });
            }
        }

        batchTask.setProgressText("setting running status as pending");
        batchTaskRepository.save(batchTask);
        wbsEntryRepository.getRunningStatusAsPending(projectId, batchTask.getId()).forEach(pendingWbsId -> {
            wbsEntryStateRepository.updateRunningStatusById(projectId, WBSEntryRunningStatus.PENDING, pendingWbsId.longValue());
        });


        // 针对焊口两个零件在计划生成后才挂置的焊口查询计划状态并变更
//        RepairWbsSearchDTO repairWbsSearchDTO = new RepairWbsSearchDTO();
//        repairWbsSearchDTO.setProcess("FITUP");
//        repairWbsSearchDTO.setFetchAll(true);
//        Page<RepairWbsListDTO> unUpdatedWbsEntries = repairDataRepository.searchUndoWbs(
//            orgId,
//            projectId,
//            repairWbsSearchDTO);
//        if (unUpdatedWbsEntries.getContent().size() > 0) {
//            Set<Long> wbsEntryIds = new HashSet<>();
//            for(RepairWbsListDTO wbsEntry : unUpdatedWbsEntries) {
//                if (wbsEntry.getWbsEntryId() != null) {
//                    wbsEntryIds.add(wbsEntry.getWbsEntryId());
//                }
//            }
//
//            List<WBSEntryState> wbsEntryStateList = wbsEntryStateRepository.findByProjectIdAndWbsEntryIdIn(projectId, wbsEntryIds);
//
//            if (wbsEntryStateList.size() > 0) {
//                for (WBSEntryState wbsEntryState : wbsEntryStateList) {
//                    wbsEntryState.setRunningStatus(WBSEntryRunningStatus.PENDING);
//                }
//                wbsEntryStateRepository.saveAll(wbsEntryStateList);
//            }
//        }


        /*----------------------------------------------------------------------
           7. 更新项目信息，返回执行结果。
         ---------------------------------------------------------------------*/

        batchTask.setProgressText("setting work load");
        batchTaskRepository.save(batchTask);
        if (!CollectionUtils.isEmpty(workLoadWbsIds)) {
            updateWorkLoad(projectId, workLoadWbsIds);
        }


        batchTask.setProgressText("updating project's revision");
        batchTaskRepository.save(batchTask);

        Project project = projectRepository.findById(projectId).orElse(null);


        if (project != null) {
            project.setLastModifiedBy(operator.getId());
            project.setLastModifiedAt(new Date());
            projectRepository.save(project);
        }

        batchTask.setProgressText("finished");
        batchTaskRepository.save(batchTask);

        return batchResult;
    }

    private void deleteAllByRepeatId(Long projectId, List<Tuple> wbsEntryDelegateList) {
//         wbs_entry_id wbsEntryId, privilege, cnt, mid, gid
        wbsEntryDelegateList.forEach(wbsEntryDelegate -> {
            BigInteger mid = (BigInteger) wbsEntryDelegate.get("mid");
            String gid = (String) wbsEntryDelegate.get("gid");
            if (StringUtils.isEmpty(gid)) return;
            Long lmid = mid.longValue();
            List<Long> gids = LongUtils.change2LongArr(gid, ",");
            for (Long id : gids) {
                if (id.equals(lmid)) continue;
                wBSEntryDelegateRepository.deleteById(id);
            }

        });
    }

    /**
     * 更新由于实体修改导致的计划变动。
     *
     * @param batchTask 批处理任务信息
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 批处理任务执行结果
     */
    @Override
    public BatchResultDTO generateWBSEntryFromEntity(BatchTask batchTask, OperatorDTO operator, Long orgId, Long projectId) {
        /*
        1. 取得模块列表
        2. 对每个模块，取得所有的 实体列表
        3. 对每个实体，根据实体类型、子类型 取得 实体类型-工序 列表
        4. 对应每个 实体类型-工序， 取得 对应的 三级计划（work）类型计划，找到最后一个四级计划的 sort 号，
        5. 插入四级计划
        6. 对于此模块 重新生成前置任务
         */
        final BatchResultDTO batchResult = new BatchResultDTO(batchTask);

        final Set<Long> workLoadWbsIds = new HashSet<>();


        List<WBSEntryExecutionHistory> entities = wbsEntryExecutionHistoryRepository
            .findByProjectIdAndExecutionStateOrderByModuleId(projectId, WBSEntryExecutionState.UNDO);
        Set<Long> moduleIds = new HashSet<>();
        Map<Long, List<WBSEntryExecutionHistory>> moduleEntitiesMap = new HashMap<>();
        Map<Long, String> moduleTypeMap = new HashMap<>();
        Map<Long, String> moduleNameMap = new HashMap<>();


        for (WBSEntryExecutionHistory entity : entities) {
            moduleIds.add(entity.getModuleId());
            moduleTypeMap.put(entity.getModuleId(), entity.getModuleType());
            moduleNameMap.put(entity.getModuleId(), entity.getModule());
        }


        for (Long moduleId : moduleIds) {
            List<WBSEntryExecutionHistory> entityList = new ArrayList<>();
            for (WBSEntryExecutionHistory entity : entities) {
                if (moduleId.equals(entity.getModuleId())) {
                    entityList.add(entity);
                }
            }
            moduleEntitiesMap.put(moduleId, entityList);
        }

        Map<Long, ModuleProcessDefinition> moduleProcessDefinitionMap = new HashMap<>();


        for (Long moduleId : moduleIds) {

            ModuleProcessDefinition moduleProcessDefinition = moduleProcessDefinitionRepository
                .findFirstByOrgIdAndProjectIdAndFuncPartAndDeletedIsFalseOrderByVersionDesc(
                    orgId,
                    projectId,
                    moduleTypeMap.get(moduleId));
            if (moduleProcessDefinition == null) {
                logger.info("there is no module process definition");
                break;
            }


            List<WBSEntryExecutionHistory> moduleEntities = moduleEntitiesMap.get(moduleId);
            for (WBSEntryExecutionHistory ehEntity : moduleEntities) {

                ehEntity.setExecutionState(WBSEntryExecutionState.DOING);


                List<ProcessEntityType> petList = processEntityTypeRepository.
                    findByOrgIdAndProjectIdAndEntityTypeAndEntitySubType(
                        orgId,
                        projectId,
                        ehEntity.getEntityType(),
                        ehEntity.getEntitySubType());


                for (ProcessEntityType pet : petList) {

                    List<WBSEntry> wbsEntries = wbsEntryRepository
                        .findByOrgIdAndProjectIdAndStageAndProcessAndTypeAndSectorAndLayerPackageAndActiveIsTrueAndDeletedIsFalse(
                            orgId,
                            projectId,
                            pet.getStageName(),
                            pet.getProcessName(),
                            WBSEntryType.WORK,
                            ehEntity.getModule(),
                            ehEntity.getLayer()
                        );

                    if (CollectionUtils.isEmpty(wbsEntries)) {
                        wbsEntries = wbsEntryRepository
                            .findByOrgIdAndProjectIdAndStageAndProcessAndTypeAndSectorAndActiveIsTrueAndDeletedIsFalse(
                                orgId,
                                projectId,
                                pet.getStageName(),
                                pet.getProcessName(),
                                WBSEntryType.WORK,
                                ehEntity.getModule()
                            );
                    }
                    if (CollectionUtils.isEmpty(wbsEntries) || wbsEntries.size() > 1) {
                        continue;

                    }

                    WBSEntry workEntry = wbsEntries.get(0);

                    workLoadWbsIds.add(workEntry.getId());

                    String parentHierarchyNodeNo = workEntry.getLayerPackage() != null
                        ? workEntry.getLayerPackage()
                        : workEntry.getSector();

                    Set<HierarchyNode> parentHierarchyNodes = hierarchyRepository
                        .findByNoAndProjectIdAndHierarchyTypeInAndDeletedIsFalse(
                            parentHierarchyNodeNo,
                            projectId,
                            AREA_HIERARCHY_TYPES
                        );

                    if (CollectionUtils.isEmpty(parentHierarchyNodes)) {

                        parentHierarchyNodes = hierarchyRepository.
                            findByNoAndProjectIdAndHierarchyTypeInAndDeletedIsFalse(
                                parentHierarchyNodeNo,
                                projectId,
                                AREA_HIERARCHY_TYPES
                            );
                        if (CollectionUtils.isEmpty(parentHierarchyNodes))
                            continue;
                    }


                    WBSEntry lastChild = wbsEntryRepository
                        .findFirstByParentIdAndTypeAndDeletedIsFalseOrderBySortDesc(workEntry.getId(), ENTITY)
                        .orElse(null);
                    final AtomicInteger entityIndex = new AtomicInteger(lastChild == null ? 0 : lastChild.getSortNo());

                    parentHierarchyNodes.forEach(parentHierarchyNode -> {

                        ProcessEntity entity = processEntityRepository
                            .findFirstByProjectIdAndStageAndProcessAndEntityIdAndHierarchyType(
                                workEntry.getProjectId(),
                                workEntry.getStage(),
                                workEntry.getProcess(),
                                ehEntity.getEntityId(),
                                parentHierarchyNode.getHierarchyType()
                            );

                        if (entity != null) {
                            batchResult.addTotalCount(1);
                            entityIndex.getAndIncrement();

                            generateEntityProcessWBSEntry(
                                operator,
                                moduleProcessDefinition,
                                workEntry,
                                entity,
                                entityIndex.get()
                            );

                        }
                    });

                }

                ehEntity.setExecutionState(WBSEntryExecutionState.DONE);
                wbsEntryExecutionHistoryRepository.save(ehEntity);

            }


            moduleProcessDefinitionMap.put(moduleId, moduleProcessDefinition);

        }


        if (!CollectionUtils.isEmpty(entities) && !moduleProcessDefinitionMap.isEmpty())
            generateEntityProcessPredecessorRelationsFromEntity(
                operator,
                projectId,
                moduleProcessDefinitionMap,
                batchTask,
                entities
            );


        batchTask.setProgressText("setting parent ISO/SPOOL entity IDs");
        batchTaskRepository.save(batchTask);


        wbsEntryRepository.getRunningStatusAsPending(projectId, batchTask.getId()).forEach(pendingWbsId -> {
            wbsEntryStateRepository.updateRunningStatusById(projectId, WBSEntryRunningStatus.PENDING, pendingWbsId.longValue());
        });




        /*----------------------------------------------------------------------
           7. 更新项目信息，返回执行结果。
         ---------------------------------------------------------------------*/

        if (!CollectionUtils.isEmpty(workLoadWbsIds)) {
            updateWorkLoad(projectId, workLoadWbsIds);
        }

        batchTask.setProgressText("updating project's revision");
        batchTaskRepository.save(batchTask);

        Project project = projectRepository.findById(projectId).orElse(null);


        if (project != null) {
            project.setLastModifiedBy(operator.getId());
            project.setLastModifiedAt(new Date());
            projectRepository.save(project);
        }

        batchTask.setProgressText("finished");
        batchTaskRepository.save(batchTask);

        return batchResult;
    }

    private void updateWorkLoad(Long projectId, Set<Long> workLoadWbsIds) {

        Set<Long> parentWorkLoadWbsIds;
        workLoadWbsIds.forEach(wlId -> {
            Double wl = wbsEntryRepository.getWbsWorkLoad(wlId, projectId);

            wbsEntryStateRepository.updateWorkLoad(wlId, wl);
        });

        parentWorkLoadWbsIds = wbsEntryRepository.getParentWorkLoadIds(workLoadWbsIds, projectId);
        if (parentWorkLoadWbsIds == null || parentWorkLoadWbsIds.size() == 0) {
            return;
        }

        updateWorkLoad(projectId, parentWorkLoadWbsIds);

    }


    /**
     * 检查实体对应的四级计划状态，确定实体是否能够合并或拆分
     * 如果可以修改 返回 "OK" ，不能修改 返回原因字符串
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entityId  实体ID
     * @return
     */
    @Override
    public String checkChange(
        Long orgId,
        Long projectId,
        Long entityId
    ) {

        List<WBSEntryState> wbsEntryStates = wbsEntryStateRepository.
            findByProjectIdAndEntityId(
                projectId, entityId);
        for (WBSEntryState wbsEntryState : wbsEntryStates) {
            if (RUNNING_STATUS_CANNOT_BE_REMOVED.contains(wbsEntryState.getRunningStatus())) {
                return wbsEntryState.getEntityId() + " entityId can not be changed due to running status is  " + wbsEntryState.getRunningStatus().name();
            }
        }

        return "OK";
    }

    /**
     * 检查实体对应的四级计划状态，确定实体是否能够合并或拆分
     * 如果可以修改 返回 "OK" ，不能修改 返回原因字符串
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entityId  实体ID
     * @return boolean
     */
    @Override
    @Transactional
    public boolean deleteWBSEntries(
        Long orgId,
        Long projectId,
        Long entityId
    ) {

        List<WBSEntryState> wbsEntries = wbsEntryStateRepository.
            findByProjectIdAndEntityId(
                projectId, entityId);
        Set<Long> entryIds = new HashSet<>();
        for (WBSEntryState wbsEntry : wbsEntries) {
            if (RUNNING_STATUS_CANNOT_BE_REMOVED.contains(wbsEntry.getRunningStatus())) {
                return false;
            } else {
                entryIds.add(wbsEntry.getId());
            }
        }

        if (!CollectionUtils.isEmpty(entryIds)) {
            wbsEntryRepository.deleteRelationsByOrgIdAndProjectIdAndIdInPre(orgId, projectId, entryIds);
            wbsEntryRepository.deleteRelationsByOrgIdAndProjectIdAndIdInSuf(orgId, projectId, entryIds);
            wbsEntryRepository.deleteByOrgIdAndProjectIdAndIdIn(orgId, projectId, entryIds);
        }

        return true;
    }


    /**
     * 取得所有待生成四级计划的 【实体】-【工序阶段】-【工序】的清单
     *
     * @param projectId           项目ID
     * @param stage               工序阶段
     * @param process             工序
     * @param hierarchyAncestorId 父级节点路径
     * @param funcPart            功能分块
     * @return 工序实体列表
     */
    private List<ProcessEntity> getProcessEntities(
        Long projectId,
        String stage,
        String process,
        Long hierarchyAncestorId,
        String funcPart) {


        String parentNodePath = null;

        List<ProcessEntity> processEntities = new ArrayList<>();
        processEntities = processEntityRepository
            .findByProjectIdAndHierarchyAncestorIdAndStageAndProcessAndFuncPartOrderByProjectNodeNoAscHierarchyDepthDesc(
                projectId,
                hierarchyAncestorId,
                stage,
                process,
                funcPart
            );
        return processEntities;
    }


    /**
     * 取得所有待生成四级计划的 【实体】-【工序阶段】-【工序】的清单
     *
     * @param projectId           项目ID
     * @param stage               工序阶段
     * @param process             工序
     * @param hierarchyAncestorId 父级节点路径
     * @param funcPart            功能分块
     * @return 工序实体列表
     */
    private List<ProcessEntity> getAddedProcessEntities(
        Long projectId,
        String stage,
        String process,
        Long hierarchyAncestorId,
        String funcPart) {


        String parentNodePath = null;

        List<ProcessEntity> processEntities = new ArrayList<>();
        switch (funcPart.toUpperCase()) {
            case ("PIPING"):
                processEntities = processEntityRepository
                    .findAddedProcessEntities(
                        projectId,
                        stage,
                        process,
                        hierarchyAncestorId
                    );
                break;
            case ("STRUCTURE"):
                List<StructureProcessEntity> structureProcessEntities = new ArrayList<>();
//                    structureProcessEntityRepository
//                    .findAddedProcessEntities(
//                        projectId,
//                        stage,
//                        process,
//                        hierarchyAncestorId
//                    );
                processEntities = BeanUtils.convertType(structureProcessEntities, ProcessEntity.class);
                break;
            default:
            case ("ELECTRICAL"):
                List<ElectricalProcessEntity> electricalProcessEntities = new ArrayList<>();
//                    electricalProcessEntityRepository
//                        .findByProjectIdAndStageAndProcessAndHierarchyPathLikeOrderByProjectIdAscHierarchyDepthDesc(
//                            projectId,
//                            stage,
//                            process,
//                            parentNodePath
//                        );
                processEntities = BeanUtils.convertType(electricalProcessEntities, ProcessEntity.class);

                break;
        }
        return processEntities;
    }


    /**
     * 对新增加的 实体生成四级计划 及相互关系
     *
     * @param batchTask  batchTask
     * @param operator   operator
     * @param orgId      orgId
     * @param projectId  projectId
     * @param wbsEntryID wbsEntryID
     *                   <p>
     *                   1.0 找到所有需要重新生成 四级计划实体 对应的模块清单
     *                   2.0 根据模块清单 对应的模块类型，专业，找到 对应的 大流程部署清单
     *                   3.0 找到 所有需要生成4级计划的实体列表
     *                   4.0 对应每一种 大流程部署文件，对应的 维度（专业），找到所有需要生成四级计划的视图实体列表
     *                   5.0 设置四级计划的所属父级信息
     *                   6.0 根据3.0步生成的计划的 实体清单，找到这些实体的 父级 祖父级（必须是 ENTITY WP01等），子级，孙级 的集合
     *                   7.0 对应 6.0这些实体，找到对应的4级计划
     *                   8.0 对应6.0中的4级计划，生成前置任务
     *                   9.0 找到上述计划的 3级计划，更新工作量
     *                   10.0 设置这些4.0生成的四级计划的任务包
     * @return BatchResultDTO
     */
    @Override
    public BatchResultDTO generateWbsEntryFromAddedWbsEntity(final BatchTask batchTask,
                                                             final OperatorDTO operator,
                                                             final Long orgId,
                                                             final Long projectId,
                                                             final Long wbsEntryID) {
        final BatchResultDTO batchResult = new BatchResultDTO(batchTask);
        Boolean existErrorTask = wbsEntryLogRepository.existsByProjectIdAndResult(projectId, ActivityExecuteResult.INIT);
        if (existErrorTask != null && existErrorTask) {
            throw new BusinessError("THERE IS not HANDLED ERROR ADD TASK");
        }

        WBSEntry we = wbsEntryRepository.findById(wbsEntryID).orElse(null);
        WBSEntryBlob weBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntryID);
        String funcPart = null;
        if (we == null || weBlob == null) {
            throw new BusinessError("CAN NOT FIND wbsEntry");
        }

        funcPart = we.getFuncPart();
        Set<Long> workLoadWbsIds = new HashSet<>();
        Set<WBSEntry> workWbsEntries = new HashSet<>();

        final Set<Long> workWbsEntryIds = new HashSet<>();
        Set<Long> relatedProjectNodeIds = new HashSet<>();
        /*----------------------------------------------------------------------
           1. 取得项目的所有类型模块的工作流定义，
              以获得工作流定义中【工序/实体类型】对应的节点 ID 以及节点的前置任务节点；
         ---------------------------------------------------------------------*/
        final Map<String, ModuleProcessDefinition> moduleProcessDefinitions = new HashMap<>();

        List<ModuleProcessDefinition> moduleProcessDefinitionSet = new ArrayList<>();
        if (funcPart != null) {
            moduleProcessDefinitionSet.add(moduleProcessDefinitionRepository.
                findByOrgIdAndProjectIdAndFuncPartAndDeletedIsFalse(orgId, projectId, funcPart));
        } else {
            moduleProcessDefinitionSet = moduleProcessDefinitionRepository
                .findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId);
        }

        if (CollectionUtils.isEmpty(moduleProcessDefinitionSet)) {
            throw new BusinessError("error.module-workflow.no-module-process-defined");
        }
        moduleProcessDefinitionSet.forEach(moduleProcessDefinition -> moduleProcessDefinitions.put(
            moduleProcessDefinition.getFuncPart(),
            moduleProcessDefinition
        ));


        String wbsEntryStr = we.getName();

        batchTask.setProgressText(wbsEntryStr + "Adding WBS entries");
        batchTask.setName(weBlob.getWbs());
        batchTaskRepository.save(batchTask);


        final Set<Long> wbsEntryProcessedIDs = new HashSet<>();

        wbsEntryRepository.findByProjectIdAndIdInOrderByDepthAsc(
            projectId,
            new HashSet<Long>() {{
                add(wbsEntryID);
            }}
        ).forEach(wbsEntry -> {


            WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntry.getId());
            if (wbsEntryBlob == null) return;
            if ((new HashSet<>(StringUtils.findAll(planBusiness.ENTITY_ID_PATTERN, wbsEntryBlob.getPath())))
                .removeAll(wbsEntryProcessedIDs)) {
                return;
            }

            wbsEntryProcessedIDs.add(wbsEntry.getId());

            /*------------------------------------------------------------------
               3. 取得指定 WBS 条目下的所有三级计划（类型为【WORK】）的条目；
             -----------------------------------------------------------------*/

            final Map<String, Set<HierarchyNode>> parentHierarchyNodesMap = new HashMap<>();

            wbsEntryRepository
                .findWorksByAncestorId(wbsEntry.getId())
                .forEach(workEntry -> {


                    if (!workEntry.isFuncPartSupported()) {
                        return;
                    }

                    String wbsFuncPart = workEntry.getFuncPart();


                    if (StringUtils.isEmpty(workEntry.getProcess())) {
                        throw new BusinessError("error.wbs.wbs-invalid", new String[]{workEntry.getName()});
                    }

                    workLoadWbsIds.add(workEntry.getId());
                    /*----------------------------------------------------------
                       3.1 取得四级计划所在区域的层级节点的 ID，以供后续取得其下所有实体信息；
                     ---------------------------------------------------------*/

                    String parentHierarchyNodeNo = workEntry.getLayerPackage() != null
                        ? workEntry.getLayerPackage()
                        : workEntry.getSector();

                    Set<HierarchyNode> parentHierarchyNodes = parentHierarchyNodesMap.get(parentHierarchyNodeNo);

                    if (CollectionUtils.isEmpty(parentHierarchyNodes)) {

                        parentHierarchyNodes = hierarchyRepository
                            .findByNoAndProjectIdAndHierarchyTypeInAndDeletedIsFalse(
                                parentHierarchyNodeNo,
                                projectId,
                                AREA_HIERARCHY_TYPES
                            );

                        parentHierarchyNodesMap.put(parentHierarchyNodeNo, parentHierarchyNodes);
                    }

                    if (CollectionUtils.isEmpty(parentHierarchyNodes)) {
                        parentHierarchyNodes = hierarchyRepository.
                            findByNoAndProjectIdAndHierarchyTypeInAndDeletedIsFalse(
                                parentHierarchyNodeNo,
                                projectId,
                                AREA_HIERARCHY_TYPES
                            );
                        if (CollectionUtils.isEmpty(parentHierarchyNodes))
                            return;
                    }

                    /*----------------------------------------------------------
                       3.2 取得四级计划所在模块的类型，以供后续取得模块工作流定义；
                     ---------------------------------------------------------*/

                    ProjectNodeModuleType projectNodeModuleType = projectNodeModuleTypeRepository
                        .findById(parentHierarchyNodes.iterator().next().getId())
                        .orElse(null);

                    if (projectNodeModuleType == null) {
                        throw new BusinessError("error.module-workflow.module-type-not-defined");
                    }

                    /*----------------------------------------------------------
                       3.3 取得四级计划所在模块的工作流定义，以供后续演算工作流，判断是否需要生成实体的四级计划；
                     ---------------------------------------------------------*/

                    ModuleProcessDefinition moduleProcessDefinition = moduleProcessDefinitions
                        .get(projectNodeModuleType.getModuleType() + "-" + workEntry.getFuncPart());

                    if (moduleProcessDefinition == null && !projectNodeModuleType.getModuleType().equalsIgnoreCase("SYSTEM")) {
                        throw new BusinessError("error.module-workflow.not-deployed");
                    }

                    Set<ModuleProcessDefinition> sysModuleProcessDefinition = new HashSet<>();
                    for (ModuleProcessDefinition mpd : moduleProcessDefinitions.values()) {
                        if (Arrays.asList().contains(mpd.getFuncPart())) {
//                            if(FuncPart.SUPPORTED_FUNC_PART_STRS.contains(mpd.getFuncPart())) {
                            sysModuleProcessDefinition.add(mpd);
                        }
                    }


                    /*----------------------------------------------------------
                       3.4 取得三级计划下所有四级计划实体的项目节点 ID；
                           对每一个实体的工序，启动模块工作流，演算相应的部分，以判断是否需要生成四级计划；
                     ---------------------------------------------------------*/
                    WBSEntry lastChild = wbsEntryRepository
                        .findFirstByParentIdAndTypeAndDeletedIsFalseOrderBySortDesc(workEntry.getId(), ENTITY)
                        .orElse(null);

                    final AtomicInteger entityIndex = new AtomicInteger(lastChild == null ? 0 : lastChild.getSortNo());

                    workWbsEntryIds.add(workEntry.getId());
                    workWbsEntries.add(workEntry);
                    /* --------------------------------------------
                        3.5 取得所有待生成四级计划的 【实体】-【工序阶段】-【工序】的清单，新增加实体的
                     ------------------------------------------------------------------*/

                    List<ProcessEntity> processEntities = new ArrayList<>();
                    parentHierarchyNodes.forEach(parentHierarchyNode -> {
                        Set<String> supportedHierarcyTypes = FUNC_PART_HIERARCHY_MAP.get(wbsFuncPart);
                        if (supportedHierarcyTypes == null || !supportedHierarcyTypes.contains(parentHierarchyNode.getHierarchyType())) {
                            return;
                        }


                        List<ProcessEntity> prEns = getAddedProcessEntities(
                            workEntry.getProjectId(),
                            workEntry.getStage(),
                            workEntry.getProcess(),
                            parentHierarchyNode.getId(),
                            workEntry.getFuncPart()
                        );
                        if (!CollectionUtils.isEmpty(prEns)) {
                            processEntities.addAll(prEns);
                        }
                    });


                    Set<Long> wbsEntityIds = new HashSet<>();


                    processEntities.forEach(entity -> {
                        entityIndex.getAndIncrement();
                        batchResult.addTotalCount(1);

                        if (moduleProcessDefinition != null) {
                            WBSEntry wbsE = generateEntityProcessWBSEntry(
                                operator,
                                moduleProcessDefinition,
                                workEntry,
                                entity,
                                entityIndex.get()
                            );
                        } else {

                            sysModuleProcessDefinition.forEach(mpd -> {
                                WBSEntry wbsE = generateEntityProcessWBSEntry(
                                    operator,
                                    mpd,
                                    workEntry,
                                    entity,
                                    entityIndex.get()
                                );
                            });
                        }


                        if (!wbsEntityIds.contains(entity.getEntityId())) {
                            wbsEntityIds.add(entity.getEntityId());
                            Set<Long> relatedPnIds = getRelatedWbsEntityIds(entity.getProjectId(), entity.getProjectNodeId(), true);
                            relatedProjectNodeIds.addAll(relatedPnIds);
                        }
                        batchResult.addProcessedCount(1);

                        if (batchResult.getProcessedCount() % 10 == 0
                            && batchTask.checkRunningStatus()
                        ) {
                            batchTask.setResult(batchResult);
                            batchTask.setLastModifiedAt();
                            batchTaskRepository.save(batchTask);
                        }
                    });

                });

        });

        if (batchResult.getProcessedCount() == 0) {
            return batchResult;
        }

        /*----------------------------------------------------------------------
           4. 信息恢复；
         ---------------------------------------------------------------------*/


        batchTask.setProgressText("setting work load");
        batchTaskRepository.save(batchTask);
        if (!CollectionUtils.isEmpty(workLoadWbsIds)) {
            updateWorkLoad(projectId, workLoadWbsIds);
        }

        /*----------------------------------------------------------------------
           5. 查找 没有前置任务的 实体ID，这些实体 根据 category (/stage/process/entityType/entitySubType） 需要有前置节点
         ---------------------------------------------------------------------*/
        Set<Long> wbsEntryPnIdsWoPredecessor = new HashSet<>();
        workWbsEntries.forEach(workWbsEntry -> {

            List<WBSEntry> wbsEntriesWoPredecessor = wbsEntryRepository.findByProjectIdAndParentIdAndPredecessorIsNull(projectId, workWbsEntry.getId());
            if (CollectionUtils.isEmpty(wbsEntriesWoPredecessor)) return;

            ModuleProcessDefinition mpd = moduleProcessDefinitions.get(wbsEntriesWoPredecessor.get(0).getModuleType() + "-" + workWbsEntry.getFuncPart());
            if (mpd == null) return;

            wbsEntriesWoPredecessor.forEach(wbsEntryWoPredecessor -> {
                String category = wbsEntryWoPredecessor.getStage() + "/" + wbsEntryWoPredecessor.getProcess() + "/" +
                    wbsEntryWoPredecessor.getEntityType() + "/" + wbsEntryWoPredecessor.getEntitySubType();

                BpmnTaskRelation bpmnTaskRelation = planRelationService.getBpmnTaskRelationFromRedis(orgId, projectId, mpd.getId(), category);
                if (bpmnTaskRelation == null) return;

                if (CollectionUtils.isEmpty(bpmnTaskRelation.getJsonPredecessorNodes())) return;
                relatedProjectNodeIds.add(wbsEntryWoPredecessor.getProjectNodeId());

            });
        });

        /*----------------------------------------------------------------------
           6. 生成四级计划的前置任务关系；
         ---------------------------------------------------------------------*/
        Map<Long, String> moduleTypeMap = new HashMap<>();
        Long version = new Date().getTime();
        relatedProjectNodeIds.forEach(relatedPnId -> {
            ProjectNode pn = projectNodeRepository.findById(relatedPnId).orElse(null);
            if (pn == null) {
                return;
            }
            Set<Long> moduleIds = hierarchyNodeRelationRepository.findModuleIdsByNodeId(projectId, relatedPnId);
            List<WBSEntry> wbsEntriesByEntityIds = wbsEntryRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, pn.getEntityId());
            wbsEntriesByEntityIds.forEach(wbsEntry -> {
                Set<BigInteger> predecessorWbses = wbsEntryRelationRepository.findAllByTaskGUID(wbsEntry.getProjectId(), wbsEntry.getGuid());
//                Set<WBSEntryRelation> successorWbses = wbsEntryRelationRepository.findSuccessorWbByTaskGUID(wbsEntry.getProjectId(), wbsEntry.getGuid());
                if (predecessorWbses == null) predecessorWbses = new HashSet<>();
//                predecessorWbses.addAll(successorWbses);
                predecessorWbses.forEach(wr -> {
                    Optional<WBSEntryRelation> wbsEntryRelation = wbsEntryRelationRepository.findById(wr.longValue());
                    WBSEntry wrr = wbsEntryRepository.findById(wr.longValue()).orElse(null);
                    if (!wbsEntryRelation.isPresent()) return;
                    if (version > wrr.getVersion()) {
                        wbsEntryRelationRepository.deleteById(wrr.getId());
                    }
                });


                for (Long moduleId : moduleIds) {
                    if (!moduleTypeMap.keySet().contains(moduleId)) {
                        projectNodeRepository.findById(moduleId).ifPresent(
                            mpn -> {
                                moduleTypeMap.put(moduleId, mpn.getModuleType());
                            }
                        );
                    }
                    String moduleType = moduleTypeMap.get(moduleId);
                    if (StringUtils.isEmpty(moduleType)) {
                        continue;
                    }
                    ModuleProcessDefinition moduleDefinition = moduleProcessDefinitions.get(moduleType + "-" + wbsEntry.getFuncPart());
                    if (moduleDefinition != null) {
                        planRelationService.
                            generateEntityProcessPredecessorRelations(
                                operator,
                                moduleDefinition,
                                wbsEntry
                            );
                    }
                }


                batchResult.addProcessedRelationCount(1);

                if (batchResult.getProcessedRelationCount() % 10 == 0
                    && batchTask.checkRunningStatus()
                ) {
                    batchTask.setResult(batchResult);
                    batchTask.setLastModifiedAt();
                    batchTaskRepository.save(batchTask);
                }
            });


            wbsEntriesByEntityIds.forEach(wbsEntry -> {
                wbsEntryRelationRepository.getRedundantRelations(projectId, wbsEntry.getGuid()).forEach(wrId -> {
                    wbsEntryRelationRepository.deleteById(wrId.longValue());
                });
            });

        });


        batchTask.setProgressText("setting running status as pending");
        batchTaskRepository.save(batchTask);

        wbsEntryRepository.getRunningStatusAsPending(projectId).forEach(pendingWbsId -> {
            wbsEntryStateRepository.updateRunningStatusById(projectId, WBSEntryRunningStatus.PENDING, pendingWbsId.longValue());
        });

        /*----------------------------------------------------------------------
           7. 更新项目信息，返回执行结果。
         ---------------------------------------------------------------------*/

        batchTask.setProgressText("setting work load");
        batchTaskRepository.save(batchTask);
        if (!CollectionUtils.isEmpty(workWbsEntryIds)) {
            updateWorkLoad(projectId, workWbsEntryIds);
        }

        batchTask.setProgressText("updating project's revision");
        batchTaskRepository.save(batchTask);

        Project project = projectRepository.findById(projectId).orElse(null);


        if (project != null) {
            project.setLastModifiedBy(operator.getId());
            project.setLastModifiedAt(new Date());
            projectRepository.save(project);
        }

        batchTask.setProgressText("finished");
        batchTaskRepository.save(batchTask);


        return batchResult;


    }

    /**
     * 针对任务包中的 实体生成四级计划 及相互关系
     *
     * @param batchTask     batchTask
     * @param operator      operator
     * @param orgId         orgId
     * @param projectId     projectId
     * @param taskPackageId taskPackageId
     *                      <p>
     *                      1.0 找到所有需要重新生成 四级计划实体 对应的模块清单
     *                      2.0 根据模块清单 对应的模块类型，专业，找到 对应的 大流程部署清单
     *                      3.0 找到 所有需要生成4级计划的实体列表
     *                      4.0 对应每一种 大流程部署文件，对应的 维度（专业），找到所有需要生成四级计划的视图实体列表
     *                      5.0 设置四级计划的所属父级信息
     *                      6.0 根据3.0步生成的计划的 实体清单，找到这些实体的 父级 祖父级（必须是 ENTITY WP01等），子级，孙级 的集合
     *                      7.0 对应 6.0这些实体，找到对应的4级计划
     *                      8.0 对应6.0中的4级计划，生成前置任务
     *                      9.0 找到上述计划的 3级计划，更新工作量
     *                      10.0 设置这些4.0生成的四级计划的任务包
     * @return BatchResultDTO
     */
    @Override
    public BatchResultDTO generateWbsEntryFromTaskPackageWbsEntity(final BatchTask batchTask,
                                                                   final OperatorDTO operator,
                                                                   final Long orgId,
                                                                   final Long projectId,
                                                                   final Long taskPackageId) {
        final BatchResultDTO batchResult = new BatchResultDTO(batchTask);
        Boolean existErrorTask = wbsEntryLogRepository.existsByProjectIdAndResult(projectId, ActivityExecuteResult.INIT);
        if (existErrorTask != null && existErrorTask) {
            throw new BusinessError("THERE IS not HANDLED ERROR ADD TASK");
        }

        Optional<TaskPackage> taskPackage = taskPackageRepository.findById(taskPackageId);
        if (!taskPackage.isPresent()) {
            throw new BusinessError("未找到相关任务包");
        }


        batchTask.setProgressText(taskPackage.get().getName() + "Adding WBS entries");
        batchTask.setName("任务包：" + taskPackage.get().getName());
        batchTaskRepository.save(batchTask);


        final Set<Long> wbsEntryProcessedIDs = new HashSet<>();

        List<Tuple> missingWbs = taskPackageRepository.findMissingWbs(taskPackageId);
        List<List<Long>> newMissingWbs = new ArrayList<>();
        missingWbs.forEach(mw -> {
            if (mw == null) return;
            Long entityId = ((BigInteger) mw.get("entityId")).longValue();
            Long processId = ((BigInteger) mw.get("processId")).longValue();
            Long entitySubTypeId = ((BigInteger) mw.get("entitySubTypeId")).longValue();
            List<Long> ep = new ArrayList<>();
            ep.add(entityId);
            ep.add(processId);
            ep.add(entitySubTypeId);
            newMissingWbs.add(ep);

        });

        Set<Long> wbsEntityIds = new HashSet<>();
        Set<Long> relatedProjectNodeIds = new HashSet<>();
        Map<Long, Set<Long>> relatedPnIdToMpdIdMap = new HashMap<>();
        Map<Long, ModuleProcessDefinition> moduleProcessDefinitionMap = new HashMap<>();
        Map<String, Set<ModuleProcessDefinition>> moduleDefinitionsMap = new HashMap<>();

        newMissingWbs.forEach(nmw -> {
//            if (!nmw.get(0).equals(1619257337761327100L)) return ;
            batchResult.addTotalCount(1);
            Long entityId = nmw.get(0);
            Long processId = nmw.get(1);
            Long entitySubTypeId = nmw.get(2);
            BpmEntitySubType entitySubType = entitySubTypeRepository.findById(entitySubTypeId).orElse(null);
            if (entitySubType == null) return;
            String entityType = entitySubType.getEntityType().getNameEn();
//            String entitySubType = entitySubType.getNameEn();
            BpmProcess bpmProcess = processService.getBpmProcess(processId);
            String stage = bpmProcess.getProcessStage().getNameEn();
            String processStr = bpmProcess.getNameEn();
            String category = stage + "/" + processStr + "/" + entityType + "/" + entitySubType;
            String funcPart = bpmProcess.getFuncPart();
            Set<ModuleProcessDefinition> moduleDefinitions;
            if (moduleDefinitionsMap.get(projectId.toString() + funcPart + category) == null) {
                moduleDefinitions = bpmnTaskRelationRepository.findModuleDefinitions(projectId, funcPart, category);
                if (moduleDefinitions == null) moduleDefinitions = new HashSet<>();
                moduleDefinitionsMap.put(projectId.toString() + funcPart + category, moduleDefinitions);
            } else {
                moduleDefinitions = moduleDefinitionsMap.get(projectId.toString() + funcPart + category);
            }
            if (CollectionUtils.isEmpty(moduleDefinitions)) return;

            ProcessEntity entity = new ProcessEntity();
            if (funcPart.equals("STRUCTURE")) {
                StructureProcessEntity sEntity = new StructureProcessEntity();
//                    structureProcessEntityRepository
//                    .findByProjectIdAndProcessIdAndEntityIdAndModuleHierarchyNodeIdIsNotNull(
//                        projectId,
//                        processId,
//                        entityId
//                    );
                if (sEntity != null)
                    BeanUtils.copyProperties(sEntity, entity);
            } else if ("PIPING".equals(funcPart)) {
                entity = processEntityRepository.findByProjectIdAndProcessIdAndEntityId(
                    projectId,
                    processId,
                    entityId
                );
            }

            if (entity == null || entity.getEntityId() == null) return;

            for (ModuleProcessDefinition moduleProcessDefinition : moduleDefinitions) {

                moduleProcessDefinitionMap.put(moduleProcessDefinition.getId(), moduleProcessDefinition);
                Set<WBSEntry> workWbses = wbsEntryRepository.findWorkByProjectIdAndProcessIdAndEntityId(projectId, processStr, stage, entityId);

                for (WBSEntry workEntry : workWbses) {
                    WBSEntry lastChild = wbsEntryRepository
                        .findFirstByParentIdAndTypeAndDeletedIsFalseOrderBySortDesc(workEntry.getId(), ENTITY)
                        .orElse(null);

                    final AtomicInteger entityIndex = new AtomicInteger(lastChild == null ? 0 : lastChild.getSortNo());
                    WBSEntry wbsE = generateEntityProcessWBSEntry(
                        operator,
                        moduleProcessDefinition,
                        workEntry,
                        entity,
                        entityIndex.get()
                    );
                    batchResult.addProcessedCount(1);
                }


//                        entityIDs.add(entity.getEntityId());
                if (!wbsEntityIds.contains(entity.getEntityId())) {
                    wbsEntityIds.add(entity.getEntityId());
                    Set<Long> relatedPnIds = getRelatedWbsEntityIds(entity.getProjectId(), entity.getProjectNodeId(), true);
                    relatedPnIds.forEach(relatedPnId -> {
                        Set<Long> mpdIds = relatedPnIdToMpdIdMap.computeIfAbsent(relatedPnId, k -> new HashSet<>());
                        mpdIds.add(moduleProcessDefinition.getId());
                        relatedPnIdToMpdIdMap.put(relatedPnId, mpdIds);
                    });

                    relatedProjectNodeIds.addAll(relatedPnIds);
                }
            }

        });


        if (batchResult.getProcessedCount() == 0) {
            return batchResult;
        }

        Long version = new Date().getTime();
        relatedProjectNodeIds.forEach(relatedPnId -> {
            ProjectNode pn = projectNodeRepository.findById(relatedPnId).orElse(null);
            if (pn == null) {
                return;
            }
            List<WBSEntry> wbsEntriesByEntityIds = wbsEntryRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, pn.getEntityId());
            wbsEntriesByEntityIds.forEach(wbsEntry -> {

                Set<WBSEntryRelation> predecessorWbses = wbsEntryRelationRepository.findAllWbByTaskGUID(wbsEntry.getProjectId(), wbsEntry.getGuid());
//                Set<WBSEntryRelation> successorWbses = wbsEntryRelationRepository.findSuccessorWbByTaskGUID(wbsEntry.getProjectId(), wbsEntry.getGuid());
                if (predecessorWbses == null) predecessorWbses = new HashSet<>();
//                predecessorWbses.addAll(successorWbses);
                predecessorWbses.forEach(wr -> {
                    Optional<WBSEntryRelation> wbsEntryRelation = wbsEntryRelationRepository.findById(wr.getId());
                    if (!wbsEntryRelation.isPresent()) return;
                    if (version > wr.getVersion()) {
                        wbsEntryRelationRepository.deleteById(wr.getId());
                    }
                });
                Set<Long> mpdIds = relatedPnIdToMpdIdMap.get(relatedPnId);
                mpdIds.forEach(mpdId -> {
                    ModuleProcessDefinition mpd = moduleProcessDefinitionMap.get(mpdId);
                    if (mpd != null) {
                        planRelationService.
                            generateEntityProcessPredecessorRelations(
                                operator,
                                mpd,
                                wbsEntry
                            );
                        batchResult.addProcessedRelationCount(1);
                    }
                });
            });
            //Delete Redundant Relations
            wbsEntriesByEntityIds.forEach(wbsEntry -> {
                wbsEntryRelationRepository.getRedundantRelations(projectId, wbsEntry.getGuid()).forEach(wrId -> {
                    wbsEntryRelationRepository.deleteById(wrId.longValue());
                });
            });
        });

        wbsEntryRepository.getRunningStatusAsPending(projectId).forEach(pendingWbsId -> {
            wbsEntryStateRepository.updateRunningStatusById(projectId, WBSEntryRunningStatus.PENDING, pendingWbsId.longValue());
        });


        return batchResult;


    }


    @Override
    public BatchResultDTO deleteGenerateWbsEntryFromAddedWbsEntity(final BatchTask batchTask,
                                                                   final OperatorDTO operator,
                                                                   final Long orgId,
                                                                   final Long projectId,
                                                                   final Long originalBatchTaskId) {


        WBSEntryLog wbsEntryLog = wbsEntryLogRepository.findByBatchTaskIdAndResult(originalBatchTaskId, ActivityExecuteResult.INIT);
        final BatchResultDTO batchResult = new BatchResultDTO(batchTask);
        if (wbsEntryLog == null) {
            return batchResult;
        }
        WBSEntry we = wbsEntryRepository.findById(wbsEntryLog.getWbsEntryId()).orElse(null);

        List<WBSEntryAddLog> wbsEntryAddLogs = wbsEntryAddLogRepository.
            findByProjectIdAndBatchTaskId(projectId, originalBatchTaskId);


        wbsEntryAddLogs.forEach(wbsEntryAddLog -> {
            List<WBSEntry> wbsEntries = wbsEntryRepository.findByProjectIdAndProjectNodeId(projectId, wbsEntryAddLog.getProjectNodeId());
            wbsEntries.forEach(wbsEntry -> {
                WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
                if (wbsEntryState == null) return;
                Set<BigInteger> predecessorWbses = wbsEntryRelationRepository.findAllByTaskGUID(projectId, wbsEntry.getGuid());
//                Set<BigInteger> successorWbses = wbsEntryRelationRepository.findSuccessorsByTaskGUID(projectId, wbsEntry.getGuid());
                if (predecessorWbses == null) predecessorWbses = new HashSet<>();
//                predecessorWbses.addAll(successorWbses);
                predecessorWbses.forEach(wrId -> {
                    wbsEntryRelationRepository.deleteById(wrId.longValue());
                });

                wbsEntryPlainRelationRepository.deleteByWbsEntryId(wbsEntry.getId());
                if (!RUNNING_STATUS_CANNOT_BE_REMOVED.contains(wbsEntryState.getRunningStatus())) {
                    wbsEntryRepository.delete(wbsEntry);
                }
            });

        });

        wbsEntryLog.setResult(ActivityExecuteResult.NG);
        wbsEntryLogRepository.save(wbsEntryLog);

        return batchResult;

    }

    private Set<Long> getRelatedWbsEntityIds(Long projectId, Long projectNodeId, boolean isSiblingIncluded) {

        Set<Long> relatedProjectNodeIds = new HashSet<>();


        List<HierarchyNodeRelation> hnrs = hierarchyNodeRelationRepository.findByProjectIdAndNodeIdOrderByDepthDesc(projectId, projectNodeId);
        Integer depth = 100;
        for (HierarchyNodeRelation hnr : hnrs) {
            if (hnr.getNodeAncestorId() != null && hnr.getNodeAncestorId().equals(hnr.getNodeId())) {
                depth = hnr.getDepth();
            }
            if (hnr.getDepth() < depth && hnr.getDepth() > depth - 2 && hnr.getNodeAncestorId() != null) {
                relatedProjectNodeIds.add(hnr.getNodeAncestorId());
            }
        }


        if (isSiblingIncluded) {
            List<BigInteger> siblingPnIds = hierarchyRepository.findSiblingProjectNodeIds(projectId, projectNodeId);
            siblingPnIds.forEach(siblingPnId -> {
                relatedProjectNodeIds.add(siblingPnId.longValue());
            });


        }


        return relatedProjectNodeIds;

    }

    /**
     * 针对选中的 实体生成四级计划 及相互关系
     *
     * @param batchTask batchTask
     * @param operator  operator
     * @param orgId     orgId
     * @param projectId projectId
     * @param entityIds entityIds
     *                  <p>
     *                  1.0 找到所有需要重新生成 四级计划实体 对应的模块清单
     *                  2.0 根据模块清单 对应的模块类型，专业，找到 对应的 大流程部署清单
     *                  3.0 找到 所有需要生成4级计划的实体列表
     *                  4.0 对应每一种 大流程部署文件，对应的 维度（专业），找到所有需要生成四级计划的视图实体列表
     *                  5.0 设置四级计划的所属父级信息
     *                  6.0 根据3.0步生成的计划的 实体清单，找到这些实体的 父级 祖父级（必须是 ENTITY WP01等），子级，孙级 的集合
     *                  7.0 对应 6.0这些实体，找到对应的4级计划
     *                  8.0 对应6.0中的4级计划，生成前置任务
     *                  9.0 找到上述计划的 3级计划，更新工作量
     *                  10.0 设置这些4.0生成的四级计划的任务包
     * @return BatchResultDTO
     */
    @Override
    public BatchResultDTO generateWbsEntryFromSelectedEntities(final BatchTask batchTask,
                                                               final OperatorDTO operator,
                                                               final Long orgId,
                                                               final Long projectId,
                                                               final List<Long> entityIds) {
        final BatchResultDTO batchResult = new BatchResultDTO(batchTask);

        List<ProjectNode> pns = projectNodeRepository.findByProjectIdAndEntityIdInAndStatus(projectId, entityIds, EntityStatus.ACTIVE);

        if (CollectionUtils.isEmpty(pns)) {
            throw new BusinessError("未找到相关项目实体");
        }


        batchTask.setProgressText("Adding entries " + pns.size());
        batchTask.setName("实体个数：" + pns.size());
        batchTaskRepository.save(batchTask);


        final Set<Long> wbsEntryProcessedIDs = new HashSet<>();


        List<List<Long>> newMissingWbs = new ArrayList<>();

        //取得 entityId -> processId 的映射 map
        pns.forEach(pn -> {
            List<ProcessEntityType> processEntityTypes = processEntityTypeRepository.findByProjectIdAndEntityTypeAndEntitySubType(projectId, pn.getEntityType(), pn.getEntitySubType());
            if (CollectionUtils.isEmpty(processEntityTypes)) return;

            processEntityTypes.forEach(pet -> {
                WBSEntry wbsEntry = wbsEntryRepository.findByProjectIdAndEntityIdAndProcessIdAndDeletedIsFalse(projectId, pn.getEntityId(), pet.getProcessId());
//                if(wbsEntry != null) return;
                List<Long> newWbsEntry = new ArrayList<>();
                //projectId, entityId, processId, entitySubTypeId
//                newWbsEntry.add(projectId);
                newWbsEntry.add(pn.getEntityId());
                newWbsEntry.add(pet.getProcessId());
                newWbsEntry.add(pet.getEntitySubTypeId());
                newMissingWbs.add(newWbsEntry);
            });

        });


        Set<Long> wbsEntityIds = new HashSet<>();
        Set<Long> relatedProjectNodeIds = new HashSet<>();
        Map<Long, Set<Long>> relatedPnIdToMpdIdMap = new HashMap<>();
        Map<Long, ModuleProcessDefinition> moduleProcessDefinitionMap = new HashMap<>();
        Map<String, Set<ModuleProcessDefinition>> moduleDefinitionsMap = new HashMap<>();

        newMissingWbs.forEach(nmw -> {
//            if (!nmw.get(0).equals(1619257337761327100L)) return ;
            batchResult.addTotalCount(1);
            Long entityId = nmw.get(0);
            Long processId = nmw.get(1);
            Long entitySubTypeId = nmw.get(2);
            BpmEntitySubType entitySubType = entitySubTypeRepository.findById(entitySubTypeId).orElse(null);
            if (entitySubType == null) return;
            String entityType = entitySubType.getEntityType().getNameEn();
//            String entitySubType = entityType.getNameEn();
            BpmProcess bpmProcess = processService.getBpmProcess(processId);
            String stage = bpmProcess.getProcessStage().getNameEn();
            String processStr = bpmProcess.getNameEn();
            String category = stage + "/" + processStr + "/" + entityType + "/" + entitySubType;
            String funcPart = bpmProcess.getFuncPart();
            Set<ModuleProcessDefinition> moduleDefinitions;
            if (moduleDefinitionsMap.get(projectId.toString() + funcPart + category) == null) {
                moduleDefinitions = bpmnTaskRelationRepository.findModuleDefinitions(projectId, funcPart, category);
                if (moduleDefinitions == null) moduleDefinitions = new HashSet<>();
                moduleDefinitionsMap.put(projectId.toString() + funcPart + category, moduleDefinitions);
            } else {
                moduleDefinitions = moduleDefinitionsMap.get(projectId.toString() + funcPart + category);
            }
            if (CollectionUtils.isEmpty(moduleDefinitions)) return;

            ProcessEntity entity = new ProcessEntity();
            if (funcPart.equals("STRUCTURE")) {
                StructureProcessEntity sEntity = new StructureProcessEntity();
//                    structureProcessEntityRepository
//                    .findByProjectIdAndProcessIdAndEntityIdAndModuleHierarchyNodeIdIsNotNull(
//                        projectId,
//                        processId,
//                        entityId
//                    );
                if (sEntity != null)
                    BeanUtils.copyProperties(sEntity, entity);
            } else if ("PIPING".equals(funcPart)) {
                entity = processEntityRepository.findByProjectIdAndProcessIdAndEntityId(
                    projectId,
                    processId,
                    entityId
                );
            }

            if (entity == null || entity.getEntityId() == null) return;

            for (ModuleProcessDefinition moduleProcessDefinition : moduleDefinitions) {

                moduleProcessDefinitionMap.put(moduleProcessDefinition.getId(), moduleProcessDefinition);
                Set<WBSEntry> workWbses = wbsEntryRepository.findWorkByProjectIdAndProcessIdAndEntityId(projectId, processStr, stage, entityId);

                for (WBSEntry workEntry : workWbses) {
                    WBSEntry lastChild = wbsEntryRepository
                        .findFirstByParentIdAndTypeAndDeletedIsFalseOrderBySortDesc(workEntry.getId(), ENTITY)
                        .orElse(null);

                    final AtomicInteger entityIndex = new AtomicInteger(lastChild == null ? 0 : lastChild.getSortNo());
                    WBSEntry wbsE = generateEntityProcessWBSEntry(
                        operator,
                        moduleProcessDefinition,
                        workEntry,
                        entity,
                        entityIndex.get()
                    );
                    batchResult.addProcessedCount(1);
                }


//                        entityIDs.add(entity.getEntityId());
                if (!wbsEntityIds.contains(entity.getEntityId())) {
                    wbsEntityIds.add(entity.getEntityId());
                    Set<Long> relatedPnIds = getRelatedWbsEntityIds(entity.getProjectId(), entity.getProjectNodeId(), true);
                    relatedPnIds.forEach(relatedPnId -> {
                        Set<Long> mpdIds = relatedPnIdToMpdIdMap.computeIfAbsent(relatedPnId, k -> new HashSet<>());
                        mpdIds.add(moduleProcessDefinition.getId());
                        relatedPnIdToMpdIdMap.put(relatedPnId, mpdIds);
                    });

                    relatedProjectNodeIds.addAll(relatedPnIds);
                }
            }

        });


        if (batchResult.getProcessedCount() == 0) {
            return batchResult;
        }

        Long version = new Date().getTime();
        relatedProjectNodeIds.forEach(relatedPnId -> {
            ProjectNode pn = projectNodeRepository.findById(relatedPnId).orElse(null);
            if (pn == null) {
                return;
            }
            List<WBSEntry> wbsEntriesByEntityIds = wbsEntryRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, pn.getEntityId());
            wbsEntriesByEntityIds.forEach(wbsEntry -> {

                Set<WBSEntryRelation> predecessorWbses = wbsEntryRelationRepository.findAllWbByTaskGUID(wbsEntry.getProjectId(), wbsEntry.getGuid());
//                Set<WBSEntryRelation> successorWbses = wbsEntryRelationRepository.findSuccessorWbByTaskGUID(wbsEntry.getProjectId(), wbsEntry.getGuid());

//                for (Long workWbsId : wbsEntryRelationToBeDeletedIds) {
//                    Set<BigInteger> pWbsEntryRelations = wbsEntryRelationRepository.findPGuidsLessThanVersion(orgId, projectId, workWbsId, version);
//                    Set<BigInteger> sWbsEntryRelations = wbsEntryRelationRepository.findSGuidsLessThanVersion(orgId, projectId, workWbsId, version);
//                    if(pWbsEntryRelations == null) pWbsEntryRelations = new HashSet<>();
//                    pWbsEntryRelations.addAll(sWbsEntryRelations);
//                    if(!CollectionUtils.isEmpty(pWbsEntryRelations)) {
//                        pWbsEntryRelations.forEach(werId -> {
//                            wbsEntryRelationRepository.deleteById(werId.longValue());
//                        });
//                    }
//                }

                if (predecessorWbses == null) predecessorWbses = new HashSet<>();
//                predecessorWbses.addAll(successorWbses);
                predecessorWbses.forEach(wr -> {
                    Optional<WBSEntryRelation> wbsEntryRelation = wbsEntryRelationRepository.findById(wr.getId());
                    if (!wbsEntryRelation.isPresent()) return;
                    if (version > wr.getVersion()) {
                        wbsEntryRelationRepository.deleteById(wr.getId());
                    }
                });
                Set<Long> mpdIds = relatedPnIdToMpdIdMap.get(relatedPnId);
                mpdIds.forEach(mpdId -> {
                    ModuleProcessDefinition mpd = moduleProcessDefinitionMap.get(mpdId);
                    if (mpd != null) {
                        planRelationService.
                            generateEntityProcessPredecessorRelations(
                                operator,
                                mpd,
                                wbsEntry
                            );
                        batchResult.addProcessedRelationCount(1);
                    }
                });
            });
            //Delete Redundant Relations
            wbsEntriesByEntityIds.forEach(wbsEntry -> {
                wbsEntryRelationRepository.getRedundantRelations(projectId, wbsEntry.getGuid()).forEach(wrId -> {
                    wbsEntryRelationRepository.deleteById(wrId.longValue());
                });
            });
        });

        wbsEntryRepository.getRunningStatusAsPending(projectId).forEach(pendingWbsId -> {
            wbsEntryStateRepository.updateRunningStatusById(projectId, WBSEntryRunningStatus.PENDING, pendingWbsId.longValue());
        });


        return batchResult;


    }

}
