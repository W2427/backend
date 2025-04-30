package com.ose.tasks.domain.model.service.plan;

import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.Organization;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.jpql.VersionQLDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.ProjectNodeModuleTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityNodePrivilegeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.process.BpmnTaskRelationRepository;
import com.ose.tasks.domain.model.repository.process.EntityProcessRepository;
import com.ose.tasks.domain.model.repository.process.EntityTypeRepository;
import com.ose.tasks.domain.model.repository.process.ModuleProcessDefinitionRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageAssignNodePrivilegesRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.service.plan.business.PlanBusinessInterface;
import com.ose.tasks.dto.WBSEntryDTO;
import com.ose.tasks.dto.WBSEntryDelegateDTO;
import com.ose.tasks.dto.WBSEntryUserDelegateDTO;
import com.ose.tasks.dto.process.EntityProcessRelationsDTO;
import com.ose.tasks.dto.wbs.WBSEntryQueryDTO;
import com.ose.tasks.entity.BpmnTaskRelation;
import com.ose.tasks.entity.ProjectNodeModuleType;
import com.ose.tasks.entity.bpm.BpmActivityNodePrivilege;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.taskpackage.TaskPackageAssignNodePrivileges;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.tasks.vo.wbs.WBSEntryType;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

/**
 * 计划管理服务。
 */
@Component
public class WBSSearchService implements WBSSearchInterface {

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 可被自动启动的四级计划运行状态
    private static final Set<WBSEntryRunningStatus> RUNNING_STATUS_TO_BE_STARTED = new HashSet<>();

    static {
        RUNNING_STATUS_TO_BE_STARTED.add(WBSEntryRunningStatus.PENDING);
    }

    // WBS 条目数据仓库
    private final WBSEntryRepository wbsEntryRepository;

    // WBS 条目数据仓库
    private final WBSEntryWithRelationsRepository wbsEntryWithRelationsRepository;

    private final WBSEntryPredecessorRepository wbsEntryPredecessorRepository;

    private final WBSEntrySuccessorRepository wbsEntrySuccessorRepository;

    // WBS 条目关系数据仓库
    // 实体类型视图数据仓库
    private final EntityTypeRepository entityTypeRepository;

    // 实体工序视图数据仓库
    private final EntityProcessRepository entityProcessRepository;

    // 工序-实体类型关系数据仓库
    private final ProcessEntityTypeRepository processEntityTypeRepository;

    // 项目节点模块类型视图仓库
    private final ProjectNodeModuleTypeRepository projectNodeModuleTypeRepository;

    // 模块工作流定义数据仓库
    private final ModuleProcessDefinitionRepository moduleProcessDefinitionRepository;

    // 工序
    private final BpmProcessRepository bpmProcessRepository;

    // 工序流程权限
    private final BpmActivityNodePrivilegeRepository bpmActivityNodePrivilegeRepository;

    // 组织
    private final OrganizationFeignAPI organizationFeignAPI;

    // 用户
    private final UserFeignAPI userFeignAPI;

    private final WBSEntryActivityInstanceRepository wbsEntryActivityInstanceRepository;

    // 计划条目活动实例视图
    private final WBSEntryDelegateRepository wbsEntryDelegateRepository;

    // Activiti 工序引擎
    private final BpmnTaskRelationRepository bpmnTaskRelationRepository;

    // 计划业务接口
    private final PlanBusinessInterface planBusiness;

    private final TaskPackageAssignNodePrivilegesRepository assignNodePrivilegesRepository;

    private final WBSEntryBlobRepository wbsEntryBlobRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final WBSEntryPlainRepository wbsEntryPlainRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public WBSSearchService(
        WBSEntryRepository wbsEntryRepository,
        WBSEntryWithRelationsRepository wbsEntryWithRelationsRepository,
        WBSEntryPredecessorRepository wbsEntryPredecessorRepository,
        WBSEntrySuccessorRepository wbsEntrySuccessorRepository,
        EntityTypeRepository entityTypeRepository,
        EntityProcessRepository entityProcessRepository,
        ProcessEntityTypeRepository processEntityTypeRepository,
        ProjectNodeModuleTypeRepository projectNodeModuleTypeRepository,
        ModuleProcessDefinitionRepository moduleProcessDefinitionRepository,
        BpmProcessRepository bpmProcessRepository,
        BpmActivityNodePrivilegeRepository bpmActivityNodePrivilegeRepository,
        WBSEntryDelegateRepository wbsEntryDelegateRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            OrganizationFeignAPI organizationFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UserFeignAPI userFeignAPI,
        WBSEntryActivityInstanceRepository wbsEntryActivityInstanceRepository,
        BpmnTaskRelationRepository bpmnTaskRelationRepository, PlanBusinessInterface planBusiness,
        TaskPackageAssignNodePrivilegesRepository assignNodePrivilegesRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        WBSEntryPlainRepository wbsEntryPlainRepository) {
        this.wbsEntryRepository = wbsEntryRepository;
        this.wbsEntryWithRelationsRepository = wbsEntryWithRelationsRepository;
        this.wbsEntryPredecessorRepository = wbsEntryPredecessorRepository;
        this.wbsEntrySuccessorRepository = wbsEntrySuccessorRepository;
        this.entityTypeRepository = entityTypeRepository;
        this.entityProcessRepository = entityProcessRepository;
        this.processEntityTypeRepository = processEntityTypeRepository;
        this.projectNodeModuleTypeRepository = projectNodeModuleTypeRepository;
        this.moduleProcessDefinitionRepository = moduleProcessDefinitionRepository;
        this.bpmProcessRepository = bpmProcessRepository;
        this.bpmActivityNodePrivilegeRepository = bpmActivityNodePrivilegeRepository;
        this.wbsEntryDelegateRepository = wbsEntryDelegateRepository;
        this.organizationFeignAPI = organizationFeignAPI;
        this.userFeignAPI = userFeignAPI;
        this.wbsEntryActivityInstanceRepository = wbsEntryActivityInstanceRepository;
        this.bpmnTaskRelationRepository = bpmnTaskRelationRepository;
        this.planBusiness = planBusiness;
        this.assignNodePrivilegesRepository = assignNodePrivilegesRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryPlainRepository = wbsEntryPlainRepository;
    }

    /**
     * 取得工序-实体类型映射表。
     * 映射表键值对形式：
     * 键：【工序阶段/工序/实体类型】字符串
     * 值：实体子类型集合
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 工序-实体类型映射表
     */
    @Override
    public EntityProcessRelationsDTO getProcessEntityTypeRelations(Long orgId, Long projectId) {
        return new EntityProcessRelationsDTO(
            entityTypeRepository.findByProjectId(projectId),
            entityProcessRepository.findByProjectId(projectId),
            processEntityTypeRepository.findByOrgIdAndProjectId(orgId, projectId)
        );
    }

    /**
     * 取得 WBS 更新版本号。
     *
     * @param projectId 项目 ID
     * @return WBS 更新版本号 DTO
     */
    @Override
    public VersionQLDTO getWBSVersionDTO(Long projectId) {
        BigInteger version = wbsEntryRepository.getWBSVersion(projectId);
        version = version == null ? BigInteger.ZERO :version;
        return new VersionQLDTO(version.longValue());

    }

    /**
     * 取得 WBS 更新版本号。
     *
     * @param projectId 项目 ID
     * @return WBS 更新版本号
     */
    @Override
    public Long getWBSVersion(Long projectId) {
        VersionQLDTO versionDTO = getWBSVersionDTO(projectId);
        return versionDTO == null ? null : versionDTO.getVersion();
    }

    /**
     * 取得 WBS 层级结构。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param queryDTO   查询条件
     * @return WBS 条目列表
     */
    @Override
    public List<WBSEntryDTO> getWBSHierarchy(
        final Long orgId,
        final Long projectId,
        final Long wbsEntryId,
        WBSEntryQueryDTO queryDTO
    ) {

        WBSEntry rootWbsEntry = null;
        WBSEntryBlob rootWbsEntryBlob = null;

        // 取得指定的根节点信息
        if (!(LongUtils.isEmpty(wbsEntryId) || projectId.equals(wbsEntryId))) {

            rootWbsEntry = wbsEntryRepository
                .findById(wbsEntryId)
                .orElse(null);
            rootWbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntryId);
            if (rootWbsEntry == null || rootWbsEntryBlob == null) {
                throw new BusinessError("指定的上级不存在"); // TODO
            }

        }

//        List<WBSEntryWithRelations> wbsEntries = wbsEntryWithRelationsRepository
//            .search(projectId, rootWbsEntry, queryDTO, rootWbsEntryBlob.getPath());

        List<WBSEntryPlain> wbsEntries = wbsEntryRepository
            .searchWbsEntry(projectId, rootWbsEntry, queryDTO, rootWbsEntryBlob == null ? "/" : rootWbsEntryBlob.getPath());

        if (wbsEntries.size() == 0) {
            return null;
        }

        List<WBSEntryDTO> wbsEntryDTOs = BeanUtils.convertType(wbsEntries, WBSEntryDTO.class);
        Map<Long, WBSEntryDTO> wbsEntryDtoMap = new HashMap<>();
        WBSEntryDTO parentWBSEntryDTO;
        List<WBSEntryDTO> result = new ArrayList<>();

        // 构造树形层级结构
        for (WBSEntryDTO wbsEntryDTO : wbsEntryDTOs) {

            wbsEntryDtoMap.put(wbsEntryDTO.getId(), wbsEntryDTO);

            parentWBSEntryDTO = wbsEntryDtoMap.get(wbsEntryDTO.getParentId());

            if (parentWBSEntryDTO == null) {
                result.add(wbsEntryDTO);
                continue;
            }

            parentWBSEntryDTO.addChild(wbsEntryDTO);
        }

        return planBusiness.sortWBS(result);
    }

    /**
     * 取得前/后置任务 ID 集合。
     *
     * @param entries 计划条目
     * @return 前/后置任务 ID 集合
     */
    private Set<Long> getReferencedWBSEntryIDs(List<WBSEntryDTO> entries) {

        final Set<Long> referencedEntryIDs = new HashSet<>();

        if (entries == null) {
            return referencedEntryIDs;
        }

        entries.forEach(entry -> {

            if (entry.getPredecessors() != null) {
                entry.getPredecessors().forEach(predecessor -> {
                    if (predecessor.getPredecessor() != null) {
                        referencedEntryIDs.add(predecessor.getPredecessor().getId());
                    }
                });
            }

            if (entry.getSuccessors() != null) {
                entry.getSuccessors().forEach(successor -> {
                    if (successor.getSuccessor() != null) {
                        referencedEntryIDs.add(successor.getSuccessor().getId());
                    }
                });
            }

            if (entry.getChildren() != null) {
                referencedEntryIDs
                    .addAll(getReferencedWBSEntryIDs(entry.getChildren()));
            }

        });

        return referencedEntryIDs;
    }

    /**
     * 取得前/后置任务信息。
     *
     * @param projectId 项目 ID
     * @param entries   计划条目
     * @return 前/后置任务信息
     */
    @Override
    public Map<Long, Object> getReferencedWBSEntries(
        final Long projectId,
        final List<WBSEntryDTO> entries
    ) {

        Map<Long, Object> referencedEntries = new HashMap<>();

        wbsEntryRepository
            .findByProjectIdAndIdInOrderByDepthAsc(projectId, getReferencedWBSEntryIDs(entries))
            .forEach(entry -> referencedEntries.put(entry.getId(), new WBSEntryDTO(entry)));

        return referencedEntries;
    }

    /**
     * 取得 WBS 条目详细信息。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     */
    @Override
    public WBSEntry get(Long projectId, Long wbsEntryId) {
        return wbsEntryRepository
            .findByProjectIdAndIdAndDeletedIsFalse(projectId, wbsEntryId)
            .orElse(null);
        //return wbsEntryWithRelationsDetailRepository
        //    .findByProjectIdAndIdAndDeletedIsFalse(projectId, wbsEntryId)
        //    .orElse(null);
    }

    /**
     * 取得 WBS 条目前置任务列表。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param pageable   分页参数
     */
    @Override
    public Page<WBSEntryPredecessorDetail> predecessors(
        final Long projectId,
        final Long wbsEntryId,
        final Pageable pageable
    ) {

        WBSEntry entry = get(projectId, wbsEntryId);

        if (entry == null) {
            throw new NotFoundError();
        }

        Page<WBSEntryPredecessorDetail> wbsps = wbsEntryPredecessorRepository
            .findByProjectIdAndSuccessorIdAAndOptionalIsNotTrue(projectId, entry.getGuid(), pageable);
        List<WBSEntryPredecessorDetail> wbspl = wbsps.getContent();
        if(!CollectionUtils.isEmpty(wbspl)) {
            wbspl.forEach(wbsp -> {
                WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsp.getPredecessor().getId());
                if(wbsEntryState != null) {
                    wbsp.setRunningStatus(wbsEntryState.getRunningStatus());
                }
            });
        }

        return wbsps;
    }

    /**
     * 取得 WBS 条目后置任务列表。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param pageable   分页参数
     */
    @Override
    public Page<WBSEntrySuccessorDetail> successors(
        final Long projectId,
        final Long wbsEntryId,
        final Pageable pageable
    ) {

        WBSEntry entry = get(projectId, wbsEntryId);

        if (entry == null) {
            throw new NotFoundError();
        }

        return wbsEntrySuccessorRepository
            .findByProjectIdAndPredecessorIdAndDeletedIsFalse(projectId, entry.getGuid(), pageable);
    }

    /**
     * 取得 WBS 条目的所有上级的信息。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param appendSelf 是否返回条目自身信息
     */
    @Override
    public List<WBSEntryPlain> getParents(Long projectId, Long wbsEntryId, boolean appendSelf) {

        WBSEntry wbsEntry = get(projectId, wbsEntryId);

        if (wbsEntry == null) {
            throw new NotFoundError();
        }
        WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntryId);
        if(wbsEntryBlob == null) {
            throw new NotFoundError();
        }
        Set<Long> wbsEntryIDs = wbsEntryBlob.getParentIDs();

        if (appendSelf) {
            wbsEntryIDs.add(wbsEntryId);
        }

        return wbsEntryPlainRepository
            .findByIdIn(wbsEntryIDs);
    }

    /**
     * 取得 WBS 实体条目。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param moduleId  模块层级 ID
     */
    @Override
    public List<WBSEntry> getWBSEntityEntries(
        final Long orgId,
        final Long projectId,
        final Long moduleId
    ) {

        Set<String> moduleEntityTypes = new HashSet<>();
        moduleEntityTypes.add("WP01");
        moduleEntityTypes.add("SYSTEM");

        // 取得模块节点的模块类型信息
        ProjectNodeModuleType projectNodeModuleType = projectNodeModuleTypeRepository
            .findByProjectIdAndIdAndEntityTypeIn(projectId, moduleId, moduleEntityTypes)
            .orElse(null);

        if (projectNodeModuleType == null) {
            throw new NotFoundError();
        }

        // 取得指定类型模块的工作流定义信息
        final List<ModuleProcessDefinition> moduleProcessDefinitions = new ArrayList<>();
         moduleProcessDefinitions.add(moduleProcessDefinitionRepository
            .findByOrgIdAndProjectIdAndFuncPartAndDeletedIsFalse(orgId, projectId, "TODO_STR"));


        if (CollectionUtils.isEmpty(moduleProcessDefinitions)) {
            throw new BusinessError("error.module-workflow.not-deployed");
        }

        Set<Long> moduleProcessDefinitionIds = new HashSet<>();
        moduleProcessDefinitions.forEach(moduleProcessDefinition -> moduleProcessDefinitionIds.add(moduleProcessDefinition.getId()));

        List<BpmnTaskRelation> bpmnTaskRelations = bpmnTaskRelationRepository.
            findByOrgIdAndProjectIdAndModuleProcessDefinitionIdInAndDeletedIsFalse(
                orgId,
                projectId,
                moduleProcessDefinitionIds

            );
        List<String> categories = new ArrayList<>();
        bpmnTaskRelations.forEach(bpmnTaskRelation -> {
            if (!StringUtils.isEmpty(bpmnTaskRelation.getCategory())) {
                categories.add(bpmnTaskRelation.getCategory());
            }
        });

        List<WBSEntry> wbsEntityEntries = new ArrayList<>();

        for (String category : categories) { //对于每一个 stage/process/entityType/entitySubType
            String[] categoryArr = category.split("/");
            String stage = categoryArr[0];
            String process = categoryArr[1];
            String entityType = categoryArr[2];
            Set<String> entitySubTypes = new HashSet<String>() {{
                add(categoryArr[3]);
            }};

            Long processId = processEntityTypeRepository.findByProjectIdAndStageNameAndProcessNameAndEntityTypeAndEntitySubType(
                projectId, stage, process, entityType, categoryArr[3]);

            List<WBSEntry> wbsEntries = wbsEntryRepository
                .findByProjectIdAndProcessIdAndEntityTypeAndEntitySubTypeInAndActiveIsTrueAndDeletedIsFalse(
                    projectId,
                    processId,
                    entityType,
                    entitySubTypes
                );

//            wbsEntries.removeIf(wbsEntry -> (wbsEntry.getStartedAt() != null));
            wbsEntityEntries.addAll(wbsEntries);

        }
        return wbsEntityEntries;
    }

    /**
     * 取得 WBS 三级计划跟四级计划设定的用户跟施工组的信息
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId 计划 ID
     */
    @Override
    public WBSEntryDelegateDTO getWBSEntryDelegateAndTeam(Long orgId, Long projectId, Long wbsEntryId) {
        WBSEntryDelegateDTO wBSEntryDelegateDTO = new WBSEntryDelegateDTO();
        List<WBSEntryUserDelegateDTO> userPrivileges = new ArrayList<>();
        Optional<WBSEntry> wBSEntryOpt = wbsEntryRepository.findById(wbsEntryId);
        if (!wBSEntryOpt.isPresent()) return null;
        WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntryId);
        if(wbsEntryState == null) return null;

        WBSEntry wBSEntry = wBSEntryOpt.get();
        String stageName = wBSEntry.getStage();
        String processName = wBSEntry.getProcess();
        Long teamId = null;
        BpmProcess bpmProcess = bpmProcessRepository
            .findByOrgIdAndProjectIdAndStageNameAndNameAndFuncPart(orgId, projectId, stageName, processName, wBSEntry.getFuncPart())
            .orElse(null);
        // 如果不是三级计划，也不是四级计划，那么要出错误
        if (wBSEntry.getType() != WBSEntryType.WORK && wBSEntry.getType() != WBSEntryType.ENTITY) {
            return null;
        }
        if (bpmProcess == null) {
            return null;
        }

        Map<String, WBSEntryDelegate> wBSEntryDelegateMap = new HashMap<>();
        if (wBSEntry.getType() == WBSEntryType.WORK) {
            List<WBSEntryDelegate> wBSEntryDelegates = wbsEntryDelegateRepository.findByWbsEntryIdAndDeletedIsFalse(wbsEntryId);
            for (WBSEntryDelegate wBSEntryDelegate : wBSEntryDelegates) {
                if (!LongUtils.isEmpty(wBSEntryDelegate.getUserId())) {
                    wBSEntryDelegateMap.put(wBSEntryDelegate.getPrivilege().name(), wBSEntryDelegate);
                }
            }

            if (wbsEntryState.getTeamId() != null) {
                    teamId = wbsEntryState.getTeamId();
                }
        } else if (wBSEntry.getType() == WBSEntryType.ENTITY) {
            List<WBSEntryDelegate> wBSEntryDelegates = wbsEntryDelegateRepository.findByWbsEntryIdAndDeletedIsFalse(wBSEntry.getParentId());
            for (WBSEntryDelegate wBSEntryDelegate : wBSEntryDelegates) {
                if (!LongUtils.isEmpty(wBSEntryDelegate.getUserId())) {
                    wBSEntryDelegateMap.put(wBSEntryDelegate.getPrivilege().name(), wBSEntryDelegate);
                }
            }
            WBSEntry wbsEntryParent = wbsEntryRepository.findById(wBSEntry.getParentId()).orElse(null);
            WBSEntryState wbsEntryParentState = wbsEntryStateRepository.findByWbsEntryId(wBSEntry.getParentId());
            if (wbsEntryParent != null && wbsEntryParentState != null && wbsEntryParentState.getTeamId() != null) {
                teamId = wbsEntryParentState.getTeamId();
            }
            if (wBSEntry != null && wbsEntryState.getTaskPackageId() != null) {
                //查询任务包工序任务分配
                List<TaskPackageAssignNodePrivileges> taskPackageAssignNodePrivileges = assignNodePrivilegesRepository
                    .findByProjectIdAndTaskPackageIdAndProcessId(projectId, wbsEntryState.getTaskPackageId(), bpmProcess.getId());

                // 如果任务包设定的人员存在会覆盖三级计划
                for (TaskPackageAssignNodePrivileges privileges : taskPackageAssignNodePrivileges) {
                    if (privileges.getUserId() != null) {
                        WBSEntryDelegate wbsEntryDelegate = new WBSEntryDelegate();
                        wbsEntryDelegate.setTeamId(privileges.getTeamId());
                        wbsEntryDelegate.setUserId(privileges.getUserId());
                        wbsEntryDelegate.setWbsEntryId(wBSEntry.getId());
                        wbsEntryDelegate.setPrivilege(privileges.getPrivilege());
                        wBSEntryDelegateMap.put(privileges.getPrivilege().name(), wbsEntryDelegate);
                    }
                }
            }
        }

        List<BpmActivityNodePrivilege> bpmActivityNodePrivileges = bpmActivityNodePrivilegeRepository.findByOrgIdAndProjectIdAndProcessIdAndStatus(orgId, projectId, bpmProcess.getId(), EntityStatus.ACTIVE);
        for (BpmActivityNodePrivilege bpmActivityNodePrivilege : bpmActivityNodePrivileges) {
            WBSEntryUserDelegateDTO wBSEntryUserDelegateDTO = new WBSEntryUserDelegateDTO();
            UserPrivilege userPrivilege = bpmActivityNodePrivilege.getPrivilege();
            wBSEntryUserDelegateDTO.setPrivilege(userPrivilege);
            String displayName = userPrivilege.getDisplayName();
            if (displayName.endsWith("执行")) {
                displayName = displayName.substring(0, displayName.length() - 2);
            }
            wBSEntryUserDelegateDTO.setPrivilegeName(displayName);
            if (wBSEntryDelegateMap.get(userPrivilege.name()) != null) {
                WBSEntryDelegate wBSEntryDelegateSub = wBSEntryDelegateMap.get(userPrivilege.name());
                Long subTeamId = wBSEntryDelegateSub.getTeamId();
                wBSEntryUserDelegateDTO.setTeamId(subTeamId);
                wBSEntryUserDelegateDTO.setId(wBSEntryDelegateSub.getId());
                Long subUserId = wBSEntryDelegateSub.getUserId();
                if (subTeamId != null && subTeamId != 0L) {
                    JsonObjectResponseBody<Organization> organizationBody = organizationFeignAPI.details(subTeamId, null);
                    if (organizationBody.getSuccess()) {
                        Organization organization = organizationBody.getData();
                        if (organization != null) {
                            wBSEntryUserDelegateDTO.setTeamName(organization.getName());
                        }
                    }
                }

                Long[] userIds = new Long[]{subUserId};
                List<String> names = new ArrayList<>();
                for (Long userId : userIds) {
                    JsonObjectResponseBody<UserProfile> userProfileBody = userFeignAPI.get(userId);
                    if (userProfileBody.getSuccess()) {
                        UserProfile userProfile = userProfileBody.getData();
                        names.add(userProfile.getName());
                    }
                }
                wBSEntryUserDelegateDTO.setUserNames(names);

                wBSEntryUserDelegateDTO.setUserIds(Arrays.asList(userIds));
            }
            userPrivileges.add(wBSEntryUserDelegateDTO);
        }
        wBSEntryDelegateDTO.setTeamId(teamId);
        if (!LongUtils.isEmpty(teamId)) {
            JsonObjectResponseBody<Organization> organizationBody = organizationFeignAPI.details(teamId, null);
            if (organizationBody.getSuccess()) {
                Organization organization = organizationBody.getData();
                if (organization != null) {
                    wBSEntryDelegateDTO.setTeamName(organization.getName());
                }
            }
        }
        wBSEntryDelegateDTO.setUserPrivileges(userPrivileges);

        return wBSEntryDelegateDTO;
    }

    /**
     * 取得可执行的计划任务。
     *
     * @param userId         用户 ID
     * @param userPrivileges 用户权限映射表（组织 - 权限集合）
     * @param projectId      项目 ID
     * @param wbsEntryId     计划条目 ID
     * @param pageable       分页参数
     * @return 计划任务分页参数
     */
    @Override
    public Page<WBSEntryActivityInstance> getWBSTasks(
        final Long userId,
        final Map<Long, Set<String>> userPrivileges,
        final Long projectId,
        final Long wbsEntryId,
        final Pageable pageable
    ) {
        return wbsEntryActivityInstanceRepository
            .findByPrivileges(userId, projectId, wbsEntryId, userPrivileges, pageable);

    }

    /**
     * 查询四级计划。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId 计划 ID
     * @param queryDTO   查询条件
     * @param pageable   分页参数
     * @return 四级计划分页数据
     */
    @Override
    public Page<WBSEntryWithRelations> searchEntityProcesses(
        final Long orgId,
        final Long projectId,
        final Long wbsEntryId,
        final WBSEntryQueryDTO queryDTO,
        final Pageable pageable
    ) {
        WBSEntryWithRelations rootWbsEntry = null;
        WBSEntryBlob rootWbsEntryBlob = null;

        if (wbsEntryId == null) return null;

        rootWbsEntry = wbsEntryWithRelationsRepository
            .findById(wbsEntryId)
            .orElse(null);
        rootWbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntryId);
        if (rootWbsEntryBlob == null || rootWbsEntry == null) {
            throw new BusinessError("指定的上级不存在"); // TODO
        }

        return wbsEntryWithRelationsRepository.search(
            projectId, rootWbsEntry, queryDTO, rootWbsEntryBlob.getPath(), pageable
        );

    }

    /**
     * 取得尚未开始的四级计划。
     *
     * @param projectId   项目 ID
     * @param timestamp   时间戳
     * @param fromEntryId 查询开始条目 ID
     * @param pageable    分页参数
     * @return 四级计划列表
     */
    @Override
    public List<WBSEntry> getNotStartedEntityProcesses(
        final Long projectId,
        final Double timestamp,
        final Long fromEntryId,
        final Pageable pageable
    ) {
        return wbsEntryRepository.findNotStartedEntityProcesses(
            projectId,
            RUNNING_STATUS_TO_BE_STARTED,
            timestamp,
            fromEntryId == null ? 0L : fromEntryId,
            pageable
        );
    }

    /**
     * 取得尚未开始的计划条目的数量。
     *
     * @param projectId 项目 ID
     * @param timestamp Unix 时间戳（秒）
     * @return 尚未开始的计划条目的数量
     */
    @Override
    public int countNotStartedEntityProcesses(
        final Long projectId,
        final Double timestamp
    ) {
        return wbsEntryRepository.countNotStartedEntityProcesses(
            projectId,
            timestamp
        );
    }

    /**
     * 根据实体和工序获取四级计划。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entityId  实体ID
     * @param process   工序英文代码
     * @return 四级计划列表
     */
    @Override
    public List<WBSEntry> getByEntityAndProcess(
        Long orgId,
        Long projectId,
        Long entityId,
        String process
    ) {

        return wbsEntryRepository.findByOrgIdAndProjectIdAndEntityIdAndProcessAndDeletedIsFalse(
            orgId,
            projectId,
            entityId,
            process
        );
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
    public WBSEntityBase getEntity(
        final Long projectId,
        final String entityType,
        final Long entityId,
        final String discipline
    ) {
        return planBusiness.getEntity(projectId, entityType, entityId, discipline);
    }

}
