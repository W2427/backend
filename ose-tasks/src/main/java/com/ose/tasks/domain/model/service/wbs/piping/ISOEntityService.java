package com.ose.tasks.domain.model.service.wbs.piping;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.HierarchyRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmDeliveryEntityRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageEntityRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.repository.wbs.piping.ComponentEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ISOEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ISOHierarchyInfoEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.PipePieceEntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.domain.model.service.wbs.piping.ISOEntityInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.structureWbs.Wp01EntryCriteriaDTO;
import com.ose.tasks.dto.wbs.ISOEntryCriteriaDTO;
import com.ose.tasks.entity.BatchTask;
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
import com.ose.tasks.entity.wbs.structureEntity.Wp01EntityBase;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.wbs.HierarchyType;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.*;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import java.math.BigDecimal;
import java.util.*;

import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;

@Component
public class ISOEntityService extends StringRedisService implements ISOEntityInterface {


    private final ISOEntityRepository isoEntityRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private final ISOHierarchyInfoEntityRepository isoHierarchyInfoEntityRepository;

    private final WBSEntryPlainRepository wbsEntryPlainRepository;

    private final HierarchyRepository hierarchyRepository;


    private ProjectNodeRepository projectNodeRepository;

    private final TaskPackageEntityRelationRepository taskPackageEntityRelationRepository;


    private final SubDrawingHistoryRepository subDrawingHistoryRepository;


    private final ComponentEntityRepository componentEntityRepository;


    private final PipePieceEntityRepository pipePieceEntityRepository;


//    private final SpmMatchLnNodeFeignAPI spmMatchLnNodeFeignAPI;


    private final ProjectRepository projectRepository;



    private HierarchyInterface hierarchyService;


    private final PlanInterface planService;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;


    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;

//    private final SpmListPosRepository spmListPosRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final WBSEntryRepository wbsEntryRepository;

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
    public ISOEntityService(
        StringRedisTemplate stringRedisTemplate,
        ISOEntityRepository isoEntityRepository,
        ISOHierarchyInfoEntityRepository isoHierarchyInfoEntityRepository,
        HierarchyRepository hierarchyRepository,
        ProjectNodeRepository projectNodeRepository,
        SubDrawingHistoryRepository subDrawingHistoryRepository,
        ComponentEntityRepository componentEntityRepository,
        PipePieceEntityRepository pipePieceEntityRepository,
//        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") SpmMatchLnNodeFeignAPI spmMatchLnNodeFeignAPI,
        ProjectRepository projectRepository,
        HierarchyInterface hierarchyService,
        PlanInterface planService,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        TaskPackageEntityRelationRepository taskPackageEntityRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
//        SpmListPosRepository spmListPosRepository,
        SubDrawingRepository subDrawingRepository,
        WBSEntryRepository wbsEntryRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        WBSEntryPlainRepository wbsEntryPlainRepository) {
        super(stringRedisTemplate);
        this.isoEntityRepository = isoEntityRepository;
        this.isoHierarchyInfoEntityRepository = isoHierarchyInfoEntityRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.subDrawingHistoryRepository = subDrawingHistoryRepository;
        this.componentEntityRepository = componentEntityRepository;
        this.pipePieceEntityRepository = pipePieceEntityRepository;
//        this.spmMatchLnNodeFeignAPI = spmMatchLnNodeFeignAPI;
        this.projectRepository = projectRepository;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.taskPackageEntityRelationRepository = taskPackageEntityRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
//        this.spmListPosRepository = spmListPosRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.wbsEntryPlainRepository = wbsEntryPlainRepository;
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
    public Page<? extends ISOEntityBase> search(
        Long orgId,
        Long projectId,
        ISOEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.PIPING)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findIsoIdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "ISO",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return isoHierarchyInfoEntityRepository.search(
                        orgId,
                        projectId,
                        criteriaDTO,
                        pageDTO,
                        ISOHierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return isoHierarchyInfoEntityRepository.search(
                    orgId,
                    projectId,
                    criteriaDTO,
                    pageDTO,
                    ISOHierarchyInfoEntity.class);
            }
        } else {
            return isoHierarchyInfoEntityRepository.search(
                orgId,
                projectId,
                criteriaDTO,
                pageDTO,
                ISOHierarchyInfoEntity.class);
        }



    }

    @Override
    public BatchResultDTO updateBomLnMatch(BatchTask batchTask, OperatorDTO operator, Project project, boolean initialMatchFlag) {
        return null;
    }

    /**
     * 查询 WBS 实体模块。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return WBS 实体分页数据
     */
    @Override
    public List<ISOEntity> searchModules(
        Long orgId,
        Long projectId
    ) {
        return wbsEntryPlainRepository.getModules(orgId, projectId);
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
    public ISOEntityBase get(Long orgId, Long projectId, Long entityId) {

        ISOEntityBase entity = isoHierarchyInfoEntityRepository
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

        ISOEntityBase entity = get(orgId, project.getId(), entityId);

        ISOEntity isoEntity = BeanUtils.copyProperties(entity, new ISOEntity());


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
                        brt.setSuspensionState(0);
                        bpmRuTaskRepository.save(brt);
//                        }
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

        isoEntity.setDeletedBy(operator.getId());
        isoEntity.setDeletedAt();
        isoEntity.setStatus(DELETED);
        isoEntity.setDeleted(true);

        hierarchyService.delete(operator, project, orgId, entityId);

        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(DELETED, project.getId(), entityId);

        isoEntityRepository.save(isoEntity);

    }

    /**
     * 插入 WBS 实体同时插入ProjectNodes表里的记录。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    ISO 实体
     */
    @Override
    public void insert(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        ISOEntityBase entity
    ) {

        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        ISOEntity isoEntity = BeanUtils.copyProperties(entity, new ISOEntity());
        isoEntityRepository.save(isoEntity);


        ProjectNode projectNode = new ProjectNode();
        projectNode.setNo(entity.getNo());
        projectNode.setCompanyId(entity.getCompanyId());
        projectNode.setEntityId(entity.getId());
        projectNode.setEntityType(WBSEntityType.ISO.name());
        projectNode.setEntitySubType(WBSEntityType.ISO.name());
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
        projectNode.setDiscipline(DisciplineCode.PIPING.name());

        projectNodeRepository.save(projectNode);
    }

    /**
     * 更新 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    ISO 实体
     */
    @Override
    public ISOEntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        ISOEntityBase entity
    ) {

        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());

        ISOEntity isoEntity = BeanUtils.copyProperties(entity, new ISOEntity());
        isoEntityRepository.save(isoEntity);
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

        if (isoEntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
            return true;
        }

        return false;
    }

    /**
     * 取得管线图纸信息。
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


        ISOEntity isoEntity = isoEntityRepository.findById(id).orElse(null);
        if (isoEntity == null) {
            return null;
        }

        SubDrawingHistory subDrawingHistory =
            subDrawingHistoryRepository.findBySubDrawingNoAndIssueFlagIsTrue(
                    isoEntity.getNo())
                .orElse(null);
        return subDrawingHistory;

    }


    /**
     * 取得ISO图纸信息。
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

        Optional<ISOEntity> opt = isoEntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(id, orgId,
            projectId);
        if (opt.isPresent()) {
            ISOEntity entity = opt.get();
            String isoName = entity.getNo();
            isoName = isoName.replaceAll("\"", "_").replaceAll("/", "_");
            List<SubDrawing> subDrawings = subDrawingRepository.findByOrgIdAndProjectIdAndStatusAndSubDrawingNo(orgId,
                projectId, EntityStatus.ACTIVE, isoName);

            return subDrawings;
        }


        return new ArrayList<>();
    }

    /**
     * 取得管线实体对应的材料信息。
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

        List<HierarchyNode> hierarchyNodes = hierarchyRepository.findByEntityIdAndDeletedIsFalse(id, orgId, projectId);

        if (hierarchyNodes == null || hierarchyNodes.size() == 0) {
            throw new BusinessError("ISO has NO hierarchyNode.");
        } else if (hierarchyNodes.size() > 1) {
            throw new BusinessError("ISO has TOO many hierarchyNode.");
        }


        List<HierarchyNode> childNodes =
            hierarchyRepository.findByParentIdAndProjectIdAndAndHierarchyTypeAndDeletedIsFalse(
                hierarchyNodes.get(0).getId(), projectId, HierarchyType.PIPING.name());


        List<Long> spoolHierarchyIds = new ArrayList<>();

        List<Long> isoComponentIds = new ArrayList<>();
        if (childNodes != null && childNodes.size() != 0) {

            for (HierarchyNode nodeTemp : childNodes) {
                if (WBSEntityType.SPOOL.name() == nodeTemp.getEntityType()) {
                    spoolHierarchyIds.add(nodeTemp.getId());
                } else if (WBSEntityType.COMPONENT.name() == nodeTemp.getEntityType()) {
                    isoComponentIds.add(nodeTemp.getNode().getEntityId());
                }
            }

        }
        List<MaterialInfoDTO> materialInfoDTOS = new ArrayList<>();

        List<HierarchyNode> spoolChildNodes =
            hierarchyRepository.findByParentIdInAndProjectIdAndAndHierarchyTypeAndDeletedIsFalse(spoolHierarchyIds,
                projectId,
                HierarchyType.PIPING.name());

        List<Long> pipePieceIds = new ArrayList<>();

        List<Long> spoolComponentIds = new ArrayList<>();
        if (spoolChildNodes != null && spoolChildNodes.size() != 0) {
            for (HierarchyNode nodeTemp : spoolChildNodes) {
                if (WBSEntityType.PIPE_PIECE.name() == nodeTemp.getEntityType()) {
                    pipePieceIds.add(nodeTemp.getNode().getEntityId());
                } else if (WBSEntityType.COMPONENT.name() == nodeTemp.getEntityType()) {
                    spoolComponentIds.add(nodeTemp.getNode().getEntityId());
                }
            }
        }

        if (pipePieceIds != null && pipePieceIds.size() != 0) {

            List<MaterialInfoDTO> pipePieceMaterial = pipePieceEntityRepository.findByIdInAndProjectIdAndDeletedIsFalse(
                pipePieceIds,
                projectId);
            if (pipePieceMaterial != null && pipePieceMaterial.size() != 0) {
                for (MaterialInfoDTO dto : pipePieceMaterial) {
                    dto.setQuantity(dto.getQuantity() + " MM");
                    dto.setType("pipe");
                }
                materialInfoDTOS.addAll(pipePieceMaterial);

            }
        }


        if (spoolComponentIds != null && spoolComponentIds.size() != 0) {
            List<ComponentEntity> spoolComponents = componentEntityRepository.findByIdInAndProjectIdAndDeletedIsFalse(
                spoolComponentIds,
                projectId);

            if (spoolComponents != null && spoolComponents.size() != 0) {
                for (ComponentEntity entity : spoolComponents) {
                    MaterialInfoDTO materialInfoDTO = new MaterialInfoDTO();
                    materialInfoDTO.setMaterialDesc(entity.getMaterial());
                    materialInfoDTO.setMaterialCode(entity.getMaterialCode());
                    materialInfoDTO.setQuantity(String.valueOf(entity.getQty()));
                    materialInfoDTO.setType("component");
                    materialInfoDTOS.add(materialInfoDTO);
                }
            }
        }


        if (isoComponentIds != null && isoComponentIds.size() != 0) {
            List<ComponentEntity> componentEntities = componentEntityRepository.findByIdInAndProjectIdAndDeletedIsFalse(
                isoComponentIds,
                projectId);
            if (componentEntities != null && componentEntities.size() != 0) {
                for (ComponentEntity entity : componentEntities) {
                    MaterialInfoDTO materialInfoDTO = new MaterialInfoDTO();
                    materialInfoDTO.setMaterialDesc(entity.getMaterial());
                    materialInfoDTO.setMaterialCode(entity.getMaterialCode());
                    materialInfoDTO.setQuantity(String.valueOf(entity.getQty()));
                    materialInfoDTO.setType("component");
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
    public File saveDownloadFile(Long orgId, Long projectId, ISOEntryCriteriaDTO criteriaDTO, Long operatorId) {

        // todo 有时间改成easyExcel下载
        return null;
//        String temporaryFileName = FileUtils.copy(
//            this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("templates/export-project-entities-iso.xlsx"),
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
//        } catch (IOException e) {
//            e.printStackTrace(System.out);
//            throw new BusinessError();
//        }
//
//        Sheet sheet = workbook.getSheet("ISO");
//
//        int rowNum = DATA_START_ROW;
//
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setFetchAll(true);
//        Page<ISOEntity> isoEntitiesPage = isoEntityRepository.search(
//            orgId,
//            projectId,
//            criteriaDTO,
//            pageDTO);
//        List<ISOEntity> isoEntities = isoEntitiesPage.getContent();
//        for (ISOEntity entity : isoEntities) {
//
//            Row row = WorkbookUtils.getRow(sheet, rowNum++);
//
//            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
//                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
//            }
//
//            WorkbookUtils.getCell(row, 0).setCellValue(entity.getModuleNo());
//
//            WorkbookUtils.getCell(row, 1).setCellValue(entity.getLayerPackageNo());
//
//            WorkbookUtils.getCell(row, 2).setCellValue(entity.getPressureTestPackageNo());
//
//            WorkbookUtils.getCell(row, 3).setCellValue(entity.getCleanPackageNo());
//
//            WorkbookUtils.getCell(row, 4).setCellValue(entity.getSubSystemNo());
//
//            WorkbookUtils.getCell(row, 5).setCellValue(entity.getNo());
//            WorkbookUtils.getCell(row, 6).setCellValue(entity.getProcessLineNo());
//            WorkbookUtils.getCell(row, 7).setCellValue(entity.getProcessSystemNo());
//            WorkbookUtils.getCell(row, 8).setCellValue(entity.getRevision());
//            WorkbookUtils.getCell(row, 9).setCellValue(entity.getNpsText());
//
//            WorkbookUtils.getCell(row, 10).setCellValue(entity.getFluid());
//            WorkbookUtils.getCell(row, 11).setCellValue(entity.getPipeClass());
//            WorkbookUtils.getCell(row, 12).setCellValue(entity.getDesignPressureText());
//            WorkbookUtils.getCell(row, 13).setCellValue(entity.getDesignTemperatureText());
//            WorkbookUtils.getCell(row, 14).setCellValue(entity.getOperatePressureText());
//            WorkbookUtils.getCell(row, 15).setCellValue(entity.getOperateTemperatureText());
//            WorkbookUtils.getCell(row, 16).setCellValue(entity.getInsulationCode());
//            WorkbookUtils.getCell(row, 17).setCellValue(entity.getInsulationThicknessText());
//            WorkbookUtils.getCell(row, 18).setCellValue(entity.getPressureTestMedium());
//            WorkbookUtils.getCell(row, 19).setCellValue(entity.getTestPressureText());
//
//            WorkbookUtils.getCell(row, 20).setCellValue(entity.getPaintingCode());
//            WorkbookUtils.getCell(row, 21).setCellValue(entity.getPipeGrade());
//            WorkbookUtils.getCell(row, 22).setCellValue(entity.getHeatTracingCode());
//            WorkbookUtils.getCell(row, 23).setCellValue(entity.getAsmeCategory());
//            WorkbookUtils.getCell(row, 24).setCellValue(entity.getPidDrawing());
//
//            if (entity.getNdeRatio() != null) {
//                WorkbookUtils.getCell(row, 25).setCellValue(entity.getNdeRatio());
//            }
//            if (entity.getJacketPipe() != null) {
//                WorkbookUtils.getCell(row, 26).setCellValue(entity.getJacketPipe() ? YES : NO);
//            }
//            if (entity.getPwht() != null) {
//                WorkbookUtils.getCell(row, 27).setCellValue(entity.getPwht() ? YES : NO);
//            }
//            if (entity.getPmiRatio() != null) {
//                WorkbookUtils.getCell(row, 28).setCellValue(entity.getPmiRatio());
//            }
//            WorkbookUtils.getCell(row, 29).setCellValue(entity.getInternalMechanicalCleaning());
//
//            WorkbookUtils.getCell(row, 30).setCellValue(entity.getRemarks());
//            WorkbookUtils.getCell(row, 31).setCellValue(entity.getRemarks2());
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
        isoEntityRepository.updateIsoAndProjectNodeIds(projectId, entityId);


    }


}
