package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.Subcon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubconRepository extends PagingAndSortingWithCrudRepository<Subcon, Long> {

    /**
     * 获取分包商列表。
     *
     * @return 分包商列表
     */
    List<Subcon> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId);

    /**
     * 获取分包商分页列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageable  分页参数
     * @return 分包商列表
     */
    Page<Subcon> findByOrgIdAndProjectIdAndDeletedIsFalseOrderByCreatedAtDesc(Long orgId, Long projectId, Pageable pageable);


    /**
     * 获取分包商详情。
     *
     * @param subconId 分包商ID
     * @return 分包商详情
     */
    Subcon findByIdAndDeletedIsFalse(Long subconId);

    /**
     * 获取分包商列表。
     *
     * @param subcons 分包商ID列表
     * @return 分包商列表
     */
    List<Subcon> findByIdInAndDeletedIsFalse(List<Long> subcons);


    List<Subcon> findByOrgIdAndProjectIdAndDeletedIsFalseAndNameIsLike(Long orgId, Long projectId, String subConNo);
}
