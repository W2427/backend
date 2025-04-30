package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectInfo;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 项目 CRUD 操作接口。
 */
public interface ProjectRepository extends PagingAndSortingWithCrudRepository<Project, Long> {

    /**
     * 根据 ID 及组织 ID 取得未被删除的项目信息。
     *
     * @param id    项目 ID
     * @param orgId 组织 ID
     * @return 项目信息
     */
    Optional<Project> findByIdAndOrgIdAndDeletedIsFalse(Long id, Long orgId);

    /**
     * 根据 ID 及组织 ID 检查项目是否存在。
     *
     * @param id    项目 ID
     * @param orgId 组织 ID
     * @return 项目是否存在
     */
    boolean existsByIdAndOrgIdAndDeletedIsFalse(Long id, Long orgId);

    /**
     * 根据所属组织查询项目。
     *
     * @param orgId    所属组织 ID
     * @param pageable 分页参数
     * @return 项目分页参数
     */
    Page<Project> findByOrgIdAndDeletedIsFalse(Long orgId, Pageable pageable);

    List<Project> findByOrgIdAndStatus(Long orgId, EntityStatus entityStatus);


    @Query(
        value = "SELECT " +
            "  *  " +
            "FROM " +
            "  `project` " +
            "WHERE " +
            "  `status` = 'ACTIVE' " +
            "  AND org_id = :orgId " +
            "  AND (have_hour IS NULL OR have_hour IS TRUE)",
        nativeQuery = true
    )
    List<Project> findByOrgIdAndStatusAndHaveHour(@Param("orgId") Long orgId);

    /**
     * 取得所有尚未完成的项目信息。
     *
     * @return 项目信息列表
     */
    @Query("SELECT p FROM Project p WHERE p.finishedAt IS NULL AND p.deleted = FALSE")
    List<Project> findByFinishedAtIsNullAndDeletedIsFalse();

    Optional<Project> findAllByIdAndDeletedIsFalse(Long id);

    @Query(
        value = "SELECT  " +
            "p.name as name, " +
            "(case when ISNULL(c.day_work_hour) then 8 else c.day_work_hour end) as dayWorkHour " +
            "FROM saint_whale_tasks.project as p " +
            "LEFT JOIN saint_whale_auth.calendar as c " +
            "ON p.id = c.project_id " +
            "WHERE p.deleted is false " +
            "ORDER BY p.name",

        nativeQuery = true
    )
    List<Map<String, Object>> archiveProjectData();

    Project findByNameAndDeletedIsFalse(String name);

    @Query("SELECT pi FROM ProjectInfo pi WHERE pi.projectId = :projectId AND pi.infoKey = :key AND pi.deleted = false")
    ProjectInfo findByProjectIdAndKey(@Param("projectId") Long projectId, @Param("key") String key);

}
