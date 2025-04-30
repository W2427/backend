package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.feign.RequestWrapper;
import com.ose.tasks.domain.model.repository.bpm.BpmPlanExecutionHistoryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.plan.PlanExecutionInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.wbs.WBSEntryPlainStartDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmPlanExecutionHistory;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.vo.bpm.BpmPlanExecutionState;
import com.ose.tasks.vo.setting.BatchTaskCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AsyncPlanService implements AsyncPlanInterface {

    private final static Logger logger = LoggerFactory.getLogger(AsyncPlanService.class);


    private final ServerConfig serverConfig;
    private final BpmPlanExecutionHistoryRepository bpmPlanExecutionHistoryRepository;
    private final PlanExecutionInterface planExecutionService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    @Autowired

    public AsyncPlanService(
        ServerConfig serverConfig,
        PlanExecutionInterface planExecutionService,
        BpmPlanExecutionHistoryRepository bpmPlanExecutionHistoryRepository,
        TodoTaskDispatchInterface todoTaskDispatchService,
        WBSEntryStateRepository wbsEntryStateRepository,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService) {
        this.serverConfig = serverConfig;
        this.planExecutionService = planExecutionService;
        this.bpmPlanExecutionHistoryRepository = bpmPlanExecutionHistoryRepository;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
    }

    @Override
    public boolean callAsyncPlanExecuteFinish(
        final ContextDTO context,
        Long version,
        Long orgId,
        Long projectId,
        boolean isPartOut
    ) {
        if (!context.isContextSet()) {
            String authorization = context.getAuthorization();
            String userAgent = context.getUserAgent();

            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                context.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
            Project project = projectService.get(orgId, projectId);

            logger.info("启动后续四级计划任务，新建线程开始：" + new Date());
            batchTaskService.runConstructTaskExecutor(
                null,
                project,
                BatchTaskCode.PLAN,
                false,
                context,
                batchTask -> {
                    Long bpmPlanExecutionHistoryId = null;
                    Boolean isHalt = null;

                    List<BpmPlanExecutionHistory> hisList = bpmPlanExecutionHistoryRepository
                        .findByProjectIdAndVersionAndExecutionStateAndServerUrl(projectId, version, BpmPlanExecutionState.UNDO, serverConfig.getUrl());
                    for (BpmPlanExecutionHistory his : hisList) {
                        try {
                            bpmPlanExecutionHistoryId = his.getId();

                            OperatorDTO operatorDTO = new OperatorDTO(his.getOperator(), his.getOperatorName());
                            his.setExecutionState(BpmPlanExecutionState.DOING);
                            his.setStartTimestamp(new Date().getTime());
                            his.setLastModifiedAt();
                            bpmPlanExecutionHistoryRepository.save(his);


                            isHalt = his.getHalt();


                            WBSEntry wBSEntry = planExecutionService.finish(context, todoTaskDispatchService, operatorDTO, orgId, projectId, his.getEntityId(),
                                his.getProcessId(), his.getApproved(), his.getHours(), isHalt, isPartOut, his.getForceStart()==null?false:his.getForceStart());


                            if (wBSEntry == null) {
                                his.setExecutionState(BpmPlanExecutionState.NOT_FOUND);
                                his.setEndTimestamp(new Date().getTime());
                                his.setLastModifiedAt();
                                bpmPlanExecutionHistoryRepository.save(his);

                            } else {
                                logger.info("管系下料实体对应计划是否查询到：" + wBSEntry.getName() == null ? null : wBSEntry.getId().toString());
                                WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wBSEntry.getId());
                                logger.info("管系下料实体对应计划详情是否查询到：" + wbsEntryState.getWbsEntryId());
                                if (wbsEntryState != null && wbsEntryState.getTaskPackageId() != null) {
                                    his.setExecutionState(BpmPlanExecutionState.DONE);
                                    his.setEndTimestamp(new Date().getTime());
                                    his.setLastModifiedAt();
                                    bpmPlanExecutionHistoryRepository.save(his);

                                }
                            }
                        } catch (Exception e) {

                            logger.error("list planExecutionService.finish error:", e);
                            if (bpmPlanExecutionHistoryId != null) {
                                BpmPlanExecutionHistory bpmPlanExecutionHistory = bpmPlanExecutionHistoryRepository.findById(bpmPlanExecutionHistoryId).orElse(null);
                                bpmPlanExecutionHistory.setMemo(e.toString());
                                bpmPlanExecutionHistoryRepository.save(bpmPlanExecutionHistory);
                            }
                        }


                    }

                    return new BatchResultDTO();
                });
        } else {

            Long bpmPlanExecutionHistoryId = null;
            Boolean isHalt = null;

            List<BpmPlanExecutionHistory> hisList = bpmPlanExecutionHistoryRepository
                .findByProjectIdAndVersionAndExecutionStateAndServerUrl(projectId, version, BpmPlanExecutionState.UNDO, serverConfig.getUrl());
            for (BpmPlanExecutionHistory his : hisList) {
                try {
                    bpmPlanExecutionHistoryId = his.getId();

                    OperatorDTO operatorDTO = new OperatorDTO(his.getOperator(), his.getOperatorName());
                    his.setExecutionState(BpmPlanExecutionState.DOING);
                    his.setStartTimestamp(new Date().getTime());
                    his.setLastModifiedAt();
                    bpmPlanExecutionHistoryRepository.save(his);


                    isHalt = his.getHalt();


                    WBSEntry wBSEntry = planExecutionService.finish(context, todoTaskDispatchService, operatorDTO, orgId, projectId, his.getEntityId(),
                        his.getProcessId(), his.getApproved(), his.getHours(), isHalt, isPartOut, his.getForceStart() == null?false:his.getForceStart());


                    if (wBSEntry == null) {
                        his.setExecutionState(BpmPlanExecutionState.NOT_FOUND);
                        his.setEndTimestamp(new Date().getTime());
                        his.setLastModifiedAt();
                        bpmPlanExecutionHistoryRepository.save(his);

                    } else {
                        logger.info("管系下料实体对应计划是否查询到：" + wBSEntry.getName() == null ? null : wBSEntry.getId().toString());
                        WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wBSEntry.getId());
                        logger.info("管系下料实体对应计划详情是否查询到：" + wbsEntryState.getWbsEntryId());
                        if (wbsEntryState != null && wbsEntryState.getTaskPackageId() != null) {
                            his.setExecutionState(BpmPlanExecutionState.DONE);
                            his.setEndTimestamp(new Date().getTime());
                            his.setLastModifiedAt();
                            bpmPlanExecutionHistoryRepository.save(his);

                        }
                    }
                } catch (Exception e) {

                    logger.error("list planExecutionService.finish error:", e);
                    if (bpmPlanExecutionHistoryId != null) {
                        BpmPlanExecutionHistory bpmPlanExecutionHistory = bpmPlanExecutionHistoryRepository.findById(bpmPlanExecutionHistoryId).orElse(null);
                        bpmPlanExecutionHistory.setMemo(e.toString());
                        bpmPlanExecutionHistoryRepository.save(bpmPlanExecutionHistory);
                    }
                }
            }

        }

        logger.info("启动后续四级计划任务，新建线程结束：" + new Date());
        return true;
    }

    @Override
    public boolean callAsyncPlanByProcessExecuteFinish(
        final ContextDTO context,
        Long orgId,
        Long projectId,
        WBSEntryPlainStartDTO wBSEntryPlainStartDTO,
        boolean isPartOut
    ) {


        if (!context.isContextSet()) {
            String authorization = context.getAuthorization();
            String userAgent = context.getUserAgent();


            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                context.getResponse()
            );

            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }


        Project project = projectService.get(orgId, projectId);

        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.PLAN,
            false,
            context,
            batchTask -> {
                Long bpmPlanExecutionHistoryId = null;

                List<BpmPlanExecutionHistory> hisList = new ArrayList<>();
                if (wBSEntryPlainStartDTO.getProcessIds() != null && wBSEntryPlainStartDTO.getProcessIds().size() > 0) {
                    hisList = bpmPlanExecutionHistoryRepository
                        .findByProjectIdAndProcessIdInAndExecutionStateAndServerUrl(projectId, wBSEntryPlainStartDTO.getProcessIds(), BpmPlanExecutionState.UNDO, serverConfig.getUrl());
                } else {
                    hisList = bpmPlanExecutionHistoryRepository
                        .findByProjectIdAndExecutionStateAndServerUrl(projectId, BpmPlanExecutionState.UNDO, serverConfig.getUrl());
                }

                Boolean isHalt = null;
                for (BpmPlanExecutionHistory his : hisList) {
                    try {

                        bpmPlanExecutionHistoryId = his.getId();

                        OperatorDTO operatorDTO = new OperatorDTO(his.getOperator(), his.getOperatorName());
                        his.setExecutionState(BpmPlanExecutionState.DOING);
                        his.setStartTimestamp(new Date().getTime());
                        his.setLastModifiedAt();
                        bpmPlanExecutionHistoryRepository.save(his);
                        isHalt = his.getHalt();


                        WBSEntry wBSEntry = planExecutionService.finish(context, todoTaskDispatchService, operatorDTO, orgId, projectId, his.getEntityId(),
                            his.getProcessId(), his.getApproved(), his.getHours(), isHalt, isPartOut, his.getForceStart()==null?false:his.getForceStart());

                        if (wBSEntry == null) {
                            his.setExecutionState(BpmPlanExecutionState.NOT_FOUND);
                            his.setEndTimestamp(new Date().getTime());
                            his.setLastModifiedAt();
                            bpmPlanExecutionHistoryRepository.save(his);


                        } else {
                            his.setExecutionState(BpmPlanExecutionState.DONE);
                            his.setEndTimestamp(new Date().getTime());
                            his.setLastModifiedAt();
                            bpmPlanExecutionHistoryRepository.save(his);


                        }
                    } catch (Exception e) {

                        logger.error("list planExecutionService.finish error:", e);
                        if (bpmPlanExecutionHistoryId != null) {
                            BpmPlanExecutionHistory bpmPlanExecutionHistory = bpmPlanExecutionHistoryRepository.findById(bpmPlanExecutionHistoryId).orElse(null);
                            bpmPlanExecutionHistory.setMemo(e.toString());
                            bpmPlanExecutionHistoryRepository.save(bpmPlanExecutionHistory);
                        }
                    }


                }

                return new BatchResultDTO();
            });
        return true;
    }

}
