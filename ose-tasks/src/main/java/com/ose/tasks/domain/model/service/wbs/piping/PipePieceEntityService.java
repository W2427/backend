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
import com.ose.tasks.domain.model.repository.drawing.SubDrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ISOEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.PipePieceEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.PipePieceHierarchyInfoEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.SpoolEntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.wbs.PipePieceEntryCriteriaDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import com.ose.tasks.entity.wbs.entity.PipePieceEntity;
import com.ose.tasks.entity.wbs.entity.PipePieceEntityBase;
import com.ose.tasks.entity.wbs.entity.PipePieceHierarchyInfoEntity;
import com.ose.tasks.entity.wbs.entity.SpoolEntity;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.BeanUtils;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;

@Component
public class PipePieceEntityService implements BaseWBSEntityInterface<PipePieceEntityBase, PipePieceEntryCriteriaDTO> {


    private final PipePieceEntityRepository pipePieceEntityRepository;
    private final PipePieceHierarchyInfoEntityRepository pipePieceHierarchyInfoEntityRepository;


    private final SpoolEntityRepository spoolEntityRepository;


    private final HierarchyRepository hierarchyRepository;


    private ProjectNodeRepository projectNodeRepository;


//    private EntityQrCodeRepository entityQrCodeRepository;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;


    private final BpmEntitySubTypeRepository bpmEntityCategoryRepository;


    private final BpmEntityTypeRepository bpmEntityCategoryTypeRepository;


    private final SubDrawingHistoryRepository subDrawingHistoryRepository;


    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;


    private final BpmCuttingEntityRepository bpmCuttingEntityRepository;


    private HierarchyInterface hierarchyService;


    private final PlanInterface planService;

    private final SubDrawingRepository subDrawingRepository;

    private final WBSEntryRepository wbsEntryRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    @Value("${application.files.temporary}")
    private String temporaryDir;


    private static final int DATA_START_ROW = 3;

    private static final int TEMPLATE_ROW_COUNT = 20;

    private final ISOEntityRepository isoEntityRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public PipePieceEntityService(
        PipePieceEntityRepository pipePieceEntityRepository,
        PipePieceHierarchyInfoEntityRepository pipePieceHierarchyInfoEntityRepository,
        HierarchyRepository hierarchyRepository,
        ProjectNodeRepository projectNodeRepository,
        SpoolEntityRepository spoolEntityRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
//        EntityQrCodeRepository entityQrCodeRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmEntitySubTypeRepository bpmEntityCategoryRepository,
        BpmEntityTypeRepository bpmEntityCategoryTypeRepository,
        SubDrawingHistoryRepository subDrawingHistoryRepository,
        BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
        BpmCuttingEntityRepository bpmCuttingEntityRepository,
        HierarchyInterface hierarchyService,
        PlanInterface planService, SubDrawingRepository subDrawingRepository,
        WBSEntryRepository wbsEntryRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        ISOEntityRepository isoEntityRepository) {
        this.pipePieceEntityRepository = pipePieceEntityRepository;
        this.pipePieceHierarchyInfoEntityRepository = pipePieceHierarchyInfoEntityRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.spoolEntityRepository = spoolEntityRepository;
        this.bpmEntityCategoryRepository = bpmEntityCategoryRepository;
        this.bpmEntityCategoryTypeRepository = bpmEntityCategoryTypeRepository;
        this.subDrawingHistoryRepository = subDrawingHistoryRepository;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.bpmCuttingEntityRepository = bpmCuttingEntityRepository;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
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
    public Page<? extends PipePieceEntityBase> search(
        Long orgId,
        Long projectId,
        PipePieceEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {






        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.PIPING)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findPipeIdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "PIPE_PIECE",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return pipePieceHierarchyInfoEntityRepository.search(
                        orgId,
                        projectId,
                        criteriaDTO,
                        pageDTO,
                        PipePieceHierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return pipePieceHierarchyInfoEntityRepository.search(
                    orgId,
                    projectId,
                    criteriaDTO,
                    pageDTO,
                    PipePieceHierarchyInfoEntity.class);
            }
        } else {
            return pipePieceHierarchyInfoEntityRepository.search(
                orgId,
                projectId,
                criteriaDTO,
                pageDTO,
                PipePieceHierarchyInfoEntity.class);
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
    public PipePieceEntityBase get(Long orgId, Long projectId, Long entityId) {

        PipePieceEntityBase entity = pipePieceHierarchyInfoEntityRepository
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


        PipePieceEntityBase entity = get(orgId, project.getId(), entityId);

        PipePieceEntity pipePieceEntity = BeanUtils.copyProperties(entity, new PipePieceEntity());

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

//        EntityQrCodeEntity entityQrCode = entityQrCodeRepository.findByProjectIdAndEntityId(project.getId(), entity.getId());
//
//        if (entityQrCode != null) {
//            entityQrCodeRepository.delete(entityQrCode);
//        }

        pipePieceEntity.setDeletedBy(operator.getId());
        pipePieceEntity.setDeletedAt();
        pipePieceEntity.setStatus(DELETED);
        pipePieceEntity.setDeleted(true);

        hierarchyService.delete(operator, project, orgId, entityId);

        bpmCuttingEntityRepository.updateCuttingEntityStatus(EntityStatus.DELETED, project.getId(), entityId);
        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entityId);
//        } else {
//
//            pipePieceEntity.setCancelled(true);
//            pipePieceEntity.setLastModifiedAt();
//            pipePieceEntity.setLastModifiedBy(operator.getId());
//            pipePieceEntity.setStatus(EntityStatus.CANCEL);
//
//
//            hierarchyService.cancel(operator, project, orgId, entityId);
//
//            bpmCuttingEntityRepository.updateCuttingEntityStatus(EntityStatus.CANCEL, project.getId(), entityId);
//            bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.CANCEL, project.getId(), entityId);
//        }
        pipePieceEntityRepository.save(pipePieceEntity);



//        if(isDeletable == null) return;
//        planService.updateStatusOfWBSOfDeletedEntity(project.getId(), entity.getWbsEntityType(), entityId, operator.getId(), isDeletable);


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
        PipePieceEntityBase entity
    ) {


        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());

        PipePieceEntity pipePieceEntity = BeanUtils.copyProperties(entity, new PipePieceEntity());
        pipePieceEntityRepository.save(pipePieceEntity);
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
    public PipePieceEntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        PipePieceEntityBase entity
    ) {


        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        PipePieceEntity pipePieceEntity = BeanUtils.copyProperties(entity, new PipePieceEntity());
        pipePieceEntityRepository.save(pipePieceEntity);
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

        if (pipePieceEntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
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
                              PipePieceEntityBase entity,
                              Long projectId,
                              Long orgId,
                              OperatorDTO operator) {

        List<HierarchyNode> parentNodes = hierarchyRepository
            .findByNoAndProjectIdAndDeletedIsFalse(
                parentNodeNo,
                projectId
            );

        if (parentNodes == null || parentNodes.size() == 0) {

            SpoolEntity se = new SpoolEntity();
//                spoolEntityRepository.findByNoAndProjectIdAndDeletedIsFalse(parentNodeNo, projectId).orElse(null);


            if (se != null) {

                ProjectNode projectNode = new ProjectNode();
                projectNode.setNo(entity.getNo());
                projectNode.setCompanyId(entity.getCompanyId());
                projectNode.setEntityId(entity.getId());
                projectNode.setEntityType(WBSEntityType.PIPE_PIECE.name());
                projectNode.setEntitySubType(WBSEntityType.PIPE_PIECE.name());
                projectNode.setEntityBusinessType(WBSEntityType.PIPE_PIECE.name());
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
                projectNode.setDwgShtNo(se.getSheetNo());
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
                throw new BusinessError("parent '" + parentNodeNo + "' doesn't exit");
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
            node.setEntitySubType(WBSEntityType.PIPE_PIECE.name());
            node.getNode().setEntityBusinessType(WBSEntityType.PIPE_PIECE.name());
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
     * 取得管段图纸信息。
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


        SpoolEntity spoolEntity = spoolEntityRepository.findById(projectNode.getEntityId()).orElse(null);
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
     * 取得 PIPE_PIECE 图纸信息。
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


        Optional<PipePieceEntity> optionalPipePieceEntity = pipePieceEntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(id, orgId,
            projectId);


        if (!optionalPipePieceEntity.isPresent()) {
            return new ArrayList<>();
        }
        PipePieceEntity entity = optionalPipePieceEntity.get();
        Long spoolEntityId = entity.getSpoolEntityId();

        Optional<SpoolEntity> optSpool = spoolEntityRepository
            .findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(spoolEntityId, orgId, projectId);
        if (!optSpool.isPresent()) {
            return new ArrayList<>();
        }

        SpoolEntity spoolEntity = optSpool.get();
        Integer sheetNo = spoolEntity.getSheetNo();


        String isoName = entity.getIsoNo();
        isoName = isoName.replaceAll("\"", "_").replaceAll("/", "_");

        List<SubDrawing> subDrawings = subDrawingRepository.findByOrgIdAndProjectIdAndStatusAndSubDrawingNoAndPageNo(orgId,
            projectId, ACTIVE, isoName, sheetNo);


        return subDrawings;


    }

    /**
     * 取得管段实体对应的材料信息。
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

        PipePieceEntity entity = pipePieceEntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(id,
            orgId,
            projectId).orElse(null);
        List<MaterialInfoDTO> materialInfoDTOS = new ArrayList<>();
        if (entity != null) {
            MaterialInfoDTO materialInfoDTO = new MaterialInfoDTO();
            materialInfoDTO.setMaterialCode(entity.getMaterialCode());
            materialInfoDTO.setMaterialDesc(entity.getMaterial());
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
                                 PipePieceEntryCriteriaDTO criteriaDTO,
                                 Long operatorId) {
        String templateFilePath = "/var/imos/resources/templates/reports/export-project-entities-pipe-piece.xlsx";
        String templateFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/imos/private/upload/" + templateFileName;
        File excel = new File(filePath);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends PipePieceEntityBase> pipepieceEntities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();
        EasyExcel.write(filePath, PipePieceEntityBase.class).withTemplate(templateFilePath).sheet("Pipe Pieces").doFill(pipepieceEntities);
        return excel;



//        String temporaryFileName = FileUtils.copy(
//            this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("templates/export-project-entities-pipe-piece.xlsx"),
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
//        Sheet sheet = workbook.getSheet("Pipe Pieces");
//
//        int rowNum = DATA_START_ROW;
//
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setFetchAll(true);
//        List<? extends PipePieceEntityBase> pipepieceEntities = search(
//            orgId,
//            projectId,
//            criteriaDTO,
//            pageDTO).getContent();
//        for (PipePieceEntityBase entity : pipepieceEntities) {
//
//            Row row = WorkbookUtils.getRow(sheet, rowNum++);
//
//            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
//                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
//            }
//
//            WorkbookUtils.getCell(row, 0).setCellValue(entity.getSpoolNo());
//
//            WorkbookUtils.getCell(row, 1).setCellValue(entity.getNo());
//
//            WorkbookUtils.getCell(row, 2).setCellValue(entity.getBevelCode1());
//            WorkbookUtils.getCell(row, 3).setCellValue(entity.getBevelCode2());
//            WorkbookUtils.getCell(row, 4).setCellValue(entity.getBendInfo());
//            WorkbookUtils.getCell(row, 5).setCellValue(entity.getCutDrawing());
//            WorkbookUtils.getCell(row, 6).setCellValue(entity.getRevision());
//            WorkbookUtils.getCell(row, 7).setCellValue(entity.getMaterialCode());
//            WorkbookUtils.getCell(row, 8).setCellValue(entity.getMaterial());
//            WorkbookUtils.getCell(row, 9).setCellValue(entity.getNpsText());
//            WorkbookUtils.getCell(row, 10).setCellValue(entity.getLengthText());
//            WorkbookUtils.getCell(row, 11).setCellValue(entity.getPipeClass());
//            WorkbookUtils.getCell(row, 12).setCellValue(entity.getRemarks());
//            WorkbookUtils.getCell(row, 13).setCellValue(entity.getRemarks2());
//            WorkbookUtils.getCell(row, 14).setCellValue(entity.getModuleNo());
//
////            if (entity.getIsoEntityId() != null) {
////                Optional<ISOEntity> isoEntityOptional = isoEntityRepository.findById(entity.getIsoEntityId());
////                if (isoEntityOptional.isPresent()) {
////                    WorkbookUtils.getCell(row, 14).setCellValue(isoEntityOptional.get().getModuleNo());
////                }
////            }
//
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
     * 设置 ISO PN ID号，更新WBS-ENTRY
     */
    @Override
    public void setIsoIdsAndWbs(Long projectId, Long entityId) {
        pipePieceEntityRepository.updatePpAndProjectNodeIds(projectId, entityId);


    }
}
