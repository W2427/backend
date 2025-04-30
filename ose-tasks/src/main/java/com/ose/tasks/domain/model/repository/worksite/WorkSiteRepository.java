package com.ose.tasks.domain.model.repository.worksite;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.worksite.WorkSite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 工作场地 CRUD 操作接口。
 */
public interface WorkSiteRepository extends PagingAndSortingWithCrudRepository<WorkSite, Long> {

    /**
     * 查询工作场地。
     *
     * @param companyId 公司 ID
     * @param depth     层级深度
     * @param pageable  分页参数
     * @return 工作场地分页数据
     */
    Page<WorkSite> findByCompanyIdAndDepthAndDeletedIsFalse(Long companyId, Integer depth, Pageable pageable);

    /**
     * 查询工作场地。
     *
     * @param companyId 公司 ID
     * @param parentId  上级 ID
     * @param pageable  分页参数
     * @return 工作场地分页数据
     */
    Page<WorkSite> findByCompanyIdAndParentIdAndDeletedIsFalse(Long companyId, Long parentId, Pageable pageable);

    /**
     * 查询工作场地。
     *
     * @param companyId 公司 ID
     * @param projectId 项目 ID
     * @param depth     层级深度
     * @param pageable  分页参数
     * @return 工作场地分页数据
     */
    @Query("SELECT ws FROM WorkSite ws WHERE ws.companyId = :companyId AND (ws.projectId IS NULL OR ws.projectId = :projectId) AND ws.depth = :depth AND ws.deleted = false")
    Page<WorkSite> findByCompanyIdAndProjectIdAndDepthAndDeletedIsFalse(
        @Param("companyId") Long companyId,
        @Param("projectId") Long projectId,
        @Param("depth") Integer depth,
        Pageable pageable
    );

    /**
     * 查询工作场地。
     *
     * @param companyId 公司 ID
     * @param projectId 项目 ID
     * @param workSiteId     场地Id
     * @return 工作场地分页数据
     */
    @Query("SELECT ws FROM WorkSite ws WHERE ws.id = :workSiteId AND ws.companyId = :companyId AND (ws.projectId IS NULL OR ws.projectId = :projectId) AND ws.deleted = FALSE")
    WorkSite findByIdAndCompanyIdAndProjectIdAndDeletedIsFalse(
        @Param("companyId") Long companyId,
        @Param("projectId") Long projectId,
        @Param("workSiteId") Long workSiteId
    );

    /**
     * 查询工作场地。
     *
     * @param companyId 公司 ID
     * @param projectId 项目 ID
     * @param parentId  上级 ID
     * @param pageable  分页参数
     * @return 工作场地分页数据
     */
    @Query("SELECT ws FROM WorkSite ws WHERE ws.companyId = :companyId AND (ws.projectId IS NULL OR ws.projectId = :projectId) AND ws.parentId = :parentId AND ws.deleted = false")
    Page<WorkSite> findByCompanyIdAndProjectIdAndParentIdAndDeletedIsFalse(
        @Param("companyId") Long companyId,
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId,
        Pageable pageable
    );

    /**
     * 取得工作场地信息。
     *
     * @param companyId  公司 ID
     * @param workSiteId 工作场地 ID
     * @return 工作场地信息
     */
    Optional<WorkSite> findByCompanyIdAndIdAndDeletedIsFalse(Long companyId, Long workSiteId);

    /**
     * 取得工作场地信息。
     *
     * @param companyId  公司 ID
     * @param projectId  项目 ID
     * @param workSiteId 工作场地 ID
     * @return 工作场地信息
     */
    Optional<WorkSite> findByCompanyIdAndProjectIdAndIdAndDeletedIsFalse(Long companyId, Long projectId, Long workSiteId);

    /**
     * 取得工作场地信息。
     *
     * @param companyId 项目 ID
     * @param parentId  上级 ID
     * @param name      工作场地名称
     * @param id        工作场地 ID
     * @return 工作场地信息
     */
    boolean existsByCompanyIdAndParentIdAndProjectIdAndNameAndIdNotAndDeletedIsFalse(Long companyId, Long parentId, Long projectId, String name, Long id);

    /**
     * 取得指定上级下的最后一个工作场地。
     *
     * @param companyId 公司 ID
     * @param parentId  上级 ID
     * @return 工作场地信息
     */
    Optional<WorkSite> findFirstByCompanyIdAndParentIdAndDeletedIsFalseOrderBySortDesc(Long companyId, Long parentId);

    /**
     * 递增排序权重。
     *
     * @param companyId 公司 ID
     * @param parentId  上级 ID
     * @param from      起始排序权重
     */
    @Transactional
    @Modifying
    @Query("UPDATE WorkSite ws SET ws.sort = ws.sort + 1 WHERE ws.companyId = :companyId AND ((:parentId IS NULL AND ws.parentId IS NULL) OR ws.parentId = :parentId) AND ws.sort >= :from")
    void increaseSortScore(@Param("companyId") Long companyId, @Param("parentId") Long parentId, @Param("from") Integer from);

}
