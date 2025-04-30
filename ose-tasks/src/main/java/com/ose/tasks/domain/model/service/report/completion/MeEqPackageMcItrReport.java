package com.ose.tasks.domain.model.service.report.completion;

import com.ose.report.api.completion.ElCableMcItrReportFeignAPI;
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
public class MeEqPackageMcItrReport implements McItrReportInterface{

    private final BpmCheckSheetRepository checkSheetRepository;

    private final ElCableMcItrReportFeignAPI elCableMcItrReportFeignAPI;

    private final ProjectNodeRepository projectNodeRepository;

    private final EntityTypeProcessItrTemplateRelationRepository etpitrRepository;

    private final ItrRepository itrRepository;

    private final SubSystemEntityRepository subSystemEntityRepository;

    private final ProcessInterface processService;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private static Map<String, String> subTypeMap = new HashMap<>();
    {{
        subTypeMap.put("LV_CABLE_CP", "LV POWER CABLE PACKAGE");
        subTypeMap.put("CONTROL_CABLE_CP", "CONTROL CABLE PACKAGE");
        subTypeMap.put("CAT_CABLE_CP", "CAT CABLE PACKAGE");
        subTypeMap.put("IN_CABLE_CP", "INSTRUMENT CABLE PACKAGE");
        subTypeMap.put("COAXIAL_CABLE_CP", "COAXIAL CABLE PACKAGE");
        subTypeMap.put("FIBER_OPTIC_CP", "FIBER OPTIC PACKAGE");
        subTypeMap.put("HVAC_HP", "HVAC COMPONENT PACKAGE");
        subTypeMap.put("FURNITURE_EP", "FURNITURE PACKAGE");

    }}



    public MeEqPackageMcItrReport(BpmCheckSheetRepository checkSheetRepository,
                                  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") ElCableMcItrReportFeignAPI elCableMcItrReportFeignAPI,
                                  ProjectNodeRepository projectNodeRepository,
                                  EntityTypeProcessItrTemplateRelationRepository etpitrRepository,
                                  SubSystemEntityRepository subSystemEntityRepository,
                                  ProcessInterface processService,
                                  ItrRepository itrRepository,
                                  HierarchyNodeRelationRepository hierarchyNodeRelationRepository) {
        this.checkSheetRepository = checkSheetRepository;
        this.elCableMcItrReportFeignAPI = elCableMcItrReportFeignAPI;
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



        if("MECH_EQ_PACKAGE".equals(pn.getEntityType()) ||
            "HVAC_EQ_PACKAGE".equals(pn.getEntityType()) ||
            "ARCH_EQ_PACKAGE".equals(pn.getEntityType())) {
//            type = subTypeMap.get(eqPackageEntityBase.getEntitySubType());
        } else if("EL_CABLE".equals(pn.getEntityType())) {
//            CableEntity cableEntity = cableEntityRepository.findByProjectIdAndIdAndDeletedIsFalse(projectId, pn.getEntityId());
//            type = cableEntity.getNo() + " " + subTypeMap.get(cableEntity.getEntitySubType());
//            entityType = cableEntity == null? null:cableEntity.getEntityType();

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

//        List<EntityTypeProcessItrTemplateRelation> etpitrs = etpitrRepository.
//            findByProjectIdAndEntitySubTypeIdAndProcessIdAndStatus(projectId, entitySubTypeId, processId, EntityStatus.ACTIVE);
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
                parameters.put("systemNo", systemNo.replaceAll("HS","").replaceAll("TS",""));
                parameters.put("subSystemName", subSystemName);
                parameters.put("subSystemNo", subSystemNo);
                parameters.put("tagNo", pn.getNo());
                parameters.put("no", pn.getNo());
                parameters.put("tagType", pn.getEntitySubType());
                parameters.put("sheetCode", checkSheet.getNo());
                parameters.put("sheetDesc", checkSheet.getDescription());
                parameters.put("manufacturer", manufacturer);
                parameters.put("location", location);
//                String tmpType = type;
//                type = subTypeMap.get(type);
//                if(type == null) type = tmpType;
                parameters.put("type", type);
                parameters.put("sn", sn);
                parameters.put("discipline", pn.getDiscipline());
                parameters.put("itrTemplate", "ITR " + checkSheet.getNo());
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
                elCableMcItrReportFeignAPI.generateMcItrReport(orgId, projectId, mcItrDTO).getData();

            reportHistories.add(reportHistory);
        }


        return reportHistories;
    }


}
