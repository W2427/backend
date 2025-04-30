package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.taskpackage.TaskPackageCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 任务包类型数据仓库。
 */
public interface TaskPackageCategoryRepository extends PagingAndSortingWithCrudRepository<TaskPackageCategory, Long>, TaskPackageCategoryCustomRepository {

    /**
     * 根据项目 ID 和名称取得任务包类型。
     *
     * @param projectId 项目 ID
     * @param name      类型名称
     * @return 工作类型信息
     */
    Optional<TaskPackageCategory> findByProjectIdAndNameAndDeletedIsFalse(Long projectId, String name);

    /**
     * 根据项目 ID 和 ID 取得任务包类型。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param id        任务包类型 ID
     * @return 任务包类型信息
     */
    Optional<TaskPackageCategory> findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long id);

    Page<TaskPackageCategory> findByProjectIdAndDeletedIsFalse(Long projectId, Pageable pageable);

    Page<TaskPackageCategory> findByProjectIdAndEntityTypeAndDeletedIsFalse(Long projectId, String entityType, Pageable pageable);

    List<TaskPackageCategory> findByIdIn(Collection<Long> categoryIDs);

    @Query("SELECT c FROM TaskPackageCategory c WHERE c.projectId = :projectId AND (c.name LIKE :name OR c.description LIKE :name) AND c.deleted = false")
    Page<TaskPackageCategory> findByProjectIdAndNameLikeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("name") String name,
        Pageable pageable
    );

    @Query("SELECT c FROM TaskPackageCategory c WHERE c.projectId = :projectId AND c.entityType = :entityType AND (c.name LIKE :name OR c.description LIKE :name) AND c.deleted = false")
    Page<TaskPackageCategory> findByProjectIdAndEntityTypeAndNameLikeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("name") String name,
        Pageable pageable
    );

    @Query("SELECT c FROM TaskPackageCategory c WHERE c.projectId = :projectId AND c.entityType = :entityType AND c.discipline = :discipline AND (c.name LIKE :name OR c.description LIKE :name) AND c.deleted = false")
    Page<TaskPackageCategory> findByProjectIdAndEntityTypeAAndDisciplineAndNameLikeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("discipline") String discipline,
        @Param("name") String name,
        Pageable pageable
    );

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(
        value = "SELECT DISTINCT"
            + "   wpe.entity_id"
            + " FROM"
            + "   task_package_category AS wpc"
            + "   INNER JOIN task_package AS wp"
            + "     ON wp.category_id = wpc.id"
            + "     AND wp.deleted = 0"
            + "   INNER JOIN task_package_entity_relation AS wpe"
            + "     ON wpe.task_package_id = wp.id"
            + " WHERE"
            + "   wpc.project_id = :projectId"
            + "   AND wpc.id = :categoryId",
        nativeQuery = true
    )
    Set<Long> findEntityIdByProjectIdAndId(
        @Param("projectId") Long projectId,
        @Param("categoryId") Long categoryId
    );

}
