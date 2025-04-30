package com.ose.tasks.domain.model.service.delegate;

import com.ose.auth.api.RoleFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.Role;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.ActivityTaskAPI;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingEntryDelegate;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 检查 节点 代理。
 */
@Component
public class MaterialReleaseReceiveAssigneeDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final ActivityTaskAPI activityTaskAPI;
    private final ActivityTaskInterface activityTaskService;

    private final UserFeignAPI userFeignAPI;

    private final RoleFeignAPI roleFeignAPI;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public MaterialReleaseReceiveAssigneeDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                                  BpmRuTaskRepository ruTaskRepository,
                                                  StringRedisTemplate stringRedisTemplate,
                                                  BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                                  ActivityTaskAPI activityTaskAPI, ActivityTaskInterface activityTaskService,
                                                  UserFeignAPI userFeignAPI,
                                                  RoleFeignAPI roleFeignAPI,
                                                  TodoTaskBaseInterface todoTaskBaseService) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.activityTaskAPI = activityTaskAPI;
        this.activityTaskService = activityTaskService;
        this.userFeignAPI = userFeignAPI;
        this.roleFeignAPI = roleFeignAPI;
        this.todoTaskBaseService = todoTaskBaseService;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
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
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        Long actInstId = execResult.getActInst().getId();
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        List<BpmActTaskAssignee> taskAssignees = activityTaskService
            .findActTaskAssigneesByActInstId(actInstId);
        if (taskAssignees.isEmpty()) {
            execResult.setErrorDesc("任务信息不存在");
            execResult.setExecResult(false);
        }

        BpmRuTask bpmRuTask = execResult.getRuTask();
        if(null==bpmRuTask){
            execResult.setErrorDesc("待办任务不存在");
            execResult.setExecResult(false);
        }

        BpmActivityTaskNodePrivilege nodePrivilege = todoTaskBaseService.getBpmActivityTaskNodePrivilege(orgId,
            projectId, execResult.getActInst().getProcessId(), bpmRuTask.getTaskDefKey());
        String category = null;
        if (nodePrivilege != null) {
            category = nodePrivilege.getSubCategory();
        }

        List<String> subCategories = new ArrayList<>();
        if (category != null && !"".equals(category)) {
            subCategories = Arrays.asList(category.split(","));
        }

        for (BpmActTaskAssignee taskAssignee : taskAssignees) {
            if (null!=taskAssignee.getTaskCategory() && taskAssignee.getTaskCategory().equals(subCategories.get(0))
                && execResult.getTodoTaskDTO().getNextAssignee() != null
            ) {
//                activityTaskAPI.activityTaskAssignee(
//                    orgId,
//                    projectId,
//                    actInstId,
//                    taskAssignee.getId(),
//                    execResult.getTodoTaskDTO().getNextAssignee()
//                );
                String name = "";
                Long userid = execResult.getTodoTaskDTO().getNextAssignee();
                Long taskAssigneeId = taskAssignee.getId();
                Long id = actInstId;
                try {
                    JsonObjectResponseBody<UserProfile> userResponse = userFeignAPI.get(userid);
                    name += ", " + userResponse.getData().getName();
                } catch (Exception e) {
                    JsonObjectResponseBody<Role> roleResponse = roleFeignAPI.get(orgId, userid);
                    if (roleResponse.getData() != null) {
                        name += ", " + roleResponse.getData().getName();
                    }
                }

                if (name.length() > 0) {
                    name = name.substring(2);
                }

                /*
                 * JsonObjectResponseBody<UserProfile> userResponse = userFeignAPI.get(userid);
                 *
                 * String userName = userResponse.getData().getName();
                 */

                BpmActTaskAssignee actAssignee = activityTaskService.findActTaskAssigneesById(taskAssigneeId);
                if (actAssignee != null) {

                    BpmActivityInstanceBase actInst = activityTaskService.findActInstById(id);
                    if (actInst != null) {
                        BpmActivityInstanceState bpmActivityInstanceState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
                        if (bpmActivityInstanceState == null) {
                            bpmActivityInstanceState = new BpmActivityInstanceState();
                            bpmActivityInstanceState.setBaiId(actInst.getId());
                            bpmActivityInstanceState.setOrgId(orgId);
                            bpmActivityInstanceState.setProjectId(projectId);
                        }


                        OperatorDTO operatorDTO = contextDTO.getOperator();
                        activityTaskService.modifyTaskAssignee(taskAssigneeId, userid, name);//, operatorDTO);
                        activityTaskService.assignee(actInst.getId(), actAssignee.getTaskDefKey(),
                            actAssignee.getTaskName(), userid, operatorDTO.getId().toString());
                        List<BpmRuTask> ruTaskList = activityTaskService.findBpmRuTaskByActInstId(actInst.getId());
                        String currentExecutor = "";

                        currentExecutor = todoTaskBaseService.setTaskAssignee(ruTaskList);

                        bpmActivityInstanceState.setCurrentExecutor(currentExecutor);
                        bpmActivityInstanceStateRepository.save(bpmActivityInstanceState);

                    }
                }
            }
        }
        return execResult;
    }
}
