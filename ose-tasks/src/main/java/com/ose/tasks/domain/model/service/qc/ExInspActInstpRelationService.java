package com.ose.tasks.domain.model.service.qc;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmExInspScheduleRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.bpm.QCReportRepository;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.report.ExInspActInstRelation;
import com.ose.tasks.entity.report.QCReport;
import com.ose.util.CollectionUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExInspActInstpRelationService implements ExInspActInstRelationInterface {

    private final BpmExInspScheduleRepository exInspScheduleRepository;
    private final BpmRuTaskRepository bpmRuTaskRepository;
    private final BpmActivityInstanceRepository actInstRepository;
    private final ExInspActInstRelationRepository exInspActInstRelationRepository;
    private final QCReportRepository qcReportRepository;



    /**
     * 构造器。
     *
     */
    @Autowired
    public ExInspActInstpRelationService(
        BpmExInspScheduleRepository exInspScheduleRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository actInstRepository,
        ExInspActInstRelationRepository exInspActInstRelationRepository,
        QCReportRepository qcReportRepository) {
        this.exInspScheduleRepository = exInspScheduleRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.actInstRepository = actInstRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.qcReportRepository = qcReportRepository;
    }

    /**
     * 创建EX INSP ACT INSP 信息。
     *
     * @param operatorId            操作人ID
     * @param orgId                 组织ID
     * @param projectId             项目ID
     */
    @Override
    public void init(
        Long orgId,
        Long projectId,
        Long operatorId
        ) {

        int pageNo = 0;
        int BATCH_FETCH_SIZE = 1000;

        while(true) {
            List<QCReport> qcReports = qcReportRepository.findByProjectId(projectId, PageRequest.of(pageNo, BATCH_FETCH_SIZE));

            qcReports.forEach(qcReport -> {
                Long reportId = qcReport.getId();
                Long scheduleId = qcReport.getScheduleId();

                List<Long> actInstIds = qcReport.getJsonActInstIds();
                actInstIds.forEach(actInstId -> {
                    Long entityId = null;
                    Long projectNodeId = null;

                    BpmActivityInstanceBase actInst = actInstRepository.findById(actInstId).orElse(null);
                    if (actInst != null) {
                        entityId = actInst.getEntityId();
                        projectNodeId = actInst.getEntityProjectNodeId();
                    }
                    List<BpmRuTask> ruTasks = bpmRuTaskRepository.findByActInstId(actInstId);
                    List<ExInspActInstRelation> exInspActInstRelations =
                        exInspActInstRelationRepository.findByOrgIdAndProjectIdAndExInspScheduleIdAndActInstId(
                            orgId,projectId,scheduleId,actInstId
                        );
                    exInspActInstRelations.forEach(exInspActInstRelation -> {
                        exInspActInstRelationRepository.deleteById(exInspActInstRelation.getId());
                    });
                    if(!CollectionUtils.isEmpty(ruTasks)) {
                        for (BpmRuTask ruTask : ruTasks) {
                            ExInspActInstRelation exInspActInstRelation = new ExInspActInstRelation();
                            Long taskId = ruTask.getId();
                            exInspActInstRelation.setTaskId(taskId);
                            exInspActInstRelation.setStatus(EntityStatus.ACTIVE);
                            exInspActInstRelation.setCreatedAt();
                            exInspActInstRelation.setLastModifiedAt();
                            exInspActInstRelation.setExInspScheduleId(scheduleId);
                            exInspActInstRelation.setOrgId(orgId);
                            exInspActInstRelation.setActInstId(actInstId);
                            exInspActInstRelation.setProjectId(projectId);
                            exInspActInstRelation.setProjectNodeId(projectNodeId);
                            exInspActInstRelation.setReportId(qcReport.getId());
                            exInspActInstRelationRepository.save(exInspActInstRelation);
                        }
                    } else {
                        ExInspActInstRelation exInspActInstRelation = new ExInspActInstRelation();
                        exInspActInstRelation.setTaskId(null);
                        exInspActInstRelation.setStatus(EntityStatus.ACTIVE);
                        exInspActInstRelation.setCreatedAt();
                        exInspActInstRelation.setLastModifiedAt();
                        exInspActInstRelation.setExInspScheduleId(scheduleId);
                        exInspActInstRelation.setOrgId(orgId);
                        exInspActInstRelation.setActInstId(actInstId);
                        exInspActInstRelation.setProjectId(projectId);
                        exInspActInstRelation.setProjectNodeId(projectNodeId);
                        exInspActInstRelation.setReportId(qcReport.getId());
                        exInspActInstRelationRepository.save(exInspActInstRelation);
                    }
                });

                });

            if (qcReports.size() < BATCH_FETCH_SIZE) {
                break;
            }

            pageNo++;

        }
    }

    @Override
    public void initScheduleId(Long orgId, Long projectId, Long scheduleId){
        qcReportRepository.findByProjectIdAndScheduleId(projectId, scheduleId).forEach(
            qcReport -> {




            List<Long> actInstIds = qcReport.getJsonActInstIds();
            actInstIds.forEach(actInstId -> {
                Long entityId = null;
                Long projectNodeId = null;

                BpmActivityInstanceBase actInst = actInstRepository.findById(actInstId).orElse(null);
                if (actInst != null) {
                    entityId = actInst.getEntityId();
                    projectNodeId = actInst.getEntityProjectNodeId();
                }
                List<BpmRuTask> ruTasks = bpmRuTaskRepository.findByActInstId(actInstId);
                List<ExInspActInstRelation> exInspActInstRelations =
                    exInspActInstRelationRepository.findByOrgIdAndProjectIdAndExInspScheduleIdAndActInstId(
                        orgId,projectId,scheduleId,actInstId
                    );
                exInspActInstRelations.forEach(exInspActInstRelation -> {
                    exInspActInstRelationRepository.deleteById(exInspActInstRelation.getId());
                });
                if(!CollectionUtils.isEmpty(ruTasks)) {
                    for (BpmRuTask ruTask : ruTasks) {
                        ExInspActInstRelation exInspActInstRelation = new ExInspActInstRelation();
                        Long taskId = ruTask.getId();
                        exInspActInstRelation.setTaskId(taskId);
                        exInspActInstRelation.setExInspScheduleId(scheduleId);
                        exInspActInstRelation.setOrgId(orgId);
                        exInspActInstRelation.setActInstId(actInstId);
                        exInspActInstRelation.setProjectId(projectId);
                        exInspActInstRelation.setProjectNodeId(projectNodeId);
                        exInspActInstRelation.setReportId(qcReport.getId());
                        exInspActInstRelation.setStatus(EntityStatus.ACTIVE);
                        exInspActInstRelation.setLastModifiedAt();
                        exInspActInstRelation.setCreatedAt();
                        exInspActInstRelationRepository.save(exInspActInstRelation);
                    }
                } else {
                    ExInspActInstRelation exInspActInstRelation = new ExInspActInstRelation();
                    exInspActInstRelation.setTaskId(null);
                    exInspActInstRelation.setStatus(EntityStatus.ACTIVE);
                    exInspActInstRelation.setCreatedAt();
                    exInspActInstRelation.setLastModifiedAt();
                    exInspActInstRelation.setExInspScheduleId(scheduleId);
                    exInspActInstRelation.setOrgId(orgId);
                    exInspActInstRelation.setActInstId(actInstId);
                    exInspActInstRelation.setProjectId(projectId);
                    exInspActInstRelation.setProjectNodeId(projectNodeId);
                    exInspActInstRelation.setReportId(qcReport.getId());
                    exInspActInstRelationRepository.save(exInspActInstRelation);
                }

            });

        });
    }

}
