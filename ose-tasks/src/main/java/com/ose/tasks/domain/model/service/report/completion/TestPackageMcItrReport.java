package com.ose.tasks.domain.model.service.report.completion;

import com.ose.report.api.completion.TestPackageMcItrReportFeignAPI;
import com.ose.report.dto.completion.McItrDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.trident.BpmCheckSheetRepository;
import com.ose.tasks.domain.model.repository.trident.EntityTypeProcessItrTemplateRelationRepository;
import com.ose.tasks.domain.model.repository.trident.ItrRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.SubSystemEntityRepository;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.domain.model.service.delegate.completion.classForList.BaseClassForListInterface;
import com.ose.tasks.entity.HierarchyNodeRelation;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.trident.CheckSheet;
import com.ose.tasks.entity.trident.EntityTypeProcessItrTemplateRelation;
import com.ose.tasks.entity.trident.Itr;
import com.ose.tasks.entity.wbs.entity.LineEntity;
import com.ose.tasks.entity.wbs.entity.SubSystemEntityBase;
import com.ose.util.SpringContextUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestPackageMcItrReport implements McItrReportInterface{

    private final BpmCheckSheetRepository checkSheetRepository;

    private final TestPackageMcItrReportFeignAPI testPackageMcItrReportFeignAPI;

    private final ProjectNodeRepository projectNodeRepository;

    private final EntityTypeProcessItrTemplateRelationRepository etpitrRepository;

    private final ItrRepository itrRepository;

    private final SubSystemEntityRepository subSystemEntityRepository;

    private final ProcessInterface processService;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private static Map<String, String> subTypeMap = new HashMap<>();
    {{
        subTypeMap.put("TEST_PACKAGE", "TOPSIDE TEST PACKAGE");
        subTypeMap.put("TEST_PACKAGE_HULL", "HULL TEST PACKAGE");

    }
    }



    public TestPackageMcItrReport(BpmCheckSheetRepository checkSheetRepository,
                                  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") TestPackageMcItrReportFeignAPI testPackageMcItrReportFeignAPI,
                                  ProjectNodeRepository projectNodeRepository,
                                  EntityTypeProcessItrTemplateRelationRepository etpitrRepository,
                                  ItrRepository itrRepository,
                                  SubSystemEntityRepository subSystemEntityRepository, ProcessInterface processService,
                                  HierarchyNodeRelationRepository hierarchyNodeRelationRepository) {
        this.checkSheetRepository = checkSheetRepository;
        this.testPackageMcItrReportFeignAPI = testPackageMcItrReportFeignAPI;
        this.projectNodeRepository = projectNodeRepository;
        this.etpitrRepository = etpitrRepository;
        this.itrRepository = itrRepository;
        this.subSystemEntityRepository = subSystemEntityRepository;
        this.processService = processService;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
    }

    @Override
    public List<ReportHistory> generateReport(Long orgId, Long projectId,
                                              Long entityId,
                                              Long entitySubTypeId,
                                              Long processId) {
        ProjectNode pn = projectNodeRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, entityId).orElse(null);
        if (pn == null) {
            return null;
        }

        String manufacturer = "";
        String location = "";
        String type = "";
        String sn = "";
        String entityType = "";
//        TestPackageEntityBase tpe = testPackageEntityRepository.findByProjectIdAndIdAndDeletedIsFalse(projectId, pn.getEntityId());

//        List<LineEntity> lines = lineEntityRepository.findByProjectIdAndParentId(projectId, tpe.getId());
        LineEntity line = new LineEntity();
//        if(!CollectionUtils.isEmpty(lines)) {
//            line = lines.get(0);
//        }

        if("TEST_PACKAGE".equals(pn.getEntityType())) {
//            TestPackageEntityBase testPackage = testPackageEntityRepository.findByProjectIdAndIdAndDeletedIsFalse(projectId, pn.getEntityId());
//            entityType = testPackage == null? null:testPackage.getEntityType();
//            type = subTypeMap.get(testPackage.getEntitySubType());
        } else {
            return null;
        }

        BpmProcess bpmProcess = processService.getBpmProcess(processId);
        List<HierarchyNodeRelation> hnrs = hierarchyNodeRelationRepository.findNodeByProjectIdAndEntityId(projectId, entityId);
        String systemNo = null;
        String systemName = null;
        String subSystemNo = null;
        String subSystemName = null;
        for (HierarchyNodeRelation hnr : hnrs) {
            if ("SYSTEM".equals(hnr.getAncestorEntityType())) {
                ProjectNode sysPn = projectNodeRepository.findById(hnr.getNodeAncestorId()).orElse(null);
                if (sysPn != null) {
                    systemName = sysPn.getDisplayName();
                    systemNo = sysPn.getNo();
                }
            } else if ("SUB_SYSTEM".equals(hnr.getAncestorEntityType())) {
                SubSystemEntityBase subSysEn = subSystemEntityRepository.findById(hnr.getAncestorEntityId()).orElse(null);
                if (subSysEn != null) {
                    subSystemName = subSysEn.getDescription();
                    subSystemNo = subSysEn.getNo();
                }
            }
        }

        List<ReportHistory> reportHistories = new ArrayList<>();
        String projectPhase = bpmProcess.getProcessStage().getNameEn();

        List<EntityTypeProcessItrTemplateRelation> etpitrs = etpitrRepository.
            findByProjectIdAndEntitySubTypeIdAndStatusOrderBySeq(projectId, entitySubTypeId, EntityStatus.ACTIVE);
        String barCode = null;
        for (EntityTypeProcessItrTemplateRelation etpitr : etpitrs) {
//            if(etpitr.getItrTemplateId().equals(1635743623808872960L)) continue;//TODO
            McItrDTO mcItrDTO = new McItrDTO();
            CheckSheet checkSheet = checkSheetRepository.findById(etpitr.getItrTemplateId()).orElse(null);
            if(checkSheet != null) {
                Map<String, Object> parameters = new HashMap<>();
                Itr itr = itrRepository.findByProjectIdAndEntityIdAndItrCheckTemplate(projectId, entityId, checkSheet.getNo());
                barCode = (itr == null || itr.getTridentItrId() == null) ? "A000000" : "A" + StringUtils.padLeft(itr.getTridentItrId().toString(),6,'0');
                mcItrDTO.setTemplateNo(checkSheet.getNo());
                mcItrDTO.setTemplateId(checkSheet.getId());
                parameters.put("barCode", barCode);
                String barCodeStr = barCode.replace("", "  ");
                parameters.put("barCodeStr", barCodeStr);
                parameters.put("projectPhase", projectPhase);
                parameters.put("systemName", systemName);
                parameters.put("systemNo", systemNo.replaceAll("HS","").replaceAll("TS","") + " " + systemName);
                parameters.put("subSystemName", subSystemName);
                parameters.put("subSystemNo", subSystemNo);
                parameters.put("tagNo", pn.getNo());
                parameters.put("no", pn.getNo());
                parameters.put("tagType", pn.getEntitySubType());
                parameters.put("sheetCode", checkSheet.getNo());
                parameters.put("sheetDesc", checkSheet.getDescription());
                parameters.put("manufacturer", manufacturer);
                parameters.put("location", location);
                parameters.put("type", type);
                parameters.put("sn", sn);
                parameters.put("discipline", bpmProcess.getFuncPart());
                parameters.put("itrTemplate", "ITR " + checkSheet.getNo());
                String designPressure = line.getDesignPressure() == null ? "":line.getDesignPressure().toString();
                String testPresure = line.getTestPressure() == null ? "":line.getTestPressure().toString();
                String testMedium = line.getPressureTestMedium() == null ? "":line.getPressureTestMedium();
                String lineSpec = line.getPipeClass() == null ? "" : line.getPipeClass();
                parameters.put("testPressure", testPresure);
                parameters.put("designPressure", designPressure);
                parameters.put("testMedium", testMedium);
                parameters.put("lineSpec", lineSpec);
                mcItrDTO.setParameters(parameters);
            }
            if(!StringUtils.isEmpty(etpitr.getClazzForList())) {
                List list = new ArrayList();
                try {
                    Class clazz = Class.forName(etpitr.getClazzForList());
                    BaseClassForListInterface delegate = (BaseClassForListInterface) SpringContextUtils.getBean(clazz);
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", orgId);
                    data.put("projectId", projectId);
                    list = delegate.getItemLists(projectId, entityId);
                    mcItrDTO.setItems(list);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }

            }
            ReportHistory reportHistory =
                testPackageMcItrReportFeignAPI.generateMcItrReport(orgId, projectId, mcItrDTO).getData();

            reportHistories.add(reportHistory);
        }


        return reportHistories;
    }


}
