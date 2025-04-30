package com.ose.tasks.domain.model.service.wbs.structure;

import com.alibaba.excel.EasyExcel;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp05EntityRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp05HierarchyInfoEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.structureWbs.Wp05EntryCriteriaDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.entity.wbs.structureEntity.Wp05Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp05EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp05HierarchyInfoEntity;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import com.ose.util.*;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

import static com.ose.tasks.vo.wbs.WBSEntityType.STRUCT_WELD_JOINT;
import static com.ose.tasks.vo.wbs.WBSEntityType.WP05;
import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;

@Component
public class Wp05EntityService extends StringRedisService implements Wp05EntityInterface {


    private final Wp05EntityRepository wp05EntityRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;


    private final Wp05HierarchyInfoEntityRepository wp05HierarchyInfoEntityRepository;


    private final ProjectNodeRepository projectNodeRepository;


    private final SubDrawingHistoryRepository subDrawingHistoryRepository;


    private final HierarchyInterface hierarchyService;

//    private final BpmStructureCuttingTaskProgramEntityRepository bpmStructureCuttingTaskProgramEntityRepository;


    private final WBSEntryRepository wbsEntryRepository;

    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;


    private final PlanInterface planService;

    private final ProjectRepository projectRepository;

    private final BpmEntityTypeRepository bpmEntityTypeRepository;


//    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;

//    private final StructureEntityQrCodeRepository structureEntityQrCodeRepository;

    @Value("${application.files.temporary}")
    private String temporaryDir;


    private static final int DATA_START_ROW = 3;


    private static final int TEMPLATE_ROW_COUNT = 20;

    private final static Logger logger = LoggerFactory.getLogger(Wp04EntityService.class);


    /**
     * 构造方法。
     */
    @Autowired
    public Wp05EntityService(
        StringRedisTemplate stringRedisTemplate,
        Wp05EntityRepository wp05EntityRepository,
        Wp05HierarchyInfoEntityRepository wp05HierarchyInfoEntityRepository,
        ProjectNodeRepository projectNodeRepository,
        SubDrawingHistoryRepository subDrawingHistoryRepository,
        HierarchyInterface hierarchyService,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
//        BpmStructureCuttingTaskProgramEntityRepository bpmStructureCuttingTaskProgramEntityRepository,
        PlanInterface planService,
        ProjectRepository projectRepository,
        BpmEntityTypeRepository bpmEntityTypeRepository,
//        BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        WBSEntryRepository wbsEntryRepository
//        StructureEntityQrCodeRepository structureEntityQrCodeRepository
    ) {
        super(stringRedisTemplate);
        this.wp05EntityRepository = wp05EntityRepository;
        this.wp05HierarchyInfoEntityRepository = wp05HierarchyInfoEntityRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.subDrawingHistoryRepository = subDrawingHistoryRepository;
//        this.bpmStructureCuttingTaskProgramEntityRepository = bpmStructureCuttingTaskProgramEntityRepository;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.projectRepository = projectRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.bpmEntityTypeRepository = bpmEntityTypeRepository;
//        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.wbsEntryRepository = wbsEntryRepository;
//        this.structureEntityQrCodeRepository = structureEntityQrCodeRepository;
    }

    /**
     * 查询 WBS 实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageDTO   分页参数
     * @return WBS 实体分页数据
     */
    @Override
    public Page<? extends Wp05EntityBase> search(
        Long orgId,
        Long projectId,
        Wp05EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {

        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.STRUCTURE)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findWp05IdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "WP05",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return wp05HierarchyInfoEntityRepository.search(orgId, projectId, criteriaDTO, pageDTO, Wp05HierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return wp05HierarchyInfoEntityRepository.search(orgId, projectId, criteriaDTO, pageDTO, Wp05HierarchyInfoEntity.class);
            }
        } else {
            return wp05HierarchyInfoEntityRepository.search(orgId, projectId, criteriaDTO, pageDTO, Wp05HierarchyInfoEntity.class);
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
    public Wp05Entity get(Long orgId, Long projectId, Long entityId) {

        Wp05Entity entity = wp05EntityRepository
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

        Wp05Entity entity = get(orgId, project.getId(), entityId);

        Wp05Entity wp05Entity = BeanUtils.copyProperties(entity, new Wp05Entity());

//        Boolean isDeletable = projectNodeRepository.existsByProjectIdAndEntityIdAndIsDeletableIsTrue(project.getId(), entity.getId());
//        if(isDeletable == null || isDeletable)  {


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

//        StructureEntityQrCode structureEntityQrCode = structureEntityQrCodeRepository.findByOrgIdAndProjectIdAndEntityIdAndDeletedIsFalse(orgId, project.getId(), entity.getId());

//        if (structureEntityQrCode != null) {
//            structureEntityQrCode.setStatus(EntityStatus.DELETED);
//            structureEntityQrCode.setDeleted(true);
//            structureEntityQrCodeRepository.save(structureEntityQrCode);
//        }

        wp05Entity.setDeletedBy(operator.getId());
        wp05Entity.setDeletedAt();
        wp05Entity.setStatus(DELETED);
        wp05Entity.setDeleted(true);

        hierarchyService.delete(operator, project, orgId, entityId);
//        bpmStructureCuttingTaskProgramEntityRepository.updateEntityStatus(EntityStatus.DELETED, orgId, project.getId(), entityId);

//        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entityId);
        wp05EntityRepository.save(wp05Entity);

//        } else {
//
//            wp05Entity.setCancelled(true);
//            wp05Entity.setStatus(EntityStatus.CANCEL);
//            wp05Entity.setLastModifiedBy(operator.getId());
//            wp05Entity.setLastModifiedAt();
//
//            hierarchyService.cancel(operator, project, orgId, entityId);
//            bpmStructureCuttingTaskProgramEntityRepository.updateEntityStatus(EntityStatus.CANCEL, orgId, project.getId(), entityId);
//            bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.CANCEL, project.getId(), entityId);
//
//        }


//        StructureEntityQrCode structureEntityQrCode = structureEntityQrCodeRepository.findByOrgIdAndProjectIdAndEntityIdAndDeletedIsFalse(orgId, project.getId(), wp05Entity.getId());
//        if (structureEntityQrCode != null) {
//            structureEntityQrCodeRepository.delete(structureEntityQrCode);
//        }
//
//        List<BpmDeliveryEntity> bpmDeliveryEntities = bpmDeliveryEntityRepository.findByOrgIdAndProjectIdAndEntityId(orgId, project.getId(), wp05Entity.getId());
//        if (bpmDeliveryEntities.size() > 0) {
//            bpmDeliveryEntityRepository.deleteAll(bpmDeliveryEntities);
//        }

//        if(isDeletable == null) return;
//        planService.updateStatusOfWBSOfDeletedEntity(project.getId(), entity.getWbsEntityType(), entityId, operator.getId(), isDeletable);
    }

    /**
     * 插入 WBS 实体同时插入ProjectNodes表里的记录。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    Part 实体
     */
    @Override
    public void insert(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Wp05EntityBase entity

    ) {
        if (entity.getNo() == null || "".equals(entity.getNo())) {
            throw new BusinessError("", "business-error: No is  not null.");
        }
        if (existsByEntityNo(entity.getNo(), projectId)) {
            throw new BusinessError("", "business-error: module no ALREADY EXISTS.");
        }


        if (entity.getRevision() == null || "".equals(entity.getRevision())) {
            throw new BusinessError("", "business-error: Revision is null.");
        }
        if (entity.getLength() == null) {
            entity.setLength(0.0);
        }
        if (entity.getLengthText() == null) {
            entity.setLengthText(entity.getLength() + entity.getLengthUnit().toString());
        }
        if (entity.getHeight() == null) {
            entity.setHeight(0.0);
        }
        if (entity.getHeightText() == null) {
            entity.setHeightText(entity.getHeight() + entity.getHeightUnit().toString());
        }
        if (entity.getWidth() == null) {
            entity.setWidth(0.0);
        }
        if (entity.getWidthText() == null) {
            entity.setWidthText(entity.getWidth() + entity.getWidthUnit().toString());
        }
        if (entity.getMaterial() == null) {
            throw new BusinessError("", "business-error: Material is null.");
        }
        if (entity.getMemberSize() == null) {
            entity.setMemberSize("0");
        }
        entity.setEntityType("WP05");
        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        Wp05Entity wp05Entity = BeanUtils.copyProperties(entity, new Wp05Entity());
        if (wp05Entity.getId() == null) {
            wp05Entity.setId(CryptoUtils.uniqueDecId());
        }
        wp05EntityRepository.save(wp05Entity);

        ProjectNode projectNode = new ProjectNode();
        projectNode.setNo(entity.getNo());
        projectNode.setCompanyId(entity.getCompanyId());
        projectNode.setEntityId(entity.getId());
        projectNode.setEntityType(entity.getEntityType());
        projectNode.setEntitySubType(entity.getEntitySubType());
        projectNode.setEntityBusinessType(entity.getEntityBusinessType());
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
        projectNode.setDiscipline(DisciplineCode.STRUCTURE.name());
        projectNodeRepository.save(projectNode);
        projectNodeRepository.setParentEntityIdOnStructureEntity(projectId, "wp05", wp05Entity.getId());

        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) return;
        BpmEntityType bpmEntityType =
            bpmEntityTypeRepository.
                findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(
                    WP05.name(),
                    project.getId(),
                    project.getOrgId(),
                    CategoryTypeTag.READONLY.name(),
                    EntityStatus.ACTIVE).orElse(null);
        if (bpmEntityType == null) {

            logger.error("There is no " + STRUCT_WELD_JOINT + "setting in DB");
            return;
        }
        wp05EntityRepository.save(wp05Entity);
    }


    /**
     * 更新 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    Part 实体
     */
    @Override
    public Wp05EntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Wp05EntityBase entity
    ) {
        if (entity.getNo() == null || "".equals(entity.getNo())) {
            throw new BusinessError("", "business-error: No is  not null.");
        }


        if (entity.getRevision() == null || "".equals(entity.getRevision())) {
            throw new BusinessError("", "business-error: Revision is null.");
        }
        if (entity.getLength() == null) {
            entity.setLength(0.0);
        }
        if (entity.getLengthText() == null) {
            entity.setLengthText(entity.getLength() + entity.getLengthUnit().toString());
        }
        if (entity.getHeight() == null) {
            entity.setHeight(0.0);
        }
        if (entity.getHeightText() == null) {
            entity.setHeightText(entity.getHeight() + entity.getHeightUnit().toString());
        }
        if (entity.getWidth() == null) {
            entity.setWidth(0.0);
        }
        if (entity.getWidthText() == null) {
            entity.setWidthText(entity.getWidth() + entity.getWidthUnit().toString());
        }
        if (entity.getMaterial() == null) {
            entity.setMaterial("0");
        }
        if (entity.getMemberSize() == null) {
            entity.setMemberSize("0");
        }
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        Wp05Entity wp05Entity = BeanUtils.copyProperties(entity, new Wp05Entity());
        wp05EntityRepository.save(wp05Entity);
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

        return wp05EntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId);

    }

    /**
     * 取得零件图纸信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return subDrawingHistory
     */
    @Override
    public SubDrawingHistory findSubDrawing(
        Long id,
        Long orgId,
        Long projectId) {

        Wp05Entity wp05Entity = wp05EntityRepository.findById(id).orElse(null);
        if (wp05Entity == null) {
            return null;
        }
        return subDrawingHistoryRepository.findBySubDrawingNoAndIssueFlagIsTrue(
            wp05Entity.getNo())
            .orElse(null);
    }


    /**
     * 取得零件实体对应的材料信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return 实体信息
     */
    @Override
    public List<MaterialInfoDTO> getMaterialInfo(
        Long id,
        Long orgId,
        Long projectId) {


        return null;
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
    public File saveDownloadFile(Long orgId, Long projectId, Wp05EntryCriteriaDTO criteriaDTO, Long operatorId) {


//        String temporaryFileName = FileUtils.copy(
//            this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("templates/export-project-entities-wp05.xlsx"),
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
//        Sheet sheet = workbook.getSheet("wp05");
//
//        int rowNum = DATA_START_ROW;

        String templateFilePath = "/var/imos/resources/templates/reports/export-project-entities-wp05.xlsx";

        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/imos/private/upload/" + temporaryFileName;
        File excel = new File(filePath);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends Wp05EntityBase> partEntities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();

        EasyExcel.write(filePath).withTemplate(templateFilePath).sheet("wp05").doFill(partEntities);
        return excel;
//        for (Wp05EntityBase entity : partEntities) {
//            Wp05HierarchyInfoEntity wp05Entity = (Wp05HierarchyInfoEntity) entity;
//            Row row = WorkbookUtils.getRow(sheet, rowNum++);
//
//            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
//                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
//            }
//
//            WorkbookUtils.getCell(row, 0).setCellValue(wp05Entity.getModuleParentNo());
//            WorkbookUtils.getCell(row, 1).setCellValue(wp05Entity.getNo());
//            WorkbookUtils.getCell(row, 2).setCellValue(wp05Entity.getDisplayName());
//            WorkbookUtils.getCell(row, 3).setCellValue(wp05Entity.getSubEntityType());
//            WorkbookUtils.getCell(row, 4).setCellValue(wp05Entity.getRevision());
//            WorkbookUtils.getCell(row, 5).setCellValue(wp05Entity.getWorkClass());
//            WorkbookUtils.getCell(row, 6).setCellValue(wp05Entity.getCostCode());
//            WorkbookUtils.getCell(row, 7).setCellValue(wp05Entity.getMemberSize());
//            WorkbookUtils.getCell(row, 8).setCellValue(wp05Entity.getMaterial());
//            WorkbookUtils.getCell(row, 9).setCellValue(wp05Entity.getLengthText());
//            WorkbookUtils.getCell(row, 10).setCellValue(wp05Entity.getWidthText());
//            WorkbookUtils.getCell(row, 11).setCellValue(wp05Entity.getHeightText());
//            WorkbookUtils.getCell(row, 12).setCellValue(wp05Entity.getWeightText());
//            WorkbookUtils.getCell(row, 13).setCellValue(wp05Entity.getPaintCode());
//            WorkbookUtils.getCell(row, 15).setCellValue(wp05Entity.getRemarks2());
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
     * 更新Part实体 材料匹配度。
     *
     * @param batchTask 批处理任务信息
     * @param operator  操作者信息
     * @param project   项目信息
     * @return 批处理执行结果
     */
    @Override
    @Transactional
    public BatchResultDTO updateBomLnMatch(
        BatchTask batchTask,
        OperatorDTO operator,
        Project project,
        boolean initialMatchFlag
    ) {

        BatchResultDTO batchResult = new BatchResultDTO(batchTask);


        return batchResult;

    }


    @Override
    public String toString() {
        return "WP05";
    }
}
