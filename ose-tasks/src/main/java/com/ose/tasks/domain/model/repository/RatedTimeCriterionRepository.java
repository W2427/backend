package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.RatedTimeCriterion;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RatedTimeCriterionRepository
    extends RatedTimeCriterionRepositoryCustom, PagingAndSortingWithCrudRepository<RatedTimeCriterion, Long> {

    /**
     * 获取定额工时查询条件详情。
     *
     * @param ratedTimeCriterionId 条件ID
     * @return 条件详情
     */
    RatedTimeCriterion findByIdAndDeletedIsFalse(Long ratedTimeCriterionId);

    /**
     * 获取全部定额工时查询条件信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 定额工时查询条件列表
     */
    List<RatedTimeCriterion> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId);

    /**
     * 获取定额工时查询条件详情。
     *
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param processId        条件ID
     * @param processStageId   条件ID
     * @param entitySubTypeId 条件ID
     * @return 条件详情
     */
    RatedTimeCriterion findByOrgIdAndProjectIdAndProcessIdAndProcessStageIdAndEntitySubTypeIdAndDeletedIsFalse
    (
        Long orgId,
        Long projectId,
        Long processId,
        Long processStageId,
        Long entitySubTypeId
    );
}
