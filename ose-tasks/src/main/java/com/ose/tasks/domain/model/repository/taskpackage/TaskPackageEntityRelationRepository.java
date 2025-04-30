package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 任务包-实体数据仓库。
 */
@Transactional
public interface TaskPackageEntityRelationRepository extends PagingAndSortingWithCrudRepository<TaskPackageEntityRelation, Long>, TaskPackageEntityRelationCustomRepository {

    Page<TaskPackageEntityRelation> findByOrgIdAndProjectIdAndTaskPackageId(Long orgId, Long projectId, Long taskPackageId, Pageable pageable);

    Page<TaskPackageEntityRelation> findByOrgIdAndProjectIdAndTaskPackageIdAndIsUsedInPipe(Long orgId, Long projectId, Long taskPackageId, Boolean isUsedInPipe, Pageable pageable);

    List<TaskPackageEntityRelation> findByOrgIdAndProjectIdAndEntityId(Long orgId, Long projectId, Long entityId);

    List<TaskPackageEntityRelation> findByOrgIdAndProjectIdAndEntityIdAndTaskPackageIdAndStatus(Long orgId, Long projectId, Long entityId, Long taskPackageId, EntityStatus entityStatus);

    List<TaskPackageEntityRelation> findByOrgIdAndProjectIdAndEntityNoAndStatus(Long orgId, Long projectId, String entityNo, EntityStatus entityStatus);

    boolean existsByOrgIdAndProjectIdAndTaskPackageIdAndStatus(Long orgId, Long projectId, Long taskPackageId, EntityStatus entityStatus);

    @Modifying
    @Transactional
    @Query("DELETE FROM TaskPackageEntityRelation wpe WHERE wpe.projectId = :projectId AND wpe.taskPackageId = :taskPackageId AND wpe.entityId IN :entityIDs")
    int deleteByProjectIdAndTaskPackageIdAndEntityIdIn(
        @Param("projectId") Long projectId,
        @Param("taskPackageId") Long taskPackageId,
        @Param("entityIDs") Collection<Long> entityIDs
    );

    @Query(
        "SELECT wpe"
            + " FROM TaskPackageEntityRelation wpe"
            + "   INNER JOIN com.ose.tasks.entity.taskpackage.TaskPackage wp"
            + "      ON wp.id = wpe.taskPackageId"
            + " WHERE wpe.projectId = :projectId"
            + "   AND wp.category.id = :categoryId"
            + "   AND wp.deleted = false"
    )
    List<TaskPackageEntityRelation> findByProjectIdAndCategoryId(
        @Param("projectId") Long projectId,
        @Param("categoryId") Long categoryId
    );

    @Query(
        "SELECT a"
            + " FROM TaskPackageEntityRelation a"
            + " LEFT JOIN com.ose.tasks.entity.taskpackage.TaskPackage b"
            + " ON a.taskPackageId = b.id"
            + " WHERE b.category.id = :categoryId"
            + " AND a.projectId = :projectId"
            + " AND a.entityId = :entityId"
    )
    TaskPackageEntityRelation findByProjectIdAndEntityIdAndTaskPackageCatogoryId(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("categoryId") Long categoryId
    );

    @Transactional
    @Modifying
    @Query(value = "UPDATE task_package_entity_relation SET status = 'DELETED' WHERE project_id = :projectId AND entity_id = :entityId",
    nativeQuery = true)
    void deleteByProjectIdAndEntityId(@Param("projectId") Long projectId,
                                      @Param("entityId") Long entityId);
}
