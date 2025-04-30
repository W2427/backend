package com.ose.tasks.domain.model.repository;

import com.ose.tasks.entity.EntityHierarchyParent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

/**
 * 实体层级视图 CRUD 操作接口。
 */
public interface EntityHierarchyParentRepository extends CrudRepository<EntityHierarchyParent, Long> {

    /**
     * 根据实体 ID 及上级节点实体类型取得层级关系数据。
     *
     * @param entityId          实体 ID
     * @param parentEntityTypes 上级实体类型集合
     * @return 层级关系数据列表
     */
    List<EntityHierarchyParent> findByEntityIdAndParentEntityTypeIn(Long entityId, Collection<String> parentEntityTypes);


    /**
     * 根据实体 ID 及上级节点实体类型取得层级关系数据。
     *
     * @param entityId         实体 ID
     * @param parentEntityType 上级实体类型
     * @return 层级关系数据列表
     */
    List<EntityHierarchyParent> findByEntityIdAndParentEntityType(Long entityId, String parentEntityType);

    /**
     * 根据上级实体 ID 及目标实体类型取得层级关系数据。
     *
     * @param parentEntityId 上级实体 ID
     * @param entityType     目标实体类型
     * @return 层级关系数据列表
     */
    List<EntityHierarchyParent> findByParentEntityIdAndEntityType(Long parentEntityId, String entityType);


    /**
     * 根据上级实体 ID 及目标实体类型取得层级关系数据。
     *
     * @param parentEntityId
     * @param entityTypes    上级实体类型集合
     * @return
     */
    List<EntityHierarchyParent> findByParentEntityIdAndEntityTypeIn(Long parentEntityId, Collection<String> entityTypes);

    /**
     * 根据上级实体 ID 及目标实体类型取得层级关系数据。chazhao chaoji ziji
     *
     * @param entityId
     * @param parentEntityType
     * @return
     */
    @Query(value = "SELECT ancestor_entity_id FROM hierarchy_node_relation WHERE " +
        "entity_id = :entityId AND ancestor_entity_type = :parentEntityType", nativeQuery = true)

    List<BigInteger> findByPathLikeParentPath(
        @Param("entityId") Long entityId,
        @Param("parentEntityType") String parentEntityType);

    /**
     * 根据上级实体 ID 及目标实体类型取得层级关系数据。chazhao chaoji ziji
     *
     * @return
     */
    @Query(value = "SELECT entity_id FROM hierarchy_node_relation WHERE " +
        "ancestor_entity_id = :parentEntityId AND entity_type = :entityType", nativeQuery = true)

    List<BigInteger> findByParentPathLikePath(
        @Param("parentEntityId") Long parentEntityId,
        @Param("entityType") String entityType);

}
