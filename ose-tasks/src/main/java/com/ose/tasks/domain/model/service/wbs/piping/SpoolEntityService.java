package com.ose.tasks.domain.model.service.wbs.piping;

import com.alibaba.excel.EasyExcel;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.HierarchyRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmDeliveryEntityRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageEntityRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.*;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.wbs.SpoolEntryCriteriaDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.wbs.HierarchyType;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.BeanUtils;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;

@Component
public class SpoolEntityService implements BaseWBSEntityInterface<SpoolEntityBase, SpoolEntryCriteriaDTO> {


    private final SpoolEntityRepository spoolEntityRepository;

    private final ISOEntityRepository isoEntityRepository;
    private final SpoolHierarchyInfoEntityRepository spoolHierarchyInfoEntityRepository;


    private final HierarchyRepository hierarchyRepository;


    private ProjectNodeRepository projectNodeRepository;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final TaskPackageEntityRelationRepository taskPackageEntityRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;


    private final SubDrawingHistoryRepository subDrawingHistoryRepository;


    private final PipePieceEntityRepository pipePieceEntityRepository;


    private final ComponentEntityRepository componentEntityRepository;

    private final WBSEntryRepository wbsEntryRepository;


    private HierarchyInterface hierarchyService;


    private final PlanInterface planService;


    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

//    private final FMaterialPrepareNodeRepository fMaterialPrepareNodeRepository;

    @Value("${application.files.temporary}")
    private String temporaryDir;


    private static final int DATA_START_ROW = 3;

    private static final int TEMPLATE_ROW_COUNT = 20;

    private static final String HYPHEN = "-";

    private static final String YES = "Y";

    private static final String NO = "N";

    /**
     * 构造方法。
     */
    @Autowired
    public SpoolEntityService(
        SpoolEntityRepository spoolEntityRepository,
        SpoolHierarchyInfoEntityRepository spoolHierarchyInfoEntityRepository,
        HierarchyRepository hierarchyRepository,
        ProjectNodeRepository projectNodeRepository,
        SubDrawingHistoryRepository subDrawingHistoryRepository,
        PipePieceEntityRepository pipePieceEntityRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        TaskPackageEntityRelationRepository taskPackageEntityRelationRepository,
        ComponentEntityRepository componentEntityRepository,
        WBSEntryRepository wbsEntryRepository, HierarchyInterface hierarchyService,
        PlanInterface planService,
        BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
        SubDrawingRepository subDrawingRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
//        FMaterialPrepareNodeRepository fMaterialPrepareNodeRepository,
        ISOEntityRepository isoEntityRepository) {
        this.spoolEntityRepository = spoolEntityRepository;
        this.spoolHierarchyInfoEntityRepository = spoolHierarchyInfoEntityRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.subDrawingHistoryRepository = subDrawingHistoryRepository;
        this.taskPackageEntityRelationRepository = taskPackageEntityRelationRepository;
        this.pipePieceEntityRepository = pipePieceEntityRepository;
        this.componentEntityRepository = componentEntityRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
//        this.fMaterialPrepareNodeRepository = fMaterialPrepareNodeRepository;
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
    public Page<? extends SpoolEntityBase> search(
        Long orgId,
        Long projectId,
        SpoolEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {

        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.PIPING)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findSpoolIdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "SPOOL",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return spoolHierarchyInfoEntityRepository.search(
                        orgId,
                        projectId,
                        criteriaDTO,
                        pageDTO,
                        SpoolHierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return spoolHierarchyInfoEntityRepository.search(
                    orgId,
                    projectId,
                    criteriaDTO,
                    pageDTO,
                    SpoolHierarchyInfoEntity.class);
            }
        } else {
            return spoolHierarchyInfoEntityRepository.search(
                orgId,
                projectId,
                criteriaDTO,
                pageDTO,
                SpoolHierarchyInfoEntity.class);
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
    public SpoolEntityBase get(Long orgId, Long projectId, Long entityId) {

        SpoolEntityBase entity = spoolHierarchyInfoEntityRepository
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
    public void delete(
        OperatorDTO operator,
        Long orgId,
        Project project,
        Long entityId
    ) {

        SpoolEntityBase entity = get(orgId, project.getId(), entityId);

        SpoolEntity spoolEntity = BeanUtils.copyProperties(entity, new SpoolEntity());

        if (haveChildren(project.getId(), entity)) {
            throw new ValidationError("Entity have children ,Please delete children first");
        }

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

        List<TaskPackageEntityRelation> taskPackageList = taskPackageEntityRelationRepository.findByOrgIdAndProjectIdAndEntityId(project.getOrgId(), project.getId(), entityId);
        if (taskPackageList.size() > 0) {
            for (TaskPackageEntityRelation taskPackageEntityRelation : taskPackageList) {
                taskPackageEntityRelation.setStatus(DELETED);
                taskPackageEntityRelationRepository.save(taskPackageEntityRelation);
            }
        }

//        List<FMaterialPrepareNodeEntity> fMaterialPrepareNodeEntities = fMaterialPrepareNodeRepository.findByProjectIdAndProjectNodeId(project.getId(), spoolEntity.getId());
//        if (fMaterialPrepareNodeEntities.size() > 0) {
//            for (FMaterialPrepareNodeEntity fMaterialPrepareNodeEntity : fMaterialPrepareNodeEntities) {
//                fMaterialPrepareNodeEntity.setStatus(DELETED);
//                fMaterialPrepareNodeRepository.save(fMaterialPrepareNodeEntity);
//            }
//        }

        spoolEntity.setDeletedBy(operator.getId());
        spoolEntity.setDeletedAt();
        spoolEntity.setStatus(DELETED);
        spoolEntity.setDeleted(true);

        hierarchyService.delete(operator, project, orgId, entityId);
        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(DELETED, project.getId(), entityId);
        spoolEntityRepository.save(spoolEntity);
    }

    /**
     * 插入 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    Spool 实体
     */
    @Override
    public void insert(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        SpoolEntityBase entity
    ) {

        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        SpoolEntity spoolEntity = BeanUtils.copyProperties(entity, new SpoolEntity());
        spoolEntityRepository.save(spoolEntity);


        ProjectNode projectNode = new ProjectNode();
        projectNode.setNo(entity.getNo());
        projectNode.setCompanyId(entity.getCompanyId());
        projectNode.setEntityId(entity.getId());
        projectNode.setEntityType(WBSEntityType.SPOOL.name());
        projectNode.setEntitySubType(WBSEntityType.SPOOL.name());
        projectNode.setEntityBusinessType(entity.getEntityBusinessType());
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
     * @param entity    Spool 实体
     */
    @Override
    public SpoolEntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        SpoolEntityBase entity
    ) {

        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());

        SpoolEntity spoolEntity = BeanUtils.copyProperties(entity, new SpoolEntity());
        spoolEntityRepository.save(spoolEntity);
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

        if (spoolEntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
            return true;
        }

        return false;
    }

    /**
     * 取得单管图纸信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return
     */
    @Override
    public SubDrawingHistory findSubDrawing(
        Long id,
        Long orgId,
        Long projectId) {


        SpoolEntity spoolEntity = spoolEntityRepository.findById(id).orElse(null);
        if (spoolEntity == null) {
            return null;
        }

        SubDrawingHistory subDrawingHistory =
            subDrawingHistoryRepository.findBySubDrawingNoAndPageNoAndIssueFlagIsTrue(
                spoolEntity.getNo(),
                spoolEntity.getSheetNo())
                .orElse(null);
        return subDrawingHistory;
    }

    /**
     * 取得SPOOL图纸信息。
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


        Optional<SpoolEntity> opt = spoolEntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(id, orgId,
            projectId);
        if (opt.isPresent()) {
            SpoolEntity entity = opt.get();
            String isoName = entity.getIsoNo();
            Integer sheetNo = entity.getSheetNo();
            isoName = isoName.replaceAll("\"", "_").replaceAll("/", "_");
            List<SubDrawing> subDrawings = subDrawingRepository.findByOrgIdAndProjectIdAndStatusAndSubDrawingNoAndPageNo(orgId,
                projectId, EntityStatus.ACTIVE, isoName, sheetNo);

            return subDrawings;
        }


        return new ArrayList<>();
    }

    /**
     * 取得单管实体对应的材料信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return 实体信息
     */
    @Override
    public List<MaterialInfoDTO> getMaterialInfo(Long id,
                                                 Long orgId,
                                                 Long projectId) {

        List<HierarchyNode> hierarchyNodes = hierarchyRepository.findByEntityIdAndDeletedIsFalse(id, orgId, projectId);

        if (hierarchyNodes == null || hierarchyNodes.size() == 0) {
            throw new BusinessError("SPOOL has NO hierarchyNode.");
        } else if (hierarchyNodes.size() > 1) {
            throw new BusinessError("SPOOL has TOO many hierarchyNode.");
        }


        List<HierarchyNode> childNodes =
            hierarchyRepository.findByParentIdAndProjectIdAndAndHierarchyTypeAndDeletedIsFalse(
                hierarchyNodes.get(0).getId(), projectId, HierarchyType.PIPING.name());

        List<Long> pipeIds = new ArrayList<>();
        List<Long> componentIds = new ArrayList<>();
        if (childNodes != null && childNodes.size() != 0) {


            for (HierarchyNode nodeTemp : childNodes) {
                if (WBSEntityType.PIPE_PIECE.name() == nodeTemp.getEntityType()) {
                    pipeIds.add(nodeTemp.getNode().getEntityId());
                } else if (WBSEntityType.COMPONENT.name() == nodeTemp.getEntityType()) {
                    componentIds.add(nodeTemp.getNode().getEntityId());
                }
            }

        }
        List<MaterialInfoDTO> materialInfoDTOS = new ArrayList<>();

        if (pipeIds != null && pipeIds.size() != 0) {

            List<MaterialInfoDTO> pipePieceMaterial = pipePieceEntityRepository.findByIdInAndProjectIdAndDeletedIsFalse(
                pipeIds,
                projectId);
            if (pipePieceMaterial != null && pipePieceMaterial.size() != 0) {
                for (MaterialInfoDTO dto : pipePieceMaterial) {
                    dto.setQuantity(dto.getQuantity() + " MM");
                    dto.setType("pipe");
                }
                materialInfoDTOS.addAll(pipePieceMaterial);

            }
        }


        if (componentIds != null && componentIds.size() != 0) {
            List<ComponentEntity> componentEntities = componentEntityRepository.findByIdInAndProjectIdAndDeletedIsFalse(
                componentIds,
                projectId);
            if (componentEntities != null && componentEntities.size() != 0) {
                for (ComponentEntity entity : componentEntities) {
                    MaterialInfoDTO materialInfoDTO = new MaterialInfoDTO();
                    materialInfoDTO.setMaterialDesc(entity.getMaterial());
                    materialInfoDTO.setMaterialCode(entity.getMaterialCode());
                    materialInfoDTO.setType("component");
                    materialInfoDTO.setQuantity(String.valueOf(entity.getQty()));
                    materialInfoDTOS.add(materialInfoDTO);
                }
            }
        }
        Map<String, Object> resultMap = new HashMap<>();

        List<MaterialInfoDTO> resultList = new ArrayList<>();

        for (MaterialInfoDTO materialInfo : materialInfoDTOS) {

            if (materialInfo.getType().equals("component")) {
                MaterialInfoDTO newMaterialInfoDTO = new MaterialInfoDTO();

                if (resultMap.get(materialInfo.getMaterialCode() + '-' + materialInfo.getMaterialDesc()) != null) {

                    BeanUtils.copyProperties(materialInfo, newMaterialInfoDTO, "quantity");

                    MaterialInfoDTO materialInfoDTOMap = (MaterialInfoDTO) resultMap.get(materialInfo.getMaterialCode() + '-' + materialInfo.getMaterialDesc());

                    int qty = Integer.parseInt(materialInfoDTOMap.getQuantity());
                    int newQty = Integer.parseInt(materialInfo.getQuantity());
                    int totalQty = qty + newQty;

                    newMaterialInfoDTO.setQuantity(String.valueOf(totalQty));

                    resultMap.put(materialInfo.getMaterialCode() + '-' + materialInfo.getMaterialDesc(), newMaterialInfoDTO);

                } else {
                    resultMap.put(materialInfo.getMaterialCode() + '-' + materialInfo.getMaterialDesc(), materialInfo);

                }
            } else {
                resultList.add(materialInfo);
            }
        }

        for (String key : resultMap.keySet()) {
            resultList.add((MaterialInfoDTO) resultMap.get(key));
        }

        return resultList;
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
    public File saveDownloadFile(Long orgId, Long projectId, SpoolEntryCriteriaDTO criteriaDTO, Long operatorId) {

        String templateFilePath = "/var/imos/resources/templates/reports/export-project-entities-spool.xlsx";
        String templateFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/imos/private/upload/" + templateFileName;
        File excel = new File(filePath);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends SpoolEntityBase> spoolEntities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();
        EasyExcel.write(filePath, SpoolEntityBase.class).withTemplate(templateFilePath).sheet("Spools").doFill(spoolEntities);
        return excel;


//        String temporaryFileName = FileUtils.copy(
//            this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("templates/export-project-entities-spool.xlsx"),
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
//        Sheet sheet = workbook.getSheet("Spools");
//
//        int rowNum = DATA_START_ROW;
//
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setFetchAll(true);
//        List<? extends SpoolEntityBase> spoolEntities = search(
//            orgId,
//            projectId,
//            criteriaDTO,
//            pageDTO).getContent();
//        for (SpoolEntityBase entity : spoolEntities) {
//
//            Row row = WorkbookUtils.getRow(sheet, rowNum++);
//
//            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
//                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
//            }
//
//            WorkbookUtils.getCell(row, 0).setCellValue(entity.getIsoNo());
//
//            WorkbookUtils.getCell(row, 1).setCellValue(entity.getNo());
//            WorkbookUtils.getCell(row, 2).setCellValue(entity.getShortCode());
//            WorkbookUtils.getCell(row, 3).setCellValue(entity.getSheetNo());
//            WorkbookUtils.getCell(row, 4).setCellValue(entity.getSheetTotal());
//            WorkbookUtils.getCell(row, 5).setCellValue(entity.getRevision());
//            WorkbookUtils.getCell(row, 6).setCellValue(entity.getNpsText());
//            WorkbookUtils.getCell(row, 7).setCellValue(entity.getLengthText());
//            WorkbookUtils.getCell(row, 8).setCellValue(entity.getMaterial());
//            WorkbookUtils.getCell(row, 9).setCellValue(entity.getWeightText());
//
//            WorkbookUtils.getCell(row, 10).setCellValue(entity.getPaintingAreaText());
//            WorkbookUtils.getCell(row, 11).setCellValue(entity.getPaintingCode());
//            if (entity.getSurfaceTreatment() != null) {
//                WorkbookUtils.getCell(row, 12).setCellValue(entity.getSurfaceTreatment() ? YES : NO);
//            }
//            if (entity.getPressureTestRequired() != null) {
//                WorkbookUtils.getCell(row, 13).setCellValue(entity.getPressureTestRequired() ? YES : NO);
//            }
//            WorkbookUtils.getCell(row, 14).setCellValue(entity.getProcessSystemNo());
//            WorkbookUtils.getCell(row, 15).setCellValue(entity.getFluid());
//            WorkbookUtils.getCell(row, 16).setCellValue(entity.getPipeClass());
//            WorkbookUtils.getCell(row, 17).setCellValue(entity.getDesignPressureText());
//            WorkbookUtils.getCell(row, 18).setCellValue(entity.getDesignTemperatureText());
//            WorkbookUtils.getCell(row, 19).setCellValue(entity.getOperatePressureText());
//
//            WorkbookUtils.getCell(row, 20).setCellValue(entity.getOperateTemperatureText());
//            WorkbookUtils.getCell(row, 21).setCellValue(entity.getInsulationCode());
//            WorkbookUtils.getCell(row, 22).setCellValue(entity.getPressureTestMedium());
//            WorkbookUtils.getCell(row, 23).setCellValue(entity.getHeatTracingCode());
//            WorkbookUtils.getCell(row, 24).setCellValue(entity.getPidDrawing());
//            WorkbookUtils.getCell(row, 25).setCellValue(entity.getInternalMechanicalCleaning());
//            WorkbookUtils.getCell(row, 26).setCellValue(entity.getRemarks());
//            WorkbookUtils.getCell(row, 27).setCellValue(entity.getRemarks2());
//            WorkbookUtils.getCell(row,28).setCellValue(entity.getModuleNo());

//            if (entity.getIsoEntityId() != null) {
//                Optional<ISOEntity> isoEntityOptional = isoEntityRepository.findById(entity.getIsoEntityId());
//                if (isoEntityOptional.isPresent()) {
//                    WorkbookUtils.getCell(row, 28).setCellValue(isoEntityOptional.get().getModuleNo());
//                }
//            }

//        }

//        try {
//            WorkbookUtils.save(workbook, excel.getAbsolutePath());
//            return excel;
//        } catch (IOException e) {
//            e.printStackTrace(System.out);
//            throw new BusinessError();
//        }
    }

    @Override
    public SpoolEntityBase getByQrCode(Long orgId, Long projectId, String qrcode) {

        SpoolEntityBase entity = spoolHierarchyInfoEntityRepository
            .findByOrgIdAndProjectIdAndDeletedIsFalseAndQrCode(
                orgId,
                projectId,
                qrcode
            )
            .orElse(null);

        if (entity == null) {
            throw new NotFoundError();
        }

        return entity;
    }

    private <T extends WBSEntityBase> boolean haveChildren(
        Long projectId,
        T entity
    ) {
        List<HierarchyNode> hierarchyNodes = hierarchyRepository.searchChild(
            projectId,
            entity.getId()
        );
        if (hierarchyNodes.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 设置 ISO PN ID号，更新WBS-ENTRY
     */
    @Override
    public void setIsoIdsAndWbs(Long projectId, Long entityId) {
        spoolEntityRepository.updateSpoolAndProjectNodeIds(projectId, entityId);


    }

}
