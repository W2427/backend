package com.ose.report.domain.repository;

import com.ose.report.entity.Checklist;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检查单 CRUD 操作接口。
 */
@Transactional
public interface ChecklistRepository extends PagingAndSortingWithCrudRepository<Checklist, Long> {

    /**
     * 根据条件检索查询检查单
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param condition 检索条件
     * @param pageable  分页信息
     * @return 检查单列表
     */
    @Query(value = "SELECT ckl FROM Checklist ckl WHERE ckl.orgId = :orgId AND ckl.projectId = :projectId AND (ckl.serial LIKE %:condition% OR ckl.name LIKE %:condition%)",
        countQuery = "SELECT COUNT(ckl) FROM Checklist ckl WHERE ckl.orgId = :orgId AND ckl.projectId = :projectId AND (ckl.serial LIKE %:condition% OR ckl.name LIKE %:condition%)")
    Page<Checklist> findChecklistWithOrgIdAndProjectIdAndCondition(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("condition") String condition,
        Pageable pageable);

    /**
     * 查询检查单
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageable  分页信息
     * @return 检查单列表
     */
    Page<Checklist> findAllByOrgIdAndProjectId(Long orgId, Long projectId, Pageable pageable);

    /**
     * 查询检查单（根据编号）
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param serial    检查单编号
     * @return 检查单
     */
    Checklist findByOrgIdAndProjectIdAndSerial(Long orgId, Long projectId, String serial);
}
