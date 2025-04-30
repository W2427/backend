package com.ose.tasks.domain.model.repository.wps.simple;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wps.simple.WpsSimplified;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目 CRUD 操作接口。
 */
@Transactional
public interface WpsSimpleRepository extends PagingAndSortingWithCrudRepository<WpsSimplified, Long> {

    /**
     * 通过焊接工艺规范编号查找焊接工艺规范。
     *
     * @param orgId
     * @param projectId
     * @param no
     * @param status
     * @return
     */
    WpsSimplified findByOrgIdAndProjectIdAndNoAndStatus(
        Long orgId,
        Long projectId,
        String no,
        EntityStatus status
    );

    /**
     * 通过id查找焊接工艺规范。
     *
     * @param orgId
     * @param projectId
     * @param id
     * @param status
     * @return
     */
    WpsSimplified findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long id,
        EntityStatus status
    );

    /**
     * 焊接工艺规范列表。
     *
     * @param orgId
     * @param projectId
     * @param status
     * @param pageable
     * @return
     */
    Page<WpsSimplified> findByOrgIdAndProjectIdAndStatusOrderByNo(
        Long orgId,
        Long projectId,
        EntityStatus status,
        Pageable pageable
    );

    Page<WpsSimplified> findByOrgIdAndProjectIdAndNoLikeAndStatusOrderByNo(
        Long orgId,
        Long projectId,
        String no,
        EntityStatus status,
        Pageable pageable
    );

    /**
     * 获取焊口下所有Wps列表。
     *
     * @param orgId
     * @param projectId
     * @param ids
     * @param status
     * @return
     */
   List<WpsSimplified> findByOrgIdAndProjectIdAndIdInAndStatus(
        Long orgId,
        Long projectId,
        List<Long> ids,
        EntityStatus status
    );
}
