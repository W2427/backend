package com.ose.tasks.domain.model.service.delegate;

import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.entity.UserBasic;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户服务。
 */
@Component
public class MaterialCreateAssigneeDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final UserFeignAPI userFeignAPI;

    private final BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository;

    private final BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public MaterialCreateAssigneeDelegate(
        UserFeignAPI userFeignAPI,
        BpmRuTaskRepository ruTaskRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository,
        BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        TodoTaskBaseInterface todoTaskBaseService) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.userFeignAPI = userFeignAPI;
        this.bpmActivityTaskNodePrivilegeRepository = bpmActivityTaskNodePrivilegeRepository;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
    }

    /**
     * 预创建材料工作流程，根据工序上分配的人员进行任务的分配。
     *
     * @param createResult 环境上下文
     * @return BpmActivityInstance 工作流实例
     */
    @Override
    public CreateResultDTO postCreateActInst(CreateResultDTO createResult) {


        Long orgId = createResult.getOrgId();


        Long projectId = createResult.getProjectId();


        Long processId = createResult.getProcess().getId();


        BpmActivityInstanceBase actInst = createResult.getActInst();

        BpmActivityInstanceState actInstState = createResult.getActInstState();


        Set<Long> userIds = new HashSet<>();


        List<BpmActTaskAssignee> bpmActTaskAssignees = new ArrayList<BpmActTaskAssignee>();


        Map<String, BpmActTaskAssignee> bpmActTaskAssigneeMap = new HashMap<>();


        String currentExecutor = "";


        int orderNo = 1;




        Long actInstId = createResult.getActInst().getId();
        if(actInstId == null) {
            createResult.setCreateResult(false);
            return createResult;
        }
        List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInstId);


        List<BpmActivityTaskNodePrivilege> nodePrivileges = bpmActivityTaskNodePrivilegeRepository
            .findByOrgIdAndProjectIdAndProcessIdAndStatus(orgId, projectId, processId, EntityStatus.ACTIVE);


        for (BpmActivityTaskNodePrivilege bpmActTaskNodePrivilege : nodePrivileges) {
            BpmActTaskAssignee assignee = new BpmActTaskAssignee();
            assignee.setSeq(0);
            assignee.setTaskDefKey(bpmActTaskNodePrivilege.getTaskDefKey());
            assignee.setTaskCategory(bpmActTaskNodePrivilege.getCategory());
            assignee.setTaskName(bpmActTaskNodePrivilege.getTaskName());
            assignee.setStatus(EntityStatus.ACTIVE);
            assignee.setOrderNo(orderNo++);
            assignee.setActInstId(actInst.getId());
            assignee.setTeamId(bpmActTaskNodePrivilege.getTeamId());
            assignee.setAssignee(bpmActTaskNodePrivilege.getAssignee());
            userIds.add(bpmActTaskNodePrivilege.getAssignee());
            bpmActTaskAssignees.add(assignee);
        }


        if (userIds.size() > 0) {

            BatchGetDTO batchGetDTO = new BatchGetDTO();
            batchGetDTO.setEntityIDs(userIds);
            Map<Long, String> userNameMap = new HashMap<>();


            JsonListResponseBody<UserBasic> users = userFeignAPI.batchGet(batchGetDTO);


            if (users != null && users.getData() != null) {
                for (UserBasic userBasic : users.getData()) {
                    userNameMap.put(userBasic.getId(), userBasic.getName());
                }
            }

            for (BpmActTaskAssignee bpmActTaskAssignee : bpmActTaskAssignees) {
                bpmActTaskAssignee.setAssigneeName(userNameMap.get(bpmActTaskAssignee.getAssignee()));
            }
        }


        for (BpmActTaskAssignee bpmActTaskAssignee : bpmActTaskAssignees) {
            bpmActTaskAssigneeRepository.save(bpmActTaskAssignee);
        }


        for (BpmActTaskAssignee bpmActTaskAssignee : bpmActTaskAssignees) {
            bpmActTaskAssigneeMap.put(bpmActTaskAssignee.getTaskDefKey(), bpmActTaskAssignee);
        }


        for (BpmRuTask ruTask : ruTasks) {
            currentExecutor = todoTaskBaseService
                .setAssigneeForBpmRuTask(bpmActTaskAssigneeMap, ruTask, currentExecutor, actInst.getId());
        }


        if (!currentExecutor.equals("")) {
            actInstState.setCurrentExecutor(currentExecutor);
            bpmActivityInstanceStateRepository.save(actInstState);
        }

        createResult.setActInst(actInst);
        createResult.setActInstState(actInstState);

        return createResult;
    }


}
