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
import com.ose.tasks.domain.model.repository.wbs.structure.Wp01EntityRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp01HierarchyInfoEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntityTypeInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.structureWbs.Wp01EntryCriteriaDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.entity.wbs.structureEntity.Wp01Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp01EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp01HierarchyInfoEntity;
import com.ose.tasks.entity.wbs.structureEntity.Wp01TaskPackageEntity;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import com.ose.util.BeanUtils;
import com.ose.util.CryptoUtils;
import com.ose.util.FileUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.ose.vo.EntityStatus.*;

@Component
public class Wp01EntityService extends StringRedisService implements BaseWBSEntityInterface<Wp01EntityBase, Wp01EntryCriteriaDTO> {


    private final Wp01EntityRepository wp01EntityRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;


    private final Wp01HierarchyInfoEntityRepository wp01HierarchyInfoEntityRepository;


    private final HierarchyRepository hierarchyRepository;


    private final ProjectNodeRepository projectNodeRepository;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;


    private final BpmEntitySubTypeRepository bpmEntitySubTypeRepository;


    private final BpmEntityTypeRepository bpmEntityTypeRepository;

    private final WBSEntryRepository wbsEntryRepository;

    private final EntityTypeInterface entityTypeService;

    private final PlanInterface planService;


    private final HierarchyInterface hierarchyService;

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
    public Wp01EntityService(
        StringRedisTemplate stringRedisTemplate,
        Wp01EntityRepository wp01EntityRepository,
        Wp01HierarchyInfoEntityRepository wp01HierarchyInfoEntityRepository,
        HierarchyRepository hierarchyRepository,
        ProjectNodeRepository projectNodeRepository,
        BpmEntitySubTypeRepository bpmEntitySubTypeRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmEntityTypeRepository bpmEntityTypeRepository,
        HierarchyInterface hierarchyService,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        WBSEntryRepository wbsEntryRepository, PlanInterface planService,
        EntityTypeInterface entityTypeService) {
        super(stringRedisTemplate);
        this.wp01EntityRepository = wp01EntityRepository;
        this.wp01HierarchyInfoEntityRepository = wp01HierarchyInfoEntityRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.bpmEntitySubTypeRepository = bpmEntitySubTypeRepository;
        this.bpmEntityTypeRepository = bpmEntityTypeRepository;
        this.hierarchyService = hierarchyService;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.planService = planService;
        this.entityTypeService = entityTypeService;
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
    public Page<? extends Wp01EntityBase> search(
        Long orgId,
        Long projectId,
        Wp01EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.STRUCTURE)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findWp01IdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "WP01",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return wp01HierarchyInfoEntityRepository.search(orgId,
                        projectId,
                        criteriaDTO,
                        pageDTO,
                        Wp01HierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return wp01HierarchyInfoEntityRepository.search(orgId,
                    projectId,
                    criteriaDTO,
                    pageDTO,
                    Wp01HierarchyInfoEntity.class);
            }
        } else {
            return wp01HierarchyInfoEntityRepository.search(orgId,
                projectId,
                criteriaDTO,
                pageDTO,
                Wp01HierarchyInfoEntity.class);
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
    public Page<? extends Wp01EntityBase> searchTaskPackageEntities(Long orgId,
                                                                    Long projectId,
                                                                    Wp01EntryCriteriaDTO criteriaDTO,
                                                                    PageDTO pageDTO) {

        return wp01HierarchyInfoEntityRepository.search(orgId,
            projectId,
            criteriaDTO,
            pageDTO,
            Wp01TaskPackageEntity.class);

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
    public Wp01EntityBase get(Long orgId, Long projectId, Long entityId) {

        Wp01EntityBase entity = wp01HierarchyInfoEntityRepository
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


        Wp01EntityBase entity = get(orgId, project.getId(), entityId);

        Wp01Entity wp01Entity = BeanUtils.copyProperties(entity, new Wp01Entity());

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

        wp01Entity.setDeletedBy(operator.getId());
        wp01Entity.setDeletedAt();
        wp01Entity.setStatus(DELETED);
        wp01Entity.setDeleted(true);

        hierarchyService.delete(operator, project, orgId, entityId);
        wp01EntityRepository.save(wp01Entity);

//        Boolean isDeletable = projectNodeRepository.existsByProjectIdAndEntityIdAndIsDeletableIsTrue(project.getId(), entity.getId());
//        if(isDeletable == null || isDeletable)  {

//        } else {
//

//
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
     * @param entity    wp01 实体
     */
    @Override
    public void insert(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Wp01EntityBase entity
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
        if (entity.getEntitySubType() == null) {
            throw new BusinessError("", "business-error:sub entity Type is null");
        }


        if (entity.getLength() == null) {
            entity.setLength(0.0);
        }

        if (entity.getLengthText() == null || "".equals(entity.getLengthText())) {
            entity.setLengthText(entity.getLength() + entity.getLengthUnit().toString());
        }
        if (entity.getHeight() == null) {
            entity.setHeight(0.0);
        }
        if (entity.getHeightText() == null || "".equals(entity.getHeightText())) {
            entity.setHeightText(entity.getHeight() + entity.getHeightUnit().toString());
        }
        if (entity.getWeight() == null) {
            entity.setWeight(0.0);
        }
        if (entity.getWeightText() == null || "".equals(entity.getWeightText())) {
            entity.setWeightText(entity.getWeight() + entity.getWeightUnit().toString());
        }
        if (entity.getWidth() == null) {
            entity.setWidth(0.0);
        }
        if (entity.getWidthText() == null) {
            entity.setWidthText(entity.getWidth() + entity.getWidthUnit().toString());
        }

        entity.setEntityType("WP01");


        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        Wp01Entity wp01Entity = BeanUtils.copyProperties(entity, new Wp01Entity());
        if (wp01Entity.getId() == null) {
            wp01Entity.setId(CryptoUtils.uniqueDecId());
        }

        wp01EntityRepository.save(wp01Entity);


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
        projectNode.setModuleType(entity.getEntitySubType());
        projectNodeRepository.save(projectNode);
        projectNodeRepository.setParentEntityIdOnStructureEntity(projectId, "wp01", wp01Entity.getId());

    }

    /**
     * 更新 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    wp01 实体
     */
    @Override
    public Wp01EntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Wp01EntityBase entity
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
        Wp01Entity wp01Entity = BeanUtils.copyProperties(entity, new Wp01Entity());
        wp01EntityRepository.save(wp01Entity);
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

        if (wp01EntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
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
     * 设置实体父级
     *
     * @param parentNodeNo 父级节点号
     * @param entity       需要设置父级的实体
     * @param projectId    项目ID
     * @param orgId        组织信息
     * @param operator     操作者信息
     */
    @Override
    public void setParentInfo(String parentNodeNo,
                              Wp01EntityBase entity,
                              Long projectId,
                              Long orgId,
                              OperatorDTO operator) {

        List<HierarchyNode> parentNodes = hierarchyRepository
            .findByNoAndProjectIdAndDeletedIsFalse(
                parentNodeNo,
                projectId
            );

        if (parentNodes == null || parentNodes.size() == 0) {

            ProjectNode projectNode = new ProjectNode();
            projectNode.setNo(entity.getNo());
            projectNode.setCompanyId(entity.getCompanyId());
            projectNode.setEntityId(entity.getId());
            projectNode.setEntityType(entity.getEntityType());
            projectNode.setEntitySubType(entity.getEntitySubType());
            projectNode.setEntityBusinessType(entity.getBusinessType());
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


            BpmEntityType bpmEntityType =
                bpmEntityTypeRepository.findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(
                    projectNode.getEntityType(), projectId, orgId, CategoryTypeTag.READONLY.name(), EntityStatus.ACTIVE)
                    .orElse(null);

            if (null == bpmEntityType) {
                throw new ValidationError("business-error: EntityType is INVALID.");
            }

            BpmEntitySubType bpmEntitySubType = bpmEntitySubTypeRepository.findByProjectIdAndNameEn(
                projectId, projectNode.getEntitySubType());

            if (null == bpmEntitySubType) {
                throw new ValidationError("validation-error: 实体子类型 is INVALID.");
            }
            projectNode.setDiscipline(DisciplineCode.STRUCTURE.name());

            projectNodeRepository.save(projectNode);
            throw new BusinessError("parent '" + parentNodeNo + "' doesn't exit");

        }

        for (HierarchyNode parentNode : parentNodes) {


            // 上级节点若为实体则检查上级节点是否接受该实体类型的子节点
            BpmEntityType pEntityType = entityTypeService.getBpmEntityType(projectId, parentNode.getEntityType());
            if (!pEntityType
                .isParentOf(entity.getEntityType())
            ) {
                // TODO
                throw new ValidationError(
                    "parent '"
                        + parentNode.getNo()
                        + "' cannot be parent of '"
                        + entity.getNo()
                        + "'"
                );
            }


            HierarchyNode lastSiblingNode = hierarchyRepository
                .findFirstByProjectIdAndParentIdAndDeletedIsFalseOrderBySortDesc(
                    projectId,
                    parentNode.getId()
                )
                .orElse(null);


            int sortWeight = lastSiblingNode != null
                ? lastSiblingNode.getSort()
                : parentNode.getSort();


            hierarchyRepository.reorder(projectId, sortWeight, 1);


            HierarchyNode node = getEntityNode(
                projectId,
                parentNode.getId(),
                entity.getNo()
            );


            if (node == null) {
                node = new HierarchyNode();
                node.setNode(
                    projectNodeRepository
                        .findByProjectIdAndNoAndDeletedIsFalse(
                            projectId,
                            entity.getNo()
                        )
                        .orElse(null)
                );
                node.setCreatedAt();
                node.setCreatedBy(operator.getId());
                node.setNew(true);
            }


            node.setParentNode(parentNode);
            node.setNo(entity.getNo(), parentNode.getNo());
            node.setEntityType(entity.getEntityType());
            node.setEntitySubType(entity.getEntitySubType());
            node.getNode().setEntityBusinessType(entity.getBusinessType());
            node.setEntityId(entity.getId());
            node.setSort(sortWeight + 1);
            node.setLastModifiedAt();
            node.setLastModifiedBy(operator.getId());
            node.setStatus(ACTIVE);


            BpmEntityType bpmEntityType =
                bpmEntityTypeRepository.findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(
                    node.getEntityType(), projectId, orgId, CategoryTypeTag.READONLY.name(), EntityStatus.ACTIVE)
                    .orElse(null);

            if (null == bpmEntityType) {
                throw new ValidationError("business-error: EntityType is INVALID.");
            }

            BpmEntitySubType bpmEntitySubType = bpmEntitySubTypeRepository.findByProjectIdAndNameEn(
                projectId, node.getEntitySubType());
            if (null == bpmEntitySubType) {
                throw new ValidationError("validation-error: 实体子类型 is INVALID.");
            }
            node.getNode().setDiscipline(DisciplineCode.STRUCTURE.name());
            projectNodeRepository.save(node.getNode());
            hset(RedisKey.PROJECT_USER_KEY.getDisplayName(), node.getNo(), node.getNode().getId().toString());
            hierarchyRepository.save(node);


            projectNodeRepository.setParentEntityIdOnStructureEntity(projectId, "wp01", entity.getId());
        }
    }

    /**
     * 取得模块图纸信息。
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
     * 取得模块实体对应的材料信息。
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

        Wp01Entity entity = wp01EntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(id,
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
                                 Wp01EntryCriteriaDTO criteriaDTO,
                                 Long operatorId) {

        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/export-project-entities-wp01.xlsx"),
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

        Sheet sheet = workbook.getSheet("wp01");

        int rowNum = DATA_START_ROW;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends Wp01EntityBase> wp01Entities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();
        for (Wp01EntityBase entity : wp01Entities) {
            Wp01HierarchyInfoEntity wp01Entity = (Wp01HierarchyInfoEntity) entity;
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
            if (entity.getEntitySubType() != null) {
                WorkbookUtils.getCell(row, 2).setCellValue(entity.getEntitySubType());
            }
            if (entity.getDisplayName() != null) {
                //描述
                WorkbookUtils.getCell(row, 3).setCellValue(entity.getDisplayName());
            }
            if (entity.getDeleted() != null) {
                WorkbookUtils.getCell(row, 4).setCellValue(entity.getDeleted());
            }
            if (entity.getDiscipline() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getDiscipline());
            }
            if (entity.getOrgId() != null) {
                WorkbookUtils.getCell(row, 6).setCellValue(entity.getOrgId());
            }
            if (entity.getProjectId() != null) {
                WorkbookUtils.getCell(row, 7).setCellValue(entity.getProjectId());
            }
            if (entity.getRevision() != null) {
                WorkbookUtils.getCell(row, 8).setCellValue(entity.getRevision());
            }
            if (entity.getLength() != null) {
                WorkbookUtils.getCell(row, 9).setCellValue(entity.getLength());
            }
            if (entity.getWidth() != null) {
                WorkbookUtils.getCell(row, 10).setCellValue(entity.getWidth());
            }
            if (entity.getHeight() != null) {
                WorkbookUtils.getCell(row, 11).setCellValue(entity.getHeight());
            }
            if (entity.getWeight() != null) {
                WorkbookUtils.getCell(row, 12).setCellValue(entity.getWeight());
            }
            if (entity.getLengthText() != null) {
                WorkbookUtils.getCell(row, 13).setCellValue(entity.getLengthText());
            }
            if (entity.getWidthText() != null) {
                WorkbookUtils.getCell(row, 14).setCellValue(entity.getWidthText());
            }
            if (entity.getHeightText() != null) {
                WorkbookUtils.getCell(row, 15).setCellValue(entity.getHeightText());
            }
            if (entity.getWeightText() != null) {
                WorkbookUtils.getCell(row, 16).setCellValue(entity.getWeightText());
            }
            if (entity.getQrCode() != null) {
                WorkbookUtils.getCell(row, 17).setCellValue(entity.getQrCode());
            }
            if (entity.getRemarks() != null) {
                WorkbookUtils.getCell(row, 18).setCellValue(entity.getRemarks());
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
    public Wp01EntityBase getByQrCode(Long orgId, Long projectId, String qrcode) {

        Wp01EntityBase entity = null;


        if (entity == null) {
            throw new NotFoundError();
        }

        return entity;

    }

    @Override
    public String toString() {
        return "WP01";
    }

}
