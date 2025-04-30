package com.ose.tasks.Listener.handler;

import com.ose.util.HttpUtils;
import com.ose.auth.api.UserFeignAPI;
import com.ose.controller.BaseController;
import com.ose.dto.EventModel;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.ServerConfig;
import com.ose.tasks.domain.model.service.bpm.ActTaskConfigInterface;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspScheduleInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ose.vo.EventType.PLAN_QUEUE;

@Component
public class PlanQueueHandler extends BaseController implements EventHandlerInterface {

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final QCReportRepository qcReportRepository;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final BpmActivityInstanceRepository actInstRepository;

    private final ExInspScheduleInterface exInspScheduleService;

    private final BpmRuTaskRepository ruTaskRepository;


    private final UserFeignAPI userFeignAPI;

    private final ProjectRepository projectRepository;

    private final ActTaskConfigInterface actTaskConfigService;

    private final BpmActTaskRepository bpmActTaskRepository;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final ProjectInterface projectService;

    private final ProcessInterface processService;

    private final BpmErrorRepository bpmErrorRepository;

    private final ServerConfig serverConfig;


    /**
     * 构造方法。
     * @param exInspActInstRelationRepository
     * @param qcReportRepository
     * @param bpmExInspScheduleRepository
     * @param projectNodeRepository
     * @param actInstRepository
     * @param exInspScheduleService
     * @param ruTaskRepository
     * @param userFeignAPI
     * @param projectRepository
     * @param actTaskConfigService
     * @param bpmActTaskRepository
     * @param todoTaskBaseService
     * @param taskRuleCheckService
     * @param projectService
     * @param processService
     * @param bpmErrorRepository
     * @param serverConfig
     */
    @Autowired
    PlanQueueHandler(
        ExInspActInstRelationRepository exInspActInstRelationRepository,
        QCReportRepository qcReportRepository,
        BpmExInspScheduleRepository bpmExInspScheduleRepository,
        ProjectNodeRepository projectNodeRepository,
        BpmActivityInstanceRepository actInstRepository,
        ExInspScheduleInterface exInspScheduleService,
        BpmRuTaskRepository ruTaskRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        ProjectRepository projectRepository, ActTaskConfigInterface actTaskConfigService,
        BpmActTaskRepository bpmActTaskRepository,
        TodoTaskBaseInterface todoTaskBaseService,
        TaskRuleCheckService taskRuleCheckService,
        ProjectInterface projectService, ProcessInterface processService, BpmErrorRepository bpmErrorRepository, ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.qcReportRepository = qcReportRepository;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.actInstRepository = actInstRepository;
        this.exInspScheduleService = exInspScheduleService;
        this.ruTaskRepository = ruTaskRepository;
        this.userFeignAPI = userFeignAPI;
        this.projectRepository = projectRepository;
        this.actTaskConfigService = actTaskConfigService;
        this.bpmActTaskRepository = bpmActTaskRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.projectService = projectService;
        this.processService = processService;
        this.bpmErrorRepository = bpmErrorRepository;
    }

    @Override
    public void doHandler(EventModel model) {

        if(model == null) {
            throw new BusinessError("There is no EventModel");
        }




        Map<String, String> body = new HashMap<>();
        body.put("authorization", model.getAuthorization());
        body.put("userAgent", model.getUserAgent());
        body.put("operatorId", model.getOperatorId().toString());
        body.put("projectId", model.getProjectId().toString());
        body.put("taskDefKey", model.getTaskDefKey());
        body.put("entityId", model.getEntityId().toString());


        HttpUtils.postJSON(serverConfig.getUrl()+"/plan-queue", body, String.class);


    }


    @Override
    public String getSupportEventType() {
        return PLAN_QUEUE.name();
    }


}
