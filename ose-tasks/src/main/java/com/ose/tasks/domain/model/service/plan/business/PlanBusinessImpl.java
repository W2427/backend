package com.ose.tasks.domain.model.service.plan.business;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessStageRepository;
import com.ose.tasks.domain.model.repository.process.EntityProcessRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.repository.wbs.piping.SubSystemEntityRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.EntityTypeInterface;
import com.ose.tasks.domain.model.service.bpm.EntityTypeProcessRelationInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.plan.WBSEntryPlainRelationInterface;
import com.ose.tasks.domain.model.service.taskpackage.TaskPackageInterface;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.wbs.WBSEntryChildStatisticsDTO;
import com.ose.tasks.dto.wbs.WBSEntryPatchDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryBlob;
import com.ose.tasks.entity.wbs.entry.WBSEntryRelation;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.util.SpringUtils;
import com.ose.tasks.vo.wbs.WBSEntryType;
import com.ose.util.LongUtils;
import com.ose.util.RegExpUtils;
import com.ose.util.StringUtils;
import com.ose.util.ValueUtils;
import com.ose.vo.EntityStatus;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

import static com.ose.vo.EntityStatus.ACTIVE;

/**
 * 计划管理服务。
 */
@Component
public class PlanBusinessImpl extends StringRedisService implements PlanBusinessInterface {

    // 项目节点数据仓库
    private final ProjectNodeRepository projectNodeRepository;

    // WBS 条目数据仓库
    private final WBSEntryRepository wbsEntryRepository;

    // WBS 条目关系数据仓库
    private final WBSEntryRelationRepository wbsEntryRelationRepository;
    private final ProjectInterface projectService;
    // 实体数据仓库
    private final SubSystemEntityRepository systemEntityRepository;
    private final WBSEntryPlainRelationInterface wbsEntryPlainRelationService;
    private final TaskPackageRepository taskPackageRepository;
    private final WBSEntryBlobRepository wbsEntryBlobRepository;
    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final EntityTypeProcessRelationInterface entityTypeProcessRelationService;

    // 工序阶段数据仓库
    private final BpmProcessStageRepository processStageRepository;

    // 实体工序视图数据仓库
    private final EntityProcessRepository entityProcessRepository;

    private final TaskPackageInterface taskPackageService;
    private final TodoTaskBaseInterface todoTaskBaseService;

    private final EntityTypeInterface entityTypeService;

    private static final String PROCESS_FUNC_PART_AT_REDIS_KEY = "PROCESS_FUNC_PART:%s";


    /**
     * 构造方法。
     */
    @Autowired
    public PlanBusinessImpl(
        StringRedisTemplate stringRedisTemplate,
        ProjectNodeRepository projectNodeRepository,
        WBSEntryRepository wbsEntryRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        ProjectInterface projectService,
        SubSystemEntityRepository systemEntityRepository,
        WBSEntryPlainRelationInterface wbsEntryPlainRelationService,
        TaskPackageRepository taskPackageRepository,
        BpmProcessStageRepository processStageRepository,
        EntityProcessRepository entityProcessRepository,
        TaskPackageInterface taskPackageService,
        TodoTaskBaseInterface todoTaskBaseService,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        EntityTypeProcessRelationInterface entityTypeProcessRelationService, EntityTypeInterface entityTypeService) {
        super(stringRedisTemplate);
        this.projectNodeRepository = projectNodeRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.projectService = projectService;
        this.systemEntityRepository = systemEntityRepository;
        this.wbsEntryPlainRelationService = wbsEntryPlainRelationService;
        this.taskPackageRepository = taskPackageRepository;
        this.processStageRepository = processStageRepository;
        this.entityProcessRepository = entityProcessRepository;
        this.taskPackageService = taskPackageService;
        this.todoTaskBaseService = todoTaskBaseService;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.entityTypeProcessRelationService = entityTypeProcessRelationService;
        this.entityTypeService = entityTypeService;
    }

    /**
     * 取得所有工序阶段及其工序名称信息。
     *
     * @param projectId 项目 ID
     * @return 工序阶段及其工序名称的映射表
     */
    @Override
    public Map<String, Set<String>> getStageProcesses(Long projectId) {

        final String REDIS_KEY = "PROJECTS:" + projectId + ":STAGES";
        final String stagesJSON = getRedisKey(REDIS_KEY, 30);

        // 若已缓存所有阶段名称则将其转为集合
        if (!StringUtils.isEmpty(stagesJSON)) {
            return StringUtils.fromJSON(
                stagesJSON,
                new TypeReference<Map<String, Set<String>>>() {
                },
                new HashMap<>()
            );
        }

        final Map<String, Set<String>> stages = new HashMap<>();

        // 否则从数据库取得所有阶段名称并缓存
        processStageRepository
            .findProcesses(projectId)
            .forEach(processDTO -> stages
                .computeIfAbsent(processDTO.getStageName(), k -> new HashSet<>())
                .add(processDTO.getProcessName())
            );

        // 缓存取得的结果
        if (stages.size() > 0) {
            setRedisKey(REDIS_KEY, StringUtils.toJSON(stages), 30);
        }

        return stages;
    }

    /**
     * 向工作类型的 WBS 中添加实体资源。
     *
     * @param operator         操作者信息
     * @param projectId        项目 ID
     * @param workEntry        工作条目
     * @param wbsEntityPostDTO 实体添加数据
     * @param entityIndex      实体序号
     */
    @Override
    public WBSEntry addEntity(
        final OperatorDTO operator,
        final Long projectId,
        final WBSEntry workEntry,
        final WBSEntityPostDTO wbsEntityPostDTO,
        final int entityIndex,
        final Long processId
    ) {

        WBSEntryBlob workEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(workEntry.getId());

        ProjectNode entityNode = projectNodeRepository
            .findByProjectIdAndIdAndStatus(
                projectId,
                wbsEntityPostDTO.getProjectNodeId(),
                EntityStatus.ACTIVE
            )
            .orElse(null);

        String redisKey = String.format(PROCESS_FUNC_PART_AT_REDIS_KEY, projectId.toString());

        String redisStr = getRedisKey(redisKey);
        Map<String, Long> processFuncPartMap = new HashMap<>();
        if (redisStr == null || redisStr.equalsIgnoreCase("null")) {
            processFuncPartMap = entityTypeProcessRelationService.getProcessFuncPartMap(projectId);//To Redis

            redisStr = StringUtils.toJSON(processFuncPartMap);
            setRedisKey(redisKey, redisStr);
        }

        if (StringUtils.isEmpty(redisStr)) {
            throw new BusinessError("There is no MODULE/SYSTEM");
        }

//        List<String> moduleNoList = StringUtils.decode(redisStr, new TypeReference<Map<String, Long>>() {
//        });

//        processFuncPartMap = new HashSet<>(moduleNoList);
//        if (entityNode == null) {
//            throw new NotFoundError();
//        }


        WBSEntry wbsEntry;
        WBSEntryState wbsEntryState;
        WBSEntryBlob wbsEntryBlob;

        wbsEntry = wbsEntryRepository
            .findByProjectIdAndEntityIdAndParentId(
                projectId,
                entityNode.getEntityId(),
                workEntry.getId()
            )
            .orElse(null);

        Long tpId = null;
        Project project = projectService.get(projectId);

        // 若已添加过该实体资源则结束
        if (wbsEntry == null) {
            wbsEntry = new WBSEntry(
                operator, workEntry, entityNode, workEntryBlob.getWbs()+"."+entityNode.getNo(), entityIndex,
                getStageProcesses(projectId),
                processFuncPartMap,
                processId,
                project.getWbsMode()
            );
            wbsEntryRepository.save(wbsEntry);
            WBSEntryBlob parentWbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(workEntry.getId());

            wbsEntryBlob = new WBSEntryBlob();
            wbsEntryBlob.setPath(parentWbsEntryBlob.getPath() + workEntry.getId() + "/");
            wbsEntryBlob.setWbs(parentWbsEntryBlob.getWbs() + "." + entityNode.getNo());

            wbsEntryBlob.setWbsEntryId(wbsEntry.getId());
            wbsEntryBlob.setOrgId(workEntry.getOrgId());
            wbsEntryBlob.setProjectId(projectId);
            wbsEntryBlobRepository.save(wbsEntryBlob);

            wbsEntryState = new WBSEntryState();
            wbsEntryState.setWbsEntryId(wbsEntry.getId());
            wbsEntryState.setOrgId(wbsEntry.getOrgId());
            wbsEntryState.setProjectId(projectId);
            wbsEntryState.setLastModifiedBy(operator.getId());
            wbsEntryState.setLastModifiedAt(new Date());
        } else {
            String guid = wbsEntry.getGuid();
            wbsEntry.updateEntry(
            operator,
                workEntry,
                entityNode,
                workEntryBlob.getWbs()+"."+entityNode.getNo(),
                entityIndex,
                getStageProcesses(projectId),
                processFuncPartMap,
                processId,
                project.getWbsMode(),
                wbsEntry
            );
            wbsEntry.setGuid(guid);
            wbsEntryRepository.save(wbsEntry);
            wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
            if (wbsEntryState == null) {
                throw new NotFoundError(wbsEntry.getId().toString());
            }
        }

        if (wbsEntryState.getTaskPackageId() == null) {

            BigInteger taskPackageId = null;
            List<BigInteger> taskPackageIds = taskPackageRepository.
                getTaskPackageId(projectId, entityNode.getEntityId());
            if (taskPackageIds != null && taskPackageIds.size() > 0) {
                taskPackageId = taskPackageIds.get(0);
            }

            //更新 工作包信息
            if (taskPackageId != null) {
                wbsEntryState.setTaskPackageId(taskPackageId.longValue());
                taskPackageService.addTaskPackageCount(taskPackageId.longValue(), wbsEntryState.getWorkLoad());
            }
            WBSEntryTeamWorkSiteDTO wbsEntryTeamWorkSiteDTO = todoTaskBaseService.getWBSEntryTeamWorkSiteInfo(null, projectId, wbsEntry, wbsEntryState, processId);
            if (wbsEntryTeamWorkSiteDTO != null) {
                wbsEntryState.setWorkSiteId(wbsEntryTeamWorkSiteDTO.getWorkSiteId());
                wbsEntryState.setWorkSiteName(wbsEntryTeamWorkSiteDTO.getWorkSiteName());
                wbsEntryState.setTeamId(wbsEntryTeamWorkSiteDTO.getTeamId());
                wbsEntryState.setTeamName(wbsEntryTeamWorkSiteDTO.getTeamName());
                wbsEntryState.setTeamPath(wbsEntryTeamWorkSiteDTO.getTeamPath());
            }
            wbsEntryPlainRelationService.saveWBSEntryPath(wbsEntry.getOrgId(), projectId, wbsEntry);
        }

        wbsEntry.setVersion(new Date().getTime());
        wbsEntry.setFuncPart(workEntry.getFuncPart());
        wbsEntryStateRepository.save(wbsEntryState);
        updateEntity(operator, workEntry, wbsEntry, wbsEntityPostDTO);

        return wbsEntry;
    }

    /**
     * 启用 WBS 条目。
     *
     * @param operator 操作者信息
     * @param entry    WBS 条目
     */
    private void active(OperatorDTO operator, WBSEntry entry) {

        if (entry.getActive()) {
            return;
        }

        entry.setActive(true);
        entry.setLastModifiedAt();
        entry.setLastModifiedBy(operator.getId());

        wbsEntryRepository.save(entry);

        updateFinishedScoreOfParents(entry);
    }

    /**
     * 停用 WBS 条目。
     *
     * @param operator 操作者信息
     * @param entry    WBS 条目
     */
    private void inactive(OperatorDTO operator, WBSEntry entry) {

        if (!entry.getActive()) {
            return;
        }

        entry.setActive(false);
        entry.setLastModifiedAt();
        entry.setLastModifiedBy(operator.getId());

        wbsEntryRepository.save(entry);

        updateFinishedScoreOfParents(entry);
    }

    /**
     * 更新上级条目已完成权重之和。
     *
     * @param entry 当前条目
     */
    @Override
    public void updateFinishedScoreOfParents(final WBSEntry entry) {
        WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(entry.getId());
        if (wbsEntryBlob == null) return;

        final Set<Long> parentIDs = wbsEntryBlob.getParentIDs();

        if (parentIDs.size() == 0) {
            return;
        }

        final Long projectId = entry.getProjectId();

        List<WBSEntry> parentEntries = wbsEntryRepository
            .findByProjectIdAndIdInAndDeletedIsFalse(
                projectId,
                parentIDs
            );

        for (WBSEntry parentEntry : parentEntries) {

            WBSEntryChildStatisticsDTO statisticsDTO = wbsEntryStateRepository
                .sumFinishedScore(projectId, parentEntry.getId());

            WBSEntryState parentEntryState = wbsEntryStateRepository.findByWbsEntryId(parentEntry.getId());
            if (parentEntryState == null) continue;
            parentEntryState.setFinishedScore(statisticsDTO.getFinishedScore());
            parentEntryState.setActualDuration(statisticsDTO.getActualDuration());
            parentEntryState.setTotalScore(statisticsDTO.getTotalScore());

            wbsEntryStateRepository.save(parentEntryState);
        }

    }

    /**
     * 取得实体信息。
     *
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @param funcPart 专业
     * @return 实体信息
     */
    @Override
    public WBSEntityBase getEntity(Long projectId, String entityType, Long entityId, String funcPart) {

        BpmEntityType bet = entityTypeService.getBpmEntityType(projectId, entityType);
        if(bet == null || bet.getRepositoryClazz()==null) throw new BusinessError("There is no entity type: " + entityType);
        String repositoryClazz = bet.getRepositoryClazz();

        Class clazz = null;
        try {
            clazz = Class.forName(repositoryClazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        WBSEntityBaseRepository entityRepository = (WBSEntityBaseRepository) SpringUtils.getBean(clazz);

        return (WBSEntityBase) entityRepository.findById(entityId).orElse(null);
    }

    /**
     * 取得实体信息。
     *
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @param discipline 专业
     * @return 实体信息
     */
    @Override
    public boolean deleteEntity(String entityType, Long entityId, String discipline, Long operatorId) {
        switch (entityType) {
            case "ISO": //"com.ose.tasks.entity.wbs.entity.ISOEntity":
//                isoEntityRepository.findById(entityId).ifPresent(iso->{
//                    iso.setDeletedAt();
//                    iso.setDeletedBy(operatorId);
//                    isoEntityRepository.save(iso);
//                });
                return true;
            case "SPOOL"://"com.ose.tasks.entity.wbs.entity.SpoolEntity":
//                spoolEntityRepository.findById(entityId).ifPresent(spool->{
//                    spool.setDeletedAt();
//                    spool.setDeletedBy(operatorId);
//                    spoolEntityRepository.save(spool);
//                });
                return true;
            case "PIPE_PIECE"://"com.ose.tasks.entity.wbs.entity.PipePieceEntity":
//                pipePieceEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    pipePieceEntityRepository.save(entity);
//                });
                return true;
            case "WELD_JOINT"://"com.ose.tasks.entity.wbs.entity.WeldEntity":
                if ("STRUCTURE".equalsIgnoreCase(discipline)) {
//                    structureWeldEntityRepository.findById(entityId).ifPresent(entity->{
//                        entity.setDeletedAt();
//                        entity.setDeletedBy(operatorId);
//                        structureWeldEntityRepository.save(entity);
//                    });
                } else if ("PIPING".equalsIgnoreCase(discipline)) {
//                    weldEntityRepository.findById(entityId).ifPresent(entity->{
//                        entity.setDeletedAt();
//                        entity.setDeletedBy(operatorId);
//                        weldEntityRepository.save(entity);
//                    });
                }
                return true;
            case "COMPONENT"://"com.ose.tasks.entity.wbs.entity.ComponentEntity":
//                componentEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    componentEntityRepository.save(entity);
//                });
                return true;
            case "TEST_PACKAGE"://"com.ose.tasks.entity.wbs.entity.TestPackageEntityBase":
//                ptpEntityRepository.findById(entityId).ifPresent(entity->{
//                entity.setDeletedAt();
//                entity.setDeletedBy(operatorId);
//                    ptpEntityRepository.save(entity);
//                });
                return true;
            case "CLEAN_PACKAGE"://"com.ose.tasks.entity.wbs.entity.CleanPackageEntityBase":
//                cpEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    cpEntityRepository.save(entity);
//                });
                return true;
            case "SUB_SYSTEM"://"com.ose.tasks.entity.wbs.entity.SubSystemEntityBase":
                systemEntityRepository.findById(entityId).ifPresent(entity->{
                    entity.setDeletedAt();
                    entity.setDeletedBy(operatorId);
                    systemEntityRepository.save(entity);
                });
                return true;
            case "WP01"://"com.ose.tasks.entity.wbs.entity.Wp01Entity":
//                wp01EntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    wp01EntityRepository.save(entity);
//                });
                return true;
            case "WP02"://"com.ose.tasks.entity.wbs.entity.Wp02Entity":
//                wp02EntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    wp02EntityRepository.save(entity);
//                });
                return true;
            case "WP03"://"com.ose.tasks.entity.wbs.entity.Wp03Entity":
//                wp03EntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    wp03EntityRepository.save(entity);
//                });
            return true;
            case "WP04"://"com.ose.tasks.entity.wbs.entity.Wp04Entity":
//                wp04EntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    wp04EntityRepository.save(entity);
//                });
                return true;
            case "WP05"://"com.ose.tasks.entity.wbs.entity.Wp05Entity":
//                wp05EntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    wp05EntityRepository.save(entity);
//                });
            return true;
            case "STRUCT_WELD_JOINT"://"com.ose.tasks.entity.wbs.entity.structureEntity
//                structureWeldEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    structureWeldEntityRepository.save(entity);
//                });
                return true;
            case "WP_MISC"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                wpMiscEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    wpMiscEntityRepository.save(entity);
//                });
                return true;
            case "OUT_FITTING"://"com.ose.tasks.entity.wbs.entity.outfitting":
//                outFittingEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    outFittingEntityRepository.save(entity);
//                });
                return true;
            case "CABLE"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                cableEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    cableEntityRepository.save(entity);
//                });
                return true;
            case "CABLE_PACKAGE"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                cablePackageEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    cablePackageEntityRepository.save(entity);
//                });
                return true;
            case "EIT_EQ"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                eitEqEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    eitEqEntityRepository.save(entity);
//                });
                return true;
            case "EIT_COMPONENT"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                eitComponentEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    eitComponentEntityRepository.save(entity);
//                });
                return true;
            case "MECH_EQ"://"com.ose.tasks.entity.wbs.entity.MechEqEntity":
            case "HVAC_EQ"://"com.ose.tasks.entity.wbs.entity.MechEqEntity":
            case "SAFETY_EQ"://"com.ose.tasks.entity.wbs.entity.MechEqEntity":
            case "ARCH_EQ"://"com.ose.tasks.entity.wbs.entity.MechEqEntity":
            case "MECHANICAL_EQUIPMENT"://"com.ose.tasks.entity.wbs.entity.MechanicalEquipmentEntity":
//                mechanicalEquipmentEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    mechanicalEquipmentEntityRepository.save(entity);
//                });
            return true;
            case "MECH_EQ_COMPONENT"://"com.ose.tasks.entity.wbs.entity.mechanical_equipment_component":
//                mechanicalEquipmentComponentEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    mechanicalEquipmentComponentEntityRepository.save(entity);
//                });
            return true;
            case "EL_CABLE":
            case "IN_CABLE":
            case "TE_CABLE":
//                cableEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    cableEntityRepository.save(entity);
//                });
                return true;
            case "EL_EQ":
            case "IN_EQ":
            case "TE_EQ":
//                eitEqEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    eitEqEntityRepository.save(entity);
//                });
                return true;
            case "EL_EQ_PACKAGE":
            case "IN_EQ_PACKAGE":
            case "TE_EQ_PACKAGE":
            case "HVAC_EQ_PACKAGE":
            case "MECH_EQ_PACKAGE":
            case "ARCH_EQ_PACKAGE":
            case "SAFETY_EQ_PACKAGE":
//                 eqPackageEntityRepository.findById(entityId).ifPresent(entity->{
//                    entity.setDeletedAt();
//                    entity.setDeletedBy(operatorId);
//                    eqPackageEntityRepository.save(entity);
//                });
                return true;
            default:
                return false;
        }
    }

    /**
     * 取得实体信息。
     *
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @param discipline 专业
     * @return 实体信息
     */
    @Override
    public boolean physicalDeleteEntity(String entityType, Long entityId, String discipline) {
        switch (entityType) {
            case "ISO": //"com.ose.tasks.entity.wbs.entity.ISOEntity":
//                isoEntityRepository.deleteById(entityId);
                return true;
            case "SPOOL"://"com.ose.tasks.entity.wbs.entity.SpoolEntity":
//                spoolEntityRepository.deleteById(entityId);
                return true;
            case "PIPE_PIECE"://"com.ose.tasks.entity.wbs.entity.PipePieceEntity":
//                pipePieceEntityRepository.deleteById(entityId);
            case "WELD_JOINT"://"com.ose.tasks.entity.wbs.entity.WeldEntity":
                if ("STRUCTURE".equalsIgnoreCase(discipline)) {
//                    structureWeldEntityRepository.deleteById(entityId);
                    return true;
                } else if ("PIPING".equalsIgnoreCase(discipline)) {
//                    weldEntityRepository.deleteById(entityId);
                    return true;
                }
            case "COMPONENT"://"com.ose.tasks.entity.wbs.entity.ComponentEntity":
//                componentEntityRepository.deleteById(entityId);
                return true;
            case "TEST_PACKAGE"://"com.ose.tasks.entity.wbs.entity.TestPackageEntityBase":
//                ptpEntityRepository.deleteById(entityId);
                return true;
            case "CLEAN_PACKAGE"://"com.ose.tasks.entity.wbs.entity.CleanPackageEntityBase":
//                cpEntityRepository.deleteById(entityId);
                return true;
            case "SUB_SYSTEM"://"com.ose.tasks.entity.wbs.entity.SubSystemEntityBase":
                systemEntityRepository.deleteById(entityId);
                return true;
            case "WP01"://"com.ose.tasks.entity.wbs.entity.Wp01Entity":
//                wp01EntityRepository.deleteById(entityId);
                return true;
            case "WP02"://"com.ose.tasks.entity.wbs.entity.Wp02Entity":
//                wp02EntityRepository.deleteById(entityId);
                return true;
            case "WP03"://"com.ose.tasks.entity.wbs.entity.Wp03Entity":
//                wp03EntityRepository.deleteById(entityId);
                return true;
            case "WP04"://"com.ose.tasks.entity.wbs.entity.Wp04Entity":
//                wp04EntityRepository.deleteById(entityId);
                return true;
            case "WP05"://"com.ose.tasks.entity.wbs.entity.Wp05Entity":
//                wp05EntityRepository.deleteById(entityId);
                return true;
            case "STRUCT_WELD_JOINT"://"com.ose.tasks.entity.wbs.entity.structureEntity
//                structureWeldEntityRepository.deleteById(entityId);
                return true;
            case "WP_MISC"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                wpMiscEntityRepository.deleteById(entityId);
                return true;
            case "OUT_FITTING"://"com.ose.tasks.entity.wbs.entity.outfitting":
//                outFittingEntityRepository.deleteById(entityId);
                return true;
            case "CABLE"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                cableEntityRepository.deleteById(entityId);
                return true;
            case "CABLE_PACKAGE"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                cablePackageEntityRepository.deleteById(entityId);
                return true;
            case "EIT_EQ"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                eitEqEntityRepository.deleteById(entityId);
                return true;
            case "EIT_COMPONENT"://"com.ose.tasks.entity.wbs.entity.WpMiscEntity":
//                eitComponentEntityRepository.deleteById(entityId);
                return true;
            case "MECH_EQ"://"com.ose.tasks.entity.wbs.entity.MechEqEntity":
            case "HVAC_EQ"://"com.ose.tasks.entity.wbs.entity.MechEqEntity":
            case "SAFETY_EQ"://"com.ose.tasks.entity.wbs.entity.MechEqEntity":
            case "ARCH_EQ"://"com.ose.tasks.entity.wbs.entity.MechEqEntity":
            case "MECHANICAL_EQUIPMENT"://"com.ose.tasks.entity.wbs.entity.MechanicalEquipmentEntity":
//                mechanicalEquipmentEntityRepository.deleteById(entityId);
                return true;
            case "MECH_EQ_COMPONENT"://"com.ose.tasks.entity.wbs.entity.mechanical_equipment_component":
//                mechanicalEquipmentComponentEntityRepository.deleteById(entityId);
                return true;
            case "EL_CABLE":
            case "IN_CABLE":
            case "TE_CABLE":
//                cableEntityRepository.deleteById(entityId);
                return true;
            case "EL_EQ":
            case "IN_EQ":
            case "TE_EQ":
//                eitEqEntityRepository.deleteById(entityId);
                return true;
            case "EL_EQ_PACKAGE":
            case "IN_EQ_PACKAGE":
            case "TE_EQ_PACKAGE":
            case "HVAC_EQ_PACKAGE":
            case "MECH_EQ_PACKAGE":
            case "ARCH_EQ_PACKAGE":
            case "SAFETY_EQ_PACKAGE":
//                eqPackageEntityRepository.deleteById(entityId);
                return true;
            default:
                return false;
        }
    }

    /**
     * 取得实体信息，并将其转为工作流参数。
     *
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @return 实体工作流参数对象
     */
    @Override
    public WorkflowProcessVariable getWorkflowEntityVariable(Long projectId,
                                                             String entityType,
                                                             Long entityId,
                                                             String funcPart) {

        WBSEntityBase entity = getEntity(projectId, entityType, entityId, funcPart);

        if (entity == null) {
            return null;
        }

        WorkflowProcessVariable variable = (WorkflowProcessVariable) entity;
        variable.setVariableFields();
        return variable;
    }

    /**
     * 设置期间。
     *
     * @param operator          操作者信息
     * @param parentEntry       上级条目信息
     * @param entityEntry       WBS 实体条目
     * @param wbsEntityPatchDTO 更新信息
     */
    @Override
    public void updateEntity(
        OperatorDTO operator,
        WBSEntry parentEntry,
        WBSEntry entityEntry,
        WBSEntityPatchDTO wbsEntityPatchDTO
    ) {

        if (parentEntry == null
            || entityEntry == null
            || entityEntry.getType() != WBSEntryType.ENTITY) {
            throw new NotFoundError();
        }

        ValueUtils.notNull(wbsEntityPatchDTO.getProportion(), entityEntry::setProportion);
        ValueUtils.notNull(wbsEntityPatchDTO.getFuncPart(), entityEntry::setFuncPart);
        ValueUtils.notNull(wbsEntityPatchDTO.getHierarchyNodeId(), entityEntry::setHierarchyNodeId);
        ValueUtils.notNull(wbsEntityPatchDTO.getParentHierarchyNodeId(), entityEntry::setParentHierarchyNodeId);
        ValueUtils.notNull(wbsEntityPatchDTO.getModuleHierarchyNodeId(), entityEntry::setModuleHierarchyNodeId);

        // 创建前置任务关系数据
        if (wbsEntityPatchDTO.getPredecessors() != null) {

            wbsEntryRelationRepository
                .deleteBySuccessorGUID(parentEntry.getProjectId(), entityEntry.getGuid());

            for (WBSRelationDTO predecessor : wbsEntityPatchDTO.getPredecessors()) {
                saveWBSEntryRelations(
                    operator,
                    parentEntry.getProjectId(),
                    predecessor.getId().toString(),
                    entityEntry.getGuid(),
                    predecessor.getType(),
                    false
                );
            }

        }

        // 创建后续任务关系数据
        if (wbsEntityPatchDTO.getSuccessors() != null) {

            wbsEntryRelationRepository
                .deleteByPredecessorGUID(parentEntry.getProjectId(), entityEntry.getGuid());

            for (WBSRelationDTO successor : wbsEntityPatchDTO.getSuccessors()) {
                saveWBSEntryRelations(
                    operator,
                    parentEntry.getProjectId(),
                    entityEntry.getGuid(),
                    successor.getId().toString(),
                    successor.getType(),
                    false
                );
            }

        }

        updateEntry(operator, parentEntry, entityEntry, wbsEntityPatchDTO);
    }

    /**
     * 更新 WBS 条目。
     *
     * @param operator         操作者信息
     * @param parentEntry      上级条目信息
     * @param entry            条目信息
     * @param wbsEntryPatchDTO 条目更新数据传输对象
     */
    @Override
    public void updateEntry(
        final OperatorDTO operator,
        WBSEntry parentEntry,
        final WBSEntry entry,
        final WBSEntryPatchDTO wbsEntryPatchDTO
    ) {

        if (entry == null) {
            throw new NotFoundError();
        }

        if (parentEntry == null) {
            if (entry.getParentId() == null) {
                parentEntry = entry;
            } else {
                throw new NotFoundError();
            }
        }

        WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(entry.getId());
        WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(entry.getId());
        if (wbsEntryBlob == null || wbsEntryState == null) return;
        ValueUtils.notNull(wbsEntryPatchDTO.getRemarks(), wbsEntryBlob::setRemarks);
        entry.setStartAt(ValueUtils.notNull(
            wbsEntryPatchDTO.getStartAt(),
            entry.getStartAt(),
            parentEntry.getStartAt()
        ));
        entry.setFinishAt(ValueUtils.notNull(
            wbsEntryPatchDTO.getFinishAt(),
            entry.getFinishAt(),
            parentEntry.getFinishAt()
        ));
        entry.setStartBeforeHours(ValueUtils.notNull(
            wbsEntryPatchDTO.getStartBeforeHours(),
            entry.getStartBeforeHours(),
            parentEntry.getStartBeforeHours()
        ));
        /* TODO 根据日历计算经历时长并设置时长单位 ... */
        entry.setDuration(ValueUtils.notNull(
            wbsEntryPatchDTO.getDuration(),
            entry.getDuration(),
            parentEntry.getDuration()
        ));
        entry.setDurationUnit(ValueUtils.ifNull(
            wbsEntryPatchDTO.getDuration(),
            ValueUtils.notNull(entry.getDurationUnit(), parentEntry.getDurationUnit()),
            TimeUnit.HOURS
        ));

        entry.setLastModifiedAt();
        entry.setLastModifiedBy(operator.getId());
        entry.setSort(ValueUtils.ifNull(entry.getSort(), Long.MAX_VALUE));
        entry.setActive(ValueUtils.notNull(entry.getActive(), parentEntry.getActive(), true));

        wbsEntryRepository.save(entry);

        wbsEntryRepository.updateStartBeforeHours(
            entry.getId(),
            entry.getStartBeforeHours()
        );

        // 启用/停用 WBS 条目
        if (wbsEntryPatchDTO.getActive() != null) {
            if (wbsEntryPatchDTO.getActive()) {
                active(operator, entry);
            } else if (!wbsEntryPatchDTO.getActive()) {
                inactive(operator, entry);
            }
        }

        // 更新条目权重
        if (wbsEntryPatchDTO.getScore() != null
            && wbsEntryPatchDTO.getScore() != wbsEntryState.getTotalScore()) {
            setScore(
                operator,
                entry.getOrgId(),
                entry.getProjectId(),
                entry.getId(),
                wbsEntryPatchDTO.getScore()
            );
        }

        // 延长上级条目的起止时间/缩减子条目的起止时间
        if ((entry.getType() == WBSEntryType.UNITS || entry.getType() == WBSEntryType.WORK)
            && entry.getStartAt() != null
            && entry.getFinishAt() != null) {

            final long startAt = entry.getStartAt().getTime();
            final long finishAt = entry.getFinishAt().getTime();
            final Set<Long> parentIDs = new HashSet<>();
            StringUtils.findAll(ENTITY_ID_PATTERN, wbsEntryBlob.getPath()).
                forEach(parentID -> {
                    parentIDs.add(LongUtils.parseLong(parentID));
                });

//            final String path = String.format("%s%s/%%", entry.getPath(), entry.getId());
            final Long wbsEntryAncestorId = entry.getId();

            // 延长上级条目的起止时间
            wbsEntryRepository
                .findParentsByStartAtAndFinishAt(entry.getProjectId(), parentIDs, entry.getStartAt(), entry.getFinishAt())
                .forEach(parent -> {

                    final long durationStartAt = ValueUtils.notNull(parent.getStartAt(), entry.getStartAt()).getTime();
                    final long durationFinishAt = ValueUtils.notNull(parent.getFinishAt(), entry.getFinishAt()).getTime();

                    long parentStartAt = durationStartAt;
                    long parentFinishAt = durationFinishAt;

                    if (durationStartAt > startAt) {
                        parentStartAt = startAt;
                    }

                    if (durationFinishAt < finishAt) {
                        parentFinishAt = finishAt;
                    }

                    parent.setStartAt(new Date(parentStartAt));
                    parent.setFinishAt(new Date(parentFinishAt));

                    wbsEntryRepository.save(parent);
                });

            // 缩减子条目的起止时间
            wbsEntryRepository
                .groupChildByStartAtAndFinishAt(entry.getProjectId(), wbsEntryAncestorId, entry.getStartAt(), entry.getFinishAt())
                .forEach(durationGroup -> {

                    final long durationStartAt = ValueUtils.notNull(durationGroup.getStartAt(), entry.getStartAt()).getTime();
                    final long durationFinishAt = ValueUtils.notNull(durationGroup.getFinishAt(), entry.getFinishAt()).getTime();
                    final long milliseconds = durationFinishAt - durationStartAt;

                    long childStartAt;
                    long childFinishAt;

                    if (durationStartAt < startAt) {
                        childStartAt = startAt;
                        childFinishAt = Math.min(finishAt, startAt + milliseconds);
                    } else if (durationFinishAt > finishAt) {
                        childStartAt = Math.max(startAt, finishAt - milliseconds);
                        childFinishAt = finishAt;
                    } else {
                        return;
                    }

                    wbsEntryRepository.updateDuration(
                        entry.getProjectId(),
                        wbsEntryAncestorId,
                        durationGroup.getStartAt(),
                        durationGroup.getFinishAt(),
                        new Date(childStartAt),
                        new Date(childFinishAt)
                    );

                });

        }

    }

    /**
     * 取得计划条目的 GUID。
     *
     * @param projectId            项目 ID
     * @param wbsEntryId           计划条目 ID
     * @param notFoundErrorMessage 未取得计划条目时的错误消息
     * @return 计划条目 GUID
     */
    private String getWBSEntryGUID(
        final Long projectId,
        final String wbsEntryId,
        final String notFoundErrorMessage
    ) {

        if (RegExpUtils.isGUID(wbsEntryId)) {
            return wbsEntryId;
        }

        String redisKey = "PROJECT:" + projectId + ":WBS:" + wbsEntryId + ":GUID";
        String guid = getRedisKey(redisKey, 60);

        if (guid != null) {
            return guid;
        }

        Optional<WBSEntry> result = wbsEntryRepository.findById(LongUtils.parseLong(wbsEntryId));

        if (!result.isPresent()) {
            throw new BusinessError(String.format(notFoundErrorMessage, wbsEntryId)); // TODO
        }

        guid = result.get().getGuid();
        setRedisKey(redisKey, guid, 60);
        return guid;
    }

    /**
     * 保存任务关系。
     *
     * @param operator      操作这信息
     * @param projectId     项目 ID
     * @param predecessorId 前置任务 GUID
     * @param successorId   后置任务 GUID
     * @param optional      是否为可选前置任务
     */
    @Override
    public void saveWBSEntryRelations(
        final OperatorDTO operator,
        final Long projectId,
        String predecessorId,
        String successorId,
        final RelationType relationType,
        final Boolean optional
    ) {
        predecessorId = getWBSEntryGUID(projectId, predecessorId, "指定的前置任务不存在（%s）");
        successorId = getWBSEntryGUID(projectId, successorId, "指定的后置任务不存在（%s）");

        WBSEntryRelation relation = wbsEntryRelationRepository
            .findByProjectIdAndPredecessorIdAndSuccessorId(
                projectId,
                predecessorId,
                successorId
            )
            .orElse(null);

        if (relation != null
            && !relation.getDeleted()
            && relationType == relation.getRelationType()) {
            relation.setVersion(new Date().getTime());
//            return;
        } else if (relation != null) {
            relation.setDeleted(false);
        } else {
            relation = new WBSEntryRelation();
            relation.setProjectId(projectId);
            relation.setPredecessorId(predecessorId);
            relation.setSuccessorId(successorId);
            relation.setCreatedAt();
            relation.setCreatedBy(operator.getId());
        }

        relation.setRelationType(relationType);
        relation.setOptional(optional);
        relation.setLastModifiedAt();
        relation.setLastModifiedBy(operator.getId());
        relation.setStatus(ACTIVE);

        wbsEntryRelationRepository.save(relation);
    }

    /**
     * 设置 WBS 条目的权重。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
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

        // 取得 WBS 条目信息
        WBSEntry wbsEntry = wbsEntryRepository
            .findByProjectIdAndIdAndActiveIsTrueAndDeletedIsFalse(projectId, wbsEntryId)
            .orElse(null);

        if (wbsEntry != null && !wbsEntry.getActive()) {
            return;
        }

        if (wbsEntry == null) {
            throw new NotFoundError("error.wbs.not-found");
        }
        WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntryId);
        if (wbsEntryState == null) {
            throw new NotFoundError(wbsEntryId.toString());
        }
        double totalScoreIncrement = score - wbsEntryState.getTotalScore();

        // 若权重未更新则结束
        if (totalScoreIncrement == 0) {
            return;
        }

        // 更新所有子条目的权重，并取得已完成条目的权重的总和
        double childrenFinishedScore = setChildrenScore(projectId, wbsEntryId, score);

        // 更新目标条目信息
        wbsEntryState.setTotalScore(score);
        wbsEntryState.setFinishedScore(wbsEntryState.isFinished() ? score : childrenFinishedScore);
        wbsEntryState.setLastModifiedBy(operator.getId());
        wbsEntryState.setLastModifiedAt(new Date());
        wbsEntryStateRepository.save(wbsEntryState);
        updateFinishedScoreOfParents(wbsEntry);
    }

    /**
     * 更新子条目权重。
     *
     * @param projectId 项目 ID
     * @param parentId  上级条目 ID
     * @param newScore  总权重
     */
    private double setChildrenScore(
        final Long projectId,
        final Long parentId,
        final double newScore
    ) {

        // 取得所有子条目
        List<WBSEntryState> childrenState = wbsEntryStateRepository
            .findByProjectIdAndParentIdAndActiveIsTrueAndDeletedIsFalse(projectId, parentId);

        int childCount = childrenState.size();

        double parentFinishedScore = 0;

        if (childCount == 0) {
            return parentFinishedScore;
        }

        double originalTotalScore = 0;
        double childTotalScore;
        double childrenFinishedScore;

        for (WBSEntryState childState : childrenState) {
            originalTotalScore += childState.getTotalScore();
        }

        for (WBSEntryState childState : childrenState) {

            // 若上级原权重为 0，则子条目评分上级的新权重
            if (originalTotalScore == 0) {
                childTotalScore = newScore / childCount;
                // 否则根据子条目权重占上级原权重的比分配上级的增量权重
            } else {
                childTotalScore = childState.getTotalScore() * newScore / originalTotalScore;
            }

            // 递归更新下级条目权重
            childrenFinishedScore = setChildrenScore(
                projectId,
                childState.getWbsEntryId(),
                childTotalScore
            );

            // 若当前子条目已完成，则当前子条目的已完成权重为该子条目的权重
            if (childState.isFinished()) {
                childrenFinishedScore = childTotalScore;
                parentFinishedScore += childrenFinishedScore;
                // 否则取该子条目的所有子条目的已完成权重的总和
            } else {
                parentFinishedScore += childrenFinishedScore;
            }

            childState.setTotalScore(childTotalScore);
            childState.setFinishedScore(childrenFinishedScore);

            // 更新子条目信息
            wbsEntryStateRepository.save(childState);
        }

        return parentFinishedScore;
    }

    /**
     * 取得实体的工序信息并生成工序-工序 ID 映射表。
     *
     * @param projectId 项目 ID
     * @return 工序-工序 ID 映射表
     */
    @Override
    public Map<String, Long> getProcesses(final Long projectId) {

        final Map<String, Long> processes = new HashMap<>();

        entityProcessRepository
            .findByProjectId(projectId)
            .forEach(entityProcess -> processes.put(
                entityProcess.getId(),
                entityProcess.getProcessId()
            ));

        return processes;
    }

    // 计划条目排序逻辑
    private static final Comparator<WBSEntryDTO> wbsEntryComparator = (a, b) -> {
        if (!((a.getType() == WBSEntryType.WORK
            || a.getType() == WBSEntryType.ENTITY)
            && (a.getType() == b.getType()))) {
            return 0;
        }
        if (a.getName() == null && b.getName() == null) {
            return 0;
        }
        if (a.getName() == null) {
            return 1;
        }
        if (b.getName() == null) {
            return -1;
        }
        int result = a.getName().compareTo(b.getName());
        if (result == 0) {
            return a.getNo().compareTo(b.getNo());
        }
        return result;
    };

    /**
     * 计划条目排序。
     *
     * @param wbsEntries 计划条目列表
     * @return 排序后的计划条目列表
     */
    @Override
    public List<WBSEntryDTO> sortWBS(List<WBSEntryDTO> wbsEntries) {
        if (wbsEntries == null || wbsEntries.size() == 0) {
            return wbsEntries;
        }
        wbsEntries.sort(wbsEntryComparator);
        wbsEntries.forEach(wbsEntry -> sortWBS(wbsEntry.getChildren()));
        return wbsEntries;
    }
}
