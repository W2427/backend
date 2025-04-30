package com.ose.tasks.domain.model.service.constructlog.pipingConstructionLog;


import com.ose.auth.vo.ExecutorRole;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActTaskRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityDocsMaterialsRepository;
import com.ose.tasks.domain.model.repository.bpm.QCReportRepository;
import com.ose.tasks.domain.model.repository.qc.PipingConstructionLogRepository;
import com.ose.tasks.dto.qc.TestResultDTO;
import com.ose.tasks.entity.bpm.BpmActTask;
import com.ose.tasks.entity.qc.BaseConstructionLog;
import com.ose.tasks.entity.qc.PipingTestLog;
import com.ose.tasks.vo.wbs.EntityTestResult;
import com.ose.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PipingConstructionLog extends AbstractConstructionLog {

    private final PipingConstructionLogRepository pipingConstructionLogRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final BpmEntityDocsMaterialsRepository entityDocsRepository;

    private final BpmActTaskRepository bpmActTaskRepository;

    private final QCReportRepository qcReportRepository;


    @Autowired
    public PipingConstructionLog(PipingConstructionLogRepository pipingConstructionLogRepository,
                                 ProjectNodeRepository projectNodeRepository,
                                 BpmEntityDocsMaterialsRepository entityDocsRepository, BpmActTaskRepository bpmActTaskRepository,
                                 QCReportRepository qcReportRepository) {
        this.pipingConstructionLogRepository = pipingConstructionLogRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.entityDocsRepository = entityDocsRepository;
        this.bpmActTaskRepository = bpmActTaskRepository;
        this.qcReportRepository = qcReportRepository;
    }


    /**
     * 创建管道检测结果。
     *
     * @param operatorDTO          操作人信息
     * @param wbsEntityType        实体类型
     * @param entityId             实体ID
     * @param processNameEn        工序
     * @param testResultDTO        测试结果DTO
     */
    @Override
    public <T extends TestResultDTO> void createConstructLog(OperatorDTO operatorDTO,
                                                            String wbsEntityType,
                                                            Long entityId,
                                                            String processNameEn,
                                                            Long processId,
                                                            String processStage,
                                                            T testResultDTO) {

        PipingTestLog pipingTestLog = buildConstructLog(operatorDTO,
                                            wbsEntityType,
                                            entityId,
                                            processNameEn,
                                            processId,
                                            processStage,
                                            testResultDTO);
        pipingTestLog = setEntitySubType(pipingTestLog, wbsEntityType);

        if (EntityTestResult.FINISHED.equals(testResultDTO.getTestResult())) {
            Map<String, Set<String>> roleTaskDefKeyMap = new HashMap<>();
            Long actInstId = testResultDTO.getActInstId();
            pipingTestLog = setRolePerformers(pipingTestLog, actInstId, entityId, testResultDTO, roleTaskDefKeyMap);
        }

        pipingConstructionLogRepository.save(pipingTestLog);

    }


    /**
     * 搭建管道检测结果实例。
     *
     * @param operatorDTO          操作人信息
     * @param wbsEntityType        实体类型
     * @param entityId             实体ID
     * @param processNameEn        工序
     * @param testResultDTO        测试结果DTO
     */
    @Override
    public <T extends TestResultDTO> PipingTestLog buildConstructLog(OperatorDTO operatorDTO,
                                                             String wbsEntityType,
                                                             Long entityId,
                                                             String processNameEn,
                                                             Long processId,
                                                             String processStage,
                                                             T testResultDTO) {



        return null;

    }

    @Override
    public <T extends TestResultDTO> PipingTestLog setRolePerformers(PipingTestLog pipingTestLog,
                                                                     Long actInstId,
                                                                     Long entityId,
                                                                     T testResultDTO,
                                                                     Map<String, Set<String>> roleTaskDefKeyMap) {
        Set<ExecutorRole> executorRoles = new HashSet<>();
        executorRoles.add(ExecutorRole.FITTER);
        executorRoles.add(ExecutorRole.QC);
        executorRoles.add(ExecutorRole.SUPERVISOR);
        List<BpmActTask> bpmActTasks = bpmActTaskRepository.findByActInstIdOrderByCreatedAtDesc(
            actInstId
        );

        Set<String> fitterTaskDefKeys = null;
        Set<String> supervisorTaskDefKeys = null;
        Set<String> inQcTaskDefKeys = null;
        Set<String> qcTaskDefKeys = null;
        if(roleTaskDefKeyMap != null) {
            fitterTaskDefKeys = roleTaskDefKeyMap.get("FITTER");
            supervisorTaskDefKeys = roleTaskDefKeyMap.get("SUPERVISOR");
            inQcTaskDefKeys = roleTaskDefKeyMap.get("INTERNAL_QC");
            qcTaskDefKeys = roleTaskDefKeyMap.get("QC");
        }

        String executors = null;
        String inspector = null;
        String inQc = null;
        String supervisor = null;
        Date performedAt = null;
        Date qcPerformedAt = null;
        Date supervisorPerformedAt = null;
        Date internalQcPeformedAt = null;
        for (BpmActTask bpmActTask : bpmActTasks) {
            if(executors ==null && ExecutorRole.FITTER.equals(bpmActTask.getExecutorRole())){
                executors = bpmActTask.getExecutors();
                if(CollectionUtils.isEmpty(fitterTaskDefKeys)) performedAt = bpmActTask.getLastModifiedAt();
            } else if(inspector == null && ExecutorRole.QC.equals(bpmActTask.getExecutorRole())) {
                inspector = bpmActTask.getExecutors();
                if(CollectionUtils.isEmpty(qcTaskDefKeys)) qcPerformedAt = bpmActTask.getLastModifiedAt();
            } else if(supervisor == null && ExecutorRole.SUPERVISOR.equals(bpmActTask.getExecutorRole())) {
                supervisor = bpmActTask.getExecutors();
                if(CollectionUtils.isEmpty(supervisorTaskDefKeys)) supervisorPerformedAt = bpmActTask.getLastModifiedAt();
            } else if(inQc == null && ExecutorRole.INTERNAL_QC.equals(bpmActTask.getExecutorRole())) {
                inQc = bpmActTask.getExecutors();
                if(CollectionUtils.isEmpty(inQcTaskDefKeys)) internalQcPeformedAt = bpmActTask.getLastModifiedAt();
            }

            if(bpmActTask.getTaskDefKey() != null && !CollectionUtils.isEmpty(fitterTaskDefKeys) && fitterTaskDefKeys.contains(bpmActTask.getTaskDefKey())) {
                performedAt = bpmActTask.getLastModifiedAt();
            } else if(bpmActTask.getTaskDefKey() != null && !CollectionUtils.isEmpty(supervisorTaskDefKeys) && supervisorTaskDefKeys.contains(bpmActTask.getTaskDefKey())) {
                supervisorPerformedAt = bpmActTask.getLastModifiedAt();
            } else if(bpmActTask.getTaskDefKey() != null && !CollectionUtils.isEmpty(inQcTaskDefKeys) && inQcTaskDefKeys.contains(bpmActTask.getTaskDefKey())) {
                internalQcPeformedAt = bpmActTask.getLastModifiedAt();
            } else if(bpmActTask.getTaskDefKey() != null && !CollectionUtils.isEmpty(qcTaskDefKeys) && qcTaskDefKeys.contains(bpmActTask.getTaskDefKey())) {
                qcPerformedAt = bpmActTask.getLastModifiedAt();
            }
        }
        pipingTestLog.setExecutors(executors);
        pipingTestLog.setQc(inspector);
        pipingTestLog.setSupervisor(supervisor);
        pipingTestLog.setQcPerformedAt(qcPerformedAt);
        pipingTestLog.setInternalQc(inQc);
        pipingTestLog.setInternalQcPerformedAt(internalQcPeformedAt);
        pipingTestLog.setSupervisorPerformedAt(supervisorPerformedAt);
        pipingTestLog.setPerformedAt(performedAt);
        return pipingTestLog;
    }

    @Override
    public PipingTestLog setEntitySubType(PipingTestLog pipingTestLog, String entityType) {
        pipingTestLog.setEntitySubType(entityType);
        return pipingTestLog;
    }

    @Override
    public String getWpsIds(Long orgId, Long projectId, Long weldEntityId) {
        return null;
    }


    @Override
    public Page<? extends BaseConstructionLog> getTestResult(Long orgId, Long projectId, Long entityId, String processNameEn, Pageable pageable) {

        return pipingConstructionLogRepository.findByEntityId(entityId, pageable);

    }

}
