package com.ose.tasks.domain.model.service.delegate;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.service.bpm.TodoIndividualTaskService;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.vo.BpmTaskDefKey;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DesignChangeReviewFormRepository;
import com.ose.tasks.domain.model.repository.drawing.DesignChangeReviewRegisterRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.bpm.ActivityInstanceDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.bpm.TodoTaskDTO;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户服务。
 */
@Component
public class DesignChangeDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final BpmProcessRepository processRepository;

    private final DesignChangeReviewRegisterRepository designChangeReviewRegisterRepository;

    private final DesignChangeReviewFormRepository designChangeReviewFormRepository;

    private final DrawingRepository drawingRepository;

    private final TaskRuleCheckService taskRuleCheckService;

    private final TodoIndividualTaskService todoIndividualTaskService;


    /**
     * 构造方法。
     */
    @Autowired
    public DesignChangeDelegate(
        DesignChangeReviewRegisterRepository designChangeReviewRegisterRepository,
        DesignChangeReviewFormRepository designChangeReviewFormRepository,
        DrawingRepository drawingRepository,
        BpmProcessRepository processRepository,
        TodoTaskDispatchInterface todoTaskDispatchService,
        TaskRuleCheckService taskRuleCheckService,
        TodoIndividualTaskService todoIndividualTaskService,
        BpmActivityInstanceRepository bpmActInstRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmRuTaskRepository ruTaskRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.designChangeReviewRegisterRepository = designChangeReviewRegisterRepository;
        this.designChangeReviewFormRepository = designChangeReviewFormRepository;
        this.drawingRepository = drawingRepository;
        this.processRepository = processRepository;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.todoIndividualTaskService = todoIndividualTaskService;
    }


    @Override
    public void prepareExecute(ContextDTO contextDTO, Map<String, Object> data,
                               BpmActivityInstanceBase actInst, BpmRuTask ruTask, TodoTaskDTO todoDTO) {


        Long projectId = (Long) data.get("projectId");

        if (taskRuleCheckService.isSupervisorConfirmChangeTaskNode(ruTask.getTaskType())) {
            List<BpmActivityInstanceBase> subActInsts = todoIndividualTaskService.getSubActInstFromModificationReviewRegister(projectId, actInst.getEntityId());
            todoDTO.setSubActInsts(subActInsts);
        }
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");

        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");

        Map<String, Object> command = (Map<String, Object>) data.get("command");

        BpmRuTask ruTask = execResult.getRuTask();

        BpmActivityInstanceBase actInst = execResult.getActInst();

        if (ruTask.getTaskDefKey().equals(BpmTaskDefKey.USERTASK_DESIGNER_ACCEPT_TASK.getType())) {
            return execResult;
        }
        Long registerId = actInst.getEntityId();


        Optional<DesignChangeReviewRegister> op = designChangeReviewRegisterRepository.findById(registerId);
        if (op.isPresent()) {
            DesignChangeReviewRegister mrr = op.get();
            if (command.containsKey(BpmCode.EXCLUSIVE_GATEWAY_RESULT)) {
                if (ruTask.getTaskDefKey().equals(BpmTaskDefKey.USERTASK_DESIGNER_ACCEPT_TASK.getType())) {
                    DesignChangeReviewForm form = designChangeReviewFormRepository.findByReviewRegisterId(mrr.getId());
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
                    mrr.setJsonDwgActInstIds(dwgActInstIds);
                    designChangeReviewRegisterRepository.save(mrr);

                }

            }

        }
        return execResult;
    }

}
