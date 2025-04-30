package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.Experience;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExperienceRepository extends PagingAndSortingWithCrudRepository<Experience, Long>, IssueCustomRepository {

    /**
     * 获取问题详情。
     *
     * @param experienceId 问题ID
     * @return 问题详情
     */
    Experience findByIdAndDeletedIsFalse(Long experienceId);

    List<Experience> findByOrgIdAndProjectIdAndParentIdAndDeletedIsFalseOrderByCreatedAtDesc(Long orgId, Long projectId, Long experienceParentId);

    /**
     * 更新currentExperience为false
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param parentId
     */
    @Transactional
    @Query("UPDATE Experience e SET e.currentExperience = false WHERE e.orgId = :orgId AND e.projectId = :projectId AND e.deleted = false AND e.parentId = :parentId")
    @Modifying
    void updateCurrentStatus(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId
    );

    /**
     * 获取当前最大流水号
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    @Query("select max(seriesNo) from Experience where orgId = :orgId and projectId = :projectId and type = 'EXPERIENCE'")
    Integer findMaxSeriesNo(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    /**
     * 获取父级编号
     *
     * @param parentId
     * @return
     */
    @Query("SELECT no FROM Experience WHERE id = :id")
    String findParentNo(@Param("id") Long parentId);

    /**
     * 获取父级流水号
     *
     * @param parentId
     * @return
     */
    @Query("SELECT seriesNo FROM Experience WHERE id = :id")
    Integer findParentSeriesNo(@Param("id") Long parentId);
}
