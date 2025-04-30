package com.ose.tasks.domain.model.service.delegate;

import com.ose.auth.api.RoleFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.Role;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseService;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

/**
 * 用户服务。
 */
@Component
public class NextAssigneeDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final TodoTaskBaseService todoTaskBaseService;

    private final UserFeignAPI userFeignAPI;

    private final RoleFeignAPI roleFeignAPI;

    private final TaskRuleCheckService taskRuleCheckService;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(NextAssigneeDelegate.class);


    /**
     * 构造方法。
     */
    @Autowired
    public NextAssigneeDelegate(
        TodoTaskBaseService todoTaskBaseService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") RoleFeignAPI roleFeignAPI,
        TaskRuleCheckService taskRuleCheckService,
        BpmRuTaskRepository ruTaskRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository,
        BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository,
        BpmRuTaskRepository ruTaskRepository1, BpmHiTaskinstRepository hiTaskinstRepository) {

        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.todoTaskBaseService = todoTaskBaseService;
        this.userFeignAPI = userFeignAPI;
        this.roleFeignAPI = roleFeignAPI;
        this.taskRuleCheckService = taskRuleCheckService;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.bpmDrawingSignTaskRepository = bpmDrawingSignTaskRepository;
        this.ruTaskRepository = ruTaskRepository1;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {


        Long actInstId = execResult.getActInst().getId();
        Long operatorId = contextDTO.getOperator().getId();
        Long projectId = execResult.getProjectId();

        for (int j = 0; j < execResult.getNextTasks().size(); j++) {
            ExecResultDTO nextExecResult = execResult.getNextTasks().get(j);
            if (nextExecResult == null || nextExecResult.getRuTask() == null || execResult.getNextTasks().get(j).getTodoTaskDTO() == null) {
                continue;
            }
            BpmRuTask nextTask = execResult.getNextTasks().get(j).getRuTask();
            String taskDefKey = nextTask.getTaskDefKey();
            String taskName = nextTask.getName();
            List<Long> nextAssignees = new ArrayList<>();
            if (taskRuleCheckService.isCounterSignTaskNode(taskDefKey, nextTask.getTaskType())) {
                nextAssignees.addAll(execResult.getNextTasks().get(j).getTodoTaskDTO().getAssignees());
            } else {
                nextAssignees.add(execResult.getNextTasks().get(j).getTodoTaskDTO().getNextAssignee());
            }
//            Long nextAssignee = execResult.getNextTasks().get(j).getTodoTaskDTO().getNextAssignee();
            for (int i = 0; i < nextAssignees.size(); i++) {
                Long nextAssignee = nextAssignees.get(i);
                if (nextAssignee != null && nextAssignee != 0L) {
                    String name = "";
                    String assigneeName="";
                    try {
                        assigneeName = getRedisKey(String.format(RedisKey.USER.getDisplayName(), nextAssignee), 60 * 60 * 8);
                        if (StringUtils.isEmpty(assigneeName)) {
                            assigneeName = userFeignAPI.get(nextAssignee).getData().getName();
                            setRedisKey(String.format(RedisKey.USER.getDisplayName(), nextAssignee), assigneeName);
                        }

                        name += ", " + assigneeName;
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

                    if (!taskRuleCheckService.isCounterSignTaskNode(taskDefKey, nextTask.getTaskType())) {

                        if (category != null) {
                        todoTaskBaseService.modifyTaskAssigneeByCategory(execResult.getRuTask().getActInstId(),
                            category,
                            nextAssignee,
                            name,
                            execResult.getTodoTaskDTO().getTeamId());
                        }

                        todoTaskBaseService.assignee(actInstId, taskDefKey, taskName,
                            nextAssignee, execResult.getActInst());
                        //只有当能执行到会签节点才创建
                    } else if(!execResult.isNotFinished()){
                        if(i>0) {
                            BpmRuTask bpmRuTask = new BpmRuTask();
                            bpmRuTask.setSeq(0);
                            bpmRuTask.setAssignee(nextAssignee.toString());
                            bpmRuTask.setCategory(nextTask.getCategory());
                            bpmRuTask.setTaskType(nextTask.getTaskType());//taskType
                            bpmRuTask.setCreateTime(new Timestamp(new Date().getTime()));//new Timestamp(new Date().getTime()));
                            bpmRuTask.setDelegation(null);
                            bpmRuTask.setDescription(nextTask.getDescription());
                            bpmRuTask.setName(nextTask.getName());
                            bpmRuTask.setOwner(operatorId.toString());
                            bpmRuTask.setActInstId(nextTask.getActInstId());
                            bpmRuTask.setSuspensionState(1);
                            bpmRuTask.setTaskDefKey(nextTask.getTaskDefKey());
                            bpmRuTask.setTenantId(projectId.toString());
                            bpmRuTask.setParentTaskId(null);
                            bpmRuTask.setSeq(i);
                            ruTaskRepository.save(bpmRuTask);

                            BpmHiTaskinst hiTask = new BpmHiTaskinst();

                            hiTask.setTaskId(bpmRuTask.getId());//(hiActinst.getTaskId());
                            hiTask.setSeq(bpmRuTask.getSeq());
                            hiTask.setAssignee(bpmRuTask.getAssignee());
                            hiTask.setCategory(bpmRuTask.getCategory());
                            hiTask.setTaskType(bpmRuTask.getTaskType()); //taskType
                            hiTask.setDescription(bpmRuTask.getDescription());
                            hiTask.setEndTime(null);
                            hiTask.setName(bpmRuTask.getName());
                            hiTask.setOwner(bpmRuTask.getOwner());
                            hiTask.setParentTaskId(bpmRuTask.getParentTaskId());
                            hiTask.setProcDefId(execResult.getActInst().getProcessId().toString() + ":" + execResult.getActInst().getBpmnVersion());
                            hiTask.setActInstId(bpmRuTask.getActInstId());
                            hiTask.setStartTime(bpmRuTask.getCreateTime());
                            hiTask.setTaskDefKey(bpmRuTask.getTaskDefKey());
                            hiTask.setTenantId(bpmRuTask.getTenantId());
                            hiTask.setOperator(operatorId.toString());
                            hiTask.setParentTaskId(bpmRuTask.getParentTaskId());
                            hiTask.setSeq(bpmRuTask.getSeq());
                            hiTaskinstRepository.save(hiTask);
                        } else {
                            BpmRuTask bpmRuTask = ruTaskRepository.findByActInstIdAndTaskDefKeyAndSeq(nextTask.getActInstId(), nextTask.getTaskDefKey(), 0);
                            if(bpmRuTask != null &&
                                (StringUtils.isEmpty(bpmRuTask.getAssignee()) ||
                                    !Objects.equals(bpmRuTask.getAssignee(), nextAssignee.toString()))) {
                                bpmRuTask.setAssignee(nextAssignee.toString());
                                ruTaskRepository.save(bpmRuTask);
                            }
                        }

                        BpmActTaskAssignee bata = bpmActTaskAssigneeRepository.findByActInstIdAndTaskDefKeyAndAssignee(actInstId, taskDefKey, nextAssignee);
                        if (bata == null) {
                            bata = new BpmActTaskAssignee();
                            bata.setActInstId(actInstId);
                            bata.setTaskDefKey(taskDefKey);
                            bata.setTaskName(taskName);
                            bata.setAssignee(nextAssignee);
                            bata.setAssigneeName(assigneeName);
                            bata.setOrderNo(0);
                            bata.setStatus(EntityStatus.ACTIVE);
                            bata.setTaskCategory(category);
                            bata.setSeq(i);
                        } else {
                            bata.setSeq(i);

                        }
                        bpmActTaskAssigneeRepository.save(bata);

                    }

                    BpmActivityInstanceState actInstState = execResult.getActInstState();
                    logger.info("1.下个节点执行人：" + name);
                    actInstState.setCurrentExecutor(name);
                    bpmActivityInstanceStateRepository.save(actInstState);
                    logger.info("2.执行人是否保存成功：" + actInstState.getCurrentExecutor());

                    Map<String, Object> variableMap = execResult.getVariables() == null? new HashMap<>():execResult.getVariables();
                    variableMap.put("nextAssignee", nextAssignee);
                    execResult.setVariables(variableMap);

                }
            }
        }

        return execResult;
    }


}
