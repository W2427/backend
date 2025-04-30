package com.ose.tasks.domain.model.repository.wps.simple;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wps.simple.WelderSimplified;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目 CRUD 操作接口。
 */
@Transactional
public interface WelderSimpleRepository extends WelderSimpleRepositoryCustom, PagingAndSortingWithCrudRepository<WelderSimplified, Long> {

    /**
     * 通过焊工编号查找焊工。
     *
     * @param orgId
     * @param projectId
     * @param no
     * @param status
     * @return
     */
    WelderSimplified findByOrgIdAndProjectIdAndNoAndStatus(
        Long orgId,
        Long projectId,
        String no,
        EntityStatus status
    );

    /**
     * 通过用户id查找焊工证信息。
     *
     * @param orgId
     * @param projectId
     * @param userId
     * @param status
     * @return
     */
    WelderSimplified findByOrgIdAndProjectIdAndUserIdAndStatus(
        Long orgId,
        Long projectId,
        Long userId,
        EntityStatus status
    );

    /**
     * 通过焊工id查找焊工信息。
     *
     * @param orgId
     * @param projectId
     * @param id
     * @param status
     * @return
     */
    WelderSimplified findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long id,
        EntityStatus status
    );

    /**
     * 焊工列表。
     *
     * @param orgId
     * @param projectId
     * @param status
     * @param pageable
     * @return
     */
    Page<WelderSimplified> findByOrgIdAndProjectIdAndStatus(
        Long orgId,
        Long projectId,
        EntityStatus status,
        Pageable pageable
    );

}
