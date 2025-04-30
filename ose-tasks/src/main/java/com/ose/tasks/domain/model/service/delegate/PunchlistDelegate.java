package com.ose.tasks.domain.model.service.delegate;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseService;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.vo.BpmTaskDefKey;
import com.ose.dto.ContextDTO;
import com.ose.issues.api.IssueFeignAPI;
import com.ose.issues.dto.IssueUpdateDTO;
import com.ose.issues.entity.Issue;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 遗留问题 代理服务。
 */
@Component
public class PunchlistDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final IssueFeignAPI issueFeignAPI;

    private final TodoTaskBaseService todoTaskBaseService;

    /**
     * 构造方法。
     */
    @Autowired
    public PunchlistDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                             @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                 IssueFeignAPI issueFeignAPI, TodoTaskBaseService todoTaskBaseService,
                             StringRedisTemplate stringRedisTemplate,
                             BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                             BpmRuTaskRepository ruTaskRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.issueFeignAPI = issueFeignAPI;
        this.todoTaskBaseService = todoTaskBaseService;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");

        Long[] taskIds = (Long[]) data.get("taskIds");

        Map<String, Object> command = (Map<String, Object>) data.get("command");

        String taskDefKey = execResult.getRuTask().getTaskDefKey();

        if (command.containsKey(BpmCode.EXCLUSIVE_GATEWAY_RESULT)) {
            for (int i = 0; i < taskIds.length; i++) {
                Long taskId = taskIds[i];
                BpmRuTask ruTask = ruTaskRepository.findById(taskId).orElse(null);
                if (ruTask != null) {
                    Optional<BpmActivityInstanceBase> opActInst = bpmActInstRepository.findByProjectIdAndId(projectId, ruTask.getActInstId());
                    if (opActInst.isPresent()) {
                        BpmActivityInstanceBase actInst = opActInst.get();
                        Issue issue = issueFeignAPI.get(orgId, projectId, actInst.getEntityId()).getData();
                        if (command.get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).equals(BpmCode.EXCLUSIVE_GATEWAY_RESULT_COMPLETE)) {
                            IssueUpdateDTO issueUpdateDTO = new IssueUpdateDTO();
                            BeanUtils.copyProperties(issue, issueUpdateDTO, "no", "attachment", "status");
                            issueUpdateDTO.setStatus(EntityStatus.FINISHED);
                            issueFeignAPI.update(orgId, projectId, issue.getId(), issueUpdateDTO);
                        }
                    }
                }
            }
        }


        if (BpmTaskDefKey.USERTASK_PUNCHLIST_CHARGER_ASIGN.getType().equalsIgnoreCase(taskDefKey)
            ||
            BpmTaskDefKey.USERTASK_PUNCHLIST_DELEGATE_OTHER.getType().equalsIgnoreCase(taskDefKey)) {
            Long assignee = (Long) execResult.getVariables().get("assignee");
            if (assignee != null) {
                todoTaskBaseService.modifyPunchlistMembers(execResult.getActInst(), assignee);
            }
        }

        return execResult;
    }

}
