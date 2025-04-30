package com.ose.tasks.domain.model.repository;

import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.ProjectNode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 项目节点 CRUD 操作接口。
 */
public interface HierarchyRepository extends CrudRepository<HierarchyNode, Long>, HierarchyCustomRepository {

    /**
     * 根据 ID 及所属项目 ID 取得节点信息。
     *
     * @param id        节点 ID
     * @param projectId 所属项目 ID
     * @return 节点信息
     */
    Optional<HierarchyNode> findByIdAndProjectIdAndDeletedIsFalse(Long id, Long projectId);

    /**
     * 根据 实体 ID 及所属项目 ID 取得节点信息。
     *
     * @param nodeId    实体 ID
     * @param projectId 所属项目 ID
     * @return 节点信息
     */
    List<HierarchyNode> findByNodeIdAndProjectIdAndDeletedIsFalse(Long nodeId, Long projectId);

    /**
     * 根据 实体 ID，层级类别 及所属项目 ID 取得节点信息。
     *
     * @param nodeId    实体 ID
     * @param projectId 所属项目 ID
     * @return 节点信息
     */
    List<HierarchyNode> findByNodeIdAndHierarchyTypeAndProjectIdAndDeletedIsFalse(Long nodeId,
                                                                                  String hierarchyType,
                                                                                  Long projectId);

    /**
     * 取得指定类型的层级节点。
     *
     * @param projectId       项目 ID
     * @param hierarchyNodeId 层级节点信息
     * @return 层级节点信息
     */
    @Query("SELECT hn"
        + " FROM HierarchyNode hn"
        + " INNER JOIN ProjectNode pn ON pn.id = hn.node.id AND hn.hierarchyType = :hierarchyType "
        + " WHERE hn.projectId = :projectId AND hn.id = :hierarchyNodeId AND hn.deleted = false")
    Optional<HierarchyNode> findByProjectIdAndIdAndHierarchyTypeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("hierarchyNodeId") Long hierarchyNodeId,
        @Param("hierarchyType") String hierarchyType
    );

    /**
     * 取得指定类型的层级节点。
     *
     * @param projectId       项目 ID
     * @param hierarchyNodeId 层级节点信息
     * @param entityType      上级节点实体类型（可选）
     * @return 层级节点信息
     */
    @Query("SELECT hn"
        + " FROM HierarchyNode hn"
        + " INNER JOIN ProjectNode pn ON pn.id = hn.node.id AND hn.hierarchyType = :hierarchyType AND pn.entityType = :entityType"
        + " WHERE hn.projectId = :projectId AND hn.id = :hierarchyNodeId AND hn.deleted = false")
    Optional<HierarchyNode> findByProjectIdAndIdAndEntityTypeAndHierarchyTypeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("hierarchyNodeId") Long hierarchyNodeId,
        @Param("entityType") String entityType,
        @Param("hierarchyType") String hierarchyType
    );

    /**
     * 取得项目节点在指定类型上级节点下的层级节点信息。
     *
     * @param projectId 项目 ID
     * @param nodeId    项目节点信息
     * @return 层级节点信息
     */
    @Query("SELECT hn"
        + " FROM HierarchyNode hn"
        + " INNER JOIN HierarchyNode phn ON phn.id = hn.parentId AND phn.deleted = false"
        + " INNER JOIN ProjectNode pn ON pn.id = phn.node.id "
        + " WHERE hn.projectId = :projectId AND hn.node.id = :nodeId AND hn.deleted = false")
    List<HierarchyNode> findByProjectIdAndNodeIdAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("nodeId") Long nodeId);

    /**
     * 取得项目节点在指定类型上级节点下的层级节点信息。
     *
     * @param projectId         项目 ID
     * @param nodeId            项目节点信息
     * @param parentEntityTypes 上级节点实体类型（可选）
     * @return 层级节点信息
     */
    @Query("SELECT hn"
        + " FROM HierarchyNode hn"
        + " INNER JOIN HierarchyNode phn ON phn.id = hn.parentId AND phn.deleted = false"
        + " INNER JOIN ProjectNode pn ON pn.id = phn.node.id AND pn.entityType IN :parentEntityTypes"
        + " WHERE hn.projectId = :projectId AND hn.node.id = :nodeId AND hn.deleted = false")
    List<HierarchyNode> findByProjectIdAndNodeIdAndParentEntityTypeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("nodeId") Long nodeId,
        @Param("parentEntityTypes") String[] parentEntityTypes
    );

    /**
     * 取得同一上级节点下的最后一个节点。
     *
     * @param projectId 所属项目 ID
     * @param parentId  上级节点 ID
     * @return 最后一个节点信息
     */
    Optional<HierarchyNode> findFirstByProjectIdAndParentIdAndDeletedIsFalseOrderBySortDesc(Long projectId, Long parentId);

    /**
     * 取得节点。
     *
     * @param id        节点 ID
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 节点信息
     */
    Optional<HierarchyNode> findByIdAndOrgIdAndProjectId(Long id, Long orgId, Long projectId);

    /**
     * 根据节点编号及所属项目 ID 取得节点信息。
     *
     * @param no        节点编号
     * @param projectId 项目 ID
     * @return 节点信息
     */
    @Query(value = "SELECT hn FROM HierarchyNode hn INNER JOIN ProjectNode pn ON pn.id = hn.node.id WHERE pn.projectId = :projectId AND pn.no = :no AND  hn.projectId = :projectId AND hn.deleted = false")
    List<HierarchyNode> findByNoAndProjectIdAndDeletedIsFalse(
        @Param("no") String no,
        @Param("projectId") Long projectId
    );

    /**
     * 根据节点编号及所属项目 ID 取得节点信息。
     *
     * @param no             节点编号
     * @param projectId      项目 ID
     * @param hierarchyTypes 层级类型
     * @return 节点信息Set
     */
    @Query(value = "SELECT hn FROM HierarchyNode hn INNER JOIN ProjectNode pn ON pn.id = hn.node.id " +
        "WHERE pn.projectId = :projectId AND pn.no = :no AND hn.projectId = :projectId AND hn.hierarchyType IN :hierarchyTypes " +
        "AND hn.deleted = false")
    Set<HierarchyNode> findByNoAndProjectIdAndHierarchyTypeInAndDeletedIsFalse(
        @Param("no") String no,
        @Param("projectId") Long projectId,
        @Param("hierarchyTypes") String[] hierarchyTypes
    );

    /**
     * 根据节点编号及所属项目 ID 取得节点信息。
     *
     * @param no        节点编号
     * @param projectId 项目 ID
     * @param parentId  上级节点 ID
     * @return 节点信息
     */
    @Query(value = "SELECT hn FROM HierarchyNode hn INNER JOIN ProjectNode pn ON pn.id = hn.node.id WHERE pn.projectId = :projectId AND pn.no = :no AND hn.projectId = :projectId AND hn.parentId = :parentId AND hn.deleted = false")
    Optional<HierarchyNode> findByNoAndProjectIdAndParentIdAndDeletedIsFalse(
        @Param("no") String no,
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId
    );

    /**
     * 根据节点编号、所属项目 ID 和 层级节点类型 取得节点信息。
     *
     * @param no            节点编号
     * @param projectId     项目 ID
     * @param parentId      上级节点 ID
     * @param hierarchyType 层级节点类型
     * @return 节点信息
     */
    @Query(value = "SELECT hn FROM HierarchyNode hn INNER JOIN ProjectNode pn ON pn.id = hn.node.id WHERE pn.projectId = :projectId AND pn.no = :no AND  hn.projectId = :projectId AND hn.parentId = :parentId AND hn.hierarchyType = :hierarchyType AND hn.deleted = false")
    Optional<HierarchyNode> findByNoAndProjectIdAndParentIdAndDeletedIsFalse(
        @Param("no") String no,
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId,
        @Param("hierarchyType") String hierarchyType
    );

    /**
     * 取得指定实体的节点。
     *
     * @param entityId 实体 ID
     * @return 节点列表
     */
    @Query(value = "SELECT hn FROM HierarchyNode hn INNER JOIN ProjectNode pn ON pn.id = hn.node.id AND pn.entityId = :entityId WHERE hn.deleted = false")
    List<HierarchyNode> findByEntityIdAndDeletedIsFalse(@Param("entityId") Long entityId);

    /**
     * 根据层级类型，实体ID取得指定实体的层级节点。
     *
     * @param orgId         组织ID
     * @param projectId     项目 ID
     * @param entityId      实体 ID
     * @param hierarchyType 层级类型
     * @return 节点
     */
    @Query(value = "SELECT hn FROM HierarchyNode hn INNER JOIN ProjectNode pn "
        + "ON pn.orgId = hn.orgId AND pn.projectId = hn.projectId AND pn.id = hn.node.id " +
        "AND hn.hierarchyType = :hierarchyType "
        + "WHERE pn.projectId = :projectId AND pn.entityId = :entityId AND hn.orgId = :orgId AND hn.projectId = :projectId AND hn.deleted = false")
    HierarchyNode findByEntityIdAndHierarchyTypeAndDeletedIsFalse(@Param("orgId") Long orgId,
                                                                  @Param("projectId") Long projectId,
                                                                  @Param("entityId") Long entityId,
                                                                  @Param("hierarchyType") String hierarchyType);

    /**
     * 取得所有节点。
     *
     * @param nodeIDs 节点 ID 列表
     * @return 节点列表
     */
    List<HierarchyNode> findByIdInOrderBySortAsc(List<Long> nodeIDs);

    /**
     * 根据 ID 取得节点列表。
     *
     * @param entryIDs 节点 ID 列表
     * @return 节点列表
     */
    List<HierarchyNode> findByIdInAndDeletedIsFalseOrderBySortAsc(List<Long> entryIDs);

    /**
     * 取得指定节点下的节点列表。
     *
     * @param projectId    所属项目 ID
     * @param rootParentId 根节点 ID
     * @param depthFrom    起始深度
     * @param depthUntil   截止深度
     * @return 节点列表
     */
    List<HierarchyNode> findByProjectIdAndPathLikeAndDepthGreaterThanEqualAndDepthLessThanEqualAndDeletedIsFalseOrderBySortAsc(Long projectId, String rootParentId, int depthFrom, int depthUntil);

    /**
     * 取得指定节点下的节点列表。
     *
     * @param projectId      所属项目 ID
     * @param hierarchyTypes 节点层级类型
     * @return 节点列表
     */
    @Query("SELECT n FROM HierarchyNode n WHERE n.projectId = :projectId AND n.hierarchyType IN :hierarchyTypes AND n.deleted = FALSE ORDER BY n.sort ASC")
    List<HierarchyNode> findChildrenByHierarchyTypes(
        @Param("projectId") Long projectId,
        @Param("hierarchyTypes") Set<String> hierarchyTypes
    );

    /**
     * 取得指定节点下的节点列表。
     *
     * @param projectId      所属项目 ID
     * @param hierarchyTypes 节点层级类型
     * @param maxDepth       截止深度
     * @return 节点列表
     */
    @Query("SELECT n FROM HierarchyNode n WHERE n.projectId = :projectId AND n.hierarchyType IN :hierarchyTypes AND n.depth <= :maxDepth AND n.deleted = FALSE ORDER BY n.sort ASC")
    List<HierarchyNode> findChildrenByHierarchyTypesAndMaxDepth(
        @Param("projectId") Long projectId,
        @Param("hierarchyTypes") Set<String> hierarchyTypes,
        @Param("maxDepth") int maxDepth
    );

    /**
     * 取得指定节点下的节点列表。
     *
     * @param projectId         所属项目 ID
     * @param hierarchyTypes    节点层级类型
     * @param exceptEntityTypes 排除的实体类型
     * @param maxDepth          截止深度
     * @return 节点列表
     */
    @Query("SELECT n FROM HierarchyNode n WHERE n.projectId = :projectId AND n.hierarchyType IN :hierarchyTypes AND n.node.entityType NOT IN :exceptEntityTypes AND n.depth <= :maxDepth AND n.deleted = FALSE ORDER BY n.sort ASC")
    List<HierarchyNode> findChildrenByEntityTypesAndMaxDepth(
        @Param("projectId") Long projectId,
        @Param("hierarchyTypes") Set<String> hierarchyTypes,
        @Param("exceptEntityTypes") Set<String> exceptEntityTypes,
        @Param("maxDepth") int maxDepth
    );


//    /**
//     * 取得指定节点下的节点列表。
//     * @param projectId 所属项目 ID
// s    * @param exceptNodeTypes 例外节点类型
//     * @param maxDepth 截止深度
//     * @return 节点列表
//     */
//    @Query("SELECT n FROM HierarchyNode n WHERE n.projectId = :projectId AND n.node.nodeType NOT IN :exceptNodeTypes AND n.depth <= :maxDepth AND n.deleted = FALSE ORDER BY n.sort ASC")
//    List<HierarchyNode> findChildrenByNodeTypeAndMaxDepth(
//        @Param("projectId") Long projectId,
//        @Param("exceptNodeTypes") Set<HierarchyNodeType> exceptNodeTypes,
//        @Param("maxDepth") int maxDepth
//    );

    /**
     * 取得指定节点下的节点列表。
     *
     * @param projectId      所属项目 ID
     * @param rootParentId   根节点 ID
     * @param hierarchyTypes 节点层级类型
     * @param maxDepth       截止深度
     * @return 节点列表
     */
    @Query("SELECT n FROM HierarchyNode n WHERE n.projectId = :projectId AND n.path LIKE :rootParentId AND n.hierarchyType IN :hierarchyTypes AND n.depth <= :maxDepth AND n.deleted = FALSE ORDER BY n.sort ASC")
    List<HierarchyNode> findChildrenByRootIdAndHierarchyTypeAndMaxDepth(
        @Param("projectId") Long projectId,
        @Param("rootParentId") String rootParentId,
        @Param("hierarchyTypes") Set<String> hierarchyTypes,
        @Param("maxDepth") int maxDepth
    );

    /**
     * 取得指定节点下的节点列表（增加图纸类型的筛选）。
     *
     * @param projectId      所属项目 ID
     * @param rootParentId   根节点 ID
     * @param hierarchyTypes 节点层级类型
     * @param maxDepth       截止深度
     * @return 节点列表
     */
    @Query("SELECT n FROM HierarchyNode n WHERE n.projectId = :projectId AND n.path LIKE :rootParentId AND n.hierarchyType IN :hierarchyTypes AND " +
        "(n.drawingType = :drawingType or n.drawingType is null) AND n.depth <= :maxDepth AND n.deleted = FALSE ORDER BY n.sort ASC")
    List<HierarchyNode> findChildrenByRootIdAndHierarchyTypeAndDrawingTypeAndMaxDepth(
        @Param("projectId") Long projectId,
        @Param("rootParentId") String rootParentId,
        @Param("hierarchyTypes") Set<String> hierarchyTypes,
        @Param("drawingType") String drawingType,
        @Param("maxDepth") int maxDepth
    );


    /**
     * 取得指定节点下的指定维度的节点列表。
     *
     * @param projectId     所属项目 ID
     * @param rootParentId  根节点 ID
     * @param hierarchyType 节点层级类型
     * @return 节点列表
     */
    @Query("SELECT n FROM HierarchyNode n WHERE n.projectId = :projectId AND n.path LIKE :rootParentId AND n.hierarchyType = :hierarchyType AND n.deleted = FALSE ORDER BY n.sort ASC")
    List<HierarchyNode> findChildrenByRootIdAndHierarchyType(
        @Param("projectId") Long projectId,
        @Param("rootParentId") String rootParentId,
        @Param("hierarchyType") String hierarchyType
    );

//    /**
//     * 取得指定节点下的节点列表。
//     * @param projectId 所属项目 ID
//     * @param rootParentId 根节点 ID
//     * @param exceptNodeTypes 例外节点类型
//     * @param maxDepth 截止深度
//     * @return 节点列表
//     */
//    @Query("SELECT n FROM HierarchyNode n WHERE n.projectId = :projectId AND n.path LIKE :rootParentId AND n.node.nodeType NOT IN :exceptNodeTypes AND n.depth <= :maxDepth AND n.deleted = FALSE ORDER BY n.sort ASC")
//    List<HierarchyNode> findChildrenByRootIdAndNodeTypeAndMaxDepth(
//        @Param("projectId") Long projectId,
//        @Param("rootParentId") String rootParentId,
//        @Param("exceptNodeTypes") Set<HierarchyNodeType> exceptNodeTypes,
//        @Param("maxDepth") int maxDepth
//    );

    /**
     * 根据上级节点取得节点列表。
     *
     * @param parentId 上级节点 ID
     * @return 节点列表
     */
    List<HierarchyNode> findByParentIdAndProjectIdAndDeletedIsFalseOrderBySortAsc(Long parentId, Long projectId);

    /**
     * 对同一项目下的节点重新排序。
     *
     * @param projectId 所属项目 ID
     * @param sortFrom  起始排序号
     * @param increment 排序增量
     */
    @Modifying
    @Transactional
    @Query("UPDATE HierarchyNode n SET n.sort = n.sort + :increment WHERE n.projectId = :projectId AND n.sort > :sortFrom")
    void reorder(
        @Param("projectId") Long projectId,
        @Param("sortFrom") int sortFrom,
        @Param("increment") int increment
    );

    /**
     * 逻辑删除项目层级结构节点。
     *
     * @param nodeIDs 层级结构节点
     */
    @Modifying
    @Transactional
    @Query("UPDATE HierarchyNode n SET n.deleted = true, n.deletedBy = :deletedBy, n.deletedAt = :deletedAt, n.version = :version WHERE n.id IN :nodeIDs AND n.deleted = false")
    void deleteByIdInAndDeletedIsFalse(
        @Param("nodeIDs") List<Long> nodeIDs,
        @Param("deletedBy") Long deletedBy,
        @Param("deletedAt") Date deletedAt,
        @Param("version") long version
    );

    /**
     * 删除子节点。
     *
     * @param nodeId    节点 ID
     * @param deletedBy 删除者 ID
     * @param deletedAt 删除时间
     * @param version   最后更新版本号
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE hierarchy_node n JOIN hierarchy_node_relation hnr ON n.id = hnr.hierarchy_ancestor_id " +
        "SET n.deleted = 1, n.status = 'DELETED', " +
        "n.deleted_by = :deletedBy, n.deleted_at = :deletedAt, n.version = :version " +
        "WHERE hnr.hierarchy_ancestor_id = :nodeId AND n.deleted = false", nativeQuery = true)
    void deleteChildren(
        @Param("nodeId") Long nodeId,
        @Param("deletedBy") Long deletedBy,
        @Param("deletedAt") Date deletedAt,
        @Param("version") long version
    );

    /**
     * 更新被移动节点的子节点。
     *
     * @param projectId     所属项目 ID
     * @param depthInc      深度变化量
     * @param pathOld       旧路径前缀
     * @param pathNew       新路径前缀
     * @param hierarchyType 层级类型
     */
    @Modifying
    @Transactional
    @Query("UPDATE HierarchyNode n SET n.depth = n.depth + :depthInc, n.path = replace(n.path, :pathOld, :pathNew) WHERE n.projectId = :projectId AND n.path LIKE '%' || :pathOld || '%' and n.hierarchyType = :hierarchyType")
    void moveChild(
        @Param("projectId") Long projectId,
        @Param("depthInc") int depthInc,
        @Param("pathOld") String pathOld,
        @Param("pathNew") String pathNew,
        @Param("hierarchyType") String hierarchyType
    );

    /**
     * 根据项目节点ID（project_node），层级类型取得被移动节点。
     *
     * @param projectId     项目 ID
     * @param nodeId        项目节点信息
     * @param hierarchyType 层级类型
     * @return 层级节点信息
     */
    HierarchyNode findByProjectIdAndNodeIdAndHierarchyTypeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("nodeId") Long nodeId,
        @Param("hierarchyType") String hierarchyType
    );

//    /**
//     * 根据项目节点ID（project_node），层级类型取得被移动节点。
//     * @param projectId 项目 ID
//     * @param nodeId 项目节点信息
//     * @param entityType 实体类型
//     * @param hierarchyType 层级类型
//     * @return 层级节点信息
//     */
//    @Query("SELECT n FROM HierarchyNode n WHERE n.projectId = :projectId AND n.path LIKE :rootParentId AND n.deleted = FALSE ORDER BY n.sort ASC")
//    HierarchyNode findByProjectIdAndNodeIdAndEntityTypeAndHierarchyTypeAndDeletedIsFalse(
//        @Param("projectId") Long projectId,
//        @Param("nodeId") String nodeId,
//        @Param("entityType") String entityType,
//        @Param("hierarchyType") String hierarchyType
//    );


    /**
     * 取得指定实体的节点。
     *
     * @param entityId  实体 ID
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 节点列表
     */
    @Query(value = "SELECT "
        + "hn "
        + "FROM HierarchyNode hn  "
        + "INNER JOIN ProjectNode pe ON pe.projectId = hn.projectId AND pe.id = hn.node.id AND hn.hierarchyType = 'PIPING' AND hn.deleted = FALSE "
        + "where pe.deleted = FALSE "
        + "  AND pe.orgId = :orgId "
        + "  AND pe.projectId = :projectId "
        + "  AND pe.entityId = :entityId ")
    List<HierarchyNode> findByEntityIdAndDeletedIsFalse(@Param("entityId") Long entityId,
                                                        @Param("orgId") Long orgId,
                                                        @Param("projectId") Long projectId);

    /**
     * 根据上级节点ID和层级类型取得节点列表。
     *
     * @param parentId      上级节点 ID
     * @param projectId     项目ID
     * @param hierarchyType 层级类型
     * @return 节点列表
     */
    List<HierarchyNode> findByParentIdAndProjectIdAndAndHierarchyTypeAndDeletedIsFalse(Long parentId,
                                                                                       Long projectId,
                                                                                       String hierarchyType);

    /**
     * 根据上级节点ID和层级类型取得节点列表。
     *
     * @param parentIds     上级节点 ID
     * @param projectId     项目ID
     * @param hierarchyType 层级类型
     * @return 节点列表
     */
    List<HierarchyNode> findByParentIdInAndProjectIdAndAndHierarchyTypeAndDeletedIsFalse(List<Long> parentIds,
                                                                                         Long projectId,
                                                                                         String hierarchyType);

    /**
     * 将导入的多余的层级信息逻辑删除。
     *
     * @param redundantId 项目 ID
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE "
        + "  hierarchy_node "
        + "SET "
        + "  deleted_at = CURRENT_TIMESTAMP(), "
        + "  deleted = 1, "
        + "  status = 'DELETED' "
        + "WHERE "
        + "  id = :redundantId", nativeQuery = true)
    void deleteRedundantNodeById(
        @Param("redundantId") Long redundantId
    );

    /**
     * 将导入的ISO多余的层级信息逻辑删除。
     *
     * @param projectId 项目 ID
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE "
        + "  hierarchy_node "
        + "SET "
        + "  deleted_at = CURRENT_TIMESTAMP(), "
        + "  deleted = 1, "
        + "  status = 'DELETED' "
        + "WHERE "
        + "  id IN ( "
        + "    SELECT "
        + "      id "
        + "    FROM ( "
        + "      SELECT "
        + "        shn.id "
        + "      FROM "
        + "        hierarchy_node AS hn "
        + "        INNER JOIN hierarchy_node AS phn "
        + "          ON phn.id = hn.parent_id "
        + "          AND phn.deleted = 0 "
        + "        INNER JOIN project_node AS pn "
        + "          ON pn.id = hn.node_id "
        + "          AND pn.entity_type = 'CABLE' "
        + "          AND pn.deleted = 0 "
        + "        INNER JOIN hierarchy_node AS shn "
        + "          ON shn.node_id = pn.id "
        + "          AND shn.id < hn.id "
        + "          AND shn.hierarchy_type = hn.hierarchy_type "
        + "          AND shn.deleted = 0 "
        + "        INNER JOIN hierarchy_node AS sphn "
        + "          ON sphn.id = shn.parent_id "
        + "          AND sphn.hierarchy_type = phn.hierarchy_type "
        + "          AND sphn.deleted = 0 "
        + "      WHERE "
        + "        hn.project_id = :projectId "
        + "        AND hn.deleted = 0 "
        + "    ) AS hn "
        + "  )", nativeQuery = true)
    void updateCableHierarchyInfo(
        @Param("projectId") Long projectId
    );


    /**
     * 根据实体ID取得层级节点
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param hierarchyId
     * @return
     */
    @Query(value = "select hhn.id, hhn.hierarchy_type " +
        "from " +
        "hierarchy_node hn left join hierarchy_node hhn on hhn.node_id = hn.node_id " +
        "where hn.project_id =:projectId " +
        "and hn.org_id =:orgId " +
        "and hn.id =:hierarchyId "
        ,
        nativeQuery = true)
    List<Tuple> findByOrgIdAndProjectIdAndEntityIdAndDeletedIsFalse(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("hierarchyId") Long hierarchyId);

    /**
     * 根据实体ID取得层级节点
     *
     * @param projectId   项目ID
     * @param hierarchyType
     * @return
     */
    @Query(value = "SELECT hn " +
        "FROM " +
        "HierarchyNode hn JOIN ProjectNode pn on hn.node.id = pn.id " +
        "WHERE pn.projectId =:projectId AND pn.entityId = :entityId AND hn.hierarchyType = :hierarchyType" +
        " and hn.deleted = FALSE AND pn.deleted = FALSE ")
    HierarchyNode findByProjectIdAndEntityIdAndHierarchyTypeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("hierarchyType") String hierarchyType);

    /**
     * 根据实体ID取得层级节点
     *
     * @param projectId   项目ID
     * @param hierarchyType
     * @return
     */
    @Query(value = "SELECT hn " +
        "FROM " +
        "HierarchyNode hn JOIN ProjectNode pn on hn.node.id = pn.id " +
        "WHERE pn.projectId =:projectId AND pn.entityId = :entityId AND hn.hierarchyType = :hierarchyType" +
        " and hn.deleted = FALSE AND pn.deleted = FALSE ")
    List<HierarchyNode> findsByProjectIdAndEntityIdAndHierarchyTypeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("hierarchyType") String hierarchyType);

    /**
     * 根据节点编号及所属项目 ID 取得节点信息。
     *
     * @param no            节点编号
     * @param projectId     项目 ID
     * @param hierarchyType 层级类型
     * @return 节点信息
     */
    @Query(value = "SELECT hn FROM HierarchyNode hn INNER JOIN ProjectNode pn ON pn.id = hn.node.id WHERE pn.projectId = :projectId " +
        "AND pn.no = :no AND hn.projectId = :projectId AND hn.hierarchyType = :hierarchyType AND hn.deleted = false AND hn.status = com.ose.vo.EntityStatus.ACTIVE")
    HierarchyNode findByNoAndProjectIdAndHierarchyTypeAndDeletedIsFalse(@Param("no") String no,
                                                                        @Param("projectId") Long projectId,
                                                                        @Param("hierarchyType") String hierarchyType);

    HierarchyNode findByProjectIdAndNodeNoAndHierarchyTypeAndDeletedIsFalse(Long projectId, String parentNo, String hierarchyType);

    @Query(value = "SELECT id AS hierarchyId, path, depth FROM hierarchy_node WHERE project_id = :projectId AND deleted = 0 limit :start, :offset",
        nativeQuery = true)
    List<Tuple> getHierarchyInfosByPage(@Param("projectId") Long projectId,
                                        @Param("start") int start,
                                        @Param("offset") int offset);


    @Query(value = "SELECT id FROM "
        + "  hierarchy_node "
        + "WHERE "
        + "  id IN ( "
        + "    SELECT "
        + "      id "
        + "    FROM ( "
        + "      SELECT "
        + "        shn.id "
        + "      FROM "
        + "        hierarchy_node AS hn "
        + "        INNER JOIN hierarchy_node AS phn "
        + "          ON phn.id = hn.parent_id "
        + "          AND phn.deleted = 0 "
        + "        INNER JOIN project_node AS pn "
        + "          ON pn.id = hn.node_id "
        + "          AND pn.deleted = 0 "
        + "        INNER JOIN hierarchy_node AS shn "
        + "          ON shn.node_id = pn.id "
        + "          AND shn.id < hn.id "
        + "          AND shn.hierarchy_type = hn.hierarchy_type "
        + "          AND shn.deleted = 0 "
        + "        INNER JOIN hierarchy_node AS sphn "
        + "          ON sphn.id = shn.parent_id "
        + "          AND sphn.hierarchy_type = phn.hierarchy_type "
        + "          AND sphn.deleted = 0 "
        + "      WHERE "
        + "        hn.project_id = :projectId "
        + "        AND hn.deleted = 0 "
        + "    ) AS hn "
        + "  )", nativeQuery = true)
    List<BigInteger> findRedundantNodeIds(@Param("projectId") Long projectId);


    @Query(value = "SELECT parent_id FROM hierarchy_node WHERE project_id = :projectId AND node_id = :projectNodeId AND deleted = 0", nativeQuery = true)
    List<BigInteger> findParentIdByProjectIdAndNodeIdAndDeletedIsFalse(@Param("projectId") Long projectId,
                                                                       @Param("projectNodeId") Long projectNodeId);

    @Query(value = "SELECT node_id FROM hierarchy_node WHERE project_id = :projectId AND parent_id = :parentHnId AND deleted = 0", nativeQuery = true)
    List<BigInteger> findSiblingPnIds(@Param("projectId") Long projectId,
                                      @Param("parentHnId") Long parentHnId);


    @Query(value = "SELECT shn.node_id FROM hierarchy_node hn JOIN hierarchy_node shn ON hn.parent_id = shn.parent_id " +
        " WHERE hn.project_id = :projectId AND hn.node_id = :projectNodeId AND shn.deleted = 0 ", nativeQuery = true)
    List<BigInteger> findSiblingProjectNodeIds(@Param("projectId") Long projectId,
                                               @Param("projectNodeId") Long projectNodeId);

    @Query(value = " SELECT pp FROM ProjectNode p "
        + " INNER JOIN HierarchyNodeRelation hr "
        + " ON p.id = hr.nodeId "
        + " AND hr.depth = 1 "
        + " INNER JOIN ProjectNode pp "
        + " ON hr.nodeAncestorId = pp.id "
        + " WHERE p.projectId =:projectId "
        + " AND p.entityId =:entityId  "
        + " AND p.discipline =:discipline  ")
    ProjectNode findProjectNodeByEntityIdAndDiscipline(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("discipline") String discipline
    );

    @Modifying
    @Transactional
    @Query(value="DELETE FROM hierarchy_node WHERE id = :id", nativeQuery = true)
    void deleteHierarchyById(@Param("id") Long id);
}
