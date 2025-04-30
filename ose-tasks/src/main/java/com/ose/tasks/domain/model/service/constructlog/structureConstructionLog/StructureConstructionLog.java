package com.ose.tasks.domain.model.service.constructlog.structureConstructionLog;


import com.ose.auth.vo.ExecutorRole;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.StructureConstructionLogRepository;
import com.ose.tasks.domain.model.service.constructlog.pipingConstructionLog.AbstractConstructionLog;
import com.ose.tasks.dto.qc.StructureTestResultDTO;
import com.ose.tasks.dto.qc.TestResultDTO;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.qc.BaseConstructionLog;
import com.ose.tasks.entity.qc.StructureTestLog;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.tasks.vo.wbs.EntityTestResult;
import com.ose.util.CollectionUtils;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StructureConstructionLog extends AbstractConstructionLog {
    private final static Logger logger = LoggerFactory.getLogger(StructureConstructionLog.class);


    private final StructureConstructionLogRepository structureConstructionLogRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final BpmEntityDocsMaterialsRepository entityDocsRepository;

    private final BpmActTaskRepository bpmActTaskRepository;

    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final QCReportRepository qcReportRepository;



    @Autowired
    public StructureConstructionLog(StructureConstructionLogRepository structureConstructionLogRepository,
                                    ProjectNodeRepository projectNodeRepository,
                                    BpmEntityDocsMaterialsRepository entityDocsRepository, BpmActTaskRepository bpmActTaskRepository,
                                    BpmActivityInstanceRepository bpmActivityInstanceRepository,
                                    BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                    QCReportRepository qcReportRepository) {
        this.structureConstructionLogRepository = structureConstructionLogRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.entityDocsRepository = entityDocsRepository;
        this.bpmActTaskRepository = bpmActTaskRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.qcReportRepository = qcReportRepository;
    }


    /**
     * 创建结构检测结果。
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

        StructureTestLog structureTestLog = buildStructureConstructLog(operatorDTO,
            wbsEntityType,
            entityId,
            processNameEn,
            processId,
            processStage,
            testResultDTO);
        structureTestLog = setStructureEntitySubType(structureTestLog, wbsEntityType);
        if(structureTestLog == null) return;

        if (EntityTestResult.FINISHED.equals(testResultDTO.getTestResult())) {
            Map<String, Set<String>> roleTaskDefKeyMap = new HashMap<>();

            Long actInstId = testResultDTO.getActInstId();
            structureTestLog = setStructureRolePerformers(structureTestLog, actInstId, entityId, testResultDTO, roleTaskDefKeyMap);
        }

        structureConstructionLogRepository.save(structureTestLog);

    }


    /**
     * 搭建结构检测结果实例。
     *
     * @param operatorDTO          操作人信息
     * @param wbsEntityType        实体类型
     * @param entityId             实体ID
     * @param processNameEn        工序
     * @param testResultDTO        测试结果DTO
     */
    @Override
    public <T extends TestResultDTO> StructureTestLog buildStructureConstructLog(OperatorDTO operatorDTO,
                                                                     String wbsEntityType,
                                                                     Long entityId,
                                                                     String processNameEn,
                                                                     Long processId,
                                                                     String processStage,
                                                                     T testResultDTO) {

        StructureTestResultDTO structureTestResultDTO = new StructureTestResultDTO();
        BeanUtils.copyProperties(testResultDTO, structureTestResultDTO);
        Long actInstId = testResultDTO.getActInstId();

        Long orgId = testResultDTO.getOrgId();
        Long projectId = testResultDTO.getProjectId();

        StructureTestLog structureTestLog = structureConstructionLogRepository.findByEntityIdAndActInstIdAndDeletedIsFalse(entityId, actInstId);
        if (structureTestLog == null) {

            structureTestLog = new StructureTestLog();
            ProjectNode pn = projectNodeRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, entityId).orElse(null);
            if(pn == null) {
                System.out.println(entityId.toString() + " PN not exist");
                return null;

            }
            structureTestLog.setWp01Id(pn.getId());
            structureTestLog.setWp02Id(pn.getId());
            structureTestLog.setWp03Id(pn.getId());
            structureTestLog.setWp04Id(pn.getId());
            structureTestLog.setTestResult(structureTestResultDTO.getTestResult());
            structureTestLog.setExInspStatus(structureTestResultDTO.getExInspStatus());
            structureTestLog.setActInstId(structureTestResultDTO.getActInstId());
            structureTestLog.setProcess(processNameEn);
            structureTestLog.setProcessId(processId);
            structureTestLog.setProcessStage(processStage);
            structureTestLog.setCreatedAt();
            structureTestLog.setCreatedBy(operatorDTO.getId());
            structureTestLog.setDeleted(false);
            structureTestLog.setLastModifiedBy(operatorDTO.getId());
            structureTestLog.setEntitySubType(pn.getEntitySubType());
            structureTestLog.setLastModifiedAt();
        }

        if (EntityTestResult.FINISHED.equals(structureTestResultDTO.getTestResult())) {
//            String entityNo = "";
//            Optional<BpmActivityInstanceBase> bpmActivityInstanceBase = bpmActivityInstanceRepository.findByActInstId(actInstId);
//            if (bpmActivityInstanceBase.isPresent()) {
//                entityNo = "%" + bpmActivityInstanceBase.get().getEntityNo() + "%";
//            }

            List<QCReport> qcReport = qcReportRepository.findByProcessAndReportStatusNotAndActInstId(processNameEn, EntityStatus.ACTIVE, ReportStatus.CANCEL, actInstId);
            if (qcReport != null && !CollectionUtils.isEmpty(qcReport)) {
                structureTestLog.setReportNos(qcReport.get(0).getReportNo());
                structureTestLog.setReportQrCodes(qcReport.get(0).getQrcode());
            } else {
                logger.info(structureTestLog.getProcess());
                if (structureTestLog.getProcess().equals("RT")) {
//                    String coverType = ndtDetailRepository.findByEntityIdAndNdtTypeOrderByCreatedAtDesc(
//                        entityId, NDEType.RT
//                    ).get(0).getCoverageType();
//                    if (coverType != null) {
//                        structureTestLog.setReportNos(coverType);
//                    }
                }
            }


            structureTestLog.setWpsNos(testResultDTO.getWpsNos());
        }

        structureTestLog.setTestResult(structureTestResultDTO.getTestResult());
        structureTestLog.setDeleted(false);
        structureTestLog.setLastModifiedBy(operatorDTO.getId());
        structureTestLog.setLastModifiedAt();
        structureTestLog.setOrgId(orgId);
        structureTestLog.setProjectId(projectId);
        structureTestLog.setEntityId(entityId);
        structureTestLog.setEntityType(wbsEntityType);
        structureTestLog.setStatus(EntityStatus.ACTIVE);
        structureTestLog.setVersion(structureTestLog.getLastModifiedAt().getTime());
        structureTestLog.setWorkSite(testResultDTO.getWorkSite());
        structureTestLog.setWorkSiteId(testResultDTO.getWorkSiteId());
        structureTestLog.setWorkTeam(testResultDTO.getWorkTeam());
        structureTestLog.setFinishedAt(testResultDTO.getFinishedAt());
        structureTestLog.setWorkTeamId(testResultDTO.getWorkTeamId());
        structureTestLog.setTaskPackage(testResultDTO.getTaskPackage());
        structureTestLog.setTaskPackageId(testResultDTO.getTaskPackageId());
        return structureTestLog;

    }

    @Override
    public <T extends TestResultDTO> StructureTestLog setStructureRolePerformers(StructureTestLog structureTestLog,
                                                                                 Long actInstId, Long entityId,
                                                                                 T testResultDTO,
                                                                                 Map<String, Set<String>> roleTaskDefKeyMap) {
        Set<ExecutorRole> executorRoles = new HashSet<>();
        Long orgId = structureTestLog.getOrgId();
        Long projectId = structureTestLog.getProjectId();
        executorRoles.add(ExecutorRole.FITTER);
        executorRoles.add(ExecutorRole.QC);


        return structureTestLog;
    }

    @Override public StructureTestLog setStructureEntitySubType(StructureTestLog structureTestLog, String entityType) {
        structureTestLog.setEntitySubType(entityType);
        return structureTestLog;
    }

    @Override
    public String getWpsIds(Long orgId, Long projectId, Long weldEntityId) {
        return null;
    }


    @Override
    public Page<? extends BaseConstructionLog> getTestResult(Long orgId, Long projectId, Long entityId, String processNameEn, Pageable pageable) {

        return structureConstructionLogRepository.findByEntityId(entityId, pageable);

    }

}
