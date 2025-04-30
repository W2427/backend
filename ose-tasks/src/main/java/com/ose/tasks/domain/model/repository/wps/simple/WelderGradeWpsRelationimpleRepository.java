package com.ose.tasks.domain.model.repository.wps.simple;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wps.simple.WelderGradeWpsSimplifiedRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目 CRUD 操作接口。
 */
@Transactional
public interface WelderGradeWpsRelationimpleRepository extends PagingAndSortingWithCrudRepository<WelderGradeWpsSimplifiedRelation, Long> {
    /**
     * 查找当前焊工的焊工证列表。
     *
     * @param orgId
     * @param projectId
     * @param wpsId
     * @param status
     * @param pageable
     * @return
     */
    Page<WelderGradeWpsSimplifiedRelation> findByOrgIdAndProjectIdAndWpsIdAndStatus(
        Long orgId,
        Long projectId,
        Long wpsId,
        EntityStatus status,
        Pageable pageable
    );

    /**
     * 查找当前焊工的焊工证列表。
     *
     * @param orgId
     * @param projectId
     * @param gradeId
     * @param status
     * @param pageable
     * @return
     */
    Page<WelderGradeWpsSimplifiedRelation> findByOrgIdAndProjectIdAndWelderGradeIdAndStatusOrderByWpsNo(
        Long orgId,
        Long projectId,
        Long gradeId,
        EntityStatus status,
        Pageable pageable
    );

    /**
     * 查找焊工证焊工关系详情信息。
     *
     * @param orgId
     * @param projectId
     * @param id
     * @param status
     * @return
     */
    WelderGradeWpsSimplifiedRelation findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long id,
        EntityStatus status
    );

    /**
     * 当前焊工和焊工证对应关系是否再存。
     *
     * @param orgId
     * @param projectId
     * @param wpsId
     * @param welderGradeId
     * @param status
     * @return
     */
    WelderGradeWpsSimplifiedRelation findByOrgIdAndProjectIdAndWpsIdAndWelderGradeIdAndStatus(
        Long orgId,
        Long projectId,
        Long wpsId,
        Long welderGradeId,
        EntityStatus status
    );

    /**
     * 查找焊工编号是否存在与关系表中。
     *
     * @param orgId
     * @param projectId
     * @param welderGradeNo
     * @param status
     * @return
     */
    List<WelderGradeWpsSimplifiedRelation> findByOrgIdAndProjectIdAndWelderGradeNoAndStatus(
        Long orgId,
        Long projectId,
        String welderGradeNo,
        EntityStatus status
    );

    /**
     * 查找焊工编号是否存在与关系表中。
     *
     * @param orgId
     * @param projectId
     * @param wpsIds
     * @param status
     * @return
     */
    List<WelderGradeWpsSimplifiedRelation> findByOrgIdAndProjectIdAndWpsIdInAndStatus(
        Long orgId,
        Long projectId,
        List<Long> wpsIds,
        EntityStatus status
    );

    List<WelderGradeWpsSimplifiedRelation> findByOrgIdAndProjectIdAndWelderGradeIdInAndStatus(
        Long orgId,
        Long projectId,
        List<Long> gradeIds,
        EntityStatus status
    );

}

