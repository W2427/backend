package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.HierarchyNodeRelation;
import com.ose.tasks.entity.ProjectNode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface HierarchyNodeRelationRepository extends PagingAndSortingWithCrudRepository<HierarchyNodeRelation, Long> {


    HierarchyNodeRelation findByHierarchyIdAndHierarchyAncestorId(Long hierarchyId, Long hierarchyAncestorId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM hierarchy_node_relation WHERE project_id = :projectId AND hierarchy_ancestor_id = :hierarchyId",
        nativeQuery = true)
    void deleteDescendant(@Param("projectId") Long projectId,
                          @Param("hierarchyId") Long hierarchyId);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM hierarchy_node_relation WHERE project_id = :projectId AND hierarchy_ancestor_id = :hierarchyId " +
        "AND hierarchy_id != hierarchy_ancestor_id",
        nativeQuery = true)
    void deleteDescendantNotIncludeSelf(@Param("projectId") Long projectId,
                                        @Param("hierarchyId") Long hierarchyId);


    @Query(value = "SELECT pn.id AS module_hierarchy_node_id FROM " +
        "hierarchy_node hn INNER JOIN project_node pn " +
        "ON hn.node_id = pn.id AND hn.deleted = 0 " +
//        "AND hn.hierarchy_type IN ("PIPING",'PRESSURE_TEST_PACKAGE','CLEAN_PACKAGE','SUB_SYSTEM') " +
        "WHERE pn.entity_type IN ('WP01','SUB_SYSTEM') AND hn.project_id = :projectId " +
        "AND hn.id = :hierarchyAncestorId " +
        "AND hn.deleted = 0",
        nativeQuery = true)
    Long findModuleHierarchyNodeId(@Param("projectId") Long projectId,
                                   @Param("hierarchyAncestorId") Long hierarchyAncestorId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM hierarchy_node_relation WHERE project_id = :projectId", nativeQuery = true)
    void deleteAllByProjectId(@Param("projectId") Long projectId);

    /**
     * 查找ISO实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_iso " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findIsoIdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找ISO实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_line " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findLineIdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找spool实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_spool " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findSpoolIdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找pipe实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_pipe_piece " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findPipeIdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找component实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_component " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findComponentIdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找weld实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_weld " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findWeldIdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找wp01实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_wp01 " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findWp01IdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找wp02实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_wp02 " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findWp02IdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找wp03实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_wp03 " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findWp03IdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找wp04实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_wp04 " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findWp04IdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找wp05实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_wp05 " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findWp05IdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    /**
     * 查找结构焊口实体层级筛选。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param hierarchyAncestorIds
     * @return
     */
    @Query(value = "select DISTINCT id  " +
        "from entity_structure_weld " +
        "where id in ( " +
        "select pn.entity_id " +
        "from hierarchy_node_relation phnr " +
        "inner join hierarchy_node_relation hnr " +
        "on phnr.hierarchy_id=hnr.hierarchy_id " +
        "and hnr.depth>phnr.depth " +
        "and hnr.entity_type=:entityType " +
        "inner join project_node pn " +
        "on pn.id=hnr.node_id " +
        "where phnr.org_id=:orgId " +
        "and phnr.project_id=:projectId " +
        "and phnr.hierarchy_ancestor_id in :hierarchyAncestorIds) "
        ,
        nativeQuery = true)
    List<Long> findStructWeldIdAndHierarchyAncestorIds(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("hierarchyAncestorIds") List<Long> hierarchyAncestorIds
    );

    List<HierarchyNodeRelation> findByOrgIdAndProjectIdAndNodeId(Long orgId, Long projectId, Long nodeId);

    List<HierarchyNodeRelation> findByProjectIdAndNodeIdOrderByDepthDesc(Long projectId, Long projectNodeId);


    List<HierarchyNodeRelation> findByProjectIdAndNodeAncestorIdOrderByDepthDesc(Long projectId, Long projectNodeId);

    @Query("SELECT hr.moduleHierarchyNodeId FROM HierarchyNodeRelation  hr " +
        "WHERE hr.nodeId = :relatedPnId AND hr.moduleHierarchyNodeId IS NOT NULL AND hr.projectId = :projectId")
    Set<Long> findModuleIdsByNodeId(@Param("projectId") Long projectId,
                                    @Param("relatedPnId") Long relatedPnId);

    HierarchyNodeRelation findFirstByOrgIdAndProjectIdAndNodeIdAndAncestorEntityTypeOrderByDepthAsc(Long orgId, Long projectId, Long id, String wbsEntityType);


    HierarchyNodeRelation findFirstByProjectIdAndNodeIdAndAncestorEntityType(Long projectId, Long projectNodeId, String ancestorEntityType);

    List<HierarchyNodeRelation> findByProjectIdAndNodeAncestorIdAndEntityType(Long projectId, Long next, String entityType);


    /**
     * 查找实体的父级信息。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @param entityId
     * @return
     */
    @Query("select  p  " +
        "from ProjectNode p " +
        "inner join HierarchyNodeRelation hr " +
        "on p.id = hr.nodeAncestorId " +
        "where p.orgId = :orgId " +
        "and p.projectId = :projectId " +
        "and hr.entityId  = :entityId  " +
        "and hr.ancestorEntityType  = :entityType  "
    )
    ProjectNode findByOrgIdAndProjectIdAndEntityTypeAndEntityId(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("entityId") Long entityId
    );


    @Query("SELECT hnr.id FROM HierarchyNodeRelation hnr WHERE hnr.projectId = :projectId AND hnr.entityId = :entityId")
    List<Long> findByProjectIdAndEntityId(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    @Query("SELECT hnr FROM HierarchyNodeRelation hnr WHERE hnr.projectId = :projectId AND hnr.entityId = :entityId")
    List<HierarchyNodeRelation> findNodeByProjectIdAndEntityId(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    @Query("SELECT hnr.hierarchyId FROM HierarchyNodeRelation hnr WHERE hnr.projectId = :projectId AND hnr.hierarchyAncestorId IN :ancestorHierarchyIds AND " +
        " hnr.entityType = 'SUB_SYSTEM'")
    Set<Long> findSubSystemIdsByAncestor(@Param("projectId") Long projectId, @Param("ancestorHierarchyIds") List<Long> ancestorHierarchyIds);

    HierarchyNodeRelation findFirstByProjectIdAndAncestorEntityIdAndEntityType(Long projectId, Long cablePackageId, String entityType);

    List<HierarchyNodeRelation> findByProjectIdAndAncestorEntityIdAndEntityType(Long projectId, Long ancestorEntityId, String entityType);

    List<HierarchyNodeRelation> findByProjectIdAndAncestorEntityIdInAndEntityType(Long projectId, Set<Long> lineIds, String entityType);


    @Query("SELECT hnr FROM HierarchyNodeRelation hnr WHERE hnr.projectId = :projectId AND hnr.entityId = :entityId")
    List<HierarchyNodeRelation> findHnrsByProjectIdAndEntityId(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    List<HierarchyNodeRelation> findByProjectIdAndAncestorEntityIdInAndEntityTypeIn(Long projectId, Set<Long> lineIds, List<String> entityTypes);


    @Modifying
    @Transactional
    @Query("DELETE FROM HierarchyNodeRelation hnr WHERE hnr.projectId = :projectId AND hnr.hierarchyId = :hierarchyId")
    void deleteByHierarchyId(@Param("projectId") Long projectId, @Param("hierarchyId") Long id);

    List<HierarchyNodeRelation> findByProjectIdAndAncestorEntityIdAndEntityTypeIn(Long projectId, Long entityId, Set<String> entityTypes);

    List<HierarchyNodeRelation> findByProjectIdAndAncestorEntityIdAndEntitySubTypeIn(Long projectId, Long entityId, Set<String> subEntityTypes);

    @Query("SELECT hnr.entityId FROM HierarchyNodeRelation hnr WHERE hnr.projectId = :projectId " +
        " AND hnr.hierarchyAncestorId IN :ancestorHierarchyIds AND hnr.entityType = 'SUB_SYSTEM'")
    List<Long> getSubSystemIdsByAncestorHierarchyIds(@Param("projectId") Long projectId,
                                                     @Param("ancestorHierarchyIds") List<Long> ancestorHierarchyIds);

    @Query("SELECT pn FROM ProjectNode pn JOIN HierarchyNodeRelation hnr ON hnr.nodeId = pn.id WHERE hnr.projectId = :projectId AND " +
        " hnr.ancestorEntityId = :pkgId AND pn.deleted = FALSE")
    List<ProjectNode> findByProjectIdAndAncestorEntityId(@Param("projectId") Long id, @Param("pkgId") Long entityId);

    @Query("SELECT pn.no FROM ProjectNode pn JOIN HierarchyNodeRelation hnr ON hnr.nodeId = pn.id WHERE hnr.projectId = :projectId AND " +
        " hnr.nodeAncestorId = :pkgId AND pn.deleted = FALSE AND hnr.entityType = 'SUB_SYSTEM'")
    List<String> getSubSystemNosByAncestorHierarchyId(@Param("projectId") Long id, @Param("pkgId") Long pkgId);
}
