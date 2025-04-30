package com.ose.tasks.domain.model.service.plan;

import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.BatchTaskRepository;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageEntityRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.service.plan.business.PlanBusinessInterface;
import com.ose.tasks.domain.model.service.taskpackage.TaskPackageInterface;
import com.ose.tasks.dto.WBSEntityPatchDTO;
import com.ose.tasks.dto.WBSEntityPostDTO;
import com.ose.tasks.dto.wbs.WBSEntryPatchDTO;
import com.ose.tasks.entity.process.ProcessEntity;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.tasks.vo.setting.BatchTaskStatus;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

import static com.ose.tasks.vo.wbs.WBSEntryType.ENTITY;
import static com.ose.tasks.vo.wbs.WBSEntryType.WORK;

/**
 * 计划管理服务。
 */
@Component
public class PlanService implements PlanInterface {

    // 不可删除的四级计划执行状态
    private static final Set<WBSEntryRunningStatus> RUNNING_STATUS_CANNOT_BE_REMOVED
        = new HashSet<>(Arrays.asList(WBSEntryRunningStatus.RUNNING, WBSEntryRunningStatus.APPROVED));

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // WBS 条目数据仓库
    private final WBSEntryRepository wbsEntryRepository;

    // 工序-实体关系数据仓库
    private final ProcessEntityRepository processEntityRepository;

    // 计划前/后置任务关系数据仓库
    private final WBSEntryRelationRepository wbsEntryRelationRepository;

    // 计划业务
    private final PlanBusinessInterface planBusiness;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    // 批处理任务数据仓库
    private final BatchTaskRepository batchTaskRepository;

    private final TaskPackageEntityRelationRepository taskPackageEntityRelationRepository;

    private final TaskPackageInterface taskPackageService;

    private final WBSEntryBlobRepository wbsEntryBlobRepository;
    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;
    /**
     * 构造方法。
     */
    @Autowired
    public PlanService(
        WBSEntryRepository wbsEntryRepository,
        ProcessEntityRepository processEntityRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        PlanBusinessInterface planBusinessImpl,
        BatchTaskRepository batchTaskRepository,
        TaskPackageEntityRelationRepository taskPackageEntityRelationRepository,
        TaskPackageInterface taskPackageService,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        WBSEntryStateRepository wbsEntryStateRepository) {
        this.wbsEntryRepository = wbsEntryRepository;
        this.processEntityRepository = processEntityRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.planBusiness = planBusinessImpl;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.batchTaskRepository = batchTaskRepository;
        this.taskPackageEntityRelationRepository = taskPackageEntityRelationRepository;
        this.taskPackageService = taskPackageService;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
    }

    /**
     * 取得工作 WBS 条目。
     *
     * @param orgId     所属组织 ID
     * @param projectId 项目 ID
     * @param entryId   WBS 条目 ID
     * @return WBS 条目
     */
    private WBSEntry getEntry(Long orgId, Long projectId, Long entryId) {

        WBSEntry entry = wbsEntryRepository
            .findByProjectIdAndIdAndDeletedIsFalse(projectId, entryId)
            .orElse(null);

        if (entry == null) {
            throw new NotFoundError();
        }

        return entry;
    }

    /**
     * 更新 WBS 条目设置。
     *
     * @param operator         操作者信息
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param entryId          条目 ID
     * @param wbsEntryPatchDTO 条目更新数据 ffttjj
     * @return WBS 条目
     */
    @Override
    @Transactional
    public WBSEntry updateEntry(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long entryId,
        WBSEntryPatchDTO wbsEntryPatchDTO
    ) {

        // 取得更新目标条目信息
        WBSEntry entry = wbsEntryRepository
            .findByProjectIdAndIdAndDeletedIsFalse(
                projectId,
                entryId
            )
            .orElse(null);

        WBSEntry parentEntry = null;

        // 取得目标条目的上级条目
        if (entry != null) {
            parentEntry = wbsEntryRepository
                .findByProjectIdAndIdAndDeletedIsFalse(
                    projectId,
                    entry.getParentId()
                )
                .orElse(null);
        }

        // 实体条目更新
        if (wbsEntryPatchDTO instanceof WBSEntityPatchDTO) {
            planBusiness.updateEntity(operator, parentEntry, entry, (WBSEntityPatchDTO) wbsEntryPatchDTO);
            // 一般更新
        } else {
            planBusiness.updateEntry(operator, parentEntry, entry, wbsEntryPatchDTO);
        }

        return entry;
    }

    /**
     * 查询实体信息。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param stageName   工序阶段名称
     * @param processName 工序名称
     * @param entityType  实体类型
     * @param pageable    分页参数
     * @return 实体信息分页数据
     */
    @Override
    public Page<ProcessEntity> searchEntities(
        final Long orgId,
        final Long projectId,
        final String stageName,
        final String processName,
        final String entityType,
        final Pageable pageable
    ) {
        return processEntityRepository
            .findByProjectIdAndStageAndProcessAndEntityType(
                projectId,
                stageName,
                processName,
                entityType,
                pageable
            );
    }

    /**
     * 向工作类型的 WBS 中添加实体资源。
     *
     * @param operator    操作者信息
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param workEntryId 工作条目 ID
     * @param entityDTO   实体添加数据 ffttjj
     */
    @Override
    public WBSEntry addEntity(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long workEntryId,
        WBSEntityPostDTO entityDTO
    ) {
        WBSEntry workEntry = getEntry(orgId, projectId, workEntryId);

        // 仅可在类型为 WORK 的 WBS 下添加实体资源
        if (workEntry.getType() != WORK) {
            throw new BusinessError("error.wbs.add-child-entry.must-be-work");
        }

        if (batchTaskRepository.existsByProjectIdAndCodeAndStatus(
            projectId,
            BatchTaskCode.ENTITY_PROCESS_WBS_GENERATE,
            BatchTaskStatus.RUNNING
        )) {
            throw new ConflictError("error.wbs.import-task-not-finished");
        }

        ProcessEntity processEntity;

        if (!LongUtils.isEmpty(entityDTO.getProjectNodeId())) {
            processEntity = processEntityRepository
                .findByProjectIdAndProjectNodeIdAndStageAndProcess(
                    projectId,
                    entityDTO.getProjectNodeId(),
                    workEntry.getStage(),
                    workEntry.getProcess()
                )
                .orElse(null);
        } else if (entityDTO.getEntityType() != null && !LongUtils.isEmpty(entityDTO.getEntityId())) {
            processEntity = processEntityRepository
                .findFirstByProjectIdAndEntityTypeAndEntityIdAndStageAndProcessOrderByIdAsc(
                    projectId,
                    entityDTO.getEntityType(),
                    entityDTO.getEntityId(),
                    workEntry.getStage(),
                    workEntry.getProcess()
                )
                .orElse(null);

            if (processEntity == null) {
                throw new ValidationError("error.wbs.entity-process-not-found");
            }
        } else {
            throw new ValidationError("error.wbs.import.entity-required");
        }

        if (processEntity != null) {
            entityDTO.setProjectNodeId(processEntity.getProjectNodeId());
        }

        if (LongUtils.isEmpty(entityDTO.getProjectNodeId())) {
            throw new ValidationError("error.wbs.import.project-node-id-required");
        }

        WBSEntry lastChild = wbsEntryRepository
            .findFirstByParentIdAndTypeAndDeletedIsFalseOrderBySortDesc(workEntryId, ENTITY)
            .orElse(null);

        WBSEntry entry = planBusiness.addEntity(
            operator,
            projectId,
            workEntry,
            entityDTO,
            lastChild == null ? 1 : (lastChild.getSortNo() + 1),
            processEntity == null ? null : processEntity.getProcessId()
        );

        Set<Long> workEntryIds = new HashSet<>();
        workEntryIds.add(workEntryId);
        setParentEntityIDs(projectId, workEntryIds);

        return entry;
    }

    /**
     * 删除实体条目。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId 条目 ID ffttjj
     * @return WBS 更新版本号
     */
    @Override
    @Transactional
    public Long deleteEntry(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long wbsEntryId
    ) {

        WBSEntry wbsEntry = wbsEntryRepository
            .findByProjectIdAndIdAndActiveIsTrueAndDeletedIsFalse(
                projectId,
                wbsEntryId
            ).orElse(null);

        WBSEntryState wbsEntryState = wbsEntryStateRepository
            .findByWbsEntryId(
                wbsEntryId
            );

        if (wbsEntry == null || wbsEntryState == null) {
            throw new NotFoundError();
        }

        if (wbsEntryState.getRunningStatus() == WBSEntryRunningStatus.RUNNING
            || wbsEntryState.getRunningStatus() == WBSEntryRunningStatus.APPROVED
            ) {
            throw new BusinessError("error.wbs.cannot-remove-running-wbs");
        }

        List<Long> wIds = wbsEntryRepository.findChildrenByPathAndRunningStatusInAndDeletedIsFalse(
            projectId,
            wbsEntryState.getId(),
            RUNNING_STATUS_CANNOT_BE_REMOVED
        );
        if (!CollectionUtils.isEmpty(wIds)) {
            throw new BusinessError("error.wbs.cannot-remove-wbs-with-running-child");
        }

        if (batchTaskRepository.existsByProjectIdAndCodeAndStatus(
            projectId,
            BatchTaskCode.ENTITY_PROCESS_WBS_GENERATE,
            BatchTaskStatus.RUNNING
        )) {
            throw new ConflictError("error.wbs.import-task-not-finished");
        }

        // 删除所有前/后置关系
        wbsEntryRelationRepository.deleteByEntryId(projectId, wbsEntryId);

        // 逻辑删除子条目
        wbsEntryRepository.deleteByPathLikeAndDeletedIsFalse(
            projectId,
            wbsEntryState.getId(),
            System.currentTimeMillis(),
            new Date(),
            operator.getId()
        );

        wbsEntry.setGuid(String.format("%s[deleted:%s]", wbsEntry.getGuid(), System.currentTimeMillis()));
        wbsEntry.setVersion(System.currentTimeMillis());
        wbsEntry.setDeletedAt();
        wbsEntry.setDeletedBy(operator.getId());
        wbsEntry.setDeleted(true);

        wbsEntryRepository.save(wbsEntry);

        planBusiness.updateFinishedScoreOfParents(wbsEntry);

        return wbsEntry.getVersion();
    }

    /**
     * 设置 WBS 条目的权重。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID ffttjj
     * @param score      权重
     */
    @Override
    public void setScore(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long wbsEntryId,
        final Double score
    ) {
        planBusiness.setScore(operator, orgId, projectId, wbsEntryId, score);
    }

    /**
     * 根据关联实体的状态更新 WBS 的状态。
     *
     * @param projectId  项目 ID
     * @param entityType 实体类型
     * @param entityId   实体 ID
     */
    @Override
    public void updateStatusOfWBSOfDeletedEntity(
        final Long projectId,
        final String entityType,
        final Long entityId,
        final Long operatorId,
        final Boolean isDeletable
    ) {

        Set<String> guids = wbsEntryRelationRepository.findGuids(projectId, entityId);

        if(!CollectionUtils.isEmpty(guids)) {
            guids.forEach(guid ->{
                wbsEntryRelationRepository.findAllByTaskGUID(projectId, guid).forEach(wrId -> {
                    wbsEntryRelationRepository.deleteById(wrId.longValue());
                });
            });

        }

        wbsEntryRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, entityId).forEach(wbsEntry -> {
            wbsEntryPlainRelationRepository.deleteByWbsEntryId(wbsEntry.getId());

            if(isDeletable == null || !isDeletable) {
                wbsEntryRepository.deleteById(wbsEntry.getId());
                wbsEntryStateRepository.deleteByWbsId(wbsEntry.getId());
                wbsEntryBlobRepository.deleteByWbsId(wbsEntry.getId());
            } else {
                wbsEntry.setActive(false);
                wbsEntry.setLastModifiedAt();
                wbsEntry.setStatus(EntityStatus.CANCEL);
                wbsEntry.setLastModifiedBy(operatorId);
                wbsEntry.setGuid(wbsEntry.getGuid() + "[SUSPENDED:" + new Date().getTime() + "]");
                wbsEntryRepository.save(wbsEntry);
            }
        });

        taskPackageEntityRelationRepository.deleteByProjectIdAndEntityId(projectId, entityId);

        wbsEntryRepository.updateStatusOfWBSOfDeletedEntity(projectId, entityId);
        hierarchyNodeRelationRepository.findByProjectIdAndEntityId(projectId, entityId).forEach(
            hierarchyNodeRelationRepository::deleteById
        );
        taskPackageService.reCalcTaskPackageCount(projectId, entityId);

    }

    @Override
    public void updateWBSEntryManualProgress(Long orgId, Long projectId, Long wbsEntryId, String manualProgress) {
//        double mpFigure = null;
        if (StringUtils.isEmpty(manualProgress)) {
//            mpFigure = null;
            wbsEntryRepository.setManualProgress(orgId, projectId, wbsEntryId);
        } else {
            double mpFigure = Double.valueOf(manualProgress);
            wbsEntryRepository.setManualProgress(orgId, projectId, wbsEntryId, mpFigure);
        }
    }

    /**
     * 更新所有需要更新 的三级计划上的 实体
     */
    @Override
    public void setParentEntityIDs(Long projectId, Set<Long> workLoadWbsIds) {

//        Set<BigInteger> entityIds = new HashSet<>();
//        Map<String, Set<BigInteger>> disciplineEntityIds = new HashMap<>();
        Set<BigInteger> pipingEntityIds = new HashSet<>();
        Set<BigInteger> structureEntityIds = new HashSet<>();
        workLoadWbsIds.forEach(wbsId -> {
            WBSEntry wbsEntry = wbsEntryRepository.findById(wbsId).orElse(new WBSEntry());
            Set<BigInteger> tmpEntityIds = wbsEntryRepository.findIdsByWorkWbsId(wbsId);
            if(tmpEntityIds != null) {
                if("PIPING".equalsIgnoreCase(wbsEntry.getFuncPart())) {
                    pipingEntityIds.addAll(tmpEntityIds);
                } else if("STRUCTURE".equalsIgnoreCase(wbsEntry.getFuncPart())){
                    structureEntityIds.addAll(tmpEntityIds);
                }
//                entityIds.addAll(tmpEntityIds);
            }

        });

    }
}
