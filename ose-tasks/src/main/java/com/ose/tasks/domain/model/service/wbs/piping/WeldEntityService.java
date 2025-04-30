package com.ose.tasks.domain.model.service.wbs.piping;

import com.alibaba.excel.EasyExcel;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmDeliveryEntityRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ISOEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.WeldEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.WeldHierarchyInfoEntityRepository;
import com.ose.tasks.domain.model.repository.wps.WelderCertificateRepository;
import com.ose.tasks.domain.model.repository.wps.WelderRepository;
import com.ose.tasks.domain.model.repository.wps.WpsDetailRepository;
import com.ose.tasks.domain.model.repository.wps.WpsRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WpsSimpleRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.piping.WeldEntityInterface;
import com.ose.tasks.dto.WpsImportDTO;
import com.ose.tasks.dto.WpsImportResultDTO;
import com.ose.tasks.dto.material.NPSThicknessSearchDTO;
import com.ose.tasks.dto.wbs.WeldEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.WeldWelderWPSDTO;
import com.ose.tasks.dto.wps.WpsCriteriaDTO;
import com.ose.tasks.dto.wps.WpsMatchingDTO;
import com.ose.tasks.dto.wps.simple.WpsSimpleSearchDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.material.NPSch;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.WeldEntity;
import com.ose.tasks.entity.wbs.entity.WeldEntityBase;
import com.ose.tasks.entity.wbs.entity.WeldHierarchyInfoEntity;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.entity.wps.Welder;
import com.ose.tasks.entity.wps.WelderCertificate;
import com.ose.tasks.entity.wps.Wps;
import com.ose.tasks.entity.wps.WpsDetail;
import com.ose.tasks.entity.wps.simple.WpsSimplified;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.qc.WeldType;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ose.util.StringUtils.trim;
import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;


@Component
public class WeldEntityService implements WeldEntityInterface {


    private final WeldEntityRepository weldEntityRepository;


    private final WelderRepository welderRepository;


    private final WelderCertificateRepository welderCertificateRepository;


    private final WpsDetailRepository wpsDetailRepository;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final WeldHierarchyInfoEntityRepository weldHierarchyInfoEntityRepository;


    private final EntitySubTypeRuleRepository entityCategoryRuleRepository;


    private ProjectNodeRepository projectNodeRepository;

    private WpsSimpleRepository wpsSimpleRepository;

    private final WpsRepository wpsRepository;

    private Set<EntitySubTypeRule> weldEntityCategoryRules;

    private final WBSEntryRepository wbsEntryRepository;


    private HierarchyInterface hierarchyService;


    private final PlanInterface planService;

    private final SubDrawingRepository subDrawingRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private static final Pattern THICKNESS = Pattern.compile(

        "(S-[\\d]{1,3}s?)|(X{1,3}S)|(STD)|(N/A)|(\\d+(\\.\\d+)?('|\"|ft|in|mm|cm|m).*)|(\\d+(\\.\\d+)?(mm|cm|m)\\((\\d+)/(\\d+)('|\"|ft|in)\\)\\s*THCK)|(\\d+(\\.\\d+)?)",
        Pattern.CASE_INSENSITIVE
    );

    private static final String COMMA = ",";

    @Value("${application.files.temporary}")
    private String temporaryDir;


    private static final int DATA_START_ROW = 3;

    private static final int TEMPLATE_ROW_COUNT = 20;

    private static final String YES = "Y";

    private static final String NO = "N";

    private final ISOEntityRepository isoEntityRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public WeldEntityService(
        WeldEntityRepository weldEntityRepository,
        WeldHierarchyInfoEntityRepository weldHierarchyInfoEntityRepository,
        ProjectNodeRepository projectNodeRepository,
        EntitySubTypeRuleRepository entityCategoryRuleRepository,
        WpsRepository wpsRepository,
        HierarchyInterface hierarchyService,
        WelderRepository welderRepository,
        WelderCertificateRepository welderCertificateRepository,
        WpsDetailRepository wpsDetailRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        WBSEntryRepository wbsEntryRepository, PlanInterface planService,
        SubDrawingRepository subDrawingRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        WpsSimpleRepository wpsSimpleRepository,
        ISOEntityRepository isoEntityRepository) {
        this.weldEntityRepository = weldEntityRepository;
        this.weldHierarchyInfoEntityRepository = weldHierarchyInfoEntityRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.entityCategoryRuleRepository = entityCategoryRuleRepository;
        this.wpsRepository = wpsRepository;
        this.hierarchyService = hierarchyService;
        this.wbsEntryRepository = wbsEntryRepository;
        this.planService = planService;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.welderRepository = welderRepository;
        this.welderCertificateRepository = welderCertificateRepository;
        this.wpsDetailRepository = wpsDetailRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.wpsSimpleRepository = wpsSimpleRepository;
        this.isoEntityRepository = isoEntityRepository;
    }

    /**
     * 查询 WBS 实体。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页参数
     * @return WBS 实体分页数据
     */
    @Override
    public Page<? extends WeldEntityBase> search(
        Long orgId,
        Long projectId,
        WeldEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.PIPING)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findWeldIdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "WELD_JOINT",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return weldHierarchyInfoEntityRepository.search(orgId,
                        projectId,
                        criteriaDTO,
                        pageDTO,
                        WeldHierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return weldHierarchyInfoEntityRepository.search(orgId,
                    projectId,
                    criteriaDTO,
                    pageDTO,
                    WeldHierarchyInfoEntity.class);
            }
        } else {
            return weldHierarchyInfoEntityRepository.search(orgId,
                projectId,
                criteriaDTO,
                pageDTO,
                WeldHierarchyInfoEntity.class);
        }
    }

    /**
     * 取得 WBS 实体详细信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  WBS 实体 ID
     * @return WBS 实体详细信息
     */
    @Override
    public WeldEntityBase get(Long orgId, Long projectId, Long entityId) {

        WeldEntityBase entity = weldHierarchyInfoEntityRepository
            .findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(
                entityId,
                orgId,
                projectId
            )
            .orElse(null);

        if (entity == null) {
            throw new NotFoundError();
        }

        return entity;
    }

    /**
     * 删除 WBS 实体。
     *
     * @param operator 操作者信息
     * @param orgId    组织 ID
     * @param project  项目
     * @param entityId WBS 实体 ID
     */
    @Override
    @Transactional
    public void delete(
        OperatorDTO operator,
        Long orgId,
        Project project,
        Long entityId
    ) {

        WeldEntityBase entity = get(orgId, project.getId(), entityId);

        WeldEntity weldEntity = BeanUtils.copyProperties(entity, new WeldEntity());

        List<BpmActivityInstanceBase> baisList = bpmActivityInstanceRepository.findByProjectIdAndEntityId(project.getId(), entity.getId());
        if (baisList.size() > 0) {
            for (BpmActivityInstanceBase bai : baisList) {
                BpmActivityInstanceState bais = bpmActivityInstanceStateRepository.findByBaiId(bai.getId());
                if (bais != null) {
                    bais.setSuspensionState(SuspensionState.SUSPEND);
                    bpmActivityInstanceStateRepository.save(bais);
                }
                List<BpmRuTask> brtList = bpmRuTaskRepository.findByActInstId(bai.getId());
                if (brtList.size() > 0) {
                    for (BpmRuTask brt : brtList) {
//                        BpmRuTask byTaskId = bpmRuTaskRepository.findByTaskId(brt.getTaskId());
//                        if (byTaskId != null) {
//                            byTaskId.setSuspensionState(0);
//                            bpmRuTaskRepository.save(byTaskId);
//                        }
                        brt.setSuspensionState(0);
                        bpmRuTaskRepository.save(brt);
                    }
                    bpmRuTaskRepository.saveAll(brtList);
                }
                bai.setStatus(DELETED);
                bpmActivityInstanceRepository.save(bai);
            }
        }

//        List<WBSEntry> wbsEntryList = wbsEntryRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(project.getId(), entity.getId());
//        if (wbsEntryList.size() > 0) {
//            for (WBSEntry wbsEntry : wbsEntryList) {
//                WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
//                if (wbsEntryState != null) {
//                    wbsEntryState.setActive(false);
//                    wbsEntryStateRepository.save(wbsEntryState);
//                }
//                WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntry.getId());
//                if (wbsEntryBlob != null) {
//                    wbsEntryBlob.setActive(false);
//                    wbsEntryBlobRepository.save(wbsEntryBlob);
//                }
//                List<WBSEntryPlainRelation> wbsEntryPlainRelations = wbsEntryPlainRelationRepository.findByWbsEntryId(wbsEntry.getId());
//                if (wbsEntryPlainRelations.size() > 0) {
//                    wbsEntryPlainRelationRepository.deleteAll(wbsEntryPlainRelations);
//                }
//                List<WBSEntryRelation> wbsEntryRelation1 = wbsEntryRelationRepository.findByProjectIdAndPredecessorId(project.getId(), wbsEntry.getGuid());
//                List<WBSEntryRelation> wbsEntryRelation2 = wbsEntryRelationRepository.findByProjectIdAndSuccessorId(project.getId(), wbsEntry.getGuid());
//                if (wbsEntryRelation1.size() > 0) {
//                    for (WBSEntryRelation wbsEntryRelation : wbsEntryRelation1) {
//                        wbsEntryRelation.setStatus(DELETED);
//                        wbsEntryRelation.setDeleted(true);
//                        wbsEntryRelation.setDeletedAt();
//                        wbsEntryRelation.setDeletedBy(operator.getId());
//                        wbsEntryRelationRepository.save(wbsEntryRelation);
//                    }
//                }
//                if (wbsEntryRelation2.size() > 0) {
//                    for (WBSEntryRelation wbsEntryRelation : wbsEntryRelation2) {
//                        wbsEntryRelation.setStatus(DELETED);
//                        wbsEntryRelation.setDeleted(true);
//                        wbsEntryRelation.setDeletedAt();
//                        wbsEntryRelation.setDeletedBy(operator.getId());
//                        wbsEntryRelationRepository.save(wbsEntryRelation);
//                    }
//                }
//                wbsEntry.setActive(false);
//                wbsEntry.setDeletedAt();
//                wbsEntry.setDeletedBy(operator.getId());
//                wbsEntry.setStatus(DELETED);
//                wbsEntry.setDeleted(true);
//                wbsEntryRepository.save(wbsEntry);
//            }
//        }
        List<WBSEntry> wbsEntryList = wbsEntryRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(project.getId(), entity.getId());
        if (wbsEntryList.size() > 0) {
            for (WBSEntry wbsEntry : wbsEntryList) {
                WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
                if (wbsEntryState != null) {
                    wbsEntryStateRepository.delete(wbsEntryState);
                }
                WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntry.getId());
                if (wbsEntryBlob != null) {
                    wbsEntryBlobRepository.delete(wbsEntryBlob);
                }
                List<WBSEntryPlainRelation> wbsEntryPlainRelations = wbsEntryPlainRelationRepository.findByWbsEntryId(wbsEntry.getId());
                if (wbsEntryPlainRelations.size() > 0) {
                    wbsEntryPlainRelationRepository.deleteAll(wbsEntryPlainRelations);
                }
                List<WBSEntryRelation> wbsEntryRelation1 = wbsEntryRelationRepository.findByProjectIdAndPredecessorId(project.getId(), wbsEntry.getGuid());
                List<WBSEntryRelation> wbsEntryRelation2 = wbsEntryRelationRepository.findByProjectIdAndSuccessorId(project.getId(), wbsEntry.getGuid());
                if (wbsEntryRelation1.size() > 0) {
                    wbsEntryRelationRepository.deleteAll(wbsEntryRelation1);
                }
                if (wbsEntryRelation2.size() > 0) {
                    wbsEntryRelationRepository.deleteAll(wbsEntryRelation2);
                }
                wbsEntryRepository.delete(wbsEntry);
            }
        }


//        Boolean isDeletable = projectNodeRepository.existsByProjectIdAndEntityIdAndIsDeletableIsTrue(project.getId(), entity.getId());
//        if(isDeletable == null || isDeletable)  {
        weldEntity.setDeletedBy(operator.getId());
        weldEntity.setDeletedAt();
        weldEntity.setStatus(DELETED);
        weldEntity.setDeleted(true);

        hierarchyService.delete(operator, project, orgId, entityId);

        weldEntityRepository.save(weldEntity);
//        } else {
//            weldEntity.setCancelled(true);
//            weldEntity.setLastModifiedAt();
//            weldEntity.setStatus(EntityStatus.CANCEL);
//            weldEntity.setLastModifiedBy(operator.getId());
//
//            hierarchyService.cancel(operator, project, orgId, entityId);
//
//        }


//        if(isDeletable == null) return;
//        planService.updateStatusOfWBSOfDeletedEntity(project.getId(), entity.getWbsEntityType(), entityId, operator.getId(), isDeletable);

    }

    /**
     * 插入 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    Weld 实体
     */
    @Override
    public void insert(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        WeldEntityBase entity
    ) {
        WeldEntity weldEntity = BeanUtils.copyProperties(entity, new WeldEntity());

        setEntityTypeByRules(orgId, projectId, weldEntity);


        weldEntity.setCreatedBy(operator.getId());
        weldEntity.setCreatedAt();
        weldEntity.setLastModifiedBy(operator.getId());
        weldEntity.setLastModifiedAt();
        weldEntity.setProjectId(projectId);
        weldEntity.setStatus(ACTIVE);
        weldEntity.setDeleted(false);
        weldEntity.setVersion(weldEntity.getLastModifiedAt().getTime());
        weldEntityRepository.save(weldEntity);


        ProjectNode projectNode = new ProjectNode();

        projectNode.setNo(entity.getNo());
        projectNode.setDisplayName(entity.getDisplayName());
        projectNode.setCompanyId(entity.getCompanyId());
        projectNode.setEntityId(entity.getId());
        projectNode.setEntityType(WBSEntityType.WELD_JOINT.name());
        projectNode.setEntitySubType(weldEntity.getEntitySubType());
        projectNode.setEntityBusinessType(weldEntity.getEntityBusinessType());
//        projectNode.setNodeType(HierarchyNodeType.ENTITY);
        projectNode.setOrgId(orgId);
        projectNode.setProjectId(entity.getProjectId());
        projectNode.setRemarks(entity.getRemarks());
        projectNode.setCreatedAt();
        projectNode.setCreatedBy(operator.getId());
        projectNode.setLastModifiedAt();
        projectNode.setLastModifiedBy(operator.getId());
        projectNode.setStatus(ACTIVE);
        projectNode.setDeleted(false);
        projectNode.setVersion(entity.getLastModifiedAt().getTime());
        projectNode.setDwgShtNo(entity.getSheetNo());
        projectNode.setWorkLoad(entity.getNps());
        projectNode.setDiscipline(DisciplineCode.PIPING.name());


        projectNodeRepository.save(projectNode);
    }

    /**
     * 更新 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    Weld 实体
     */
    @Override
    public WeldEntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        WeldEntityBase entity
    ) {

        WeldEntity weldEntity = BeanUtils.copyProperties(entity, new WeldEntity());

        setEntityTypeByRules(orgId, projectId, weldEntity);

        weldEntity.setLastModifiedBy(operator.getId());
        weldEntity.setLastModifiedAt();
        weldEntity.setProjectId(projectId);
        weldEntity.setStatus(ACTIVE);
        weldEntity.setDeleted(false);
        weldEntity.setVersion(weldEntity.getLastModifiedAt().getTime());

        weldEntityRepository.save(weldEntity);

        ProjectNode projectNode = projectNodeRepository.
            findByProjectIdAndEntityIdAndDeleted(projectId, weldEntity.getId(), false).
            orElse(null);
        if (projectNode != null) {
            if (projectNode.getEntitySubType() != null && !projectNode.getEntitySubType().equalsIgnoreCase(weldEntity.getEntitySubType())) {
                projectNode.setEntitySubType(weldEntity.getEntitySubType());
                projectNodeRepository.save(projectNode);
            }
        }
        return entity;
    }

    /**
     * 判断 WBS 实体是否已经存在。
     *
     * @param projectId 项目 ID
     * @param entityNO  WBS 实体号
     * @return WBS 实体是否存在  存在 ：true；不存在：false；
     */
    @Override
    public boolean existsByEntityNo(String entityNO, Long projectId) {

        if (weldEntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
            return true;
        }

        return false;
    }

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param operatorId  项目ID
     * @return 实体下载临时文件
     */
    @Override
    public File saveDownloadFile(Long orgId, Long projectId, WeldEntryCriteriaDTO criteriaDTO, Long operatorId) {

        String templateFilePath = "/var/imos/resources/templates/reports/export-project-entities-weld.xlsx";
        String templateFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/imos/private/upload/" + templateFileName;
        File excel = new File(filePath);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends WeldEntityBase> weldEntities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();
        EasyExcel.write(filePath, WeldEntityBase.class).withTemplate(templateFilePath).sheet("Welds").doFill(weldEntities);
        return excel;


//        String temporaryFileName = FileUtils.copy(
//            this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("templates/export-project-entities-weld.xlsx"),
//            temporaryDir,
//            operatorId.toString()
//        );
//
//
//        File excel;
//        Workbook workbook;
//
//        try {
//            excel = new File(temporaryDir, temporaryFileName);
//            workbook = WorkbookFactory.create(excel);
//        } catch (IOException | InvalidFormatException e) {
//            e.printStackTrace(System.out);
//            throw new BusinessError();
//        }
//
//        Sheet sheet = workbook.getSheet("Welds");
//
//        int rowNum = DATA_START_ROW;
//
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setFetchAll(true);
//        List<? extends WeldEntityBase> weldEntities = search(
//            orgId,
//            projectId,
//            criteriaDTO,
//            pageDTO).getContent();
//        for (WeldEntityBase entity : weldEntities) {
//
//            Row row = WorkbookUtils.getRow(sheet, rowNum++);
//
//            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
//                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
//            }
//            if (entity.getSpoolNo() != null) {
//                WorkbookUtils.getCell(row, 0).setCellValue(entity.getSpoolNo());
//            }
//            if (entity.getIsoNo() != null) {
//                WorkbookUtils.getCell(row, 1).setCellValue(entity.getIsoNo());
//            }
//
//            WorkbookUtils.getCell(row, 2).setCellValue(entity.getDisplayName());
//
//            WorkbookUtils.getCell(row, 3).setCellValue(entity.getShopField());
//            WorkbookUtils.getCell(row, 4).setCellValue(entity.getWeldType());
//            WorkbookUtils.getCell(row, 5).setCellValue(entity.getSheetNo());
//            WorkbookUtils.getCell(row, 6).setCellValue(entity.getSheetTotal());
//            WorkbookUtils.getCell(row, 7).setCellValue(entity.getRevision());
//            WorkbookUtils.getCell(row, 8).setCellValue(entity.getWpsNo());
//            if (entity.getRt() != null) {
//                WorkbookUtils.getCell(row, 9).setCellValue(entity.getRt());
//            }
//
//            if (entity.getUt() != null) {
//                WorkbookUtils.getCell(row, 10).setCellValue(entity.getUt());
//            }
//            if (entity.getMt() != null) {
//                WorkbookUtils.getCell(row, 11).setCellValue(entity.getMt());
//            }
//            if (entity.getPt() != null) {
//                WorkbookUtils.getCell(row, 12).setCellValue(entity.getPt());
//            }
//            if (entity.getPwht() != null) {
//                WorkbookUtils.getCell(row, 13).setCellValue(entity.getPwht() ? YES : NO);
//            }
//            if (entity.getHardnessTest() != null) {
//                WorkbookUtils.getCell(row, 14).setCellValue(entity.getHardnessTest() ? YES : NO);
//            }
//            if (entity.getPmiRatio() != null) {
//                WorkbookUtils.getCell(row, 15).setCellValue(entity.getPmiRatio());
//            }
//            if (entity.getFn() != null) {
//                WorkbookUtils.getCell(row, 16).setCellValue(entity.getFn());
//            }
//            WorkbookUtils.getCell(row, 17).setCellValue(entity.getPipeClass());
//            WorkbookUtils.getCell(row, 18).setCellValue(entity.getNpsText());
//            WorkbookUtils.getCell(row, 19).setCellValue(entity.getThickness());
//
//            WorkbookUtils.getCell(row, 20).setCellValue(entity.getTagNo1());
//            WorkbookUtils.getCell(row, 21).setCellValue(entity.getMaterialCode1());
//            WorkbookUtils.getCell(row, 22).setCellValue(entity.getMaterial1());
//            WorkbookUtils.getCell(row, 23).setCellValue(entity.getRemarks1());
//            WorkbookUtils.getCell(row, 24).setCellValue(entity.getTagNo2());
//            WorkbookUtils.getCell(row, 25).setCellValue(entity.getMaterialCode2());
//            WorkbookUtils.getCell(row, 26).setCellValue(entity.getMaterial2());
//            WorkbookUtils.getCell(row, 27).setCellValue(entity.getRemarks2());
//            WorkbookUtils.getCell(row, 28).setCellValue(entity.getPaintingCode());
//            WorkbookUtils.getCell(row, 29).setCellValue(entity.getMaterialType());
//
//            if (entity.getMaterialTraceability() != null) {
//                WorkbookUtils.getCell(row, 30).setCellValue(entity.getMaterialTraceability() ? YES : NO);
//            }
//            WorkbookUtils.getCell(row, 31).setCellValue(entity.getRemarks());
//            WorkbookUtils.getCell(row, 32).setCellValue(entity.getRemarks3());
//            WorkbookUtils.getCell(row, 33).setCellValue(entity.getModuleNo());
//
////            if (entity.getIsoEntityId() != null) {
////                Optional<ISOEntity> isoEntityOptional = isoEntityRepository.findById(entity.getIsoEntityId());
////                if (isoEntityOptional.isPresent()) {
////                    WorkbookUtils.getCell(row, 33).setCellValue(isoEntityOptional.get().getModuleNo());
////                }
////            }
//        }
//
//        try {
//            WorkbookUtils.save(workbook, excel.getAbsolutePath());
//            return excel;
//        } catch (IOException e) {
//            e.printStackTrace(System.out);
//            throw new BusinessError();
//        }
    }

    /**
     * 取得焊口实体的WPS信息。
     * 其他实体（管线，单管，管段，管件）不必实现这个方法
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return WPS信息
     */
    @Override
    public List<Wps> getAlreadySetWPSInfo(
        Long id,
        Long orgId,
        Long projectId
    ) {
        WeldEntityBase entity = weldEntityRepository
            .findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(
                id,
                orgId,
                projectId
            )
            .orElse(null);

        if (entity == null) {
            throw new NotFoundError();
        }


        String wpsInfo = entity.getWpsId();

        if (wpsInfo != null) {
            String[] wpsIds = wpsInfo.split(COMMA);

            List<Wps> wpsList = wpsRepository.findByIdInAndOrgIdAndProjectIdAndDeletedIsFalse(
                LongUtils.change2Str(wpsIds),
                orgId,
                projectId);

            return wpsList;
        }
        return null;
    }

    @Override
    public WpsImportResultDTO updateWeldEntityWps(Long orgId, Long projectId, Long userId, WpsImportDTO uploadDTO) {
        Workbook workbook = null;
        File excel;


        try {
            excel = new File(temporaryDir, uploadDTO.getFileName());
            workbook = WorkbookFactory.create(excel);
        } catch (RecordFormatException e) {
            throw new ValidationError("WRONG XLS FORMAT");
        } catch (IOException e) {
            throw new NotFoundError();
        }

        int errorCount = 0;
        int successCount = 0;
        int skipCount = 0;
        List<String> errLine = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);

        WpsSimpleSearchDTO wpsSimpleSearchDTO = new WpsSimpleSearchDTO();
        wpsSimpleSearchDTO.setFetchAll(true);

        Page<WpsSimplified> wpsPageList = wpsSimpleRepository.findByOrgIdAndProjectIdAndStatusOrderByNo(
            orgId,
            projectId,
            EntityStatus.ACTIVE,
            wpsSimpleSearchDTO.toPageable()
        );
        List<WpsSimplified> wpsList = wpsPageList.getContent();
        if (wpsList.size() == 0) {
            throw new BusinessError("该项目wps列表为空");
        }

        Row row;
        boolean skipRow = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            row = rows.next();
            if (row.getRowNum() < 1) {
                continue;
            }

            try {
                int colIndex = 0;
                String isoNo = trim(WorkbookUtils.readAsString(row, colIndex++));
                String weldNo = trim(WorkbookUtils.readAsString(row, colIndex++));
                String wpsStr = trim(WorkbookUtils.readAsString(row, colIndex++));
                if (StringUtils.isEmpty(isoNo) || StringUtils.isEmpty(weldNo) || StringUtils.isEmpty(wpsStr)) {
                    errorCount++;
                    errLine.add("" + (row.getRowNum() + 1) + " information is not enough");
                    continue;
                }

                String weldFullNo = isoNo + "-W" + StringUtils.padLeft(weldNo, 4, '0');
                List<String> wpsNos = Arrays.asList(wpsStr.split(","));
                List<String> wpsFoundNoList = new ArrayList<>();
                List<Long> wpsIdsList = new ArrayList<>();
                String wpsFoundStr = "";
                String wpsFoundIdsStr = "";

                WeldEntity weldEntityFind = weldEntityRepository.findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(
                    orgId,
                    projectId,
                    weldFullNo
                ).orElse(null);
                if (weldEntityFind == null) {
                    errorCount++;
                    errLine.add("焊口编号" + weldFullNo + "不存在");
                    continue;
                }

                for (String wpsNo : wpsNos) {
                    for (WpsSimplified wps : wpsList) {
                        if (wps.getNo().equalsIgnoreCase(wpsNo)) {
                            wpsFoundNoList.add(wpsNo);
                            wpsIdsList.add(wps.getId());
                            break;
                        }
                    }
                }
                if (wpsFoundNoList.size() > 0) {
                    wpsFoundStr = StringUtil.join(wpsFoundNoList.toArray(), ",");
                    wpsFoundIdsStr = StringUtil.join(wpsIdsList.toArray(), ",");

                    weldEntityRepository.updateWeldWpsNo(orgId, projectId, userId, weldFullNo, wpsFoundStr, wpsFoundIdsStr);
                    successCount++;
                } else {
                    errorCount++;
                    errLine.add("" + (row.getRowNum() + 1) + " no such wps in wps lib");
                }

            } catch (Exception e) {
                e.printStackTrace(System.out);
                errorCount++;
                errLine.add("" + (row.getRowNum() + 1));
            }
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        WpsImportResultDTO dto = new WpsImportResultDTO();
        dto.setErrorCount(errorCount);
        dto.setSkipCount(skipCount);
        dto.setErrorList(errLine);
        dto.setSuccessCount(successCount);
        return dto;
    }

    private void setEntityTypeByRules(Long orgId,
                                      Long projectId,
                                      WeldEntity weldEntity) {
        weldEntityCategoryRules = getEntityCategoryRules(
            orgId,
            projectId,
            WBSEntityType.WELD_JOINT);

        EntitySubTypeRule rule = getWeldEntityTypeRuleBySetting(weldEntity, weldEntityCategoryRules);
        if (rule != null) {

            weldEntity.setWeldEntityType(rule.getEntitySubType().getNameEn());

            if (rule.getEntitySubType() != null
                && rule.getEntitySubType().getEntityBusinessType() != null) {
                weldEntity.setEntityBusinessType(rule.getEntitySubType().getEntityBusinessType().getNameEn());
            }



            if (rule.getThicknessRequired()) {
                if (StringUtils.isEmpty(weldEntity.getThickness())) {
                    throw new ValidationError("thickness is INVALID");
                }
                String thicknessStr = weldEntity.getThickness();
                thicknessStr = thicknessStr.replaceAll("(S)(CH|-)?(\\d+)s?", "$1-$3");
                thicknessStr = thicknessStr.replaceAll("S-(STD|XS|XXS|XXXS)", "$1");
                Matcher matcher = THICKNESS.matcher(thicknessStr);

                if (matcher.find()) {
                    weldEntity.setThickness(matcher.group());
                } else {
                    throw new ValidationError("thickness is INVALID");
                }
            }
        }
    }

    /**
     * 取得实体类型设置规则。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param categoryType 实体大类型
     * @return 实体类型设置规则列表
     */
    private Set<EntitySubTypeRule> getEntityCategoryRules(Long orgId,
                                                           Long projectId,
                                                           WBSEntityType categoryType) {
        return entityCategoryRuleRepository.findByEntityTypeAndOrgIdAndProjectIdAndDeletedIsFalseOrderByRuleOrder(
            categoryType.name(),
            orgId,
            projectId);
    }

    /**
     * 设置焊口实体类型。
     *
     * @param weldEntity 焊口实体
     * @param rules      焊口实体设定规则
     * @return EntityCategoryRule 实体类型设置规则
     */
    private EntitySubTypeRule getWeldEntityTypeRuleBySetting(
        WeldEntity weldEntity,
        Set<EntitySubTypeRule> rules) {


        for (EntitySubTypeRule rule : rules) {
            if (!StringUtils.isEmpty(rule.getValue1()) && !StringUtils.isEmpty(rule.getValue2())) {
                if (weldEntity.getWeldType().equalsIgnoreCase(rule.getValue2())
                    && weldEntity.getShopField().equalsIgnoreCase(rule.getValue1())) {
                    return rule;
                }
            }

        }
        return null;
    }

    /**
     * 取得焊口和焊工共同的WPS列表。
     *
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return 焊工证书信息
     */
    @Override
    public List<WpsMatchingDTO> getWelderWps(
        Long orgId,
        Long projectId,
        WeldWelderWPSDTO weldWelderWPSDTO
    ) {

        Long userId = weldWelderWPSDTO.getWelderIds().get(0);


        Long welderId = 0L;


        Long entityId = weldWelderWPSDTO.getWeldIds().get(0);


        List<WpsMatchingDTO> weldWpsList = new ArrayList<>();


        List<WpsMatchingDTO> wpsList = new ArrayList<>();


        List<WpsMatchingDTO> returnList = new ArrayList<>();


        List<Welder> welder = welderRepository
            .findByOrgIdAndProjectIdAndUserIdAndDeletedIsFalse(
                orgId,
                projectId,
                userId
            );
        if (welder.isEmpty()) {
            throw new ValidationError("welder not Exit");
        } else {
            welderId = welder.get(0).getId();
        }


        WeldEntityBase entity = weldHierarchyInfoEntityRepository
            .findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(
                entityId,
                orgId,
                projectId
            )
            .orElse(null);

        if (entity == null) {
            throw new ValidationError("weld no not Exit");
        }


        String weldWpsInfo = entity.getWpsId();
        if (weldWpsInfo != null) {
            String[] weldWpsIds = weldWpsInfo.split(COMMA);

            List<Wps> weldWpsInitList = wpsRepository.findByIdInAndOrgIdAndProjectIdAndDeletedIsFalse(LongUtils.change2Str(weldWpsIds),
                orgId,
                projectId);

            if (weldWpsInitList.size() > 0) {
                for (Wps weldWps : weldWpsInitList) {
                    List<WpsDetail> weldWpsDetailList = wpsDetailRepository.findByWpsIdAndDeletedIsFalse(weldWps.getId());
                    for (WpsDetail weldWpsDetail : weldWpsDetailList) {
                        if (weldWpsDetail.getPosition() != null) {
                            WpsMatchingDTO weldWpsMatching = new WpsMatchingDTO();
                            weldWpsMatching.setPosition(weldWpsDetail.getPosition());
                            weldWpsMatching.setProcess(weldWps.getProcess());
                            weldWpsMatching.setWpsNo(weldWps.getCode());
                            weldWpsList.add(weldWpsMatching);
                        }
                    }
                }
            }
        }


        List<WelderCertificate> welderCertificateList = null;


        if (entity.getMaterial1().equals("") || entity.getWeldType().equals("")) {
            throw new BusinessError("", "business-error:WELD Material is Empty.");
        }

        WpsCriteriaDTO wpsCriteriaDTO = new WpsCriteriaDTO();
        wpsCriteriaDTO.setBaseMetal(entity.getMaterial1());

        wpsCriteriaDTO.setBaseMetal2(entity.getMaterial2());

        wpsCriteriaDTO.setWeldType(entity.getWeldType());

        if (entity.getWeldType().equals(WeldType.BW.name())) {
            NPSThicknessSearchDTO searchDTO = new NPSThicknessSearchDTO();
            searchDTO.setNps(entity.getNpsText());
            searchDTO.setNpsValue(String.valueOf(entity.getNps()));
            searchDTO.setThicknessSch(entity.getThickness());
//            NPSch npSch = npsInterface.searchThickness(orgId, projectId, searchDTO);
//
//            if (npSch != null) {
//                wpsCriteriaDTO.setMaxDiaRange(String.valueOf(entity.getNps()));
//                wpsCriteriaDTO.setContainMaxDiaRange("true");
//                wpsCriteriaDTO.setMinDiaRange(String.valueOf(entity.getNps()));
//                wpsCriteriaDTO.setContainMinDiaRange("true");
//                wpsCriteriaDTO.setMaxThickness(String.valueOf(npSch.getThickness()));
//                wpsCriteriaDTO.setContainMaxThickness("true");
//                wpsCriteriaDTO.setMinThickness(String.valueOf(npSch.getThickness()));
//                wpsCriteriaDTO.setContainMinThickness("true");
//
//                welderCertificateList = welderCertificateRepository.search(welderId, wpsCriteriaDTO);
//
//            }
        } else {
            welderCertificateList = welderCertificateRepository.search(welderId, wpsCriteriaDTO);
        }
        if (welderCertificateList == null) {
            throw new BusinessError("", "business-error: WPS info does NOT exist.");
        }

        for (WelderCertificate welderCertificate : welderCertificateList) {
            for (WpsMatchingDTO weldWps : weldWpsList) {
                if (positionMatch(weldWps.getPosition(), welderCertificate.getActualPosition())
                    && processMatch(weldWps.getProcess(), welderCertificate.getActualProcess())) {
                    wpsList.add(weldWps);
                }
            }
        }


        Map map = new HashMap<>();

        for (WpsMatchingDTO wps : wpsList) {
            map.put(wps.getWpsNo(), wps);
        }
        for (Object key : map.keySet()) {
            returnList.add((WpsMatchingDTO) map.get(key));
        }
        return returnList;

    }

    /**
     * 匹配焊接工艺。
     *
     * @param positionA 位置1
     * @param positionB 位置2
     */
    private Boolean positionMatch(String positionA, String positionB) {
        if (positionA != null & positionB != null) {
            if (positionA.equals("ALL") || positionB.equals("ALL")) {
                return true;
            } else if (positionA.equals(positionB)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 匹配焊接位置。
     *
     * @param processA 焊接工艺1
     * @param processB 焊接工艺2
     */
    private Boolean processMatch(String processA, String processB) {
        if (processA != null & processB != null) {
            if (processA.equals(processB)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 取得 WELD 图纸信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return subDrawingHistory
     */
    @Override
    public List<SubDrawing> getDrawingInfo(
        Long orgId,
        Long projectId,
        Long id,
        BpmActivityInstanceBase actInst) {


        Optional<WeldEntity> opt = weldEntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(id, orgId,
            projectId);
        if (opt.isPresent()) {
            WeldEntity entity = opt.get();
            String isoName = entity.getIsoNo();
            Integer sheetNo = entity.getSheetNo();
            isoName = isoName.replaceAll("\"", "_").replaceAll("/", "_");

            return subDrawingRepository.findByOrgIdAndProjectIdAndStatusAndSubDrawingNoAndPageNo(orgId,
                projectId, EntityStatus.ACTIVE, isoName, sheetNo);
        }


        return new ArrayList<>();
    }

    /**
     * 设置 ISO PN ID号，更新WBS-ENTRY
     */






    /**
     * 设置 ISO PN ID号，更新WBS-ENTRY
     */
    @Override
    public void setIsoIdsAndWbs(Long projectId, Long entityId) {
        weldEntityRepository.updateWeldAndProjectNodeIds(projectId, entityId);


    }

}
