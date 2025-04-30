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
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.repository.wbs.piping.ComponentEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ComponentHierarchyInfoEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ISOEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.SpoolEntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.dto.ComponentEntryCriteriaDTO;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;

@Component
public class ComponentEntityService implements ComponentEntityInterface {


    private final ComponentEntityRepository componentEntityRepository;

    private final ComponentHierarchyInfoEntityRepository componentHierarchyInfoEntityRepository;

    private ISOEntityRepository isoEntityRepository;


    private final SpoolEntityRepository spoolEntityRepository;


//    private final EntityQrCodeRepository entityQrCodeRepository;


    private final HierarchyRepository hierarchyRepository;


    private ProjectNodeRepository projectNodeRepository;


    private final EntitySubTypeRuleRepository entityCategoryRuleRepository;


    private final BpmEntitySubTypeRepository bpmEntityCategoryRepository;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;


    private final BpmEntityTypeRepository bpmEntityCategoryTypeRepository;


    private final SubDrawingHistoryRepository subDrawingHistoryRepository;

    private Set<EntitySubTypeRule> componentEntityCategoryRules;

    private final WBSEntryRepository wbsEntryRepository;

    private final SubDrawingRepository subDrawingRepository;


    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;


    private HierarchyInterface hierarchyService;


    private final PlanInterface planService;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private static final String PIPE = "PIPE";
    private static final String SEPARATOR = ";";
    private static final String FITTING = "FITTING";
    private static final String OTHERS_ON_SPOOL = "OTHERS_ON_SPOOL";
    private static final String OTHERS_ON_ISO = "OTHERS_ON_ISO";

    @Value("${application.files.temporary}")
    private String temporaryDir;


    private static final int DATA_START_ROW = 3;

    private static final int TEMPLATE_ROW_COUNT = 20;

    /**
     * 构造方法。
     */
    @Autowired
    public ComponentEntityService(
        ComponentEntityRepository componentEntityRepository,
        HierarchyRepository hierarchyRepository,
        ComponentHierarchyInfoEntityRepository componentHierarchyInfoEntityRepository,
        ProjectNodeRepository projectNodeRepository,
        ISOEntityRepository isoEntityRepository,
//        EntityQrCodeRepository entityQrCodeRepository,
        SpoolEntityRepository spoolEntityRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmEntitySubTypeRepository bpmEntityCategoryRepository,
        BpmEntityTypeRepository bpmEntityCategoryTypeRepository,
        EntitySubTypeRuleRepository entityCategoryRuleRepository,
        SubDrawingHistoryRepository subDrawingHistoryRepository,
        WBSEntryRepository wbsEntryRepository, SubDrawingRepository subDrawingRepository, BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
        HierarchyInterface hierarchyService,
        PlanInterface planService,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository) {
        this.componentEntityRepository = componentEntityRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.componentHierarchyInfoEntityRepository = componentHierarchyInfoEntityRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.isoEntityRepository = isoEntityRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
//        this.entityQrCodeRepository = entityQrCodeRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.spoolEntityRepository = spoolEntityRepository;
        this.bpmEntityCategoryRepository = bpmEntityCategoryRepository;
        this.bpmEntityCategoryTypeRepository = bpmEntityCategoryTypeRepository;
        this.entityCategoryRuleRepository = entityCategoryRuleRepository;
        this.subDrawingHistoryRepository = subDrawingHistoryRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
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
    public Page<? extends ComponentEntityBase> search(
        Long orgId,
        Long projectId,
        ComponentEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {






        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.PIPING)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findComponentIdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "COMPONENT",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return componentHierarchyInfoEntityRepository.search(orgId,
                        projectId,
                        criteriaDTO,
                        pageDTO,
                        ComponentHierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return componentHierarchyInfoEntityRepository.search(orgId,
                    projectId,
                    criteriaDTO,
                    pageDTO,
                    ComponentHierarchyInfoEntity.class);
            }
        } else {
            return componentHierarchyInfoEntityRepository.search(orgId,
                projectId,
                criteriaDTO,
                pageDTO,
                ComponentHierarchyInfoEntity.class);
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
    public ComponentEntityBase get(Long orgId, Long projectId, Long entityId) {

        ComponentEntityBase entity = componentHierarchyInfoEntityRepository
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

        ComponentEntityBase entity = get(orgId, project.getId(), entityId);

        ComponentEntity componentEntity = BeanUtils.copyProperties(entity, new ComponentEntity());

//        Boolean isDeletable = projectNodeRepository.existsByProjectIdAndEntityIdAndIsDeletableIsTrue(project.getId(), entity.getId());
//        if(isDeletable == null || isDeletable)  {

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

        componentEntity.setDeletedBy(operator.getId());
        componentEntity.setDeletedAt();
        componentEntity.setStatus(DELETED);
        componentEntity.setDeleted(true);

        hierarchyService.delete(operator, project, orgId, entityId);
        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entityId);
//        } else {
//
//            componentEntity.setCancelled(true);
//            componentEntity.setLastModifiedAt();
//            componentEntity.setLastModifiedBy(operator.getId());
//            componentEntity.setStatus(EntityStatus.CANCEL);
//
//
//            hierarchyService.cancel(operator, project, orgId, entityId);
//            bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.CANCEL, project.getId(), entityId);
//
//        }

        componentEntityRepository.save(componentEntity);




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
        ComponentEntityBase entity
    ) {

        ComponentEntity componentEntity = BeanUtils.copyProperties(entity, new ComponentEntity());




        componentEntity.setCreatedBy(operator.getId());
        componentEntity.setCreatedAt();
        componentEntity.setLastModifiedBy(operator.getId());
        componentEntity.setLastModifiedAt();
        componentEntity.setProjectId(projectId);
        componentEntity.setStatus(ACTIVE);
        componentEntity.setDeleted(false);
        componentEntity.setVersion(componentEntity.getLastModifiedAt().getTime());

        componentEntityRepository.save(componentEntity);
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
    public ComponentEntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        ComponentEntityBase entity
    ) {

        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());

        ComponentEntity componentEntity = BeanUtils.copyProperties(entity, new ComponentEntity());
        componentEntityRepository.save(componentEntity);

        ProjectNode projectNode = projectNodeRepository.
            findByProjectIdAndEntityIdAndDeleted(projectId, componentEntity.getId(), false).
            orElse(null);
        if (projectNode != null) {
            if (projectNode.getEntitySubType() != null && !projectNode.getEntitySubType().equalsIgnoreCase(componentEntity.getEntitySubType())) {
                projectNode.setEntitySubType(componentEntity.getEntitySubType());
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

        if (componentEntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
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

//        if (node.getNodeType() != ENTITY) {
//
//            throw new ValidationError(
//                "existing node " + node.getNo() + " is not an entity"
//            );
//        }

        return node;
    }

    @Override
    public void setParentInfo(String parentNodeNo,
                              ComponentEntityBase entity,
                              Long projectId,
                              Long orgId,
                              OperatorDTO operator) {


        List<HierarchyNode> parentNodes = hierarchyRepository
            .findByNoAndProjectIdAndDeletedIsFalse(
                parentNodeNo,
                projectId
            );

        if (parentNodes == null || parentNodes.size() == 0) {

            if (spoolEntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(parentNodeNo, projectId)
                || isoEntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(parentNodeNo, projectId)) {


                ProjectNode projectNode = new ProjectNode();

                projectNode.setNo(entity.getNo());
                projectNode.setDisplayName(entity.getDisplayName());
                projectNode.setCompanyId(entity.getCompanyId());
                projectNode.setEntityId(entity.getId());
                projectNode.setEntityType(WBSEntityType.COMPONENT.name());
                projectNode.setEntityBusinessType(entity.getEntityBusinessType());
                projectNode.setEntitySubType(entity.getComponentEntityType());
//                projectNode.setNodeType(HierarchyNodeType.ENTITY);
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


                BpmEntityType bpmEntityCategoryType =
                    bpmEntityCategoryTypeRepository.findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(
                        projectNode.getEntityType(), projectId, orgId, CategoryTypeTag.READONLY.name(), ACTIVE)
                        .orElse(null);

                if (null == bpmEntityCategoryType) {
                    throw new ValidationError("business-error: EntityType is INVALID.");
                }

                BpmEntitySubType bpmEntityCategory = bpmEntityCategoryRepository.findByProjectIdAndNameEn(
                    projectId, projectNode.getEntitySubType());

                if (null == bpmEntityCategory) {
                    throw new ValidationError("validation-error: 实体子类型 is INVALID.");
                }
                return;
            } else {
                throw new ValidationError("parent '" + parentNodeNo + "' doesn't exit");
            }
        }

        for (HierarchyNode parentNode : parentNodes) {


//            if (ENTITY == parentNode.getNodeType()
//                && !parentNode
//                .getEntityType()
//                .isParentOf(entity.getWbsEntityType())
//            ) {
//
//                throw new ValidationError(
//                    "parent '"
//                        + parentNode.getNo()
//                        + "' cannot be parent of '"
//                        + entity.getMaterialCode()
//                        + "'"
//                );
//            }


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
//                node.setNodeType(ENTITY);
                node.setCreatedAt();
                node.setCreatedBy(operator.getId());
                node.setNew(true);
            }


            node.setParentNode(parentNode);
            node.setNo(entity.getNo(), parentNode.getNo());
            node.setEntityType(entity.getEntityType());
            node.setEntitySubType(entity.getComponentEntityType());
            node.getNode().setEntityBusinessType(entity.getEntityBusinessType());
            node.setEntityId(entity.getId());
            node.setSort(sortWeight + 1);
            node.setLastModifiedAt();
            node.setLastModifiedBy(operator.getId());
            node.setStatus(ACTIVE);
            node.getNode().setDiscipline(DisciplineCode.PIPING.name());

            projectNodeRepository.save(node.getNode());
            hierarchyRepository.save(node);


            BpmEntityType bpmEntityCategoryType =
                bpmEntityCategoryTypeRepository.findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(
                    node.getEntityType(), projectId, orgId, CategoryTypeTag.READONLY.name(), ACTIVE)
                    .orElse(null);

            if (null == bpmEntityCategoryType) {
                throw new ValidationError("business-error: EntityType is INVALID.");
            }

            BpmEntitySubType bpmEntityCategory = bpmEntityCategoryRepository.findByProjectIdAndNameEn(
                projectId, node.getEntitySubType());
            if (null == bpmEntityCategory) {
                throw new ValidationError("validation-error: 实体子类型 is INVALID.");
            }
        }
    }

    /**
     * 根据规则设定管件实体类型
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param parentNodeNo 父级节点号
     * @param shortCode    管件的短代码
     * @return
     */
    @Override
    public Map<String, String> setEntityTypeByRules(Long orgId,
                                                    Long projectId,
                                                    String parentNodeNo,
                                                    String shortCode) {

        Map<String, String> entityTypes = new HashMap<>();
        componentEntityCategoryRules = getEntityCategoryRules(orgId, projectId, WBSEntityType.COMPONENT);
        String componentEntityType = null;
        String entityBusinessType = null;


        EntitySubTypeRule rule = getComponentEntityTypeRuleBySetting(shortCode,
            componentEntityCategoryRules);
        if (rule != null) {

            componentEntityType = rule.getEntitySubType().getNameEn();
            if (rule.getEntitySubType() != null
                && rule.getEntitySubType().getEntityBusinessType() != null) {
                entityBusinessType = rule.getEntitySubType().getEntityBusinessType().getNameEn();
            }

            if (null != rule.getParentType()) {
                ProjectNode projectNode = projectNodeRepository.findByProjectIdAndNoAndDeletedIsFalse(
                    projectId,
                    parentNodeNo).orElse(null);
                if (null == projectNode || projectNode.getEntityType() != rule.getParentType()) {
                    throw new BusinessError(
                        "parent node does NOT exist OR parent node's ENTITY TYPE does NOT match rules.");
                }
            }

        } else {
            String shortCodeUpper = shortCode.toUpperCase();
            if (!shortCodeUpper.contains(PIPE)) {

                ProjectNode projectNode = projectNodeRepository.findByProjectIdAndNoAndDeletedIsFalse(
                    projectId,
                    parentNodeNo).orElse(null);
                if (projectNode != null) {
                    if (projectNode.getEntityType() == WBSEntityType.SPOOL.name()) {
                        componentEntityType = OTHERS_ON_SPOOL;
                    } else if (projectNode.getEntityType() == WBSEntityType.ISO.name()) {
                        componentEntityType = OTHERS_ON_ISO;
                    }
                    entityBusinessType = FITTING;
                }
            }
        }
        entityTypes.put("subEntityType", componentEntityType);
        entityTypes.put("entityBusinessType", entityBusinessType);

        return entityTypes;
    }

    @Override
    public SubDrawingHistory findSubDrawing(Long id,
                                            Long orgId,
                                            Long projectId) {

        List<HierarchyNode> hierarchyNodes = hierarchyRepository.findByEntityIdAndDeletedIsFalse(
            id,
            orgId,
            projectId);
        if (hierarchyNodes == null || hierarchyNodes.size() == 0) {
            throw new BusinessError("Component has NO hierarchyNode.");
        } else if (hierarchyNodes.size() > 1) {
            throw new BusinessError("Component has TOO many hierarchyNode.");
        }


        ProjectNode projectNode = projectNodeRepository.findByHierarchyIdAndProjectIdAndOrgId(
            hierarchyNodes.get(0).getParentId(),
            projectId,
            orgId)
            .orElse(null);
        if (projectNode == null) {
            return null;
        }

        if (WBSEntityType.SPOOL.name() == projectNode.getEntityType()) {

            SpoolEntity spoolEntity = spoolEntityRepository.findById(projectNode.getEntityId()).orElse(null);
            if (spoolEntity == null) {
                return null;
            }

            return subDrawingHistoryRepository.findBySubDrawingNoAndPageNoAndIssueFlagIsTrue(
                spoolEntity.getNo(),
                spoolEntity.getSheetNo())
                .orElse(null);
        } else if (WBSEntityType.ISO.name() == projectNode.getEntityType()) {

            ISOEntity isoEntity = isoEntityRepository.findById(projectNode.getEntityId()).orElse(null);
            if (isoEntity == null) {
                return null;
            }

            return subDrawingHistoryRepository.findBySubDrawingNoAndIssueFlagIsTrue(
                isoEntity.getNo())
                .orElse(null);
        } else {
            throw new BusinessError("Component'parent is INVALID.");
        }

    }


    /**
     * 取得 COMPONENT 图纸信息。
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


        Optional<ComponentEntity> opt = componentEntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(id, orgId,
            projectId);
        if (opt.isPresent()) {
            ComponentEntity entity = opt.get();
            String isoName = entity.getIsoNo();
            Integer sheetNo = entity.getSheetNo();
            isoName = isoName.replaceAll("\"", "_").replaceAll("/", "_");

            return subDrawingRepository.findByOrgIdAndProjectIdAndStatusAndSubDrawingNoAndPageNo(orgId,
                projectId, ACTIVE, isoName, sheetNo);
        }


        return new ArrayList<>();
    }

    /**
     * 取得管件实体对应的材料信息。
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

        ComponentEntity componentEntity = componentEntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(
            id,
            orgId,
            projectId).orElse(null);
        List<MaterialInfoDTO> materialInfoDTOS = new ArrayList<>();
        if (componentEntity != null) {
            MaterialInfoDTO materialInfoDTO = new MaterialInfoDTO();
            materialInfoDTO.setMaterialCode(componentEntity.getMaterialCode());
            materialInfoDTO.setMaterialDesc(componentEntity.getMaterial());
            materialInfoDTO.setQuantity(String.valueOf(componentEntity.getQty()));
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
     * @param operatorId  项目ID
     * @return 实体下载临时文件
     */
    @Override
    public File saveDownloadFile(Long orgId,
                                 Long projectId,
                                 ComponentEntryCriteriaDTO criteriaDTO,
                                 Long operatorId) {

        String templateFilePath = "/var/imos/resources/templates/reports/export-project-entities-component.xlsx";
        String templateFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/imos/private/upload/" + templateFileName;
        File excel = new File(filePath);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends ComponentEntityBase> componentEntities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();
        EasyExcel.write(filePath, ComponentEntityBase.class).withTemplate(templateFilePath).sheet("Components").doFill(componentEntities);
        return excel;


//        String temporaryFileName = FileUtils.copy(
//            this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("templates/export-project-entities-component.xlsx"),
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
//        Sheet sheet = workbook.getSheet("Components");
//
//        int rowNum = DATA_START_ROW;
//
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setFetchAll(true);
//        List<? extends ComponentEntityBase> componentEntities = search(
//            orgId,
//            projectId,
//            criteriaDTO,
//            pageDTO).getContent();
//        for (ComponentEntityBase entity : componentEntities) {
//
//            Row row = WorkbookUtils.getRow(sheet, rowNum++);
//
//            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
//                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
//            }
//
//
//            WorkbookUtils.getCell(row, 0).setCellValue(entity.getIsoNo());
//
//            WorkbookUtils.getCell(row, 1).setCellValue(entity.getSpoolNo());
//
//
//            if (entity.getSheetNo() != null) {
//                WorkbookUtils.getCell(row, 2).setCellValue(entity.getSheetNo());
//            }
//            if (entity.getSheetTotal() != null) {
//                WorkbookUtils.getCell(row, 3).setCellValue(entity.getSheetTotal());
//            }
//            WorkbookUtils.getCell(row, 4).setCellValue(entity.getRevision());
//            WorkbookUtils.getCell(row, 5).setCellValue(entity.getShortCode());
//            WorkbookUtils.getCell(row, 6).setCellValue(entity.getNpsText());
//            WorkbookUtils.getCell(row, 7).setCellValue(entity.getThickness());
//            WorkbookUtils.getCell(row, 8).setCellValue(entity.getMaterialCode());
//            WorkbookUtils.getCell(row, 9).setCellValue(entity.getMaterial());
//            WorkbookUtils.getCell(row, 10).setCellValue(entity.getQty());
//            WorkbookUtils.getCell(row, 11).setCellValue(entity.getPipeClass());
//            WorkbookUtils.getCell(row, 12).setCellValue(entity.getCoordinateX());
//            WorkbookUtils.getCell(row, 13).setCellValue(entity.getCoordinateY());
//            WorkbookUtils.getCell(row, 14).setCellValue(entity.getCoordinateZ());
//            WorkbookUtils.getCell(row, 15).setCellValue(entity.getFabricated());
//            WorkbookUtils.getCell(row, 16).setCellValue(entity.getMaterialWithPositionalMark());
//            WorkbookUtils.getCell(row, 17).setCellValue(entity.getRemarks());
//            WorkbookUtils.getCell(row, 18).setCellValue(entity.getRemarks2());
//            WorkbookUtils.getCell(row, 19).setCellValue(entity.getModuleNo());
//
////            if (entity.getIsoEntityId() != null) {
////                Optional<ISOEntity> isoEntityOptional = isoEntityRepository.findById(entity.getIsoEntityId());
////                if (isoEntityOptional.isPresent()) {
////                    WorkbookUtils.getCell(row, 19).setCellValue(isoEntityOptional.get().getModuleNo());
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
     * 设置管件实体类型。
     *
     * @param shortCode 管件shortCode
     * @param rules     管件实体类型设置规则
     * @return EntityCategoryRule 实体类型设置规则
     */
    private EntitySubTypeRule getComponentEntityTypeRuleBySetting(
        String shortCode,
        Set<EntitySubTypeRule> rules) {


        for (EntitySubTypeRule rule : rules) {
            String value1 = rule.getValue1();
            String value2 = rule.getValue2();
            if (!StringUtils.isEmpty(value1) || !StringUtils.isEmpty(value2)) {
                boolean containValue1 = true;
                boolean notContainValue2 = true;
                if (!StringUtils.isEmpty(value1)) {
                    String[] keywords = value1.split(SEPARATOR);
                    for (String keyword : keywords) {
                        if (shortCode.toUpperCase().contains(keyword.toUpperCase())) {
                            containValue1 &= true;
                        } else {
                            containValue1 &= false;
                        }
                    }
                    if (!StringUtils.isEmpty(value2)) {
                        if (shortCode.toUpperCase().contains(value2.toUpperCase())) {
                            notContainValue2 = false;
                        }
                    }
                    if (containValue1 && notContainValue2) {
                        return rule;
                    }

                } else if (!StringUtils.isEmpty(rule.getValue2())) {
                    if (!shortCode.toUpperCase().contains(value2.toUpperCase())) {
                        return rule;
                    }
                }
            }

        }
        return null;
    }

    /**
     * 设置 ISO PN ID号，更新WBS-ENTRY
     */
    @Override
    public void setIsoIdsAndWbs(Long projectId, Long entityId) {
        componentEntityRepository.updateComponentAndProjectNodeIds(projectId, entityId);


    }

}
