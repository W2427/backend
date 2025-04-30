package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.NotFoundError;
import com.ose.tasks.Event.ConstructionLogEvent;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.service.AsyncPlanInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseService;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.qc.TestResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.vo.bpm.ExInspStatus;
import com.ose.tasks.vo.wbs.EntityTestResult;
import com.ose.util.CryptoUtils;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 建造 工序节点生成LOG 的代理服务。
 */
@Component
public class ConstructionBatchDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {


    private final TodoTaskBaseService todoTaskBaseService;

    private final AsyncPlanInterface asyncPlanService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final BpmProcessRepository processRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final static Logger logger = LoggerFactory.getLogger(ConstructionBatchDelegate.class);

    /**
     * 构造方法。
     */
    @Autowired
    public ConstructionBatchDelegate(
        TodoTaskBaseService todoTaskBaseService,
        BpmActivityInstanceRepository bpmActInstRepository,
        AsyncPlanInterface asyncPlanService,
        ApplicationEventPublisher applicationEventPublisher,
        BpmRuTaskRepository ruTaskRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmProcessRepository processRepository) {

        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.todoTaskBaseService = todoTaskBaseService;
        this.asyncPlanService = asyncPlanService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.processRepository = processRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
    }

    @Override
    @SuppressWarnings("unckecked")

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchCompleteExecute(ContextDTO contextDTO,
                                                                                      Map<String, Object> data,
                                                                                      P todoBatchTaskCriteriaDTO,
                                                                                      TodoBatchTaskDTO todoBatchTaskDTO) {
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        List<BpmActivityInstanceBase> actInsts = todoBatchTaskDTO.getBatchActInsts();
        Map<Long, BpmActivityInstanceState> actInstStateMap = todoBatchTaskDTO.getBatchActInstStateMap();
        if (actInsts == null || actInsts.size() == 0) {
            return todoBatchTaskDTO;
        }
        Long version = CryptoUtils.uniqueDecId();
        todoBatchTaskCriteriaDTO.setExeVersion(version);
        logger.info("任务数量->" + actInsts.size() + "当前时间->" + new Date());
        for (BpmActivityInstanceBase actInst : actInsts) {
            logger.info("修复接口分配任务 -> " + "当前任务名 ->" + actInst.getEntityNo() + "任务数量->" + actInsts.size() + "当前时间->" + new Date());
            OperatorDTO operator = contextDTO.getOperator();
            BpmActivityInstanceState actInstState = actInstStateMap.get(actInst.getId());
            if (actInstState == null) throw new NotFoundError(actInstState.getBaiId().toString());
            logger.info("开始给任务赋值完成时间");
            if (actInstState.getEndDate() == null) {
                actInstState.setEndDate(new Timestamp(new Date().getTime()));
                bpmActivityInstanceStateRepository.save(actInstState);
                logger.info("任务赋值完成时间成功");
            }
            boolean approved = true;
            if (actInstState.isExecuteNgFlag()) {
                approved = false;
            }

            double hours = todoTaskBaseService.getProcessCostHour(actInst.getId());
            todoTaskBaseService.createBpmPlanExecutionHistory(
                projectId, operator, actInst.getEntityId(), actInst.getProcessId(), actInst.getId(), approved,
                hours, version);

            BpmProcess bpmProcess = processRepository.findById(actInst.getProcessId()).orElse(null);
            if (bpmProcess == null) {
                System.out.println("bpmProcess is NULL");
                continue;
            }


            String entityType = todoBatchTaskDTO.getEntityType();

            if (StringUtils.isEmpty(bpmProcess.getConstructionLogClass())) {
                continue;
            }

            TestResultDTO testResultDTO = new TestResultDTO();
            testResultDTO.setTestResult(EntityTestResult.FINISHED);
            if (actInstState.isExecuteNgFlag()) {
                testResultDTO.setExInspStatus(ExInspStatus.FAILED);
            } else {
                testResultDTO.setExInspStatus(ExInspStatus.OK);
            }

            testResultDTO.setActInstId(actInst.getId());
            testResultDTO.setOrgId(orgId);
            testResultDTO.setProjectId(projectId);
            testResultDTO.setActInstId(actInst.getId());
            testResultDTO.setWorkTeamId(actInstState.getTeamId());
            testResultDTO.setWorkTeam(actInstState.getTeamName());
            testResultDTO.setWorkSiteId(actInstState.getWorkSiteId());
            testResultDTO.setWorkSite(actInstState.getWorkSiteName());
            Date finishedAt = actInstState.getEndDate();
            testResultDTO.setFinishedAt(finishedAt == null ? new Date() : finishedAt);
            testResultDTO.setTaskPackage(actInstState.getTaskPackageName());
            testResultDTO.setTaskPackageId(actInstState.getTaskPackageId());


            if (!StringUtils.isEmpty(bpmProcess.getConstructionLogClass())) {
                String wbsEntityType;
                try {
                    wbsEntityType = entityType;
                    ConstructionLogEvent constructionLogEvent =
                        new ConstructionLogEvent(bpmProcess.getConstructionLogClass(),
                            contextDTO.getOperator(),
                            wbsEntityType,
                            actInst.getEntityId(),
                            bpmProcess.getNameEn(),
                            bpmProcess.getId(),
                            bpmProcess.getProcessStage().getNameEn(),
                            testResultDTO);
                    applicationEventPublisher.publishEvent(constructionLogEvent);
                } catch (Exception e) {
                    System.out.println("There is no such entity Type");
                }
            }
        }

        asyncPlanService.callAsyncPlanExecuteFinish(contextDTO, version, orgId, projectId, false);
        return todoBatchTaskDTO;
    }
}
