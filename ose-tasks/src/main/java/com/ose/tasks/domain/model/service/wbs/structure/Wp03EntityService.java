package com.ose.tasks.domain.model.service.wbs.structure;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.HierarchyRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp03EntityRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp03HierarchyInfoEntityRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageEntityRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.structureWbs.Wp03EntryCriteriaDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.entity.wbs.structureEntity.Wp03Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp03EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp03HierarchyInfoEntity;
import com.ose.tasks.entity.wbs.structureEntity.Wp03TaskPackageEntity;
import com.ose.util.*;
import com.ose.vo.DisciplineCode;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;

@Component
public class Wp03EntityService extends StringRedisService implements BaseWBSEntityInterface<Wp03EntityBase, Wp03EntryCriteriaDTO> {


    private final Wp03EntityRepository wp03EntityRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;


    private final Wp03HierarchyInfoEntityRepository wp03HierarchyInfoEntityRepository;


    private final HierarchyRepository hierarchyRepository;


    private final TaskPackageEntityRelationRepository taskPackageEntityRelationRepository;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;


    private final ProjectNodeRepository projectNodeRepository;


//    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;


//    private final BpmCuttingEntityRepository bpmCuttingEntityRepository;


    private final HierarchyInterface hierarchyService;

    private final WBSEntryRepository wbsEntryRepository;

    private final PlanInterface planService;

    @Value("${application.files.temporary}")
    private String temporaryDir;


    private static final int DATA_START_ROW = 3;

    private static final int TEMPLATE_ROW_COUNT = 20;

    private static final String YES = "Y";

    private static final String NO = "N";


    /**
     * 构造方法。
     */
    @Autowired
    public Wp03EntityService(
        StringRedisTemplate stringRedisTemplate,
        Wp03EntityRepository wp03EntityRepository,
        HierarchyRepository hierarchyRepository,
        Wp03HierarchyInfoEntityRepository wp03HierarchyInfoEntityRepository,
        ProjectNodeRepository projectNodeRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        TaskPackageEntityRelationRepository taskPackageEntityRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
//        BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
//        BpmCuttingEntityRepository bpmCuttingEntityRepository,
        HierarchyInterface hierarchyService,
        PlanInterface planService,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository, WBSEntryRepository wbsEntryRepository) {
        super(stringRedisTemplate);
        this.wp03EntityRepository = wp03EntityRepository;
        this.wp03HierarchyInfoEntityRepository = wp03HierarchyInfoEntityRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.taskPackageEntityRelationRepository = taskPackageEntityRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
//        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
//        this.bpmCuttingEntityRepository = bpmCuttingEntityRepository;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.wbsEntryRepository = wbsEntryRepository;
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
    public Page<? extends Wp03EntityBase> search(
        Long orgId,
        Long projectId,
        Wp03EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.STRUCTURE)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findWp03IdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "WP03",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return wp03HierarchyInfoEntityRepository.search(orgId,
                        projectId,
                        criteriaDTO,
                        pageDTO,
                        Wp03HierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return wp03HierarchyInfoEntityRepository.search(orgId,
                    projectId,
                    criteriaDTO,
                    pageDTO,
                    Wp03HierarchyInfoEntity.class);
            }
        } else {
            return wp03HierarchyInfoEntityRepository.search(orgId,
                projectId,
                criteriaDTO,
                pageDTO,
                Wp03HierarchyInfoEntity.class);
        }


    }


    /**
     * 查询 任务包添加的 WBS 实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageDTO   分页参数
     * @return WBS 实体分页数据
     */
    @Override
    public Page<? extends Wp03EntityBase> searchTaskPackageEntities(Long orgId,
                                                                    Long projectId,
                                                                    Wp03EntryCriteriaDTO criteriaDTO,
                                                                    PageDTO pageDTO) {

        return wp03HierarchyInfoEntityRepository.search(orgId,
            projectId,
            criteriaDTO,
            pageDTO,
            Wp03TaskPackageEntity.class);

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
    public Wp03EntityBase get(Long orgId, Long projectId, Long entityId) {

        Wp03EntityBase entity = wp03HierarchyInfoEntityRepository
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


        Wp03EntityBase entity = get(orgId, project.getId(), entityId);

        Wp03Entity wp03Entity = BeanUtils.copyProperties(entity, new Wp03Entity());

        if (haveChildren(project.getId(), entity)) {
            throw new ValidationError("Entity have children ,Please delete children first");
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
            taskPackageList.get(0).setStatus(DELETED);
            taskPackageEntityRelationRepository.save(taskPackageList.get(0));
        }

        wp03Entity.setDeletedBy(operator.getId());
        wp03Entity.setDeletedAt();
        wp03Entity.setStatus(DELETED);
        wp03Entity.setDeleted(true);

        hierarchyService.delete(operator, project, orgId, entityId);
//        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entityId);

        wp03EntityRepository.save(wp03Entity);

    }

    /**
     * 插入 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    Pipe-piece 实体
     */
    @Override
    public void insert(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Wp03EntityBase entity
    ) {
        if (entity.getNo() == null || "".equals(entity.getNo())) {
            throw new BusinessError("", "business-error: No is  not null.");
        }
        if (existsByEntityNo(entity.getNo(), projectId)) {
            throw new BusinessError("", "business-error: wp03 no ALREADY EXISTS.");
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
        if (entity.getWeight() == null) {
            entity.setWeight(0.0);
        }

        if (entity.getWeightText() == null) {
            entity.setWeightText(entity.getWeight() + entity.getWeightUnit().toString());
        }
        if (entity.getWidth() == null) {
            entity.setWidth(0.0);
        }
        if (entity.getWidthText() == null) {
            entity.setWidthText(entity.getWidth() + entity.getWidthUnit().toString());
        }



        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        entity.setEntityType("WP03");
        Wp03Entity wp03Entity = BeanUtils.copyProperties(entity, new Wp03Entity());
        if (wp03Entity.getId() == null) {
            wp03Entity.setId(CryptoUtils.uniqueDecId());
        }

        wp03EntityRepository.save(wp03Entity);


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
        projectNodeRepository.setParentEntityIdOnStructureEntity(projectId, "wp03", wp03Entity.getId());
    }

    /**
     * 更新 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    Pipe-piece 实体
     */
    @Override
    public Wp03EntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Wp03EntityBase entity
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
        if (entity.getWeight() == null) {
            entity.setWeight(0.0);
        }
        if (entity.getWeightText() == null) {
            entity.setWeightText(entity.getWeight() + entity.getWeightUnit().toString());
        }
        if (entity.getWidth() == null) {
            entity.setWidth(0.0);
        }
        if (entity.getWidthText() == null) {
            entity.setWidthText(entity.getWidth() + entity.getWidthUnit().toString());
        }

        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        Wp03Entity wp03Entity = BeanUtils.copyProperties(entity, new Wp03Entity());
        wp03EntityRepository.save(wp03Entity);
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

        if (wp03EntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
            return true;
        }

        return false;
    }

    private HierarchyNode getEntityNode(
        Long projectId,
        Long parentId,
        String nodeNo
    ) {

        HierarchyNode node = hierarchyRepository
            .findByNoAndProjectIdAndParentIdAndDeletedIsFalse(
                nodeNo,
                projectId,
                parentId
            )
            .orElse(null);

        if (node == null) {
            return null;
        }

        return node;
    }


    /**
     * 取得构件图纸信息。
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


        List<HierarchyNode> hierarchyNodes = hierarchyRepository.findByEntityIdAndDeletedIsFalse(
            id,
            orgId,
            projectId);
        if (hierarchyNodes == null || hierarchyNodes.size() == 0) {
            throw new BusinessError("Pipe-piece has NO hierarchyNode.");
        } else if (hierarchyNodes.size() > 1) {
            throw new BusinessError("Pipe-piece has TOO many hierarchyNode.");
        }


        ProjectNode projectNode = projectNodeRepository.findByHierarchyIdAndProjectIdAndOrgId(
            hierarchyNodes.get(0).getParentId(),
            projectId,
            orgId)
            .orElse(null);
        if (projectNode == null) {
            return null;
        }
        return null;
    }

    /**
     * 取得构件实体对应的材料信息。
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

        Wp03Entity entity = wp03EntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(id,
            orgId,
            projectId).orElse(null);
        List<MaterialInfoDTO> materialInfoDTOS = new ArrayList<>();
        if (entity != null) {
            MaterialInfoDTO materialInfoDTO = new MaterialInfoDTO();


            materialInfoDTO.setQuantity(String.valueOf(entity.getLength()) + " MM");
            materialInfoDTOS.add(materialInfoDTO);
        }

        return materialInfoDTOS;
    }

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param operatorId  操作者ID
     * @return 实体下载临时文件
     */
    @Override
    public File saveDownloadFile(Long orgId,
                                 Long projectId,
                                 Wp03EntryCriteriaDTO criteriaDTO,
                                 Long operatorId) {

        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/export-project-entities-wp03.xlsx"),
            temporaryDir,
            operatorId.toString()
        );


        File excel;
        Workbook workbook;

        try {
            excel = new File(temporaryDir, temporaryFileName);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }

        Sheet sheet = workbook.getSheet("wp03");

        int rowNum = DATA_START_ROW;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends Wp03EntityBase> wp03Entities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();
        for (Wp03EntityBase entity : wp03Entities) {
            Wp03HierarchyInfoEntity wp03Entity = (Wp03HierarchyInfoEntity) entity;
            Row row = WorkbookUtils.getRow(sheet, rowNum++);

            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
            }





            WorkbookUtils.getCell(row, 2).setCellValue(wp03Entity.getNo());

            WorkbookUtils.getCell(row, 6).setCellValue(wp03Entity.getRevision());
            WorkbookUtils.getCell(row, 9).setCellValue(wp03Entity.getLengthText());
            WorkbookUtils.getCell(row, 10).setCellValue(wp03Entity.getWidthText());
            WorkbookUtils.getCell(row, 11).setCellValue(wp03Entity.getHeightText());
            WorkbookUtils.getCell(row, 12).setCellValue(wp03Entity.getWeightText());
            WorkbookUtils.getCell(row, 17).setCellValue(wp03Entity.getPaintCode());
            WorkbookUtils.getCell(row, 18).setCellValue(wp03Entity.getRemarks());


        }

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
            return excel;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
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

    @Override
    public String toString() {
        return "WP03";
    }
}
