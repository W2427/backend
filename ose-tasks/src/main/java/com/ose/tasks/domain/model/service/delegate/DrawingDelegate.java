package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.service.AsyncPlanInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.util.CryptoUtils;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 图纸 工序 代理。
 */
@Component
public class DrawingDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final DrawingBaseInterface drawingBaseService;

    private final BpmProcessRepository processRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;

    private final static Logger logger = LoggerFactory.getLogger(DrawingDelegate.class);

    private final AsyncPlanInterface asyncPlanService;


    /**
     * 构造方法。
     */
    @Autowired
    public DrawingDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                           BpmRuTaskRepository ruTaskRepository,
                           TodoTaskBaseInterface todoTaskBaseService,
                           DrawingBaseInterface drawingBaseService,
                           BpmProcessRepository processRepository,
                           DrawingDetailRepository drawingDetailRepository,
                           StringRedisTemplate stringRedisTemplate,
                           BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                           BpmActivityInstanceRepository bpmActivityInstanceRepository, AsyncPlanInterface asyncPlanService) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.todoTaskBaseService = todoTaskBaseService;
        this.drawingBaseService = drawingBaseService;
        this.processRepository = processRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.asyncPlanService = asyncPlanService;
    }

    /**
     * 预创建工作流。
     *
     * @param createResult 环境上下文
     * @return BpmActivityInstance 工作流实例
     */
    @Override
    public CreateResultDTO preCreateActInst(CreateResultDTO createResult) {

        Long projectId = createResult.getProjectId();
        OperatorDTO operatorDTO = createResult.getContext().getOperator();
        Long orgId = createResult.getOrgId();


        drawingActivityCreateCheck(createResult.getContext(), orgId, projectId, operatorDTO, createResult);

        if (!createResult.isCreateResult()) {
            return createResult;
        }

        createResult = todoTaskBaseService.getDrawingActInstInfo(createResult);

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
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {
        List<ActReportDTO> documents = new ArrayList<>();

        execResult = drawingBaseService.checkRuTask(execResult);


        ActReportDTO document = todoTaskBaseService.getDrawing(execResult.getActInst(), execResult.getRuTask().getTaskDefKey());
        if (document != null) {
            documents.add(document);
            if (execResult.getVariables() != null) {
                execResult.getVariables().put("jsonDocuments", documents);
            }
        }


        return execResult;
    }

    private void drawingActivityCreateCheck(
        ContextDTO context,
        Long orgId,
        Long projectId,
        OperatorDTO operator,
        CreateResultDTO createResult
    ) {

        Long drawingId = createResult.getActInstDTO().getEntityId();

        BpmProcess bpmProcess = processRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, createResult.getActInstDTO().getProcessId(), EntityStatus.ACTIVE);
        if (bpmProcess == null) {
            createResult.setErrorDesc("工序不存在");
            createResult.setCreateResult(false);

        }


        if (bpmProcess.getNameEn().equals(BpmCode.ENGINEERING)) {

            List<EntityStatus> statusList = new ArrayList<>();

            statusList.add(EntityStatus.PENDING);

            statusList.add(EntityStatus.ACTIVE);

            List<DrawingDetail> drawingDetails = drawingDetailRepository.findByOrgIdAndProjectIdAndDrawingIdAndStatusIn(orgId, projectId, createResult.getActInstDTO().getEntityId(), statusList);
            if (drawingDetails != null && drawingDetails.size() > 0) {
                createResult.setErrorDesc("本图纸已出图或存在正在出图任务");
                createResult.setCreateResult(false);
            }
        }


        if (bpmProcess.getNameEn().equals(BpmCode.DRAWING_PARTIAL_UPDATE) || bpmProcess.getNameEn().equals(BpmCode.DRAWING_INTEGRAL_UPDATE)) {
            List<EntityStatus> activeStatusList = new ArrayList<>();

            activeStatusList.add(EntityStatus.ACTIVE);


            List<DrawingDetail> activeDrawingDetails = drawingDetailRepository.findByOrgIdAndProjectIdAndDrawingIdAndStatusIn(orgId, projectId, createResult.getActInstDTO().getEntityId(), activeStatusList);
            if (activeDrawingDetails == null || activeDrawingDetails.size() != 1) {
                createResult.setErrorDesc("本图纸未进行出图任务");
                createResult.setCreateResult(false);
            }


            List<EntityStatus> suspendStatusList = new ArrayList<>();

            suspendStatusList.add(EntityStatus.ACTIVE);
            suspendStatusList.add(EntityStatus.SUSPEND);
            List<DrawingDetail> suspendDrawingDetails = drawingDetailRepository.findByOrgIdAndProjectIdAndDrawingIdAndRevAndStatusIn(orgId, projectId, createResult.getActInstDTO().getEntityId(), createResult.getActInstDTO().getVersion(), suspendStatusList);
            if (suspendDrawingDetails != null && suspendDrawingDetails.size() > 0) {
                createResult.setErrorDesc("本图纸未进行过当前版本升版图纸任务或与出图版本冲突");
                createResult.setCreateResult(false);
            }


            List<BpmActivityInstanceBase> bpmActivityInstanceList = bpmActivityInstanceRepository.findByProjectIdAndEntityIdAndFinishStateAndSuspensionState(
                projectId,
                drawingId,
                ActInstFinishState.NOT_FINISHED,
                SuspensionState.ACTIVE
            );

            if (bpmActivityInstanceList != null && bpmActivityInstanceList.size() > 0) {
                createResult.setErrorDesc("本图纸正在进行REDMARK，不能进行升版任务");
                createResult.setCreateResult(false);
            }
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public ExecResultDTO completeExecute(ExecResultDTO execResult) {

        Long orgId = execResult.getOrgId();
        Long projectId =execResult.getProjectId();
        BpmActivityInstanceBase actInst = execResult.getActInst();
        Boolean forceStart = execResult.getVariables() != null && (Boolean) execResult.getVariables().get("forceStart");

        BpmProcess bpmProcess = execResult.getBpmProcess();
        if(bpmProcess == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("BPM PROCESS IS NULL");
            return execResult;
        }


        String entityType = execResult.getActInst().getEntityType();

        logger.info("开始更新后续四级计划状态-> 当前时间："+new Date());
        BpmActivityInstanceState actInstState = execResult.getActInstState();

        double hours = todoTaskBaseService.getProcessCostHour(actInstState.getBaiId());
        //TODO appoved = false;
        boolean approved = true;
        Long version = CryptoUtils.uniqueDecId();
        execResult.setExeVersion(version);
        Boolean isHalt = null;
        OperatorDTO operator = execResult.getContext().getOperator();

        todoTaskBaseService.createBpmPlanExecutionHistory(
            projectId, operator, actInstState.getEntityId(), actInst.getProcessId(), actInst.getId(), approved,
            hours, version, isHalt, forceStart);
        asyncPlanService.callAsyncPlanExecuteFinish(execResult.getContext(),
            execResult.getExeVersion(),
            execResult.getOrgId(),
            execResult.getProjectId(),
            false);
        logger.info("开始更新后续四级计划状态-> 结束后时间："+new Date());

        return execResult;
    }
}
