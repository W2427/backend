package com.ose.tasks.domain.model.service.delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.service.bpm.TodoIndividualTaskService;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.vo.BpmTaskDefKey;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.bpm.ActivityInstanceDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.bpm.TodoTaskDTO;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.vo.bpm.BpmCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.drawing.ConstructionChangeRegisterRepository;
import com.ose.tasks.domain.model.repository.drawing.DesignChangeReviewFormRepository;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.ConstructionChangeRegister;
import com.ose.tasks.entity.DesignChangeReviewForm;
import com.ose.vo.EntityStatus;

/**
 * 用户服务。
 */
@Component
public class ConstructionChangeDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final BpmProcessRepository processRepository;

    private final DesignChangeReviewFormRepository designChangeReviewFormRepository;

    private final DrawingRepository drawingRepository;

    private final ConstructionChangeRegisterRepository constructionChangeRegisterRepository;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final TodoIndividualTaskService todoIndividualTaskService;


    /**
     * 构造方法。
     */
    @Autowired
    public ConstructionChangeDelegate(
        DesignChangeReviewFormRepository designChangeReviewFormRepository,
        DrawingRepository drawingRepository,
        BpmProcessRepository processRepository,
        ConstructionChangeRegisterRepository constructionChangeRegisterRepository,
        TodoTaskDispatchInterface todoTaskDispatchService,
        TaskRuleCheckService taskRuleCheckService,
        TodoIndividualTaskService todoIndividualTaskService,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmRuTaskRepository ruTaskRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.designChangeReviewFormRepository = designChangeReviewFormRepository;
        this.drawingRepository = drawingRepository;
        this.processRepository = processRepository;
        this.constructionChangeRegisterRepository = constructionChangeRegisterRepository;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.todoIndividualTaskService = todoIndividualTaskService;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");

        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");

        Map<String, Object> command = (Map<String, Object>) data.get("command");


        BpmRuTask ruTask = execResult.getRuTask();

        BpmActivityInstanceBase actInst = execResult.getActInst();

        if (BpmTaskDefKey.USERTASK_DESIGNER_ACCEPT_TASK.getType().equalsIgnoreCase(ruTask.getTaskDefKey())) {
            return execResult;
        }
        Long registerId = actInst.getEntityId();

        Optional<ConstructionChangeRegister> op = constructionChangeRegisterRepository.findById(registerId);
        if (!op.isPresent()) return execResult;

        ConstructionChangeRegister ccr = op.get();
        if (ruTask.getTaskDefKey().equals(BpmTaskDefKey.USERTASK_DESIGNER_ACCEPT_TASK.getType())) {
            DesignChangeReviewForm form = designChangeReviewFormRepository.findByReviewRegisterId(ccr.getId());
            List<String> dwgNos = form.getJsonActionItemReadOnly();
            List<String> newRevs = form.getJsonItemVersionReadOnly();
            List<Long> dwgActInstIds = new ArrayList<>();
            for (int i = 0; i < dwgNos.size(); i++) {
                String dwgNo = dwgNos.get(i);
                String newRev = newRevs.get(i);
                Optional<Drawing> opDwg = drawingRepository.findByOrgIdAndProjectIdAndDwgNoAndStatus(orgId, projectId, dwgNo, EntityStatus.ACTIVE);
                if (opDwg.isPresent()) {
                    Drawing dwg = opDwg.get();
                    ActivityInstanceDTO taskDTO = new ActivityInstanceDTO();
                    taskDTO.setEntityId(dwg.getId());
                    taskDTO.setEntityNo(dwgNo);
                    taskDTO.setVersion(newRev);
                    List<BpmProcess> process = processRepository.findByOrgIdAndProjectIdAndNameEnAndStatus(orgId, projectId, BpmCode.DRAWING_PARTIAL_UPDATE, EntityStatus.ACTIVE);
                    if (!process.isEmpty()) {
                        taskDTO.setProcessId(process.get(0).getId());
                        try {
                            CreateResultDTO createResult = todoTaskDispatchService.create(contextDTO, orgId, projectId, operatorDTO, taskDTO);
                            BpmActivityInstanceBase dwgActInst = createResult.getActInst();

                            if (dwgActInst != null) {
                                dwgActInstIds.add(dwgActInst.getId());
                            }
                        } catch (ValidationError e) {
                            e.printStackTrace(System.out);
                        }
                    }
                }
            }
            ccr.setJsonDwgActInstIds(dwgActInstIds);
            constructionChangeRegisterRepository.save(ccr);
        } else {
            if (!command.get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).equals("驳回") && !command.get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).equals(BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT)) {
                ccr.setPassFlag(true);
            } else {
                ccr.setPassFlag(false);
            }
        }
        constructionChangeRegisterRepository.save(ccr);

        return execResult;
    }


    @Override
    public void prepareExecute(ContextDTO contextDTO, Map<String, Object> data,
                               BpmActivityInstanceBase actInst, BpmRuTask ruTask, TodoTaskDTO todoDTO) {


        if (taskRuleCheckService.isSupervisorConfirmChangeTaskNode(ruTask.getTaskType())) {
            Long projectId = (Long) data.get("projectId");
            List<BpmActivityInstanceBase> subActInsts = todoIndividualTaskService.getSubActInstFromConstructionChangeRegister(projectId, actInst.getEntityId());
            todoDTO.setSubActInsts(subActInsts);
        }
    }


}
