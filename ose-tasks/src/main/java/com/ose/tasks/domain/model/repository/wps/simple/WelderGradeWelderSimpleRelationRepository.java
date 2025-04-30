package com.ose.tasks.domain.model.repository.wps.simple;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wps.simple.WelderGradeWelderSimplifiedRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目 CRUD 操作接口。
 */
@Transactional
public interface WelderGradeWelderSimpleRelationRepository extends PagingAndSortingWithCrudRepository<WelderGradeWelderSimplifiedRelation, Long> {

    /**
     * 查找当前焊工的焊工证列表。
     *
     * @param orgId
     * @param projectId
     * @param welderId
     * @param status
     * @param pageable
     * @return
     */
    Page<WelderGradeWelderSimplifiedRelation> findByOrgIdAndProjectIdAndWelderIdAndStatus(
        Long orgId,
        Long projectId,
        Long welderId,
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
    WelderGradeWelderSimplifiedRelation findByOrgIdAndProjectIdAndIdAndStatus(
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
     * @param welderId
     * @param welderGradeId
     * @param status
     * @return
     */
    WelderGradeWelderSimplifiedRelation findByOrgIdAndProjectIdAndWelderIdAndWelderGradeIdAndStatus(
        Long orgId,
        Long projectId,
        Long welderId,
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
    List<WelderGradeWelderSimplifiedRelation> findByOrgIdAndProjectIdAndWelderGradeNoAndStatus(
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
     * @param welderIds
     * @param status
     * @return
     */
    List<WelderGradeWelderSimplifiedRelation> findByOrgIdAndProjectIdAndWelderIdInAndStatus(
        Long orgId,
        Long projectId,
        List<Long> welderIds,
        EntityStatus status
    );
}
