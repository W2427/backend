package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.ProjectNode;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 项目节点 CRUD 操作接口。
 */
public interface ProjectNodeRepository extends PagingAndSortingWithCrudRepository<ProjectNode, Long>, ProjectNodeCustomRepository {

    /**
     * 根据项目 ID 及节点编号取得节点信息。
     *
     * @param projectId 项目 ID
     * @param no        节点编号
     * @return 节点信息
     */
    Optional<ProjectNode> findByProjectIdAndNoAndDeletedIsFalse(Long projectId, String no);

    List<ProjectNode> findByIdIn(Long[] ids);

    List<ProjectNode> findByIdInAndDeletedIsFalse(Long[] ids);

    Optional<ProjectNode> findByProjectIdAndEntityIdAndDeletedIsFalse(Long projectId, Long entityId);

    /**
     * 根据项目 ID 及节点 ID 取得节点信息。
     *
     * @param projectId 项目 ID
     * @param id        节点 ID
     * @return 节点信息
     */
    Optional<ProjectNode> findByProjectIdAndIdAndDeletedIsFalse(Long projectId, Long id);

    /**
     * 取得实体的项目节点。
     *
     * @param projectId  项目 ID
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @return 项目节点
     */
    Optional<ProjectNode> findByProjectIdAndEntityTypeAndEntityIdAndDeletedIsFalse(Long projectId, String entityType, Long entityId);

    /**
     * 取得指定类型的项目节点的列表。
     *
     * @param projectId 所属项目 ID
     * @param entityTypes 项目节点类型数组
     * @return 项目节点列表
     */
    List<ProjectNode> findByProjectIdAndEntityTypeInAndDeletedIsFalse(Long projectId, String[] entityTypes);

    /**
     * 取得未挂载到层级结构的实体。
     *
     * @param projectId 所属项目 ID
     * @param pageable  分页参数
     * @return 项目节点分页数据
     */
    @Query("SELECT pn FROM ProjectNode pn LEFT JOIN com.ose.tasks.entity.HierarchyNode hn ON hn.node.id = pn.id AND hn.deleted = FALSE WHERE pn.projectId = :projectId AND pn.deleted = FALSE AND hn.id IS NULL")
    Page<ProjectNode> findUnmountedEntities(
        @Param("projectId") Long projectId,
        Pageable pageable
    );

    /**
     * 取得未挂载到层级结构的实体。
     *
     * @param projectId  所属项目 ID
     * @param entityType 实体类型
     * @param pageable   分页参数
     * @return 项目节点分页数据
     */
    @Query("SELECT pn FROM ProjectNode pn LEFT JOIN com.ose.tasks.entity.HierarchyNode hn ON hn.node.id = pn.id AND hn.deleted = FALSE WHERE pn.projectId = :projectId AND pn.entityType = :entityType AND pn.deleted = FALSE AND hn.id IS NULL")
    Page<ProjectNode> findUnmountedEntitiesWithEntityType(
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        Pageable pageable
    );

    /**
     * 删除指定实体的project_node节点。
     *
     * @param nodeId    实体 PN_ID
     * @param deletedBy 删除者 ID
     * @param deletedAt 删除时间
     * @param version   最后更新版本号
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE ProjectNode pn SET pn.no = CONCAT(pn.no, '[deleted:', :version, ']'), pn.deleted = true, pn.status = 'DELETED', pn.deletedBy = :deletedBy, pn.deletedAt = :deletedAt, pn.version = :version  WHERE pn.id = :nodeId AND pn.deleted = false ")
    void deleteByEntityIdAndDeletedIsFalse(
        @Param("nodeId") Long nodeId,
        @Param("deletedBy") Long deletedBy,
        @Param("deletedAt") Date deletedAt,
        @Param("version") long version
    );

    @Query("SELECT pn FROM ProjectNode pn WHERE pn.projectId = :projectId AND pn.orgId = :orgId AND pn.entityId = :entityId AND pn.deleted = FALSE")
    ProjectNode findEntityByEntityId(
        @Param("projectId") Long projectId,
        @Param("orgId") Long orgId,
        @Param("entityId") Long entityId
    );

    /**
     * 根据项目 ID 及节点编号以及节点实体类型判断是否存在。
     *
     * @param projectId  项目 ID
     * @param no         节点编号
     * @param entityType 节点实体类型
     * @return 存在：true; 不存在：false；
     */
    boolean existsByProjectIdAndNoAndEntityTypeAndDeletedIsFalse(Long projectId, String no, String entityType);

    @Query(value = "SELECT count(pn) FROM ProjectNode pn WHERE pn.projectId = :projectId AND pn.entityId = :entityId AND pn.isDeletable = TRUE " )
    Long isDeletableEntity(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    boolean existsByProjectIdAndEntityIdAndIsDeletableIsTrue(Long projectId, Long entityId);

    /**
     * 根据项目 ID 及节点编号取得节点信息。
     *
     * @param orgId      组织ID
     * @param projectId  项目 ID
     * @param entityType 实体类型
     * @param no         节点编号
     * @return 节点信息
     */
    Optional<ProjectNode> findByOrgIdAndProjectIdAndEntityTypeAndNoAndDeletedIsFalse(Long orgId, Long projectId, String entityType, String no);

    /**
     * 根据层级ID、项目ID、组织ID取得实体信息。
     *
     * @param hierarchyId 层级ID
     * @param projectId   所属项目 ID
     * @param orgId       组织ID
     * @return 项目节点信息
     */
    @Query("SELECT pn FROM ProjectNode pn INNER JOIN HierarchyNode hn ON hn.node.id = pn.id AND pn.projectId = hn.projectId AND pn.deleted = FALSE WHERE hn.id = :hierarchyId AND hn.projectId = :projectId AND hn.orgId = :orgId AND hn.deleted = FALSE")
    Optional<ProjectNode> findByHierarchyIdAndProjectIdAndOrgId(
        @Param("hierarchyId") Long hierarchyId,
        @Param("projectId") Long projectId,
        @Param("orgId") Long orgId
    );

    Optional<ProjectNode> findByProjectIdAndEntityIdAndDeleted(Long projectId, Long pipePieceEntityId, boolean deteted);

    /**
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param module
     */
    ProjectNode findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("no") String module);

    /**
     * 根据组织ID、项目ID、实体编号取得实体节点信息。
     *
     * @param orgId     组织ID
     * @param projectId 所属项目 ID
     * @param no        实体编号
     * @return 项目节点信息
     */
    @Query("SELECT pn FROM ProjectNode pn INNER JOIN HierarchyNode hn ON hn.node.id = pn.id AND pn.orgId = hn.orgId AND pn.projectId = hn.projectId WHERE pn.orgId = :orgId AND pn.projectId = :projectId AND pn.no=:no AND pn.deleted=FALSE AND hn.hierarchyType=:hierarchyType AND hn.deleted = FALSE")
    Optional<ProjectNode> findByOrgIdAndProjectIdAndNo(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("no") String no,
        @Param("hierarchyType") String hierarchyType
    );

    @Query(value = "SELECT no FROM project_node WHERE project_id = :projectId AND org_id = :orgId AND deleted = 0 AND entity_type IN ('WP01','SUB_SYSTEM')",
        nativeQuery = true)
    Set<String> findModuleNames(@Param("orgId") Long orgId,
                                @Param("projectId") Long projectId);

    @Query(value = "SELECT no FROM project_node WHERE project_id = :projectId AND org_id = :orgId AND deleted = 0 AND entity_type IN ('FUNCTION')",
        nativeQuery = true)
    Set<String> findFunctions(@Param("orgId") Long orgId,
                                @Param("projectId") Long projectId);

    @Query(value = "SELECT no FROM project_node WHERE project_id = :projectId AND org_id = :orgId AND deleted = 0 AND entity_type IN ('TYPE')",
        nativeQuery = true)
    Set<String> findTypes(@Param("orgId") Long orgId,
                              @Param("projectId") Long projectId);

    @Query(value = "SELECT ppn.entity_id FROM project_node pn JOIN hierarchy_node hn ON pn.id = hn.node_id  AND hn.hierarchy_type = 'GENERAL'" +
        "JOIN hierarchy_node phn on phn.id = hn.parent_id JOIN project_node ppn ON ppn.id = phn.node_id " +
        "WHERE pn.entity_id = :spoolId AND ppn.entity_type = 'ISO' AND pn.deleted = 0 AND hn.deleted = 0 AND pn.project_id = :projectId " +
        "AND pn.org_id = :orgId", nativeQuery = true)

    Long findIsoNoBySpoolId(@Param("orgId") Long orgId,
                            @Param("projectId") Long projectId,
                            @Param("spoolId") Long spoolId);

    /**
     * 删除指定实体的project_node节点。
     *
     * @param nodeId        实体 PN_ID
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE project_node SET is_cancelled = 1, status = 'CANCEL', " +
        "last_modified_by = :operatorId, last_modified_at = :cancelledAt, version = :version  " +
        "WHERE id = :nodeId AND deleted = 0 ",
        nativeQuery = true)
    void cancelByEntityIdAndDeletedIsFalse(
        @Param("nodeId") Long nodeId,
        @Param("operatorId") Long operatorId,
        @Param("cancelledAt") Date deletedAt,
        @Param("version") long version);

    Optional<ProjectNode> findByProjectIdAndEntityIdAndStatus(Long projectId, Long pipePieceEntityId, EntityStatus status);

    Optional<ProjectNode> findByProjectIdAndIdAndStatus(Long projectId, Long projectNodeId, EntityStatus status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE " +
        " project_node " +
        " SET is_deletable = 0 " +
        " WHERE project_id = :projectId AND entity_id = :entityId ", nativeQuery = true)
    void setDeletableStatus(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    @Query(value = "SELECT no FROM project_node WHERE project_id = :projectId AND org_id = :orgId AND deleted = 0 AND entity_type = 'MODULE'",
        nativeQuery = true)
    Set<String> findModuleNameSet(@Param("orgId") Long orgId,
                                  @Param("projectId") Long projectId);

    Set<ProjectNode> findByProjectIdAndEntityTypeAndDeletedIsFalse(Long projectId, String system);

    @Query("SELECT ppn FROM ProjectNode pn JOIN HierarchyNode hn on hn.node.id = pn.id " +
        "JOIN HierarchyNode phn ON phn.id = hn.parentId JOIN ProjectNode ppn ON ppn.id = phn.node.id " +
        " WHERE pn.projectId = :projectId AND pn.id = :id")
    ProjectNode findParentByProjectIdAndId(@Param("projectId") Long projectId,
                                           @Param("id") Long id);


    @Query("SELECT pn FROM ProjectNode pn JOIN HierarchyNode hn on hn.node.id = pn.id " +
        "JOIN HierarchyNode phn ON phn.id = hn.parentId " +
        " WHERE phn.projectId = :projectId AND phn.node.id = :id AND pn.deleted = FALSE")
    Set<ProjectNode> findByProjectIdAndParentIdAndDeletedIsFalse(@Param("projectId") Long projectId,
                                                                 @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectNode eq SET eq.no = :intmName,eq.displayName = :intmName WHERE eq.no = :sourceName")
    void updateNo(@Param("sourceName") String sourceName, @Param("intmName") String intmName);

    @Query("SELECT pn.id FROM ProjectNode pn WHERE pn.projectId = :projectId AND pn.entityId IN :entityIds AND pn.deleted = FALSE")
    List<Long> findByProjectIdAndEntityIdInAndDeletedIsFalse(@Param("projectId") Long projectId,
                                                             @Param("entityIds") List<Long> ssIds);


    List<ProjectNode> findByProjectIdAndEntityIdInAndStatus(Long projectId, List<Long> entityIds, EntityStatus entityStatus);

    Optional<ProjectNode> findByProjectIdAndNoAndEntityTypeAndDeletedIsFalse(Long id, String parentNo, String parentType);
}
