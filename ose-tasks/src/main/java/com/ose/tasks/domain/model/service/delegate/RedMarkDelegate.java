package com.ose.tasks.domain.model.service.delegate;

import com.ose.auth.api.RoleFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.Role;
import com.ose.auth.entity.UserProfile;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.util.CryptoUtils;
import com.ose.vo.BpmTaskDefKey;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.drawing.DrawingPackageReturnDTO;
import com.ose.tasks.dto.drawing.SubDrawingCriteriaDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.util.*;

import static com.ose.tasks.vo.setting.BatchTaskCode.DRAWING_PACKAGE;
import static com.ose.tasks.vo.setting.BatchTaskCode.EFFECTIVE_DRAWING_PACKAGE;

/**
 * 图纸 REDMARK 任务代理
 */
@Component
@Lazy
public class RedMarkDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final static Logger logger = LoggerFactory.getLogger(RedMarkDelegate.class);

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingRepository drawingRepository;

    private final DrawingBaseInterface drawingBaseService;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final UserFeignAPI userFeignAPI;

    private final RoleFeignAPI roleFeignAPI;

    private final TaskRuleCheckService taskRuleCheckService;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository;

    private final ProjectInterface projectService;

    private final DrawingDetailRepository drawingDetailRepository;

    private final BpmRuTaskRepository ruTaskRepository;


    private final BatchTaskInterface batchTaskService;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final DrawingHistoryRepository drawingHistoryRepository;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final BpmActTaskRepository bpmActTaskRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public RedMarkDelegate(
        BpmActivityInstanceRepository bpmActInstRepository,
        SubDrawingRepository subDrawingRepository,
        DrawingRepository drawingRepository,
        BpmRuTaskRepository ruTaskRepository,
        DrawingBaseInterface drawingBaseService,
        TodoTaskBaseInterface todoTaskBaseService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") RoleFeignAPI roleFeignAPI,
        TaskRuleCheckService taskRuleCheckService,
        BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository,
        ProjectInterface projectService,
        DrawingDetailRepository drawingDetailRepository,
        BatchTaskInterface batchTaskService,
        BpmEntityDocsMaterialsRepository docsMaterialsRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        DrawingHistoryRepository drawingHistoryRepository,
        BpmHiTaskinstRepository hiTaskinstRepository,
        BpmActTaskRepository bpmActTaskRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.subDrawingRepository = subDrawingRepository;
        this.drawingRepository = drawingRepository;
        this.drawingBaseService = drawingBaseService;
        this.todoTaskBaseService = todoTaskBaseService;
        this.userFeignAPI = userFeignAPI;
        this.roleFeignAPI = roleFeignAPI;
        this.taskRuleCheckService = taskRuleCheckService;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.bpmDrawingSignTaskRepository = bpmDrawingSignTaskRepository;
        this.projectService = projectService;
        this.drawingDetailRepository = drawingDetailRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.batchTaskService = batchTaskService;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.drawingHistoryRepository = drawingHistoryRepository;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.bpmActTaskRepository = bpmActTaskRepository;
    }

    @Override
    public CreateResultDTO preCreateActInst(
        CreateResultDTO createResult
    ) {

        Long orgId = createResult.getOrgId();
        Long projectId = createResult.getProjectId();

        if (createResult.getActInstDTO() == null || createResult.getActInstDTO().getEntityId() == null) {
            createResult.setCreateResult(false);
            createResult.setErrorDesc("未选择实体创建REDMARK流程");
            return createResult;
        }
        Long drawingId = createResult.getActInstDTO().getEntityId();

        Drawing opDrawing = drawingRepository.findById(drawingId).orElse(null);
        if (opDrawing == null) {
            createResult.setCreateResult(false);
            createResult.setErrorDesc("图纸不存在");
            return createResult;
        }

        if (opDrawing.getLatestApprovedRev() == null || !createResult.getActInstDTO().getVersion().equals(opDrawing.getLatestApprovedRev())) {
            createResult.setCreateResult(false);
            createResult.setErrorDesc("请输入图纸当前最新有效版本若不存在请先执行出图任务");
            return createResult;
        }


        List<EntityStatus> statusList = new ArrayList<>();

        statusList.add(EntityStatus.PENDING);
        List<DrawingDetail> drawingDetails = drawingDetailRepository.findByOrgIdAndProjectIdAndDrawingIdAndStatusIn(orgId, projectId, createResult.getActInstDTO().getEntityId(), statusList);
        if (drawingDetails != null && drawingDetails.size() > 0) {
            createResult.setErrorDesc("本图纸正在出图或升版，不能进行RedMark");
            createResult.setCreateResult(false);
        }

//        opDrawing.setCurrentProcessNameEn(createResult.getProcess().getNameEn());
        drawingRepository.save(opDrawing);
        return createResult;
    }

    /**
     * 预处理。
     *
     * @param contextDTO
     * @param data
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO preExecute(
        ContextDTO contextDTO,
        Map<String, Object> data,
        ExecResultDTO execResult
    ) {


        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        execResult = drawingBaseService.checkRuTask(execResult);
        BpmRuTask ruTask = execResult.getRuTask();

        if (execResult.getActInst() == null || execResult.getActInst().getId() == null) {
            execResult.setExecResult(false);
            return execResult;
        }
        Drawing drawing = drawingRepository.findById(execResult.getActInst().getEntityId()).orElse(null);

        Long actInstId = execResult.getActInst().getId();

        String taskDefKey = ruTask.getTaskDefKey();


        if (BpmTaskDefKey.USERTASK_REDMARK_DESIGN.getType().equalsIgnoreCase(taskDefKey)) {
            execResult = redMarkDesignPreExec(orgId, projectId, actInstId, execResult);
        }

        if (BpmTaskDefKey.USERTASK_REDMARK_CHECK.getType().equalsIgnoreCase(taskDefKey)) {
            execResult = checkNodePreExec(orgId, projectId, actInstId, drawing.getId(), execResult);
        }


        if (BpmTaskDefKey.USERTASK_REDMARK_MODIFY.getType().equalsIgnoreCase(taskDefKey)) {
            execResult = modifyNodePreExec(orgId, projectId, actInstId, drawing, execResult);
        }


        if (BpmTaskDefKey.USERTASK_REDMARK_RECEIPT.getType().equalsIgnoreCase(taskDefKey)) {

            if (isDrawingAssignTaskDone(contextDTO, execResult)) {

                execResult.setExecResult(true);

                PageDTO page = new PageDTO();
                page.setFetchAll(true);
                SubDrawingCriteriaDTO criteriaDTO = new SubDrawingCriteriaDTO();
                criteriaDTO.setDrawingVersion(drawing.getLatestRev());
                criteriaDTO.setActInstId(actInstId);
                Page<SubDrawing> result = subDrawingRepository.searchLatestSubDrawing(orgId, projectId, drawing.getId(), criteriaDTO, page.toPageable());
                List<SubDrawing> subDrawingList = result.getContent();
                for (SubDrawing subDrawing : subDrawingList) {
                    subDrawing.setTaskId(null);
                    subDrawing.setIsRedMark(EntityStatus.ACTIVE);
                }
                subDrawingRepository.saveAll(subDrawingList);

            } else {

                execResult.setExecResult(false);
            }
            updateDrawingAssignTask(contextDTO, execResult);
        }

        return execResult;
    }


    /**
     * 后执行任务接口。
     *
     * @param contextDTO
     * @param data
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO postExecute(
        ContextDTO contextDTO,
        Map<String, Object> data,
        ExecResultDTO execResult
    ) {

        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");

        Drawing drawing = drawingRepository.findById(execResult.getActInst().getEntityId()).orElse(null);
        Project project = projectService.get(orgId, projectId);
        Long drawingId = drawing.getId();
        BpmRuTask ruTask = execResult.getRuTask();
        String taskDefKey = ruTask.getTaskDefKey();
        Long actInstId = execResult.getRuTask().getActInstId();
        DrawingDetail drawingDetail = null;

        Optional<DrawingDetail> op = drawingDetailRepository.findByDrawingIdAndRevAndStatus(drawing.getId(), drawing.getLatestRev(), EntityStatus.ACTIVE);
        if (op.isPresent()) {
            drawingDetail = op.get();
        } else {
            return execResult;
        }


        if (BpmTaskDefKey.USERTASK_REDMARK_DESIGN.getType().equalsIgnoreCase(taskDefKey)) {
            execResult = redMarkDesignPostExec(orgId, projectId, drawingId, execResult);

            packSubFiles(orgId, project, actInstId, contextDTO.getOperator(), drawing, execResult, drawingDetail, contextDTO);
            if (execResult.getTodoTaskDTO().getNextAssignee() != null) {
                return nextAssign(execResult);
            } else {
                return execResult;
            }
        }


        if (BpmTaskDefKey.USERTASK_REDMARK_MODIFY.getType().equalsIgnoreCase(taskDefKey)) {
            Set<DrawingReviewStatus> inStatus = new HashSet<>();
            inStatus.add(DrawingReviewStatus.INIT);
            subDrawingRepository.updateReviewStatusByDrawingIdAndDrawingVersion(orgId, projectId, DrawingReviewStatus.CHECK, drawing.getId(), drawing.getLatestRev(), inStatus);
            packSubFiles(
                orgId,
                project,
                actInstId,
                contextDTO.getOperator(),
                drawing,
                execResult,
                drawingDetail,
                contextDTO
            );
        }


        if (BpmTaskDefKey.USERTASK_REDMARK_CHECK.getType().equalsIgnoreCase(taskDefKey)) {
            execResult = redMarkCheckPostExec(orgId, projectId, drawingId, execResult, contextDTO);
        }
        return execResult;

    }

    /**
     * redmark校审节点处理。
     *
     * @param orgId
     * @param projectId
     * @param drawingId
     * @param execResult
     * @return
     */
    private ExecResultDTO redMarkCheckPostExec(
        Long orgId,
        Long projectId,
        Long drawingId,
        ExecResultDTO execResult,
        ContextDTO contextDTO) {

        List<Long> assignees = execResult.getTodoTaskDTO().getAssignees();


        Drawing drawing = drawingRepository.findById(execResult.getActInst().getEntityId()).orElse(null);

        Long actInstId = execResult.getRuTask().getActInstId();

        OperatorDTO operatorDTO = contextDTO.getOperator();
        Project project = projectService.get(orgId, projectId);

        DrawingDetail drawingDetail = null;


        Optional<DrawingDetail> op = drawingDetailRepository.findByDrawingIdAndRevAndStatus(drawing.getId(), drawing.getLatestRev(), EntityStatus.ACTIVE);
        if (op.isPresent()) {

        } else {
            return execResult;
        }


        if (execResult.getTodoTaskDTO() != null && execResult.getTodoTaskDTO().getCommand() != null
            && BpmCode.EXCLUSIVE_GATEWAY_RESULT_ACCEPT_SIGN.equals(execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT))) {
            updateRedMarkSubDwgReviewStatusWhereApproved(orgId, project.getId(), drawing.getId(), actInstId);

            subDrawingRepository.updateRedMarkSubDwgStatus(orgId, project.getId(), drawing.getId(), actInstId, EntityStatus.ACTIVE.name());

            logger.info(drawing.getDwgNo() + " : REDMARK流程审核节点开始打包图纸");
            packEffectiveSubFiles(
                orgId,
                project,
                actInstId,
                contextDTO.getOperator(),
                drawing,
                execResult,
                drawingDetail,
                contextDTO
            );
            logger.info(drawing.getDwgNo() + " : REDMARK流程审核节点结束打包图纸");

            setAssign(orgId, project.getId(), execResult, assignees, userFeignAPI);
        } else {
            subDrawingRepository.updateRedMarkSubDwgReviewStatus(orgId, projectId, drawingId, actInstId, DrawingReviewStatus.MODIFY.name());
        }







        return execResult;
    }

    /**
     * redmark图纸出图处理。
     *
     * @param orgId
     * @param projectId
     * @param drawingId
     * @param execResult
     * @return
     */
    private ExecResultDTO redMarkDesignPostExec(
        Long orgId,
        Long projectId,
        Long drawingId,
        ExecResultDTO execResult
    ) {

        Set<DrawingReviewStatus> reviewStatus = new HashSet<>();
        reviewStatus.add(DrawingReviewStatus.INIT);
        reviewStatus.add(DrawingReviewStatus.CHECK);
        reviewStatus.add(DrawingReviewStatus.REVIEW);
        Set<EntityStatus> status = new HashSet<>();
        status.add(EntityStatus.ACTIVE);
        status.add(EntityStatus.PENDING);
        Long actInstId = execResult.getRuTask().getActInstId();

        subDrawingRepository.updateRedMarkSubDwgReviewStatus(orgId, projectId, drawingId, actInstId, DrawingReviewStatus.CHECK.name());

        List<SubDrawing> subDrawings = subDrawingRepository.findByOrgIdAndProjectIdAndDrawingIdAndReviewStatusInAndStatusIn(
            orgId, projectId, drawingId, reviewStatus, status
        );

        if (CollectionUtils.isEmpty(subDrawings)) {
            Drawing drawing = drawingRepository.findById(drawingId).orElse(null);
            if (drawing == null) return execResult;
            drawing.setRedMarkOnGoing(false);
            drawingRepository.save(drawing);
        }

        return execResult;
    }

    /**
     * redmark图纸设计处理。
     *
     * @param orgId
     * @param projectId
     * @param actInstId
     * @param execResult
     * @return
     */
    private ExecResultDTO redMarkDesignPreExec(
        Long orgId,
        Long projectId,
        Long actInstId,
        ExecResultDTO execResult
    ) {


        return execResult;
    }

    /**
     * 图纸 CHECK 节点 预检查
     *
     * @param orgId
     * @param projectId
     * @param actInstId
     * @param execResult
     * @return
     */
    private ExecResultDTO checkNodePreExec(
        Long orgId,
        Long projectId,
        Long actInstId,
        Long drawingId,
        ExecResultDTO execResult
    ) {

        Tuple tuple = subDrawingRepository.
            findCheckStatusCount(orgId, projectId, actInstId, drawingId);

        if (tuple == null) {
            execResult.setExecResult(false);
            return execResult;
        }

        Long totalCount = ((BigDecimal) tuple.get("totalCount")).longValue();

        Long checkDoneCount = ((BigDecimal) tuple.get("checkDoneCount")).longValue();



        if (totalCount == 0L) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("Not Found");
            return execResult;
        }
        if (checkDoneCount < totalCount) {

            if (execResult.getTodoTaskDTO().getCommand() == null) {
                execResult.setExecResult(false);
                execResult.setErrorDesc("Not Found Gateway");
                return execResult;
            }
            Map<String, Object> command = execResult.getTodoTaskDTO().getCommand();
            if (command.values().contains("ACCEPT") || command.values().contains("CO_SIGN")) {
                execResult.setExecResult(false);
                execResult.setErrorDesc("Some sub drawing not DONE");
                return execResult;
            }

        }
        return execResult;
    }

    /**
     * 图纸 修改 节点 预检查
     *
     * @param orgId
     * @param projectId
     * @param actInstId
     * @param execResult
     * @return
     */
    private ExecResultDTO modifyNodePreExec(
        Long orgId,
        Long projectId,
        Long actInstId,
        Drawing drawing,
        ExecResultDTO execResult
    ) {

        Tuple tuple = subDrawingRepository.
            findNodeModifyStatusCount(orgId, projectId, drawing.getId(), drawing.getLatestRev());

        if (tuple == null) {
            execResult.setExecResult(false);
            return execResult;
        }

        Long totalCount = ((BigDecimal) tuple.get("totalCount")).longValue();
        Long nodeModifyCount = ((BigDecimal) tuple.get("modifyCount")).longValue();

        if (totalCount == 0L) {
            execResult.setExecResult(false);
            return execResult;
        } else if (nodeModifyCount > 0L) {

            execResult.setExecResult(false);
            execResult.setErrorDesc("Some sub drawing not MODIFIED");
            return execResult;
        }
        return execResult;
    }

    /**
     * 设置后续节点执行人。
     *
     * @param execResult
     * @return
     */
    private ExecResultDTO nextAssign(
        ExecResultDTO execResult
    ) {

        Long actInstId = execResult.getActInst().getId();
        for (int j = 0; j < execResult.getNextTasks().size(); j++) {
            ExecResultDTO nextExecResult = execResult.getNextTasks().get(j);
            if (nextExecResult == null || nextExecResult.getRuTask() == null || execResult.getNextTasks().get(j).getTodoTaskDTO() == null) {
                continue;
            }
            BpmRuTask nextTask = execResult.getNextTasks().get(j).getRuTask();
            String taskDefKey = nextTask.getTaskDefKey();
            String taskName = nextTask.getName();
            Long nextAssignee = execResult.getNextTasks().get(j).getTodoTaskDTO().getNextAssignee();
            if (nextAssignee != null && nextAssignee != 0L) {
                String name = "";
                try {
                    JsonObjectResponseBody<UserProfile> userResponse = userFeignAPI.get(nextAssignee);
                    name += ", " + userResponse.getData().getName();
                } catch (Exception e) {
                    JsonObjectResponseBody<Role> roleResponse = roleFeignAPI.get(execResult.getOrgId(), nextAssignee);
                    if (roleResponse.getData() != null) {
                        name += ", " + roleResponse.getData().getName();
                    }

                }
                if (name.length() > 0) {
                    name = name.substring(2);
                }

                BpmActivityTaskNodePrivilege nodePrivilege = todoTaskBaseService.
                    getBpmActivityTaskNodePrivilege(
                        execResult.getOrgId(),
                        execResult.getProjectId(),
                        execResult.getActInst().getProcessId(),
                        execResult.getRuTask().getTaskDefKey()
                    );
                String category = null;
                if (nodePrivilege != null) {
                    category = nodePrivilege.getSubCategory();
                }

                if (category != null) {
                    todoTaskBaseService.modifyTaskAssigneeByCategory(execResult.getRuTask().getActInstId(),
                        category,
                        nextAssignee,
                        name,
                        execResult.getTodoTaskDTO().getTeamId());
                }




                if (!taskRuleCheckService.isCounterSignTaskNode(taskDefKey, nextTask.getTaskType())) {

                    todoTaskBaseService.assignee(actInstId, taskDefKey, taskName,
                        nextAssignee, execResult.getActInst());
                }

                BpmActivityInstanceState actInstState = execResult.getActInstState();
                actInstState.setCurrentExecutor(name);
                bpmActivityInstanceStateRepository.save(actInstState);

                Map<String, Object> variableMap = new HashMap<>();
                variableMap.put("nextAssignee", nextAssignee);
                execResult.setVariables(variableMap);

            }
        }
        return execResult;
    }

    /**
     * redmark图纸会签节点（创建多条任务，并创建图纸会签记录）。
     *
     * @param orgId
     * @param projectId
     * @param execResult
     * @param assignees
     * @param userFeignAPI
     */
    public void setAssign(
        Long orgId,
        Long projectId,
        ExecResultDTO execResult,
        List<Long> assignees,
        UserFeignAPI userFeignAPI
    ) {

        logger.info("创建会签及待办任务->开始");

        List<ExecResultDTO> nextTasks = execResult.getNextTasks();
        BpmRuTask bpmRuTask = execResult.getRuTask();
        List<BpmRuTask> bpmRuTaskList = new ArrayList<>();
        List<BpmRuTask> ruTasks = new ArrayList<>();
        nextTasks.forEach(nextTask -> {
            ruTasks.add(nextTask.getRuTask());
        });

        for (BpmRuTask ruTask : ruTasks) {
            logger.info("创建会签及待办任务->查找待办任务");
            BpmRuTask bpmRuTaskOriginal = ruTaskRepository.findById(ruTask.getId()).orElse(null);
            logger.info("创建会签及待办任务->创建会签记录1");
            if (assignees != null && assignees.size() > 0) {
                logger.info("创建会签及待办任务->创建会签记录2");
                for (Long userid : assignees) {
                    logger.info("创建会签及待办任务->创建会签记录3");
                    BpmDrawingSignTask entity = new BpmDrawingSignTask();
                    entity.setAssignee(userid);
                    logger.info("创建会签及待办任务->开始查找用户信息");
                    JsonObjectResponseBody<UserProfile> response = userFeignAPI.get(userid);
                    logger.info("创建会签及待办任务->结束查找用户信息");
                    String name = response.getData().getName();
                    entity.setAssigneeName(name);
                    entity.setAssignee(response.getData().getId());
                    entity.setCreatedAt();
                    entity.setActInstId(ruTask.getActInstId());
                    entity.setStatus(EntityStatus.ACTIVE);
                    logger.info("创建会签及待办任务->开始保存会签记录 -> 会签记录id：" + entity.getId());
                    BpmRuTask newBmRuTask = new BpmRuTask();
                    bpmRuTask.setSeq(0);
                    if (assignees.indexOf(userid) != 0) {
                        logger.info("创建会签及待办任务->创建待办任务1");
                        BeanUtils.copyProperties(bpmRuTaskOriginal, newBmRuTask, "id");
                        newBmRuTask.setId(CryptoUtils.uniqueDecId());
                        newBmRuTask.setParentTaskId(bpmRuTask.getId());
                    } else {
                        logger.info("创建会签及待办任务->查找待办任务2");
                        newBmRuTask = bpmRuTaskOriginal;
                        newBmRuTask.setParentTaskId(bpmRuTask.getId());
                    }
                    newBmRuTask.setAssignee(userid.toString());
                    newBmRuTask.setSignFlag(false);
                    logger.info("创建会签及待办任务->开始保存待办任务 ->" + newBmRuTask.getId());
                    entity.setTaskId(newBmRuTask.getId());
                    bpmDrawingSignTaskRepository.save(entity);
                    ruTaskRepository.save(newBmRuTask);
                    BpmRuTask findBpmRuTask = ruTaskRepository.findById(newBmRuTask.getId()).orElse(null);
                    logger.info("创建会签及待办任务->结束保存待办任务 -> 流程id ： " + findBpmRuTask.getActInstId());
                }
            }
        }

    }

    /**
     * 更新图纸会签表中处理任务的完成状态。
     *
     * @param contextDTO
     * @param execResult
     */
    private void updateDrawingAssignTask(
        ContextDTO contextDTO,
        ExecResultDTO execResult
    ) {
        Long actInstId = execResult.getRuTask().getActInstId();
        Long projectId = execResult.getProjectId();
        Long userId = contextDTO.getOperator().getId();
        BpmDrawingSignTask entity = bpmDrawingSignTaskRepository.findByActInstIdAndAssignee(actInstId, userId);
        entity.setFinished(true);
        bpmDrawingSignTaskRepository.save(entity);
        BpmRuTask ruTask = execResult.getRuTask();
        OperatorDTO operator = execResult.getContext().getOperator();
        if (!execResult.isExecResult()) {

            todoTaskBaseService.deleteRuTaskByAssign(execResult.getRuTask().getId(), String.valueOf(userId));

        } else {

            List<BpmHiTaskinst> hiTasks = hiTaskinstRepository.getNextHiTask(ruTask.getActInstId());

            if (hiTasks != null && hiTasks.size() > 0) {
                for (BpmHiTaskinst hiTask : hiTasks) {
                    hiTask.setTaskId(ruTask.getId());
                    hiTask.setAssignee(ruTask.getAssignee());
                    hiTask.setCategory(ruTask.getCategory());
                    hiTask.setTaskType(ruTask.getTaskType());
                    hiTask.setDescription(ruTask.getDescription());
                    hiTask.setEndTime(new Date());
                    hiTask.setName(ruTask.getName());
                    hiTask.setOwner(ruTask.getOwner());
                    hiTask.setParentTaskId(ruTask.getParentTaskId());
                    hiTask.setProcDefId(execResult.getProcessId().toString() + ":" + String.valueOf(execResult.getActInst().getBpmnVersion()));
                    hiTask.setActInstId(ruTask.getActInstId());
                    hiTask.setStartTime(ruTask.getCreateTime());
                    hiTask.setTaskDefKey(ruTask.getTaskDefKey());
                    hiTask.setTenantId(ruTask.getTenantId());
                    hiTask.setOperator(operator.getId().toString());
                    if (todoTaskBaseService.saveBpmHiTaskinst(hiTask) == null) {
                        throw new BusinessError("can't not save Bpm History");
                    }
                }

            }

        }

    }

    /**
     * 判断图纸会签是否完成。
     *
     * @param contextDTO
     * @param execResult
     * @return
     */
    private Boolean isDrawingAssignTaskDone(
        ContextDTO contextDTO,
        ExecResultDTO execResult
    ) {
        Long actInstId = execResult.getRuTask().getActInstId();
        Long userId = contextDTO.getOperator().getId();
        List<BpmDrawingSignTask> drawingAssignTaskNotFinishedList = bpmDrawingSignTaskRepository.findByActInstIdAndFinished(actInstId, false);
        if (drawingAssignTaskNotFinishedList.size() > 1) {
            return false;
        }
        if (drawingAssignTaskNotFinishedList.size() == 1 && drawingAssignTaskNotFinishedList.get(0).getAssignee().equals(userId)) {
            return true;
        } else {
            throw new BusinessError("流程有错误");
        }

    }

    /**
     * 将主图纸下最新版本下的有效子图纸改为删除状态。
     *
     * @param orgId
     * @param projectId
     * @param drawingId
     * @param actInstId
     */
    private void updateRedMarkSubDwgReviewStatusWhereApproved(
        Long orgId,
        Long projectId,
        Long drawingId,
        Long actInstId
    ) {
        List<SubDrawing> redMarkSubDrawingList = subDrawingRepository.findByOrgIdAndProjectIdAndDrawingIdAndActInstId(orgId, projectId, drawingId, actInstId);
        List<SubDrawing> subDrawingSaveList = new ArrayList<>();
        if (redMarkSubDrawingList != null && redMarkSubDrawingList.size() > 0) {
            for (SubDrawing redMarkSubDrawing : redMarkSubDrawingList) {
                SubDrawing subDrawingSave = subDrawingRepository.findByOrgIdAndProjectIdAndDrawingIdAndSubDrawingNoAndPageNoAndStatus(
                    orgId,
                    projectId,
                    drawingId,
                    redMarkSubDrawing.getSubDrawingNo(),
                    redMarkSubDrawing.getPageNo(),
                    EntityStatus.ACTIVE
                );
                if (subDrawingSave != null) {
                    subDrawingSave.setStatus(EntityStatus.DELETED);
                    subDrawingSaveList.add(subDrawingSave);
                }
            }
            subDrawingRepository.saveAll(subDrawingSaveList);
        }
    }

    /**
     * 新起线程打包图纸文件。
     */
    private void packSubFiles(
        Long orgId,
        Project project,
        Long actInstId,
        OperatorDTO operatorDTO,
        Drawing dwg,
        ExecResultDTO execResult,
        DrawingDetail drawingDetail,
        ContextDTO contextDTO
    ) {
        List<ExecResultDTO> nextTasks = execResult.getNextTasks();
        List<BpmRuTask> bpmRuTaskList = new ArrayList<>();
        List<BpmRuTask> ruTasks = new ArrayList<>();
        nextTasks.forEach(nextTask -> {
            ruTasks.add(nextTask.getRuTask());
        });

        batchTaskService.runDrawingPackage(
            contextDTO,
            project,
            DRAWING_PACKAGE,
            execResult.getActInst(),
            execResult.getRuTask(),
            ruTasks,
            dwg,
            drawingDetail,
            null,
            null,
            null,
            batchTask -> {
                logger.info("打包有效图纸开始->redmark ");
                DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packSubFiles(
                    orgId,
                    project,
                    actInstId,
                    operatorDTO,
                    true,
                    dwg,
                    execResult.getProcessId(),
                    drawingDetail);

                if (drawingPackageReturnDTO.getBpmDoc() != null) {
                    docsMaterialsRepository.save(drawingPackageReturnDTO.getBpmDoc());
                }

                if (drawingPackageReturnDTO.getDrawing() != null) {
                    drawingRepository.save(drawingPackageReturnDTO.getDrawing());
                }

                if (drawingPackageReturnDTO.getDrawingDetail() != null) {
                    drawingDetailRepository.save(drawingPackageReturnDTO.getDrawingDetail());
                }


                if (drawingPackageReturnDTO.getSubDrawingList() != null && drawingPackageReturnDTO.getSubDrawingList().size() > 0) {
                    subDrawingRepository.saveAll(drawingPackageReturnDTO.getSubDrawingList());
                }


                if (drawingPackageReturnDTO.getDrawingHisList() != null && drawingPackageReturnDTO.getDrawingHisList().size() > 0) {
                    drawingHistoryRepository.saveAll(drawingPackageReturnDTO.getDrawingHisList());
                }
                logger.info("打包有效图纸结束->redmark ");
                return new BatchResultDTO();

            }
        );
    }

    /**
     * 新起线程打包图纸文件。
     */
    private void packEffectiveSubFiles(
        Long orgId,
        Project project,
        Long actInstId,
        OperatorDTO operatorDTO,
        Drawing dwg,
        ExecResultDTO execResult,
        DrawingDetail drawingDetail,
        ContextDTO contextDTO
    ) {
        logger.info("有效图纸打包开始->redmark ");
        List<ExecResultDTO> nextTasks = execResult.getNextTasks();
        List<BpmRuTask> bpmRuTaskList = new ArrayList<>();
        List<BpmRuTask> ruTasks = new ArrayList<>();
        nextTasks.forEach(nextTask -> {
            ruTasks.add(nextTask.getRuTask());
        });
        batchTaskService.runDrawingPackage(
            contextDTO,
            project,
            EFFECTIVE_DRAWING_PACKAGE,
            execResult.getActInst(),
            execResult.getRuTask(),
            ruTasks,
            dwg,
            drawingDetail,
            null,
            null,
            null,
            batchTask -> {
                DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packEffectiveSubFiles(
                    orgId,
                    project,
                    operatorDTO,
                    dwg);
                if (drawingPackageReturnDTO.getDrawing() != null) {
                    drawingRepository.save(drawingPackageReturnDTO.getDrawing());
                }
                return new BatchResultDTO();
            }
        );
        logger.info("有效图纸打包结束->redmark ");
    }

}
