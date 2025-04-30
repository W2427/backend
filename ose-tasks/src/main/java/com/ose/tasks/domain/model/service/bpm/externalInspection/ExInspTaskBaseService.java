package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.*;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.vo.bpm.*;
import com.ose.vo.EntityStatus;
import com.ose.vo.InspectParty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务。
 */
@Component
public class ExInspTaskBaseService implements ExInspTaskBaseInterface {

    private final static Logger logger = LoggerFactory.getLogger(ExInspTaskBaseService.class);



    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmExInspScheduleDetailRepository bpmExInspScheduleDetailRepository;

    private final HierarchyInterface hierarchyService;

    private final WBSEntryRepository wbsEntryRepository;

    private final QCReportRepository qcReportRepository;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final BpmProcessRepository bpmProcessRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;
    private final WBSEntryStateRepository wbsEntryStateRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public ExInspTaskBaseService(
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmRuTaskRepository ruTaskRepository,
        BpmExInspScheduleDetailRepository bpmExInspScheduleDetailRepository,
        HierarchyInterface hierarchyService,
        WBSEntryRepository wbsEntryRepository,
        QCReportRepository qcReportRepository,
        BpmExInspScheduleRepository bpmExInspScheduleRepository,
        BpmProcessRepository bpmProcessRepository,
        ExInspActInstRelationRepository exInspActInstRelationRepository,
        WBSEntryStateRepository wbsEntryStateRepository) {
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmExInspScheduleDetailRepository = bpmExInspScheduleDetailRepository;
        this.hierarchyService = hierarchyService;
        this.wbsEntryRepository = wbsEntryRepository;
        this.qcReportRepository = qcReportRepository;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.bpmProcessRepository = bpmProcessRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
    }


    @Override
    public Page<BpmRuTask> getExternalInspectionRuTaskList(Long orgId, Long projectId, Long assignee,
                                                           List<Long> actInstIds, PageDTO pageDTO) {
        return ruTaskRepository.getExternalInspectionRuTaskList(orgId, projectId, assignee.toString(), actInstIds, pageDTO);
    }


    @Override
    public List<ExInspEntityInfoDTO> getExternalInspectionEntityInfo(Long orgId, Long projectId,
                                                                     String seriesNo) {
        List<ExInspEntityInfoDTO> result = new ArrayList<>();
        QCReport qcReport = null;
        try {
            qcReport = qcReportRepository
                .findByProjectIdAndSeriesNo(projectId, seriesNo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if (qcReport == null) {return result;}

        Long reportFileId = qcReport.getUploadFileId();
        Long scheduleId = qcReport.getScheduleId();
        if(scheduleId == null) {return result;}

        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(
            orgId, projectId, scheduleId
        );

        List<Long> actInstIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getActInstId() != null).
            map(TaskProcQLDTO::getActInstId).collect(Collectors.toList());
        Long[] strInsts = new Long[actInstIds.size()];
        List<BpmActivityInstanceBase> actInsts = bpmActInstRepository.findByProjectIdAndIdInAndStatus(projectId, actInstIds.toArray(strInsts), EntityStatus.ACTIVE);
        BpmProcess bpmProcess = null;
        for (BpmActivityInstanceBase actInst : actInsts) {
            if(bpmProcess == null) {
                bpmProcess = bpmProcessRepository.findById(actInst.getProcessId()).orElse(null);
            }
            if (bpmProcess == null) {
                continue;
            }

            List<ExInspEntityInfoDTO> inspEntityInfos = new ArrayList<>();
            String reportNo = qcReport.getReportNo();
            if (bpmProcess.getProcessType() == ProcessType.DELIVERY_LIST){
//                bpmDeliveryEntityRepository.findByDeliveryId(actInst.getEntityId()).forEach(entity -> {
//
//                    ExInspEntityInfoDTO dto = new ExInspEntityInfoDTO();
//                    dto.setReportNo(reportNo);
//                    dto.setEntityId(entity.getEntityId());
//                    dto.setEntityNo(entity.getEntityNo());
//                    dto.setProcess(actInst.getProcess());
//                    dto.setProcessStage(actInst.getProcessStage());
//                    dto.setReportId(reportFileId);
//                    inspEntityInfos.add(dto);
//                });

            } else {
                ExInspEntityInfoDTO dto = new ExInspEntityInfoDTO();
                dto.setReportNo(qcReport.getReportNo());
                dto.setEntityId(actInst.getEntityId());
                dto.setEntityNo(actInst.getEntityNo());
                dto.setProcess(actInst.getProcess());
                dto.setProcessStage(actInst.getProcessStage());
                dto.setReportId(reportFileId);
                inspEntityInfos.add(dto);
            }

            for(ExInspEntityInfoDTO dto :  inspEntityInfos) {
                try {
                    dto.setArea(
                        hierarchyService
                            .getHierarchyNodeByEntityIdAndHierarchyType(orgId, projectId, dto.getEntityId(), "PIPING")
                            .getNode()
                            .getNo()
                    );
                } catch (Exception e) {

                }

                try {
                    dto.setCleanPackage(
                        hierarchyService
                            .getHierarchyNodeByEntityIdAndHierarchyType(orgId, projectId, dto.getEntityId(), "CLEAN_PACKAGE")
                            .getNode()
                            .getNo()
                    );
                } catch (Exception e) {


                }

                try {
                    dto.setLayer(
                        hierarchyService
                            .getHierarchyNodeByEntityIdAndHierarchyType(orgId, projectId, dto.getEntityId(), "PIPING")
                            .getNode()
                            .getNo()
                    );
                } catch (Exception e) {

                }

                try {
                    dto.setPressureTestPackage(
                        hierarchyService
                            .getHierarchyNodeByEntityIdAndHierarchyType(orgId, projectId, dto.getEntityId(), "PRESSURE_TEST_PACKAGE")
                            .getNode()
                            .getNo()
                    );
                } catch (Exception e) {

                }

                try {
                    dto.setSubSystem(
                        hierarchyService
                            .getHierarchyNodeByEntityIdAndHierarchyType(orgId, projectId, dto.getEntityId(), "SUB_SYSTEM")
                            .getNode()
                            .getNo()
                    );
                } catch (Exception e) {

                }

                result.add(dto);
            }
        }

        return result;
    }


    /**
     * 取得工作场地
     *
     * @param projectId     项目ID
     * @param actInstList   流程列表
     * @return
     */
    @Override
    public String getWorkSiteName(Long projectId, List<BpmActivityInstanceBase> actInstList) {
        String workSiteName = "";
        for (BpmActivityInstanceBase actInst : actInstList) {
            Optional<WBSEntryState> op = wbsEntryStateRepository.findByProjectIdAndEntityIdAndStageAndProcessAndDeletedIsFalse(
                projectId, actInst.getEntityId(), actInst.getProcessStage(), actInst.getProcess());
            if (op.isPresent()) {
                String name = op.get().getWorkSiteName();
                if (name != null
                    && !"".equals(name)) {
                    workSiteName = op.get().getWorkSiteName();
                    break;
                }
            }
        }
        return workSiteName;
    }



    /**
     * 生成 报检详情，对应于 不同的检验方
     *
     * @param orgId                            组织ID
     * @param projectId                        项目ID
     * @param exInspSchedule                   报检安排
     * @param inspectParty                     外检方
     * @param operator                         用户
     * @return  外检计划详情
     */
    @Override
    public BpmExInspScheduleDetail generateExInspScheduleDetail(Long orgId,
                                                                Long projectId,
                                                                BpmExInspSchedule exInspSchedule,
                                                                InspectParty inspectParty,
                                                                OperatorDTO operator,
                                                                BpmExInspScheduleDetail exInspScheduleDetail) {


        exInspScheduleDetail.setInspectParty(inspectParty);
        exInspScheduleDetail.setOrgId(orgId);
        exInspScheduleDetail.setProjectId(projectId);
        exInspScheduleDetail.setScheduleId(exInspSchedule.getId());

        exInspScheduleDetail.setOperator(operator.getId());
        exInspScheduleDetail.setStatus(EntityStatus.ACTIVE);
        exInspScheduleDetail.setCreatedBy(operator.getId());
        exInspScheduleDetail.setLastModifiedBy(operator.getId());
        exInspScheduleDetail.setApplyStatus(ExInspApplyStatus.APPLY);


        bpmExInspScheduleDetailRepository.save(exInspScheduleDetail);

        return exInspScheduleDetail;
    }




    @Override
    public BpmExInspSchedule getExInspScheduleByActInst(Long orgId, Long projectId,
                                                                    BpmActivityInstanceBase actInst) {

        if (actInst == null){
            return null;
        }

        List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInst.getId());
        String reportQrCode = null;
        for (BpmRuTask ruTask : ruTasks) {
            List<ActReportDTO> reports = ruTask.getJsonReportsReadOnly();
            for (ActReportDTO report : reports) {
                reportQrCode = report.getReportQrCode();
                break;
            }
            if (reportQrCode != null) {break;}
        }

        if (reportQrCode != null) {return null;}

        BpmExInspSchedule exInspSchedule = bpmExInspScheduleRepository.findByReportQrCode(reportQrCode, EntityStatus.ACTIVE);
        return exInspSchedule;


    }



}
