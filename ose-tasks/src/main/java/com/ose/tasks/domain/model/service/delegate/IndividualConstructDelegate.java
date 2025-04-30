package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.AsyncPlanInterface;
import com.ose.tasks.domain.model.service.bpm.TodoIndividualTaskInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseService;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.bpm.ActivityInstanceDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.util.CryptoUtils;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.AddressException;
import java.util.Map;

/**
 * 建造 工序节点生成LOG 的代理服务。
 */
@Component
public class IndividualConstructDelegate extends ConstructionDelegate implements BaseBpmTaskInterfaceDelegate {


    private final ProjectNodeRepository projectNodeRepository;

    private final TodoTaskBaseService todoTaskBaseService;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;
    private final WBSEntryStateRepository wbsEntryStateRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public IndividualConstructDelegate(
        ProjectNodeRepository projectNodeRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        TaskRuleCheckService taskRuleCheckService,
        TodoTaskBaseService todoTaskBaseService,
        AsyncPlanInterface asyncPlanService,
        TodoIndividualTaskInterface todoIndividualTaskService,
        ApplicationEventPublisher applicationEventPublisher,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmRuTaskRepository ruTaskRepository,
        WBSEntryStateRepository wbsEntryStateRepository) {
        super(
            todoTaskBaseService,
            taskRuleCheckService,
            bpmActInstRepository,
            asyncPlanService,
            todoIndividualTaskService,
            applicationEventPublisher, stringRedisTemplate,
            bpmActivityInstanceStateRepository,
            ruTaskRepository);

        this.projectNodeRepository = projectNodeRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
    }


    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        ProjectNode projectNode = projectNodeRepository
            .findByProjectIdAndEntityIdAndDeleted(
                execResult.getProjectId(),
                execResult.getActInst().getEntityId(),
                true)
            .orElse(null);
        if (projectNode != null) {
            execResult.getActInstState().setMemo(StringUtils.trim(execResult.getActInstState().getMemo()) + BpmCode.ENTITY_MARK_DELETED);
            execResult.getActInstState().setSuspensionState(SuspensionState.SUSPEND);
            bpmActivityInstanceStateRepository.save(execResult.getActInstState());
            execResult.setExecResult(false);
            return execResult;
        }

        return execResult;
    }

    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) throws AddressException {

        execResult = super.postExecute(contextDTO, data, execResult);

        return execResult;
    }

    @Override
    public ExecResultDTO completeExecute(ExecResultDTO execResult) {

        BpmActivityInstanceState actInstState = execResult.getActInstState();
        BpmActivityInstanceBase actInst = execResult.getActInst();
        Long projectId = execResult.getProjectId();
        OperatorDTO operator = execResult.getContext().getOperator();
        boolean approved = true;
        Long version = CryptoUtils.uniqueDecId();
        execResult.setExeVersion(version);
        Boolean isHalt = null;


        double hours = todoTaskBaseService.getProcessCostHour(actInstState.getBaiId());
        todoTaskBaseService.createBpmPlanExecutionHistory(
            projectId, operator, actInstState.getEntityId(), actInst.getProcessId(), actInst.getId(), approved,
            hours, version, isHalt,false);

        super.completeExecute(execResult);

        return execResult;
    }

    @Override
    public CreateResultDTO preCreateActInst(CreateResultDTO createResult) {


        createResult = super.preCreateActInst(createResult);
        if (!createResult.isCreateResult())
            return createResult;

        createResult = getConstructActInstInfo(createResult);

        return createResult;
    }

    @Override
    public CreateResultDTO createActInst(CreateResultDTO createResult) {
        if (!createResult.isCreateResult()) return createResult;
        createResult = super.createActInst(createResult);


        return createResult;
    }

    @Override
    public CreateResultDTO postCreateActInst(CreateResultDTO createResult) {
        if (!createResult.isCreateResult()) return createResult;
        if (!createResult.isCreateResult()) return createResult;



        Long projectId = createResult.getProjectId();
        OperatorDTO operator = createResult.getContext().getOperator();
        BpmActivityInstanceBase actInst = createResult.getActInst();




        WBSEntryState wbsEntryTempState = wbsEntryStateRepository.findByProjectIdAndEntityIdAndStageAndProcessAndDeletedIsFalse(
            projectId, actInst.getEntityId(), actInst.getProcessStage(), actInst.getProcess()).orElse(null);
        if (wbsEntryTempState != null) {
            if (wbsEntryTempState.getStartedAt() == null) {
                wbsEntryTempState.setStartedAt(actInst.getStartDate());
                wbsEntryTempState.setStartedBy(operator.getId());
                wbsEntryTempState.setRunningStatus(WBSEntryRunningStatus.RUNNING);
                wbsEntryStateRepository.save(wbsEntryTempState);
            }
        }
        createResult = super.postCreateActInst(createResult);

        return createResult;
    }


    /**
     * 取得建造流程的信息
     *
     * @param createResult 创建流程结果
     * @return createResult
     */

    private CreateResultDTO getConstructActInstInfo(CreateResultDTO createResult) {

        ActivityInstanceDTO actInstDTO = createResult.getActInstDTO();
        createResult.setActInstDTO(actInstDTO);
        createResult.setCreateResult(true);
        return createResult;
    }
}
