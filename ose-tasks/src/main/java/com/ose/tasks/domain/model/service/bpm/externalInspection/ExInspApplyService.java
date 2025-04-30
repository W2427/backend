package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.feign.RequestWrapper;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.bpm.ExInspActInstHandleHistoryRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.bpm.ExInspApplyCriteriaDTO;
import com.ose.tasks.dto.bpm.ExInspApplyDTO;
import com.ose.tasks.dto.bpm.ExInspApplyFilterConditionDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmExInspApply;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.report.ExInspActInstHandleHistory;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.bpm.ExInspApplyHandleType;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

/**
 * 用户服务。
 */
@Component
public class ExInspApplyService implements ExInspApplyInterface {

    private final static Logger logger = LoggerFactory.getLogger(ExInspApplyService.class);



    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final BpmRuTaskRepository ruTaskRepository;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final ExInspActInstHandleHistoryRepository exInspActInstHandleHistoryRepository;

    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    /**
     * 构造方法。
     */
    @Autowired
    public ExInspApplyService(
        BpmRuTaskRepository ruTaskRepository,
        TodoTaskDispatchInterface todoTaskDispatchService,
        ExInspActInstHandleHistoryRepository exInspActInstHandleHistoryRepository,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService) {
        this.ruTaskRepository = ruTaskRepository;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.exInspActInstHandleHistoryRepository = exInspActInstHandleHistoryRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
    }

    /**
     * 外检申请&外检报告做成
     */
    @Override
    public boolean externalInspectionApply(
        ContextDTO context,
        Long orgId,
        Long projectId,
        OperatorDTO operator,
        ExInspApplyDTO applyDTO) {

//        if(applyDTO.getExternalInspectionTime().after(new Date())){
//            throw new BusinessError("外检时间不得晚于当前时间");
//        }

        if (!context.isContextSet()) {
            String authorization = context.getAuthorization();
            String userAgent = context.getUserAgent();

            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                context.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }
        List<String> entityNos = new ArrayList<>();
        List<Long> actInstIds = new ArrayList<>();
        List<Long> taskIds = new ArrayList<>();
        if (applyDTO.getExternalInspectionApplyList() != null && applyDTO.getExternalInspectionApplyList().size() > 0) {
            for (BpmExInspApply bpmExInspApply : applyDTO.getExternalInspectionApplyList()) {
                entityNos.add(bpmExInspApply.getEntityNo());
                actInstIds.add(bpmExInspApply.getActInstId());
                taskIds.add(bpmExInspApply.getTaskId());
            }
        }

        List<ExInspActInstHandleHistory> exInspActInstHandleHistories = exInspActInstHandleHistoryRepository.findByOrgIdAndProjectIdAndTypeAndRunningStatus(
            orgId,
            projectId,
            ExInspApplyHandleType.EXTERNAL_INSPECTION_APPLY,
            EntityStatus.ACTIVE
        );
        if (exInspActInstHandleHistories.size() > 27) {
            throw new BusinessError("当前项目存在正在进行的外检申请");
        }

        List<ExInspActInstHandleHistory> exInspActInstHandleHistoriesExited = exInspActInstHandleHistoryRepository.findByOrgIdAndProjectIdAndTypeAndRunningStatusAndEntityNosIn(
            orgId,
            projectId,
            ExInspApplyHandleType.EXTERNAL_INSPECTION_APPLY,
            EntityStatus.ACTIVE,
            entityNos
        );
        if (exInspActInstHandleHistoriesExited.size() > 0) {
            throw new BusinessError("当前项目存在正在进行的外检申请");
        }

        ExInspActInstHandleHistory exInspActInstHandleHistory = new ExInspActInstHandleHistory();
        exInspActInstHandleHistory.setOrgId(orgId);
        exInspActInstHandleHistory.setProjectId(projectId);
        exInspActInstHandleHistory.setExInspScheduleNo(applyDTO.getName());
        exInspActInstHandleHistory.setCreatedAt(new Date());
        exInspActInstHandleHistory.setCreatedBy(context.getOperator().getId());
        exInspActInstHandleHistory.setLastModifiedAt(new Date());
        exInspActInstHandleHistory.setLastModifiedBy(context.getOperator().getId());
        exInspActInstHandleHistory.setRunningStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setJsonEntityNos(entityNos);
        exInspActInstHandleHistory.setJsonActInstIds(actInstIds);
        exInspActInstHandleHistory.setRemarks("外检申请编号: " + applyDTO.getName());
        exInspActInstHandleHistory.setType(ExInspApplyHandleType.EXTERNAL_INSPECTION_APPLY);
        exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);

        for (Long taskId : taskIds) {
            BpmRuTask bpmRuTask = ruTaskRepository.findById(taskId).orElse(null);
            if (bpmRuTask != null) {
                bpmRuTask.setHandling(true);
                ruTaskRepository.save(bpmRuTask);
            }
        }


        Project project = projectService.get(orgId, projectId);

        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.EXTERNAL_INSPECTION_APPLY,
            false,
            context,
            batchTask -> {
                try {
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", orgId);
                    data.put("projectId", projectId);
                    todoTaskDispatchService.batchExec(context, data, applyDTO);
                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(context.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.APPROVED);
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                } catch (Exception e) {
                    for (Long taskId : taskIds) {
                        BpmRuTask bpmRuTask = ruTaskRepository.findById(taskId).orElse(null);
                        if (bpmRuTask != null) {
                            bpmRuTask.setHandling(false);
                            ruTaskRepository.save(bpmRuTask);
                        }
                    }
                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(context.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.DISABLED);
                    exInspActInstHandleHistory.setErrors(e.getMessage());
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                }

                return new BatchResultDTO();
            }
        );




        return false;
    }

    /**
     * 批量执行外检申请，有协调员
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param operator
     * @param applyDTO
     * @return
     */
    private Map<String, String> completeExternalInspectionApplyTaskBatchCoordinate(ContextDTO context,
                                                                                   Long orgId,
                                                                                   Long projectId,
                                                                                   String operator,
                                                                                   ExInspApplyDTO applyDTO,
                                                                                   String coordinateOption) {

        List<BpmExInspApply> applyList = applyDTO.getExternalInspectionApplyList();
        Set<Long> actTaskidList = new HashSet<>();
        for (BpmExInspApply apply : applyList) {
        }


        Map<String, Object> result = new HashMap<>();

        result.put(BpmCode.EXCLUSIVE_GATEWAY_RESULT, coordinateOption);

        applyDTO.setCommand(result);
        applyDTO.setTaskIds(new ArrayList<>(actTaskidList));
        Map<String, Object> data = new HashMap<>();
        data.put("orgId", orgId);
        data.put("projectId", projectId);

        return todoTaskDispatchService.batchExec(context, data, applyDTO).getCompleteTasksMap();
    }

    /**
     * 查询外检申请列表
     */
    @Override
    public Page<BpmExInspApply> getExternalInspectionApplyList(Long orgId, Long projectId,
                                                               List<Long> assignees, ExInspApplyCriteriaDTO criteriaDTO) {
        List<String> strAssignees = new ArrayList<>();
        assignees.forEach(assignee -> {
            strAssignees.add(assignee.toString());
        });
        return ruTaskRepository.getExternalInspectionApplyList(orgId, projectId, strAssignees, criteriaDTO);
    }


    /**
     * 获取外检申请页面检索数据
     */
    @Override
    public ExInspApplyFilterConditionDTO getExternalInspectionApplyFilterCondition(Long orgId,
                                                                                   Long projectId, List<Long> assignees) {
        return ruTaskRepository.getExternalInspectionApplyFilterCondition(orgId, projectId, assignees);
    }

}
