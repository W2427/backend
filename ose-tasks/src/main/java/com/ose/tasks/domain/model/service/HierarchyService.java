package com.ose.tasks.domain.model.service;

import com.ose.dto.BaseDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.HierarchyRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.setting.HierarchyTypeRepository;
import com.ose.tasks.dto.*;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.setting.HierarchyType;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;

/**
 * 项目层级结构管理服务。
 */
@Component
public class HierarchyService extends StringRedisService implements HierarchyInterface {


    @Value("${application.files.temporary}")
    private String temporaryDir;


    private final ProjectRepository projectRepository;

    private final HierarchyTypeRepository hierarchyTypeRepository;
    private final HierarchyRepository hierarchyRepository;


    private final ProjectNodeRepository projectNodeRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;
    private final HierarchyNodeRelationInterface hierarchyNodeRelationService;

    private static final String PATH_SEPARATOR = "/";


    private static final int HEADER_ROW_COUNT = 3;
    private static final String HYPHEN = "-";

    private static Set<String> hierarchyTypeSet = new HashSet<>();

    private static final Set<String> areaHierarchyTypeSet = new HashSet<String>(){{
//        add("PIPING");
//        add("STRUCTURE");
//        add("PRESSURE_TEST_PACKAGE");
//        add("CLEAN_PACKAGE");
//        add("ELECTRICAL");
//        add("CABLE_PULLING_PACKAGE");
        add("GENERAL");
    }};

    private static final Set<String> systemHierarchyTypeSet = new HashSet<String>(){{
        add("SUB_SYSTEM");
    }};

    private static final Set<String> engineeringHierarchyTypeSet = new HashSet<String>(){{
        add("ENGINEERING_COMMON");
//        add("ENGINEERING_AREA.name());
//        add("ENGINEERING_SUB_SYSTEM.name());
    }};

    /**
     * 构造方法。
     *
     * @param projectRepository               项目数据仓库实例
     * @param hierarchyTypeRepository
     * @param hierarchyRepository             项目层级结构数据仓库实例
     * @param projectNodeRepository           项目节点数据仓库实例
     * @param hierarchyNodeRelationRepository
     * @param hierarchyNodeRelationService
     */
    @Autowired
    public HierarchyService(
        StringRedisTemplate stringRedisTemplate,
        ProjectRepository projectRepository,
        HierarchyTypeRepository hierarchyTypeRepository, HierarchyRepository hierarchyRepository,
        ProjectNodeRepository projectNodeRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        HierarchyNodeRelationInterface hierarchyNodeRelationService) {
        super(stringRedisTemplate);
        this.projectRepository = projectRepository;
        this.hierarchyTypeRepository = hierarchyTypeRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.hierarchyNodeRelationService = hierarchyNodeRelationService;
    }

    /**
     * 设置节点信息。
     *
     * @param operatorDTO 操作者信息
     * @param project     项目信息
     * @param entryId     节点 ID
     * @param entryDTO    设置数据
     * @return 编辑后的节点信息
     */
    private HierarchyNode edit(
        final OperatorDTO operatorDTO,
        final Project project,
        final Long entryId,
        final HierarchyNodeModifyDTO entryDTO
    ) {

        final Long projectId = project.getId();
        Long parentEntryId = entryDTO.getParentId();
        final HierarchyNode parentEntry;
        List<HierarchyNode> parentEntries = new ArrayList<>();
        final Long previousEntryId = entryDTO.getPreviousEntryId();
        final HierarchyNode previousEntry;
        final Date timestamp = new Date();
        HierarchyNode entry;

        // 新建时，添加节点时，走这个分支
        if (entryId == null) {
            HierarchyNode parentEntryTemp;

            // 添加节点时，如果是实体节点，先判断节点号是否存在
            // 如果不存在，添加操作终止
            // 如果存在，跟移动实体的操作
            if (!projectNodeRepository.existsByProjectIdAndNoAndEntityTypeAndDeletedIsFalse(
                projectId,
                entryDTO.getNo(),
                entryDTO.getEntityType())) {
                throw new BusinessError(
                    "",
                    "error.project-entry.entry-no-does-NOT-exist"); // TODO 使用业务的错误
            }

            // ISO和SPOOL以外的实体父级不能是区域，模块等非实体节点
            parentEntryTemp = hierarchyRepository
                .findByIdAndProjectIdAndDeletedIsFalse(parentEntryId, projectId)
                .orElse(null);


            entry = BeanUtils.copyProperties(entryDTO, new HierarchyNode());


            entry.setNode(
                projectNodeRepository
                    .findByProjectIdAndNoAndDeletedIsFalse(
                        projectId,
                        entryDTO.getNo()
                    )
                    .orElse(null)
            );
            entry.setCreatedAt(timestamp);
            entry.setCreatedBy(operatorDTO.getId());

            // 更新时
        } else {

            entry = hierarchyRepository
                .findByIdAndProjectIdAndDeletedIsFalse(entryId, projectId)
                .orElse(null);

            if (entry == null) {
                throw new NotFoundError();
            }

            if (parentEntryId == null) {
                parentEntryId = entry.getParentId();
            }

            List<String> ignoreProperties = new ArrayList<>();

            if (entryDTO.getParentId() == null) {
                ignoreProperties.add("parentId");
            } else if (LongUtils.isEmpty(entryDTO.getParentId())) {
                entryDTO.setParentId(null);
            }

            if (entryDTO.getNo() == null) {
                ignoreProperties.add("no");
            } else if (StringUtils.isBlank(entryDTO.getNo())) {
                entryDTO.setNo(null);
            }

            if (entryDTO.getRemarks() == null) {
                ignoreProperties.add("remarks");
            } else if (StringUtils.isBlank(entryDTO.getRemarks())) {
                entryDTO.setRemarks("");
            }

            entry = BeanUtils.copyProperties(entryDTO, entry, ignoreProperties);
        }

        // 取得上级节点
        if (!LongUtils.isEmpty(parentEntryId)) {

            parentEntry = hierarchyRepository
                .findByIdAndProjectIdAndDeletedIsFalse(parentEntryId, projectId)
                .orElse(null);

            if (parentEntry == null) {
                throw new BusinessError("error.project-entry.parent-not-found");
            }

            parentEntries = hierarchyRepository
                .findByIdInAndDeletedIsFalseOrderBySortAsc(
                    parentEntry.getParentIDs()
                );

            parentEntries.add(parentEntry);

        } else {
            parentEntry = null;
        }

        // 默认排在末尾
        if (entryId == null && LongUtils.isEmpty(previousEntryId)) {

            previousEntry = hierarchyRepository
                .findFirstByProjectIdAndParentIdAndDeletedIsFalseOrderBySortDesc(
                    projectId,
                    parentEntry == null ? null : parentEntry.getId()
                )
                .orElse(null);

            // 指定位置时且非首位时
        } else if (
            !LongUtils.isEmpty(previousEntryId)
                && !previousEntryId.equals(0L)
        ) {

            previousEntry = hierarchyRepository
                .findByIdAndProjectIdAndDeletedIsFalse(
                    previousEntryId,
                    projectId
                )
                .orElse(null);

            if (previousEntry == null) {
                throw new BusinessError("error.project-entry.previous-not-found");
            }

        } else {
            previousEntry = null;
        }

        int previousEntrySort = -1, sort = 0;

        if (entryId != null && previousEntry == null) {
            sort = entry.getSort();
        } else if (previousEntry != null) {
            previousEntrySort = previousEntry.getSort();
            sort = previousEntrySort + 1;
        } else if (parentEntry != null) {
            previousEntrySort = parentEntry.getSort();
            sort = previousEntrySort + 1;
        }

        entry.setCompanyId(project.getCompanyId());
        entry.setOrgId(project.getOrgId());
        entry.setProjectId(projectId);
        entry.setParentId(parentEntry != null ? parentEntry.getId() : null);
        entry.setPath(
            parentEntry != null
                ? (parentEntry.getPath() + parentEntry.getId() + PATH_SEPARATOR)
                : PATH_SEPARATOR
        );
        entry.setDepth(parentEntries.size());
        entry.setSort(sort);
        entry.setLastModifiedAt(timestamp);
        entry.setLastModifiedBy(operatorDTO.getId());
        entry.setStatus(ACTIVE);

        // 重新对插入位置以后的节点进行排序
        if (previousEntrySort >= 0) {
            hierarchyRepository.reorder(projectId, previousEntrySort, 1);
        }

        // 如果是模块，处理模块类型，批次编号，是否打包三个字段
        if ("WP01" == entry.getEntityType()) {
            // 模块类型
            entry.getNode().setModuleType(entryDTO.getModuleType());
            // 批次编号
            entry.getNode().setBatchNo(entryDTO.getBatchNo());
            // 是否打包
            entry.getNode().setPack(entryDTO.isPack());
        }
        // 保存项目节点
        projectNodeRepository.save(entry.getNode());
        hset(RedisKey.PROJECT_USER_KEY.getDisplayName(), entry.getNode().getNo(), entry.getNode().getId().toString());

        // 保存层级结构节点
        entry = hierarchyRepository.save(entry);

        // 更新项目信息
        project.setLastModifiedBy(operatorDTO.getId());
        project.setLastModifiedAt(timestamp);
        projectRepository.save(project);

        return entry;
    }

    /**
     * 添加节点。
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param entryDTO    节点信息
     * @return 节点信息
     */
    @Override
    @Transactional
    public HierarchyNode add(
        OperatorDTO operatorDTO,
        Project project,
        HierarchyNodeModifyDTO entryDTO
    ) {
        // 传入的父级ID的节点
        HierarchyNode parentNodeIn = hierarchyRepository.findByIdAndProjectIdAndDeletedIsFalse(
            entryDTO.getParentId(),
            project.getId()).orElse(null);
        if (parentNodeIn == null) {
            throw new BusinessError(
                "",
                "error.project-entry.parent-node-does-NOT-exist"); // TODO 使用业务的错误
        }

        // 挂载实体节点，父级也是实体类型的时候
        if (entryDTO.getEntityType() != null) {

            // 前一条目的ProjectNodeID
            Long previousEntryNodeId = null;
            if (!LongUtils.isEmpty(entryDTO.getPreviousEntryId())) {
                List<HierarchyNode> children = parentNodeIn.getChildren();
                for (HierarchyNode childNode : children) {
                    if (entryDTO.getPreviousEntryId().equals(childNode.getId())) {
                        previousEntryNodeId = childNode.getNode().getEntityId();
                        break;
                    }
                }
            }

            // 取得所有父级节点
            List<HierarchyNode> parentNodes = hierarchyRepository
                .findByNodeIdAndProjectIdAndDeletedIsFalse(parentNodeIn.getNode().getId(), project.getId());

            for (HierarchyNode parentNode : parentNodes) {
                HierarchyNodeModifyDTO entryDTOTemp = BeanUtils.copyProperties(entryDTO, new HierarchyNodeModifyDTO());
                // 父级ID设定
                entryDTOTemp.setParentId(parentNode.getId());
                // 前一条目ID设定
                Long previousEntryId = null;
                if (!LongUtils.isEmpty(entryDTO.getPreviousEntryId())
                    && !LongUtils.isEmpty(previousEntryNodeId)) {
                    List<HierarchyNode> children = parentNode.getChildren();
                    for (HierarchyNode childNode : children) {
                        if (previousEntryNodeId.equals(childNode.getNode().getEntityId())) {
                            previousEntryId = childNode.getId();
                            break;
                        }
                    }
                }
                entryDTOTemp.setPreviousEntryId(previousEntryId);
                edit(operatorDTO, project, null, entryDTOTemp);
            }
            return null;
        } else {
            // 实体以外的节点
            return edit(operatorDTO, project, null, entryDTO);
        }

    }

    /**
     * 更新节点。
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param entryId     节点 ID
     * @param entryDTO    节点信息
     * @return 节点信息
     */
    @Override
    @Transactional
    public HierarchyNode update(
        OperatorDTO operatorDTO,
        Project project,
        Long entryId,
        HierarchyNodeModifyDTO entryDTO
    ) {
        return edit(operatorDTO, project, entryId, entryDTO);
    }

    /**
     * 删除节点。
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param nodeId      节点 ID
     */
    @Override
    @Transactional
    public ProjectNode delete(
        OperatorDTO operatorDTO,
        Project project,
        Long nodeId
    ) {

        Long projectId = project.getId();
        Date timestamp = new Date();
        Long operatorId = operatorDTO.getId();

        HierarchyNode entry = hierarchyRepository
            .findByIdAndProjectIdAndDeletedIsFalse(nodeId, projectId)
            .orElse(null);

        if (entry == null) {
            throw new NotFoundError();
        }

        ProjectNode pn = entry.getNode();


        entry.setDeletedAt(timestamp);
        entry.setDeletedBy(operatorId);
        hierarchyRepository.save(entry);


        hierarchyRepository.deleteChildren(
            nodeId,
            operatorId,
            timestamp,
            timestamp.getTime()
        );

        hierarchyNodeRelationRepository.deleteDescendant(projectId, nodeId);


        project.setLastModifiedAt(timestamp);
        project.setLastModifiedBy(operatorId);
        projectRepository.save(project);
        return pn;
    }

    /**
     * 通过实体ID 删除节点。（暂时只有实体删除时候调用）
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param orgId       组织 ID
     * @param entityId    实体 ID
     */
    @Override
    @Transactional
    public void delete(
        OperatorDTO operatorDTO,
        Project project,
        Long orgId,
        Long entityId
    ) {

        Long projectId = project.getId();
        Date timestamp = new Date();
        Long operatorId = operatorDTO.getId();

        ProjectNode node = projectNodeRepository.findEntityByEntityId(projectId, orgId, entityId);


        if (node == null) {
            return;
        }


        projectNodeRepository.deleteByEntityIdAndDeletedIsFalse(node.getId(), operatorId, timestamp, timestamp.getTime());


        List<HierarchyNode> hierarchyNodes = hierarchyRepository
            .findByNodeIdAndProjectIdAndDeletedIsFalse(node.getId(), projectId);

        if (hierarchyNodes == null || hierarchyNodes.size() == 0) {
            return;
        }


        for (HierarchyNode hierarchyNode : hierarchyNodes) {
            hierarchyNode.setStatus(EntityStatus.DELETED);
            hierarchyNode.setDeletedAt(timestamp);
            hierarchyNode.setDeletedBy(operatorId);
            hierarchyRepository.save(hierarchyNode);


            hierarchyRepository.deleteChildren(
                hierarchyNode.getId(),
                operatorId,
                timestamp,
                timestamp.getTime()
            );
            hierarchyNodeRelationRepository.deleteDescendant(projectId, hierarchyNode.getId());

        }


        project.setLastModifiedAt(timestamp);
        project.setLastModifiedBy(operatorId);
        projectRepository.save(project);
    }


    /**
     * 通过实体ID 取消节点。
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param orgId       组织 ID
     * @param entityId    实体 ID
     */
    @Override
    @Transactional
    public void cancel(
        OperatorDTO operatorDTO,
        Project project,
        Long orgId,
        Long entityId
    ) {

        Long projectId = project.getId();
        Date timestamp = new Date();
        Long operatorId = operatorDTO.getId();

        ProjectNode node = projectNodeRepository.findEntityByEntityId(projectId, orgId, entityId);


        if (node == null || LongUtils.isEmpty(node.getId())) {
            return;
        }


        projectNodeRepository.cancelByEntityIdAndDeletedIsFalse(node.getId(), operatorId, timestamp, timestamp.getTime());


        List<HierarchyNode> hierarchyNodes = hierarchyRepository
            .findByNodeIdAndProjectIdAndDeletedIsFalse(node.getId(), projectId);

        if (hierarchyNodes == null || hierarchyNodes.size() == 0) {
            return;
        }


        for (HierarchyNode hierarchyNode : hierarchyNodes) {






            hierarchyRepository.deleteChildren(
                hierarchyNode.getId(),
                operatorId,
                timestamp,
                timestamp.getTime()
            );
            hierarchyNodeRelationRepository.deleteDescendantNotIncludeSelf(projectId, hierarchyNode.getId());

        }


        project.setLastModifiedAt(timestamp);
        project.setLastModifiedBy(operatorId);
        projectRepository.save(project);
    }


    /**
     * 通过实体ID 删除指定维度所有层级信息（只删除层级信息，project_nodes和entity_*表数据不动）
     *
     * @param operatorDTO   操作者信息
     * @param project       所属项目信息
     * @param orgId         组织 ID
     * @param entityId      实体 ID
     * @param hierarchyType 层级类别
     * @return 是否可以删除
     */
    @Override
    @Transactional
    public void deleteOnlyHierarchyInfoByHierarchyType(
        OperatorDTO operatorDTO,
        Project project,
        Long orgId,
        Long entityId,
        String hierarchyType
    ) {

        Long projectId = project.getId();
        Date timestamp = new Date();
        Long operatorId = operatorDTO.getId();

        ProjectNode node = projectNodeRepository.findEntityByEntityId(projectId, orgId, entityId);


        if (LongUtils.isEmpty(node.getId())) {
            return;
        }


        List<HierarchyNode> hierarchyNodes = hierarchyRepository
            .findByNodeIdAndHierarchyTypeAndProjectIdAndDeletedIsFalse(node.getId(), hierarchyType, projectId);

        if (hierarchyNodes == null || hierarchyNodes.size() == 0) {
            return;
        }


        for (HierarchyNode hierarchyNode : hierarchyNodes) {
            hierarchyNode.setStatus(DELETED);
            hierarchyNode.setDeletedAt(timestamp);
            hierarchyNode.setDeletedBy(operatorId);
            hierarchyRepository.save(hierarchyNode);


            hierarchyRepository.deleteChildren(
                hierarchyNode.getId(),
                operatorId,
                timestamp,
                timestamp.getTime()
            );
            hierarchyNodeRelationRepository.deleteDescendant(projectId, hierarchyNode.getId());
        }


        project.setLastModifiedAt(timestamp);
        project.setLastModifiedBy(operatorId);
        projectRepository.save(project);
    }

    /**
     * 通过实体ID 删除所有维度所有层级信息（只删除层级信息，project_nodes和entity_*表数据不动）
     * （SPOOL，PIPE_PIECE, WELD_JOINT, COMPONENT实体删除所有层级信息时用）
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param orgId       组织 ID
     * @param entityId    实体 ID
     */
    @Override
    @Transactional
    public void deleteAllHierarchyInfoByEntityId(
        OperatorDTO operatorDTO,
        Project project,
        Long orgId,
        Long entityId
    ) {

        Long projectId = project.getId();
        Date timestamp = new Date();
        Long operatorId = operatorDTO.getId();

        ProjectNode node = projectNodeRepository.findEntityByEntityId(projectId, orgId, entityId);


        if (LongUtils.isEmpty(node.getId())) {
            return;
        }


        List<HierarchyNode> hierarchyNodes = hierarchyRepository
            .findByNodeIdAndProjectIdAndDeletedIsFalse(node.getId(), projectId);

        if (hierarchyNodes == null || hierarchyNodes.size() == 0) {
            return;
        }


        for (HierarchyNode hierarchyNode : hierarchyNodes) {
            hierarchyNode.setStatus(DELETED);
            hierarchyNode.setDeletedAt(timestamp);
            hierarchyNode.setDeletedBy(operatorId);
            hierarchyRepository.save(hierarchyNode);


            hierarchyRepository.deleteChildren(
                hierarchyNode.getId(),
                operatorId,
                timestamp,
                timestamp.getTime()
            );

            hierarchyNodeRelationRepository.deleteDescendant(projectId, hierarchyNode.getId());

        }


        project.setLastModifiedAt(timestamp);
        project.setLastModifiedBy(operatorId);
        projectRepository.save(project);
    }

    /**
     * 取得项目层级结构节点列表。
     *
     * @param orgId      组织 ID
     * @param projectId  所属项目 ID
     * @param rootNodeId 根节点 ID
     * @param depth      查询深度
     * @return 层级结构节点列表
     */
    @Override
    public List<HierarchyNode> getFlatList(
        Long orgId,
        Long projectId,
        Long rootNodeId,
        int depth
    ) {


        if (!projectRepository
            .existsByIdAndOrgIdAndDeletedIsFalse(projectId, orgId)) {
            throw new NotFoundError();
        }

        depth = depth <= 0 ? Short.MAX_VALUE : depth;

        HierarchyNode rootEntry;
        int depthFrom = 0;
        int depthUntil = depth;


        if (rootNodeId != null && !projectId.equals(rootNodeId)) {

            rootEntry = hierarchyRepository
                .findByIdAndProjectIdAndDeletedIsFalse(rootNodeId, projectId)
                .orElse(null);

            if (rootEntry == null) {
                throw new NotFoundError();
            }

            depthFrom = rootEntry.getDepth() + 1;
            depthUntil = rootEntry.getDepth() + depth;
        }


        return hierarchyRepository
            .findByProjectIdAndPathLikeAndDepthGreaterThanEqualAndDepthLessThanEqualAndDeletedIsFalseOrderBySortAsc(
                projectId,
                rootNodeId == null ? "%" : ("%/" + rootNodeId.toString() + "/%"),
                depthFrom,
                depthUntil
            );

    }

    /**
     * 取得节点。LAYER_PACKAGE
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entryId   节点 ID
     * @return 节点信息
     */
    @Override
    public HierarchyNode get(Long orgId, Long projectId, Long entryId) {

        HierarchyNode entry;

        if (
            (entry = hierarchyRepository
                .findByIdAndOrgIdAndProjectId(entryId, orgId, projectId)
                .orElse(null)
            ) == null
            ) {
            throw new NotFoundError();
        }

        return entry;
    }

    /**
     * 将节点列表转为节点 ID 与节点数据实体的映射表。
     *
     * @param nodes 节点列表
     * @return 节点映射表
     */
    private Map<Long, HierarchyNode> convertToNodeMap(
        List<HierarchyNode> nodes
    ) {

        Map<Long, HierarchyNode> persistentNodeMap = new HashMap<>();

        for (HierarchyNode node : nodes) {
            persistentNodeMap.put(node.getId(), node);
        }

        return persistentNodeMap;
    }

    /**
     * 将数据实体层级列表转换为数据传输对象层级列表。
     *
     * @param nodes 数据实体层级列表
     * @return 数据传输对象层级列表
     */
    @Override
    public List<HierarchyNodePutDTO> convertToHierarchicalDTOList(
        List<HierarchyNode> nodes
    ) {

        if (nodes == null || nodes.size() == 0) {
            return null;
        }

        List<HierarchyNodePutDTO> dtos = new ArrayList<>();
        HierarchyNodeDTO dto;

        for (HierarchyNode node : nodes) {
            dto = BeanUtils.copyProperties(node, new HierarchyNodeDTO());
            dto.setChildren(convertToHierarchicalDTOList(node.getChildren()));
            dtos.add(dto);
        }

        return dtos;
    }

    /**
     * 层级结构列表转换为扁平化列表。
     *
     * @param projectId    项目 ID
     * @param nodeMap      节点 ID 与数据实体映射表
     * @param childNoMap   节点 ID 与子节点编号集合映射表
     * @param parent       上级节点信息
     * @param hierarchical 层级结构列表
     * @param nodes        节点列表
     * @param merge        是否合并旧数据
     * @return 错误数
     */
    private int flatten(
        final Long projectId,
        Map<Long, HierarchyNode> nodeMap,
        Map<Long, Map<String, HierarchyNode>> childNoMap,
        HierarchyNode parent,
        List<HierarchyNodePutDTO> hierarchical,
        List<HierarchyNode> nodes,
        String hierarchyType,
        boolean merge
    ) {

        Long parentId = parent == null ? null : parent.getId();
        Long parentKey = parentId == null ? 0L : parentId;
        String path = parent == null ? PATH_SEPARATOR : (parent.getPath() + parent.getId() + PATH_SEPARATOR);
        int depth = parent == null ? 0 : (parent.getDepth() + 1);
        Map<String, HierarchyNode> childNoNodeMap;

        if (childNoMap == null) {
            childNoMap = new HashMap<>();
        }

        if ((childNoNodeMap = childNoMap.get(parentKey)) == null) {
            childNoNodeMap = new HashMap<>();
            childNoMap.put(parentKey, childNoNodeMap);
        }


        if (merge) {

            List<HierarchyNode> children = hierarchyRepository
                .findByParentIdAndProjectIdAndDeletedIsFalseOrderBySortAsc(parentId, projectId);

            if (children.size() > 0) {

                for (HierarchyNode child : children) {
                    child.setPath(path);
                    child.setDepth(depth);
                    nodeMap.put(child.getId(), child);
                    childNoNodeMap.put(child.getNo(), child);
                }

                nodes.addAll(children);
            }

        }

        if (hierarchical == null || hierarchical.size() == 0) {
            return 0;
        }

        HierarchyNodeWithErrorDTO dto;
        HierarchyNode node;
        Long nodeId;
        String nodeNo;
        String fullNodeNo;
        int errorCount = 0;


        for (int index = 0; index < hierarchical.size(); index++) {

            dto = new HierarchyNodeWithErrorDTO(hierarchical.get(index));
            dto.setErrors(null);

            hierarchical.set(index, dto);

            nodeId = dto.getId();
            fullNodeNo = nodeNo = StringUtils.trim(dto.getNo());


            if (StringUtils.isEmpty(nodeNo) || dto.getEntityType() == null) {

                errorCount++;

                if (StringUtils.isEmpty(nodeNo)) {
                    dto.addError("name is required");
                }

                if (dto.getEntityType() == null) {
                    dto.addError("entity type is required");
                }

                continue;
            }

            if (
                (node = nodeMap.get(nodeId)) == null
                    && (node = childNoNodeMap.get(fullNodeNo)) == null
                ) {
                node = new HierarchyNode();
                node.setNew(true);
                node.setNode(projectNodeRepository
                    .findByProjectIdAndNoAndDeletedIsFalse(
                        projectId,
                        fullNodeNo
                    )
                    .orElse(null));
                childNoNodeMap.put(fullNodeNo, node);

            } else if (parentId != null && !parentId.equals(node.getParentId())
                && fullNodeNo.equals(node.getNo())) {
                dto.addErrorMessage(fullNodeNo + " DUPLICATED");
                continue;
            }

            if (hierarchyType == null) {
                node.setHierarchyType("SUB_SYSTEM");
            } else {
                node.setHierarchyType(hierarchyType);
            }
            node.setNo(nodeNo, parent == null ? null : parent.getNode().getNo());
            node.setParentId(parentId);
            node.setPath(path);
            node.setDepth(depth);
            node.setModuleType(dto.getModuleType());
            node.setEntityType(dto.getEntityType());
            node.setBatchNo(dto.getBatchNo());
            node.setPack(dto.isPack());
//            node.setDescription(dto.getDescription());
            node.setDto(dto);

            nodeMap.remove(node.getId());
            nodes.remove(node);
            nodes.add(node);


            errorCount += flatten(
                projectId,
                nodeMap,
                childNoMap,
                node,
                dto.getChildren(),
                nodes,
                hierarchyType,
                merge
            );

        }

        return errorCount;
    }

    /**
     * 取得项目层级结构。
     *
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param rootNodeId   根节点 ID
     * @param depth        取得深度
     * @param hierarchyType     维度类型
     * @param needEntity   是否要显示实体
     * @param hierarchical 是否要按层级返回
     * @return 层级结构列表
     */
    @Override
    public List<HierarchyNodeDTO> getHierarchy(
        Long orgId,
        Long projectId,
        Long rootNodeId,
        int depth,
        String hierarchyType,
        String drawingType,
        Boolean needEntity,
        Boolean hierarchical
    ) {

        depth = depth < 0 ? Short.MAX_VALUE : depth;

        Set<String> hierarchyTypes = new HashSet<>();
        List<HierarchyNode> nodes;


        if (hierarchyType == null) {
            hierarchyTypes.add("PIPING");
            hierarchyTypes.add("PRESSURE_TEST_PACKAGE");
            hierarchyTypes.add("SUB_SYSTEM");
            hierarchyTypes.add("CLEAN_PACKAGE");
            hierarchyTypes.add("STRUCTURE");
            hierarchyTypes.add("ELECTRICAL");
            hierarchyTypes.add("CABLE_PULLING_PACKAGE");
        } else {
            hierarchyTypes.add(hierarchyType);
        }


        if (!LongUtils.isEmpty(rootNodeId) && !projectId.equals(rootNodeId)) {
            HierarchyNode parent = hierarchyRepository.findById(rootNodeId).orElse(null);
            if (parent != null) {
                depth = parent.getDepth() + depth;
            }

            if (drawingType != null) {
                nodes = hierarchyRepository.findChildrenByRootIdAndHierarchyTypeAndDrawingTypeAndMaxDepth(projectId,
                    "%/" + rootNodeId.toString() + "/%",
                    hierarchyTypes,
                    drawingType,
                    depth);
            } else {
                nodes = hierarchyRepository.findChildrenByRootIdAndHierarchyTypeAndMaxDepth(projectId,
                    "%/" + rootNodeId.toString() + "/%",
                    hierarchyTypes,
                    depth);
            }
        } else {
            nodes = hierarchyRepository.findChildrenByHierarchyTypesAndMaxDepth(projectId,
                hierarchyTypes,
                depth);
        }

        List<HierarchyNodeDTO> nodeDTOs = BeanUtils.convertType(
            nodes,
            HierarchyNodeDTO.class
        );

        nodeDTOs.forEach(ndto ->{
            ndto.setDescription(ndto.getNode() != null ? ndto.getNode().getDescription() : null);
            ndto.setDiscipline(ndto.getNode() != null ? ndto.getNode().getDiscipline() : null);
            ndto.setFixedLevel(ndto.getNode() != null ? ndto.getNode().getFixedLevel() : null);
        });

        if (!hierarchical) {
            return nodeDTOs;
        }

        Map<Long, HierarchyNodeDTO> nodeDtoMap = new HashMap<>();
        HierarchyNodeDTO parentNodeDTO;
        List<HierarchyNodeDTO> result = new ArrayList<>();


        for (HierarchyNodeDTO nodeDTO : nodeDTOs) {
            nodeDtoMap.put(nodeDTO.getId(), nodeDTO);
        }

        for (HierarchyNodeDTO nodeDTO : nodeDTOs) {

            parentNodeDTO = nodeDtoMap.get(nodeDTO.getParentId());

            if (parentNodeDTO == null) {
                result.add(nodeDTO);
                continue;
            }

            parentNodeDTO.addChild(nodeDTO);
        }

        return result;
    }

    /**
     * 取得任务包选择需要的 项目层级结构。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param depth         取得深度
     * @param tpcEntityType 工作包类型中指定的实体类型
     * @return 层级结构列表
     */
    @Override
    public List<HierarchyNodeDTO> getTaskPackageHierarchy(Long orgId,
                                                          Long projectId,
                                                          int depth,
                                                          String tpcEntityType) {
        depth = depth <= 0 ? Short.MAX_VALUE : depth;

        Set<String> hierarchyTypes = new HashSet<>();
        Set<String> exceptEntityTypes = new HashSet<>();
        List<HierarchyNode> nodes;


        String hierarchyType;
        try {
            hierarchyType = tpcEntityType;
        } catch (Exception e) {
            hierarchyType = "STRUCTURE";
        }
        hierarchyTypes.add(hierarchyType);

        HierarchyType hierarchyTypeEntity = hierarchyTypeRepository.findByProjectIdAndNameEn(projectId, hierarchyType);
        List<HierarchyType> children = hierarchyTypeRepository.findByProjectIdAndParentId(projectId, hierarchyTypeEntity.getParentId());
        exceptEntityTypes.addAll(children.stream().map(HierarchyType::getNameEn).collect(Collectors.toList()));



        HierarchyNode parent = hierarchyRepository.findById(projectId).orElse(null);
        if (parent != null) {
            depth = parent.getDepth() + depth;
        }

        exceptEntityTypes.add(tpcEntityType);


        nodes = hierarchyRepository.findChildrenByEntityTypesAndMaxDepth(projectId,
            hierarchyTypes,
            exceptEntityTypes,
            depth);


        List<HierarchyNodeDTO> nodeDTOs = BeanUtils.convertType(
            nodes,
            HierarchyNodeDTO.class
        );


        Map<Long, HierarchyNodeDTO> nodeDtoMap = new HashMap<>();
        HierarchyNodeDTO parentNodeDTO;
        List<HierarchyNodeDTO> result = new ArrayList<>();


        for (HierarchyNodeDTO nodeDTO : nodeDTOs) {
            nodeDtoMap.put(nodeDTO.getId(), nodeDTO);
        }

        for (HierarchyNodeDTO nodeDTO : nodeDTOs) {

            parentNodeDTO = nodeDtoMap.get(nodeDTO.getParentId());

            if (parentNodeDTO == null) {
                result.add(nodeDTO);
                continue;
            }

            parentNodeDTO.addChild(nodeDTO);
        }

        return result;

    }

    @Override
    public boolean saveHierarchy(OperatorDTO operatorDTO,
                                 Project project,
                                 Long parentNodeId,
                                 List<HierarchyNodePutDTO> children,
                                 String hierarchyType,
                                 boolean merge) {
        LinkedHashMap<String, List<HierarchyNodePutDTO>> hierarchyDtoMap = new LinkedHashMap<>();

        hierarchyDtoMap.put(hierarchyType, children);
        List<String> errors = saveHierarchy(
            operatorDTO,
            project,
            parentNodeId,
            hierarchyDtoMap,
            true
        );

        if(CollectionUtils.isEmpty(errors)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存项目层级结构。
     *
     * @param operatorDTO  操作者信息
     * @param project      项目信息
     * @param rootNodeId   上级节点 ID
     * @param hierarchyDtoMap 节点列表Map
     * @return 错误数据
     */
    @Override
//    @Transactional
    public List<String> saveHierarchy(
        OperatorDTO operatorDTO,
        Project project,
        Long rootNodeId,
        LinkedHashMap<String,List<HierarchyNodePutDTO>> hierarchyDtoMap,
        boolean merge
    ) {

        Long operatorId = operatorDTO.getId();
        Long orgId = project.getOrgId();
        Long projectId = project.getId();
        Date timestamp = new Date();
        HierarchyNode parent = null;
        List<String> errors = new ArrayList<>();

        if (!LongUtils.isEmpty(rootNodeId)) {
            parent = get(orgId, projectId, rootNodeId);
        } else {
            rootNodeId = null;
        }


        int deletedCount = 0;
        int sort = parent == null ? 0 : parent.getSort();


        if (!merge) {

            Map<Long, HierarchyNode> nodeMap = convertToNodeMap(
                getFlatList(orgId, projectId, rootNodeId, 0)
            );

            List<Long> deleteIDs = new ArrayList<>(nodeMap.keySet());

            deletedCount = deleteIDs.size();

            if (deletedCount > 0) {
                hierarchyRepository.deleteByIdInAndDeletedIsFalse(
                    deleteIDs,
                    operatorId,
                    timestamp,
                    timestamp.getTime()
                );
            }


            hierarchyRepository.reorder(
                projectId,
                sort,
                Math.max(0, hierarchyDtoMap.get(0).size() * hierarchyDtoMap.size() - deletedCount)
            );
            sort = Math.max(0, hierarchyDtoMap.get(0).size() * hierarchyDtoMap.size() - deletedCount);
        }






        Map<String, ProjectNode> projectNodeMap = new LinkedHashMap<>();
        Map<String, HierarchyNode> hierarchyNodeMap = new LinkedHashMap<>();
        for(Map.Entry<String, List<HierarchyNodePutDTO>> hierarchyNodesMap : hierarchyDtoMap.entrySet()) {
            String hierarchyType = hierarchyNodesMap.getKey();

            for(HierarchyNodePutDTO hierarchyNodePutDTO : hierarchyNodesMap.getValue()) {
                HierarchyNode parentHierarchyNode = null;
                if(StringUtils.isEmpty(hierarchyNodePutDTO.getParentNo()) &&
                    LongUtils.isEmpty(hierarchyNodePutDTO.getParentId()) &&
                    rootNodeId != null) {
                    errors.add(hierarchyNodePutDTO.getNo() + " there is no parent no or id");
                    continue;
                }

                if(!StringUtils.isEmpty(hierarchyNodePutDTO.getNo())
                    && hierarchyNodeMap.containsKey(hierarchyNodePutDTO.getParentNo() + hierarchyType)) {
                    parentHierarchyNode = hierarchyNodeMap.get(hierarchyNodePutDTO.getParentNo() + hierarchyType);
                } else if(!LongUtils.isEmpty(hierarchyNodePutDTO.getParentId())){
                    parentHierarchyNode = hierarchyRepository.findById(hierarchyNodePutDTO.getParentId()).orElse(null);
                } else if(!StringUtils.isEmpty(hierarchyNodePutDTO.getParentNo())) {
                    parentHierarchyNode = hierarchyRepository.findByProjectIdAndNodeNoAndHierarchyTypeAndDeletedIsFalse(
                        projectId, hierarchyNodePutDTO.getParentNo(), hierarchyType
                    );
                } else if(LongUtils.isEmpty(rootNodeId)) {

                    parentHierarchyNode = new HierarchyNode();
                    parentHierarchyNode.setCompanyId(project.getCompanyId());
                    parentHierarchyNode.setOrgId(orgId);
                    parentHierarchyNode.setProjectId(projectId);
                    parentHierarchyNode.setId(null);
                    parentHierarchyNode.setDepth(-1);
                    parentHierarchyNode.setHierarchyType(hierarchyType);
                }

                if(parentHierarchyNode == null){
                    errors.add(hierarchyNodePutDTO.getNo() + " there is no parent node for id:" + hierarchyNodePutDTO.getParentId());
                    continue;
                }


                ProjectNode projectNode = projectNodeMap.get(hierarchyNodePutDTO.getNo());

                if(projectNode == null) {
                    projectNode = projectNodeRepository.
                        findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(orgId, projectId, hierarchyNodePutDTO.getNo());
                }
                if(projectNode == null) {
                    projectNode = new ProjectNode();
                    projectNode.setNo(hierarchyNodePutDTO.getNo(),hierarchyNodePutDTO.getParentNo());
                    projectNode.setCompanyId(project.getCompanyId());
                    projectNode.setOrgId(orgId);
                    projectNode.setProjectId(projectId);
                    projectNode.setCreatedBy(operatorId);
                    projectNode.setCreatedAt();
                    projectNode.setStatus(EntityStatus.ACTIVE);
                    projectNode.setDeleted(false);
                    projectNode.setVersion(new Date().getTime());
                    projectNode.setDescription(hierarchyNodePutDTO.getDescription());
                    projectNode.setEntityType(hierarchyNodePutDTO.getEntityType());
                    projectNode.setEntitySubType(hierarchyNodePutDTO.getEntitySubType());
                    projectNodeMap.put(projectNode.getNo(), projectNode);
                }

                projectNodeMap.put(hierarchyNodePutDTO.getNo(), projectNode);
                HierarchyNode hierarchyNode;
                hierarchyNode = hierarchyRepository.
                    findByProjectIdAndNodeIdAndHierarchyTypeAndDeletedIsFalse(projectId, projectNode.getId(), hierarchyType);

                if(hierarchyNode != null) {
                    hierarchyNode.setLastModifiedBy(operatorId);
                    hierarchyNode.setEntitySubType(hierarchyNodePutDTO.getEntitySubType());
                    hierarchyNode.setEntityType(hierarchyNodePutDTO.getEntityType());
                    projectNodeRepository.save(hierarchyNode.getNode());

                    continue;
                }


                if(parentHierarchyNode == null
                    && !projectNodeMap.keySet().contains(hierarchyNodePutDTO.getParentNo())) {
                    errors.add("There is no parent for node: " + hierarchyNodePutDTO.getNo() + " parent is : " +
                    hierarchyNodePutDTO.getParentNo() + " @ Dimension " + hierarchyType);
                    continue;
                }

                hierarchyNode = new HierarchyNode(parentHierarchyNode, projectNode);
                hierarchyNode.setStatus(EntityStatus.ACTIVE);
                hierarchyNode.setDeleted(false);
                hierarchyNode.setCreatedBy(operatorId);
                hierarchyNode.setCreatedAt(new Date());
                hierarchyNode.setLastModifiedBy(operatorId);
                hierarchyNode.setLastModifiedAt();
                hierarchyNode.setSort(sort++);
//                hierarchyNode.setDescription(hierarchyNodePutDTO.getDescription());
                hierarchyNodeMap.put(hierarchyNodePutDTO.getNo() + hierarchyType, hierarchyNode);
            }

        }


        Collection<ProjectNode> projectNodes = projectNodeMap.values();
        projectNodeRepository.saveAll(projectNodes);
        Collection<HierarchyNode> hierarchyNodes = hierarchyNodeMap.values();
        hierarchyRepository.saveAll(hierarchyNodes);
        hierarchyNodes.forEach(hn -> {
            hierarchyNodeRelationService.saveHierarchyPath(orgId, projectId, hn);
        });

        project.setLastModifiedBy(operatorId);
        project.setLastModifiedAt(timestamp);
        projectRepository.save(project);

        return errors;
    }


    /**
     * 导入层级结构（扁平结构）。
     *
     * @param batchTask  批处理任务信息
     * @param operator   操作者信息
     * @param project    项目信息
     * @param rootNodeId 根节点 ID
     * @param sheet      导入数据工作表
     * @param errorStyle 错误消息单元格样式
     * @return 批处理执行结果
     */
    private BatchResultDTO importHierarchyByFlattenStructure(
        final BatchTask batchTask,
        final OperatorDTO operator,
        final Project project,
        final Long rootNodeId,
        final Sheet sheet,
        final List<String> entityTypes,
        final CellStyle errorStyle,
        final String hierarchyViewType
    ) {

        BatchResultDTO batchResult = new BatchResultDTO(batchTask);

        if (entityTypes == null || entityTypes.size() == 0) {
            throw new BusinessError("no node type set");
        }

        Iterator<Row> rows = sheet.rowIterator();

        for (int rowIndex = 0; rowIndex < HEADER_ROW_COUNT; rowIndex++) {
            rows.next();
        }

        short levelColCount = (short) (entityTypes.size() * 2);
        short maxColNum = (short) (levelColCount + 3);
        Row row;
        short colNum;
        int depth;
        String nodeNo;
        String description;
        Cell errorMessageCell;
        List<HierarchyNodePutDTO> hierarchyNodePutDtos = new ArrayList<>();
        HierarchyNodePutDTO node;
        StringBuilder fullNo;
        String dtoKey;
        Set<String> hierarchyNoSet = new HashSet<>();
        LinkedHashMap<String, List<HierarchyNodePutDTO>> hierarchyDtoMap = new LinkedHashMap<>();


        while (rows.hasNext()) {

            if ((row = rows.next()) == null) {
                continue;
            }


            if (StringUtils.isEmpty(WorkbookUtils.readAsString(row, 0))) {
                continue;
            }

            errorMessageCell = row.getCell(maxColNum) != null
                ? row.getCell(maxColNum)
                : row.createCell(maxColNum);


            String moduleType = WorkbookUtils.readAsString(row, levelColCount);
            if(ProjectInterface.IMPORT_FILE_TYPE_ENGINEERING.equalsIgnoreCase(hierarchyViewType)) {
                moduleType = "ENG";
            } else if (StringUtils.isEmpty(moduleType)) {
                errorMessageCell.setCellStyle(errorStyle);

                errorMessageCell.setCellValue("no module type.");
                continue;
            }

            colNum = 0;
            depth = 0;
            fullNo = new StringBuilder();

            String moduleNo = null;
            String currentParentNo = null;
            String parentStr = "";


            for (String entityType : entityTypes) {

                nodeNo = StringUtils.trim(WorkbookUtils.readAsString(row, colNum));
                description = StringUtils.trim(WorkbookUtils.readAsString(row, colNum + 1));

                colNum += 2;

                if (StringUtils.isEmpty(nodeNo)) {
                    continue;
                }

                if(ProjectInterface.IMPORT_FILE_TYPE_ENGINEERING.equalsIgnoreCase(hierarchyViewType)){
                    if(StringUtils.isEmpty(parentStr)) {
//                        dtoKey = fullNo.append(nodeNo).toString();
                        parentStr = nodeNo;
                    } else {
                        nodeNo = parentStr + "_" + nodeNo;
                        parentStr = nodeNo;
                    }
                }
                dtoKey = fullNo.append(nodeNo).toString();



                if (hierarchyNoSet.contains(dtoKey)) {
                    currentParentNo = nodeNo;
                    if ("WP01" == entityType) {
                        moduleNo = nodeNo;
                    }
                    depth++;
                    continue;
                }

                node = new HierarchyNodePutDTO();
                hierarchyNoSet.add(dtoKey);

                node.setEntityType(entityType);
                node.setEntitySubType(entityType);
                node.setDepth(depth);
                node.setNo(nodeNo);
                node.setParentNo(currentParentNo);
                node.setDescription(description);
                node.setErrorMessage(errorMessageCell);
                node.getErrorMessage().setCellValue(" ");

                hierarchyNodePutDtos.add(node);
                currentParentNo = nodeNo;

                depth++;



            }







        }





        hierarchyTypeSet.forEach(hierarchyType -> {
            List<HierarchyNodePutDTO> selectedHierarchyNodes = BeanUtils.convertType(
                hierarchyNodePutDtos,
                HierarchyNodePutDTO.class
            );


            hierarchyDtoMap.put(hierarchyType, selectedHierarchyNodes);
        });


        List<String> errors;

            errors = saveHierarchy(
                operator,
                project,
                rootNodeId,
                hierarchyDtoMap,
                true
            );


        if (!CollectionUtils.isEmpty(errors)) {
            batchResult.addErrorCount(errors.size());
            batchResult.setLog(errors);

        }

        return batchResult;
    }




    /**
     * 导入层级结构。
     *
     * @param batchTask     批处理任务信息
     * @param operator      操作者信息
     * @param project       项目信息
     * @param rootNodeId    根节点 ID
     * @param nodeImportDTO 节点导入操作数据传输对象
     * @return 批处理执行结果
     */
    @Override
//    @Transactional
    public BatchResultDTO importHierarchy(
        BatchTask batchTask,
        OperatorDTO operator,
        Project project,
        Long rootNodeId,
        HierarchyNodeImportDTO nodeImportDTO
    ) {

        File excel;
        Workbook workbook;
        BatchResultDTO batchResult;


        try {
            excel = new File(temporaryDir, nodeImportDTO.getFilename());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new NotFoundError();
        }


        Sheet settings = workbook.getSheet("Settings");
        Sheet sheet = workbook.getSheet("Hierarchy");

        if (
            settings == null
                || sheet == null
                || sheet.getPhysicalNumberOfRows() <= HEADER_ROW_COUNT
            ) {
            throw new BusinessError("import file not valid");
        }


        if ("flatten".equals(WorkbookUtils.readAsString(settings.getRow(4), 1))) {


            if (!project.getId().toString().equals(WorkbookUtils.readAsString(settings.getRow(1), 1))) {
                throw new BusinessError("project not matched");
            }

            String hierarchyViewType = WorkbookUtils.readAsString(settings.getRow(2), 1);
            String templateVersion = WorkbookUtils.readAsString(settings.getRow(3), 1);
            String latestVersion;
            List<String> entityTypes = new ArrayList<>();
            List<String> unselectedOptionalEntityTypes = new ArrayList<>();




            switch (hierarchyViewType) {
                case ProjectInterface.IMPORT_FILE_TYPE_AREA:
                    latestVersion = project.getAreaImportFileVersion();
                    entityTypes = new ArrayList<>(ProjectInterface.AREA_NODE_TYPES);
                    unselectedOptionalEntityTypes = new ArrayList<>(ProjectInterface.AREA_OPTIONAL_NODE_TYPES);
                    unselectedOptionalEntityTypes.removeAll(Project.toHierarchyNodeTypeList(project.getAreaOptionalNodeTypes()));
                    hierarchyTypeSet = areaHierarchyTypeSet;
                    break;
                case ProjectInterface.IMPORT_FILE_TYPE_SYSTEM:
                    latestVersion = project.getSystemImportFileVersion();
                    entityTypes = new ArrayList<>(ProjectInterface.SYSTEM_NODE_TYPES);
                    unselectedOptionalEntityTypes = new ArrayList<>(ProjectInterface.SYSTEM_OPTIONAL_NODE_TYPES);
                    unselectedOptionalEntityTypes.removeAll(Project.toHierarchyNodeTypeList(project.getSystemOptionalNodeTypes()));
                    hierarchyTypeSet = systemHierarchyTypeSet;
                    break;
                case ProjectInterface.IMPORT_FILE_TYPE_ENGINEERING:
                    latestVersion = project.getEngineeringImportFileVersion();
                    entityTypes = new ArrayList<>(ProjectInterface.ENGINEERING_NODE_TYPES);
                    unselectedOptionalEntityTypes = new ArrayList<>(ProjectInterface.ENGINEERING_OPTIONAL_NODE_TYPES);
                    unselectedOptionalEntityTypes.removeAll(Project.toHierarchyNodeTypeList(project.getEngineeringOptionalNodeTypes()));
                    hierarchyTypeSet = engineeringHierarchyTypeSet;
                    break;
                default:
                    latestVersion = null;
            }


            if (!templateVersion.equals(latestVersion)) {
                throw new BusinessError("import file out of date");
            }

            entityTypes.removeAll(unselectedOptionalEntityTypes);

            batchResult = importHierarchyByFlattenStructure(
                batchTask,
                operator,
                project,
                rootNodeId,
                sheet,
                entityTypes,
                settings.getRow(5).getCell(1).getCellStyle(),
                hierarchyViewType
            );

            List<String> errors = batchResult.getLog();
            if(!CollectionUtils.isEmpty(errors)) {
                Sheet errorSheet = workbook.getSheet("ERRORS");
                int i = 1;
                for (String error : errors) {
                    Row row = WorkbookUtils.getRow(errorSheet, i);

                    if (i >= 35) {
                        WorkbookUtils.copyRow(sheet.getRow(2), row);
                    }
                    WorkbookUtils.getCell(row, 0).setCellValue(i++);
                    WorkbookUtils.getCell(row, 1).setCellValue(error);
                }
            }


        } else {
            throw new BusinessError("Only can import flatten hierarchy data");
        }


        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException ioe) {
            ioe.printStackTrace(System.out);
        }

        return batchResult;
    }

    /**
     * 将节点移动到层级结构的指定位置。
     *
     * @param operator                操作者信息
     * @param project                 项目信息
     * @param entityType                移动对象节点类型
     * @param entityType              移动对象节点的实体类型
     * @param entityId                移动对象节点的实体 ID
     * @param targetEntityType 移动目标层级节点的类型
     * @param targetHierarchyNodeId   移动目标层级节点的 ID
     */
    @Override
    @Transactional
    public void moveTo(
        OperatorDTO operator,
        Project project,
        String entityType,
        Long entityId,
        String targetEntityType,
        Long targetHierarchyNodeId
    ) {
        moveTo(
            operator,
            project,
            projectNodeRepository
                .findByProjectIdAndEntityTypeAndEntityIdAndDeletedIsFalse(
                    project.getId(),
                    entityType,
                    entityId
                )
                .orElse(null),
            targetEntityType,
            targetHierarchyNodeId
        );
    }


    /**
     * 更新层级节点上的计划实施进度信息。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @Override
    public void refreshWBSProgress(OperatorDTO operator, Long orgId, Long projectId) {
        hierarchyRepository.startSettingWBSProgress(projectId);
    }

    /**
     * 通过实体ID，层级类型取得模块下的第一个节点信息
     * 例：
     * 维度     层级类型                        取得节点的NODE_TYPE      取得节点的ENTITY_TYPE
     * 区域     ISO                            ENTITY                  ISO
     * 试压包   PRESSURE_TEST_PACKAGE          PRESSURE_TEST_PACKAGE    --
     * 子系统   SUB_SYSTEM                     SUB_SYSTEM               --
     * 层      LAYER_PACKAGE                  LAYER_PACKAGE            --
     * 清洁包   CLEAN_PACKAGE                  CLEAN_PACKAGE            --
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param entityId      实体ID
     * @param hierarchyType 层级类型
     * @return HierarchyNodeDTO
     */
    @Override
    public HierarchyNodeDTO getHierarchyNodeByEntityIdAndHierarchyType(Long orgId,
                                                                       Long projectId,
                                                                       Long entityId,
                                                                       String hierarchyType) {

        HierarchyNode hierarchyNode = hierarchyRepository.findByEntityIdAndHierarchyTypeAndDeletedIsFalse(
            orgId,
            projectId,
            entityId,
            hierarchyType);

        if (hierarchyNode == null) {
            throw new BusinessError("", "No hierarchyNode info.");
        }

        // 如果传入的是ISO实体的ID，最上级父级就是自己
//        return BeanUtils.copyProperties(hierarchyNode, new HierarchyNodeDTO());

        // 根据path取得父级节点
        String path = hierarchyNode.getPath();
        List<Long> hierarchyIds = LongUtils.change2Str(path.split(PATH_SEPARATOR));

        List<HierarchyNode> parentNodes = hierarchyRepository.findByIdInAndDeletedIsFalseOrderBySortAsc(hierarchyIds);

        if (parentNodes == null || parentNodes.size() == 0) {
            throw new BusinessError("", "No parent info.");
        }

        String targetEntityType = null;

        for (HierarchyNode nodeTemp : parentNodes) {
            if (targetEntityType != null) {
                if (targetEntityType == nodeTemp.getNode().getEntityType()) {
                    return BeanUtils.copyProperties(nodeTemp, new HierarchyNodeDTO());
                }
            }
        }

        return null;
    }

    /**
     * 将节点移动到层级结构的指定位置。
     *
     * @param operator                操作者信息
     * @param project                 项目信息
     * @param projectNode             被移动项目节点
     * @param targetEntityType 移动目标层级节点的类型
     * @param targetHierarchyNodeId   移动目标层级节点的 ID
     */

    private void moveTo(
        final OperatorDTO operator,
        final Project project,
        final ProjectNode projectNode,
        final String targetEntityType,
        final Long targetHierarchyNodeId
    ) {


        // 若移动对象项目节点不存在则返回错误
        if (projectNode == null) {
            throw new NotFoundError(); // TODO
        }

        String targetHierarchyType;
        String[] targetEntityTypes = new String[]{targetEntityType};
        HierarchyNode hierarchyParentNode = hierarchyRepository.findById(targetHierarchyNodeId).orElse(null);
        if (hierarchyParentNode == null) {
            throw new BusinessError("There is no such parent hierarchy Id");
        }

        targetHierarchyType = hierarchyParentNode.getHierarchyType();

        // 取得上级层级节点
        HierarchyNode parentHierarchyNode;

        if (targetEntityType == null) {
            parentHierarchyNode = hierarchyRepository
                .findByProjectIdAndIdAndHierarchyTypeAndDeletedIsFalse(
                    project.getId(),
                    targetHierarchyNodeId,
                    targetHierarchyType
                )
                .orElse(null);
        } else {
            // 父级是实体的情况
            parentHierarchyNode = hierarchyRepository
                .findByProjectIdAndIdAndEntityTypeAndHierarchyTypeAndDeletedIsFalse(
                    project.getId(),
                    targetHierarchyNodeId,
                    targetEntityType,
                    targetHierarchyType
                )
                .orElse(null);
        }

        // 如果是ISO在区域的移动，被移动的节点类型改为ISO
//        if ("AREA == targetHierarchyType) {
//            targetHierarchyType = "ISO;
//        }
        // 取得被移动节点的层级ID
        HierarchyNode hierarchyNodeToMove = hierarchyRepository.findByProjectIdAndNodeIdAndHierarchyTypeAndDeletedIsFalse(
            project.getId(),
            projectNode.getId(),
            targetHierarchyType
        );

        if (parentHierarchyNode != null) {
            // 更新移动的节点
            String pathPrefix = parentHierarchyNode.getPath() + parentHierarchyNode.getId() + PATH_SEPARATOR;
            int depthInc;
            String pathOld;
            String pathNew;

            if (hierarchyNodeToMove != null) {
                depthInc = parentHierarchyNode.getDepth() - hierarchyNodeToMove.getDepth() + 1;
                pathOld = hierarchyNodeToMove.getPath() + hierarchyNodeToMove.getId() + PATH_SEPARATOR;
                pathNew = parentHierarchyNode.getPath() + parentHierarchyNode.getId() + PATH_SEPARATOR
                    + hierarchyNodeToMove.getId() + PATH_SEPARATOR;
            } else {
                hierarchyNodeToMove = new HierarchyNode(parentHierarchyNode, projectNode);
                depthInc = 0; // 新建层级节点的深度不用找层差
                pathOld = hierarchyNodeToMove.getPath() + hierarchyNodeToMove.getId() + PATH_SEPARATOR;
                pathNew = parentHierarchyNode.getPath() + parentHierarchyNode.getId() + PATH_SEPARATOR
                    + hierarchyNodeToMove.getId() + PATH_SEPARATOR;
                hierarchyNodeToMove.setStatus(ACTIVE);
                hierarchyNodeToMove.setCreatedAt();
                hierarchyNodeToMove.setCreatedBy(operator.getId());
                hierarchyNodeToMove.setLastModifiedAt();
                hierarchyNodeToMove.setLastModifiedBy(operator.getId());
            }

            // hierarchyNodeToMove.setSort(sortFrom++); TODO
            hierarchyNodeToMove.setParentId(parentHierarchyNode.getId());
            hierarchyNodeToMove.setPath(pathPrefix);
            hierarchyNodeToMove.setDepth(hierarchyNodeToMove.getDepth() + depthInc);
            hierarchyRepository.save(hierarchyNodeToMove);
            // 更新移动节点的子节点
            hierarchyRepository.moveChild(project.getId(), depthInc, pathOld, pathNew, targetHierarchyType);

            // 【注意】
            // 如果是SPOOL，PIPE_PIECE，WELD_JOINT，COMPONENT在区域维度移动了，其他维度也要更新
            if (("SPOOL" == projectNode.getEntityType()
                || "PIPE_PIECE" == projectNode.getEntityType()
                || "WELD_JOINT" == projectNode.getEntityType()
                || "COMPONENT" == projectNode.getEntityType())
            ) {
                // 试压包维度
                // 被移动节点的试压包维度层级信息
                dealWithHierarchyInfoByHierarchyType(operator,
                    project,
                    projectNode,
                    parentHierarchyNode.getNode().getId(),
                    "TEST_PACKAGE");
                // 清洁包维度
                dealWithHierarchyInfoByHierarchyType(operator,
                    project,
                    projectNode,
                    parentHierarchyNode.getNode().getId(),
                    "CLEAN_PACKAGE");
                // 子系统维度
                dealWithHierarchyInfoByHierarchyType(operator,
                    project,
                    projectNode,
                    parentHierarchyNode.getNode().getId(),
                    "SUB_SYSTEM");
                // 层维度（父级为SPOOL时）
                if ("SPOOL" == parentHierarchyNode.getNode().getEntityType()) {
                    dealWithHierarchyInfoByHierarchyType(operator,
                        project,
                        projectNode,
                        parentHierarchyNode.getNode().getId(),
                        "PIPING");
                }
                // 移动目的地实体是ISO时，要把实体原来所在的层(LAYER_PACKAGE)维度的信息清掉
                if (parentHierarchyNode.getEntityType() == "ISO"
                    && "SPOOL" != projectNode.getEntityType()) {
                    deleteOnlyHierarchyInfoByHierarchyType(operator,
                        project,
                        project.getOrgId(),
                        projectNode.getEntityId(),
                        "PIPING");

                }
            }

        }
    }

    /**
     * 通过实体ID和父级项目ID（project_node）处理指定维度的数据
     * 父级节点在指定维度有数据，把指定实体移动过来
     * 如果父级节点在指定维度没有数据，把指定实体信息在指定维度上的数据删除
     * （暂时仅移动节点时用）
     *
     * @param operatorDTO   操作者信息
     * @param project       所属项目信息
     * @param projectNode   被移动的实体项目节点
     * @param parentNodeId  移动到的目的父级项目节点ID
     * @param hierarchyType 层级类别
     */
    void dealWithHierarchyInfoByHierarchyType(
        OperatorDTO operatorDTO,
        Project project,
        ProjectNode projectNode,
        Long parentNodeId,
        String hierarchyType
    ) {
        HierarchyNode hierarchyNode = hierarchyRepository
            .findByProjectIdAndNodeIdAndHierarchyTypeAndDeletedIsFalse(
                project.getId(),
                projectNode.getId(),
                hierarchyType);

        HierarchyNode parentHierarchyNode = hierarchyRepository
            .findByProjectIdAndNodeIdAndHierarchyTypeAndDeletedIsFalse(
                project.getId(),
                parentNodeId,
                hierarchyType);

        if (hierarchyNode == null) {
            if (null != parentHierarchyNode) {
                HierarchyNode lastSibling =
                    hierarchyRepository.findFirstByProjectIdAndParentIdAndDeletedIsFalseOrderBySortDesc(
                        project.getId(),
                        parentHierarchyNode.getId()).orElse(null);
                hierarchyNode = new HierarchyNode(parentHierarchyNode, projectNode);
                hierarchyNode.setStatus(ACTIVE);
                hierarchyNode.setDeleted(false);
                if (lastSibling == null) {
                    hierarchyNode.setSort(parentHierarchyNode.getSort() + 1);
                } else {
                    hierarchyNode.setSort(lastSibling.getSort() + 1);
                }
                hierarchyNode.setCreatedBy(operatorDTO.getId());
                hierarchyNode.setCreatedAt();
                hierarchyNode.setLastModifiedAt();
                hierarchyNode.setLastModifiedBy(operatorDTO.getId());
                hierarchyRepository.save(hierarchyNode);
            }
        } else {

            if (null == parentHierarchyNode) {
                deleteOnlyHierarchyInfoByHierarchyType(operatorDTO,
                    project,
                    project.getOrgId(),
                    projectNode.getEntityId(),
                    hierarchyType);
            } else {

                String pathPrefix = parentHierarchyNode.getPath() + parentHierarchyNode.getId() + PATH_SEPARATOR;
                int depthInc = parentHierarchyNode.getDepth() - hierarchyNode.getDepth() + 1;
                String pathOld = hierarchyNode.getPath() + hierarchyNode.getId() + PATH_SEPARATOR;
                String pathNew = parentHierarchyNode.getPath() + parentHierarchyNode.getId() + PATH_SEPARATOR
                    + hierarchyNode.getId() + PATH_SEPARATOR;



                hierarchyNode.setParentId(parentHierarchyNode.getId());
                hierarchyNode.setPath(pathPrefix);
                hierarchyNode.setDepth(hierarchyNode.getDepth() + depthInc);
                hierarchyRepository.save(hierarchyNode);

                hierarchyRepository.moveChild(project.getId(), depthInc, pathOld, pathNew, hierarchyType);
            }
        }
    }

    /**
     * 设置实体信息及项目节点的所属管线及单管实体的 ID。
     *
     * @param projectId 项目 ID
     */
    @Override
    public void setParentISOAndSpoolEntityIDs(Long projectId) {
        projectNodeRepository.setParentISOAndSpoolEntityIDs(projectId);
    }

    /**
     * 根据实体ID取得所有的层级节点
     *
     * @param orgId       组织ID
     * @param projectId   项目 ID
     * @param hierarchyId
     * @return
     */
    @Override
    public List<Tuple> getHierarchyNodeByEntityId(Long orgId, Long projectId, Long hierarchyId) {

        return hierarchyRepository.
            findByOrgIdAndProjectIdAndEntityIdAndDeletedIsFalse(orgId, projectId, hierarchyId);
    }

    /**
     * 设置删除一个WP0？之后的WP01/02/03/04/05
     *
     * @param projectId 项目ID
     * @param wpName    wp01 wp02 wp03 wp04 wp05
     */
    @Override
    public void setDeletedParentEntityIdOnStructureEntities(Long projectId, String wpName, Long entityId) {
        projectNodeRepository.setDeletedParentEntityIdOnStructureEntities(projectId, wpName, entityId);
    }

    /**
     * 设置关联项目层级结构节点数据。
     *
     * @param <T>      数据实体范型
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        List<T> entities
    ) {

        Set<Long> nodeIdSet = new HashSet<>();

        for (T entity : entities) {

            if (!(entity instanceof HierarchyNode)) {
                continue;
            }

            nodeIdSet.addAll(((HierarchyNode) entity).getParentIDs());
        }

        List<HierarchyNode> nodes = hierarchyRepository
            .findByIdInOrderBySortAsc(new ArrayList<>(nodeIdSet));

        for (HierarchyNode node : nodes) {
            included.put(node.getId(), node);
        }

        return included;
    }


    /**
     * 删除冗余的 hierarchy Node 节点
     * @param projectId
     */
    public void deleteRedundantNodes(Long projectId){
        List<BigInteger> redundantNodeIds = hierarchyRepository.findRedundantNodeIds(projectId);

        if(CollectionUtils.isEmpty(redundantNodeIds)) {
            return;
        }

        redundantNodeIds.forEach(redundantNodeId -> {
            if(redundantNodeId != null)
                hierarchyRepository.deleteRedundantNodeById(redundantNodeId.longValue());

        });
    }

}
