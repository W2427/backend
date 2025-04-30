package com.ose.tasks.domain.model.service.delegate.completion;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.report.api.completion.ElEqMcItrReportFeignAPI;
import com.ose.report.dto.completion.EqMcItrDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.trident.BpmCheckSheetRepository;
import com.ose.tasks.domain.model.repository.trident.EntityTypeProcessItrTemplateRelationRepository;
import com.ose.tasks.domain.model.repository.trident.ItrRepository;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.domain.model.service.delegate.BaseBpmTaskDelegate;
import com.ose.tasks.domain.model.service.delegate.BaseBpmTaskInterfaceDelegate;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.completion.ReportInfoDTO;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.trident.CheckSheet;
import com.ose.tasks.entity.trident.EntityTypeProcessItrTemplateRelation;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.tasks.vo.qc.InspectType;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外检申请 代理服务。
 */
@Component
public class ElEqMcItrDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 上传文件的临时路径
    @Value("${application.files.protected}")
    private String protectedDir;

    private final BpmCheckSheetRepository checkSheetRepository;

    private final ElEqMcItrReportFeignAPI elEqMcItrReportFeignAPI;

    private final ProjectNodeRepository projectNodeRepository;

    private final EntityTypeProcessItrTemplateRelationRepository etpitrRepository;

    private final ItrRepository itrRepository;

    private final ProcessInterface processService;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private final QCReportRepository qcReportRepository;

    private final BpmEntityDocsMaterialsRepository bpmEntityDocsMaterialsRepository;

    private final static Logger logger = LoggerFactory.getLogger(ElEqMcItrDelegate.class);

    /**
     * 构造方法。
     */
    @Autowired
    public ElEqMcItrDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                             BpmRuTaskRepository ruTaskRepository,
                             StringRedisTemplate stringRedisTemplate,
                             BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                             BpmCheckSheetRepository checkSheetRepository,
                             @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") ElEqMcItrReportFeignAPI elEqMcItrReportFeignAPI,
                             ProjectNodeRepository projectNodeRepository,
                             EntityTypeProcessItrTemplateRelationRepository etpitrRepository,
                             ItrRepository itrRepository, ProcessInterface processService,
                             HierarchyNodeRelationRepository hierarchyNodeRelationRepository, QCReportRepository qcReportRepository, BpmEntityDocsMaterialsRepository bpmEntityDocsMaterialsRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.checkSheetRepository = checkSheetRepository;
        this.elEqMcItrReportFeignAPI = elEqMcItrReportFeignAPI;
        this.projectNodeRepository = projectNodeRepository;
        this.etpitrRepository = etpitrRepository;
        this.itrRepository = itrRepository;
        this.processService = processService;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.qcReportRepository = qcReportRepository;
        this.bpmEntityDocsMaterialsRepository = bpmEntityDocsMaterialsRepository;
    }

    //准备批处理执行任务接口,设置值到 TODODTO中
    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        Long projectId = execResult.getProjectId();
        Long orgId = execResult.getOrgId();
        OperatorDTO operatorDTO = contextDTO.getOperator();

        BpmActivityInstanceBase actInst = execResult.getActInst();
        Long entityId = actInst.getEntityId();
        String entityNo = actInst.getEntityNo();
        ProjectNode pn = projectNodeRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, entityId).orElse(null);
        if (pn == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("THERE IS NO SUCH PROJECT NODE " + actInst.getEntityNo());
            return execResult;
        }
//        List<HierarchyNodeRelation> hnrs = hierarchyNodeRelationRepository.findNodeByProjectIdAndEntityId(projectId, eitEq.getId());
//        String systemNo = null;
//        String systemName = null;
//        String subSystemNo = null;
//        String subSystemName = null;
//        for (HierarchyNodeRelation hnr : hnrs) {
//            if ("SYSTEM".equals(hnr.getAncestorEntityType())) {
//                ProjectNode sysPn = projectNodeRepository.findById(hnr.getNodeAncestorId()).orElse(null);
//                if (sysPn != null) {
//                    systemName = sysPn.getDisplayName();
//                    systemNo = sysPn.getNo();
//                }
//            } else if ("SUB_SYSTEM".equals(hnr.getAncestorEntityType())) {
//                ProjectNode subSysPn = projectNodeRepository.findById(hnr.getNodeAncestorId()).orElse(null);
//                if (subSysPn != null) {
//                    subSystemName = subSysPn.getDisplayName();
//                    subSystemNo = subSysPn.getNo();
//                }
//            }
//        }

        List<ReportHistory> reportHistories = new ArrayList<>();
        Long entitySubTypeId = actInst.getEntitySubTypeId();
        Long processId = actInst.getProcessId();
        BpmProcess bpmProcess = processService.getBpmProcess(processId);
        String projectPhase = bpmProcess.getProcessStage().getNameEn();

        List<EntityTypeProcessItrTemplateRelation> etpitrs = etpitrRepository.
            findByProjectIdAndEntitySubTypeIdAndProcessIdAndStatus(projectId, entitySubTypeId, processId, EntityStatus.ACTIVE);
        String barCode = null;
        for (EntityTypeProcessItrTemplateRelation etpitr : etpitrs) {
            EqMcItrDTO eqMcItrDTO = new EqMcItrDTO();
            CheckSheet checkSheet = checkSheetRepository.findById(etpitr.getItrTemplateId()).orElse(null);
            if(checkSheet != null) {
                Map<String, Object> parameters = new HashMap<>();
//                Itr itr = itrRepository.findByProjectIdAndEntityIdAndItrCheckTemplate(projectId, eitEq.getId(), checkSheet.getNo());
//                barCode = (itr == null || itr.getTridentItrId() == null) ? "A00000" : "A" + itr.getTridentItrId();
//                eqMcItrDTO.setTemplateNo(checkSheet.getNo());
//                eqMcItrDTO.setTemplateId(checkSheet.getId());
//
//                parameters.put("barCode", barCode);
//                String barCodeStr = barCode.replace("", "  ");
//                parameters.put("barCodeStr", barCodeStr);
//                parameters.put("projectPhase", projectPhase);
//                parameters.put("systemName", systemName);
//                parameters.put("systemNo", systemNo);
//                parameters.put("subSystemName", subSystemName);
//                parameters.put("subSystemNo", subSystemNo);
//                parameters.put("tagNo", pn.getNo());
//                parameters.put("tagType", pn.getEntitySubType());
//                parameters.put("sheetCode", checkSheet.getNo());
//                parameters.put("sheetDesc", checkSheet.getDescription());
//                parameters.put("manufacturer", eitEq.getManufacturer());
//                parameters.put("location", eitEq.getEquipmentLocationNo());
//                parameters.put("type", eitEq.getType());
//                parameters.put("sn", eitEq.getSerialNo());
                parameters.put("discipline", "ELECTRICAL");
                parameters.put("itrTemplate", "ITR " + checkSheet.getNo());
                eqMcItrDTO.setParameters(parameters);
                ReportHistory reportHistory =
                    elEqMcItrReportFeignAPI.generateElEqMcItrReport(orgId, projectId, eqMcItrDTO).getData();
                reportHistories.add(reportHistory);


            }
        }
        if(execResult.getVariables() == null) execResult.setVariables(new HashMap<>());
        execResult.getVariables().put("itrs", reportHistories);
        Map<String, ReportInfoDTO> reportInfoMap = new HashMap<>();

        reportHistories.forEach(reportHistory -> {
            String bc = reportHistory.getReportQrCode();
            ReportInfoDTO reportInfoDTO = reportInfoMap.computeIfAbsent(bc, k-> new ReportInfoDTO());
            reportInfoDTO.setBarCode(bc);
            if("PDF".equalsIgnoreCase(reportHistory.getFileType())) {
                reportInfoDTO.setPdfFileId(reportHistory.getFileId());
                reportInfoDTO.setPdfFilePath(reportHistory.getFilePath());
                reportInfoDTO.setFileType("PDF");
            }
        });

        for (ReportHistory reportHistory : reportHistories) {
            QCReport qcReport = new QCReport();
            qcReport.setOrgId(orgId);
            qcReport.setProjectId(projectId);
            qcReport.setReportStatus(ReportStatus.INIT);
            qcReport.setInspectType(InspectType.EXTERNAL);
            qcReport.setOperatorName(operatorDTO.getName());
            qcReport.setOperator(operatorDTO.getId());
            qcReport.setSeriesNo(barCode);
            qcReport.setJsonEntityNos(new ArrayList<String>(){{add(execResult.getActInst().getEntityNo());}});
            qcReport.setScheduleId(0L);
            qcReport.setProcess(execResult.getActInst().getProcess());
            qcReport.setProcessStage(execResult.getActInst().getProcessStage());
            qcReport.setQrcode(barCode);
            qcReport.setJsonActInstIds(new ArrayList<Long>(){{execResult.getActInst().getId();}});
//            qcReport.setModuleName(subSystemNo);
            qcReport.setCreatedAt();
            qcReport.setStatus(EntityStatus.ACTIVE);
            qcReport.setLastModifiedAt();
            qcReport.setPdfReportFileId(reportInfoMap.get(barCode).getPdfFileId());
            qcReport.setExcelReportFileId(reportInfoMap.get(barCode).getXlsFileId());

            qcReportRepository.save(qcReport);

            BpmEntityDocsMaterials bedm = new BpmEntityDocsMaterials();
            bedm.setType(ActInstDocType.EXTERNAL_INSPECTION);
            List<ActReportDTO> docs = new ArrayList<>();
            if(reportInfoMap.get(barCode).getPdfFileId() != null) {
                ActReportDTO actReportDTO = new ActReportDTO();
                actReportDTO.setFileId(reportInfoMap.get(barCode).getPdfFileId());
                actReportDTO.setFilePath(reportInfoMap.get(barCode).getPdfFilePath());
                actReportDTO.setReportQrCode(barCode);
                actReportDTO.setReportNo(barCode);
                actReportDTO.setReportType("MC_ITR");
                actReportDTO.setDrawingName(barCode);
                docs.add(actReportDTO);
            }
            if(reportInfoMap.get(barCode).getXlsFileId() != null) {
                ActReportDTO actReportDTO = new ActReportDTO();
                actReportDTO.setFileId(reportInfoMap.get(barCode).getXlsFileId());
                actReportDTO.setFilePath(reportInfoMap.get(barCode).getXlsFilePath());
                actReportDTO.setReportQrCode(barCode);
                docs.add(actReportDTO);
            }
            bedm.setJsonDocs(docs);
            bedm.setEntityId(entityId);
            bedm.setEntityNo(entityNo);
            bedm.setProcessId(processId);
            bedm.setActInstanceId(actInst.getId());
            bedm.setOperator(operatorDTO.getId());
            bedm.setProjectId(projectId);
            bedm.setCreatedAt();
            bedm.setStatus(EntityStatus.ACTIVE);
            bedm.setLastModifiedAt();
            bpmEntityDocsMaterialsRepository.save(bedm);
        }
        return execResult;
    }


}
