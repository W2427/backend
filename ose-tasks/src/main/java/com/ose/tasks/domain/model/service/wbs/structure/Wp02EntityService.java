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
import com.ose.tasks.domain.model.repository.wbs.structure.Wp02EntityRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp02HierarchyInfoEntityRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageEntityRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.structureWbs.Wp02EntryCriteriaDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.entity.wbs.structureEntity.Wp02Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp02EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp02HierarchyInfoEntity;
import com.ose.tasks.entity.wbs.structureEntity.Wp02TaskPackageEntity;
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
public class Wp02EntityService extends StringRedisService implements BaseWBSEntityInterface<Wp02EntityBase, Wp02EntryCriteriaDTO> {


    private final Wp02EntityRepository wp02EntityRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;


    private final Wp02HierarchyInfoEntityRepository wp02HierarchyInfoEntityRepository;


    private final HierarchyRepository hierarchyRepository;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final TaskPackageEntityRelationRepository taskPackageEntityRelationRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final ProjectNodeRepository projectNodeRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;


    private final WBSEntryRepository wbsEntryRepository;


//    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;


//    private final BpmCuttingEntityRepository bpmCuttingEntityRepository;


    private final HierarchyInterface hierarchyService;


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
    public Wp02EntityService(
        StringRedisTemplate stringRedisTemplate,
        Wp02EntityRepository wp02EntityRepository,
        Wp02HierarchyInfoEntityRepository wp02HierarchyInfoEntityRepository,
        HierarchyRepository hierarchyRepository,
        ProjectNodeRepository projectNodeRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
//        BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
//        BpmCuttingEntityRepository bpmCuttingEntityRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        TaskPackageEntityRelationRepository taskPackageEntityRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        HierarchyInterface hierarchyService,
        PlanInterface planService,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository, WBSEntryRepository wbsEntryRepository) {
        super(stringRedisTemplate);
        this.wp02EntityRepository = wp02EntityRepository;
        this.wp02HierarchyInfoEntityRepository = wp02HierarchyInfoEntityRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.taskPackageEntityRelationRepository = taskPackageEntityRelationRepository;
//        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
//        this.bpmCuttingEntityRepository = bpmCuttingEntityRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.hierarchyService = hierarchyService;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.planService = planService;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
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
    public Page<? extends Wp02EntityBase> search(
        Long orgId,
        Long projectId,
        Wp02EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {

        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.STRUCTURE)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findWp02IdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "WP02",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return wp02HierarchyInfoEntityRepository.search(orgId,
                        projectId,
                        criteriaDTO,
                        pageDTO,
                        Wp02HierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return wp02HierarchyInfoEntityRepository.search(orgId,
                    projectId,
                    criteriaDTO,
                    pageDTO,
                    Wp02HierarchyInfoEntity.class);
            }
        } else {
            return wp02HierarchyInfoEntityRepository.search(orgId,
                projectId,
                criteriaDTO,
                pageDTO,
                Wp02HierarchyInfoEntity.class);
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
    public Page<? extends Wp02EntityBase> searchTaskPackageEntities(Long orgId,
                                                                    Long projectId,
                                                                    Wp02EntryCriteriaDTO criteriaDTO,
                                                                    PageDTO pageDTO) {

        return wp02HierarchyInfoEntityRepository.search(orgId,
            projectId,
            criteriaDTO,
            pageDTO,
            Wp02TaskPackageEntity.class);

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
    public Wp02EntityBase get(Long orgId, Long projectId, Long entityId) {

        Wp02EntityBase entity = wp02HierarchyInfoEntityRepository
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


        Wp02EntityBase entity = get(orgId, project.getId(), entityId);

        Wp02Entity wp02Entity = BeanUtils.copyProperties(entity, new Wp02Entity());

        Boolean isDeletable = projectNodeRepository.existsByProjectIdAndEntityIdAndIsDeletableIsTrue(project.getId(), entity.getId());
        if (isDeletable == null || isDeletable) {

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


            wp02Entity.setDeletedBy(operator.getId());
            wp02Entity.setDeletedAt();
            wp02Entity.setStatus(DELETED);
            wp02Entity.setDeleted(true);

            List<TaskPackageEntityRelation> taskPackageList = taskPackageEntityRelationRepository.findByOrgIdAndProjectIdAndEntityId(project.getOrgId(), project.getId(), entityId);
            if (taskPackageList.size() > 0) {
                taskPackageList.get(0).setStatus(DELETED);
                taskPackageEntityRelationRepository.save(taskPackageList.get(0));
            }

            hierarchyService.delete(operator, project, orgId, entityId);
//            bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entityId);
            wp02EntityRepository.save(wp02Entity);
        }

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
        Wp02EntityBase entity
    ) {
        if (entity.getNo() == null || "".equals(entity.getNo())) {
            throw new BusinessError("", "business-error: No is  not null.");
        }
        if (existsByEntityNo(entity.getNo(), projectId)) {
            throw new BusinessError("", "business-error: section no ALREADY EXISTS.");
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


        entity.setEntityType("WP02");

        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        Wp02Entity wp02Entity = BeanUtils.copyProperties(entity, new Wp02Entity());
        wp02EntityRepository.save(wp02Entity);

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
        projectNodeRepository.setParentEntityIdOnStructureEntity(projectId, "wp02", wp02Entity.getId());
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
    public Wp02EntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Wp02EntityBase entity
    ) {
        if (entity.getNo() == null || "".equals(entity.getNo())) {
            throw new BusinessError("", "business-error: section is  not null.");
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
        Wp02Entity wp02Entity = BeanUtils.copyProperties(entity, new Wp02Entity());
        if (wp02Entity.getId() == null) {
            wp02Entity.setId(CryptoUtils.uniqueDecId());
        }
        wp02EntityRepository.save(wp02Entity);
        projectNodeRepository.setParentEntityIdOnStructureEntity(projectId, "wp02", wp02Entity.getId());
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

        if (wp02EntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
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
        } else if (hierarchyNodes.size() > 2) {
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

        Wp02Entity entity = wp02EntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(id,
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
                                 Wp02EntryCriteriaDTO criteriaDTO,
                                 Long operatorId) {

        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/export-project-entities-wp02.xlsx"),
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

        Sheet sheet = workbook.getSheet("wp02");

        int rowNum = DATA_START_ROW;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends Wp02EntityBase> wp02Entities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();
        for (Wp02EntityBase entity : wp02Entities) {
            Wp02HierarchyInfoEntity wp02Entity = (Wp02HierarchyInfoEntity) entity;
            Row row = WorkbookUtils.getRow(sheet, rowNum++);

            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
            }


            if (entity.getParentNo() != null) {
                // 父级层级id
                WorkbookUtils.getCell(row, 0).setCellValue(entity.getParentNo());
            }
            if (entity.getNo() != null) {
                // 模块号
                WorkbookUtils.getCell(row, 1).setCellValue(entity.getNo());
            }
            if (entity.getDisplayName() != null) {
                WorkbookUtils.getCell(row, 2).setCellValue(entity.getDisplayName());
            }
            if (entity.getDeleted() != null) {
                WorkbookUtils.getCell(row, 3).setCellValue(entity.getDeleted());
            }
            if (entity.getRevision() != null) {
                WorkbookUtils.getCell(row, 4).setCellValue(entity.getRevision());
            }
            if (entity.getOrgId() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getOrgId());
            }
            if (entity.getProjectId() != null) {
                WorkbookUtils.getCell(row, 6).setCellValue(entity.getProjectId());
            }
            if (entity.getWorkClass() != null) {
                WorkbookUtils.getCell(row, 7).setCellValue(entity.getWorkClass());
            }
            if (entity.getLength() != null) {
                WorkbookUtils.getCell(row, 8).setCellValue(entity.getLength());
            }
            if (entity.getWidth() != null) {
                WorkbookUtils.getCell(row, 9).setCellValue(entity.getWidth());
            }
            if (entity.getHeight() != null) {
                WorkbookUtils.getCell(row, 10).setCellValue(entity.getHeight());
            }
            if (entity.getWeight() != null) {
                WorkbookUtils.getCell(row, 11).setCellValue(entity.getWeight());
            }
            if (entity.getLengthText() != null) {
                WorkbookUtils.getCell(row, 12).setCellValue(entity.getLengthText());
            }
            if (entity.getWidthText() != null) {
                WorkbookUtils.getCell(row, 13).setCellValue(entity.getWidthText());
            }
            if (entity.getHeightText() != null) {
                WorkbookUtils.getCell(row, 14).setCellValue(entity.getHeightText());
            }
            if (entity.getWeightText() != null) {
                WorkbookUtils.getCell(row, 15).setCellValue(entity.getWeightText());
            }
            if (entity.getQrCode() != null) {
                WorkbookUtils.getCell(row, 16).setCellValue(entity.getQrCode());
            }
            if (entity.getRemarks() != null) {
                WorkbookUtils.getCell(row, 17).setCellValue(entity.getRemarks());
            }
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
        return "WP02";
    }

}
