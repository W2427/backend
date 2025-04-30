package com.ose.tasks.domain.model.repository.wps.simple;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wps.simple.WelderGradeSimplified;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目 CRUD 操作接口。
 */
@Transactional
public interface WelderGradeSimpleRepository extends PagingAndSortingWithCrudRepository<WelderGradeSimplified, Long> {

    /**
     * 通过焊工证编号查找焊工证。
     *
     * @param orgId
     * @param projectId
     * @param no
     * @param status
     * @return
     */
    WelderGradeSimplified findByOrgIdAndProjectIdAndNoAndStatus(
        Long orgId,
        Long projectId,
        String no,
        EntityStatus status
    );

    /**
     * 通过焊工整id查找焊工证信息。
     *
     * @param orgId
     * @param projectId
     * @param id
     * @param status
     * @return
     */
    WelderGradeSimplified findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long id,
        EntityStatus status
    );

    /**
     * 通过焊工整id查找焊工证信息。
     *
     * @param orgId
     * @param projectId
     * @param ids
     * @param status
     * @return
     */
    List<WelderGradeSimplified> findByOrgIdAndProjectIdAndIdInAndStatus(
        Long orgId,
        Long projectId,
        List<Long> ids,
        EntityStatus status
    );

    /**
     * 焊工证列表。
     *
     * @param orgId
     * @param projectId
     * @param status
     * @param pageable
     * @return
     */
    Page<WelderGradeSimplified> findByOrgIdAndProjectIdAndStatus(
        Long orgId,
        Long projectId,
        EntityStatus status,
        Pageable pageable
    );

    List<WelderGradeSimplified> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status);
}
