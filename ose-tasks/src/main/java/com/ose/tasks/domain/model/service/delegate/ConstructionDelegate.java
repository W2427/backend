package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.Event.ConstructionLogEvent;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.service.AsyncPlanInterface;
import com.ose.tasks.domain.model.service.bpm.TodoIndividualTaskInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseService;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.bpm.TaskMaterialDTO;
import com.ose.tasks.dto.bpm.TodoTaskDTO;
import com.ose.tasks.dto.qc.TestResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.vo.bpm.ExInspStatus;
import com.ose.tasks.vo.wbs.EntityTestResult;
import com.ose.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 建造 工序节点生成LOG 的代理服务。
 */
@Component
public class ConstructionDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {


    private final TodoTaskBaseService todoTaskBaseService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final AsyncPlanInterface asyncPlanService;

    private final TodoIndividualTaskInterface todoIndividualTaskService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final static Logger logger = LoggerFactory.getLogger(ConstructionDelegate.class);
    /**
     * 构造方法。
     */
    @Autowired
    public ConstructionDelegate(
        TodoTaskBaseService todoTaskBaseService,
        TaskRuleCheckService taskRuleCheckService,
        BpmActivityInstanceRepository bpmActInstRepository,
        AsyncPlanInterface asyncPlanService,
        TodoIndividualTaskInterface todoIndividualTaskService,
        ApplicationEventPublisher applicationEventPublisher,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmRuTaskRepository ruTaskRepository) {

        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.todoTaskBaseService = todoTaskBaseService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.asyncPlanService = asyncPlanService;
        this.todoIndividualTaskService = todoIndividualTaskService;
        this.applicationEventPublisher = applicationEventPublisher;
    }



    @Override
    @SuppressWarnings("unchecked")
    public CreateResultDTO postCreateActInst(CreateResultDTO createResult) {

        if (!createResult.isCreateResult()) return createResult;
        BpmProcess bpmProcess = createResult.getProcess();
        if(bpmProcess == null) {
            createResult.setCreateResult(false);
            createResult.setErrorDesc("BpmProcess is NULL");
            return createResult;
        }

        BpmActivityInstanceBase actInst = createResult.getActInst();
        BpmActivityInstanceState actInstState = createResult.getActInstState();

        String entityType = createResult.getActInst().getEntityType();



        TestResultDTO testResultDTO = new TestResultDTO();
        testResultDTO.setTestResult(EntityTestResult.UNFINISHED);
        testResultDTO.setActInstId(actInst.getId());
        String process = bpmProcess.getNameEn();
        testResultDTO.setActInstId(createResult.getActInst().getId());
        testResultDTO.setOrgId(createResult.getOrgId());
        testResultDTO.setProjectId(createResult.getProjectId());

        testResultDTO.setWorkTeamId(createResult.getActInstState().getTeamId());
        testResultDTO.setWorkTeam(createResult.getActInstState().getTeamName());
        testResultDTO.setWorkSiteId(createResult.getActInstState().getWorkSiteId());
        testResultDTO.setWorkSite(createResult.getActInstState().getWorkSiteName());
        testResultDTO.setTaskPackage(createResult.getActInstState().getTaskPackageName());
        testResultDTO.setTaskPackageId(createResult.getActInstState().getTaskPackageId());



        if (!StringUtils.isEmpty(createResult.getProcess().getConstructionLogClass())) {

            String wbsEntityType;
            try {
                wbsEntityType = entityType;
                ConstructionLogEvent constructionLogEvent =
                    new ConstructionLogEvent(createResult.getProcess().getConstructionLogClass(),
                        createResult.getContext().getOperator(),
                        wbsEntityType,
                        createResult.getActInstDTO().getEntityId(),
                        process,
                        bpmProcess.getId(),
                        bpmProcess.getProcessStage().getNameEn(),
                        testResultDTO);
                applicationEventPublisher.publishEvent(constructionLogEvent);
            } catch (Exception e) {
                System.out.println("There is no such entity Type");
            }

        }

        return createResult;
    }

    @Override
    public void prepareExecute(ContextDTO contextDTO, Map<String, Object> data,
                               BpmActivityInstanceBase actInst, BpmRuTask ruTask, TodoTaskDTO todoDTO) {
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
//        WeldHistory weldHis = todoIndividualTaskService.getWeldHis(actInst.getEntityId());
//        if (weldHis != null)
//            todoDTO.setWeldCount(weldHis.getWeldCount());
//        todoDTO.setWeldEntity(todoIndividualTaskService.getWeldEntity(orgId, projectId, actInst));

        if (taskRuleCheckService.isWeldExecuteTaskNode(ruTask.getTaskType())
            || taskRuleCheckService.isFormanAssignWorkerTaskNode(ruTask.getTaskType())) {
            TaskMaterialDTO materialDTO = todoTaskBaseService.getMaterial(ruTask.getActInstId());
            todoDTO.setMaterialDTO(materialDTO);
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public ExecResultDTO completeExecute(ExecResultDTO execResult) {

        Long orgId = execResult.getOrgId();
        Long projectId =execResult.getProjectId();

        BpmProcess bpmProcess = execResult.getBpmProcess();
        if(bpmProcess == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("BPM PROCESS IS NULL");
            return execResult;
        }


        String entityType = execResult.getActInst().getEntityType();

        if (!StringUtils.isEmpty(bpmProcess.getConstructionLogClass())) {

            TestResultDTO testResultDTO = new TestResultDTO();
            testResultDTO.setTestResult(EntityTestResult.FINISHED);
            if(execResult.getActInstState().isExecuteNgFlag()) {
                testResultDTO.setExInspStatus(ExInspStatus.FAILED);
            } else {
                testResultDTO.setExInspStatus(ExInspStatus.OK);
            }
            testResultDTO.setActInstId(execResult.getActInst().getId());
            testResultDTO.setOrgId(orgId);
            testResultDTO.setProjectId(projectId);
            testResultDTO.setActInstId(execResult.getActInst().getId());
            testResultDTO.setWorkTeamId(execResult.getActInstState().getTeamId());
            testResultDTO.setWorkTeam(execResult.getActInstState().getTeamName());
            testResultDTO.setWorkSiteId(execResult.getActInstState().getWorkSiteId());
            testResultDTO.setWorkSite(execResult.getActInstState().getWorkSiteName());
            Date finishedAt = execResult.getActInstState().getEndDate();
            testResultDTO.setFinishedAt(finishedAt == null ? new Date() : finishedAt);
            testResultDTO.setTaskPackage(execResult.getActInstState().getTaskPackageName());
            testResultDTO.setTaskPackageId(execResult.getActInstState().getTaskPackageId());


            if (!StringUtils.isEmpty(execResult.getBpmProcess().getConstructionLogClass())) {

                String wbsEntityType;
                try {
                    wbsEntityType = entityType;
                    ConstructionLogEvent constructionLogEvent =
                        new ConstructionLogEvent(execResult.getBpmProcess().getConstructionLogClass(),
                            execResult.getContext().getOperator(),
                            wbsEntityType,
                            execResult.getActInst().getEntityId(),
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
        logger.info("开始更新后续四级计划状态-> 当前时间："+new Date());

        asyncPlanService.callAsyncPlanExecuteFinish(execResult.getContext(), execResult.getExeVersion(), execResult.getOrgId(), execResult.getProjectId(), false);
        logger.info("开始更新后续四级计划状态-> 结束后时间："+new Date());

        return execResult;
    }

}
