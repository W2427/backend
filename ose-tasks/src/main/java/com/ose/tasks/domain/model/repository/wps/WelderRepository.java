package com.ose.tasks.domain.model.repository.wps;

import com.ose.tasks.entity.wps.Welder;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WelderRepository extends PagingAndSortingRepository<Welder, Long>, WelderRepositoryCustom {

    /**
     * 获取焊工详情。
     *
     * @param welderId 焊工ID
     * @return 焊工详情
     */
    Welder findByIdAndDeletedIsFalse(Long welderId);

    /**
     * 根据编号获取焊工信息。
     *
     * @param no 编号
     * @return 焊工信息
     */
    Integer countByNoAndDeletedIsFalse(String no);

    /**
     * 获取焊工列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 焊工列表
     */
    List<Welder> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId);

    /**
     * 获取焊工。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 焊工列表
     */
    List<Welder> findByOrgIdAndProjectIdAndUserIdAndDeletedIsFalse(Long orgId, Long projectId, Long userId);

    /**
     * 根据焊工ID列表获取焊工列表。
     *
     * @param welderIds 焊工ID
     * @return 焊工列表
     */
    List<Welder> findByIdInAndDeletedIsFalse(List<Long> welderIds);

    Welder findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(Long orgId, Long projectId, String welderNo);
}
