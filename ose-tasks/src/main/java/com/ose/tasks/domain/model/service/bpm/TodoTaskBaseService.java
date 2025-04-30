package com.ose.tasks.domain.model.service.bpm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.auth.api.UserFeignAPI;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.issues.api.IssueFeignAPI;
import com.ose.issues.dto.IssueUpdateDTO;
import com.ose.issues.entity.Issue;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.HierarchyRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageAssignSiteTeamsRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.service.ServerConfig;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.WBSEntryTeamWorkSiteDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingHistory;
import com.ose.tasks.entity.taskpackage.TaskPackageAssignSiteTeams;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.*;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.BeanUtils;
import com.ose.util.FileUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

/**
 * 任务执行基础服务。
 */
@Component
//@Transactional
public class TodoTaskBaseService extends StringRedisService implements TodoTaskBaseInterface {
    private final static Logger logger = LoggerFactory.getLogger(TodoTaskBaseService.class);


    //先检查 REDIS中是否存在 (orgId + projectId) -> 的报告编号值
//    private static final String TASK_INFO_AT_REDIS_KEY = "TASK_INFO:%s";

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 上传文件的临时路径
    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${mail.server.host}")
    private String mailHost;

    @Value("${mail.server.port}")
    private String mailPort;

    @Value("${mail.server.username}")
    private String mailUsername;

    @Value("${mail.server.password}")
    private String mailPassword;

    @Value("${mail.server.fromAddress}")
    private String mailFromAddress;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private final BpmActTaskRepository bpmActTaskRepository;

    private final BpmProcessRepository processRepository;

    private final BpmEntitySubTypeRepository entitySubTypeRepository;

    private final BpmProcessStageRepository processStageRepository;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmActInstVariableConfigRepository bpmActInstVariableRepository;

    private final BpmActInstVariableTaskConfigRepository bpmActInstVariableTaskConfigRepository;

    private final BpmActInstVariableValueRepository bpmActInstVariableValueRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final BpmActInstMaterialRepository bpmActInstMaterialRepository;

    private final BpmProcessCategoryRepository bpmProcessCategoryRepository;

    private final BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository;

    private final WBSEntryRepository wBSEntryRepository;

    private final HierarchyRepository hierarchyRepository;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final IssueFeignAPI issueFeignAPI;

    private final BpmProcessTaskNodeEnableConfigRepository bpmProcessTaskNodeEnableConfigRepository;

    private final BpmPlanExecutionHistoryRepository bpmPlanExecutionHistoryRepository;

    private final ServerConfig serverConfig;

    private final TaskPackageAssignSiteTeamsRepository assignSiteTeamsRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final DrawingRepository drawingRepository;

    private final UserFeignAPI userFeignAPI;

    private final DrawingHistoryRepository drawingHistoryRepository;

    private final BpmProcessCheckListRepository processCheckListRepository;

    private final UploadFeignAPI uploadFeignAPI;

    private final BpmProcessStageRepository bpmProcessStageRepository;

    private final EntitySubTypeInterface entitySubTypeService;
    /**
     * 构造方法。
     */
    @Autowired
    public TodoTaskBaseService(BpmActivityInstanceRepository bpmActInstRepository,
                               StringRedisTemplate stringRedisTemplate,
                               BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository, BpmActTaskRepository bpmActTaskRepository,
                               BpmProcessRepository processRepository, BpmEntitySubTypeRepository entitySubTypeRepository,
                               BpmProcessStageRepository processStageRepository,
                               BpmHiTaskinstRepository hiTaskinstRepository,
                               BpmRuTaskRepository ruTaskRepository,
                               BpmActInstVariableConfigRepository bpmActInstVariableRepository,
                               BpmActInstVariableTaskConfigRepository bpmActInstVariableTaskConfigRepository,
                               BpmActInstVariableValueRepository bpmActInstVariableValueRepository,
                               ProjectNodeRepository projectNodeRepository,
                               BpmActInstMaterialRepository bpmActInstMaterialRepository,
                               BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository,
                               BpmProcessCategoryRepository bpmProcessCategoryRepository,
                               WBSEntryRepository wBSEntryRepository,
                               HierarchyRepository hierarchyRepository,
                               BpmEntityDocsMaterialsRepository docsMaterialsRepository,
                               @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") IssueFeignAPI issueFeignAPI,
                               BpmProcessTaskNodeEnableConfigRepository bpmProcessTaskNodeEnableConfigRepository,
                               BpmPlanExecutionHistoryRepository bpmPlanExecutionHistoryRepository,
                               ServerConfig serverConfig,
                               TaskPackageAssignSiteTeamsRepository assignSiteTeamsRepository,
                               DrawingDetailRepository drawingDetailRepository,
                               DrawingRepository drawingRepository,
                               @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
                               DrawingHistoryRepository drawingHistoryRepository,
                               BpmProcessCheckListRepository processCheckListRepository,
                               @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
                               BpmProcessStageRepository bpmProcessStageRepository, EntitySubTypeInterface entitySubTypeService) {
        super(stringRedisTemplate);
        this.bpmActInstRepository = bpmActInstRepository;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
        this.bpmActTaskRepository = bpmActTaskRepository;
        this.processRepository = processRepository;
        this.entitySubTypeRepository = entitySubTypeRepository;
        this.processStageRepository = processStageRepository;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmActInstVariableRepository = bpmActInstVariableRepository;
        this.bpmActInstVariableTaskConfigRepository = bpmActInstVariableTaskConfigRepository;
        this.bpmActInstVariableValueRepository = bpmActInstVariableValueRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.bpmActInstMaterialRepository = bpmActInstMaterialRepository;
        this.bpmProcessCategoryRepository = bpmProcessCategoryRepository;
        this.bpmActivityTaskNodePrivilegeRepository = bpmActivityTaskNodePrivilegeRepository;
        this.wBSEntryRepository = wBSEntryRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.bpmProcessTaskNodeEnableConfigRepository = bpmProcessTaskNodeEnableConfigRepository;
        this.issueFeignAPI = issueFeignAPI;
        this.bpmPlanExecutionHistoryRepository = bpmPlanExecutionHistoryRepository;
        this.serverConfig = serverConfig;
        this.assignSiteTeamsRepository = assignSiteTeamsRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.drawingRepository = drawingRepository;
        this.userFeignAPI = userFeignAPI;
        this.drawingHistoryRepository = drawingHistoryRepository;
        this.processCheckListRepository = processCheckListRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.bpmProcessStageRepository = bpmProcessStageRepository;
        this.entitySubTypeService = entitySubTypeService;
    }

    /**
     * 根据id获取实体类型
     */
    @Override
    public BpmEntitySubType findEntitySubTypeById(Long id) {
        Optional<BpmEntitySubType> op = entitySubTypeRepository.findById(id);
        return op.orElse(null);
    }

    /**
     * 根据id获取工序类型
     */
    @Override
    public BpmProcessStage findProcessStageById(Long id) {
        Optional<BpmProcessStage> op = processStageRepository.findById(id);
        return op.orElse(null);
    }

    /**
     * 根据id获取工序
     */
    @Override
    public BpmProcess findProcessById(Long id) {
        Optional<BpmProcess> op = processRepository.findById(id);
        return op.orElse(null);
    }

    /**
     * 根据流程查询流程实例id
     */
    @Override
    public List<Long> findActInstIdsInActivityInstance(Long orgId, Long projectId,
                                                          TodoTaskCriteriaDTO taskCriteria) {
        return bpmActInstRepository.findActInstIdsInActivityInstance(orgId, projectId, taskCriteria);
    }

    /**
     * 根据流程实例id查询流程任务信息
     */
    @Override
    public BpmActivityInstanceBase findActInstByProjectIdAndActInstId(Long projectId, Long actInstId) {
        Optional<BpmActivityInstanceBase> op = bpmActInstRepository.findById(actInstId);
        return op.orElse(null);
    }


    /**
     * 根据任务节点信息获取任务分配记录
     */
    @Override
    public BpmActTaskAssignee getTaskAssigneesByTaskInfo(String taskDefKey, String taskName, Long actInstId) {
        Optional<BpmActTaskAssignee> taskAssignee = bpmActTaskAssigneeRepository
            .findByActInstIdAndTaskDefKeyAndTaskName(actInstId, taskDefKey, taskName);
        return taskAssignee.orElse(null);
    }

    /**
     * 保存任务流程实例
     */
    @Override
    public BpmActivityInstanceBase saveBpmActivityInstance(BpmActivityInstanceBase actInst) {
        return bpmActInstRepository.save(actInst);
    }


    /**
     * 保存历史任务节点
     */
    @Override
    public BpmHiTaskinst saveBpmHiTaskinst(BpmHiTaskinst hiTask) {
        return hiTaskinstRepository.save(hiTask);
    }

    /**
     * 查询已完成任务
     */
    @Override
    public Page<BpmActivityInstanceDTO> searchCompletedTask(Long orgId, Long projectId,
                                                            TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO, String assignee) {
        List<Long> actInstIds = hiTaskinstRepository.findActInstIdsInHiTaskinst(projectId, taskCriteria, assignee);
        if (actInstIds.isEmpty()) {
            actInstIds.add(0L);
        }

        return bpmActInstRepository.findCompletedTask(orgId, projectId, taskCriteria, actInstIds, pageDTO);
    }

    /**
     * 获取待办任务层级数据-实体类型
     */
    @Override
    public List<HierarchyBaseDTO> getEntitiyCategoriesInRuTask(List<String> entityModuleNames, String taskNode, String taskDefKey,
                                                               Long processStageId, Long processId, Long orgId, Long projectId, String assignee) {
        return ruTaskRepository.getEntitiyCategoriesInRuTask(entityModuleNames, taskNode, taskDefKey, processStageId, processId,
            orgId, projectId, assignee);
    }

    /**
     * 获取待办任务层级数据-工序阶段
     */
    @Override
    public List<HierarchyBaseDTO> getProcessStagesInRuTask(Long orgId, Long projectId, Long assignee,
                                                           HierarchyCriteriaDTO criteriaDTO) {
        String psCountKey = String.format(RedisKey.TASK_INFO_PROCESS_STAGE_COUNT.getDisplayName(), projectId.toString(), assignee.toString());
        String initField = "INIT";
        String initResult = hget(psCountKey, initField);

        List<HierarchyBaseDTO> psList = new ArrayList<>();
        if (true) { //initResult == null || initResult.equals("0")) {
            List<HierarchyBaseCountDTO> psCountList = ruTaskRepository.getProcessStagesInRuTask(orgId, projectId, assignee.toString(), criteriaDTO);
//            psList = BeanUtils.convertType(psCountList, HierarchyBaseDTO.class);
            for (HierarchyBaseCountDTO psCount : psCountList) {
                HierarchyBaseDTO ps = BeanUtils.copyProperties(psCount, new HierarchyBaseDTO());
                psList.add(ps);
                sadd(String.format(RedisKey.TASK_INFO_PROCESS_STAGE.getDisplayName(), projectId.toString(), assignee.toString()),
                    StringUtils.toJSON(ps));
                //TS_INFO_PROCESS_STAGE_COUNT

                hset(String.format(RedisKey.TASK_INFO_PROCESS_STAGE_COUNT.getDisplayName(), projectId.toString(), assignee.toString()),
                    ps.getId().toString(),
                    psCount.getCount().toString()
                );
            }
            hset(psCountKey, initField, "1");


        } else {
            Set<String> processStageStrs = smembers(String.format(RedisKey.TASK_INFO_PROCESS_STAGE.getDisplayName(), projectId.toString(), assignee.toString()));
            for (String processStageStr : processStageStrs) {
                HierarchyBaseDTO hierarchyBaseDTO = StringUtils.decode(processStageStr, new TypeReference<HierarchyBaseDTO>() {
                });
                if (hierarchyBaseDTO != null) {
                    psList.add(hierarchyBaseDTO);
                }
            }

//            processStageStrs.forEach(processStageStr ->{
//                psList.add(StringUtils.decode(processStageStr, new TypeReference<HierarchyBaseDTO>() {}));
//            });

        }
        return psList;

    }


    /**
     * 获取待办任务层级数据-实体
     */
    @Override
    public List<HierarchyBaseDTO> getProcessesInRuTask(Long processStageId, Long orgId, Long projectId, Long assignee,
                                                       HierarchyCriteriaDTO criteriaDTO) {
        String prCountKey = String.format(RedisKey.TASK_INFO_PROCESS_COUNT.getDisplayName(), projectId.toString(), assignee.toString());
        String initField = "INIT";
        String initResult = hget(prCountKey, initField);

        List<HierarchyBaseDTO> prList = new ArrayList<>();
        if (true) {//initResult == null || initResult.equals("0")) {
            List<HierarchyBaseCountDTO> prCountList = ruTaskRepository.getProcessesInRuTask(projectId, assignee.toString(), criteriaDTO);
            if (prCountList == null) prCountList = new ArrayList<>();
//            prList = BeanUtils.convertType(prCountList, HierarchyBaseDTO.class);
            for (HierarchyBaseCountDTO prCount : prCountList) {
                HierarchyBaseDTO pr = BeanUtils.copyProperties(prCount, new HierarchyBaseDTO());
                if (processStageId.equals(prCount.getProcessStageId())) {
                    prList.add(pr);
                }
                sadd(String.format(RedisKey.TASK_INFO_PROCESS.getDisplayName(), projectId.toString(), assignee.toString()),
                    StringUtils.toJSON(prCount));
                //TS_INFO_PROCESS_COUNT

                hset(String.format(RedisKey.TASK_INFO_PROCESS_COUNT.getDisplayName(), projectId.toString(), assignee.toString()),
                    pr.getId().toString(),
                    prCount.getCount().toString()
                );
            }
            hset(prCountKey, initField, "1");

        } else {
            Set<String> processStrs = smembers(String.format(RedisKey.TASK_INFO_PROCESS.getDisplayName(), projectId.toString(), assignee.toString()));

            for (String processStr : processStrs) {
                HierarchyBaseCountDTO hierarchyBaseCountDTO = StringUtils.decode(processStr, new TypeReference<HierarchyBaseCountDTO>() {
                });
                if (hierarchyBaseCountDTO != null && processStageId.equals(hierarchyBaseCountDTO.getProcessStageId())) {
                    HierarchyBaseDTO hierarchyBaseDTO = BeanUtils.copyProperties(hierarchyBaseCountDTO, new HierarchyBaseDTO());
                    prList.add(hierarchyBaseDTO);
                }
            }

//            processStageStrs.forEach(processStageStr ->{
//                psList.add(StringUtils.decode(processStageStr, new TypeReference<HierarchyBaseDTO>() {}));
//            });

        }
        return prList;

//        return ruTaskRepository.getProcessesInRuTask(processStageId, orgId, projectId, assignee.toString());
    }

    /**
     * 获取待办任务层级数据-任务节点
     */
    @Override
    public List<TaskNodeDTO> getTaskNodesInRuTask(Long processStageId, Long processId, Long orgId, Long projectId,
                                                  Long assignee, Boolean batchFlag) {

        List<TaskNodeDTO> nodes = ruTaskRepository.getTaskNodesInRuTask(processStageId, processId, orgId, projectId,
            assignee.toString());

        List<TaskNodeDTO> result = new ArrayList<>();

        String processName = "";
        String processStageName = "";

        BpmProcess process = processRepository.findById(processId).orElse(null);
        if (process != null) {
            processName = process.getNameEn();
        }

        BpmProcessStage processStage = processStageRepository.findById(processStageId).orElse(null);
        if (processStage != null) {
            processStageName = processStage.getNameEn();
        }

        if (batchFlag) {
            for (TaskNodeDTO node : nodes) {
                BpmProcessTaskNodeEnableConfig config = bpmProcessTaskNodeEnableConfigRepository
                    .findByProcessAndProcessStageAndTaskDefKeyAndType(processName, processStageName, node.getTaskDefKey(), BpmCode.PC_BATCH);
                if (config != null
                    && !config.getEnable()) {
                    result.add(node);
                } else if (config == null) {
                    result.add(node);
                }
            }
        } else {
            result = nodes;
        }

        return result;
    }

    /**
     * 保存正在运行任务信息
     */
    @Override
    public BpmRuTask saveBpmRuTask(BpmRuTask bpmRuTask) {
        return ruTaskRepository.save(bpmRuTask);
    }

    /**
     * 获取已完成任务页面层级数据-实体类型
     */
    @Override
    public List<Long> getEntitiyCategoriesInHiTask(Long processCategoryId, Long orgId, Long projectId,
                                                   Long assignee) {
        return hiTaskinstRepository.getEntitiyCategoriesInHiTask(processCategoryId, orgId, projectId, assignee.toString());
    }

    /**
     * 获取已完成任务页面层级数据-工序阶段
     */
    @Override
    public List<Long> getProcessStagesInHiTask(Long processCategoryId, Long entitySubTypeId, Long orgId,
                                               Long projectId, Long assignee) {
        return hiTaskinstRepository.getProcessStagesInHiTask(processCategoryId, entitySubTypeId, orgId, projectId,
            assignee.toString());
    }

    /**
     * 获取已完成任务页面层级数据-工序
     */
    @Override
    public List<Long> getProcessesInHiTask(Long processCategoryId, Long entitySubTypeId, Long processStageId,
                                           Long orgId, Long projectId, Long assignee) {
        return hiTaskinstRepository.getProcessesInHiTask(processCategoryId, entitySubTypeId, processStageId, orgId,
            projectId, assignee.toString());
    }

    /**
     * 获取已完成任务页面层级数据-任务节点
     */
    @Override
    public List<String> getTaskNodesInHiTask(Long processCategoryId, Long entitySubTypeId, Long processStageId,
                                             Long processId, Long orgId, Long projectId, Long assignee) {
        return hiTaskinstRepository.getTaskNodesInHiTask(processCategoryId, entitySubTypeId, processStageId, processId,
            orgId, projectId, assignee.toString());
    }

    /**
     * 修改任务分配信息担当人
     */
    @Override
    public boolean assignee(Long actInstId, String taskDefKey, String taskName, Long assignee, BpmActivityInstanceBase actInst) {
        BpmRuTask ruTask = ruTaskRepository.findByActInstIdAndTaskDefKeyAndName(actInstId, taskDefKey, taskName);
        if (ruTask != null) {
            ruTask.setAssignee(assignee.toString());
            ruTaskRepository.save(ruTask);


            String projectId = ruTask.getTenantId();
            String psCountKey = String.format(RedisKey.TASK_INFO_PROCESS_STAGE_COUNT.getDisplayName(), projectId, assignee.toString());
            String initField = "INIT";
            String initResult = hget(psCountKey, initField);
            if (initResult == null || initResult.equals("0")) return true;

            Long processStageId = actInst.getProcessStageId();
            Long processId = actInst.getProcessId();
            if (processId == null || processStageId == null) return false;

            String hierarchyBaseDTOPsStr = null;
            String hierarchyBaseCountDTOPrStr = null;

            hierarchyBaseDTOPsStr = hget(
                String.format(RedisKey.PROCESS_STAGE.getDisplayName(), projectId),
                processStageId.toString()
            );

            hierarchyBaseCountDTOPrStr = hget(
                String.format(RedisKey.PROCESS.getDisplayName(), projectId),
                processId.toString()
            );

            hincrBy(String.format(RedisKey.TASK_INFO_PROCESS_STAGE_COUNT.getDisplayName(), projectId, assignee.toString()),
                processStageId.toString(), 1L);

            hincrBy(String.format(RedisKey.TASK_INFO_PROCESS_COUNT.getDisplayName(), projectId, assignee.toString()),
                processId.toString(), 1L);

            if (hierarchyBaseDTOPsStr == null) {
                HierarchyBaseDTO hierarchyBaseDTO = bpmProcessStageRepository.findDTOById(processStageId);
                if (hierarchyBaseDTO != null) {
                    hierarchyBaseDTOPsStr = StringUtils.toJSON(hierarchyBaseDTO);
                    hset(String.format(RedisKey.PROCESS_STAGE.getDisplayName(), projectId), processStageId.toString(),
                        hierarchyBaseDTOPsStr
                    );
                }
            }

            if (hierarchyBaseCountDTOPrStr == null) {
                HierarchyBaseCountDTO hierarchyBaseCountDTO = ruTaskRepository.findDTOById(processId);
                if (hierarchyBaseCountDTO != null) {
                    hierarchyBaseCountDTOPrStr = StringUtils.toJSON(hierarchyBaseCountDTO);
                    hset(String.format(RedisKey.PROCESS.getDisplayName(), projectId), processId.toString(),
                        hierarchyBaseCountDTOPrStr
                    );
                }
            }

            if (hierarchyBaseDTOPsStr != null
                && !hierarchyBaseDTOPsStr.equalsIgnoreCase("\"{}\"")) {
                sadd(
                    String.format(RedisKey.TASK_INFO_PROCESS_STAGE.getDisplayName(), projectId, assignee.toString()),
                    hierarchyBaseDTOPsStr
                );
            }

            if (hierarchyBaseCountDTOPrStr != null
                && !hierarchyBaseCountDTOPrStr.equalsIgnoreCase("\"{}\"")) {
                sadd(
                    String.format(RedisKey.TASK_INFO_PROCESS.getDisplayName(), projectId, assignee.toString()),
                    hierarchyBaseCountDTOPrStr
                );
            }

            return true;
        }
        return false;
    }

    /**
     * 从运行时任务记录删除已完成任务
     */
    @Override
    public boolean deleteRuTask(Long taskId) {
        BpmRuTask ruTask = ruTaskRepository.findById(taskId).orElse(null);
        if (ruTask != null) {
            ruTaskRepository.delete(ruTask);
            return true;
        }
        return false;
    }

    /**
     * 从运行时任务记录删除当前执行人已完成任务
     */
    @Override
    public boolean deleteRuTaskByAssign(Long taskId, String assign) {
        BpmRuTask ruTask = ruTaskRepository.findByIdAndAssignee(taskId, assign);
        if (ruTask != null) {
            ruTaskRepository.delete(ruTask);
            return true;
        }
        return false;
    }

    /**
     * 根据id获取待办任务信息
     */
    @Override
    public BpmRuTask findBpmRuTaskByActTaskId(Long taskId) {
        return ruTaskRepository.findById(taskId).orElse(null);
    }

    /**
     * 根据id获取待办任务信息
     */
    @Override
    public BpmRuTask findFirstBpmRuTaskByActTaskId(Long taskId) {
        return ruTaskRepository.findById(taskId).orElse(null);
    }

    /**
     * 根据流程定义key获取变量
     */
    @Override
    public List<BpmActInstVariableConfig> findActInstVariables(Long orgId, Long projectId, Long processId) {
        Optional<BpmProcess> optinal = processRepository.findById(processId);
        if (optinal.isPresent()) {
            BpmProcess process = optinal.get();
            String processStageNameEn = process.getProcessStage().getNameEn();
            return bpmActInstVariableRepository.findByProcessKeyAndStatus(processStageNameEn + "-" + process.getNameEn(), EntityStatus.ACTIVE);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 根据流程定义key，任务名称，变量名获取流程变量任务配置
     */
    @Override
    public BpmActInstVariableTaskConfig findVariableConfigByTaskDefKeyAndVariableName(Long processId, String taskDefKey,
                                                                                      String variableName) {
        Optional<BpmProcess> optinal = processRepository.findById(processId);
        if (optinal.isPresent()) {
            BpmProcess process = optinal.get();
            String processStageNameEn = process.getProcessStage().getNameEn();
            return bpmActInstVariableTaskConfigRepository.findByProcessKeyAndTaskDefKeyAndVariableNameAndStatus(
                processStageNameEn + "-" + process.getNameEn(), taskDefKey, variableName, EntityStatus.ACTIVE);
        } else {
            return null;
        }
    }

    /**
     * 根据流程key，变量名获取变量信息
     */
    @Override
    public BpmActInstVariableConfig getVariableByVariableName(Long orgId, Long projectId, String variableName,
                                                              Long processId) {
        Optional<BpmProcess> optinal = processRepository.findById(processId);
        if (optinal.isPresent()) {
            BpmProcess process = optinal.get();
            String processStageNameEn = process.getProcessStage().getNameEn();
            return bpmActInstVariableRepository.findByNameAndProcessKey(variableName,
                processStageNameEn + "-" + process.getNameEn());
        } else {
            return null;
        }
    }

    /**
     * 保存流程变量值
     */
    @Override
    public BpmActInstVariableValue saveBpmActInstVariableValue(BpmActInstVariableValue variableValue) {
        return bpmActInstVariableValueRepository.save(variableValue);
    }

    /**
     * 根据流程实例id及变量名查询变量值
     */
    @Override
    public BpmActInstVariableValue findBpmActInstVariableValue(Long orgId, Long projectId, Long actInstId,
                                                               String variableName) {
        return bpmActInstVariableValueRepository.findByOrgIdAndProjectIdAndActInstIdAndVariableName(orgId, projectId,
            actInstId, variableName);
    }

    /**
     * 获取待办任务任务包列表
     */
    @Override
    public List<TaskPackageDTO> getTaskPackages(Long orgId, Long projectId, Long assignee) {

        List<TaskPackageDTO> taskPackageList = ruTaskRepository.getTaskPackageList(orgId, projectId, assignee.toString());
        return taskPackageList;
    }

    /**
     * 获取待办任务列表
     */
    @Override
    public Page<TodoTaskDTO> getRuTaskList(Long orgId, Long projectId, Long assignee,
                                           TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO) {

        List<Long> entityIdList = findEntityIdListForRuTask(orgId, projectId, taskCriteria.getQrcode());
        List<String> taskPackageList = taskCriteria.getTaskPackageList();

        Page<TodoTaskDTO> page = ruTaskRepository.getTodoTaskList(orgId, projectId, assignee.toString(), taskPackageList, taskCriteria, pageDTO, entityIdList);

        if (taskCriteria.getClientType() != null
            && !"".equals(taskCriteria.getClientType())) {
            for (TodoTaskDTO dto : page.getContent()) {
                BpmProcessTaskNodeEnableConfig config = bpmProcessTaskNodeEnableConfigRepository.findByProcessAndProcessStageAndTaskDefKeyAndType(
                    dto.getProcess(), dto.getProcessStage(), dto.getTaskDefKey(), taskCriteria.getClientType());
                if (config != null) {
                    dto.setEnable(config.getEnable());
                }

                ProjectNode projectNode = projectNodeRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, dto.getEntityId()).orElse(null);
                //如果没有此项目节点
                if (projectNode != null && projectNode.getEntityType() != null) {
                    Drawing drawing = findDrawing(projectNode.getEntityType(), dto.getEntityId(), orgId, projectId);
                    if (drawing != null) {
                        dto.setInstallationDrawingName(drawing.getDrawingTitle());
                        dto.setInstallationDrawingNo(drawing.getDwgNo());
                    }
                }
            }
        }

        return page;
    }

    public Drawing findDrawing(String wbsEntityType, Long entityId, Long orgId, Long projectId) {
        String drawingNo = "";
        switch (wbsEntityType) {

            default:
                drawingNo = "";

        }

        return drawingRepository.findByOrgIdAndProjectIdAndDwgNoAndStatus(
            orgId,
            projectId,
            drawingNo,
            EntityStatus.ACTIVE
        ).orElse(null);

    }

    /**
     * 返回运行任务的实体列表
     *
     * @param orgId     组织id
     * @param projectId 项目ID
     * @param qrcode    二维码
     */
    @Override
    public List<Long> findEntityIdListForRuTask(Long orgId, Long projectId, String qrcode) {
        if (qrcode == null
            || "".equals(qrcode)) {
            return null;

        }

        List<Long> result = new ArrayList<>();

        return result;
    }

    /**
     * 获取待办任务列表
     */
    @Override
    public List<TodoTaskMobileCriteriaDTO> getTodoTaskForMobile(Long orgId, Long projectId, Long assignee) {
        return ruTaskRepository.getTodoTaskForMobile(orgId, projectId, assignee.toString());
    }


    /**
     * 获取代办任务
     */
    @Override
    public BpmRuTask findBpmRuTaskById(Long bpmActTaskId) {
        Optional<BpmRuTask> op = ruTaskRepository.findById(bpmActTaskId);
        return op.orElse(null);
    }

    /**
     * 上传附件
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param temporaryFileNames 临时文件名
     * @return List 返回列表
     */
    @Override
    public List<ActReportDTO> uploadTemporaryFileNames(Long orgId, Long projectId, List<String> temporaryFileNames) {
        List<ActReportDTO> actReports = new ArrayList<>();

        for (String temporaryFileName : temporaryFileNames) {
            logger.error("任务基础 保存docs服务->开始");
            JsonObjectResponseBody<FileES> responseBody =
                uploadFeignAPI.save(orgId.toString(), projectId.toString(), temporaryFileName, new FilePostDTO());
            logger.error("任务基础 保存docs服务->结束");
            FileES fileES = responseBody.getData();
            if (fileES != null && fileES.getId() != null) {
                ActReportDTO actReportDTO = new ActReportDTO();
                actReportDTO.setFileId(LongUtils.parseLong(fileES.getId()));
                actReportDTO.setFilePath(fileES.getPath());
                actReports.add(actReportDTO);
            }
        }
        return actReports;
    }

    /**
     * 获取材料信息
     */
    @Override
    public TaskMaterialDTO getMaterial(Long actInstId) {
        Optional<BpmActivityInstanceBase> actInstOp = bpmActInstRepository.findById(actInstId);
        if (actInstOp.isPresent()) {
            BpmActivityInstanceBase actInst = actInstOp.get();
            ProjectNode pn = projectNodeRepository.findEntityByEntityId(actInst.getProjectId(),
                actInst.getOrgId(), actInst.getEntityId());
            if (pn != null) {
            }
        }
        return null;
    }

    /**
     * 根据二维码获取材料信息
     */
    @Override
    public MaterialInfoDTO materialInfo(Long orgId, Long projectId, String qrCode) {
        return null;
    }

    /**
     * 添加流程实例材料关联信息
     *
     * @param actInst 工作流实例
     */
    @Override
    public BpmActInstMaterial creatBpmActInstMaterial(BpmRuTask ruTask, List<MaterialInfoDTO> materialInfos, BpmActivityInstanceBase actInst) {
        BpmActInstMaterial actInstMaterial = new BpmActInstMaterial();
        actInstMaterial.setCreatedAt();
        actInstMaterial.setStatus(EntityStatus.ACTIVE);
        return bpmActInstMaterialRepository.save(actInstMaterial);
    }

    @Override
    public FileES uploadReportTodocs(Long orgId, Long projectId, String reportPath) {
        DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file", APPLICATION_PDF_VALUE,
            true, reportPath);
        MockMultipartFile fileItem1 = null;
        try {
            IOUtils.copy(new FileInputStream(reportPath), fileItem.getOutputStream());
            fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
        } catch (IOException e) {
            logger.error(e.toString());
//            e.printStackTrace(System.out);
            throw new BusinessError(); // TODO
        }

        // 将文件上传到文档服务器
        logger.error("任务管理1 上传docs服务->开始");

        JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI.uploadProjectDocumentFile(orgId.toString(),
            fileItem1);
        logger.error("任务管理1 上传docs服务->结束");
        logger.error("任务管理1 保存docs服务->开始");
        // 保存上传的文件
        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
            tempFileResBody.getData().getName(), new FilePostDTO());
        logger.error("任务管理1 保存docs服务->结束");
        // 更新历史记录信息
        FileES fileES = fileESResBody.getData();
        // reportHistory.setFileId(fileES.getId());
        // reportHistory.setFilePath(fileES.getPath());
        // reportHistoryService.save(operator, reportHistory);

        FileUtils.remove(reportPath);

        return fileES;
    }

    /**
     * 查询工序分类
     */
    @Override
    public BpmProcessCategory findProcessCategoryById(Long id) {
        Optional<BpmProcessCategory> op = bpmProcessCategoryRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    @Override
    public List<Long> getProcessCategoriesInHiTask(Long orgId, Long projectId, Long assignee) {
        return hiTaskinstRepository.getProcessCategoriesInHiTask(orgId, projectId, assignee.toString());
    }

    /**
     * 获取未安排任务实体
     */
    @Override
    public List<EntityNoBpmActivityInstanceDTO> getTodoTaskEntity(Long orgId, Long projectId, Long processId,
                                                                  Long entitySubTypeId, String keyWord, PageDTO pageDTO) {
        Optional<BpmEntitySubType> bpmEntityCategoryOpt = entitySubTypeRepository.findById(entitySubTypeId);
        String entitySubTypeTypeNameEn = "";
        String entitySubTypeNameEn = "";
        if (bpmEntityCategoryOpt.isPresent()) {
            BpmEntitySubType bpmEntityCategory = bpmEntityCategoryOpt.get();
            entitySubTypeTypeNameEn = bpmEntityCategory.getEntityType().getNameEn();
            entitySubTypeNameEn = bpmEntityCategory.getNameEn();
        }

        return bpmActInstRepository.getEntitiyTodo(orgId, projectId, processId,
            entitySubTypeId, entitySubTypeNameEn, entitySubTypeTypeNameEn, keyWord, pageDTO);
    }


    @Override
    public List<BpmRuTask> findBpmRuTaskByActInstId(Long actInstId) {
        return ruTaskRepository.findByActInstId(actInstId);
    }


    @Override
    public BpmActivityTaskNodePrivilege getBpmActivityTaskNodePrivilege(Long orgId, Long projectId,
                                                                        Long processId, String taskDefKey) {
        return bpmActivityTaskNodePrivilegeRepository.findByOrgIdAndProjectIdAndProcessIdAndTaskDefKeyAndStatus(orgId,
            projectId, processId, taskDefKey, EntityStatus.ACTIVE);
    }


    @Override
    public List<String> getEntityModuleNameInRuTask(String taskNode, String taskDefKey, Long processStageId, Long processId,
                                                    Long orgId, Long projectId, Long assignee) {
        return ruTaskRepository.getEntityModuleNameInRuTask(taskNode, taskDefKey, processStageId, processId, orgId, projectId,
            assignee.toString());
    }


    /**
     * 取得计划的施工场地信息
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param wbsEntryState 计划
     * @param processId     工序
     * @return
     */
    @Override
    public WBSEntryTeamWorkSiteDTO getWBSEntryTeamWorkSiteInfo(Long orgId, Long projectId, WBSEntry wbsEntry, WBSEntryState wbsEntryState, Long processId) {
        WBSEntryTeamWorkSiteDTO teamSiteDTO = new WBSEntryTeamWorkSiteDTO();

        if (wbsEntryState.getTaskPackageId() != null) {
            TaskPackageAssignSiteTeams assigneeSite = assignSiteTeamsRepository
                .findByProjectIdAndTaskPackageIdAndProcessId(projectId, wbsEntryState.getTaskPackageId(), processId);
            if (assigneeSite != null && assigneeSite.getTeamId() != null) {
                teamSiteDTO.setTeamId(assigneeSite.getTeamId());
                teamSiteDTO.setTeamName(assigneeSite.getTeamName());
                teamSiteDTO.setWorkSiteId(assigneeSite.getWorkSiteId());
                teamSiteDTO.setWorkSiteName(assigneeSite.getWorkSiteName());
                teamSiteDTO.setWorkSiteAddress(assigneeSite.getWorkSiteAddress());
            }
        } else {
            if (wbsEntry == null) {
                wbsEntry = wBSEntryRepository.findById(wbsEntryState.getWbsEntryId()).orElse(null);
            }
            if (wbsEntry == null) {
                throw new NotFoundError(wbsEntryState.getWbsEntryId().toString());
            }
            WBSEntry wbsEntryParent = wBSEntryRepository.findById(wbsEntry.getParentId()).orElse(null);
            if (wbsEntryParent != null && !wbsEntryParent.getDeleted()) {
                teamSiteDTO.setTeamId(wbsEntryState.getTeamId());
                teamSiteDTO.setWorkSiteId(wbsEntryState.getWorkSiteId());
                teamSiteDTO.setWorkSiteName(wbsEntryState.getWorkSiteName());
                teamSiteDTO.setTeamName(wbsEntryState.getTeamName());
            }
        }

//        Long companyId = null;
//        Organization team = null;
//        if (teamSiteDTO.getTeamId() != null) {
//            team = organizationFeignAPI
//                .details(teamSiteDTO.getTeamId(), orgId)
//                .getData();
//            if (team != null) {
//                companyId = team.getCompanyId();
//                teamSiteDTO.setTeamName(team.getName());
//            }
//        }
//        if (companyId == null) {
//            Organization org = organizationFeignAPI.details(orgId, null).getData();
//            if (org != null) {
//                companyId = org.getCompanyId();
//            }
//        }
//        if (teamSiteDTO.getWorkSiteId() != null) {
//            WorkSite workSite = workSiteService.get(companyId, projectId, teamSiteDTO.getWorkSiteId());
//            if (workSite != null) {
//                teamSiteDTO.setWorkSiteName(workSite.getName());
//                teamSiteDTO.setWorkSiteAddress(workSite.getAddress());
//            }
//        }
        return teamSiteDTO;
    }



    /**
     * 取得流程花费的时间
     *
     * @param actInstId 流程ID
     * @return
     */
    @Override
    public double getProcessCostHour(Long actInstId) {
        double hours = 0;
        List<BpmActTask> list = bpmActTaskRepository.findByActInstId(actInstId);
        for (BpmActTask actTask : list) {
            if (actTask.getCostHour() != null) {
                hours += actTask.getCostHour();
            }
        }
        return hours;
    }


    /**
     * 船检计划执行异步任务
     *
     * @param projectId             项目ID
     * @param operatorDTO           操作者DTO
     * @param entityId              实体ID
     * @param processId             工序ID
     * @param bpmActivityInstanceId 流程实例ID
     * @param approved              完成
     * @param hours                 消耗的时间
     * @return
     */
    @Override
    public BpmPlanExecutionHistory createBpmPlanExecutionHistory(Long projectId, OperatorDTO operatorDTO, Long entityId,
                                                                 Long processId, Long bpmActivityInstanceId,
                                                                 Boolean approved, double hours, Long version) {
        return createBpmPlanExecutionHistory(projectId, operatorDTO, entityId,
            processId, bpmActivityInstanceId, approved, hours, version, false, false);

    }

    /**
     * 船检计划执行异步任务
     *
     * @param projectId             项目ID
     * @param operatorDTO           操作者DTO
     * @param entityId              实体ID
     * @param processId             工序ID
     * @param bpmActivityInstanceId 流程实例ID
     * @param approved              完成
     * @param hours                 消耗的时间
     * @return
     */
    @Override
    public BpmPlanExecutionHistory createBpmPlanExecutionHistory(Long projectId, OperatorDTO operatorDTO, Long entityId,
                                                                 Long processId, Long bpmActivityInstanceId,
                                                                 Boolean approved, double hours,
                                                                 Long version, Boolean isHalt,
                                                                 Boolean forceStart) {
        BpmPlanExecutionHistory his = new BpmPlanExecutionHistory();
        his.setApproved(approved);
        his.setBpmActivityInstanceId(bpmActivityInstanceId);
        his.setCreatedAt();
        his.setEntityId(entityId);
        his.setExecutionState(BpmPlanExecutionState.UNDO);
        his.setOperator(operatorDTO.getId());
        his.setOperatorName(operatorDTO.getName());
        his.setProcessId(processId);
        his.setProjectId(projectId);
        his.setStatus(EntityStatus.ACTIVE);
        his.setServerUrl(serverConfig.getUrl());
        BigDecimal b = new BigDecimal(hours);
        his.setHalt(isHalt);
        his.setForceStart(forceStart);

        his.setHours(b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        his.setVersion(version);
        return bpmPlanExecutionHistoryRepository.save(his);
    }

    /**
     * 更改遗留问题 解决人清单
     *
     * @param actInst      流程实例
     * @param nextAssignee 下一个执行人
     */
    @Override
    public void modifyPunchlistMembers(BpmActivityInstanceBase actInst, Long nextAssignee) {
        Issue issue = issueFeignAPI.get(actInst.getOrgId(), actInst.getProjectId(), actInst.getEntityId()).getData();
        List<Long> members = new ArrayList<>();
        members.add(nextAssignee);
        IssueUpdateDTO issueUpdateDTO = new IssueUpdateDTO();
        BeanUtils.copyProperties(issue, issueUpdateDTO, "no", "attachment", "status", "properties");
        issueUpdateDTO.setStatus(EntityStatus.ACTIVE);
        issueUpdateDTO.setMembers(members);
        issueFeignAPI.update(actInst.getOrgId(), actInst.getProjectId(), issue.getId(), issueUpdateDTO);
    }

    @Override
    public void modifyTaskAssigneeByCategory(Long actInstId, String category, Long nextAssignee, String name,
                                             Long teamId) {
        List<BpmActTaskAssignee> assignees = bpmActTaskAssigneeRepository.findByActInstIdAndTaskCategory(actInstId, category);
        if (!assignees.isEmpty()) {
            for (BpmActTaskAssignee assignee : assignees) {
                assignee.setAssignee(nextAssignee);
                assignee.setAssigneeName(name);
                assignee.setTeamId(teamId);
                assignee.setLastModifiedAt();
                bpmActTaskAssigneeRepository.save(assignee);
            }
        }
    }

    private String toStrAssignees(List<Long> assignees) {
        List<String> strAssignees = new ArrayList<>();
        assignees.forEach(assignee -> {
            strAssignees.add(assignee.toString());
        });

        return String.join(",", strAssignees);
    }


    /**
     * 取得父级实体ID
     *
     * @param projectId 项目ID
     * @param entityId
     * @return
     */
    @Override
    public List<HierarchyNode> __getParentEntitiesByEntityId(Long projectId, Long entityId) {
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
                String[] hierarchyIdArray = path.substring(1).split("/");// List<String> stringA = Arrays.asList
                if (hierarchyIdArray != null && hierarchyIdArray.length > 0) {
                    Arrays.asList(hierarchyIdArray).forEach(hierarchyId ->
                    {
                        hierarchyIds.add(LongUtils.parseLong(hierarchyId));
                    });
                }
            }
            if (hierarchyIds.size() > 0) {
                return hierarchyRepository
                    .findByIdInAndDeletedIsFalseOrderBySortAsc(hierarchyIds);
                /*List<String> projectNodeIds = new ArrayList<String>();
                for (HierarchyNode parent : parentNodes) {
                    projectNodeIds.add(parent.getNode().getId());
                }
                if (projectNodeIds.size() > 0) {
                    String[] idsss = projectNodeIds.toArray(new String[0]);
                    return (List<ProjectNode>) projectNodeRepository.findByIdInAndDeletedIsFalse(idsss);
                }*/
            }
        }

        return new ArrayList<>();
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


    @Override
    public void saveReportFromAttachment(BpmEntityDocsMaterials bpmDoc, Long actInstId, Long taskId) {
        BpmActTask actTask = bpmActTaskRepository.findById(taskId).orElse(null);
        List<ActReportDTO> reports = actTask.getJsonAttachmentsReadOnly();
        if (reports.size() > 0) {
            bpmDoc.setJsonDocs(reports);
            docsMaterialsRepository.save(bpmDoc);
        }
    }


    @Override
    public BpmActivityInstanceBase getBpmActivityInstanceByEntityId(Long orgId, Long projectId, Long entityId) {
        List<BpmActivityInstanceBase> actInstList = bpmActInstRepository.findByProjectIdAndEntityIdAndFinishStateAndSuspensionState(
            projectId, entityId, ActInstFinishState.NOT_FINISHED, SuspensionState.ACTIVE);
        if (actInstList.isEmpty()) {
            return null;
        }
        return actInstList.get(0);
    }


    @Override
    public List<BpmActTaskAssignee> getTaskAssigneesByTaskCategory(Long actInstId, String category) {
        return bpmActTaskAssigneeRepository.findByActInstIdAndTaskCategory(actInstId, category);
    }


    @Override
    public void saveReportFromDocuments(BpmEntityDocsMaterials bpmDoc, Long actInstId, Long taskId) {
        BpmActTask actTask = bpmActTaskRepository.findById(taskId).orElse(null);
        List<ActReportDTO> reports = actTask.getJsonDocumentsReadOnly();
        if (reports.size() > 0) {
            bpmDoc.setJsonDocs(reports);
            docsMaterialsRepository.save(bpmDoc);
        }
    }



    @Override
    public BpmEntityDocsMaterials saveBpmEntityDocsMaterials(BpmEntityDocsMaterials bpmDoc) {
        return docsMaterialsRepository.save(bpmDoc);
    }


    /**
     * 保存流程上的变量
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param toDoTaskDTO 待办任务DTO
     * @param id          任务ID
     */
    @Override
    public void saveVariableValue(Long orgId, Long projectId, TodoTaskExecuteDTO toDoTaskDTO, Long id) {
        BpmRuTask bpmRuTask = this.findBpmRuTaskById(id);
        if (bpmRuTask == null) {
            return;
        }
        Long taskId = bpmRuTask.getId();

        Long processKey = 0L;
//        JsonObjectResponseBody<BpmActReProcdef> responseProcDef = todoTaskFeignAPI.getTaskProcessKey(actTaskId);
//        BpmActReProcdef procDef = responseProcDef.getData();
//        if (procDef != null) {
//            processKey = LongUtils.parseLong(procDef.getKey());
//        }

//        JsonObjectResponseBody<BpmActRuTask> feignResponse = todoTaskFeignAPI.getTodoTask(actTaskId);
//        BpmActRuTask ruTask = feignResponse.getData();

        Map<String, Object> variables = toDoTaskDTO.getVariables();
        if (variables != null) {
            Set<String> keySet = variables.keySet();
            Iterator<String> keyIyerator = keySet.iterator();
            while (keyIyerator.hasNext()) {
                String key = keyIyerator.next();

                BpmActInstVariableConfig variable = this.getVariableByVariableName(orgId, projectId, key,
                    processKey);
                if (variable != null) {
                    BpmActInstVariableValue variableValue = this.findBpmActInstVariableValue(orgId, projectId,
                        bpmRuTask.getActInstId(), key);
                    if (variableValue == null) {
                        variableValue = new BpmActInstVariableValue();
                        variableValue.setCreatedAt();
                        variableValue.setLastModifiedAt();
                        variableValue.setOrgId(orgId);
                        variableValue.setActInstId(bpmRuTask.getActInstId());
                        variableValue.setProjectId(projectId);
                        variableValue.setStatus(EntityStatus.ACTIVE);
                        variableValue.setVariableDisplayName(variable.getDisplayName());
                        variableValue.setVariableName(variable.getName());
                        variableValue.setVariableType(variable.getType());
                    } else {
                        variableValue.setLastModifiedAt();
                    }
                    variableValue.setValue(variables.get(key).toString());
                    this.saveBpmActInstVariableValue(variableValue);
                }
            }
        }
    }

    @Override
    public List<HierarchyStageProcessDTO> getStageProcessesInRuTask(
        Long orgId,
        Long projectId,
        Long assignee,
        String taskDefKey
    ) {

        return ruTaskRepository.getStageProcessesInRuTask(orgId, projectId, assignee.toString(), taskDefKey);
    }


    /**
     * 匹配焊接工艺。
     *
     * @param positionA 位置1
     * @param positionB 位置2
     */
    @Override
    public Boolean positionMatch(String positionA, String positionB) {
        if (positionA != null && positionB != null) {
            if (positionA.equals("ALL") | positionB.equals("ALL")) {
                return true;
            } else if (positionA.equals(positionB)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 匹配焊接位置。
     *
     * @param processA 焊接工艺1
     * @param processB 焊接工艺2
     */
    @Override
    public Boolean processMatch(String processA, String processB) {
        if (processA != null & processB != null) {
            if (processA.equals(processB)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 获得图纸文件。
     *
     * @param actInsta
     * @param taskDefKey
     * @return
     */
    @Override
    public ActReportDTO getDrawing(BpmActivityInstanceBase actInsta, String taskDefKey) {
        Long drawingId = actInsta.getEntityId();
        String version = actInsta.getVersion();
        Optional<DrawingDetail> opd = drawingDetailRepository
            .findByDrawingIdAndRevAndStatus(drawingId, version, EntityStatus.ACTIVE);
        if (opd.isPresent()) {
            DrawingDetail detail = opd.get();
            ActReportDTO dto = new ActReportDTO();
            if (detail.getIssueFileId() != null && detail.getIssueFileId() != 0L) {
                dto.setFileId(detail.getIssueFileId());
                dto.setFilePath(detail.getIssueFilePath());
                return dto;
            }/* else if (detail.getFileId() != null && detail.getFileId() != 0L) {
                dto.setFileId(detail.getFileId());
                dto.setFilePath(detail.getFilePath());
                return dto;
            } */else {
                return null;
            }

        }
        return null;
    }


    //设置流程实例的版本
    //设置流程的版本 0，1，2
    @Override
    public BpmActivityInstanceBase setActInstVersion(BpmActivityInstanceBase actInst, ActivityInstanceDTO actInstDTO, Long orgId, Long projectId) {
        if (!StringUtils.isEmpty(actInstDTO.getVersion())) {
            actInst.setVersion(actInstDTO.getVersion());
        } else {
            List<BpmActivityInstanceBase> actInstList = bpmActInstRepository.findByProjectIdAndEntityIdAndProcessStageAndProcessOrderByVersionDesc(
                projectId,
                actInstDTO.getEntityId(),
                actInstDTO.getProcessStage(),
                actInstDTO.getProcess()
            );
            if (actInstList.isEmpty()) {
                actInst.setVersion("0");
            } else {
                try {
                    actInst.setVersion("" + (Integer.parseInt(actInstList.get(0).getVersion()) + 1));
                } catch (Exception e) {
                    logger.error(e.toString());
//                    e.printStackTrace(logger);
                    actInst.setVersion(actInstList.get(0).getVersion() + 1);
                }
            }
        }
        return actInst;
    }


    /**
     * 取得图纸设计的任务信息，并创建更新图纸详情。
     *
     * @param createResult 创建流程实例
     * @return
     */
    @Override
    public CreateResultDTO getDrawingActInstInfo(CreateResultDTO createResult) {
        Long orgId = createResult.getOrgId();
        Long projectId = createResult.getProjectId();
        ActivityInstanceDTO actInstDTO = createResult.getActInstDTO();
        Drawing drawing;

        // 找到图纸信息
        Optional<Drawing> opDrawing = drawingRepository.findById(actInstDTO.getEntityId());
        // 如果图纸信息存在
        if (!opDrawing.isPresent()) {
            createResult.setCreateResult(false);
            createResult.setErrorDesc("找不到图纸信息。");
            return createResult;
        }

        // 保存图纸信息
        drawing = opDrawing.get();

        // 查看是否存在已经运行的图纸任务
//        List<BpmActivityInstance> actInst = bpmActInstRepository.findByOrgIdAndProjectIdAndEntityIdAndFinishStateAndSuspensionState(
//            orgId, projectId, drawing.getId(), ActInstFinishState.NOT_FINISHED, SuspensionState.ACTIVE);
//        if (!actInst.isEmpty()) {
//            createResult.setCreateResult(false);
//            createResult.setErrorDesc("当前图集已经有正在运行的任务。");
//            return createResult;
//        }

        // 判断图纸任务版本不能存在历史版本中
//        List <DrawingDetail> drawingDetailHistory = drawingDetailRepository.findByDrawingIdAndRev(drawing.getId(),createResult.getActInstDTO().getVersion());
//
//        if (drawingDetailHistory!=null && drawingDetailHistory.size()>0) {
//            createResult.setCreateResult(false);
//            createResult.setErrorDesc("当前图集任务版本已存在。");
//            return createResult;
//        }

        drawing.setLocked(false);
//        drawing.setCurrentProcessNameEn(actInstDTO.getProcess());
        drawing.setLatestRev(actInstDTO.getVersion());
        actInstDTO.setEntityNo(drawing.getDwgNo());
        actInstDTO.setDrawingTitle(drawing.getDrawingTitle());
        actInstDTO.setEntityType("SHOP_DRAWING");
        // 查找图纸实体类型
        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, drawing.getEntitySubType());

        BpmEntitySubType category = best;
        if (category != null) {
            actInstDTO.setEntitySubTypeId(category.getId());
            actInstDTO.setEntitySubType(category.getNameEn());
        }

        drawingRepository.save(drawing);
        // 通过图纸id和图纸最新版本查找图纸详情
        DrawingDetail opD = drawingDetailRepository
            .findByDrawingIdAndRev(drawing.getId(), actInstDTO.getVersion()).orElse(null);
        // 图纸详情不存在时
        if (opD == null) {
            DrawingDetail detail = new DrawingDetail();
            detail.setCreatedAt();
            detail.setRev(actInstDTO.getVersion());
            detail.setDrawingId(drawing.getId());
            detail.setStatus(EntityStatus.PENDING);
            detail.setOrgId(orgId);
            detail.setProjectId(projectId);
            drawingDetailRepository.save(detail);

            // 如果不包含子图纸

            if (!best.isSubDrawingFlg()) {
                DrawingHistory his = drawingHistoryRepository
                    .findByDrawingIdAndVersion(drawing.getId(), drawing.getLatestRev());
                if (his == null) {
                    his = new DrawingHistory();
                    his.setCreatedAt();
                    his.setDrawingId(drawing.getId());
                    his.setOrgId(orgId);
                    his.setProjectId(projectId);
                    his.setStatus(EntityStatus.ACTIVE);
                    his.setVersion(drawing.getLatestRev());
                    drawingHistoryRepository.save(his);
                }
            }
        } else {
            opD.setLastModifiedAt(new Date());
            opD.setStatus(EntityStatus.PENDING);
            drawingDetailRepository.save(opD);
        }
        createResult.setActInstDTO(actInstDTO);
        return createResult;
    }


    /**
     * 取得材料类的任务信息
     *
     * @param createResult 创建流程实例DTO
     * @return createResult
     */
    @Override
    public CreateResultDTO getMaterialActInstInfo(CreateResultDTO createResult) {
        Long orgId = createResult.getOrgId();
        Long projectId = createResult.getProjectId();
        ActivityInstanceDTO actInstDTO = createResult.getActInstDTO();

        // 跟据 entitySubType 取得 EntityCategory
        Optional<BpmEntitySubType> bpmEntityCategoryOpt = entitySubTypeRepository
            .findByProjectIdAndNameEnAndStatus(projectId, actInstDTO.getEntitySubType(),
                EntityStatus.ACTIVE);
        if (bpmEntityCategoryOpt.isPresent()) {
            BpmEntitySubType bpmEntityCategory = bpmEntityCategoryOpt.get();
            actInstDTO.setEntitySubTypeId(bpmEntityCategory.getId());
            actInstDTO.setEntityType(bpmEntityCategory.getEntityType().getNameEn());
            // actInstDTO.setEntityCategory(bpmEntityCategory.getNameEn());
        }

        createResult.setActInstDTO(actInstDTO);
        return createResult;
    }


    /**
     * 保存Checklist 信息到 工作流 文档表
     *
     * @param processId
     * @return
     */
    @Override
    public boolean saveCheckListToDocsMaterialTable(Long processId, Long entityId, Long projectId, BpmActivityInstanceBase actInst) {
        List<BpmProcessCheckList> checkList = processCheckListRepository
            .findByProcessId(processId);
        if (!checkList.isEmpty()) {
            for (BpmProcessCheckList cl : checkList) {
                BpmEntityDocsMaterials bedm = new BpmEntityDocsMaterials();
                bedm.setProjectId(projectId);
                bedm.setEntityNo(actInst.getEntityNo());
                bedm.setActInstanceId(actInst.getId());
                bedm.setCreatedAt();
                bedm.setLastModifiedAt();
                bedm.setProcessId(processId);
                bedm.setStatus(EntityStatus.ACTIVE);
                bedm.setEntityId(entityId);
                bedm.setType(ActInstDocType.CHECK_LIST);

                List<ActReportDTO> fileList = new ArrayList<>();
                ActReportDTO reportDTO = new ActReportDTO();
                reportDTO.setFileId(cl.getFileId());
                fileList.add(reportDTO);
                bedm.setJsonDocs(fileList);
                docsMaterialsRepository.save(bedm);
            }
        }
        return true;
    }


    /**
     * 指定bpmActTask的执行人
     *
     * @param bpmActTaskAssigneeMap
     * @param ruTask
     * @param currentExecutor
     * @param actInstId
     * @return
     */
    @Override
    public String setAssigneeForBpmRuTask(Map<String, BpmActTaskAssignee> bpmActTaskAssigneeMap,
                                          BpmRuTask ruTask,
                                          String currentExecutor,
                                          Long actInstId) {

        if (ruTask == null) return null;

//        BpmRuTask bpmRuTask = new BpmRuTask();
//        bpmRuTask.setTaskId(actRuTask.getId());
        Long assignee = null;
        if (bpmActTaskAssigneeMap.get(ruTask.getTaskDefKey()) != null) {
            assignee = bpmActTaskAssigneeMap.get(ruTask.getTaskDefKey()).getAssignee();
            if (assignee != null) {
                String assigneeName = "";
                assigneeName = getRedisKey(String.format(RedisKey.USER.getDisplayName(), assignee), 60 * 60 * 8);
                if (StringUtils.isEmpty(assigneeName)) {
                    assigneeName = userFeignAPI.get(assignee).getData().getName();
                    setRedisKey(String.format(RedisKey.USER.getDisplayName(), assignee), assigneeName);
                }
                currentExecutor = assigneeName;
            }

        }
        ruTask.setAssignee(LongUtils.toString(assignee));
//        bpmRuTask.setCategory(actRuTask.getCategory());
//        bpmRuTask.setTaskType(actRuTask.getFormKey());//taskType
//        bpmRuTask.setCreateTime(actRuTask.getCreateTime());
//        bpmRuTask.setDelegation(actRuTask.getDelegation());
//        bpmRuTask.setDescription(actRuTask.getDescription());
//        bpmRuTask.setName(actRuTask.getName());
//        bpmRuTask.setOwner(actRuTask.getOwner());
//        bpmRuTask.setParentTaskId(actRuTask.getParentTaskId());
//        bpmRuTask.setActInstId(actRuTask.getActInstId());
//        bpmRuTask.setSuspensionState(actRuTask.getSuspensionState());
//        bpmRuTask.setTaskDefKey(actRuTask.getTaskDefKey());
//        bpmRuTask.setTenantId(actRuTask.getTenantId());
        ruTaskRepository.save(ruTask);
//        saveBpmRuTask(bpmRuTask);

//        if (assignee != null) {
//            actTaskFeignAPI.assignee(actInstId, ruTask.getTaskDefKey(), ruTask.getName(),
//                assignee);
//        }
        return currentExecutor;
    }

    @Override
    public void updateActivityExecuteResultForWeldEntity(Long orgId, Long projectId, Long entityId,
                                                         ActivityExecuteResult ndtResult,
                                                         ActivityExecuteResult pmiResult) {
//        WeldEntity weldEntity = weldEntityRepository.findByIdAndDeletedIsFalse(entityId);
//        if (weldEntity != null) {
//            if (ndtResult != null) {
//                weldEntity.setNdtResult(ndtResult);
//            } else if (pmiResult != null) {
//                weldEntity.setPmiResult(pmiResult);
//            }
//            weldEntityRepository.save(weldEntity);
//        }
    }


    /**
     * 为任务执行执行人
     *
     * @param ruTaskList
     * @return
     */
    @Override
    public String setTaskAssignee(List<BpmRuTask> ruTaskList) {
        String currentExecutor = "";
        for (BpmRuTask newRuTask : ruTaskList) {
            String assigneeName = "";
            if (newRuTask.getAssignee() != null) {
                Long assignee = LongUtils.parseLong(newRuTask.getAssignee());
                assigneeName = getRedisKey(String.format(RedisKey.USER.getDisplayName(), assignee), 60 * 60 * 8);
                if (StringUtils.isEmpty(assigneeName)) {
                    assigneeName = userFeignAPI.get(assignee).getData().getName();
                    setRedisKey(String.format(RedisKey.USER.getDisplayName(), assignee), assigneeName);
                }
                currentExecutor = assigneeName;
            }
        }
        return currentExecutor;
    }


    /**
     * 根据网关字符串判断 执行结果
     */
    @Override
    public String getInspectResult(String commandStr) {
        String inspectResultStr = null;
        if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_NO_COMMENT.equals(commandStr)) {
            inspectResultStr = "A";
        } else if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT.equals(commandStr)) {
            inspectResultStr = "C";
        } else if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_COMMENT.equals(commandStr)) {
            inspectResultStr = "B";
        } else if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_PENDING.equals(commandStr)) {
            inspectResultStr = "D";

        } else {
            inspectResultStr = "A";
        }

        return inspectResultStr;
    }

    /**
     * 根据网关字符串判断 报告状态
     */
    @Override
    public ReportStatus getReportStatus(String commandStr) {
        ReportStatus reportStatus = null;
        if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_NO_COMMENT.equals(commandStr)) {
            reportStatus = ReportStatus.DONE;
        } else if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT.equals(commandStr)) {
            reportStatus = ReportStatus.CANCEL;
        } else if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_COMMENT.equals(commandStr)) {
            reportStatus = ReportStatus.COMMENTS;
        } else {
            reportStatus = ReportStatus.DONE;
        }
//        else if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_PENDING.equals(commandStr)) {
//            reportStatus = ReportStatus.PENDING;

//        }


        return reportStatus;
    }


    @Override
    public boolean updateProcessStageCount(BpmRuTask ruTask, BpmActivityInstanceBase actInst) {

        if (ruTask != null && actInst != null) {
            String projectId = ruTask.getTenantId();
            Long processStageId = actInst.getProcessStageId();
            Long processId = actInst.getProcessId();
            String assignee = ruTask.getAssignee() == null ? "0" : ruTask.getAssignee();
//            String psCountKey = String.format(RedisKey.TASK_INFO_PROCESS_STAGE_COUNT.getDisplayName(), projectId, assignee);
//            String initField = "INIT";

            String taskPsCountKey = String.format(RedisKey.TASK_INFO_PROCESS_STAGE_COUNT.getDisplayName(), projectId, assignee);

            hincrBy(taskPsCountKey, processStageId.toString(), -1L);

            String psCount = hget(taskPsCountKey, processStageId.toString());
            if ("0".equals(psCount)) {
                hdel(taskPsCountKey, processStageId.toString());
            }

            String taskPrCountKey = String.format(RedisKey.TASK_INFO_PROCESS_COUNT.getDisplayName(), projectId, assignee);

            hincrBy(taskPrCountKey, processId.toString(), -1L);

            String prCount = hget(taskPrCountKey, processId.toString());
            if ("0".equals(prCount)) {
                hdel(taskPrCountKey, processId.toString());
            }
            return true;

        }
        return false;
    }
}
